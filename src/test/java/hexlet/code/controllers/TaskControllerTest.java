package hexlet.code.controllers;

import hexlet.code.controller.TaskController;
import hexlet.code.dto.TaskDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest extends BaseIT {

    @SneakyThrows
    @Test
    public void testCreate() {
        var createdTask = new TaskDTO(generateTask());

        getMockMvc().perform(post(TaskController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(getOm().writeValueAsString(createdTask))
            .with(jwt())
        ).andExpect(status().isCreated());

        var tasks = getTaskRepository().findAll();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0))
            .matches(t -> t.getName().equals(createdTask.getName().get()), "task.title")
            .matches(t -> t.getDescription().equals(createdTask.getDescription().get()), "task.description")
            .matches(t -> t.getTaskStatus().equals(getTestStatus()), "task.status")
            .matches(t -> t.getAssignee().equals(getTestUser()), "task.assignee")
            .matches(t -> t.getLabels().equals(Set.of(getTestLabel())), "task.labels");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdTask = createTask();
        var taskForUpdate = new TaskDTO(generateTask());
        getMockMvc().perform(put(TaskController.PATH + "/" + createdTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(getOm().writeValueAsString(taskForUpdate))
            .with(jwt())
        ).andExpect(status().isOk());

        var task = getTaskRepository().findById(createdTask.getId()).orElseThrow();
        assertThat(task)
            .matches(t -> t.getName().equals(taskForUpdate.getName().get()), "task.title")
            .matches(t -> t.getDescription().equals(taskForUpdate.getDescription().get()), "task.description")
            .matches(t -> t.getTaskStatus().equals(getTestStatus()), "task.status")
            .matches(t -> t.getAssignee().equals(getTestUser()), "task.assignee")
            .matches(t -> t.getLabels().equals(Set.of(getTestLabel())), "task.labels");
    }


    @SneakyThrows
    @Test
    public void testGetAll() {
        var createTasksCount = Stream.generate(this::createTask)
            .limit(5)
            .count();

        var response = getMockMvc().perform(get(TaskController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        var tasks = getOm().readValue(response, List.class);
        assertThat(tasks).hasSize((int) createTasksCount);
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var createdTask = createTask();

        var response = getMockMvc()
            .perform(get(TaskController.PATH + "/" + createdTask.getId()).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        var task = getOm().readValue(response, TaskDTO.class);
        assertThat(task)
            .matches(t -> t.getName().get().equals(createdTask.getName()), "task.title")
            .matches(t -> t.getDescription().get().equals(createdTask.getDescription()), "task.description")
            .matches(t -> t.getTaskStatusSlug().get().equals(getTestStatus().getSlug()), "task.status")
            .matches(t -> t.getAssigneeId().get().equals(getTestUser().getId()), "task.assignee")
            .matches(t -> t.getTaskLabelIds().get().equals(Set.of(getTestLabel().getId())), "task.labels");
    }

    @SneakyThrows
    @Test
    public void testDelete() {
        var createdTask = createTask();
        assertThat(getTaskRepository().count()).isOne();
        getMockMvc()
            .perform(delete(TaskController.PATH + "/" + createdTask.getId()).with(jwt()))
            .andExpect(status().isNoContent());
        assertThat(getTaskRepository().count()).isZero();
    }

}
