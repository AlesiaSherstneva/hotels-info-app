package com.example.hotelsinfoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDto {
    @NotBlank(message = "House number is required")
    @Size(max = 10, message = "House number must not exceed 10 characters")
    @Schema(description = "House number", example = "34")
    private String houseNumber;

    @NotBlank(message = "Street name is required")
    @Size(max = 20, message = "Street name must not exceed 20 characters")
    @Schema(description = "Street name", example = "Central Street")
    private String street;

    //предполагаю, что city и country выбираются из выпадающего списка, поэтому не проверяю на валидность
    @NotBlank(message = "City is required")
    @Schema(description = "City name", example = "Moscow, Minsk")
    private String city;

    @NotBlank(message = "Country is required")
    @Schema(description = "Country name", examples = "Russia, Belarus")
    private String country;

    @NotBlank(message = "Post code is required")
    @Pattern(regexp = "\\d{6}", message = "Invalid postal code format (6 digits required)")
    @Schema(description = "Postal code", example = "123456")
    private String postCode;
}