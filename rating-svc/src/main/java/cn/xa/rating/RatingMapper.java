package cn.xa.rating;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RatingMapper {

    public abstract RatingDto toDto(Rating entity);

    public abstract Rating toEntity(RatingDto dto);

}