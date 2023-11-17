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
            new Label()
                .setName(label.name())
                .setCreatedAt(LocalDate.now())
        );
    }

    public Label update(Long id, LabelDTO labelDTO) {
        var label = labelRepository.findById(id)
            .map(l -> l.setName(labelDTO.name()))
            .orElseThrow();
        return labelRepository.save(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
