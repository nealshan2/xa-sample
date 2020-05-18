package cn.xa.task;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.common.tcc.TccState;
import cn.xa.tracking.TrackingClient;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.apache.servicecomb.saga.omega.transaction.annotations.Compensable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Transactional
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CollaborationClient collaborationClient;
    private final TrackingClient trackingClient;
    private final RestTemplate restTemplate;
    private Set<String> executedSet = new ConcurrentHashMap<>().newKeySet();

    private Set<String> canceledSet = new ConcurrentHashMap<>().newKeySet();

    @Compensable(timeout = 1, compensationMethod = "cancel")
    public TaskDto save(TaskDto taskDto) {
        if(executedSet.contains(taskDto.getTxId()) || canceledSet.contains(taskDto.getTxId())){
            return findByTxIdAndObjectIdAndObjectClassId(
                    taskDto.getTxId(),
                    taskDto.getObjectId(),
                    taskDto.getObjectClassId()
            );
        }

        taskDto.setState(TccState.CONFIRMED);

        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);

        executedSet.add(task.getTxId());

        return taskMapper.toDto(task);
    }

    public TaskDto findByTxIdAndObjectIdAndObjectClassId(String txId, Long objectId, Long objectClassId) {
        return taskMapper.toDto(taskRepository.findByTxIdAndObjectIdAndObjectClassId(txId, objectId, objectClassId));
    }

    public TaskDto cancel(TaskDto taskDto) {
        if(canceledSet.contains(taskDto.getTxId()) || !executedSet.contains(taskDto.getTxId())){
            return findByTxIdAndObjectIdAndObjectClassId(
                    taskDto.getTxId(),
                    taskDto.getObjectId(),
                    taskDto.getObjectClassId()
            );
        }

        Task result = taskRepository.findByTxIdAndObjectIdAndObjectClassId(
                taskDto.getTxId(),
                taskDto.getObjectId(),
                taskDto.getObjectClassId()
        );
        result.setState(TccState.CANCELED);
        Task saved = taskRepository.save(result);
        canceledSet.add(saved.getTxId());

        return taskMapper.toDto(saved);
    }

}
