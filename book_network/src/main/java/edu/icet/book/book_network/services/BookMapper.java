package edu.icet.book.book_network.services;

import edu.icet.book.book_network.dto.BookRequest;
import edu.icet.book.book_network.dto.BookResponse;
import edu.icet.book.book_network.entity.Book;
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
}
