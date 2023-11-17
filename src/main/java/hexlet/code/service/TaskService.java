package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

    public List<Task> getAll(Specification<Task> specification) {
        return taskRepository.findAll(specification);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task create(TaskDTO taskDTO) {
        var taskToCreate = merge(new Task(), taskDTO);
        return taskRepository.save(taskToCreate);
    }

    public Task update(Long id, TaskDTO task) {
        var taskForUpdate = taskRepository.findById(id).orElseThrow();
        return taskRepository.save(
            merge(taskForUpdate, task)
        );
    }

    public Task merge(Task target, TaskDTO source) {
        if (isNotBlank(source.status())) {
            var status = taskStatusRepository.findByName(source.status()).orElseThrow();
            target.setTaskStatus(status);
        }
        if (source.taskLabelIds() != null) {
            var labels = source.taskLabelIds().stream()
                .map(id -> new Label().setId(id))
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
