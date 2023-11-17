package hexlet.code.dto;

import hexlet.code.model.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO {

    private Long id;
    private String name;
    private LocalDate createdAt;
    public LabelDTO(Label label) {
        this(
            label.getId(),
            label.getName(),
            label.getCreatedAt()
        );
    }
}
