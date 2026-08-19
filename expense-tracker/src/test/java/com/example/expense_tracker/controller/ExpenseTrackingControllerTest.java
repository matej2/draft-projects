package com.example.expense_tracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ExpenseTrackingControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    void testThatRootReturns200WhenAuthenticated() throws Exception {
    MvcResult result = this.mockMvc
            .perform(
                    post("/auth/token")
                            .with(httpBasic("john", "password")))
            .andExpect(status().isOk())
            .andReturn();

    String token = result.getResponse().getContentAsString();

    this.mockMvc
            .perform(
                get("/api/").header("Authorization", "Bearer " + token))
            .andExpect(content().string("john"));
    }

    @Test
    @WithMockUser
    void testThatRootReturns200WhenCalledByMock() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/"))
                .andExpect(status().isOk());
    }
}
