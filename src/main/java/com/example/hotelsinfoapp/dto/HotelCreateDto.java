package com.example.hotelsinfoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelCreateDto {
    @NotBlank(message = "Hotel name is required")
    @Size(max = 50, message = "Hotel name must not exceed 50 characters")
    @Schema(description = "Hotel name", example = "Grand Plaza Luxury Hotel")
    private String name;

    @Size(max = 1000, message = "Hotel description must not exceed 1000 characters")
    @Schema(description = "Hotel description", example = "Luxury VIP hotel with nice service", nullable = true)
    private String description;

    @NotBlank(message = "Hotel brand is required")
    @Size(max = 50, message = "Hotel brand must not exceed 50 characters")
    @Schema(description = "Hotel brand", example = "Luxury Hotels")
    private String brand;

    @Valid
    @NotNull(message = "Address is required")
    private AddressDto address;

    @Valid
    @NotNull(message = "Contacts is required")
    private ContactsDto contacts;

    @Valid
    @NotNull(message = "Arrival time is required")
    private ArrivalTimeDto arrivalTime;
}