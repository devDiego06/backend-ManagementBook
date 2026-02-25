package com.std.test_java.repository;


import com.std.test_java.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    //Buscamos prestamos por email del solicitante
    List<Loan> findByBorrowerEmail(String email);

    //buscamos prestamos activos (sin devolver)
    List<Loan> findByReturnDateIsNull();

    //buscamos prestamos vencidos (JPQL)
    @Query("SELECT l FROM Loan l WHERE " +
            "l.dueDate < :today AND " +
            "l.returnDate IS NULL")
    List<Loan> findOverdueLoans(@Param("today") LocalDate today);

    //verificar si un libro esta prestado
  //  boolean existByBookIdAndReturnDateIsNull(Long bookId);

    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate today);

    long countByReturnDateIsNull();
}
