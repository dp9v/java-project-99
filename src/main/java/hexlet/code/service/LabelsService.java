package hexlet.code.service;

import hexlet.code.dto.LabelDTO;
import hexlet.code.mapper.LabelMapper;
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
    private final LabelMapper labelMapper;

    public LabelDTO getById(Long id) {
        return labelMapper.map(
            labelRepository.findById(id)
                .orElseThrow()
        );
    }

    public List<LabelDTO> getAll() {
        return labelMapper.map(
            labelRepository.findAll()
        );
    }

    public LabelDTO create(LabelDTO label) {
        var updatedLabel = labelMapper.update(label, new Label())
            .setCreatedAt(LocalDate.now());
        return labelMapper.map(
            labelRepository.save(updatedLabel)
        );
    }

    public LabelDTO update(Long id, LabelDTO label) {
        var updatedLabel = labelMapper.update(
            label,
            labelRepository.findById(id).orElseThrow()
        );
        return labelMapper.map(
            labelRepository.save(updatedLabel)
        );
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
