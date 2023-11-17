package hexlet.code.dtos;

import hexlet.code.models.User;

import java.time.LocalDate;

public record UserTO(
    Long id,

    String email,
    String firstName,
    String lastName,
    LocalDate createdAt,
    LocalDate updatedAt,
    String password
) {
    public UserTO(User user) {
        this(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getCreatedAt(),
            null,
            null
        );
    }
}
