package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.model.City;
import com.example.hotelsinfoapp.model.Street;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreetRepository extends JpaRepository<Street, Integer> {
    Optional<Street> findByNameAndCity(String street, City city);
}