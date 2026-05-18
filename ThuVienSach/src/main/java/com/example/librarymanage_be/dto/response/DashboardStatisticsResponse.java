package com.example.librarymanage_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatisticsResponse {
//    private long totalBooks;
    private long availableBooks;
//    private long borrowedBooks;
//    private long inactiveBooks;

//    private long totalBorrows;
    private long borrowingCount;
//    private long overdueBorrowsCount;

    private BigDecimal totalFineRevenue;
//    private BigDecimal unpaidFine;

    private List<TopBorrowedBookResponse> topBorrowedBook;
}
