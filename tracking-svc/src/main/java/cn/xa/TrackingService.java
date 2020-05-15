package cn.xa;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.CollaborationType;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public TrackingDto create(TrackingDto trackingDto) {
        Tracking tracking = trackingMapper.toEntity(trackingDto);
        trackingRepository.save(tracking);

        // TODO: use tcc client to try confirm cancel
        CollaborationDto collaborationDto = CollaborationDto.builder()
                .parentId(100L)
                .childId(tracking.getId())
                .type(CollaborationType.TRACKING)
                .txId(trackingDto.getTxId())
                .state(trackingDto.getState())
                .build();


        collaborationClient.createCollaboration(collaborationDto);

        return trackingMapper.toDto(tracking);
    }

    public TrackingDto findByTxId(String txId) {
        return trackingMapper.toDto(trackingRepository.findByTxId(txId));
    }

    public void save(TrackingDto trackingDto) {

    }
}
