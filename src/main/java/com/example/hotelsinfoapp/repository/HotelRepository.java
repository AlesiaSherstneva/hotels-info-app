package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}