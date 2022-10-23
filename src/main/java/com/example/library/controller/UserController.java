package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.repository.BookRepo;
import com.example.library.repository.UserRepo;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;
    private final BookService bookService;

    @GetMapping("/users")
    public String getAllUsersBYActiveTrue(Model model) {
        model.addAttribute("list", userService.getAll());
        return "admin/usersForAdmin_page";
    }

    @GetMapping("/getBooksForAdmin")
    public String getBook(Model model) {
        model.addAttribute("listOfBooks", bookService.getAllBooks());
        return "admin/booksForAdmin_page";
    }

    @GetMapping("/getHome")
    public String getHomePageForAdmin() {
        return "admin/admin_page";
    }

    @GetMapping("/logOut")
    public String logOut() {
        return "index";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerRequest", new User());
        return "user/user-add";
    }


    @PostMapping("/addUser")
    public String register(@ModelAttribute User user) {
        System.out.println("register request " + user);
        User user1 = userService.add(user.getName(), user.getLogin(),
                user.getPassword(), user.getPhoneNumber());
        return user1 == null ? "user/user-add-error" : "user/user-added";
    }

    @GetMapping("/reLogin")
    public String reLogin() {
        return "errors/loginError";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        System.out.println("login request" + user);
        User authenticated = userService.authenticate(user.getLogin(), user.getPassword());
        Optional<User> admin = userRepo.findById(Long.valueOf(1));
        if (authenticated == null) {
            return "redirect:/user/reLogin";
        }
        if (authenticated.getLogin().equals(admin.get().getLogin()) &&
                authenticated.getPassword().equals(admin.get().getPassword())) {
            return "admin/admin_page";
        } else {
            model.addAttribute("userLogin", authenticated.getLogin());
            return "librarian/librarian_page";
        }
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(Model model, @PathVariable(value = "id") Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        User user = userOptional.get();
        model.addAttribute("user", user);
        return "user/user-edit";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable(value = "id") Long id, @ModelAttribute User user) {
        return userService.edit(id, user) == null ? "" : "redirect:/user/users";
    }

    @GetMapping("/delete/{id}")
    public String activeFalse(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/user/users";
    }

}