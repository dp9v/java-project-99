package hexlet.code.service;

import hexlet.code.dto.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserDTO> getAll() {
        return userMapper.map(userRepository.findAll());
    }

    public UserDTO getById(Long id) {
        return userMapper.map(
            userRepository.findById(id).orElseThrow()
        );
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO create(UserDTO user) {
        var userToCreate = userMapper.update(user, new User())
            .setCreatedAt(LocalDate.now());

        return userMapper.map(userRepository.save(userToCreate));
    }

    public UserDTO update(Long id, UserDTO user) {
        var userToUpdate = userRepository.findById(id)
            .map(u->userMapper.update(user, u))
            .orElseThrow();

        return userMapper.map(userRepository.save(userToUpdate));
    }

    @Override
    public void createUser(UserDetails user) {
        userRepository.save(new User()
            .setEmail(user.getUsername())
            .setPassword(passwordEncoder.encode(user.getPassword()))
        );
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("Username '%s' was not found", username)));
    }
}
