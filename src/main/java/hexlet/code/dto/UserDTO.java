package hexlet.code.dto;

import hexlet.code.model.User;

import java.time.LocalDate;

public record UserDTO(
    Long id,

    String email,
    String firstName,
    String lastName,
    LocalDate createdAt,
    LocalDate updatedAt,
    String password
) {
    public UserDTO(User user) {
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
