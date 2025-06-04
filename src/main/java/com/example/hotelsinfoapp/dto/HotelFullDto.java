package com.example.hotelsinfoapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelFullDto {
    private Long id;
    private String name;
    private String brand;
    private AddressDto address;
    private ContactsDto contacts;
    private ArrivalTimeDto arrivalTime;
    private List<String> amenities;
}