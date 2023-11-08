package hexlet.code.app.utils;

import hexlet.code.app.dtos.TaskStatusTO;
import hexlet.code.app.dtos.UserTO;
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
    }
}
