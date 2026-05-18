package com.example.librarymanage_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopBorrowedBookResponse {
    private Integer bookId;
    private String title;
    private long borrowedQuantity;
}
