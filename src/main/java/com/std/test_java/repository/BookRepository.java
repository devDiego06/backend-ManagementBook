package com.std.test_java.repository;

import com.std.test_java.model.Book;
import com.std.test_java.model.BookStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // buscar por ISBN exacto
    Optional<Book> findByIsbn(String isbn);

    // buscar por estado
    List<Book> findByStatus(BookStatus status);

    // buscar libros por autor
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Búsqueda por título O autor (JPQL)
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("keyword") String keyword);

    //verificar si el libro existe
    boolean existsByIsbn(String isbn);

    long countByStatus(BookStatus status);


}
