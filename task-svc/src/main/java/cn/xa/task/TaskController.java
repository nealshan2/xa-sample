package cn.xa.task;

import cn.xa.common.tcc.TccParticipantController;
import cn.xa.common.tcc.TccState;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
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
public class TaskController extends TccParticipantController<TaskDto> {

    private final TaskService taskService;

//    @PostMapping("/create")
//    public TaskDto createSpec(@RequestBody TaskDto taskDto) {
//        return taskService.create(taskDto);
//
//    }


    @Override
    public String getParticipantName() {
        return "task";
    }

    @Override
    public ResponseEntity executeTry(String txId, TaskDto body) {
        body.setTxId(txId);
        body.setState(TccState.TRY);
        try{
            taskService.create(body);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @Override
    public ResponseEntity executeCancel(String txId) {
        TaskDto taskDto = taskService.findByTxId(txId);
        if (taskDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        taskDto.setState(TccState.CANCELED);
        taskService.delete(taskDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity executeConfirm(String txId) {
        TaskDto taskDto = taskService.findByTxId(txId);
        if (taskDto == null) {
//            return ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        taskDto.setState(TccState.CONFIRMED);
        taskService.save(taskDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
