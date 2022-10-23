package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.payload.AddBookDTO;
import com.example.library.payload.ApiResponse;
import com.example.library.payload.IssuedBookDTO;
import com.example.library.payload.ReturnBookDTO;
import com.example.library.repository.BookRepo;
import com.example.library.repository.IssuedBooksRepo;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/booksList")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookRepo bookRepo;

    @GetMapping("/getBooks")
    public String getBook(Model model) {
        model.addAttribute("bookList", bookService.getAllBooks());
        return "librarian/librarian_page";
    }
    @GetMapping("/reGetBooks")
    public String reGetBook(Model model) {
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book/add-book-quantity-error";
    }
    @GetMapping("add/{id}")
    public String getAddPage(@PathVariable Long id, Model model){
        Optional<Book> bookOptional = bookRepo.findById(id);
        if (!bookOptional.isPresent()) return "redirect:/booksList/get";
        model.addAttribute("edited", bookOptional.get());
        return "book/add-book-quantity";
    }

    @PostMapping("/add/{id}")
    public String addQuantity(@PathVariable Long id, @ModelAttribute AddBookDTO addBookDTO) {
        Book book = bookService.addBook(id, addBookDTO);
        System.out.println(book);
        return book != null ? "redirect:/booksList/getBooks"
                : "redirect:/booksList/reGetBooks";
    }


    @GetMapping("/getAddBookPage")
    public String registerBookPage(Model model) {
        model.addAttribute("registerRequest", new Book());
        return "book/add-book";
    }

    @PostMapping("/addBook")
    public String registerBook(@ModelAttribute Book book) {
        Book book1 = bookService.addNewBook(book);
        return book1 == null ? "book/add-book-error" : "book/added-book";
    }


    @GetMapping("/getViewIssuedBookPage")
    public String getIssuedBooks(Model model) {
        model.addAttribute("issuedBookList", bookService.getIssuedBooks());
        return "librarian/view-AnIssuedBooks";
    }

    @GetMapping("/getAdAnIssuedBookPage")
    public String registerIssuedBook(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book/issueBook/add-issuedBook";
    }
    @GetMapping("/getAdAnIssuedBookPage_error")
    public String reIssuedBook(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book/issueBook/add-IssuedBook-error";
    }

    @PostMapping("/addAnIssuedBook")
    public String registerAnIssuedBook(@ModelAttribute @RequestBody IssuedBookDTO issuedBookDto) {
        ApiResponse apiResponse = bookService.addAnIssuedBook(issuedBookDto);
        return apiResponse.isSuccess() ? "book/issueBook/added-issuedBook" :
                "book/issueBook/add-IssuedBook-error";
    }

    @GetMapping("/getReturnBookPage")
    public String returnBookPage() {
        return "book/return-book";
    }

    @GetMapping("/returnBook")
    public String returnBook(@ModelAttribute ReturnBookDTO returnBookDTO) {
        ApiResponse apiResponse = bookService.returnBook(returnBookDTO);
        return apiResponse.isSuccess() ? "book/returned-book"
                : "book/return-book-error";
    }



    @GetMapping("/delete/{id}")
    public String activeFalse(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/booksList/getBooks";
    }


}
