package cn.xa;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/v1/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping("/create")
    public JobDto createJob(@RequestBody JobDto jobRequest) {
        return jobService.create(jobRequest);
    }
}
