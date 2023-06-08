package com.example.btlweb.service;

import com.example.btlweb.entity.User;
import com.example.btlweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    public Boolean isAdmin(String username, String password) { return username.equals("admin") && password.equals("admin"); }

    public User findUserByUsername(String username) {
        return userRepository.findByTen_tk(username);
    }

    public Boolean isUsernameExist(String newUsername) {
        return newUsername.equalsIgnoreCase("admin") || userRepository.existsByUsername(newUsername);
    }

    public void saveNewUser(User user) {
        userRepository.save(user);
    }
}
