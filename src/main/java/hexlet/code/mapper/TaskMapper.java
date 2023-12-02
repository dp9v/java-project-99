package hexlet.code.mapper;

import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    uses = {JsonNullableMapper.class, ReferenceMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Mappings({
        @Mapping(source = "taskStatus.slug", target = "taskStatusSlug"),
        @Mapping(source = "assignee.id", target = "assigneeId"),
        @Mapping(source = "labels", target = "taskLabelIds")
    })
    public abstract TaskDTO map(Task model);

    public abstract List<TaskDTO> map(List<Task> model);

    @Mappings({
        @Mapping(source = "taskStatusSlug", target = "taskStatus"),
        @Mapping(source = "assigneeId", target = "assignee.id"),
        @Mapping(source = "taskLabelIds", target = "labels")
    })
    public abstract Task update(TaskDTO source, @MappingTarget Task target);

    public TaskStatus toEntity(String slug) {
        return taskStatusRepository.findBySlug(slug)
            .orElseThrow();
    }

    public Set<Label> toEntity(Set<Long> labelIds) {
        if (labelIds == null) {
            return null;
        }
        return labelIds.stream()
            .map(labelId -> new Label().setId(labelId))
            .collect(Collectors.toSet());
    }

    public Set<Long> toDto(Set<Label> labels) {
        return labels.stream()
            .map(Label::getId)
            .collect(Collectors.toSet());
    }
}
