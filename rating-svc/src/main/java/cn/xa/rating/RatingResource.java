package cn.xa.rating;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.common.tcc.TccCoordinatorClient;
import cn.xa.common.tcc.TccState;
import cn.xa.task.TaskDto;
import cn.xa.tracking.TrackingDto;
import com.atomikos.tcc.rest.ParticipantLink;
import com.atomikos.tcc.rest.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/client/rating")
@RequiredArgsConstructor
public class RatingResource {

    private final RestTemplate restTemplate;
    private final TccCoordinatorClient tccCoordinatorClient;

    @PostMapping("/create")
    public ResponseEntity createRating(@RequestBody CreateRatingRequest createRatingRequest) {
        String txId = createRatingRequest.getTxId();
        long expireTime = System.currentTimeMillis() + TccConfig.TRANSACTION_TIMEOUT_MS;

        List<ParticipantLink> participantLinks = new ArrayList<>(7);

        Transaction transaction = new Transaction(participantLinks);
        try {

            // 1. try
            // create rating
            String ratingServiceUrl = String.format(TccConfig.RATING_TCC_URL, txId);
            participantLinks.add(new ParticipantLink(ratingServiceUrl, expireTime));
            RatingDto ratingDto = RatingDto.builder()
                    .txId(txId)
                    .state(TccState.TRY)
                    .title(createRatingRequest.getTitle())
                    .reference(createRatingRequest.getReference())
                    .build();
            ratingDto = restTemplate.postForObject(ratingServiceUrl, ratingDto, RatingDto.class);


            // create collaboration for rating
            String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, ratingDto.getId(), ObjectClassId.RATING);
            participantLinks.add(new ParticipantLink(collaborationServiceUrl, expireTime));
            CollaborationDto collaborationDto = CollaborationDto.builder()
                    .parentObjectId(100L)
                    .parentObjectClassId(ObjectClassId.PROJECT)
                    .objectId(ratingDto.getId())
                    .objectClassId(ObjectClassId.RATING)
                    .build();
            restTemplate.postForEntity(collaborationServiceUrl, collaborationDto, String.class);


            // create tracking for rating
            String trackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId, ratingDto.getId(), ObjectClassId.RATING);
            participantLinks.add(new ParticipantLink(trackingServiceUrl, expireTime));
            TrackingDto trackingDto = TrackingDto.builder()
                    .title("Create Rating " + ratingDto.getTitle())
                    .detail(ratingDto.toString())
                    .objectId(ratingDto.getId())
                    .objectClassId(ObjectClassId.RATING)
                    .build();
            trackingDto = restTemplate.postForObject(trackingServiceUrl, trackingDto, TrackingDto.class);

            // create collaboration for rating's tracking
            String trackingCollaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, trackingDto.getId(), ObjectClassId.TRACKING);
            participantLinks.add(new ParticipantLink(trackingCollaborationServiceUrl, expireTime));
            CollaborationDto trackingCollaborationDto = CollaborationDto.builder()
                    .parentObjectId(100L)
                    .parentObjectClassId(ObjectClassId.PROJECT)
                    .objectId(trackingDto.getId())
                    .objectClassId(ObjectClassId.TRACKING)
                    .build();
            restTemplate.postForEntity(trackingCollaborationServiceUrl, trackingCollaborationDto, String.class);

            // create task for rating
            String taskServiceUrl = String.format(TccConfig.TASK_TCC_URL, txId, ratingDto.getId(), ObjectClassId.RATING);
            participantLinks.add(new ParticipantLink(taskServiceUrl, expireTime));
            TaskDto taskDto = TaskDto.builder()
                    .title("Review rating " + ratingDto.getTitle())
                    .objectId(ratingDto.getId())
                    .objectClassId(ObjectClassId.RATING)
                    .build();
            taskDto = restTemplate.postForObject(taskServiceUrl, taskDto, TaskDto.class);

            // create collaboraiton for task
            String taskCollaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, taskDto.getId(), ObjectClassId.TASK);
            participantLinks.add(new ParticipantLink(taskCollaborationServiceUrl, expireTime));
            CollaborationDto taskCollaborationDto = CollaborationDto.builder()
                    .parentObjectId(100L)
                    .parentObjectClassId(ObjectClassId.PROJECT)
                    .objectId(taskDto.getId())
                    .objectClassId(ObjectClassId.TASK)
                    .build();
            restTemplate.postForEntity(taskCollaborationServiceUrl, taskCollaborationDto, String.class);

            // create tracking for task
            String taskTrackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId, taskDto.getId(), ObjectClassId.TASK);
            participantLinks.add(new ParticipantLink(taskTrackingServiceUrl, expireTime));
            TrackingDto taskTrackingDto = TrackingDto.builder()
                    .title("Create Task " + ratingDto.getTitle())
                    .detail(ratingDto.toString())
                    .objectId(taskDto.getId())
                    .objectClassId(ObjectClassId.TASK)
                    .build();
            taskTrackingDto = restTemplate.postForObject(taskTrackingServiceUrl, taskTrackingDto, TrackingDto.class);

            // create collaboraiton for task's tracking
            String taskTrackingCollaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, taskTrackingDto.getId(), ObjectClassId.TRACKING);
            participantLinks.add(new ParticipantLink(taskTrackingCollaborationServiceUrl, expireTime));
            CollaborationDto taskTrackingCollaborationDto = CollaborationDto.builder()
                    .parentObjectId(100L)
                    .parentObjectClassId(ObjectClassId.PROJECT)
                    .objectId(taskTrackingDto.getId())
                    .objectClassId(ObjectClassId.TRACKING)
                    .build();
            restTemplate.postForEntity(taskTrackingCollaborationServiceUrl, taskTrackingCollaborationDto, String.class);


            //2. call coordinator to confirm
            tccCoordinatorClient.confirm(transaction);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            //3. call coordinator to cancel
            tccCoordinatorClient.cancel(transaction);
            String msg = e instanceof HttpStatusCodeException ? ((HttpStatusCodeException) e).getResponseBodyAsString() : e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }


}
