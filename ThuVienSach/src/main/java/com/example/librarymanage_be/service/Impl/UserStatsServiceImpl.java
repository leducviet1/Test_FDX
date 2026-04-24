package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.dto.response.TopUserBorrowResponse;
import com.example.librarymanage_be.entity.UserStats;
import com.example.librarymanage_be.entity.Users;
import com.example.librarymanage_be.repo.UserStatsRepository;
import com.example.librarymanage_be.service.UserService;
import com.example.librarymanage_be.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl implements UserStatsService {
    private final UserStatsRepository userStatsRepository;
    private final UserService userService;

    private UserStats getOrCreate(Integer userId){
        return userStatsRepository.findById(userId).orElseGet(()->{
            Users user = userService.findEntityById(userId);
            UserStats userStats = new UserStats();
            userStats.setUser(user);
            userStats.setTotalBorrowTimes(0);
            userStats.setTotalBorrowBooks(0);
            userStats.setTotalOverdueTimes(0);
            userStats.setTotalReturnBooks(0);
            userStats.setUpdatedAt(LocalDateTime.now());
            return userStats;
        });
    }

    @Override
    public void increaseBorrowStats(Integer userId, Integer totalBooks) {
        UserStats userStats = getOrCreate(userId);
        userStats.setTotalBorrowTimes(userStats.getTotalBorrowTimes() + 1);
        userStats.setTotalBorrowBooks(userStats.getTotalBorrowBooks() + totalBooks);
        userStats.setUpdatedAt(LocalDateTime.now());
        userStatsRepository.save(userStats);
    }

    @Override
    public void increaseReturnStats(Integer userId, Integer totalBooks) {
        UserStats userStats = getOrCreate(userId);
        userStats.setTotalReturnBooks(userStats.getTotalReturnBooks() + totalBooks);
        userStats.setUpdatedAt(LocalDateTime.now());
        userStatsRepository.save(userStats);
    }

    @Override
    public void increaseOverdueStats(Integer userId) {
        UserStats userStats = getOrCreate(userId);
        userStats.setTotalOverdueTimes(userStats.getTotalOverdueTimes() + 1);
        userStats.setUpdatedAt(LocalDateTime.now());
        userStatsRepository.save(userStats);
    }

    @Override
    public List<TopUserBorrowResponse> getTopUsersByBorrowTimes() {
        return userStatsRepository.findTop10ByOrderByTotalBorrowTimesDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TopUserBorrowResponse> getTopUsersByBorrowBooks() {
        return userStatsRepository.findTop10ByOrderByTotalBorrowBooksDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TopUserBorrowResponse toResponse(UserStats userStats) {
        TopUserBorrowResponse response = new TopUserBorrowResponse();
        response.setUserId(userStats.getUserId());
        response.setUserName(userStats.getUser().getUsername());
        response.setTotalBorrowBooks(userStats.getTotalBorrowBooks());
        response.setTotalBorrowTimes(userStats.getTotalBorrowTimes());
        return response;
    }
}
