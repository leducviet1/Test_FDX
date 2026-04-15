package com.example.librarymanage_be.utils;

import com.example.librarymanage_be.exception.NotFoundException;

import java.util.Optional;

public class EntityUtils {
    public static <T> T getOrThrow(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new NotFoundException(message));
    }
}
