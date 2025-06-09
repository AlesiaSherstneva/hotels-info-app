package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.dto.ErrorResponseDto;
import com.example.hotelsinfoapp.dto.HotelCreateDto;
import com.example.hotelsinfoapp.dto.HotelFullDto;
import com.example.hotelsinfoapp.dto.HotelShortDto;
import com.example.hotelsinfoapp.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotel Management", description = "Operations related to hotel management")
public class HotelController {
    private final HotelService hotelService;

    @Operation(summary = "Get all hotels", description = "Returns list of all existing hotels (short info)")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of hotels",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = HotelShortDto.class))
            )
    )
    @GetMapping
    public ResponseEntity<List<HotelShortDto>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());
    }

    @Operation(summary = "Get hotel by ID", description = "Returns single hotel by ID (full info)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved hotel",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelFullDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Hotel with id 999 not found",
                                                "status": 404,
                                                "path": "uri=/property-view/hotels/999",
                                                "timestamp": "2025-06-09T18:42:44.9708481+03:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelFullDto> getHotelsById(
            @Parameter(description = "ID of the hotel", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @Operation(summary = "Create new hotel", description = "Creates new hotel and returns it (short info)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Hotel data is not valid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "address.postCode - Invalid postal code format (6 digits required); brand - Hotel brand is required",
                                                "status": 400,
                                                "path": "uri=/property-view/hotels",
                                                "timestamp": "2025-06-09T18:48:19.8612474+03:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<HotelShortDto> createHotel(
            @Parameter(description = "Hotel creation data", required = true)
            @Valid @RequestBody HotelCreateDto hotelCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createHotel(hotelCreateDto));
    }

    @Operation(summary = "Add amenities to hotel", description = "Adds amenities to existing hotel")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities added successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Hotel with id 999 not found",
                                                "status": 404,
                                                "path": "uri=/property-view/hotels/999",
                                                "timestamp": "2025-06-09T18:42:44.9708481+03:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/{id}/amenities")
    public ResponseEntity<Void> addAmenities(
            @Parameter(description = "ID of the hotel to add amenities", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(description = "Set of amenities to add", required = true, example = "[\"Free parking\", \"Free WiFi\"]")
            @Schema(description = "Set of amenities to add", example = "[\"Free WiFi\", \"Swimming pool\", \"Free parking\"]")
            @RequestBody Set<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }
}