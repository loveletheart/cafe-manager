package myapp.controller;

import myapp.entity.UserData;
import myapp.repository.UserRepository;
import myapp.service.QRTokenService;
import myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {
	
    @Autowired
    private UserService userService;
    @Autowired
    public QRTokenService qrTokenService;
    @Autowired
    public UserRepository userRepository; 

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃 되었습니다.");
        }
        return "login";
    }
    
    /**
     * 회원가입 페이지를 보여줍니다.
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        return "register";
    }
    
    /**
     * 회원가입 폼을 처리합니다.
     * 폼에서 전달된 id와 password를 사용해 회원가입을 수행하고,
     * 회원가입에 성공하면 로그인 페이지로 리디렉션하고, 실패하면 다시 회원가입 페이지를 표시합니다.
     */
    @PostMapping("/register")
    public String processRegistration(@RequestParam String id,
                                      @RequestParam String password,
                                      @RequestParam String username,
                                      @RequestParam String role,
                                      Model model) {
        System.out.println("회원가입 요청 받음: " + id);
        boolean success = userService.registerUser(id, password, username, role);
        
        if (!success) {
            model.addAttribute("errorMessage", "이미 존재하는 ID 또는 Username입니다.");
            return "register"; // 실패 시 회원가입 페이지 다시 표시
        }
        return "redirect:/menu"; // 성공 시 로그인 페이지로 이동
    }
    
    /**
     * QR코드 촬여후 보안을 위해 html로 들어가서 post요청을한다.
     */
    @GetMapping("/QRredirect")
    public String qrRedirect(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "QRredirect"; // 자동 POST 요청을 수행할 HTML 페이지 반환
    }
    
    /**
     * QR코드 촬여후 로그인정보가 일치한다면 아이디값으로 로그인후,바로 메뉴판을 보여줍니다
     */
    @PostMapping("/QRlogin")
    public void loginWithQR(@RequestBody Map<String, String> requestData, 
                            HttpSession session, 
                            HttpServletResponse response) throws IOException {
        String token = requestData.get("token"); // 요청에서 토큰 가져오기
        UserData user = qrTokenService.getUserByToken(token);
        
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "QR 코드가 유효하지 않습니다.");
            return;
        }

        // Spring Security 인증 객체 생성
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth); // 인증 정보 저장
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 세션에 인증 정보 저장

        // 로그인 성공 후 메뉴 페이지로 리디렉션
        response.sendRedirect("/menu");
    }
    //업데이트 권환테스트5
}
