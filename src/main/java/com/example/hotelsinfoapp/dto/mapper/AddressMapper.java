package com.example.hotelsinfoapp.dto.mapper;

import com.example.hotelsinfoapp.dto.AddressDto;
import com.example.hotelsinfoapp.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "street", source = "address.street.name")
    @Mapping(target = "city", source = "address.street.city.name")
    @Mapping(target = "country", source = "address.street.city.country.name")
    AddressDto addressToDto(Address address);

    default StringBuilder mapAddressToString(Address address) {
        return new StringBuilder()
                .append(address.getHouseNumber()).append(" ")
                .append(address.getStreet().getName()).append(", ")
                .append(address.getStreet().getCity().getName()).append(", ")
                .append(address.getPostCode()).append(", ")
                .append(address.getStreet().getCity().getCountry().getName());
    }
}