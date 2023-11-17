package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    @JsonAlias("title")
    private String name;
    @JsonAlias("content")
    private String description;
    @JsonAlias("assignee_id")
    private Long assigneeId;
    @JsonAlias("status")
    private String taskStatusSlug;
    private Set<Long> taskLabelIds;
    private LocalDate createdAt;
    private Long index;

    public TaskDTO(Task task) {
        this(
            task.getId(),
            task.getName(),
            task.getDescription(),
            task.getAssignee() == null ? null : task.getAssignee().getId(),
            task.getTaskStatus().getName(),
            task.getLabels()
                .stream()
                .map(Label::getId)
                .collect(Collectors.toSet()),
            task.getCreatedAt(),
            task.getIndex()
        );
    }
}
