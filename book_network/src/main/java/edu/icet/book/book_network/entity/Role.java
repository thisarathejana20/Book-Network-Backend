package edu.icet.book.book_network.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Role {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
