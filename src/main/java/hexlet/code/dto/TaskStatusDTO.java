package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDTO {
    private Long id;
    private JsonNullable<String> name = JsonNullable.undefined();
    private JsonNullable<String> slug = JsonNullable.undefined();
    private LocalDate createdAt;

    public TaskStatusDTO(TaskStatus source) {
        this(
            source.getId(),
            JsonNullable.of(source.getName()),
            JsonNullable.of(source.getSlug()),
            source.getCreatedAt()
        );
    }
}
