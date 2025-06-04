package com.example.hotelsinfoapp.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}