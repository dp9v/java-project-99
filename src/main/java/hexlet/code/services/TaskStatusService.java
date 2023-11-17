package hexlet.code.services;

import hexlet.code.dtos.TaskStatusTO;
import hexlet.code.models.TaskStatus;
import hexlet.code.repositories.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository repository;

    public List<TaskStatus> getAll() {
        return repository.findAll();
    }

    public TaskStatus getById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public TaskStatus create(TaskStatusTO statusTO) {
        var statusToCreate = merge(new TaskStatus(), statusTO)
                .setCreatedAt(LocalDate.now());
        return repository.save(statusToCreate);
    }

    public TaskStatus update(Long id, TaskStatusTO statusTO) {
        var statusToCreate = repository.findById(id)
                .map(s -> merge(s, statusTO))
                .orElseThrow();
        return repository.save(statusToCreate);
    }

    private TaskStatus merge(TaskStatus target, TaskStatusTO source) {
        if (isNotBlank(source.name())) {
            target.setName(source.name());
        }
        if (isNotBlank(source.slug())) {
            target.setSlug(source.slug());
        }
        return target;
    }
}
