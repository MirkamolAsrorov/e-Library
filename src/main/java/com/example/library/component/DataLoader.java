package com.example.library.component;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repository.BookRepo;
import com.example.library.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepo userRepo;

    private final BookRepo bookRepo;

    @Value("always")
    String mode;

    @Override
    public void run(String... args) throws Exception {

        if (mode.equals("always")) {
            User admin = new User();
            admin.setName("Mirkamol");
            admin.setLogin("admin123@gmail.com");
            admin.setPhoneNumber(Long.valueOf("998945713171"));
            admin.setPassword("mfeiskdnfjN23434");

            userRepo.save(admin);

            User librarian = new User();
            librarian.setName("Neli");
            librarian.setLogin("neli123@gmail.com");
            librarian.setPassword("nfjehifWerj1234");
            librarian.setPhoneNumber(Long.valueOf("998945712311"));
            userRepo.save(librarian);


            Book book = new Book();
            book.setCallno("A-1")   ;
            book.setName("Reach Dad and Poor Dad");
            book.setAuthor("Robert Ciyosaki");
            book.setPublisher("BPB");
            book.setQuantity(20);

            bookRepo.save(book);


            Book book1 = new Book();
            book1.setCallno("B-1");
            book1.setName("Odamiylik mulki");
            book1.setAuthor("Tohir Malik");
            book1.setPublisher("OzMu");
            book1.setQuantity(20);

            bookRepo.save(book1);

        }
    }
}
