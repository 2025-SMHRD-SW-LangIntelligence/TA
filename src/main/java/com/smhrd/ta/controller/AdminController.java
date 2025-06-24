package com.smhrd.ta.controller;

import com.smhrd.ta.entity.User;
import com.smhrd.ta.repository.UserRepository;

import jakarta.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");

        // 로그인 안했거나 관리자가 아니면 차단
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "관리자만 접근 가능합니다.");
            return "redirect:/";
        }

        return "admin/dashboard"; // 관리자 전용 페이지로 이동
    }
    
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/users/list")
    public String listUsers(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        List<User> allUsers = userRepository.findAll();

        // 관리자 먼저 정렬
        List<User> sortedUsers = allUsers.stream()
            .sorted((u1, u2) -> {
                if ("ADMIN".equals(u1.getRole())) return -1;
                if ("ADMIN".equals(u2.getRole())) return 1;
                return 0;
            })
            .toList();

        model.addAttribute("users", sortedUsers);
        return "userList";
    }
    // 유저 삭제
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "관리자만 삭제할 수 있습니다.");
            return "redirect:/admin/users/list";
        }

        User targetUser = userRepository.findById(id).orElse(null);
        if (targetUser == null || "ADMIN".equals(targetUser.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 사용자를 삭제할 수 없습니다.");
            return "redirect:/admin/users/list";
        }

        userRepository.delete(targetUser);
        redirectAttributes.addFlashAttribute("message", "사용자를 삭제했습니다.");
        return "redirect:/admin/users/list";
    }




}
