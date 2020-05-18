package cn.xa;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.common.tcc.TccState;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Transactional
@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingMapper trackingMapper;
    private final TrackingRepository trackingRepository;
    private final CollaborationClient collaborationClient;
    private final RestTemplate restTemplate;

    private Set<String> executedSet = new ConcurrentHashMap<>().newKeySet();

    private Set<String> canceledSet = new ConcurrentHashMap<>().newKeySet();

    @Compensable(timeout = 1, compensationMethod = "cancel")
    public TrackingDto save(TrackingDto trackingDto) {

        String uniqueCode = trackingDto.uniqueCode();
        if(executedSet.contains(uniqueCode) || canceledSet.contains(uniqueCode)){
            return findByTxIdAndObjectIdAndObjectClassId(
                    trackingDto.getTxId(),
                    trackingDto.getObjectId(),
                    trackingDto.getObjectClassId()
            );
        }

        trackingDto.setState(TccState.CONFIRMED);

        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

        CollaborationDto trackingCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(tracking.getId())
                .objectClassId(ObjectClassId.TRACKING)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, trackingCollaborationDto, String.class);

        executedSet.add(uniqueCode);

        return trackingMapper.toDto(tracking);
    }

    public TrackingDto findByTxIdAndObjectIdAndObjectClassId(String txId, Long objectId, Long objectClassId) {
        return trackingMapper.toDto(trackingRepository.findByTxIdAndObjectIdAndObjectClassId(txId, objectId, objectClassId));
    }

    public TrackingDto cancel(TrackingDto trackingDto) {

        String uniqueCode = trackingDto.uniqueCode();
        if(canceledSet.contains(uniqueCode) || !executedSet.contains(uniqueCode)){
            return findByTxIdAndObjectIdAndObjectClassId(
                    trackingDto.getTxId(),
                    trackingDto.getObjectId(),
                    trackingDto.getObjectClassId()
            );
        }

        Tracking result = trackingRepository.findByTxIdAndObjectIdAndObjectClassId(
                trackingDto.getTxId(),
                trackingDto.getObjectId(),
                trackingDto.getObjectClassId()
        );
        result.setState(TccState.CANCELED);
        Tracking saved = trackingRepository.save(result);
        canceledSet.add(uniqueCode);

        return trackingMapper.toDto(saved);
    }
}
