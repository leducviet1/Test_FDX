package com.example.librarymanage_be.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorId implements Serializable {
    private Integer book;
    private Integer author;
}
