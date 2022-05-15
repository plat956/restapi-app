package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TagControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

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