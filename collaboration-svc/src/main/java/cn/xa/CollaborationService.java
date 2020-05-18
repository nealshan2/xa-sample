package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import cn.xa.common.tcc.TccState;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Transactional
@Service
@RequiredArgsConstructor
public class CollaborationService {

    private final CollaborationMapper collaborationMapper;
    private final CollaborationRepository collaborationRepository;

    private Set<String> executedSet = new ConcurrentHashMap<>().newKeySet();

    private Set<String> canceledSet = new ConcurrentHashMap<>().newKeySet();

    @Compensable(timeout = 1, compensationMethod = "cancel")
    public CollaborationDto save(CollaborationDto collaborationDto) {

        String uniqueCode = collaborationDto.uniqueCode();
        if(executedSet.contains(uniqueCode) || canceledSet.contains(uniqueCode)){
            return findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(
                    collaborationDto.getTxId(),
                    collaborationDto.getParentObjectId(),
                    collaborationDto.getParentObjectClassId(),
                    collaborationDto.getObjectId(),
                    collaborationDto.getObjectClassId()
            );
        }

        collaborationDto.setState(TccState.CONFIRMED);
        Collaboration collaboration = collaborationMapper.toEntity(collaborationDto);
        collaborationRepository.save(collaboration);
        executedSet.add(uniqueCode);

        return collaborationMapper.toDto(collaboration);
    }

    public CollaborationDto cancel(CollaborationDto collaborationDto) {
        String uniqueCode = collaborationDto.uniqueCode();
        if(canceledSet.contains(uniqueCode) || !executedSet.contains(uniqueCode)){
            return findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(
                    collaborationDto.getTxId(),
                    collaborationDto.getParentObjectId(),
                    collaborationDto.getParentObjectClassId(),
                    collaborationDto.getObjectId(),
                    collaborationDto.getObjectClassId()
            );
        }

        Collaboration result = collaborationRepository.findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(
                collaborationDto.getTxId(),
                collaborationDto.getParentObjectId(),
                collaborationDto.getParentObjectClassId(),
                collaborationDto.getObjectId(),
                collaborationDto.getObjectClassId()
        );

        if (result == null) {
            throw new IllegalStateException();
        }
        result.setState(TccState.CANCELED);
        Collaboration saved = collaborationRepository.save(result);
        canceledSet.add(uniqueCode);

        return collaborationMapper.toDto(saved);
    }

    public CollaborationDto findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(String txId, Long parentObjectId, Long parentObjectClassId, Long objectId, Long objectClassId) {
        return collaborationMapper.toDto(collaborationRepository.findByTxIdAndParentObjectIdAndParentObjectClassIdAndObjectIdAndObjectClassId(txId, parentObjectId, parentObjectClassId, objectId, objectClassId));
    }
}
