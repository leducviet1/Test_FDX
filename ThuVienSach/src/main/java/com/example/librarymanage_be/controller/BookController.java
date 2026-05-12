package com.example.librarymanage_be.controller;

import com.example.librarymanage_be.dto.request.BookRequest;
import com.example.librarymanage_be.dto.response.BookResponse;
import com.example.librarymanage_be.dto.response.ExportResponse;
import com.example.librarymanage_be.export.ExcelExportService;
import com.example.librarymanage_be.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ExcelExportService excelExportService;

    @PreAuthorize("hasAuthority('BOOK_VIEW')")
    @GetMapping
    public Page<BookResponse> getBooks(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.getBooks(pageable);
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Integer id) {
        return bookService.findById(id);
    }

    @GetMapping("/search")
    public Page<BookResponse> searchBooks(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String categoryName,
                                          @RequestParam(required = false) String authorName
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.searchBooks(keyword, categoryName, authorName, pageable);
    }

    @PreAuthorize("hasAuthority('BOOK_CREATE')")
    @PostMapping("/create")
    public BookResponse create(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.create(bookRequest);
    }

    @PutMapping("/update/{id}")
    public BookResponse update(@PathVariable Integer id, @Valid @RequestBody BookRequest bookRequest) {
        return bookService.update(id, bookRequest);
    }

    @PreAuthorize("hasAuthority('BOOK_DELETE')")
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Integer id) {
        bookService.delete(id);
    }

    @GetMapping("/export")
    public ExportResponse export() throws IOException {
        String url = excelExportService.exportBooks();
        return new ExportResponse(url, "Export successful");
    }
}
