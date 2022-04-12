package com.epam.esm.controller;

import com.epam.esm.config.profile.TestProfileConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestProfileConfig.class)
@ActiveProfiles("test")
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeAll
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates"))
                .andExpect(handler().handlerType(GiftCertificateController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllParametrized() throws Exception {
        String tag = "online";
        String sort = "+name,-createDate";
        String search = "ificate";

        mockMvc.perform(MockMvcRequestBuilders.get("/certificates")
                .param("tag", tag)
                .param("sort", sort)
                .param("search", search))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].tags[?(@.name == \'online\')]").exists())
                .andExpect(jsonPath("$[*].name", containsInRelativeOrder("Evroopt delivery certificate", "Gym Minsk certificate", "New Year certificate")))
                .andExpect(jsonPath("$[*].createDate", containsInRelativeOrder("2022-01-06T14:29:04", "2021-01-01T20:48:32", "2020-12-06T14:29:04")));
    }

    @ParameterizedTest
    @CsvSource("3, Gym Minsk certificate")
    void getOne(Long id, String expectedCertificateName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedCertificateName));
    }

    @ParameterizedTest
    @ValueSource(longs = 999L)
    void getOneNotFound(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void save() throws Exception {
        GiftCertificate c = new GiftCertificate();
        c.setName("new cert");
        c.setDescription("descr of the new cert");
        c.setDuration(2);
        c.setPrice(BigDecimal.TEN);

        Tag existedTag = new Tag();
        existedTag.setName("online");
        Tag newTag = new Tag();
        newTag.setName("tv");

        c.getTags().add(existedTag);
        c.getTags().add(newTag);

        mockMvc.perform(MockMvcRequestBuilders.post("/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.createDate").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.lastUpdateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.tags.[*].name", containsInAnyOrder("online", "tv")));
    }

    @ParameterizedTest
    @CsvSource("3, updated descr, 100.78")
    void update(Long id, String newDescription, BigDecimal newPrice) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastUpdateDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.tags[?(@.name == \'holiday\')]").doesNotExist())
                .andExpect(jsonPath("$.tags[?(@.name == \'online\')]").exists());

        GiftCertificate c = new GiftCertificate();
        c.setDescription(newDescription);
        c.setPrice(newPrice);

        Tag additionalTag = new Tag();
        additionalTag.setName("holiday");

        c.getTags().add(additionalTag);

        mockMvc.perform(MockMvcRequestBuilders.patch("/certificates/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastUpdateDate").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.description").value(newDescription))
                .andExpect(jsonPath("$.price").value(newPrice))
                .andExpect(jsonPath("$.tags[?(@.name == \'holiday\')]").exists())
                .andExpect(jsonPath("$.tags[?(@.name == \'online\')]").exists());
    }

    @ParameterizedTest
    @ValueSource(longs = 4L)
    void delete(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/certificates/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(result -> this.getOneNotFound(id));
    }
}