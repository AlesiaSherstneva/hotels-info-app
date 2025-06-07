package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.repository.AmenityRepository;
import com.example.hotelsinfoapp.repository.CityRepository;
import com.example.hotelsinfoapp.repository.CountryRepository;
import com.example.hotelsinfoapp.repository.HotelRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.Random.class)
public class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected HotelRepository hotelRepository;

    @Autowired
    protected CityRepository cityRepository;

    @Autowired
    protected CountryRepository countryRepository;

    @Autowired
    protected AmenityRepository amenityRepository;

    protected static final Random RANDOM = new Random();
}