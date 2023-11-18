package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import lombok.Getter;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Getter
public class BaseIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private ObjectMapper om;

    private Label testLabel;
    private TaskStatus testStatus;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testStatus = createStatus();
        testUser = createUser();
        testLabel = createLabel();
    }

    @AfterEach
    public void clear() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
    }

    protected User createUser() {
        return userRepository.save(
            generateUser()
        );
    }

    protected User generateUser() {
        return Instancio.of(modelGenerator.getUserModel())
            .create();
    }


    protected TaskStatus createStatus() {
        return taskStatusRepository.save(
            generateStatus()
        );
    }

    protected TaskStatus generateStatus() {
        return Instancio.of(modelGenerator.getTaskStatusModel())
            .create();
    }

    protected Task createTask() {
        return taskRepository.save(generateTask());
    }

    protected Task generateTask() {
        return Instancio.of(modelGenerator.getTaskModel())
            .set(Select.field(Task::getTaskStatus), testStatus)
            .set(Select.field(Task::getAssignee), testUser)
            .set(Select.field(Task::getLabels), Set.of(testLabel))
            .create();
    }


    protected Label createLabel() {
        return labelRepository.save(generateLabel());
    }

    protected Label generateLabel() {
        return Instancio.of(modelGenerator.getLabelModel())
            .create();
    }
}
