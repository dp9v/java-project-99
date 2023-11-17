package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.LabelsController;
import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.ModelGenerator;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public final class LabelsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private ObjectMapper om;

    @AfterEach
    public void clear() {
        labelRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void testCreate() {
        var labelToCreate = Instancio.of(modelGenerator.getLabelTOModel())
                .create();
        mockMvc.perform(
                post(LabelsController.PATH)
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(labelToCreate))
        ).andExpect(status().isCreated());

        var labels = labelRepository.findAll();
        assertThat(labels).hasSize(1);
        assertThat(labels.get(0))
                .matches(s -> s.getName().equals(labelToCreate.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdLabel = createLabel();

        var labelForUpdate = Instancio.of(modelGenerator.getLabelTOModel())
                .create();

        mockMvc.perform(
                put(LabelsController.PATH + "/" + createdLabel.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(labelForUpdate))
        ).andExpect(status().isOk());

        var updatedLabel = labelRepository.findById(createdLabel.getId()).orElseThrow();
        assertThat(updatedLabel)
            .matches(s -> s.getName().equals(labelForUpdate.getName()), "label.name");
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var createdLabel = createLabel();

        var response = mockMvc.perform(
                        get(LabelsController.PATH + "/" + createdLabel.getId())
                                .with(jwt())
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var label = om.readValue(response, LabelDTO.class);
        assertThat(label)
                .matches(s -> s.getName().equals(createdLabel.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var labelsToCreate = Instancio.of(modelGenerator.getLabelModel())
                .stream()
                .limit(10)
                .toList();
        labelRepository.saveAll(labelsToCreate);

        var response = mockMvc.perform(get(LabelsController.PATH).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var labels = om.readValue(response, List.class);
        assertThat(labels).hasSize(10);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        var createdLabel = createLabel();
        mockMvc.perform(delete(LabelsController.PATH + "/" + createdLabel.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(labelRepository.count()).isZero();
    }

    private Label createLabel() {
        var label = Instancio.of(modelGenerator.getLabelModel())
                .create();
        return labelRepository.save(label);
    }
}
