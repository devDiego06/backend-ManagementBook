package com.std.test_java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "loan")

public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "El libro es obligatorio")
    private Book book;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String borrowerName;

    @Column(nullable = false)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String borrowerEmail;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @PrePersist
    protected void onCreate() {
        this.loanDate = LocalDate.now();
        this.dueDate = this.loanDate.plusDays(14);
    }

    @Column
    private LocalDate returnDate;
}

