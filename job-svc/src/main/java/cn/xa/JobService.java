package cn.xa;

import cn.xa.common.exception.ServiceException;
import cn.xa.job.JobDto;
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
public class JobService {

    private final JobRepository jobRepository;

    public JobDto create(JobDto jobRequest) {
        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .detail(jobRequest.getDetail())
                .build();
        jobRepository.save(job);

        if(job.getId() != null) {
            throw new ServiceException("force to throw exception for testing transaction");
        }

        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .detail(job.getDetail())
                .build();
    }
}
