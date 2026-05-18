package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.dto.response.DashboardStatisticsResponse;
import com.example.librarymanage_be.dto.response.TopBorrowedBookResponse;
import com.example.librarymanage_be.repo.BookRepository;
import com.example.librarymanage_be.repo.BorrowDetailRepository;
import com.example.librarymanage_be.repo.BorrowRepository;
import com.example.librarymanage_be.repo.FineRepository;
import com.example.librarymanage_be.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final FineRepository fineRepository;
    private final BorrowDetailRepository borrowDetailRepository;
    @Override
    public DashboardStatisticsResponse getDashboardStatistics() {
        long availableBooks = bookRepository.countAvailableBooks();
        long borrowingBooks = borrowRepository.countBorrowing();
        BigDecimal fineRevenue = fineRepository.sumPaidFine();
        List<TopBorrowedBookResponse> topBorrowedBookResponses = borrowDetailRepository.findTop5BorrowedBooks();
        return new DashboardStatisticsResponse(
                availableBooks,
                borrowingBooks,
                fineRevenue,
                topBorrowedBookResponses
        );
    }
}
