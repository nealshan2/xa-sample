package cn.xa;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.CollaborationType;
import cn.xa.common.tcc.TccConfig;
import cn.xa.tracking.TrackingDto;
import com.atomikos.tcc.rest.ParticipantLink;
import com.atomikos.tcc.rest.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

        String txId = trackingDto.getTxId();
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
        CollaborationDto collaborationDto = CollaborationDto.builder()
                .parentId(100L)
                .childId(tracking.getId())
                .type(CollaborationType.TRACKING)
                .txId(trackingDto.getTxId())
                .state(trackingDto.getState())
                .build();
        restTemplate.postForEntity(collaborationServiceUrl, collaborationDto, String.class);

        return trackingMapper.toDto(tracking);
    }

    public TrackingDto findByTxId(String txId) {
        return trackingMapper.toDto(trackingRepository.findByTxId(txId));
    }

    public TrackingDto save(TrackingDto trackingDto) {

        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

        String txId = trackingDto.getTxId();
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
        restTemplate.put(collaborationServiceUrl, null);

        return trackingMapper.toDto(tracking);

    }

    public TrackingDto delete(TrackingDto trackingDto) {
        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

        String txId = trackingDto.getTxId();
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
        restTemplate.delete(collaborationServiceUrl);

        return trackingMapper.toDto(tracking);
    }
}
