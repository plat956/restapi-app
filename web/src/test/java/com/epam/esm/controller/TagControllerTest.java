package com.epam.esm.controller;

import com.epam.esm.config.profile.H2TestProfileConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2TestProfileConfig.class)
@ActiveProfiles("h2test")
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagControllerTest {

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
        mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(handler().handlerType(TagController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    void getOne(Long id) throws Exception {
        String expectedTagName = "holiday";

        mockMvc.perform(MockMvcRequestBuilders.get("/tags/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedTagName));
    }

    @ParameterizedTest
    @ValueSource(longs = 999L)
    void getOneNotFound(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tags/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @ParameterizedTest
    @ValueSource(strings = "culture")
    void save(String tagName) throws Exception {
        Tag tag = new Tag();
        tag.setName(tagName);

        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(tagName));
    }

    @ParameterizedTest
    @ValueSource(strings = "sport")
    void saveDuplicate(String tagName) throws Exception {
        Tag tag = new Tag();
        tag.setName(tagName);

        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceDuplicateException));
    }

    @ParameterizedTest
    @ValueSource(longs = 6L)
    void delete(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tags/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/tags/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(result -> this.getOneNotFound(id));
    }
}