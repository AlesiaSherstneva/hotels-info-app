package com.example.hotelsinfoapp.controller;

import com.example.hotelsinfoapp.service.HistogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/histogram")
@RequiredArgsConstructor
public class HistogramController {
    private final HistogramService histogramService;

    @GetMapping("/{param}")
    public ResponseEntity<Map<String, Integer>> getHistogram(@PathVariable String param) {
        return ResponseEntity.ok(histogramService.getHistogram(param));
    }
}