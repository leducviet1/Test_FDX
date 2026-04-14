package com.example.librarymanage_be.elasticsearch.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(indexName = "books")
@Data
public class BookDocument {
    @Id
    private Integer id;
    private String title;
    private BigDecimal price;
    private String description;
    private String categoryName;
    private String publisherName;
    private List<String> authorNames;
}
