package com.starwars.resistance.api.v1.mapper;

import com.starwars.resistance.api.v1.dto.input.RebelInputDto;
import com.starwars.resistance.api.v1.dto.output.RebelOutputDto;
import com.starwars.resistance.api.v1.dto.output.RebelSummaryOutputDto;
import com.starwars.resistance.domain.model.Rebel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RebelMapper {

    private final ModelMapper modelMapper;

    public RebelMapper(final ModelMapper modelMapper) {
        this. modelMapper = modelMapper;
    }

    public Rebel toModel(final RebelInputDto rebelInputDto) {
        return modelMapper.map(rebelInputDto, Rebel.class);
    }

    public RebelOutputDto toDto(final Rebel rebel) {
        return modelMapper.map(rebel, RebelOutputDto.class);
    }

    public RebelSummaryOutputDto toSummaryDto(final Rebel rebel) {
        return modelMapper.map(rebel, RebelSummaryOutputDto.class);
    }

    public List<RebelSummaryOutputDto> toSummaryDtoList(final List<Rebel> rebels) {
        return rebels.stream()
            .map(this::toSummaryDto)
            .collect(Collectors.toList());
    }

}
