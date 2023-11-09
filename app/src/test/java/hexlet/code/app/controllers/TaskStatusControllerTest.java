package hexlet.code.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dtos.TaskStatusTO;
import hexlet.code.app.models.TaskStatus;
import hexlet.code.app.repositories.TaskStatusRepository;
import hexlet.code.app.utils.ModelGenerator;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

@SpringBootTest
@AutoConfigureMockMvc
public final class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private ObjectMapper om;

    @AfterEach
    public void clear() {
        taskStatusRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void testCreate() {
        var statusToCreate = Instancio.of(modelGenerator.getTaskStatusTOModel())
                .create();
        mockMvc.perform(
                post(TaskStatusController.PATH)
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(statusToCreate))
        ).andExpect(status().isCreated());

        var statuses = taskStatusRepository.findAll();
        assertThat(statuses).hasSize(1);
        assertThat(statuses.get(0))
                .matches(s -> s.getName().equals(statusToCreate.name()), "taskStatus.title")
                .matches(s -> s.getSlug().equals(statusToCreate.slug()), "taskStatus.slug")
                .matches(s -> s.getCreatedAt().equals(LocalDate.now()), "taskStatus.createdAt");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdStatus = createStatus();

        var statusForUpdate = Instancio.of(modelGenerator.getTaskStatusTOModel())
                .create();

        mockMvc.perform(
                put(TaskStatusController.PATH + "/" + createdStatus.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(statusForUpdate))
        ).andExpect(status().isOk());

        var updatedStatus = taskStatusRepository.findById(createdStatus.getId()).orElseThrow();
        assertThat(updatedStatus)
                .matches(s -> s.getName().equals(statusForUpdate.name()), "taskStatus.title")
                .matches(s -> s.getSlug().equals(statusForUpdate.slug()), "taskStatus.slug");
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

        var status = om.readValue(response, TaskStatusTO.class);
        assertThat(status)
                .matches(s -> s.name().equals(createdStatus.getName()), "taskStatus.title")
                .matches(s -> s.slug().equals(createdStatus.getSlug()), "taskStatus.slug");
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
        assertThat(statuses).hasSize(5);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        var createdStatus = createStatus();
        mockMvc.perform(delete(TaskStatusController.PATH + "/" + createdStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.count()).isZero();
    }

    private TaskStatus createStatus() {
        var status = Instancio.of(modelGenerator.getTaskStatusModel())
                .create();
        return taskStatusRepository.save(status);
    }
}
