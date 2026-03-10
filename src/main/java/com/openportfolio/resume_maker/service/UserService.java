package com.openportfolio.resume_maker.service;

import com.openportfolio.resume_maker.model.User;
import com.openportfolio.resume_maker.model.Profile;
import com.openportfolio.resume_maker.repository.UserRepository;
import com.openportfolio.resume_maker.repository.ProfileRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String username, String email, String password, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        User user = User.builder()
                .username(username.toLowerCase().trim())
                .email(email.toLowerCase().trim())
                .password(passwordEncoder.encode(password))
                .fullName(fullName.trim())
                .build();
        user = userRepository.save(user);
        Profile profile = Profile.builder().user(user).build();
        profileRepository.save(profile);
        return user;
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username.toLowerCase().trim())
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase().trim());
    }
}
