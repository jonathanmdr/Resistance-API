package com.starwars.resistance.api.v1.mapper;

import com.starwars.resistance.api.v1.dto.input.TraitorReportInputDto;
import com.starwars.resistance.domain.model.TraitorReport;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TraitorReportMapper {

    private final ModelMapper modelMapper;

    public TraitorReportMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TraitorReport toModel(final TraitorReportInputDto traitorReportInputDto) {
        return modelMapper.map(traitorReportInputDto, TraitorReport.class);
    }

}
