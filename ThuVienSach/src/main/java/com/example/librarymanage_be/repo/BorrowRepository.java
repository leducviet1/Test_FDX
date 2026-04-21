package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.entity.Borrows;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Borrows,Integer> {
}
