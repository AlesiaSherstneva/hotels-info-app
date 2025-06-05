package com.example.hotelsinfoapp.dto.mapper;

import com.example.hotelsinfoapp.dto.HotelCreateDto;
import com.example.hotelsinfoapp.dto.HotelFullDto;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.model.Amenity;
import com.example.hotelsinfoapp.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class})
public interface HotelMapper {
    HotelShortDto hotelToShortDto(Hotel hotel);

    @Mapping(target = "contacts.phone", source = "phone")
    @Mapping(target = "contacts.email", source = "email")
    @Mapping(target = "arrivalTime.checkIn", source = "checkIn")
    @Mapping(target = "arrivalTime.checkOut", source = "checkOut")
    HotelFullDto hotelToFullDto(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", source = "contacts.phone")
    @Mapping(target = "email", source = "contacts.email")
    @Mapping(target = "checkIn", source = "arrivalTime.checkIn")
    @Mapping(target = "checkOut", source = "arrivalTime.checkOut")
    @Mapping(target = "amenities", ignore = true)
    Hotel createDtoToHotel(HotelCreateDto dto);

    default List<String> getAmenitiesNames(Set<Amenity> amenities) {
        if (amenities.isEmpty()) {
            return List.of();
        }
        return amenities.stream()
                .sorted(Comparator.comparing(Amenity::getId))
                .map(Amenity::getName)
                .toList();
    }
}