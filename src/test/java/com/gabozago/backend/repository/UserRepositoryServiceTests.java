package com.gabozago.backend.repository;

import com.gabozago.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = User.builder().
                email("rosejap97@gmail.com").
                username("user").
                password("password").
                build();

        userRepository.save(user);

        User foundUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        assertEquals(user, foundUser);
    }


    @Test
    public void testFindById() {
        User user = User.builder().username("user").password("password").build();

        userRepository.save(user);

        User userOptional = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        assertEquals(userOptional.getUsername(), user.getUsername());
    }

    @Test
    void testFindByNickname() {
        User user = User.builder().username("user").nickname("test").password("password").build();

        userRepository.save(user);

        User userOptional = userRepository.findByNickname(user.getNickname())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        assertEquals(userOptional.getNickname(), "test");
    }
}
