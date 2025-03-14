package edu.icet.book.book_network.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Double rate;
    private boolean returned;
    private boolean returnApproved;
}
