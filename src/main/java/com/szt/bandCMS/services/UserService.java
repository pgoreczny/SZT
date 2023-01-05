package com.szt.bandCMS.services;

import com.szt.bandCMS.models.User;
import com.szt.bandCMS.repositories.UserRepository;
import com.szt.bandCMS.security.AuthProvider;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthProvider authProvider;

    public UserService(UserRepository userRepository, AuthProvider authProvider) {
        this.userRepository = userRepository;
        this.authProvider = authProvider;
    }

    public int changePassword(String oldPassword, String newPassword, String newPassword2, String username) {
        if (!newPassword.equals(newPassword2)) {
            return -1;
        }
        User user = userRepository.findUserByUsername(username).get();
        if (!authProvider.passwordEncoder().matches(oldPassword, user.getPassword())) {
            return -2;
        }
        user.setPassword(authProvider.passwordEncoder().encode(newPassword));
        userRepository.save(user);
        return 0;
    }

    public User getUser(String username) {
        return userRepository.findById(username).get();
    }
}
