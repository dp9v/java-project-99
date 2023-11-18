package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.TaskStatusController;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import lombok.SneakyThrows;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
        taskStatusRepository.deleteAll();
        var statusToCreate = new TaskStatusDTO(generateStatus());

        mockMvc.perform(
                post(TaskStatusController.PATH)
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(statusToCreate))
        ).andExpect(status().isCreated());

        var statuses = taskStatusRepository.findAll();
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

        mockMvc.perform(
                put(TaskStatusController.PATH + "/" + createdStatus.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(statusForUpdate))
        ).andExpect(status().isOk());

        var updatedStatus = taskStatusRepository.findById(createdStatus.getId()).orElseThrow();
        AssertionsForClassTypes.assertThat(updatedStatus)
                .matches(s -> s.getName().equals(statusForUpdate.getName()), "taskStatus.title")
                .matches(s -> s.getSlug().equals(statusForUpdate.getSlug()), "taskStatus.slug");
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var createdStatus = createStatus();

        var response = mockMvc.perform(
                        get(TaskStatusController.PATH + "/" + createdStatus.getId())
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var status = om.readValue(response, TaskStatusDTO.class);
        assertThat(status)
                .matches(s -> s.getName().get().equals(createdStatus.getName()), "taskStatus.title")
                .matches(s -> s.getSlug().get().equals(createdStatus.getSlug()), "taskStatus.slug");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var statusToCreate = Instancio.of(modelGenerator.getTaskStatusModel())
                .stream()
                .limit(5)
                .toList();
        taskStatusRepository.saveAll(statusToCreate);

        var response = mockMvc.perform(get(TaskStatusController.PATH).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var statuses = om.readValue(response, List.class);
        assertThat(statuses).hasSize(6);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        mockMvc.perform(delete(TaskStatusController.PATH + "/" + testStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.count()).isZero();
    }
}
