package edu.icet.book.book_network.services.impl;

import edu.icet.book.book_network.dto.FeedbackRequest;
import edu.icet.book.book_network.dto.FeedbackResponse;
import edu.icet.book.book_network.entity.Book;
import edu.icet.book.book_network.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .comment(feedback.getComment())
                .note(feedback.getNote())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
