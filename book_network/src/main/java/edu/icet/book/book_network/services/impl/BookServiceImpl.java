package edu.icet.book.book_network.services.impl;

import edu.icet.book.book_network.dto.BookRequest;
import edu.icet.book.book_network.dto.BookResponse;
import edu.icet.book.book_network.dto.BorrowedBookResponse;
import edu.icet.book.book_network.dto.PageResponse;
import edu.icet.book.book_network.entity.Book;
import edu.icet.book.book_network.entity.BookTransactionHistory;
import edu.icet.book.book_network.entity.User;
import edu.icet.book.book_network.repository.BookRepository;
import edu.icet.book.book_network.repository.BookTransactionHistoryRepository;
import edu.icet.book.book_network.util.BookSpecification;
import edu.icet.book.book_network.util.exception.custom.OperationNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Integer save(BookRequest bookRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse getBook(Integer id) {
        return bookMapper.toBookResponse(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
        ));
    }

    public PageResponse<BookResponse> getBooks(
            Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(user.getId(), pageable);
        List<BookResponse> list = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                list,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
        List<BookResponse> list = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                list,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(
            Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = bookTransactionHistoryRepository.
                findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> list = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                list,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(
            Integer page, Integer size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = bookTransactionHistoryRepository.
                findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> list = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                list,
                borrowedBooks.getNumber(),
                borrowedBooks.getSize(),
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    public Integer setSharable(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        // check authorization of user to update book
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You do not have permission to update sharing status of this book");
        } else {
            book.setSharable(!book.isSharable());
            return bookRepository.save(book).getId();
        }
    }

    public Integer setArchived(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        // check authorization of user to update book
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You do not have permission to archive this book");
        } else {
            book.setArchived(!book.isArchived());
            return bookRepository.save(book).getId();
        }
    }

    public Integer borrowBook(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("This book is not available");
        }
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(id, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("You have already borrowed this book");
        }
        BookTransactionHistory transaction = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(transaction).getId();
    }

    public Integer returnBook(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("This book is not available");
        }
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot return your own book");
        }
        BookTransactionHistory transaction = bookTransactionHistoryRepository.
                findByBookIdAndUserId(id, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You have not borrowed this book"));
        transaction.setReturned(true);
        return bookTransactionHistoryRepository.save(transaction).getId();
    }
}
