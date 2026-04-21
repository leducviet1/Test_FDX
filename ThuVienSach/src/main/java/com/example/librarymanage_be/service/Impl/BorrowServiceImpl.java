package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.config.FineConfig;
import com.example.librarymanage_be.dto.request.BorrowItemRequest;
import com.example.librarymanage_be.dto.request.BorrowRequest;
import com.example.librarymanage_be.dto.response.BorrowDetailResponse;
import com.example.librarymanage_be.dto.response.BorrowResponse;
import com.example.librarymanage_be.entity.*;
import com.example.librarymanage_be.enums.BorrowDetailStatus;
import com.example.librarymanage_be.enums.BorrowStatus;
import com.example.librarymanage_be.enums.FineStatus;
import com.example.librarymanage_be.enums.FineType;
import com.example.librarymanage_be.exception.BadRequestException;
import com.example.librarymanage_be.repo.BorrowDetailRepository;
import com.example.librarymanage_be.repo.BorrowRepository;
import com.example.librarymanage_be.service.BookService;
import com.example.librarymanage_be.service.BorrowDetailService;
import com.example.librarymanage_be.service.BorrowService;
import com.example.librarymanage_be.service.UserService;
import com.example.librarymanage_be.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final UserService userService;
    private final BorrowRepository borrowRepository;
    private final BookService bookService;
    private final BorrowDetailRepository borrowDetailRepository;
    private final BorrowDetailService borrowDetailService;

    @Override
    public BorrowResponse toResponse(Borrows borrows, List<BorrowDetail> details) {
        BorrowResponse borrowResponse = new BorrowResponse();
        borrowResponse.setBorrowId(borrows.getBorrowId());
        borrowResponse.setUserId(borrows.getUser().getUserId());
        borrowResponse.setBorrowDate(borrows.getBorrowDate());
        borrowResponse.setDueDate(borrows.getDueDate());
        borrowResponse.setReturnDate(borrows.getReturnDate());
        borrowResponse.setBorrowStatus(borrows.getStatus().toString());
        List<BorrowDetailResponse> detailResponses = details.stream()
                .map(detail -> {
                    BorrowDetailResponse d = new BorrowDetailResponse();
                    d.setBookId(detail.getBook().getBookId());
                    d.setTitle(detail.getBook().getTitle());
                    d.setQuantity(detail.getQuantity());
                    d.setNote(detail.getNote());
                    return d;
                })
                .toList();
        borrowResponse.setDetails(detailResponses);
        return borrowResponse;
    }

    @Override
    public BorrowResponse borrowBooks(BorrowRequest borrowRequest) {
        log.info("[BORROW] Borrowing book with userId={}", borrowRequest.getUserId());
        Users user = userService.findEntityById(borrowRequest.getUserId());
        Borrows borrows = new Borrows();
        borrows.setUser(user);
        borrows.setBorrowDate(LocalDateTime.now());
        borrows.setDueDate(LocalDateTime.now().plusDays(30));
        borrows.setStatus(BorrowStatus.BORROWING);
        borrowRepository.save(borrows);
        List<BorrowDetail> details = new ArrayList<>();
        for (BorrowItemRequest itemRequest : borrowRequest.getItems()) {
            Book book = bookService.getEntityById(itemRequest.getBookId());
            if (book.getAvailableQuantity() < itemRequest.getQuantity()) {
                log.error("[BORROW] Book quantity less than available item quantity={} ", itemRequest.getQuantity());
                throw new RuntimeException("Không đủ sách: " + book.getTitle());
            }
            book.setAvailableQuantity(book.getAvailableQuantity() - itemRequest.getQuantity());
            BorrowDetail detail = new BorrowDetail();
            detail.setBorrows(borrows);
            detail.setBook(book);
            detail.setQuantity(itemRequest.getQuantity());
            detail.setNote(itemRequest.getNote());
            detail.setStatus(BorrowDetailStatus.BORROWING);
            details.add(detail);
        }
        borrowDetailRepository.saveAll(details);
        log.info("[BORROW] Borrowing successful with borrowId={}", borrows.getBorrowId());
        return toResponse(borrows, details);
    }

    @Override
    public void returnBook(Integer borrowDetailId) {
        BorrowDetail detail = borrowDetailService.findById(borrowDetailId);
        if (detail.getStatus().equals(BorrowDetailStatus.RETURNED)) {
            log.error("[BORROW_DETAIL] Borrows already returned");
            throw new BadRequestException("Sách đã trả");
        }
        Book book = detail.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + detail.getQuantity());
        detail.setStatus(BorrowDetailStatus.RETURNED);
        detail.setReturnDate(LocalDateTime.now());
        //Số ngày trễ
        long lateDays = 0;
        if (LocalDateTime.now().isAfter(detail.getReturnDate())) {
            lateDays = ChronoUnit.DAYS.between(detail.getReturnDate(), LocalDateTime.now());
        }
        if (lateDays > 0) {
            BigDecimal amount = BigDecimal.valueOf(lateDays).multiply(FineConfig.LATE_FEE_PER_DAY);
            Fine fine = new Fine();
            fine.setAmount(amount);
            fine.setBorrowDetail(detail);
            fine.setType(FineType.LATE);
            fine.setStatus(FineStatus.PENDING);
            fine.setReason("Trả muộn:" + lateDays + " ngày");
            fine.setCreatedAt(LocalDateTime.now());
        }
        borrowDetailRepository.save(detail);
        Borrows borrows = detail.getBorrows();
        boolean allReturned = borrows.getDetails().stream().allMatch(d -> d.getReturnDate() != null);
        if (allReturned) {
            borrows.setStatus(BorrowStatus.RETURNED);
            borrows.setReturnDate(LocalDateTime.now());
            borrowRepository.save(borrows);
            log.info("[BORROW] Borrows successful with borrowId={}", borrows.getBorrowId());
        }
    }

    @Override
    public BorrowResponse returnAllBooks(Integer borrowId) {
        Borrows borrows = findById(borrowId);
        if (borrows.getStatus().equals(BorrowStatus.RETURNED)) {
            log.error("[BORROW_DETAIL] BorrowDetail already returned");
            throw new RuntimeException("Phiếu này đã trả");
        }
        List<BorrowDetail> details = borrowDetailRepository.findByBorrow_BorrowId(borrowId);
        for (BorrowDetail detail : details) {
            Book book = detail.getBook();
            book.setAvailableQuantity(book.getAvailableQuantity() + detail.getQuantity());
            detail.setReturnDate(LocalDateTime.now());
            detail.setStatus(BorrowDetailStatus.RETURNED);
        }
        borrows.setReturnDate(LocalDateTime.now());
        borrows.setStatus(BorrowStatus.RETURNED);
        borrowRepository.save(borrows);
        log.info("[BORROW] Return successful with borrowId={}", borrows.getBorrowId());
        return toResponse(borrows, details);
    }

    @Override
    public Borrows findById(Integer borrowId) {
        return EntityUtils.getOrThrow(borrowRepository.findById(borrowId)
                ,"Borrows not found with id=" + borrowId);
    }
}
