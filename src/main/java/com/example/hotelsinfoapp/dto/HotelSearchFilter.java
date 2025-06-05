package com.example.hotelsinfoapp.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HotelSearchFilter {
    private String name;
    private String brand;
    private String city;
    private String country;
    private List<String> amenities;
}