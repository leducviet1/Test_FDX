package com.example.librarymanage_be.export;

import com.example.librarymanage_be.dto.response.BookResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ExcelExportService {
    String exportBooks(Page<BookResponse> books) throws IOException;
}
