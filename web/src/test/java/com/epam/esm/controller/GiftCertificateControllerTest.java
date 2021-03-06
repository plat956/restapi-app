package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GiftCertificateControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates"))
                .andExpect(handler().handlerType(GiftCertificateController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    @WithAnonymousUser
    void getAllParametrized() throws Exception {
        String tags = "online";
        String sorts = "+name,-createDate";
        String search = "ificate";

        mockMvc.perform(MockMvcRequestBuilders.get("/certificates")
                .param("tags", tags)
                .param("sorts", sorts)
                .param("search", search))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.certificates[*].tags[?(@.name == \'online\')]").exists())
                .andExpect(jsonPath("$._embedded.certificates[*].name", containsInRelativeOrder("Evroopt delivery certificate", "Gym Minsk certificate", "New Year certificate")))
                .andExpect(jsonPath("$._embedded.certificates[*].createDate", containsInRelativeOrder("2022-01-06T14:29:04", "2021-01-01T20:48:32", "2020-12-06T14:29:04")))
                .andExpect(jsonPath("$.page").exists());
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @CsvSource("4, 7")
    @WithUserDetails(value = "test-admin", userDetailsServiceBeanName = "jwtUserDetailsService")
    void unbindTag(Long certId, Long tagId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/{id}", certId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[?(@.id == " + tagId + ")]").exists());

        mockMvc.perform(MockMvcRequestBuilders.delete("/certificates/{certId}/tags/{tagId}", certId, tagId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[?(@.id == " + tagId + ")]").doesNotExist());
    }

    @ParameterizedTest
    @ValueSource(longs = 5L)
    @WithUserDetails(value = "test-admin", userDetailsServiceBeanName = "jwtUserDetailsService")
    void delete(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/certificates/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/certificates/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(result -> this.getOneNotFound(id));
    }
}