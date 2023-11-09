package hexlet.code.app.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import hexlet.code.app.models.Task;

import java.time.LocalDate;

public record TaskTO(
    Long id,
    String title,
    String content,
    @JsonAlias("assignee_id")
    Long assigneeId,
    String status,
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
            task.getCreatedAt(),
            task.getIndex()
        );
    }
}
