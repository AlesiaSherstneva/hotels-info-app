package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HistogramControllerIntegrationTest extends BaseControllerTest {
    private final String BASE_URI = "/histogram";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getHistogramByBrandTest() throws Exception {
        Map<String, Integer> expectedResult = hotelRepository.groupHotelsByBrand()
                .stream()
                .collect(Collectors.toMap(HistogramDto::getKey, HistogramDto::getValue));

        MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/brand"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        parseResponseAndCheckEquality(expectedResult, mvcResult);
    }

    @Test
    void getHistogramByCityTest() throws Exception {
        Map<String, Integer> expectedResult = cityRepository.groupHotelsByCity()
                .stream()
                .collect(Collectors.toMap(HistogramDto::getKey, HistogramDto::getValue));

        MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/city"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        parseResponseAndCheckEquality(expectedResult, mvcResult);
    }

    @Test
    void getHistogramByCountryTest() throws Exception {
        Map<String, Integer> expectedResult = countryRepository.groupHotelsByCountry()
                .stream()
                .collect(Collectors.toMap(HistogramDto::getKey, HistogramDto::getValue));

        MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/country"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        parseResponseAndCheckEquality(expectedResult, mvcResult);
    }

    @Test
    void getHistogramByAmenitiesTest() throws Exception {
        Map<String, Integer> expectedResult = amenityRepository.groupHotelsByAmenity()
                .stream()
                .collect(Collectors.toMap(HistogramDto::getKey, HistogramDto::getValue));

        MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/amenities"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        parseResponseAndCheckEquality(expectedResult, mvcResult);
    }

    @Test
    void getHistogramByIncorrectParamTest() throws Exception {
        String incorrectParam = "address";
        String requestURI = BASE_URI + "/" + incorrectParam;

        mockMvc.perform(get(requestURI))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message", is("Unsupported histogram parameter: " + incorrectParam)),
                        jsonPath("$.status", is(400)),
                        jsonPath("$.path", containsString(requestURI)),
                        jsonPath("$.timestamp").exists()
                );
    }

    private void parseResponseAndCheckEquality(Map<String, Integer> expectedResult, MvcResult mvcResult) throws Exception {
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Map<String, Integer> actualResult = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}