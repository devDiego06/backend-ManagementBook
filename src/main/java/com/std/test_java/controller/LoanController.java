package com.std.test_java.controller;


import com.std.test_java.model.Book;
import com.std.test_java.model.Loan;
import com.std.test_java.repository.LoanRepository;
import com.std.test_java.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor

public class LoanController {
    private final LoanRepository loanRepository;

    public record LoanRequest(
            @NotNull(message = "El id del libro es obligatorio") Long bookId,
            @NotBlank(message = "El nombre del prestatario es obligatorio") String borrowerName,
            @Email(message = "El email no tiene un formato válido") String borrowerEmail
    ) {}

    private final LibraryService libraryService;


    ///crear prestamo
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody @Valid LoanRequest loanRequest){
        Loan loan = libraryService.borrowBook(
                loanRequest.bookId(),
                loanRequest.borrowerName(),
                loanRequest.borrowerEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    //regresar libro
    @PutMapping("/{id}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long id){
        Book book = libraryService.returnBook(id);
        return ResponseEntity.ok(book);
    }

    //obtener prestamos vencidos
    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getLoansOverdue(){
        List<Loan> loan = loanRepository.findOverdueLoans(LocalDate.now());
        return ResponseEntity.ok(loan);
    }

    //obtener prestamos activos
    @GetMapping("/actives")
    public ResponseEntity<List<Loan>> getLoansActive(){
        List<Loan> loan = loanRepository.findByReturnDateIsNull();
        return ResponseEntity.ok(loan);
    }




}
