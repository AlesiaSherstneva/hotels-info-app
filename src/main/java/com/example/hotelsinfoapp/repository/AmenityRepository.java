package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    List<Amenity> findByNameIn(List<String> names);
}