package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;

public interface FineRepository extends JpaRepository<Fine, Integer>, PagingAndSortingRepository<Fine, Integer> {
    @Query("""
       select coalesce(SUM(f.amount), 0)
       FROM Fine f
       where f.status = 'PAIDED'
""")
    BigDecimal sumPaidFine();
}
