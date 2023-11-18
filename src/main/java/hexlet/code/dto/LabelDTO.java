package hexlet.code.dto;

import hexlet.code.model.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO {

    private Long id;
    private JsonNullable<String> name = JsonNullable.undefined();
    private LocalDate createdAt;
    public LabelDTO(Label label) {
        this(
            label.getId(),
            JsonNullable.of(label.getName()),
            label.getCreatedAt()
        );
    }
}
