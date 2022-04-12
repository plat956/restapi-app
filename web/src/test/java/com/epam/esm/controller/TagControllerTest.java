package com.epam.esm.controller;

import com.epam.esm.config.profile.H2TestProfileConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2TestProfileConfig.class)
@ActiveProfiles("h2test")
@WebAppConfiguration
class TagControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(handler().handlerType(TagController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk());
    }

    @Test
    void getOne() throws Exception {
        Long tagId = 1L;
        String expectedTagName = "holiday";

        mockMvc.perform(MockMvcRequestBuilders.get("/tags/{id}", tagId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedTagName));
    }

    @Test
    void getOneNotFound() throws Exception {
        Long tagId = 999L;

        mockMvc.perform(MockMvcRequestBuilders.get("/tags/{id}", tagId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void save() throws Exception {
        String tagName = "culture";

        Tag tag = new Tag();
        tag.setName(tagName);

        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(tagName));
    }

    @Test
    void saveDuplicate() throws Exception {
        Tag tag = new Tag();
        tag.setName("sport");

        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceDuplicateException));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tags/{id}", 10L))
                .andExpect(status().isNoContent());
    }
}