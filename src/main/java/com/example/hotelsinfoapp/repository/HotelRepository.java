package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.model.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @EntityGraph(attributePaths = {"address.street.city.country", "amenities"})
    List<Hotel> findAll();

    @EntityGraph(attributePaths = {"address.street.city.country", "amenities"})
    List<Hotel> findAll(Specification<Hotel> spec);

    @Query("SELECT h.brand AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.brand")
    List<HistogramDto> groupHotelsByBrand();
}