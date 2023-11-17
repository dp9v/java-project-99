package hexlet.code.components;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelsRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("!test")
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
                .orElseGet(() -> {
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
                .map(l -> labelsRepository.findByName(l)
                        .orElse(new Label()
                                .setName(l)
                                .setCreatedAt(LocalDate.now()))
                ).peek(labelsRepository::save)
                .collect(Collectors.toSet());
    }
}
