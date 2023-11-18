package hexlet.code.service;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelsService {
    private final LabelRepository labelRepository;

    public Label getById(Long id) {
        return labelRepository.findById(id)
            .orElseThrow();
    }

    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    public Label create(LabelDTO label) {
        return labelRepository.save(
            merge(new Label(), label)
                .setCreatedAt(LocalDate.now())
        );
    }

    public Label update(Long id, LabelDTO labelDTO) {
        return labelRepository.save(
            merge(
                labelRepository.findById(id).orElseThrow(),
                labelDTO
            )
        );
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }

    private Label merge(Label target, LabelDTO labelDTO) {
        labelDTO.getName().ifPresent(target::setName);
        return target;
    }
}
