package com.example.hotelsinfoapp.dto.mapper;

import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.model.Address;
import com.example.hotelsinfoapp.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    @Mapping(target = "address",
            expression = "java(mapAddressToString(hotel.getAddress()).toString())")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortDto hotelToShortDto(Hotel hotel);

    default StringBuilder mapAddressToString(Address address) {
        return new StringBuilder()
                .append(address.getHouseNumber()).append(" ")
                .append(address.getStreet().getName()).append(", ")
                .append(address.getStreet().getCity().getName()).append(", ")
                .append(address.getPostCode()).append(", ")
                .append(address.getStreet().getCity().getCountry().getName());
    }
}