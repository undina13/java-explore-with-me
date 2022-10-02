package ru.practicum.main_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main_server.dto.ApiError;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.WrongRequestException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ApiError notFound(RuntimeException e) {
        return ApiError.builder()
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .reason("object not found")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(WrongRequestException.class)
    public ApiError forbidden(RuntimeException e) {
        return ApiError.builder()
                .status(String.valueOf(HttpStatus.FORBIDDEN))
                .reason("object not found")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
