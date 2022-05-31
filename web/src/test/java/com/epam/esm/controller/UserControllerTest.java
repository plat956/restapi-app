package com.epam.esm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
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
                .param("size", "100"))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalPages").value(1));
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    @WithUserDetails(value = "qwerty", userDetailsServiceBeanName = "jwtUserDetailsService")
    void getAllOrders(Long userId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/orders", userId))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getAllOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").exists());
    }
}