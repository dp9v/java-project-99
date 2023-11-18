package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
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
        source.getTaskStatusSlug().ifPresent(s-> target.setTaskStatus(
            taskStatusRepository.findByName(s).orElseThrow()
        ));
        source.getTaskLabelIds().ifPresent(labels -> target.setLabels(
            labels.stream()
                .map(id -> new Label().setId(id))
                .collect(Collectors.toSet())
        ));
        source.getName().ifPresent(target::setName);
        source.getDescription().ifPresent(target::setDescription);
        source.getIndex().ifPresent(target::setIndex);
        source.getAssigneeId().ifPresent(id -> target.setAssignee(
            new User().setId(id)
        ));
        return target;
    }
}
