package edu.icet.book.book_network.repository;

import edu.icet.book.book_network.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
// JpaSpecificationExecutor<Book> allows us to use the Specification pattern criteria builder to build queries
public interface BookRepository extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {
    @Query("SELECT Book FROM Book book WHERE book.archived = false AND book.sharable = true AND book.owner.id != :userId")
    Page<Book> findAllDisplayableBooks(Integer userId, Pageable pageable);
}
