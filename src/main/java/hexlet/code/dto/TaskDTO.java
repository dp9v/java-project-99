package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    @JsonAlias("title")
    private JsonNullable<String> name = JsonNullable.undefined();
    @JsonAlias("content")
    private JsonNullable<String> description = JsonNullable.undefined();
    @JsonAlias("assignee_id")
    private JsonNullable<Long> assigneeId = JsonNullable.undefined();
    @JsonAlias("status")
    private JsonNullable<String> taskStatusSlug = JsonNullable.undefined();
    private JsonNullable<Set<Long>> taskLabelIds = JsonNullable.undefined();
    private JsonNullable<Long> index = JsonNullable.undefined();
    private LocalDate createdAt;

    public TaskDTO(Task task) {
        this(
            task.getId(),
            JsonNullable.of(task.getName()),
            JsonNullable.of(task.getDescription()),
            JsonNullable.of(task.getAssignee() == null ? null : task.getAssignee().getId()),
            JsonNullable.of(task.getTaskStatus().getName()),
            JsonNullable.of(task.getLabels()
                .stream()
                .map(Label::getId)
                .collect(Collectors.toSet())),
            JsonNullable.of(task.getIndex()),
            task.getCreatedAt()
        );
    }
}
