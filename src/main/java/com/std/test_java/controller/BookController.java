package com.std.test_java.controller;


import com.std.test_java.model.Book;
import com.std.test_java.repository.BookRepository;
import com.std.test_java.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final LibraryService libraryService;
    private final BookRepository bookRepository;

    // crear prestamo
    @PostMapping
    public ResponseEntity<Book> createdBook(@RequestBody @Valid Book book) {
        Book saved = libraryService.registerBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // listar libros
    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(libraryService.searchBooks(null));
    }

    // Listar libro por id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el libro de id: " + id));

        return ResponseEntity.ok(book);
    }

    // buscar por palabra
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(name = "q", required = false) String keyword ) {
        return ResponseEntity.ok(libraryService.searchBooks(keyword));
    }

    // eliminar libro
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        libraryService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
