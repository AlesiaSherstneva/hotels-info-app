package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.dto.HotelSearchFilter;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Hotel Search", description = "Operation for searching hotels by various parameters")
public class SearchController {
    private final HotelService hotelService;

    @Operation(summary = "Search hotels", description = "Returns list of hotels matching search parameters")
    @ApiResponse(
            responseCode = "200",
            description = "Search successful (may be empty if no matches found)",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = HotelShortDto.class)))
    )
    @GetMapping
    public ResponseEntity<List<HotelShortDto>> searchHotels(
            @Parameter(name = "name", description = "Hotel name or its part", example = "Grand Plaza")
            @RequestParam(required = false) String name,

            @Parameter(name = "brand", description = "Hotel brand or its part", example = "Grand Plaza")
            @RequestParam(required = false) String brand,

            @Parameter(name = "city", description = "City where hotel is located", example = "Moscow, Minsk")
            @RequestParam(required = false) String city,

            @Parameter(name = "country", description = "Country where hotel is located", example = "Russia, Belarus")
            @RequestParam(required = false) String country,

            @Parameter(name = "amenities", description = "Available amenities", example = "[\"Free parking\", \"Free WiFi\"]")
            @RequestParam(required = false) Set<String> amenities) {
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