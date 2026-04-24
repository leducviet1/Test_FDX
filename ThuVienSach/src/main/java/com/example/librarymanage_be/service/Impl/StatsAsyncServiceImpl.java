package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.service.StatsAsyncService;
import com.example.librarymanage_be.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsAsyncServiceImpl implements StatsAsyncService {
    private final UserStatsService userStatsService;

    @Async("statsExecutor")
    @Override
    public void handleBorrowCreated(Integer userId, int totalBooks) {
        try {
            log.info("[ASYNC_STATS] Start update borrow stats for userId={}, totalBooks={}", userId, totalBooks);
            userStatsService.increaseBorrowStats(userId, totalBooks);
            log.info("[ASYNC_STATS] Done update borrow stats for userId={}", userId);
        } catch (Exception e) {
            log.error("[ASYNC_STATS] Failed update borrow stats for userId={}", userId, e);
        }
    }

    @Async("statsExecutor")
    @Override
    public void handleReturnBook(Integer userId, int totalBooks) {
        try {
            log.info("[ASYNC_STATS] Start update return stats for userId={}, totalBooks={}", userId, totalBooks);
            userStatsService.increaseReturnStats(userId, totalBooks);
            log.info("[ASYNC_STATS] Done update return stats for userId={}", userId);
        } catch (Exception e) {
            log.error("[ASYNC_STATS] Failed update return stats for userId={}", userId, e);
        }
    }
}
