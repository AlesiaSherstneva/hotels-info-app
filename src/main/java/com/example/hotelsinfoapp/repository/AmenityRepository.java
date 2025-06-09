package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    Set<Amenity> findByNameIn(Set<String> names);

    @Query("SELECT a.name AS key, COUNT(h) AS value FROM Amenity a " +
            "LEFT JOIN a.hotels h GROUP BY a.name")
    List<HistogramDto> groupHotelsByAmenity();
}