package hexlet.code.app.components;

import hexlet.code.app.models.Label;
import hexlet.code.app.models.TaskStatus;
import hexlet.code.app.models.User;
import hexlet.code.app.repositories.LabelsRepository;
import hexlet.code.app.repositories.TaskStatusRepository;
import hexlet.code.app.repositories.UserRepository;
import hexlet.code.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private static final List<String> DEFAULT_LABELS = List.of(
            "bug", "feature"
    );

    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelsRepository labelsRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {
        var createdUser = userRepository.findByEmail(DEFAULT_LOGIN)
                .orElseGet(()->{
                    var userToCreate = new User()
                            .setEmail(DEFAULT_LOGIN)
                            .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                            .setCreatedAt(LocalDate.now())
                            .setUpdatedAt(LocalDate.now());
                    return userRepository.save(userToCreate);
                });

        var createdStatuses = DEFAULT_STATUSES.stream()
            .map(s -> taskStatusRepository.findByName(s)
                        .orElse(new TaskStatus()
                                .setName(s)
                                .setSlug(s)
                                .setCreatedAt(LocalDate.now()))
            )
            .peek(taskStatusRepository::save)
            .collect(Collectors.toSet());

        var createdLabels = DEFAULT_LABELS.stream()
                .map(l-> labelsRepository.findByName(l)
                        .orElse(new Label()
                                .setName(l)
                                .setCreatedAt(LocalDate.now()))
                ).peek(labelsRepository::save)
                .collect(Collectors.toSet());
    }
}
