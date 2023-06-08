package com.example.btlweb.controller;

import com.example.btlweb.entity.User;
import com.example.btlweb.repository.UserRepository;
import com.example.btlweb.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
public class LoginController {
    @Autowired
    private LoginService service;

    @GetMapping("/")
    String displayHomePage() { return "home"; }

    @GetMapping("/login")
    String displayLoginPage(
        @RequestParam(required = false) String error,
        Model model
    ) {
        model.addAttribute("error", error);
        return "login/login";
    }

    @GetMapping("/register")
    String displayRegisterPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String success,
            Model model
    ) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "login/signup";
    }

    @PostMapping("/authenticate")
    String authenticateUser(
        @RequestParam String username,
        @RequestParam String password,
        RedirectAttributes redirectAttributes
    ) {
        //nếu là admin
        if (service.isAdmin(username, password)) {
            redirectAttributes.addFlashAttribute("admin", "1");
            return "redirect:/books";
        }

        User result = service.findUserByUsername(username);
        //find nothing
        if (result == null) {
            String encodedError = URLEncoder.encode("Tài khoản không hợp lệ", StandardCharsets.UTF_8);
            return "redirect:/login?error="+encodedError;
        }
        //contain username in database
        String dbpassword = result.getMat_khau();
        if (!Objects.equals(password, dbpassword)) {
            String encodedError = URLEncoder.encode("Mật khẩu tương ứng với tài khoản không hợp lệ", StandardCharsets.UTF_8);
            return "redirect:/login?error="+encodedError;
        }
        //take userId to destination
        Integer dbid = result.getId();
        return "redirect:/ubooks/"+dbid;
    }

    @PostMapping("/register")
    String registerUser(
            User user,
            Model model
    ) {
        //kiểm tra nếu trùng tên tài khoản
        if (service.isUsernameExist(user.getTen_tk())) {
            String encodedError = URLEncoder.encode("Tên tài khoản đã tồn tại", StandardCharsets.UTF_8);
            return "redirect:/register?error="+encodedError;
        }

        //thêm vào CSDL
        service.saveNewUser(user);
        String encodedSuccess = URLEncoder.encode("Đăng kí thành công!", StandardCharsets.UTF_8);
        return "redirect:/register?success="+encodedSuccess;
    }
}
