package com.example.librarymanage_be.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUserBorrowResponse {
    private Integer userId;
    private String userName;
    private Integer totalBorrowTimes;
    private Integer totalBorrowBooks;
}
