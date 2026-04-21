package com.example.librarymanage_be.service;

import com.example.librarymanage_be.dto.request.BorrowRequest;
import com.example.librarymanage_be.dto.response.BorrowResponse;
import com.example.librarymanage_be.entity.Borrows;
import com.example.librarymanage_be.entity.BorrowDetail;

import java.util.List;

public interface BorrowService {
    BorrowResponse toResponse(Borrows borrows, List<BorrowDetail> details);

    BorrowResponse borrowBooks(BorrowRequest borrowRequest);

    void returnBook(Integer borrowDetailId);

    BorrowResponse returnAllBooks(Integer borrowId);

    Borrows findById(Integer borrowId);


}
