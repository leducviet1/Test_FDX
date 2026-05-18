package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.entity.Book;
import com.example.librarymanage_be.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends
        JpaRepository<Book, Integer>,
        PagingAndSortingRepository<Book, Integer>,
        JpaSpecificationExecutor<Book> {
    Page<Book> findAllByBookStatusNot(BookStatus bookStatus, Pageable pageable);

    @Query("""
        SELECT coalesce(sum(b.availableQuantity),0)
        FROM Book b
        WHERE b.bookStatus="ACTIVE"
""")
    long countAvailableBooks();


//    void increaseQuantity();
}
