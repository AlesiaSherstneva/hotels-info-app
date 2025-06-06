package com.example.hotelsinfoapp.dto.mapper;

import com.example.hotelsinfoapp.dto.AddressDto;
import com.example.hotelsinfoapp.model.Address;
import com.example.hotelsinfoapp.model.City;
import com.example.hotelsinfoapp.model.Country;
import com.example.hotelsinfoapp.model.Street;
import com.example.hotelsinfoapp.repository.CityRepository;
import com.example.hotelsinfoapp.repository.CountryRepository;
import com.example.hotelsinfoapp.repository.StreetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {
    @Autowired
    protected StreetRepository streetRepository;

    @Autowired
    protected CityRepository cityRepository;

    @Autowired
    protected CountryRepository countryRepository;

    @Mapping(target = "street", source = "address.street.name")
    @Mapping(target = "city", source = "address.street.city.name")
    @Mapping(target = "country", source = "address.street.city.country.name")
    public abstract AddressDto addressToDto(Address address);

    public StringBuilder mapAddressToString(Address address) {
        return new StringBuilder()
                .append(address.getHouseNumber()).append(" ")
                .append(address.getStreet().getName()).append(", ")
                .append(address.getStreet().getCity().getName()).append(", ")
                .append(address.getPostCode()).append(", ")
                .append(address.getStreet().getCity().getCountry().getName());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    public abstract Address createAddress(AddressDto addressDto);

    @AfterMapping
    protected void fillAddress(@MappingTarget Address address, AddressDto dto) {
        Country country = countryRepository.findByName(dto.getCountry())
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        City city = cityRepository.findByNameAndCountry(dto.getCity(), country)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        Street street = streetRepository.findByNameAndCity(dto.getStreet(), city)
                .orElseGet(() -> Street.builder()
                        .name(dto.getStreet())
                        .city(city)
                        .build());

        address.setStreet(street);
    }
}