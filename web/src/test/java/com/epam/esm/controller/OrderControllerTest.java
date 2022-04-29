package com.epam.esm.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(longs = 2L)
    void getOne(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.certificates.[*]", containsInAnyOrder(2, 3)))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.cost").value(1.11));
    }

    @ParameterizedTest
    @ValueSource(longs = 4L)
    void getOrderCertificatesWithPagination(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}/certificates", id)
                .param("page", "1")
                .param("limit", "2"))
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("getOrderCertificates"))
                .andExpect(jsonPath("$.page.totalElements").value(5))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(3))
                .andExpect(status().isOk());
    }
}