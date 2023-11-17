package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.service.TaskStatusService;
import hexlet.code.utils.ResponseEntityBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(TaskStatusController.PATH)
public final class TaskStatusController {
    public static final String PATH = "/api/task_statuses";

    private final TaskStatusService service;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> getAll() {
        return ResponseEntityBuilder.build(service.getAll(), TaskStatusDTO::new);
    }

    @GetMapping("/{id}")
    public TaskStatusDTO getById(@PathVariable Long id) {
        return new TaskStatusDTO(
                service.getById(id)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@RequestBody TaskStatusDTO taskStatus) {
        return new TaskStatusDTO(
                service.create(taskStatus)
        );
    }

    @PutMapping("/{id}")
    public TaskStatusDTO update(@PathVariable Long id, @RequestBody TaskStatusDTO taskStatus) {
        return new TaskStatusDTO(
                service.update(id, taskStatus)
        );
    }
}
