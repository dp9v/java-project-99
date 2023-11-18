package hexlet.code.dto;

import hexlet.code.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private JsonNullable<String> email = JsonNullable.undefined();
    private JsonNullable<String> firstName = JsonNullable.undefined();
    private JsonNullable<String> lastName = JsonNullable.undefined();
    private JsonNullable<String> password = JsonNullable.undefined();
    private LocalDate createdAt;

    public UserDTO(User user) {
        this(
            user.getId(),
            JsonNullable.of(user.getEmail()),
            JsonNullable.of(user.getFirstName()),
            JsonNullable.of(user.getLastName()),
            JsonNullable.undefined(),
            user.getCreatedAt()
        );
    }
}
