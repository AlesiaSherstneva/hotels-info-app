package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.model.Amenity;
import com.example.hotelsinfoapp.model.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchControllerIntegrationTest extends BaseControllerTest {
    private final String URI = "/search";

    @Test
    void searchHotelsByNameSuccessfulTest() throws Exception {
        String existingHotelName = createParamValue("name");

        mockMvc.perform(get(URI)
                        .param("name", existingHotelName))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$[*].name", everyItem(containsString(existingHotelName)))
                );
    }

    @Test
    void searchHotelsByBrandSuccessfulTest() throws Exception {
        String existingHotelBrand = createParamValue("brand");

        mockMvc.perform(get(URI)
                        .param("brand", existingHotelBrand))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty()
                );
    }

    @Test
    void searchHotelsByCitySuccessfulTest() throws Exception {
        String existingCity = createParamValue("city");

        mockMvc.perform(get(URI)
                        .param("city", existingCity))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$[*].address", everyItem(containsString(existingCity)))
                );
    }

    @Test
    void searchHotelsByCountrySuccessfulTest() throws Exception {
        String existingCountry = createParamValue("country");

        mockMvc.perform(get(URI)
                        .param("country", existingCountry))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$[*].address", everyItem(containsString(existingCountry)))
                );
    }

    @Test
    void searchHotelsByAmenitiesSuccessfulTest() throws Exception {
        String existingAmenities = createParamValue("amenities");

        mockMvc.perform(get(URI)
                        .param("amenities", existingAmenities))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray()
                );
    }

    @Test
    void searchHotelsWithCombinationOfParams() throws Exception {
        List<String> params = Arrays.asList("name", "brand", "city", "country", "amenities");
        Collections.shuffle(params);

        mockMvc.perform(get(URI)
                        .param(params.get(0), createParamValue(params.get(0)))
                        .param(params.get(1), createParamValue(params.get(1))))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray()
                );
    }

    @Test
    void searchHotelsWithoutAnyParams() throws Exception {
        List<Hotel> hotelInDb = hotelRepository.findAll();

        mockMvc.perform(get(URI))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$", hasSize(hotelInDb.size()))
                );
    }

    private String createParamValue(String parameter) {
        switch (parameter) {
            case "name", "brand", "city", "country": {
                List<Hotel> hotelsInDb = hotelRepository.findAll();
                assertThat(hotelsInDb).isNotEmpty();

                Hotel hotelToBeFound = hotelsInDb.get(RANDOM.nextInt(hotelsInDb.size()));
                assertThat(hotelToBeFound).isNotNull();

                switch (parameter) {
                    case "name":
                        return hotelToBeFound.getName();
                    case "brand":
                        return hotelToBeFound.getBrand();
                    case "city":
                        return hotelToBeFound.getAddress().getStreet().getCity().getName();
                    case "country":
                        return hotelToBeFound.getAddress().getStreet().getCity().getCountry().getName();
                }
            }
            case "amenities": {
                List<Amenity> amenitiesInDb = amenityRepository.findAll();
                Collections.shuffle(amenitiesInDb);

                return String.join(",", amenitiesInDb.get(0).getName(), amenitiesInDb.get(1).getName());
            }
        }
        return null;
    }
}