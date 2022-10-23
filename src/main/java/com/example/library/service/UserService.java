package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repository.BookRepo;
import com.example.library.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final BookRepo bookRepo;
    public User add(String name, String login, String password, Long phoneNumber) {
        if (name == null || login == null || password == null || phoneNumber == null) {
            return null;
        } else {
            User user = new User();
            user.setName(name);
            user.setLogin(login);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);
            return userRepo.save(user);
        }
    }

    public User authenticate(String login, String password) {
        return userRepo.findByLoginAndPassword(login, password).orElse(null);
    }

    public List<User> getAll() {
        return userRepo.findAllByActiveTrue();
    }

    public List<Book> getAllB() {
        return bookRepo.findAllByActiveTrue();
    }

    public User edit(Long id, User user) {
        if (user.getPassword() == null || user.getLogin() == null ||
                user.getPassword() == null || user.getPhoneNumber() == null) {
            return null;
        } else {
            Optional<User> userOptional = userRepo.findById(id);

            User newUser = userOptional.get();
            newUser.setName(user.getName());
            newUser.setLogin(user.getLogin());
            newUser.setPassword(user.getPassword());
            newUser.setPhoneNumber(user.getPhoneNumber());
            return userRepo.save(newUser);
        }
    }
    public User delete(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            User oldUser = userOptional.get();
            oldUser.setActive(false);
            return userRepo.save(oldUser);
        }
        return null;
    }
}
