package com.starwars.resistance.api.v1.controller;

import com.starwars.resistance.api.v1.controller.openapi.ReportSummaryOpenApi;
import com.starwars.resistance.api.v1.dto.output.ReportSummaryOutputDto;
import com.starwars.resistance.domain.service.ReportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportSummaryController implements ReportSummaryOpenApi {

    private final ReportService reportService;

    public ReportSummaryController(final ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<ReportSummaryOutputDto> getReport() {
        return ResponseEntity.ok(reportService.generateAndGet());
    }

}
