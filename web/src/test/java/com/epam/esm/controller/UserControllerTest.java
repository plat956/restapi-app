package com.epam.esm.controller;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(longs = 2L)
    void getOneUser(Long id) throws Exception {
        String expectedLogin = "ivan";

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(expectedLogin));
    }

    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .param("page", "2")
                .param("limit", "100"))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalPages").value(1));
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    void getAllOrders(Long userId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/orders", userId))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getAllOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists());
    }

    @ParameterizedTest
    @ValueSource(longs = 2L)
    void createOrder(Long userId) throws Exception {
        JSONObject certificates = new JSONObject();
        certificates.appendField("certificates",
                new JSONArray().appendElement(2)
                        .appendElement(2)
                        .appendElement(1));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{id}/orders", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(certificates.toJSONString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cost").value(351.00));
    }
}