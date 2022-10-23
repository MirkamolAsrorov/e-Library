package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book,Long> {
    List<Book> findAllByActiveTrue();
    Optional<Book> findByCallno(String callno);
}
