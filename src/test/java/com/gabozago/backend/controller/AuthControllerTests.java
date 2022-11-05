package com.gabozago.backend.controller;

import com.gabozago.backend.entity.User;
import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.jwt.TokenProvider;
import com.gabozago.backend.service.UserService;
import org.json.JSONObject;
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
    @DisplayName("회원가입 성공")
    void testJoin() throws Exception {
        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("같은 이메일을 가지고 있는 유저가 있다면 회원가입 실패")
    void testJoin_이메일_중복이면_실패() throws Exception {
        Mockito.when(userService.checkExistsByEmail("test@example.com"))
                .thenReturn(true);

        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isConflict()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals(ErrorCode.DUPLICATED_EMAIL.getCode());
                    assert jsonObject.get("message").equals(ErrorCode.DUPLICATED_EMAIL.getMessage());
                });
    }

    @Test
    @DisplayName("같은 닉네임을 가지고 있는 유저가 있다면 회원가입 실패")
    void testJoin_닉네임_중복이면_실패() throws Exception {
        Mockito.when(userService.checkExistsByNickname("test"))
                .thenReturn(true);

        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isConflict()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals(ErrorCode.DUPLICATED_NICKNAME.getCode());
                    assert jsonObject.get("message").equals(ErrorCode.DUPLICATED_NICKNAME.getMessage());
                });
    }

    @Test
    @DisplayName("Test Login")
    void testLogin() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("password");

        Mockito.when(userService.findByEmail("test@example.com"))
                .thenReturn(User.builder()
                        .id(1L)
                        .email("test@example.com")
                        .password(encoded)
                        .build());

        Mockito.when(passwordEncoder.matches("password", encoded))
                .thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Login with wrong password")
    void testLoginWithWrongPassword() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("passworld");

        Mockito.when(userService.findByEmail("test@example.com"))
                .thenReturn(User.builder()
                        .id(1L)
                        .email("test@example.com")
                        .password(encoded)
                        .build());

        Mockito.when(passwordEncoder.matches("password", encoded))
                .thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isUnauthorized());
    }
}
