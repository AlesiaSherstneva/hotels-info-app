package com.example.hotelsinfoapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Short hotel information")
public class HotelShortDto {
    @Schema(description = "Unique hotel ID", example = "1")
    private Long id;

    @Schema(description = "Hotel name", example = "Grand Plaza Hotel")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Hotel description", example = "Luxury VIP hotel with nice service", nullable = true)
    private String description;

    @Schema(description = "Hotel address", example = "\"34 Central Street, Moscow, Russia\"")
    private String address;

    @Schema(description = "Hotel contact phone number", example = "+375 17 111-11-11, +7 495 111-11-11")
    private String phone;
}