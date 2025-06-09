package com.example.hotelsinfoapp.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class HotelSearchFilter {
    private String name;
    private String brand;
    private String city;
    private String country;
    private Set<String> amenities;
}