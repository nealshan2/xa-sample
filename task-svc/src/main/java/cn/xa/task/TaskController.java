package cn.xa.task;

import cn.xa.common.tcc.TccState;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.context.OmegaContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final OmegaContext omegaContext;

    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody TaskDto body) {
        body.setTxId(omegaContext.globalTxId());
        TaskDto taskDto = taskService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
    }

}
