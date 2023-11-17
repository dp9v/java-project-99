package hexlet.code.dto;

import hexlet.code.model.TaskStatus;

import java.time.LocalDate;

public record TaskStatusDTO(
        Long id,
        String name,
        String slug,
        LocalDate createdAt
) {
    public TaskStatusDTO(TaskStatus source) {
        this(source.getId(), source.getName(), source.getSlug(), source.getCreatedAt());
    }
}
