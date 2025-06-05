package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.model.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @EntityGraph(attributePaths = {"address.street.city.country"})
    List<Hotel> findAll(Specification<Hotel> spec);
}