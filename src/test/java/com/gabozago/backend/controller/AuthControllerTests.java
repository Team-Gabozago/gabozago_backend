package com.gabozago.backend.controller;

import com.gabozago.backend.entity.User;
import com.gabozago.backend.jwt.TokenProvider;
import com.gabozago.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Test Join")
    void testJoin() throws Exception {
        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"abc@abcd.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Login")
    void testLogin() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("password");

        Mockito.when(userService.findByEmail("abc@abcd.com"))
                .thenReturn(User.builder()
                        .id(1L)
                        .email("abc@abcd.com")
                        .password(encoded)
                        .build());

        Mockito.when(passwordEncoder.matches("password", encoded))
                .thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"abc@abcd.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Login with wrong password")
    void testLoginWithWrongPassword() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("passworld");

        Mockito.when(userService.findByEmail("ppp@kakao.com"))
                .thenReturn(User.builder()
                        .id(1L)
                        .email("ppp@kakao.com")
                        .password(encoded)
                        .build());

        Mockito.when(passwordEncoder.matches("password", encoded))
                .thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"ppp@kakao.com\", \"password\": \"password\"}"))
                .andExpect(status().isUnauthorized());
    }
}
