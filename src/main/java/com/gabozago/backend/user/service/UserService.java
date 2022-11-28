package com.gabozago.backend.user.service;

import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        long userId = Long.parseLong(id);
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean checkExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean checkExistsByNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
}
