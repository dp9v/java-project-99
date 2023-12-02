package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("title")
    private JsonNullable<String> name = JsonNullable.undefined();
    @JsonProperty("content")
    private JsonNullable<String> description = JsonNullable.undefined();
    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId = JsonNullable.undefined();
    @JsonProperty("status")
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
            JsonNullable.of(task.getTaskStatus().getSlug()),
            JsonNullable.of(task.getLabels()
                .stream()
                .map(Label::getId)
                .collect(Collectors.toSet())),
            JsonNullable.of(task.getIndex()),
            task.getCreatedAt()
        );
    }
}
