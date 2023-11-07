package hexlet.code.app.controllers;

import hexlet.code.app.dtos.UserTO;
import hexlet.code.app.services.UserService;
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
    public ResponseEntity<List<UserTO>> getAll() {
        var users = userService.getAll()
            .stream()
            .map(UserTO::new)
            .toList();
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(users.size()))
            .body(users);
    }

    @GetMapping("/{id}")
    public UserTO getById(@PathVariable Long id) {
        return new UserTO(
            userService.getById(id)
        );
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    public UserTO create(@PathVariable Long id, @RequestBody UserTO user) {
        return new UserTO(
            userService.update(id, user)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTO create(@RequestBody UserTO user) {
        return new UserTO(
            userService.create(user)
        );
    }
}
