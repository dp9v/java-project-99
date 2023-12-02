package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    uses = { JsonNullableMapper.class, ReferenceMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class TaskStatusMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdAt", ignore = true)
    })
    public abstract TaskStatusDTO map(TaskStatus source);

    public abstract List<TaskStatusDTO> map(List<TaskStatus> source);

    @InheritConfiguration
    public abstract TaskStatus update(TaskStatusDTO source, @MappingTarget TaskStatus target);
}
