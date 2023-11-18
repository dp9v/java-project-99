package hexlet.code.controllers;

import hexlet.code.controller.UsersController;
import hexlet.code.dto.UserDTO;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class UserControllerTest extends BaseIT {

    @SneakyThrows
    @Test
    public void testCreate() {
        var userToCreate = new UserDTO(generateUser());
        getMockMvc().perform(
            post(UsersController.PATH)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOm().writeValueAsString(userToCreate))
        ).andExpect(status().isCreated());

        assertThat(getUserRepository().count()).isEqualTo(2);
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var userForUpdate = new UserDTO(generateUser());

        getMockMvc().perform(
            put(UsersController.PATH + "/" + getTestUser().getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOm().writeValueAsString(userForUpdate))
        ).andExpect(status().isOk());

        var updatedUser = getUserRepository().findById(getTestUser().getId()).orElseThrow();
        assertThat(updatedUser)
            .matches(u -> u.getEmail().equals(userForUpdate.getEmail().get()), "user.email")
            .matches(u -> u.getFirstName().equals(userForUpdate.getFirstName().get()), "user.firstName")
            .matches(u -> u.getLastName().equals(userForUpdate.getLastName().get()), "user.lastName");
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var response = getMockMvc().perform(
                get(UsersController.PATH + "/" + getTestUser().getId())
                    .with(jwt())
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var user = getOm().readValue(response, UserDTO.class);
        assertThat(user)
            .matches(u -> u.getEmail().get().equals(getTestUser().getEmail()), "user.email")
            .matches(u -> u.getFirstName().get().equals(getTestUser().getFirstName()), "user.firstName")
            .matches(u -> u.getLastName().get().equals(getTestUser().getLastName()), "user.lastName");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var usersToCreate = Instancio.of(getModelGenerator().getUserModel())
            .stream()
            .limit(10)
            .toList();
        getUserRepository().saveAll(usersToCreate);

        var response = getMockMvc().perform(get(UsersController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var users = getOm().readValue(response, List.class);
        assertThat(users).hasSize(11);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        getMockMvc().perform(delete(UsersController.PATH + "/" + getTestUser().getId()).with(jwt()))
            .andExpect(status().isNoContent());

        assertThat(getUserRepository().count()).isZero();
    }

}
