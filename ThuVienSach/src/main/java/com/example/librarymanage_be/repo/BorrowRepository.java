package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.entity.Borrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BorrowRepository extends JpaRepository<Borrows,Integer> {
    @Query("""
        SELECT count(b)
        FROM Borrows b
        where b.status="BORROWING"
""")
    long countBorrowing();
}
