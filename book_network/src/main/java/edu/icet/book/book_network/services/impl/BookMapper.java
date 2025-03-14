package edu.icet.book.book_network.services.impl;

import edu.icet.book.book_network.dto.BookRequest;
import edu.icet.book.book_network.dto.BookResponse;
import edu.icet.book.book_network.dto.BorrowedBookResponse;
import edu.icet.book.book_network.entity.Book;
import edu.icet.book.book_network.entity.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequest book) {
        return Book.builder()
                .id(book.id())
                .title(book.title())
                .synopsis(book.synopsis())
                .author(book.author())
                .archived(false)
                .sharable(book.sharable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .synopsis(book.getSynopsis())
                .isbn(book.getIsbn())
                .owner(book.getOwner().getEmail())
// todo               .bookCover(book.getBookCover())
                .rate(book.getRate())
                .archived(book.isArchived())
                .sharable(book.isSharable())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
        return BorrowedBookResponse.builder()
                .id(bookTransactionHistory.getBook().getId())
                .title(bookTransactionHistory.getBook().getTitle())
                .author(bookTransactionHistory.getBook().getAuthor())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .returned(bookTransactionHistory.isReturned())
                .returnApproved(bookTransactionHistory.isReturnApproved())
                .rate(bookTransactionHistory.getBook().getRate())
                .build();
    }
}
