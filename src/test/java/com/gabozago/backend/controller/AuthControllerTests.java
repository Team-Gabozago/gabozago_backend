package com.gabozago.backend.controller;

import com.gabozago.backend.dto.auth.JoinRequestDto;
import com.gabozago.backend.dto.auth.LoginRequestDto;
import com.gabozago.backend.entity.RefreshToken;
import com.gabozago.backend.entity.User;
import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.auth.TokenProvider;
import com.gabozago.backend.service.RefreshTokenService;
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

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RefreshTokenService refreshTokenService;

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
    @DisplayName("회원가입 @Valid 테스트 이메일이 없다면")
    void testJoinValid_이메일() throws Exception {
        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isBadRequest()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals("BAD_REQUEST");
                    assert jsonObject.get("message").equals(JoinRequestDto.EMAIL_NOT_NULL);
                });
    }

    @Test
    @DisplayName("회원가입 @Valid 테스트 비밀번호가 없다면")
    void testJoinValid_비밀번호() throws Exception {
        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"\", \"nickname\": \"test\"}"))
                .andExpect(status().isBadRequest()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals("BAD_REQUEST");
                    assert jsonObject.get("message").equals(JoinRequestDto.PASSWORD_NOT_NULL);
                });
    }

    @Test
    @DisplayName("회원가입 @Valid 테스트 닉네임이 없다면")
    void testJoinValid_닉네임() throws Exception {
        mockMvc.perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"\"}"))
                .andExpect(status().isBadRequest()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals("BAD_REQUEST");
                    assert jsonObject.get("message").equals(JoinRequestDto.NICKNAME_NOT_NULL);
                });
    }

    @Test
    @DisplayName("로그인 성공")
    void testLogin() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("password");

        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password(encoded)
                .build();

        Mockito.when(userService.findByEmail("test@example.com"))
                .thenReturn(user);

        Mockito.when(passwordEncoder.matches("password", encoded))
                .thenReturn(true);

        Mockito.when(tokenProvider.createAccessToken(user.getId(), user.getRoles()))
                .thenReturn("accessToken");

        Mockito.when(tokenProvider.createRefreshTokenEntity(user))
                .thenReturn(RefreshToken
                        .builder()
                        .token("refreshToken")
                        .build());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isOk()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    header().string("Authorization", "Bearer accessToken").match(result);

                    System.out.println("AccessToken: " + result.getResponse().getHeaders("Set-Cookie").get(0));
                    System.out.println("RefreshToken: " + result.getResponse().getHeaders("Set-Cookie").get(1));
                });
    }

    @Test
    @DisplayName("로그인 @Valid 테스트 이메일이 없다면")
    void testLoginValid_이메일() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"\", \"password\": \"password\", \"nickname\": \"test\"}"))
                .andExpect(status().isBadRequest()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals("BAD_REQUEST");
                    assert jsonObject.get("message").equals(LoginRequestDto.EMAIL_NOT_NULL);
                });
    }

    @Test
    @DisplayName("로그인 @Valid 테스트 비밀번호가 없다면")
    void testLoginValid_비밀번호() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .content("{\"email\": \"test@example.com\", \"password\": \"\", \"nickname\": \"test\"}"))
                .andExpect(status().isBadRequest()).andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    System.out.println(contentAsString);

                    JSONObject jsonObject = new JSONObject(contentAsString);
                    assert jsonObject.get("code").equals("BAD_REQUEST");
                    assert jsonObject.get("message").equals(LoginRequestDto.PASSWORD_NOT_NULL);
                });
    }


    @Test
    @DisplayName("로그인 패스워드 틀리면")
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

    @Test
    @DisplayName("토큰 갱신")
    void testRefreshToken() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        RefreshToken refreshToken = tokenProvider.createRefreshTokenEntity(user);

        Mockito.when(tokenProvider.refreshAccessToken(refreshToken))
                .thenReturn("accessToken");

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .cookie(new Cookie("refreshToken", "refreshToken")))
                .andExpect(status().isOk()).andExpect(result -> {
                    header().string("Authorization", "Bearer accessToken").match(result);
                });
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 토큰이 유효하지 않은 경우")
    void testRefreshTokenFail() throws Exception {
        Mockito.when(refreshTokenService.findByToken("refreshToken"))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("*/*")
                        .cookie(new Cookie("refreshToken", "refreshToken")))
                .andExpect(status().isUnauthorized());
    }
}
