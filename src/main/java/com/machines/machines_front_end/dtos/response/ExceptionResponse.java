package com.machines.machines_front_end.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private LocalDateTime dateTime;
    private HttpStatus status;
    private Integer statusCode;
}