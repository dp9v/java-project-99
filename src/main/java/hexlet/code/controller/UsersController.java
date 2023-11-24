package hexlet.code.controller;

import hexlet.code.dto.UserDTO;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import hexlet.code.utils.ResponseEntityBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(UsersController.PATH)
public final class UsersController {
    public static final String PATH = "/api/users";

    private final UserService userService;
    private final UserRepository userRepository;

    private static final String ONLY_OWNER_BY_ID = """
                @userRepository.findById(#id).get().getEmail() == authentication.name
            """;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntityBuilder.build(userService.getAll(), UserDTO::new);
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return new UserDTO(
                userService.getById(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserDTO user) {
        return new UserDTO(
                userService.create(user)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserDTO create(@PathVariable Long id, @RequestBody UserDTO user) {
        return new UserDTO(
                userService.update(id, user)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
