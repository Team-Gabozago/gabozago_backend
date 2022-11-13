package com.gabozago.backend.controller;

import com.gabozago.backend.controller.annotation.WithAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("내 정보 가져오기")
    @WithAuthUser(id = "1", role = "ROLE_USER")
    public void testGetMe() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"));
    }
}
