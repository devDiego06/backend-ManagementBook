package com.std.test_java.service;

import com.std.test_java.exception.BookAlreadyExistsException;
import com.std.test_java.exception.BookNotAvailableException;
import com.std.test_java.exception.InvalidBookStatusException;
import com.std.test_java.exception.LoanAlreadyReturnedException;
import com.std.test_java.exception.ResourceNotFoundException;
import com.std.test_java.model.Book;
import com.std.test_java.model.BookStatus;
import com.std.test_java.model.Loan;
import com.std.test_java.repository.BookRepository;
import com.std.test_java.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    //registrar libro
    public Book registerBook(Book book){
        if(bookRepository.existsByIsbn(book.getIsbn())){
            throw new BookAlreadyExistsException("Ya existe un libro con el ISBN: " + book.getIsbn());
        }
        book.setCreatedAt(LocalDate.now());
        return bookRepository.save(book);
    }

    //realizar prestamo
    public Loan borrowBook(Long bookId, String borrowerName, String borrowerEmail){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + bookId));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("El libro no está disponible para préstamo");
        }

        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setBorrowerEmail(borrowerEmail);
        loan.setBorrowerName(borrowerName);

        loan.setDueDate(LocalDate.now().plusDays(14));
        return loanRepository.save(loan);
    }

    //devolver libro
    public Book returnBook(Long longId){

        Loan loan = loanRepository.findById(longId)
                .orElseThrow(() -> new ResourceNotFoundException("El prestamo no fue encontrado: " + longId));

        if(loan.getReturnDate() != null){
            throw new LoanAlreadyReturnedException("Este prestamo ya fue devuelto");
        }

        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        return bookRepository.save(book);
    }

    //buscar libros
    public List<Book> searchBooks(String keyword ){
        if(keyword == null || keyword.isBlank()){
            return bookRepository.findAll();
        }
        return bookRepository.searchByTitleOrAuthor(keyword);
    }

    //buscar prestamos
    public List<Loan> searchLoan(String borrowerEmail){
        if(borrowerEmail == null || borrowerEmail.isBlank()){
            return loanRepository.findAll();
        }
        return loanRepository.findByBorrowerEmail(borrowerEmail);
    }

    //obtener estadisticas
    public Map<String, Object> getLibraryStats() {
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.countByStatus(BookStatus.AVAILABLE);
        long borrowedBooks = bookRepository.countByStatus(BookStatus.BORROWED);
        long activeLoans = loanRepository.countByReturnDateIsNull();
        long overdueLoans = loanRepository.countOverdueLoans(LocalDate.now());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", totalBooks);
        stats.put("availableBooks", availableBooks);
        stats.put("borrowedBooks", borrowedBooks);
        stats.put("activeLoans", activeLoans);
        stats.put("overdueLoans", overdueLoans);

        return stats;
    }

    //eliminar libro
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new InvalidBookStatusException("No se puede eliminar un libro que no está disponible");
        }

        bookRepository.delete(book);
    }




}
