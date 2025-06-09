package com.example.hotelsinfoapp.service;

import com.example.hotelsinfoapp.dto.HotelCreateDto;
import com.example.hotelsinfoapp.dto.HotelFullDto;
import com.example.hotelsinfoapp.dto.HotelSearchFilter;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.dto.mapper.HotelMapper;
import com.example.hotelsinfoapp.model.Address;
import com.example.hotelsinfoapp.model.Amenity;
import com.example.hotelsinfoapp.model.City;
import com.example.hotelsinfoapp.model.Country;
import com.example.hotelsinfoapp.model.Hotel;
import com.example.hotelsinfoapp.model.Street;
import com.example.hotelsinfoapp.repository.AmenityRepository;
import com.example.hotelsinfoapp.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public List<HotelShortDto> getHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::hotelToShortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelFullDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel with id " + id + " not found"));

        return hotelMapper.hotelToFullDto(hotel);
    }

    @Transactional(readOnly = true)
    public List<HotelShortDto> searchHotels(HotelSearchFilter filter) {
        Specification<Hotel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // предполагаю, что поиск по name и brand производится через свободный ввод данных
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getBrand() != null && !filter.getBrand().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("brand")), "%" + filter.getBrand().toLowerCase() + "%"));
            }

            // предполагаю, что поиск по city и country производится через выбор из выпадающего списка
            if (filter.getCity() != null && !filter.getCity().isBlank()) {
                Join<Hotel, Address> addressJoin = root.join("address");
                Join<Address, Street> streetJoin = addressJoin.join("street");
                Join<Street, City> cityJoin = streetJoin.join("city");
                predicates.add(cb.equal(
                        cb.lower(cityJoin.get("name")),
                        filter.getCity().toLowerCase()));
            }

            if (filter.getCountry() != null && !filter.getCountry().isBlank()) {
                Join<Hotel, Address> addressJoin = root.join("address");
                Join<Address, Street> streetJoin = addressJoin.join("street");
                Join<Street, City> cityJoin = streetJoin.join("city");
                Join<City, Country> countryJoin = cityJoin.join("country");
                predicates.add(cb.equal(
                        cb.lower(countryJoin.get("name")),
                        filter.getCountry().toLowerCase()));
            }

            // предполагаю, что поиск по amenities производится через выбор чек-боксов
            if (filter.getAmenities() != null && !filter.getAmenities().isEmpty()) {
                for (String amenity : filter.getAmenities()) {
                    Subquery<Long> subQuery = query.subquery(Long.class);
                    Root<Hotel> subRoot = subQuery.from(Hotel.class);
                    Join<Hotel, Amenity> subAmenities = subRoot.join("amenities");

                    subQuery.select(cb.literal(1L))
                            .where(cb.equal(subRoot.get("id"), root.get("id")),
                                    cb.equal(subAmenities.get("name"), amenity));

                    predicates.add(cb.exists(subQuery));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return hotelRepository.findAll(spec)
                .stream()
                .map(hotelMapper::hotelToShortDto)
                .toList();
    }

    @Transactional
    public HotelShortDto createHotel(HotelCreateDto hotelCreateDto) {
        Hotel newHotel = hotelMapper.createDtoToHotel(hotelCreateDto);

        Hotel savedHotel = hotelRepository.save(newHotel);

        return hotelMapper.hotelToShortDto(savedHotel);
    }

    @Transactional
    public void addAmenities(Long id, Set<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel with id " + id + " not found"));

        // предполагаю, что amenities для добавления выбираются с помощью чек-боксов
        amenities.removeAll(hotel.getAmenities()
                .stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet()));

        Set<Amenity> newAmenities = amenityRepository.findByNameIn(amenities);

        hotel.getAmenities().addAll(newAmenities);
        hotelRepository.save(hotel);
    }
}