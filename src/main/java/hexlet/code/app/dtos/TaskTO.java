package hexlet.code.app.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import hexlet.code.app.models.Label;
import hexlet.code.app.models.Task;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record TaskTO(
    Long id,
    String title,
    String content,
    @JsonAlias("assignee_id")
    Long assigneeId,
    String status,
    Set<Long> taskLabelIds,
    LocalDate createdAt,
    Long index
) {
    public TaskTO(Task task) {
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
