package com.example.hotelsinfoapp.service;

import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.dto.mapper.HotelMapper;
import com.example.hotelsinfoapp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public List<HotelShortDto> getHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::hotelToShortDto)
                .toList();
    }
}