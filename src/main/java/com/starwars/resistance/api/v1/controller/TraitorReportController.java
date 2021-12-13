package com.starwars.resistance.api.v1.controller;

import com.starwars.resistance.api.v1.controller.openapi.TraitorReportOpenApi;
import com.starwars.resistance.api.v1.dto.input.TraitorReportInputDto;
import com.starwars.resistance.api.v1.mapper.TraitorReportMapper;
import com.starwars.resistance.domain.model.TraitorReport;
import com.starwars.resistance.domain.service.TraitorReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/traitors", produces = MediaType.APPLICATION_JSON_VALUE)
public class TraitorReportController implements TraitorReportOpenApi {

    private final TraitorReportService traitorReportService;
    private final TraitorReportMapper traitorReportMapper;

    public TraitorReportController(final TraitorReportService traitorReportService,
        final TraitorReportMapper traitorReportMapper) {
        this.traitorReportService = traitorReportService;
        this.traitorReportMapper = traitorReportMapper;
    }

    @PostMapping("/reporting")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void reportingTraitor(@RequestBody @Valid TraitorReportInputDto traitorReportInputDto) {
        TraitorReport traitorReport = traitorReportMapper.toModel(traitorReportInputDto);
        traitorReportService.save(traitorReport);
    }

}
