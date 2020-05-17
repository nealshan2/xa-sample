package cn.xa.task;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.ObjectClassId;
import cn.xa.common.tcc.TccConfig;
import cn.xa.tracking.TrackingClient;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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

    public TaskDto create(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);

        String txId = taskDto.getTxId();
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
        CollaborationDto collaborationDto = CollaborationDto.builder()
                .parentObjectId(100L)
                .parentObjectClassId(ObjectClassId.PROJECT)
                .objectId(task.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        restTemplate.postForEntity(collaborationServiceUrl, collaborationDto, String.class);

        String trackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId);
        TrackingDto trackingDto = TrackingDto.builder()
                .title("create Task " + task.getTitle())
                .detail(task.toString())
                .objectId(task.getId())
                .objectClassId(ObjectClassId.TASK)
                .build();
        restTemplate.postForEntity(trackingServiceUrl, trackingDto, String.class);

        return taskMapper.toDto(task);
    }

    public TaskDto findByTxId(String txId) {
        return taskMapper.toDto(taskRepository.findByTxId(txId));
    }

    public TaskDto delete(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);

        String txId = taskDto.getTxId();
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
        restTemplate.delete(collaborationServiceUrl);

        String trackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId);
        restTemplate.delete(trackingServiceUrl);

        return taskMapper.toDto(task);
    }

    public TaskDto save(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);

        String txId = taskDto.getTxId();
        String collaborationServiceUrl = String.format(TccConfig.COLLABORATION_TCC_URL, txId);
        RequestEntity<Void> requestEntity = RequestEntity.put(URI.create(collaborationServiceUrl))
                .contentType(new MediaType("application", "tcc"))
                .build();
        restTemplate.exchange(requestEntity, String.class);

        String trackingServiceUrl = String.format(TccConfig.TRACKING_TCC_URL, txId);
        requestEntity = RequestEntity.put(URI.create(trackingServiceUrl))
                .contentType(new MediaType("application", "tcc"))
                .build();
        restTemplate.exchange(requestEntity, String.class);

        return taskMapper.toDto(task);

    }
}
