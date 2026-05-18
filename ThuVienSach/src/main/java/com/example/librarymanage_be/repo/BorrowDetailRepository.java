package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.dto.response.TopBorrowedBookResponse;
import com.example.librarymanage_be.entity.BorrowDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BorrowDetailRepository extends JpaRepository<BorrowDetail,Integer> {
    List<BorrowDetail> findByBorrow_BorrowId(Integer borrowId);

    @Query("""
        select new com.example.librarymanage_be.dto.response.TopBorrowedBookResponse(
            b.bookId,
            b.title,
            sum (bd.quantity)
        )
        from BorrowDetail bd
        join bd.book b
        group by b.bookId, b.title
        order by sum(bd.quantity) desc
        
""")
    List<TopBorrowedBookResponse> findTop5BorrowedBooks();
}
