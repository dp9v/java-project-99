package hexlet.code.service;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelsService {
    private final LabelsRepository labelsRepository;

    public Label getById(Long id) {
        return labelsRepository.findById(id)
            .orElseThrow();
    }

    public List<Label> getAll() {
        return labelsRepository.findAll();
    }

    public Label create(LabelDTO label) {
        return labelsRepository.save(
            new Label()
                .setName(label.name())
                .setCreatedAt(LocalDate.now())
        );
    }

    public Label update(Long id, LabelDTO labelDTO) {
        var label = labelsRepository.findById(id)
            .map(l -> l.setName(labelDTO.name()))
            .orElseThrow();
        return labelsRepository.save(label);
    }

    public void delete(Long id) {
        labelsRepository.deleteById(id);
    }
}
