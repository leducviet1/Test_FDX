package com.example.librarymanage_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_stats")
public class UserStats {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "total_borrow_times", insertable = false, updatable = false)
    private Integer totalBorrowTimes;

    @Column(name = "total_borrow_books")
    private Integer totalBorrowBooks;

    @Column(name = "total_return_books")
    private Integer totalReturnBooks;

    @Column(name = "total_overdue_times")
    private Integer totalOverdueTimes;

    @Column(name = "total_fine_amount")
    private BigDecimal totalFineAmount;

    @Column(name = "total_paid_fine_amount")
    private BigDecimal totalPaidFineAmount;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
