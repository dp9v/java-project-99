package hexlet.code.dto;

import hexlet.code.model.Label;

import java.time.LocalDate;

public record LabelDTO(
    Long id,
    String name,
    LocalDate createdAt
) {
    public LabelDTO(Label label) {
        this(
            label.getId(),
            label.getName(),
            label.getCreatedAt()
        );
    }
}
