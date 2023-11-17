package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;

    public TaskStatusDTO(TaskStatus source) {
        this(source.getId(), source.getName(), source.getSlug(), source.getCreatedAt());
    }
}
