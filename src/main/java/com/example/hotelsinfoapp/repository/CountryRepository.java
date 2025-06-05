package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByName(String country);

    @Query("SELECT co.name AS key, COUNT(h) AS value FROM Country co " +
            "LEFT JOIN co.cities ci LEFT JOIN ci.streets s " +
            "LEFT JOIN s.addresses a LEFT JOIN a.hotel h " +
            "GROUP BY co.name")
    List<HistogramDto> groupHotelsByCountry();
}