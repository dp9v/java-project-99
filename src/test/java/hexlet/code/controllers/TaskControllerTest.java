package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.TaskController;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;
    private TaskStatus testStatus;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testStatus = taskStatusRepository.save(
            Instancio.of(modelGenerator.getTaskStatusModel()).create()
        );
        testUser = userRepository.save(
            Instancio.of(modelGenerator.getUserModel()).create()
        );
        testLabel = labelRepository.save(
            Instancio.of(modelGenerator.getLabelModel()).create()
        );
    }

    @AfterEach
    public void clear() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void testCreate() {
        var createdTask = Instancio.of(modelGenerator.getTaskTOModel())
            .set(Select.field(TaskDTO::getTaskStatusSlug), testStatus.getName())
            .set(Select.field(TaskDTO::getAssigneeId), testUser.getId())
            .set(Select.field(TaskDTO::getTaskLabelIds), Set.of(testLabel.getId()))
            .create();

        mockMvc.perform(post(TaskController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(createdTask))
            .with(jwt())
        ).andExpect(status().isCreated());

        var tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0))
            .matches(t -> t.getName().equals(createdTask.getName()), "task.title")
            .matches(t -> t.getDescription().equals(createdTask.getDescription()), "task.description")
            .matches(t -> t.getTaskStatus().equals(testStatus), "task.status")
            .matches(t -> t.getAssignee().equals(testUser), "task.assignee")
            .matches(t -> t.getLabels().equals(Set.of(testLabel)), "task.labels");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdTask = createTask();

        var taskForUpdate = Instancio.of(modelGenerator.getTaskTOModel())
            .set(Select.field(TaskDTO::getTaskStatusSlug), null)
            .set(Select.field(TaskDTO::getAssigneeId), testUser.getId())
            .set(Select.field(TaskDTO::getTaskLabelIds), Set.of())
            .create();
        mockMvc.perform(put(TaskController.PATH + "/" + createdTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(taskForUpdate))
            .with(jwt())
        ).andExpect(status().isOk());

        var task = taskRepository.findById(createdTask.getId()).orElseThrow();
        assertThat(task)
            .matches(t -> t.getName().equals(taskForUpdate.getName()), "task.title")
            .matches(t -> t.getDescription().equals(taskForUpdate.getDescription()), "task.description")
            .matches(t -> t.getTaskStatus().equals(testStatus), "task.status")
            .matches(t -> t.getAssignee().equals(testUser), "task.assignee")
            .matches(t -> t.getLabels().isEmpty(), "task.labels");
    }


    @SneakyThrows
    @Test
    public void testGetAll() {
        var createTasksCount = Stream.generate(this::createTask)
            .limit(5)
            .count();

        var response = mockMvc.perform(get(TaskController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        var tasks = om.readValue(response, List.class);
        assertThat(tasks).hasSize((int) createTasksCount);
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var createdTask = createTask();

        var response = mockMvc
            .perform(get(TaskController.PATH + "/" + createdTask.getId()).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        var task = om.readValue(response, TaskDTO.class);
        assertThat(task)
            .matches(t -> t.getName().equals(createdTask.getName()), "task.title")
            .matches(t -> t.getDescription().equals(createdTask.getDescription()), "task.description")
            .matches(t -> t.getTaskStatusSlug().equals(testStatus.getName()), "task.status")
            .matches(t -> t.getAssigneeId().equals(testUser.getId()), "task.assignee")
            .matches(t -> t.getTaskLabelIds().equals(Set.of(testLabel.getId())), "task.labels");
    }

    @SneakyThrows
    @Test
    public void testDelete() {
        var createdTask = createTask();
        assertThat(taskRepository.count()).isOne();
        mockMvc
            .perform(delete(TaskController.PATH + "/" + createdTask.getId()).with(jwt()))
            .andExpect(status().isNoContent());
        assertThat(taskRepository.count()).isZero();
    }

    private Task createTask() {
        var taskToCreate = Instancio.of(modelGenerator.getTaskModel())
            .set(Select.field(Task::getTaskStatus), testStatus)
            .set(Select.field(Task::getAssignee), testUser)
            .set(Select.field(Task::getLabels), Set.of(testLabel))
            .create();
        return taskRepository.save(taskToCreate);
    }
}
