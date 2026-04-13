package com.example.librarymanage_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponse {
    private Integer authorId;
    private String authorName;
    private String description;

}
