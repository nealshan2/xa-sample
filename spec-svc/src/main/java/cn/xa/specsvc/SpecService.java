package cn.xa.specsvc;

import cn.xa.spec.SpecDto;
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
public class SpecService {

    private final SpecRepository specRepository;

    public SpecDto create(SpecDto specRequest) {
        Spec spec = Spec.builder()
                .title(specRequest.getTitle())
                .quantity(specRequest.getQuantity())
                .build();
        specRepository.save(spec);

        return SpecDto.builder()
                .id(spec.getId())
                .title(spec.getTitle())
                .quantity(spec.getQuantity())
                .build();
    }
}
