package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.dto.HistogramDto;
import com.example.hotelsinfoapp.service.HistogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/histogram")
@RequiredArgsConstructor
@Tag(name = "Hotels Histograms", description = "Operation for generating and retrieving histogram data")
public class HistogramController {
    private final HistogramService histogramService;

    @Operation(summary = "Get histogram data", description = "Returns a histogram for the specified parameter")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histogram data retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistogramDto.class),
                            examples = @ExampleObject(
                                    name = "Histogram by city",
                                    value = """
                                            {
                                                "Minsk": 3,
                                                "Moscow": 2,
                                                "Mogilev": 0
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter value",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Unsupported histogram parameter: street",
                                                "status": 400,
                                                "path": "uri=/property-view/histogram/street",
                                                "timestamp": "2025-06-09T19:48:48.7325619+03:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{param}")
    public ResponseEntity<Map<String, Integer>> getHistogram(
            @Parameter(name = "parameter", description = "Parameter to generate a histogram",
                    example = "brand, city, county, amenities")
            @PathVariable String param) {
        return ResponseEntity.ok(histogramService.getHistogram(param));
    }
}