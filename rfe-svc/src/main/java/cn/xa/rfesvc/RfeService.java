package cn.xa.rfesvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor
public class RfeService {

    private final RfeRepository rfeRepository;
//    private final SpecClient specClient;
//    private final JobClient jobClient;
//    private final TaskClient taskClient;
//    private final TrackingClient trackingClient;

    public RfeDto create(RfeDto rfeRequest) {

        if (StringUtils.hasText(rfeRequest.getReference())) {
            Rfe rfe = rfeRepository.findByReference(rfeRequest.getReference());
            if (rfe != null) {
                throw new ServiceException("Rfe reference is exist, please input a new one");
            }
        }

        // CREATE RFE
        Rfe rfe = Rfe.builder()
                .title(rfeRequest.getTitle())
                .reference(rfeRequest.getReference())
                .build();
        rfeRepository.save(rfe);

        RfeDto rfeDto = this.convertToDto(rfe);

        // TODO: CREATE RFE ITEMS

        // TODO: CREATE SPEC
        // TODO: CREATE JOB
        // TODO: CREATE TASK
        // TODO: CREATE TRACKING

        return rfeDto;
    }

    private RfeDto convertToDto(Rfe rfe) {
        return RfeDto.builder()
                .id(rfe.getId())
                .title(rfe.getTitle())
                .reference(rfe.getReference())
                .build();
    }
}