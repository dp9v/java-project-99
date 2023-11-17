package hexlet.code.controller;

import hexlet.code.dto.UserDTO;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(UsersController.PATH)
public final class UsersController {
    public static final String PATH = "/api/users";

    private final UserService userService;

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    public UserDTO create(@PathVariable Long id, @RequestBody UserDTO user) {
        return new UserDTO(
            userService.update(id, user)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserDTO user) {
        return new UserDTO(
            userService.create(user)
        );
    }
}
