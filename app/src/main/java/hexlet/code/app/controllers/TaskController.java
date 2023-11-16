package hexlet.code.app.controllers;

import hexlet.code.app.dtos.TaskFilterRequest;
import hexlet.code.app.dtos.TaskTO;
import hexlet.code.app.services.TaskService;
import hexlet.code.app.utils.TaskSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(TaskController.PATH)
public class TaskController {
    public static final String PATH = "/api/tasks";

    private final TaskService taskService;
    private final TaskSpecificationBuilder taskSpecificationBuilder;

    @GetMapping
    public ResponseEntity<List<TaskTO>> getAll(TaskFilterRequest filter) {
        var result = taskService.getAll(taskSpecificationBuilder.build(filter))
                .stream()
                .map(TaskTO::new)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping("/{id}")
    public TaskTO getById(@PathVariable Long id) {
        return new TaskTO(
                taskService.getById(id)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskTO create(@RequestBody TaskTO taskStatus) {
        return new TaskTO(
                taskService.create(taskStatus)
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskTO update(@PathVariable Long id, @RequestBody TaskTO task) {
        return new TaskTO(
                taskService.update(id, task)
        );
    }
}
