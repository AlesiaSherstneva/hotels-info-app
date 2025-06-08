package com.example.hotelsinfoapp.service;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.repository.AmenityRepository;
import com.example.hotelsinfoapp.repository.CityRepository;
import com.example.hotelsinfoapp.repository.CountryRepository;
import com.example.hotelsinfoapp.repository.HotelRepository;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistogramServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private HistogramService histogramService;

    private static final Random RANDOM = new Random();

    @Test
    void getHistogramByBrandTest() {
        List<HistogramDto> histogramDtos = List.of(
                HistogramDtoImpl.builder().key("Test brand 1").value(RANDOM.nextInt(100)).build(),
                HistogramDtoImpl.builder().key("Test brand 2").value(RANDOM.nextInt(100)).build()
        );

        when(hotelRepository.groupHotelsByBrand()).thenReturn(histogramDtos);

        Map<String, Integer> receivedHistogram = histogramService.getHistogram("brand");

        checkTestAssertion(histogramDtos, receivedHistogram);

        verify(hotelRepository, times(1)).groupHotelsByBrand();
    }

    @Test
    void getHistogramByCityTest() {
        List<HistogramDto> histogramDtos = List.of(
                HistogramDtoImpl.builder().key("Test city 1").value(RANDOM.nextInt(100)).build(),
                HistogramDtoImpl.builder().key("Test city 2").value(RANDOM.nextInt(100)).build()
        );

        when(cityRepository.groupHotelsByCity()).thenReturn(histogramDtos);

        Map<String, Integer> receivedHistogram = histogramService.getHistogram("city");

        checkTestAssertion(histogramDtos, receivedHistogram);

        verify(cityRepository, times(1)).groupHotelsByCity();
    }

    @Test
    void getHistogramByCountryTest() {
        List<HistogramDto> histogramDtos = List.of(
                HistogramDtoImpl.builder().key("Test country 1").value(RANDOM.nextInt(100)).build(),
                HistogramDtoImpl.builder().key("Test country 2").value(RANDOM.nextInt(100)).build()
        );

        when(countryRepository.groupHotelsByCountry()).thenReturn(histogramDtos);

        Map<String, Integer> receivedHistogram = histogramService.getHistogram("country");

        checkTestAssertion(histogramDtos, receivedHistogram);

        verify(countryRepository, times(1)).groupHotelsByCountry();
    }

    @Test
    void getHistogramByAmenitiesTest() {
        List<HistogramDto> histogramDtos = List.of(
                HistogramDtoImpl.builder().key("Test amenity 1").value(RANDOM.nextInt(100)).build(),
                HistogramDtoImpl.builder().key("Test amenity 2").value(RANDOM.nextInt(100)).build()
        );

        when(amenityRepository.groupHotelsByAmenity()).thenReturn(histogramDtos);

        Map<String, Integer> receivedHistogram = histogramService.getHistogram("amenities");

        checkTestAssertion(histogramDtos, receivedHistogram);

        verify(amenityRepository, times(1)).groupHotelsByAmenity();
    }

    @Test
    void getHistogramByIncorrectParameterTest() {
        String incorrectParam = "incorrect";

        Exception receivedException = assertThrows(
                Exception.class, () -> histogramService.getHistogram(incorrectParam)
        );

        assertThat(receivedException).isInstanceOf(IllegalArgumentException.class);
        assertThat(receivedException.getMessage()).isEqualTo("Unsupported histogram parameter: " + incorrectParam);
    }

    private static void checkTestAssertion(List<HistogramDto> histogramDtos, Map<String, Integer> receivedHistogram) {
        Map<String, Integer> expectedMap = Map.of(
                histogramDtos.get(0).getKey(), histogramDtos.get(0).getValue(),
                histogramDtos.get(1).getKey(), histogramDtos.get(1).getValue()
        );
        assertThat(receivedHistogram).containsExactlyInAnyOrderEntriesOf(expectedMap);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(hotelRepository, cityRepository, countryRepository, amenityRepository);
    }

    @Getter
    @Builder
    private static class HistogramDtoImpl implements HistogramDto {
        private final String key;
        private final Integer value;
    }
}