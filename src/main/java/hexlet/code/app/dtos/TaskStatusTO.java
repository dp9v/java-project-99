package hexlet.code.app.dtos;

import hexlet.code.app.models.TaskStatus;

import java.time.LocalDate;

public record TaskStatusTO(
        Long id,
        String name,
        String slug,
        LocalDate createdAt
) {
    public TaskStatusTO(TaskStatus source) {
        this(source.getId(), source.getName(), source.getSlug(), source.getCreatedAt());
    }
}
