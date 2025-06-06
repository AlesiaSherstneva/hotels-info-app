package com.example.hotelsinfoapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ErrorResponseDto {
    private String message;
    private Integer status;
    private String path;
    private ZonedDateTime timestamp;
}