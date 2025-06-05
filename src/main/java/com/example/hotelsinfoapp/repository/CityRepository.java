package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.model.City;
import com.example.hotelsinfoapp.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameAndCountry(String city, Country country);

    @Query("SELECT c.name AS key, COUNT(h) AS value FROM City c " +
            "LEFT JOIN c.streets s LEFT JOIN s.addresses a " +
            "LEFT JOIN a.hotel h GROUP BY c.name")
    List<HistogramDto> groupHotelsByCity();
}