package hexlet.code.app.components;

import hexlet.code.app.models.TaskStatus;
import hexlet.code.app.models.User;
import hexlet.code.app.repositories.TaskStatusRepository;
import hexlet.code.app.repositories.UserRepository;
import hexlet.code.app.services.TaskStatusService;
import hexlet.code.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private static final String DEFAULT_LOGIN = "hexlet@example.com";
    private static final String DEFAULT_PASSWORD = "qwerty";

    private static final List<String> DEFAULT_STATUSES = List.of(
            "draft", "to_review", "to_be_fixed", "to_publish", "published"
    );

    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(DEFAULT_LOGIN).isEmpty()) {
            var userToCreate = new User()
                    .setEmail(DEFAULT_LOGIN)
                    .setPassword(DEFAULT_PASSWORD)
                    .setCreatedAt(LocalDate.now())
                    .setUpdatedAt(LocalDate.now());
            userService.createUser(userToCreate);
        }


        var createdStatuses = DEFAULT_STATUSES.stream()
                .map(s -> new TaskStatus()
                        .setName(s)
                        .setSlug(s)
                        .setCreatedAt(LocalDate.now()))
                .collect(Collectors.toSet());
        taskStatusRepository.saveAll(createdStatuses);
    }
}
