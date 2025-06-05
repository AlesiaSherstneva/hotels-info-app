package com.example.hotelsinfoapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ArrivalTimeDto {
    // предполагаю, что время будет назначаться с помощью time picker
    @NotNull(message = "Check-in time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalTime checkOut;
}