package hexlet.code.app.components;

import hexlet.code.app.models.User;
import hexlet.code.app.repositories.UserRepository;
import hexlet.code.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private static final String DEFAULT_LOGIN = "hexlet@example.com";
    private static final String DEFAULT_PASSWORD = "qwerty";

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        if(userRepository.findByEmail(DEFAULT_LOGIN).isPresent()) {
            return;
        }

        var userToCreate = new User()
            .setEmail(DEFAULT_LOGIN)
            .setPassword(DEFAULT_PASSWORD);
        userService.createUser(userToCreate);
    }
}
