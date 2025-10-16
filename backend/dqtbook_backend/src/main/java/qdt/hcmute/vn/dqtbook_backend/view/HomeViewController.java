package qdt.hcmute.vn.dqtbook_backend.view;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeViewController {

    @GetMapping("")
    public String home(Model model, HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        model.addAttribute("userId", userId.toString());
        return "index";
    }
}

@Controller
@RequestMapping("/views")
class ViewController {
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        System.out.println(">>> RegisterViewController called");
        return "register";
    }

    @GetMapping("/user")
    public String user(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        model.addAttribute("viewingUserId", userId.toString());
        model.addAttribute("canDeletePost", true);
        return "user";
    }

    @GetMapping("/user/{id}")
    public String getUserById(HttpServletRequest request, @PathVariable("id") String id, Model model) {
        Object sessionUserId = request.getSession().getAttribute("userId");
        if (sessionUserId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        if (sessionUserId.toString().equals(id)) {
            return "redirect:/views/user";
        }
        model.addAttribute("viewingUserId", id);
        return "another_user";
    }

    @GetMapping("/friends")
    public String friends(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        return "friends";
    }

    @GetMapping("/event")
    public String event(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        model.addAttribute("role", role);
        
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            
            return "error";
        }
        return "event";
    }

    @GetMapping("/adminManager")
    public String adminManager(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");
        model.addAttribute("role", role);
        
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            
            return "error";
        }
        return "adminManager";
    }

    @GetMapping("/marketplace")
    public String market(HttpServletRequest request, Model model) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            model.addAttribute("error", "You must be logged in to access this page");
            return "error";
        }
        model.addAttribute("userId", userId.toString());
        return "market";
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