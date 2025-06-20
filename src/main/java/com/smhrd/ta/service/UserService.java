package com.smhrd.ta.service;

import com.smhrd.ta.entity.User;
import com.smhrd.ta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUsernameAvailable(String username) {
        return !userRepository.findByUsername(username).isPresent();  // 중요
    }



    public void registerUser(User user) {
        // 비밀번호 암호화 추가
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
