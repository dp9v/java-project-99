package hexlet.code.service;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository repository;
    private final TaskStatusMapper mapper;

    public List<TaskStatusDTO> getAll() {
        return mapper.map(
            repository.findAll()
        );
    }

    public TaskStatusDTO getById(Long id) {
        return mapper.map(
            repository.findById(id).orElseThrow()
        );
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public TaskStatusDTO create(TaskStatusDTO status) {
        var statusToCreate = mapper.update(status, new TaskStatus())
                .setCreatedAt(LocalDate.now());
        return mapper.map(
            repository.save(statusToCreate)
        );
    }

    public TaskStatusDTO update(Long id, TaskStatusDTO statusTO) {
        var statusToUpdate = repository.findById(id)
                .map(s -> mapper.update(statusTO, s))
                .orElseThrow();
        return mapper.map(
            repository.save(statusToUpdate)
        );
    }
}
