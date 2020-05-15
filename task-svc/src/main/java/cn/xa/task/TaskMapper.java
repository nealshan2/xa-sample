package cn.xa.task;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TaskMapper {

    public abstract TaskDto toDto(Task entity);

    public abstract Task toEntity(TaskDto dto);

}