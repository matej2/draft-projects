package com.matej2.budget_lens.service;

import com.matej2.budget_lens.domain.entity.User;
import com.matej2.budget_lens.repository.UserRepository;
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
