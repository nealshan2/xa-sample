package cn.xa.rating;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.common.tcc.TccState;
import cn.xa.task.TaskDto;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.context.annotations.SagaStart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/client/rating")
@RequiredArgsConstructor
public class RatingResource {

    private final RestTemplate restTemplate;

    @SagaStart(timeout = 2)
    @PostMapping("/create")
    public ResponseEntity createRating(@RequestBody CreateRatingRequest createRatingRequest) {
        String txId = createRatingRequest.getTxId();

        // create rating
        String ratingServiceUrl = String.format(TccConfig.RATING_TCC_URL, txId);
        RatingDto ratingDto = RatingDto.builder()
                .txId(txId)
                .state(TccState.TRY)
                .title(createRatingRequest.getTitle())
                .reference(createRatingRequest.getReference())
                .build();
        ratingDto = restTemplate.postForObject(ratingServiceUrl, ratingDto, RatingDto.class);


        // create collaboration for rating
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, ratingDto.getId(), ObjectClassId.RATING);
        CollaborationDto collaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(ratingDto.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        restTemplate.postForEntity(collaborationServiceUrl, collaborationDto, String.class);


        // create tracking for rating
        String trackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId, ratingDto.getId(), ObjectClassId.RATING);
        TrackingDto trackingDto = TrackingDto.builder()
                .title("Create Rating " + ratingDto.getTitle())
                .detail(ratingDto.toString())
                .objectId(ratingDto.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        trackingDto = restTemplate.postForObject(trackingServiceUrl, trackingDto, TrackingDto.class);

        // create collaboration for rating's tracking
        String trackingCollaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, trackingDto.getId(), ObjectClassId.TRACKING);
        CollaborationDto trackingCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(trackingDto.getId())
                .objectClassId(ObjectClassId.TRACKING)
                .build();
        restTemplate.postForEntity(trackingCollaborationServiceUrl, trackingCollaborationDto, String.class);

        // create task for rating
        String taskServiceUrl = String.format(TccConfig.TASK_TCC_URL, txId, ratingDto.getId(), ObjectClassId.RATING);
        TaskDto taskDto = TaskDto.builder()
                .title("Review rating " + ratingDto.getTitle())
                .objectId(ratingDto.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        taskDto = restTemplate.postForObject(taskServiceUrl, taskDto, TaskDto.class);

        // create collaboraiton for task
        String taskCollaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, taskDto.getId(), ObjectClassId.TASK);
        CollaborationDto taskCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(taskDto.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        restTemplate.postForEntity(taskCollaborationServiceUrl, taskCollaborationDto, String.class);

        // create tracking for task
        String taskTrackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId, taskDto.getId(), ObjectClassId.TASK);
        TrackingDto taskTrackingDto = TrackingDto.builder()
                .title("Create Task " + ratingDto.getTitle())
                .detail(ratingDto.toString())
                .objectId(taskDto.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        taskTrackingDto = restTemplate.postForObject(taskTrackingServiceUrl, taskTrackingDto, TrackingDto.class);

        // create collaboraiton for task's tracking
        String taskTrackingCollaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId, 100L, ObjectClassId.PROJECT, taskTrackingDto.getId(), ObjectClassId.TRACKING);
        CollaborationDto taskTrackingCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(taskTrackingDto.getId())
                .objectClassId(ObjectClassId.TRACKING)
                .build();
        restTemplate.postForEntity(taskTrackingCollaborationServiceUrl, taskTrackingCollaborationDto, String.class);

        return ResponseEntity.ok().body(ratingDto);

    }


}
