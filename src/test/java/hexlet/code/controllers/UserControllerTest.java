package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.UsersController;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public final class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private ObjectMapper om;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = createUser();
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void testCreate() {
        var userToCreate = new UserDTO(generateUser());
        mockMvc.perform(
            post(UsersController.PATH)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userToCreate))
        ).andExpect(status().isCreated());

        assertThat(userRepository.count()).isEqualTo(2);
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var userForUpdate = new UserDTO(generateUser());

        mockMvc.perform(
            put(UsersController.PATH + "/" + testUser.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userForUpdate))
        ).andExpect(status().isOk());

        var updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updatedUser)
            .matches(u -> u.getEmail().equals(userForUpdate.getEmail().get()), "user.email")
            .matches(u -> u.getFirstName().equals(userForUpdate.getFirstName().get()), "user.firstName")
            .matches(u -> u.getLastName().equals(userForUpdate.getLastName().get()), "user.lastName");
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var response = mockMvc.perform(
                get(UsersController.PATH + "/" + testUser.getId())
                    .with(jwt())
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var user = om.readValue(response, UserDTO.class);
        assertThat(user)
            .matches(u -> u.getEmail().get().equals(testUser.getEmail()), "user.email")
            .matches(u -> u.getFirstName().get().equals(testUser.getFirstName()), "user.firstName")
            .matches(u -> u.getLastName().get().equals(testUser.getLastName()), "user.lastName");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var usersToCreate = Instancio.of(modelGenerator.getUserModel())
            .stream()
            .limit(10)
            .toList();
        userRepository.saveAll(usersToCreate);

        var response = mockMvc.perform(get(UsersController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var users = om.readValue(response, List.class);
        assertThat(users).hasSize(11);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        mockMvc.perform(delete(UsersController.PATH + "/" + testUser.getId()).with(jwt()))
            .andExpect(status().isNoContent());

        assertThat(userRepository.count()).isZero();
    }

    private User createUser() {
        return userRepository.save(
            generateUser()
        );
    }

    private User generateUser() {
        return Instancio.of(modelGenerator.getUserModel())
            .create();
    }
}
