package cn.xa.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@FeignClient(name = "specClient", path = "/v1/spec", url = "http://localhost:8081")
public interface SpecClient {

    @PostMapping("/create")
    SpecDto createSpec(@RequestBody SpecDto specRequest);


}
