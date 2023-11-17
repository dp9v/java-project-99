package hexlet.code.controller;

import hexlet.code.dto.TaskFilterRequest;
import hexlet.code.dto.TaskDTO;
import hexlet.code.service.TaskService;
import hexlet.code.utils.ResponseEntityBuilder;
import hexlet.code.utils.TaskSpecificationBuilder;
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
    public ResponseEntity<List<TaskDTO>> getAll(TaskFilterRequest filter) {
        return ResponseEntityBuilder.build(
                taskService.getAll(taskSpecificationBuilder.build(filter)),
                TaskDTO::new
        );
    }

    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return new TaskDTO(
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
    public TaskDTO create(@RequestBody TaskDTO taskStatus) {
        return new TaskDTO(
                taskService.create(taskStatus)
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@PathVariable Long id, @RequestBody TaskDTO task) {
        return new TaskDTO(
                taskService.update(id, task)
        );
    }
}
