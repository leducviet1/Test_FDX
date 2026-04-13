package com.example.librarymanage_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequest {
    @NotBlank(message = "Không được để trống tên tác giả")
    private String authorName;
    private String description;
}
