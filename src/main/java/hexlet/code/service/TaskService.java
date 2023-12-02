package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    public TaskDTO getById(Long id) {
        return mapper.map(
            taskRepository.findById(id).orElseThrow()
        );
    }

    public List<TaskDTO> getAll(Specification<Task> specification) {
        return mapper.map(
            taskRepository.findAll(specification)
        );
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskDTO create(TaskDTO task) {
        var taskToCreate = mapper.update(task, new Task())
            .setCreatedAt(LocalDate.now());
        return mapper.map(
            taskRepository.save(taskToCreate)
        );
    }

    public TaskDTO update(Long id, TaskDTO task) {
        var taskToUpdate = taskRepository.findById(id)
            .map(t -> mapper.update(task, t))
            .orElseThrow();
        return mapper.map(
            taskRepository.save(taskToUpdate)
        );
    }
}
