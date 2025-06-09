package com.example.hotelsinfoapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ArrivalTimeDto {
    // предполагаю, что время будет назначаться с помощью time picker, поэтому не проверяю на валидность
    @NotNull(message = "Check-in time is required")
    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "Сheck-in time", example = "14:00")
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Сheck-out time", example = "12:00", nullable = true)
    private LocalTime checkOut;
}