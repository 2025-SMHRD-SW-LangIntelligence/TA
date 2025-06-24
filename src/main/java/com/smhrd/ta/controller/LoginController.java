package com.smhrd.ta.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smhrd.ta.entity.User;
import com.smhrd.ta.repository.EmailVerificationRepository;
import com.smhrd.ta.repository.UserRepository;
import com.smhrd.ta.service.OccurService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private final EmailVerificationRepository emailVerificationRepository;

	private final EmailController emailController;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OccurService occurService;

	LoginController(EmailController emailController, EmailVerificationRepository emailVerificationRepository) {
		this.emailController = emailController;
		this.emailVerificationRepository = emailVerificationRepository;
	}

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Optional<User> optionalUser = userRepository.findByUsername(username);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();

			if (user.getPassword().equals(password)) {
				session.setAttribute("loginUser", user);
				redirectAttributes.addFlashAttribute("message", "로그인 되었습니다.");
				
				// ✅ 관리자 계정이라면 관리자 페이지로 이동
	            if ("ADMIN".equals(user.getRole())) {
	                return "redirect:/admin/dashboard";
	            }
				
				return "redirect:/"; // 리다이렉트 시 flash 메시지 유지
			}
		}

		redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
		return "redirect:/login"; // 실패시 리다이렉트 + 메시지 유지
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate();
		redirectAttributes.addFlashAttribute("message", "로그아웃 되었습니다.");
		return "redirect:/";
	}

	@GetMapping("/")
	public String main(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser != null) {
			model.addAttribute("userName", loginUser.getName());
		}
		return "index";
	}

	@GetMapping("/index")
	public String main_page() {
		return "index";
	}
}
