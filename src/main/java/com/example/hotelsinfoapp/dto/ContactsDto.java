package com.example.hotelsinfoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactsDto {
    // предполагаю, что все отели находятся в Беларуси или России
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+(375|7)\\s\\d{2,3}\\s\\d{3}-\\d{2}-\\d{2}$",
            message = "Invalid phone format. Valid examples: +375 17 111-11-11 or +7 495 111-11-11")
    @Schema(description = "Contact phone number", example = "+375 17 111-11-11, +7 495 111-11-11")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Schema(description = "Email address", example = "info@grandplaza.ru")
    private String email;
}