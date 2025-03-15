package edu.icet.book.book_network.services.impl;

import edu.icet.book.book_network.dto.FeedbackRequest;
import edu.icet.book.book_network.dto.FeedbackResponse;
import edu.icet.book.book_network.dto.PageResponse;
import edu.icet.book.book_network.entity.Book;
import edu.icet.book.book_network.entity.Feedback;
import edu.icet.book.book_network.entity.User;
import edu.icet.book.book_network.repository.BookRepository;
import edu.icet.book.book_network.repository.FeedbackRepository;
import edu.icet.book.book_network.services.FeedbackService;
import edu.icet.book.book_network.util.exception.custom.OperationNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    @Override
    public Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Book book = bookRepository.findById(feedbackRequest.bookId()).
                orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("You cannot give feedback for an archived or not sharable book");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give feedback for your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> getFeedbacksByBookId(Integer id, Integer page, Integer size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(id,pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
