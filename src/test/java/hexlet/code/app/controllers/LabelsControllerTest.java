package hexlet.code.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dtos.LabelTO;
import hexlet.code.app.models.Label;
import hexlet.code.app.repositories.LabelsRepository;
import hexlet.code.app.utils.ModelGenerator;
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
    private LabelsRepository labelsRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private ObjectMapper om;

    @AfterEach
    public void clear() {
        labelsRepository.deleteAll();
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

        var labels = labelsRepository.findAll();
        assertThat(labels).hasSize(1);
        assertThat(labels.get(0))
                .matches(s -> s.getName().equals(labelToCreate.name()), "label.name");
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

        var updatedLabel = labelsRepository.findById(createdLabel.getId()).orElseThrow();
        assertThat(updatedLabel)
            .matches(s -> s.getName().equals(labelForUpdate.name()), "label.name");
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

        var label = om.readValue(response, LabelTO.class);
        assertThat(label)
                .matches(s -> s.name().equals(createdLabel.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var labelsToCreate = Instancio.of(modelGenerator.getLabelModel())
                .stream()
                .limit(3)
                .toList();
        labelsRepository.saveAll(labelsToCreate);

        var response = mockMvc.perform(get(LabelsController.PATH).with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var labels = om.readValue(response, List.class);
        assertThat(labels).hasSize(3);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        var createdLabel = createLabel();
        mockMvc.perform(delete(LabelsController.PATH + "/" + createdLabel.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(labelsRepository.count()).isZero();
    }

    private Label createLabel() {
        var label = Instancio.of(modelGenerator.getLabelModel())
                .create();
        return labelsRepository.save(label);
    }
}
