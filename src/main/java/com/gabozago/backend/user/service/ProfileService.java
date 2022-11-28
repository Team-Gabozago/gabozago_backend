package com.gabozago.backend.user.service;

import com.gabozago.backend.common.exception.ConflictException;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.infrastructure.UserRepository;
import com.gabozago.backend.user.interfaces.dto.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    public void update(User user, ProfileUpdateRequest request) {
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new ConflictException("Nickname is already taken");
        }

        user.update(request.getNickname());
        userRepository.save(user);
    }

    public void leave(User user) {
        user.leave();
        userRepository.save(user);
    }
}
