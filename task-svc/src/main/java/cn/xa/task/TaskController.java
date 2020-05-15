package cn.xa.task;

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
@RequestMapping(path = "/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public TaskDto createSpec(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);

    }


}
