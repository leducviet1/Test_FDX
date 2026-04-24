package com.example.librarymanage_be.controller;

import com.example.librarymanage_be.dto.response.TopUserBorrowResponse;
import com.example.librarymanage_be.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats/users")
@RequiredArgsConstructor
public class UserStatController {
    private final UserStatsService userStatsService;

    @GetMapping("/top-borrow-times")
    public List<TopUserBorrowResponse> getTopUsersByBorrowTimes() {
        return userStatsService.getTopUsersByBorrowTimes();
    }

    @GetMapping("/top-borrow-books")
    public List<TopUserBorrowResponse> getTopUsersByBorrowBooks() {
        return userStatsService.getTopUsersByBorrowBooks();
    }
}
