package cn.xa.rfesvc;

import cn.xa.job.JobClient;
import cn.xa.job.JobDto;
import cn.xa.spec.SpecClient;
import cn.xa.spec.SpecDto;
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
    private final SpecClient specClient;
    private final JobClient jobClient;
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
        SpecDto specRequest = SpecDto.builder()
                .title("spec 1")
                .quantity(100L)
                .build();
        SpecDto specDto = specClient.createSpec(specRequest);

        // TODO: CREATE JOB

        JobDto jobRequest = JobDto.builder()
                .title("create Estimate")
                .detail("Neal created a estimate")
                .build();
        JobDto jobDto = jobClient.createJob(jobRequest);

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
