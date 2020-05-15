package cn.xa;

import cn.xa.tracking.TrackingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TrackingMapper {

    public abstract TrackingDto toDto(Tracking entity);

    public abstract Tracking toEntity(TrackingDto dto);

}