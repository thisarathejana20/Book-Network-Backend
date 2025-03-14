package edu.icet.book.book_network.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String author;
    private String synopsis;
    private String isbn;
    private String owner;
    private byte[] bookCover;
    private double rate;
    private boolean archived;
    private boolean sharable;
}
