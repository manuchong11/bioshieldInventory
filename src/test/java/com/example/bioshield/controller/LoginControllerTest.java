package com.example.bioshield.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class  LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test 1: Simulates a successful Login
    @Test
    void testSuccessfulLogin() throws Exception {
        // We pass the correct credentials that match the hash in LoginController
        String loginJson = "{\"username\":\"admin\",\"password\":\"admin123\"}";

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk()) // 200 Ok status
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    // Test 2: Simulates a failed Login
    @Test
    public void testFailedLogin() throws Exception {
        // We pass incorrect credentials
        String loginJson = "{\"username\":\"wrongUser\",\"password\":\"wrongPassword\"}";

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized()); // 401 Unauthorized status
    }
}
