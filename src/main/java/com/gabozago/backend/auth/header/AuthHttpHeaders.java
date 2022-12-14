package com.gabozago.backend.auth.header;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletRequest;

public class AuthHttpHeaders extends HttpHeaders {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public void setAccessToken(String accessToken, HttpServletRequest request) {
        this.set(AUTHORIZATION, BEARER + accessToken);
        this.setContentType(MediaType.APPLICATION_JSON);
    }

    public void setRefreshToken(String refreshToken, HttpServletRequest request) {
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 30) // 30 days
                .secure(true)
                .sameSite("None")
                .build();

        this.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
