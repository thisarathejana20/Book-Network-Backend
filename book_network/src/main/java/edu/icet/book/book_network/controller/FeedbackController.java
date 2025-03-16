package edu.icet.book.book_network.controller;

import edu.icet.book.book_network.dto.FeedbackRequest;
import edu.icet.book.book_network.dto.FeedbackResponse;
import edu.icet.book.book_network.dto.PageResponse;
import edu.icet.book.book_network.services.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest,
                                                Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.save(feedbackRequest, connectedUser));
    }

    @GetMapping("book/{id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByBookId(
            @PathVariable Integer id,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            Authentication connectedUser
            ) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByBookId(id, page, size, connectedUser));
    }
}
