package com.gabozago.backend.fixture;

import com.gabozago.backend.entity.User;

public class UserFixture {

    private UserFixture() {
    }

    public static User 봄봄_생성() {
        return User.builder()
                .email("simbomi06@gmail.com")
                .username("kate")
                .password("1234")
                .nickname("봄봄")
                .build();
    }

    public static User 신디_생성() {
        return User.builder()
                .email("sindy@gmail.com")
                .username("sindy")
                .password("12345")
                .nickname("신디")
                .build();
    }

    public static User ID_있는_유저_생성(Long id) {
        return User.builder()
                .id(id)
                .email("simbomi06@gmail.com")
                .username("kate")
                .password("1234")
                .nickname("봄봄")
                .build();
    }

}
