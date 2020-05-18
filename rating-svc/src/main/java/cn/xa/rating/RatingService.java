package cn.xa.rating;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.common.tcc.TccState;
import cn.xa.task.TaskClient;
import cn.xa.tracking.TrackingClient;
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

        if(executedSet.contains(ratingDto.getTxId()) || canceledSet.contains(ratingDto.getTxId())){
            return findByTxId(
                    ratingDto.getTxId()
            );
        }

        ratingDto.setState(TccState.CONFIRMED);
        Rating rating = ratingMapper.toEntity(ratingDto);
        ratingRepository.save(rating);
        executedSet.add(rating.getTxId());

        return ratingMapper.toDto(rating);
    }

    public RatingDto findByTxId(String txId) {
        return ratingMapper.toDto(ratingRepository.findByTxId(txId));
    }

    public RatingDto cancel(RatingDto ratingDto) {
        if(canceledSet.contains(ratingDto.getTxId()) || !executedSet.contains(ratingDto.getTxId())){
            return findByTxId(
                    ratingDto.getTxId()
            );
        }

        Rating result = ratingRepository.findByTxId(
                ratingDto.getTxId()
        );
        result.setState(TccState.CANCELED);
        Rating saved = ratingRepository.save(result);
        canceledSet.add(saved.getTxId());

        return ratingMapper.toDto(saved);
    }

}
