package hexlet.code.controllers;

import hexlet.code.controller.TaskStatusController;
import hexlet.code.dto.TaskStatusDTO;
import lombok.SneakyThrows;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class TaskStatusControllerTest extends BaseIT {

    @SneakyThrows
    @Test
    public void testCreate() {
        getTaskStatusRepository().deleteAll();
        var statusToCreate = new TaskStatusDTO(generateStatus());

        getMockMvc().perform(
            post(TaskStatusController.PATH)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOm().writeValueAsString(statusToCreate))
        ).andExpect(status().isCreated());

        var statuses = getTaskStatusRepository().findAll();
        AssertionsForInterfaceTypes.assertThat(statuses).hasSize(1);
        AssertionsForClassTypes.assertThat(statuses.get(0))
            .matches(s -> s.getName().equals(statusToCreate.getName().get()), "taskStatus.title")
            .matches(s -> s.getSlug().equals(statusToCreate.getSlug().get()), "taskStatus.slug")
            .matches(s -> s.getCreatedAt().equals(LocalDate.now()), "taskStatus.createdAt");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdStatus = createStatus();

        var statusForUpdate = generateStatus();

        getMockMvc().perform(
            put(TaskStatusController.PATH + "/" + createdStatus.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOm().writeValueAsString(statusForUpdate))
        ).andExpect(status().isOk());

        var updatedStatus = getTaskStatusRepository().findById(createdStatus.getId()).orElseThrow();
        AssertionsForClassTypes.assertThat(updatedStatus)
            .matches(s -> s.getName().equals(statusForUpdate.getName()), "taskStatus.title")
            .matches(s -> s.getSlug().equals(statusForUpdate.getSlug()), "taskStatus.slug");
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var createdStatus = createStatus();

        var response = getMockMvc().perform(
                get(TaskStatusController.PATH + "/" + createdStatus.getId())
                    .with(jwt())
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var status = getOm().readValue(response, TaskStatusDTO.class);
        assertThat(status)
            .matches(s -> s.getName().get().equals(createdStatus.getName()), "taskStatus.title")
            .matches(s -> s.getSlug().get().equals(createdStatus.getSlug()), "taskStatus.slug");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var statusToCreate = Instancio.of(getModelGenerator().getTaskStatusModel())
            .stream()
            .limit(5)
            .toList();
        getTaskStatusRepository().saveAll(statusToCreate);

        var response = getMockMvc().perform(get(TaskStatusController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var statuses = getOm().readValue(response, List.class);
        assertThat(statuses).hasSize(6);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        getMockMvc().perform(delete(TaskStatusController.PATH + "/" + getTestStatus().getId())
                .with(jwt()))
            .andExpect(status().isNoContent());

        assertThat(getTaskStatusRepository().count()).isZero();
    }
}
