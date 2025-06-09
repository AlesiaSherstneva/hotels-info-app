package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.dto.HotelCreateDto;
import com.example.hotelsinfoapp.dto.HotelFullDto;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelShortDto>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelFullDto> getHotelsById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @PostMapping
    public ResponseEntity<HotelShortDto> createHotel(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createHotel(hotelCreateDto));
    }

    @PostMapping("/{id}/amenities")
    public ResponseEntity<Void> addAmenities(@PathVariable Long id,
                                             @RequestBody Set<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }
}