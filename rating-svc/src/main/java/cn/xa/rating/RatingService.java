package cn.xa.rating;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.common.tcc.TccState;
import cn.xa.task.TaskClient;
import cn.xa.task.TaskDto;
import cn.xa.tracking.TrackingClient;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private final RestTemplate restTemplate;

    private Set<String> executedSet = new ConcurrentHashMap<>().newKeySet();

    private Set<String> canceledSet = new ConcurrentHashMap<>().newKeySet();

    @Compensable(timeout = 1, compensationMethod = "cancel")
    public RatingDto save(RatingDto ratingDto) {

        String uniqueCode = ratingDto.uniqueCode();
        if(executedSet.contains(uniqueCode) || canceledSet.contains(uniqueCode)){
            return findByTxId(
                    ratingDto.getTxId()
            );
        }

        ratingDto.setState(TccState.CONFIRMED);
        Rating rating = ratingMapper.toEntity(ratingDto);
        ratingRepository.save(rating);

        // create collaboration for rating
        CollaborationDto collaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(rating.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, collaborationDto, String.class);

        // create tracking for rating
        TrackingDto trackingDto = TrackingDto.builder()
                .title("Created Rating " + rating.getTitle())
                .detail(rating.toString())
                .objectId(rating.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        trackingDto = restTemplate.postForObject(TccConfig.TRACKING_TCC_URL, trackingDto, TrackingDto.class);

        // create task for rating
        TaskDto taskDto = TaskDto.builder()
                .title("Review rating " + rating.getTitle())
                .objectId(rating.getId())
                .objectClassId(ObjectClassId.RATING)
                .build();
        taskDto = restTemplate.postForObject(TccConfig.TASK_TCC_URL, taskDto, TaskDto.class);


        executedSet.add(uniqueCode);

        return ratingMapper.toDto(rating);
    }

    public RatingDto findByTxId(String txId) {
        return ratingMapper.toDto(ratingRepository.findByTxId(txId));
    }

    public RatingDto cancel(RatingDto ratingDto) {
        String uniqueCode = ratingDto.uniqueCode();
        if(canceledSet.contains(uniqueCode) || !executedSet.contains(uniqueCode)){
            return findByTxId(
                    ratingDto.getTxId()
            );
        }

        Rating result = ratingRepository.findByTxId(
                ratingDto.getTxId()
        );
        result.setState(TccState.CANCELED);
        Rating saved = ratingRepository.save(result);
        canceledSet.add(uniqueCode);

        return ratingMapper.toDto(saved);
    }

}
