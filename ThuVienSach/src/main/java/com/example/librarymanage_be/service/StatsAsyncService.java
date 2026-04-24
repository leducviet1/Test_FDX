package com.example.librarymanage_be.service;

public interface StatsAsyncService {
    void handleBorrowCreated(Integer userId, int totalBooks);

    void handleReturnBook(Integer userId, int totalBooks);
}
