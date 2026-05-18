package com.example.librarymanage_be.controller;

import com.example.librarymanage_be.dto.response.DashboardStatisticsResponse;
import com.example.librarymanage_be.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class DashboardController {
    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public DashboardStatisticsResponse getDashboardStatistics() {
        return statisticsService.getDashboardStatistics();
    }

}
