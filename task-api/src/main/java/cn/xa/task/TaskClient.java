package cn.xa.task;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@FeignClient(name = "taskClient", path = "/v1/task", url = "http://localhost:8086")
public interface TaskClient {

    @PostMapping("/create")
    TaskDto createTask(@RequestBody TaskDto taskDto);


}
