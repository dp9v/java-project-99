package hexlet.code.app.services;

import hexlet.code.app.dtos.LabelTO;
import hexlet.code.app.models.Label;
import hexlet.code.app.repositories.LabelsRepository;
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

    public Label create(LabelTO label) {
        return labelsRepository.save(
            new Label()
                .setName(label.name())
                .setCreatedAt(LocalDate.now())
        );
    }

    public Label update(Long id, LabelTO labelTO) {
        var label = labelsRepository.findById(id)
            .map(l -> l.setName(labelTO.name()))
            .orElseThrow();
        return labelsRepository.save(label);
    }

    public void delete(Long id) {
        labelsRepository.deleteById(id);
    }
}
