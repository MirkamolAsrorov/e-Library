package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.IssuedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssuedBooksRepo extends JpaRepository<IssuedBook, Long> {
    List<IssuedBook> findAll();
    Optional<IssuedBook> findByStudentIdAndBook(Long studentId, Book book);
}
