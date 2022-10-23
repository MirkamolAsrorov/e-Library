package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.IssuedBook;
import com.example.library.entity.enums.ReturnStatus;
import com.example.library.payload.AddBookDTO;
import com.example.library.payload.ApiResponse;
import com.example.library.payload.IssuedBookDTO;
import com.example.library.payload.ReturnBookDTO;
import com.example.library.repository.BookRepo;
import com.example.library.repository.IssuedBooksRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepo bookRepo;

    private final IssuedBooksRepo issuedBooksRepo;
    private static int countIssued = 0;
    private static int countQuantity = 0;

    public List<Book> getAllBooks() {
        return bookRepo.findAllByActiveTrue();
    }

    public Book addBook(Long id, AddBookDTO dto){
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (bookOptional.isPresent() && dto.getQuantity()>=0) {
            Book oldBook = bookOptional.get();
            oldBook.setQuantity(oldBook.getQuantity()+dto.getQuantity());
            return bookRepo.save(oldBook);
        }
        return null;
    }
    public Book delete(Long id) {
        Optional<Book> optionalBook = bookRepo.findById(id);
        if (optionalBook.isPresent()) {
            Book oldBook = optionalBook.get();
            oldBook.setActive(false);
            return bookRepo.save(oldBook);
        }
        return null;
    }

    public Book addNewBook(Book book) {
        if (book.getCallno() == null || book.getName() == null || book.getQuantity() == null
                || book.getAuthor() == null || book.getPublisher() == null) {
            return null;
        }
        Book newBook = new Book();
        newBook.setCallno(book.getCallno());
        newBook.setName(book.getName());
        newBook.setAuthor(book.getAuthor());
        newBook.setPublisher(book.getPublisher());
        newBook.setQuantity(book.getQuantity());
        return bookRepo.save(newBook);
    }

    public ApiResponse addAnIssuedBook(IssuedBookDTO issuedBookDto) {
        Optional<Book> bookOptional = bookRepo.findByCallno(issuedBookDto.getCallno());
        if (bookOptional.isEmpty()){
            return ApiResponse.builder().
                    message("Null").
                    success(false).
                    build();
        }
        Book currentBook = bookOptional.get();
        if (issuedBookDto.getCallno() == null ||
                issuedBookDto.getStudentId() == null ||
                issuedBookDto.getStudentName() == null ||
                issuedBookDto.getStudentPhoneNumber() == null) {
            return new ApiResponse("An Empty place has to be filled with data", false);
        }
        if (bookOptional.isEmpty()) {
            return ApiResponse.builder().
                    message("The data is not available in our data base").
                    success(false).
                    build();
        }
        if (currentBook.getQuantity() == 0) {
            return ApiResponse.builder().
                    message("The number of book is up").
                    success(false).
                    build();
        }
        currentBook.setIssued(currentBook.getIssued() + 1);
        currentBook.setQuantity(currentBook.getQuantity() - 1);

        IssuedBook newIssuedBook = new IssuedBook();
        newIssuedBook.setBook(bookOptional.get());
        newIssuedBook.setStudentId(issuedBookDto.getStudentId());
        newIssuedBook.setStudentName(issuedBookDto.getStudentName());
        newIssuedBook.setStudentPhoneNumber(Long.valueOf(issuedBookDto.getStudentPhoneNumber()));
        IssuedBook save = issuedBooksRepo.save(newIssuedBook);
        return ApiResponse.builder().
                message("saved").
                success(true).
                object(save).
                build();
    }

    public List<IssuedBook> getIssuedBooks() {
        return issuedBooksRepo.findAll();
    }

    public ApiResponse returnBook(ReturnBookDTO returnBookDTO) {
        Optional<Book> bookOptional = bookRepo.findByCallno(returnBookDTO.getCallno());
        if (!bookOptional.isPresent()){
            return new ApiResponse("Null",false);
        }
        Book current = bookOptional.get();
        Optional<IssuedBook> byStudentIdAndBook = issuedBooksRepo.findByStudentIdAndBook(returnBookDTO.getStudentId(), current);
        if (!byStudentIdAndBook.isPresent()) {
            return ApiResponse.builder().
                    message("The student did not issue book or  Book was not issued before").
                    success(false).
                    build();
        }

        current.setIssued(current.getIssued() - 1);
        current.setQuantity(current.getQuantity() + 1);
        Book save = bookRepo.save(current);

        IssuedBook issuedBook = byStudentIdAndBook.get();
        issuedBook.setReturnStatus(ReturnStatus.YES);
        issuedBook.setBook(save);
        issuedBooksRepo.save(issuedBook);

        return ApiResponse.builder().
                 message("Returned").
                 success(true).build();
    }

}
