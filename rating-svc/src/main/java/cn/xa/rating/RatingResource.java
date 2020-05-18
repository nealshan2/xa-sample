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

        // create rating
        RatingDto ratingDto = RatingDto.builder()
                .title(createRatingRequest.getTitle())
                .reference(createRatingRequest.getReference())
                .build();
        ratingDto = restTemplate.postForObject(TccConfig.RATING_TCC_URL, ratingDto, RatingDto.class);


        // create collaboration for rating
        CollaborationDto collaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(ratingDto.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, collaborationDto, String.class);


        // create tracking for rating
        TrackingDto trackingDto = TrackingDto.builder()
                .title("Create Rating " + ratingDto.getTitle())
                .detail(ratingDto.toString())
                .objectId(ratingDto.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        trackingDto = restTemplate.postForObject(TccConfig.TRACKING_TCC_URL, trackingDto, TrackingDto.class);

        // create collaboration for rating's tracking
        CollaborationDto trackingCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(trackingDto.getId())
                .objectClassId(ObjectClassId.TRACKING)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, trackingCollaborationDto, String.class);

        // create task for rating
        TaskDto taskDto = TaskDto.builder()
                .title("Review rating " + ratingDto.getTitle())
                .objectId(ratingDto.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        taskDto = restTemplate.postForObject(TccConfig.TASK_TCC_URL, taskDto, TaskDto.class);

        // create collaboraiton for task
        CollaborationDto taskCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(taskDto.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, taskCollaborationDto, String.class);

        // create tracking for task
        TrackingDto taskTrackingDto = TrackingDto.builder()
                .title("Create Task " + ratingDto.getTitle())
                .detail(ratingDto.toString())
                .objectId(taskDto.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        taskTrackingDto = restTemplate.postForObject(TccConfig.TRACKING_TCC_URL, taskTrackingDto, TrackingDto.class);

        // create collaboraiton for task's tracking
        CollaborationDto taskTrackingCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(taskTrackingDto.getId())
                .objectClassId(ObjectClassId.TRACKING)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, taskTrackingCollaborationDto, String.class);

        return ResponseEntity.ok().body(ratingDto);

    }


}
