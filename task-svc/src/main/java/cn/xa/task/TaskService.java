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
        String uniqueCode = taskDto.uniqueCode();
        if(executedSet.contains(uniqueCode) || canceledSet.contains(uniqueCode)){
            return findByTxIdAndObjectIdAndObjectClassId(
                    taskDto.getTxId(),
                    taskDto.getObjectId(),
                    taskDto.getObjectClassId()
            );
        }

        taskDto.setState(TccState.CONFIRMED);

        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);

        // create collaboraiton for task
        CollaborationDto taskTrackingCollaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(task.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        restTemplate.postForEntity(TccConfig.COLLABORATION_TCC_URL, taskTrackingCollaborationDto, String.class);

        // create tracking for task
        TrackingDto taskTrackingDto = TrackingDto.builder()
                .title("Created Task " + taskDto.getTitle())
                .detail(taskDto.toString())
                .objectId(task.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        taskTrackingDto = restTemplate.postForObject(TccConfig.TRACKING_TCC_URL, taskTrackingDto, TrackingDto.class);



        executedSet.add(uniqueCode);

        return taskMapper.toDto(task);
    }

    public TaskDto findByTxIdAndObjectIdAndObjectClassId(String txId, Long objectId, Long objectClassId) {
        return taskMapper.toDto(taskRepository.findByTxIdAndObjectIdAndObjectClassId(txId, objectId, objectClassId));
    }

    public TaskDto cancel(TaskDto taskDto) {
        String uniqueCode = taskDto.uniqueCode();
        if(canceledSet.contains(uniqueCode) || !executedSet.contains(uniqueCode)){
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
        canceledSet.add(uniqueCode);

        return taskMapper.toDto(saved);
    }

}
