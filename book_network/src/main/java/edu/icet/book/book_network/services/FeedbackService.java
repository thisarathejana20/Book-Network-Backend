package edu.icet.book.book_network.services;

import edu.icet.book.book_network.dto.FeedbackRequest;
import edu.icet.book.book_network.dto.FeedbackResponse;
import edu.icet.book.book_network.dto.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser);

    PageResponse<FeedbackResponse> getFeedbacksByBookId(Integer id, Integer page, Integer size, Authentication connectedUser);
}
