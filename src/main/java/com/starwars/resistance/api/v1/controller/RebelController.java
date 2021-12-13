package com.starwars.resistance.api.v1.controller;

import com.starwars.resistance.api.v1.controller.openapi.RebelOpenApi;
import com.starwars.resistance.api.v1.dto.input.RebelInputDto;
import com.starwars.resistance.api.v1.dto.output.RebelOutputDto;
import com.starwars.resistance.api.v1.dto.output.RebelSummaryOutputDto;
import com.starwars.resistance.api.v1.mapper.RebelMapper;
import com.starwars.resistance.api.v1.util.ResourceUriHelper;
import com.starwars.resistance.domain.model.Rebel;
import com.starwars.resistance.domain.service.RebelService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/rebels", produces = MediaType.APPLICATION_JSON_VALUE)
public class RebelController implements RebelOpenApi {

    private final RebelService rebelService;
    private final RebelMapper rebelMapper;

    public RebelController(final RebelService rebelService, final RebelMapper rebelMapper) {
        this.rebelService = rebelService;
        this.rebelMapper = rebelMapper;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<RebelSummaryOutputDto>> findAll() {
        List<RebelSummaryOutputDto> rebelSummaryOutputDtoList = rebelMapper.toSummaryDtoList(rebelService.findAllWithoutTraitors());
        return ResponseEntity.ok(rebelSummaryOutputDtoList);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<RebelOutputDto> findById(@PathVariable String id) {
        RebelOutputDto rebelOutputDto = rebelMapper.toDto(rebelService.findById(id));
        return ResponseEntity.ok(rebelOutputDto);
    }

    @PostMapping
    @Override
    public ResponseEntity<RebelOutputDto> save(@RequestBody @Valid RebelInputDto rebelInputDto) {
        Rebel rebel = rebelMapper.toModel(rebelInputDto);
        RebelOutputDto rebelOutputDto = rebelMapper.toDto(rebelService.save(rebel));
        return ResponseEntity.created(ResourceUriHelper.getUri(rebelOutputDto.getId())).body(rebelOutputDto);
    }

}
