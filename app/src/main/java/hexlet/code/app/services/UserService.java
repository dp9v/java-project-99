package hexlet.code.app.services;

import hexlet.code.app.dtos.UserTO;
import hexlet.code.app.models.User;
import hexlet.code.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
@RequiredArgsConstructor
public final class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User create(UserTO user) {
        var userToCreate = merge(new User(), user);

        return userRepository.save(userToCreate);
    }

    public User update(Long id, UserTO user) {
        var userToUpdate = merge(userRepository.findById(id).orElseThrow(), user);

        return userRepository.save(userToUpdate);
    }

    private User merge(User user, UserTO userTO) {
        if (isNotBlank(userTO.email())) {
            user.setEmail(userTO.email());
        }
        if (isNotBlank(userTO.lastName())) {
            user.setLastName(userTO.lastName());
        }
        if (isNotBlank(userTO.firstName())) {
            user.setFirstName(userTO.firstName());
        }
        if (isNotBlank(userTO.password())) {
            user.setPassword(userTO.password());
        }

        return user;
    }
}