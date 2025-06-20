package com.smhrd.ta.controller;

import java.util.List;

import java.util.Optional;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.smhrd.ta.entity.EmailVerification;
import com.smhrd.ta.entity.User;
import com.smhrd.ta.repository.EmailVerificationRepository;
import com.smhrd.ta.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;  // Repository 주입
    
    @GetMapping("/join")
    public String showJoinPage() {
        return "join";  // join.html 뷰 이름
    }
    
 // 아이디 중복 확인 요청 처리
    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return userService.isUsernameAvailable(username);
    }

    

    @PostMapping("/join")
    public String joinUser(User user, Model model) {
        try {
            Optional<EmailVerification> optional = emailVerificationRepository
                    .findTopByEmailOrderByCreatedAtDesc(user.getEmail());

            if (optional.isEmpty() || !optional.get().isVerified()) {
                model.addAttribute("error", "이메일 인증을 완료해야 회원가입할 수 있습니다.");
                model.addAttribute("user", user);
                return "join";
            }

            userService.registerUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "서버 오류 발생: " + e.getMessage());
            model.addAttribute("user", user);
            return "join";
        }
    }
    
    
}

