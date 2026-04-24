package com.example.librarymanage_be.service;

import com.example.librarymanage_be.dto.response.TopUserBorrowResponse;

import java.util.List;

public interface UserStatsService {
    void increaseBorrowStats(Integer userId, Integer totalBooks);

    void increaseReturnStats(Integer userId, Integer totalBooks);

    void increaseOverdueStats(Integer userId);

    List<TopUserBorrowResponse> getTopUsersByBorrowTimes();

    List<TopUserBorrowResponse> getTopUsersByBorrowBooks();
}
