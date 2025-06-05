package com.example.hotelsinfoapp.service;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.repository.AmenityRepository;
import com.example.hotelsinfoapp.repository.CityRepository;
import com.example.hotelsinfoapp.repository.CountryRepository;
import com.example.hotelsinfoapp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistogramService {
    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final AmenityRepository amenityRepository;

    public Map<String, Integer> getHistogram(String param) {
        List<HistogramDto> histogram = switch (param) {
            case "brand" -> hotelRepository.groupHotelsByBrand();
            case "city" -> cityRepository.groupHotelsByCity();
            case "country" -> countryRepository.groupHotelsByCountry();
            case "amenities" -> amenityRepository.groupHotelsByAmenity();
            default -> throw new IllegalArgumentException("Unsupported histogram parameter: " + param);
        };

        return histogram.stream()
                .collect(Collectors.toMap(HistogramDto::getKey, HistogramDto::getValue));
    }
}