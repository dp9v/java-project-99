package hexlet.code.app.controllers;

import hexlet.code.app.dtos.TaskStatusTO;
import hexlet.code.app.services.TaskStatusService;
import hexlet.code.app.utils.ResponseEntityBuilder;
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
    public ResponseEntity<List<TaskStatusTO>> getAll() {
        return ResponseEntityBuilder.build(service.getAll(), TaskStatusTO::new);
    }

    @GetMapping("/{id}")
    public TaskStatusTO getById(@PathVariable Long id) {
        return new TaskStatusTO(
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
    public TaskStatusTO create(@RequestBody TaskStatusTO taskStatus) {
        return new TaskStatusTO(
                service.create(taskStatus)
        );
    }

    @PutMapping("/{id}")
    public TaskStatusTO update(@PathVariable Long id, @RequestBody TaskStatusTO taskStatus) {
        return new TaskStatusTO(
                service.update(id, taskStatus)
        );
    }
}
