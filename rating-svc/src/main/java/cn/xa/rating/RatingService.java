package cn.xa.rating;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.CollaborationType;
import cn.xa.common.exception.ServiceException;
import cn.xa.task.TaskClient;
import cn.xa.task.TaskDto;
import cn.xa.tracking.TrackingClient;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Transactional
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final TaskClient taskClient;
    private final TrackingClient trackingClient;
    private final CollaborationClient collaborationClient;

    public RatingDto create(RatingDto ratingDto1) {

        if (StringUtils.hasText(ratingDto1.getReference())) {
            Rating rating = ratingRepository.findByReference(ratingDto1.getReference());
            if (rating != null) {
                throw new ServiceException("Rfe reference is exist, please input a new one");
            }
        }

        Rating rating = ratingMapper.toEntity(ratingDto1);
        ratingRepository.save(rating);

        collaborationClient.createCollaboration(CollaborationDto.builder()
                .parentId(100L)
                .childId(rating.getId())
                .type(CollaborationType.RATING)
                .build());

        trackingClient.createTracking(TrackingDto.builder()
                .title("create Rating " + rating.getTitle())
                .detail(rating.toString())
                .build());

        taskClient.createTask(TaskDto.builder()
                .title("review rating")
                .build());


        return ratingMapper.toDto(rating);
    }

}
