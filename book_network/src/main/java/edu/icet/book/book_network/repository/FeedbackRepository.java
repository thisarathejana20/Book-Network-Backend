package edu.icet.book.book_network.repository;

import edu.icet.book.book_network.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
    @Query("SELECT feedback FROM Feedback feedback WHERE feedback.book.id = :id")
    Page<Feedback> findAllByBookId(Integer id, Pageable pageable);
}
