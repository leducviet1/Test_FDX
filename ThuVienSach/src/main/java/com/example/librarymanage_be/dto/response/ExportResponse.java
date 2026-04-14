package com.example.librarymanage_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExportResponse {
    private String url;
    private String message;
}
