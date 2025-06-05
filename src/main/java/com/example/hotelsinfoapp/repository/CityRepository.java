package com.example.hotelsinfoapp.repository;

import com.example.hotelsinfoapp.model.City;
import com.example.hotelsinfoapp.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameAndCountry(String city, Country country);
}