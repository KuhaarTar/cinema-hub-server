package com.onlinecinema.controller;

import com.onlinecinema.dto.ReportDto;
import com.onlinecinema.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportDto> getReport() {
        return ResponseEntity.ok(reportService.generateReport());
    }
}

