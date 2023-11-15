package hexlet.code.app.dtos;

import hexlet.code.app.models.Label;

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
