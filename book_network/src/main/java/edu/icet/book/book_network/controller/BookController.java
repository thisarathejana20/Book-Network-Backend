package edu.icet.book.book_network.controller;

import edu.icet.book.book_network.dto.BookRequest;
import edu.icet.book.book_network.dto.BookResponse;
import edu.icet.book.book_network.dto.BorrowedBookResponse;
import edu.icet.book.book_network.dto.PageResponse;
import edu.icet.book.book_network.services.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {
    private final BookServiceImpl bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@RequestBody @Valid BookRequest bookRequest,
                                            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.save(bookRequest, connectedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getBooks(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getBooks(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> finAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> finAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/sharable/{id}")
    public ResponseEntity<Integer> setSharable(@PathVariable Integer id, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.setSharable(id, connectedUser));
    }

    @PatchMapping("/archive/{id}")
    public ResponseEntity<Integer> setArchived(@PathVariable Integer id, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.setArchived(id, connectedUser));
    }

    @PostMapping("/borrow/{id}")
    public ResponseEntity<Integer> borrowBook(@PathVariable Integer id, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.borrowBook(id, connectedUser));
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<Integer> returnBook(@PathVariable Integer id, Authentication connectedUser) {
        return ResponseEntity.ok(bookService.returnBook(id, connectedUser));
    }
}
