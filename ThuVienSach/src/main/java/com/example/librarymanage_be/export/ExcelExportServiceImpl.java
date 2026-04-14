package com.example.librarymanage_be.export;

import com.example.librarymanage_be.dto.response.BookResponse;
import com.example.librarymanage_be.properties.MinioProperties;
import com.example.librarymanage_be.service.BookService;
import com.example.librarymanage_be.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelExportServiceImpl implements ExcelExportService {
    private final MinioService minioService;
    private final MinioProperties minioProperties;
    private final BookService bookService;

    @Override
    public String exportBooks() throws IOException {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "book_" + time + ".xlsx";
        File file = new File(fileName);
        int page = 0;
        int size = 1000;
        int rowIndex = 1;

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Books");

        Row rowHeader = sheet.createRow(0);
        rowHeader.createCell(0).setCellValue("ID");
        rowHeader.createCell(1).setCellValue("Title");
        rowHeader.createCell(2).setCellValue("Author");
        rowHeader.createCell(3).setCellValue("Price");
        rowHeader.createCell(4).setCellValue("Category");
        rowHeader.createCell(5).setCellValue("AvailableQuantity");
        rowHeader.createCell(6).setCellValue("Publisher");
        rowHeader.createCell(7).setCellValue("Status");

        Page<BookResponse> books;
        do {
            Pageable pageable = PageRequest.of(page, size);
            books = bookService.getBooks(pageable);
            for (BookResponse book : books) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(book.getBookId());
                row.createCell(1).setCellValue(book.getTitle());
                String authors = String.join(", ", book.getAuthorNames());
                row.createCell(2).setCellValue(authors);
                row.createCell(3).setCellValue(String.valueOf(book.getPrice()));
                row.createCell(4).setCellValue(book.getCategoryName());
                row.createCell(5).setCellValue(String.valueOf(book.getAvailableQuantity()));
                row.createCell(6).setCellValue(book.getPublisherName());
                row.createCell(7).setCellValue(book.getBookStatus().toString());
            }
            page++;
        } while (books.hasNext());
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.dispose(); //Xóa file tạm
        workbook.close();

        //upload minio
        try (InputStream inputStream = new FileInputStream(fileName)) {
            return minioService.uploadFile(minioProperties.getBucket().getReport(),
                    inputStream,
                    file.length(),
                    fileName,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } catch (IOException e) {
            throw new RuntimeException("File error while upload", e);
        } catch (Exception e) {
            throw new RuntimeException("Upload to minIO failed", e);
        } finally {
            if (file.exists() && !file.delete()) {
                log.warn("Failed to delete minIO file {}", fileName);
            }
        }
    }
}
