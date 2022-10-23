package com.example.library.repository;

import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
List<User> findAllByActiveTrue();
Optional<User> findByLoginAndPassword(String login,String password);
}
