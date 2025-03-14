package edu.icet.book.book_network.util;

import edu.icet.book.book_network.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    // specification interface should override the criteria builder method
    // root points to Entity class and query points to JpaQuery object
    public static Specification<Book> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
