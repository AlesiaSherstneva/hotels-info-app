package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.model.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HotelControllerIntegrationTest extends BaseControllerTest {
    private static final String BASE_URI = "/hotels";

    @Test
    void getHotelsSuccessfulTest() throws Exception {
        List<Hotel> hotelsInDb = hotelRepository.findAll();
        assertThat(hotelsInDb).isNotEmpty();

        mockMvc.perform(get(BASE_URI))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$", hasSize(hotelsInDb.size())),
                        jsonPath("$[0].name", is(hotelsInDb.get(0).getName())),
                        jsonPath("$[1].phone", is(hotelsInDb.get(1).getPhone())),
                        jsonPath("$[2].address", allOf(
                                containsString(hotelsInDb.get(2).getAddress().getHouseNumber()),
                                containsString(hotelsInDb.get(2).getAddress().getStreet().getName()),
                                containsString(hotelsInDb.get(2).getAddress().getStreet().getCity().getName()),
                                containsString(hotelsInDb.get(2).getAddress().getStreet().getCity().getCountry().getName())))
                );
    }

    @Test
    void getHotelByIdSuccessfulTest() throws Exception {
        List<Hotel> hotelsInDb = hotelRepository.findAll();
        assertThat(hotelsInDb).isNotEmpty();

        Hotel receivedHotel = hotelsInDb.get(RANDOM.nextInt(hotelsInDb.size()));
        assertThat(receivedHotel).isNotNull();

        String requestURI = BASE_URI + "/" + receivedHotel.getId();

        mockMvc.perform(get(requestURI))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(receivedHotel.getId().intValue())),
                        jsonPath("$.name", is(receivedHotel.getName())),
                        jsonPath("$.brand", is(receivedHotel.getBrand())),
                        jsonPath("$.address.street", is(receivedHotel.getAddress().getStreet().getName())),
                        jsonPath("$.address.city", is(receivedHotel.getAddress().getStreet().getCity().getName())),
                        jsonPath("$.address.country", is(receivedHotel.getAddress().getStreet().getCity().getCountry().getName())),
                        jsonPath("$.contacts.email", is(receivedHotel.getEmail())),
                        jsonPath("$.arrivalTime.checkIn", is(receivedHotel.getCheckIn().toString())),
                        jsonPath("$.amenities", hasSize(receivedHotel.getAmenities().size()))
                );
    }

    @Test
    void getHotelByIdDoesNotExistTest() throws Exception {
        long nonExistingId = hotelRepository.findAll().size() + 1;
        assertThat(hotelRepository.findById(nonExistingId)).isEmpty();

        String requestURI = BASE_URI + "/" + nonExistingId;

        mockMvc.perform(get(requestURI))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message", is("Hotel with id " + nonExistingId + " not found")),
                        jsonPath("$.status", is(404)),
                        jsonPath("$.path", containsString(requestURI)),
                        jsonPath("$.timestamp").exists()
                );

    }

    @Test
    @Transactional
    void createHotelSuccessfulTest() throws Exception {
        String name = "New Hotel";
        String address = "\"houseNumber\": 1, \"street\": \"Test Street\", \"city\": \"TestCity2\"," +
                "\"country\": \"TestCountry1\", \"postCode\": \"123456\"";
        String phone = "+7 000 000-00-00";
        String email = "newhotel@test.com";

        String hotelJson = createHotelJson(name, address, phone, email);
        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelJson))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").exists(),
                        jsonPath("$.name", is(name)),
                        jsonPath("$.address", allOf(
                                containsString("Test Street"),
                                containsString("TestCity2"),
                                containsString("TestCountry1")
                        )),
                        jsonPath("$.phone").value(phone)
                );

        List<Hotel> hotelsInDb = hotelRepository.findAll();
        assertThat(hotelsInDb).isNotEmpty();

        Hotel savedHotel = hotelsInDb.get(hotelsInDb.size() - 1);
        assertAll(
                () -> assertThat(savedHotel).isNotNull(),
                () -> assertThat(savedHotel.getId()).isNotNull(),
                () -> assertThat(savedHotel.getName()).isNotNull().isEqualTo(name),
                () -> assertThat(savedHotel.getPhone()).isNotNull().isEqualTo(phone),
                () -> assertThat(savedHotel.getEmail()).isNotNull().isEqualTo(email)
        );
    }

    @Test
    void createHotelWithEmptyFieldsTest() throws Exception {
        String name = "";
        String address = "";
        String phone = "+7 000 000-00-00";
        String email = "newhotel@test.com";

        String hotelJson = createHotelJson(name, address, phone, email);

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelJson))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message", allOf(
                                containsString("Hotel name is required"),
                                containsString("House number is required"),
                                containsString("Street name is required"),
                                containsString("City is required"),
                                containsString("Country is required"),
                                containsString("Post code is required")
                        )),
                        jsonPath("$.status", is(400)),
                        jsonPath("$.path", containsString(BASE_URI)),
                        jsonPath("$.timestamp").exists()
                );
    }

    @Test
    void createHotelWithNotValidFieldsTest() throws Exception {
        String name = "New Hotel";
        String address = "\"houseNumber\": 1, \"street\": \"Test Street\", \"city\": \"TestCity2\"," +
                "\"country\": \"TestCountry1\", \"postCode\": \"123456\"";
        String phone = "+70000000000";
        String email = "hotel.com";

        String hotelJson = createHotelJson(name, address, phone, email);

        mockMvc.perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelJson))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message", allOf(
                                containsString("Invalid email format"),
                                containsString("Invalid phone format")
                        )),
                        jsonPath("$.status", is(400)),
                        jsonPath("$.path", containsString(BASE_URI)),
                        jsonPath("$.timestamp").exists()
                );
    }

    @Test
    @Transactional
    void addAmenitiesToHotelSuccessfulTest() throws Exception {
        List<Hotel> hotelsInDb = hotelRepository.findAll();
        assertThat(hotelsInDb).isNotEmpty();

        Hotel receivedHotel = hotelsInDb.get(RANDOM.nextInt(hotelsInDb.size()));
        assertThat(receivedHotel).isNotNull();

        String[] amenitiesNames = getRandomAmenitiesFromDb();

        String requestURI = BASE_URI + "/" + receivedHotel.getId() + "/amenities";

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(amenitiesNames)))
                .andExpect(status().isOk());

        Hotel updatedHotel = hotelRepository.findById(receivedHotel.getId()).orElse(null);
        assertThat(updatedHotel).isNotNull();
        assertThat(updatedHotel.getAmenities().size())
                .isGreaterThanOrEqualTo(receivedHotel.getAmenities().size())
                .isGreaterThanOrEqualTo(amenitiesNames.length)
                .isLessThanOrEqualTo(receivedHotel.getAmenities().size() + amenitiesNames.length);
    }

    @Test
    void addAmenitiesToHotelDoesNotExistTest() throws Exception {
        long nonExistingId = hotelRepository.findAll().size() + 1;
        assertThat(hotelRepository.findById(nonExistingId)).isEmpty();

        String[] amenitiesNames = getRandomAmenitiesFromDb();

        String requestURI = BASE_URI + "/" + nonExistingId + "/amenities";

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Arrays.toString(amenitiesNames)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.message", is("Hotel with id " + nonExistingId + " not found")),
                        jsonPath("$.status", is(404)),
                        jsonPath("$.path", containsString(requestURI)),
                        jsonPath("$.timestamp").exists()
                );
    }

    private String createHotelJson(String name, String address, String phone, String email) {
        return String.format("""
                {
                    "name": "%s",
                    "brand": "New brand",
                    "address": {
                        %s
                    },
                    "contacts": {
                        "phone": "%s",
                        "email": "%s"
                    },
                    "arrivalTime": {
                        "checkIn": "14:00",
                        "checkIn": "12:00"
                    }
                }
                """, name, address, phone, email);
    }

    private String[] getRandomAmenitiesFromDb() {
        return amenityRepository.findAll().stream()
                .filter(a -> RANDOM.nextBoolean())
                .map(a -> "\"" + a.getName() + "\"")
                .toArray(String[]::new);
    }
}