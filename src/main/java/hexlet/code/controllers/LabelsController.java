package hexlet.code.controllers;

import hexlet.code.dtos.LabelTO;
import hexlet.code.services.LabelsService;
import hexlet.code.utils.ResponseEntityBuilder;
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
@RequestMapping(LabelsController.PATH)
public final class LabelsController {
    public static final String PATH = "/api/labels";

    private final LabelsService labelsService;


    @GetMapping
    public ResponseEntity<List<LabelTO>> getAll() {
        return ResponseEntityBuilder.build(
                labelsService.getAll(),
                LabelTO::new
        );
    }

    @GetMapping("/{id}")
    public LabelTO getById(@PathVariable Long id) {
        return new LabelTO(
            labelsService.getById(id)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelTO create(@RequestBody LabelTO label) {
        return new LabelTO(
            labelsService.create(label)
        );
    }

    @PutMapping("/{id}")
    public LabelTO update(@PathVariable Long id, @RequestBody LabelTO label) {
        return new LabelTO(
            labelsService.update(id, label)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        labelsService.delete(id);
    }
}
