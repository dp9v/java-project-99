package hexlet.code.components;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
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
    private final LabelRepository labelRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {
        userRepository.findByEmail(DEFAULT_LOGIN)
            .orElseGet(() -> {
                var userToCreate = new User()
                    .setEmail(DEFAULT_LOGIN)
                    .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                    .setCreatedAt(LocalDate.now())
                    .setUpdatedAt(LocalDate.now());
                return userRepository.save(userToCreate);
            });

        DEFAULT_STATUSES.stream()
            .map(s -> taskStatusRepository.findByName(s)
                .orElse(new TaskStatus()
                    .setName(s)
                    .setSlug(s)
                    .setCreatedAt(LocalDate.now()))
            )
            .forEach(taskStatusRepository::save);

        DEFAULT_LABELS.stream()
            .map(l -> labelRepository.findByName(l)
                .orElse(new Label()
                    .setName(l)
                    .setCreatedAt(LocalDate.now()))
            ).forEach(labelRepository::save);
    }
}
