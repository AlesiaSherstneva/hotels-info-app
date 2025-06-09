package com.example.hotelsinfoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Full hotel information")
public class HotelFullDto {
    @Schema(description = "Unique hotel ID", example = "1")
    private Long id;

    @Schema(description = "Hotel name", example = "Grand Plaza Luxury Hotel")
    private String name;

    @Schema(description = "Hotel brand", example = "Luxury Hotels")
    private String brand;

    @Schema(description = "Physical address")
    private AddressDto address;

    @Schema(description = "Contact information")
    private ContactsDto contacts;

    @Schema(description = "Arrival time information")
    private ArrivalTimeDto arrivalTime;

    @Schema(description = "List of available amenities", example = "[\"Free WiFi\", \"Swimming pool\"]")
    private List<String> amenities;
}