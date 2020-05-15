package cn.xa.task;

import cn.xa.collaboration.CollaborationClient;
import cn.xa.collaboration.CollaborationDto;
import cn.xa.collaboration.CollaborationType;
import cn.xa.tracking.TrackingClient;
import cn.xa.tracking.TrackingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public TaskDto create(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        taskRepository.save(task);

        collaborationClient.createCollaboration(CollaborationDto.builder()
                .parentId(100L)
                .childId(task.getId())
                .type(CollaborationType.TASK)
                .build());

        trackingClient.createTracking(TrackingDto.builder()
                .title("create Task " + task.getTitle())
                .detail(task.toString())
                .build());

        return taskMapper.toDto(task);
    }
}
