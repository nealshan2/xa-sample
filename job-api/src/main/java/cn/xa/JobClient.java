package cn.xa;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@FeignClient(name = "jobClient", path = "/v1/job", url = "http://localhost:8082")
public interface JobClient {

    @PostMapping("/create")
    JobDto createJob(@RequestBody JobDto jobRequest);

}
