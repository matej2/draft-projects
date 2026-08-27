package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.entity.User;
import com.example.expense_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserRepository userRepository;
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public void addUser(User user) {
        userRepository.save(user);
    }
}
