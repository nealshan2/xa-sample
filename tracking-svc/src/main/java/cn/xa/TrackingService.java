package cn.xa;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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

    public TrackingDto create(TrackingDto trackingDto) {

        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

//        String txId = trackingDto.getTxId();
//        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
//        CollaborationDto collaborationDto = CollaborationDto.builder()
//                .parentObjectId(100L)
//                .parentObjectClassId(ObjectClassId.PROJECT)
//                .objectId(tracking.getId())
//                .objectClassId(ObjectClassId.TRACKING)
//                .txId(trackingDto.getTxId())
//                .state(trackingDto.getState())
//                .build();
//        restTemplate.postForEntity(collaborationServiceUrl, collaborationDto, String.class);

        return trackingMapper.toDto(tracking);
    }

    public TrackingDto findByTxIdAndObjectIdAndObjectClassId(String txId, Long objectId, Long objectClassId) {
        return trackingMapper.toDto(trackingRepository.findByTxIdAndObjectIdAndObjectClassId(txId, objectId, objectClassId));
    }

    public TrackingDto save(TrackingDto trackingDto) {

        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

//        String txId = trackingDto.getTxId();
//        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
//        RequestEntity<Void> requestEntity = RequestEntity.put(URI.create(collaborationServiceUrl))
//                .contentType(new MediaType("application", "tcc"))
//                .build();
//        restTemplate.exchange(requestEntity, String.class);

        return trackingMapper.toDto(tracking);

    }

    public TrackingDto delete(TrackingDto trackingDto) {
        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

//        String txId = trackingDto.getTxId();
//        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
//        restTemplate.delete(collaborationServiceUrl);

        return trackingMapper.toDto(tracking);
    }
}
