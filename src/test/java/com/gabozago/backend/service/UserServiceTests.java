package com.gabozago.backend.service;

import com.gabozago.backend.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UserServiceTests {
    UserService userService;

    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("유저중에 같은 이메일이 있다면 true를 반환한다.")
    void testCheckExistsByEmail() {
        String email = "test@example.com";
        Mockito.when(userRepository.findByEmail(email)).
                thenReturn(java.util.Optional.of(new com.gabozago.backend.entity.User()));

        boolean userExists = userService.checkExistsByEmail("test@example.com");
        assertTrue(userExists);
    }

    @Test
    @DisplayName("유저중에 같은 이메일이 없다면 false를 반환한다.")
    void testCheckExistsByEmail_유저가_없다면_false() {
        boolean userExists = userService.checkExistsByEmail("test@example.com");
        assertFalse(userExists);
    }

    @Test
    @DisplayName("유저중에 같은 닉네임이 있다면 true를 반환한다.")
    void testCheckExistsByNickname() {
        String nickname = "test";
        Mockito.when(userRepository.findByNickname(nickname)).
                thenReturn(java.util.Optional.of(new com.gabozago.backend.entity.User()));

        boolean userExists = userService.checkExistsByNickname("test");
        assertTrue(userExists);
    }

    @Test
    @DisplayName("유저중에 같은 닉네임이 없다면 false를 반환한다.")
    void testCheckExistsByNickname_유저가_없다면_false() {
        boolean userExists = userService.checkExistsByNickname("test");
        assertFalse(userExists);
    }
}
