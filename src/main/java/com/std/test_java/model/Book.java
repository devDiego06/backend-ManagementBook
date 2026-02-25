package com.std.test_java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El título es obligatorio")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "El autor es obligatorio")
    private String author;

    @Column(unique = true, nullable = false, length = 13)
    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 13, max = 13, message = "El ISBN debe tener exactamente 13 caracteres")
    private String isbn;

    @Column(nullable = false)
    @Min(value = 1000, message = "El año debe ser mayor a 1000")
    @Max(value = 2025, message = "El año no puede ser futuro")
    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        if (this.status == null) {
            this.status = BookStatus.AVAILABLE;
        }
    }

}