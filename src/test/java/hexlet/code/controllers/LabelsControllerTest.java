package hexlet.code.controllers;

import hexlet.code.controller.LabelsController;
import hexlet.code.dto.LabelDTO;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

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
        getLabelRepository().deleteAll();
        var labelToCreate = new LabelDTO(
            Instancio.of(getModelGenerator().getLabelModel()).create()
        );
        getMockMvc().perform(
            post(LabelsController.PATH)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOm().writeValueAsString(labelToCreate))
        ).andExpect(status().isCreated());

        var labels = getLabelRepository().findAll();
        assertThat(labels).hasSize(1);
        assertThat(labels.get(0))
            .matches(s -> labelToCreate.getName().get().equals(s.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        var createdLabel = createLabel();

        var labelForUpdate = new LabelDTO(
            Instancio.of(getModelGenerator().getLabelModel()).create()
        );

        getMockMvc().perform(
            put(LabelsController.PATH + "/" + createdLabel.getId())
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getOm().writeValueAsString(labelForUpdate))
        ).andExpect(status().isOk());

        var updatedLabel = getLabelRepository().findById(createdLabel.getId()).orElseThrow();
        assertThat(updatedLabel)
            .matches(s -> labelForUpdate.getName().get().equals(s.getName()), "label.name");
    }

    @SneakyThrows
    @Test
    public void testGet() {
        var createdLabel = createLabel();

        var response = getMockMvc().perform(
                get(LabelsController.PATH + "/" + createdLabel.getId())
                    .with(jwt())
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var label = getOm().readValue(response, LabelDTO.class);
        assertThat(label)
            .matches(s -> s.getName().get().equals(createdLabel.getName()), "label.name");
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        var labelsToCreate = Instancio.of(getModelGenerator().getLabelModel())
            .stream()
            .limit(3)
            .toList();
        getLabelRepository().saveAll(labelsToCreate);

        var response = getMockMvc().perform(get(LabelsController.PATH).with(jwt()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var labels = getOm().readValue(response, List.class);
        assertThat(labels).hasSize(4);
    }


    @Test
    @SneakyThrows
    public void testDelete() {
        getMockMvc().perform(delete(LabelsController.PATH + "/" + getTestLabel().getId()).with(jwt()))
            .andExpect(status().isNoContent());

        assertThat(getLabelRepository().count()).isZero();
    }

}
