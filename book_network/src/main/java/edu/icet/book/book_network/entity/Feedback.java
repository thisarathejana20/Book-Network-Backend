package edu.icet.book.book_network.entity;

import edu.icet.book.book_network.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Feedback extends BaseEntity {
    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
