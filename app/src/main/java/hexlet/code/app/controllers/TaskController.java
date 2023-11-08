package hexlet.code.app.controllers;

import hexlet.code.app.dtos.TaskTO;
import hexlet.code.app.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(TaskController.PATH)
public class TaskController {
    public static final String PATH = "/api/tasks";

    private final TaskService taskService;

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
