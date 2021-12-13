package com.starwars.resistance.api.v1.controller;

import com.starwars.resistance.api.v1.controller.openapi.LocalizationOpenApi;
import com.starwars.resistance.api.v1.dto.input.LocalizationInputDto;
import com.starwars.resistance.api.v1.dto.output.RebelOutputDto;
import com.starwars.resistance.api.v1.mapper.RebelMapper;
import com.starwars.resistance.domain.model.Rebel;
import com.starwars.resistance.domain.service.RebelService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/rebels/{id}/localizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocalizationController implements LocalizationOpenApi {

    private final RebelService rebelService;
    private final RebelMapper rebelMapper;

    public LocalizationController(final RebelService rebelService, final RebelMapper rebelMapper) {
        this.rebelService = rebelService;
        this.rebelMapper = rebelMapper;
    }

    @PutMapping
    @Override
    public ResponseEntity<RebelOutputDto> updateLocalization(@PathVariable String id, @RequestBody @Valid LocalizationInputDto localizationInputDto) {
        Rebel rebel = rebelService.findById(id);
        BeanUtils.copyProperties(localizationInputDto, rebel.getLocalization());
        RebelOutputDto rebelOutputDto = rebelMapper.toDto(rebelService.save(rebel));
        return ResponseEntity.ok(rebelOutputDto);
    }

}
