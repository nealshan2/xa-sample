package cn.xa;

import cn.xa.collaboration.CollaborationDto;
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
public class CollaborationService {

    private final CollaborationMapper collaborationMapper;
    private final CollaborationRepository collaborationRepository;

    public CollaborationDto create(CollaborationDto collaborationDto) {
        Collaboration collaboration = collaborationMapper.toEntity(collaborationDto);
        collaborationRepository.save(collaboration);
        return collaborationMapper.toDto(collaboration);
    }
}
