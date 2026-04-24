package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.entity.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserStatsRepository extends JpaRepository<UserStats, Integer> {
    List<UserStats> findTop10ByOrderByTotalBorrowTimesDesc();
    List<UserStats> findTop10ByOrderByTotalBorrowBooksDesc();
}
