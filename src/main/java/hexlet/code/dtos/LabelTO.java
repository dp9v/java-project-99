package hexlet.code.dtos;

import hexlet.code.models.Label;

import java.time.LocalDate;

public record LabelTO(
    Long id,
    String name,
    LocalDate createdAt
) {
    public LabelTO(Label label) {
        this(
            label.getId(),
            label.getName(),
            label.getCreatedAt()
        );
    }
}
