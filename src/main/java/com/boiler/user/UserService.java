package com.boiler.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto create(UserDto request) {
        return convertUser(userRepository.save(User.builder()
                .uid(UUID.randomUUID().toString())
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build()));
    }

    @Transactional
    public UserDto update(String id, UserDto request) {
        var user = getRequiredUser(id);

        user.setName(request.name());
        return convertUser(userRepository.save(user));
    }

    private UserDto convertUser(User user) {
        return new UserDto(user.getUid(), user.getName(), user.getEmail(), user.getPassword());
    }

    public UserDto getUser(String id) {
        return userRepository.findUserByUid(id)
                .map(this::convertUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private User getRequiredUser(String id) {
        return userRepository.findUserByUid(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void delete(String id) {
        userRepository.deleteByUid(id);
    }
}
