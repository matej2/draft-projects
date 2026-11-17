package com.matej2.budget_lens.service;

import com.matej2.budget_lens.model.entity.User;
import com.matej2.budget_lens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailService {
    private final UserRepository userRepository;
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }
}
