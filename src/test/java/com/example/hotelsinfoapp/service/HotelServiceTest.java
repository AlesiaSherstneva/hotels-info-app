package com.example.hotelsinfoapp.service;

import com.example.hotelsinfoapp.dto.HotelCreateDto;
import com.example.hotelsinfoapp.dto.HotelFullDto;
import com.example.hotelsinfoapp.dto.HotelSearchFilter;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.dto.mapper.HotelMapper;
import com.example.hotelsinfoapp.model.Amenity;
import com.example.hotelsinfoapp.model.Hotel;
import com.example.hotelsinfoapp.repository.AmenityRepository;
import com.example.hotelsinfoapp.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelService hotelService;

    private static List<Hotel> testHotels;

    @BeforeAll
    static void beforeAll() {
        testHotels = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            testHotels.add(mock(Hotel.class));
        }
    }

    @Test
    void getHotelsReturnsListOfHotelsTest() {
        when(hotelRepository.findAll()).thenReturn(testHotels);
        when(hotelMapper.hotelToShortDto(any(Hotel.class)))
                .thenReturn(mock(HotelShortDto.class));

        List<HotelShortDto> receivedHotels = hotelService.getHotels();

        assertThat(receivedHotels).isNotEmpty().hasSize(testHotels.size());

        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, times(testHotels.size())).hotelToShortDto(any(Hotel.class));
    }

    @Test
    void getHotelsReturnsEmptyListTest() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<HotelShortDto> receivedHotels = hotelService.getHotels();

        assertThat(receivedHotels).isEmpty();

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void getHotelByIdReturnExistingHotelTest() {
        Hotel testHotel = Hotel.builder().id(1L).build();
        HotelFullDto expectedDto = mock(HotelFullDto.class);

        when(hotelRepository.findById(testHotel.getId())).thenReturn(Optional.of(testHotel));
        when(hotelMapper.hotelToFullDto(testHotel)).thenReturn(expectedDto);

        HotelFullDto receivedDto = hotelService.getHotelById(testHotel.getId());

        assertThat(receivedDto).isNotNull().isEqualTo(expectedDto);

        verify(hotelRepository, times(1)).findById(testHotel.getId());
        verify(hotelMapper, times(1)).hotelToFullDto(testHotel);
    }

    @Test
    void getHotelByIdDoesNotExistTest() {
        long nonExistingId = 1L;

        when(hotelRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Exception receivedException = assertThrows(
                Exception.class, () -> hotelService.getHotelById(nonExistingId)
        );

        assertThat(receivedException).isInstanceOf(EntityNotFoundException.class);
        assertThat(receivedException.getMessage()).isEqualTo("Hotel with id " + nonExistingId + " not found");

        verify(hotelRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @SuppressWarnings("unchecked")
    void searchHotelsWithVariousParamsTest() {
        HotelSearchFilter singleParamFilter = HotelSearchFilter.builder()
                .name("Test name").build();
        HotelSearchFilter doubleParamsFilter = HotelSearchFilter.builder()
                .brand("Test brand").country("Test country").build();
        HotelSearchFilter amenitiesParamFilter = HotelSearchFilter.builder()
                .amenities(Set.of("Test amenity 1", "Test amenity 2")).build();
        HotelSearchFilter filterWithoutParams = HotelSearchFilter.builder().build();

        List<HotelSearchFilter> searchFilters = List.of(
                singleParamFilter, doubleParamsFilter, amenitiesParamFilter, filterWithoutParams
        );

        for (HotelSearchFilter filter : searchFilters) {
            when(hotelRepository.findAll(any(Specification.class))).thenReturn(testHotels);
            when(hotelMapper.hotelToShortDto(any(Hotel.class))).thenReturn(mock(HotelShortDto.class));

            List<HotelShortDto> receivedHotels = hotelService.searchHotels(filter);

            assertThat(receivedHotels).isNotEmpty().hasSize(testHotels.size());
        }

        verify(hotelRepository, times(searchFilters.size())).findAll(any(Specification.class));
        verify(hotelMapper, times(testHotels.size() * searchFilters.size())).hotelToShortDto(any(Hotel.class));
    }

    @Test
    void createNewHotelSuccessfulTest() {
        HotelCreateDto hotelDtoToCreate = mock(HotelCreateDto.class);
        Hotel newHotel = mock(Hotel.class);
        HotelShortDto createdHotelDto = mock(HotelShortDto.class);

        when(hotelMapper.createDtoToHotel(any(HotelCreateDto.class))).thenReturn(newHotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(newHotel);
        when(hotelMapper.hotelToShortDto(any(Hotel.class))).thenReturn(createdHotelDto);

        HotelShortDto savedHotel = hotelService.createHotel(hotelDtoToCreate);

        assertThat(savedHotel).isNotNull().isEqualTo(createdHotelDto);

        verify(hotelMapper, times(1)).createDtoToHotel(hotelDtoToCreate);
        verify(hotelRepository, times(1)).save(newHotel);
        verify(hotelMapper, times(1)).hotelToShortDto(newHotel);
    }

    @Test
    void addAmenitiesToExistingHotelTest() {
        Amenity firstAmenity = Amenity.builder().name("Test amenity 1").build();
        Amenity secondAmenity = Amenity.builder().name("Test amenity 2").build();
        Amenity thirdAmenity = Amenity.builder().name("Test amenity 3").build();

        Hotel existingHotel = Hotel.builder().id(1L)
                .amenities(new HashSet<>(Set.of(firstAmenity, secondAmenity))).build();

        Set<String> newAmenitiesNames = new HashSet<>(Set.of(secondAmenity.getName(), thirdAmenity.getName()));

        when(hotelRepository.findById(existingHotel.getId())).thenReturn(Optional.of(existingHotel));
        when(amenityRepository.findByNameIn(newAmenitiesNames)).thenReturn(Set.of(secondAmenity, thirdAmenity));
        when(hotelRepository.save(existingHotel)).thenReturn(existingHotel);

        hotelService.addAmenities(existingHotel.getId(), newAmenitiesNames);

        assertThat(existingHotel.getAmenities()).isNotEmpty().hasSize(3)
                .extracting(Amenity::getName)
                .containsExactlyInAnyOrder(firstAmenity.getName(), secondAmenity.getName(), thirdAmenity.getName());
        assertThat(existingHotel.getAmenities())
                .filteredOn(a -> a.getName().equals(secondAmenity.getName()))
                .hasSize(1);

        verify(hotelRepository).findById(existingHotel.getId());
        verify(amenityRepository).findByNameIn(newAmenitiesNames);
        verify(hotelRepository).save(existingHotel);
    }

    @Test
    void addAmenitiesToNonExistingHotelTest() {
        long nonExistingId = 1L;

        when(hotelRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Exception receivedException = assertThrows(
                Exception.class, () -> hotelService.addAmenities(nonExistingId, Set.of())
        );

        assertThat(receivedException).isInstanceOf(EntityNotFoundException.class);
        assertThat(receivedException.getMessage()).isEqualTo("Hotel with id " + nonExistingId + " not found");

        verify(hotelRepository, times(1)).findById(nonExistingId);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(hotelRepository, amenityRepository, hotelMapper);
    }
}