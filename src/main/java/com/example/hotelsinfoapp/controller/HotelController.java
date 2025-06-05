package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.dto.HotelFullDto;
import com.example.hotelsinfoapp.dto.HotelSearchFilter;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortDto>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullDto> getHotelsById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelShortDto>> searchHotels(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String brand,
                                                            @RequestParam(required = false) String city,
                                                            @RequestParam(required = false) String country,
                                                            @RequestParam(required = false) List<String> amenities) {
        // pagination я не делала, так как её не было в ТЗ
        HotelSearchFilter filter = HotelSearchFilter.builder()
                .name(name)
                .brand(brand)
                .city(city)
                .country(country)
                .amenities(amenities)
                .build();

        return ResponseEntity.ok(hotelService.searchHotels(filter));
    }
}