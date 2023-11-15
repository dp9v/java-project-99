package hexlet.code.app.services;

import hexlet.code.app.dtos.TaskTO;
import hexlet.code.app.models.Label;
import hexlet.code.app.models.Task;
import hexlet.code.app.models.User;
import hexlet.code.app.repositories.TaskRepository;
import hexlet.code.app.repositories.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task create(TaskTO taskTO) {
        var taskToCreate = merge(new Task(), taskTO);
        return taskRepository.save(taskToCreate);
    }

    public Task update(Long id, TaskTO task) {
        var taskForUpdate = taskRepository.findById(id).orElseThrow();
        return taskRepository.save(
            merge(taskForUpdate, task)
        );
    }

    public Task merge(Task target, TaskTO source) {
        if (isNotBlank(source.status())) {
            var status = taskStatusRepository.findByName(source.status()).orElseThrow();
            target.setTaskStatus(status);
        }
        if(source.taskLabelIds() != null) {
            var labels = source.taskLabelIds().stream()
                .map(id->new Label().setId(id))
                .collect(Collectors.toSet());
            target.setLabels(labels);
        }
        if (isNotBlank(source.title())) {
            target.setName(source.title());
        }
        if (isNotBlank(source.content())) {
            target.setDescription(source.content());
        }
        if (source.index() != null) {
            target.setIndex(source.index());
        }
        if (source.assigneeId() != null) {
            target.setAssignee(new User().setId(source.assigneeId()));
        }
        return target;
    }
}
