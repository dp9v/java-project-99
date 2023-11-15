package hexlet.code.app.utils;

import hexlet.code.app.dtos.LabelTO;
import hexlet.code.app.dtos.TaskStatusTO;
import hexlet.code.app.dtos.TaskTO;
import hexlet.code.app.dtos.UserTO;
import hexlet.code.app.models.Label;
import hexlet.code.app.models.Task;
import hexlet.code.app.models.TaskStatus;
import hexlet.code.app.models.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ModelGenerator {
    private Model<User> userModel;
    private Model<UserTO> userTOModel;

    private Model<TaskStatus> taskStatusModel;
    private Model<TaskStatusTO> taskStatusTOModel;

    private Model<Task> taskModel;
    private Model<TaskTO> taskTOModel;

    private Model<Label> labelModel;
    private Model<LabelTO> labelTOModel;

    @PostConstruct
    private void init() {
        var faker = new Faker();
        userModel = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
            .supply(Select.field(User::getLastName), () -> faker.name().lastName())
            .supply(Select.field(User::getPassword), () -> faker.internet().password())
            .toModel();

        userTOModel = Instancio.of(UserTO.class)
            .ignore(Select.field(UserTO::createdAt))
            .ignore(Select.field(UserTO::updatedAt))
            .ignore(Select.field(UserTO::id))
            .supply(Select.field(UserTO::email), () -> faker.internet().emailAddress())
            .supply(Select.field(UserTO::firstName), () -> faker.name().firstName())
            .supply(Select.field(UserTO::lastName), () -> faker.name().lastName())
            .supply(Select.field(UserTO::password), () -> faker.internet().password())
            .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
            .ignore(Select.field(TaskStatus::getId))
            .supply(Select.field(TaskStatus::getName), () -> faker.animal().name())
            .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
            .toModel();

        taskStatusTOModel = Instancio.of(TaskStatusTO.class)
            .ignore(Select.field(TaskStatusTO::id))
            .ignore(Select.field(TaskStatusTO::createdAt))
            .supply(Select.field(TaskStatusTO::name), () -> faker.animal().name())
            .supply(Select.field(TaskStatusTO::slug), () -> faker.internet().slug())
            .toModel();

        taskModel = Instancio.of(Task.class)
            .ignore(Select.field(Task::getId))
            .supply(Select.field(Task::getName), () -> faker.videoGame().title())
            .supply(Select.field(Task::getDescription), () -> faker.videoGame().platform())
            .toModel();

        taskTOModel = Instancio.of(TaskTO.class)
            .ignore(Select.field(TaskTO::id))
            .supply(Select.field(TaskTO::title), () -> faker.videoGame().title())
            .supply(Select.field(TaskTO::content), () -> faker.videoGame().platform())
            .toModel();

        labelModel = Instancio.of(Label.class)
            .ignore(Select.field(Label::getId))
            .supply(Select.field(Label::getName), () -> faker.animal().name())
            .toModel();

        labelTOModel = Instancio.of(LabelTO.class)
            .ignore(Select.field(LabelTO::id))
            .supply(Select.field(LabelTO::name), () -> faker.animal().name())
            .toModel();
    }
}
