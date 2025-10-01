package qdt.hcmute.vn.dqtbook_backend.view;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class HomeViewController {

    @GetMapping("")
    public String home(Model model, HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            return "redirect:/views/login";
        }
        return "index";
    }
} 

@Controller
@RequestMapping("/views")
class LoginViewController {

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId != null) {
            return "redirect:/";
        }
        return "login";
    }
}

@Controller
@RequestMapping("/views")
class RegisterViewController {

    @GetMapping("/register")
    public String login(Model model) {
        System.out.println(">>> RegisterViewController called");
        return "register";
    }
}

@Controller
@RequestMapping("/views")
class UserViewController {

    @GetMapping("/user")
    public String user(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        return "user";
    }
}

@Controller
@RequestMapping("/views")
class FriendViewController {

    @GetMapping("/friends")
    public String user(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        return "friends";
    }
}

@Controller
@RequestMapping("/views")
class ErrorViewController {

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}