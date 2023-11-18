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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class LabelsControllerTest extends BaseIT {

    @SneakyThrows
    @Test
    public void testCreate() {
        labelRepository.deleteAll();
        var labelToCreate = new LabelDTO(
            Instancio.of(modelGenerator.getLabelModel()).create()
        );
        mockMvc.perform(
            post(LabelsController.PATH)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(labelToCreate))
        ).andExpect(status().isCreated());

        var labels = labelRepository.findAll();
        assertThat(labels).hasSize(1);
        assertThat(labels.get(0))
            .matches(s -> labelToCreate.getName().get().equals(s.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdLabel = createLabel();

        var labelForUpdate = new LabelDTO(
            Instancio.of(modelGenerator.getLabelModel()).create()
        );

        mockMvc.perform(
            put(LabelsController.PATH + "/" + createdLabel.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(labelForUpdate))
        ).andExpect(status().isOk());

        var updatedLabel = labelRepository.findById(createdLabel.getId()).orElseThrow();
        assertThat(updatedLabel)
            .matches(s -> labelForUpdate.getName().get().equals(s.getName()), "label.name");
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
            .matches(s -> s.getName().get().equals(createdLabel.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var labelsToCreate = Instancio.of(modelGenerator.getLabelModel())
            .stream()
            .limit(3)
            .toList();
        labelRepository.saveAll(labelsToCreate);

        var response = mockMvc.perform(get(LabelsController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var labels = om.readValue(response, List.class);
        assertThat(labels).hasSize(4);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        mockMvc.perform(delete(LabelsController.PATH + "/" + testLabel.getId()).with(jwt()))
            .andExpect(status().isNoContent());

        assertThat(labelRepository.count()).isZero();
    }

}
