package com.example.hotelsinfoapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class HotelShortDto {
    private Long id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    private String address;
    private String phone;
}