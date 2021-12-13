package com.starwars.resistance.api.v1.controller.openapi;

import com.starwars.resistance.api.v1.dto.output.ApiErrorDto;
import com.starwars.resistance.api.v1.dto.output.ReportSummaryOutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface ReportSummaryOpenApi {

    @Operation(summary = "Does get report general statistics")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Report generated with success", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = ReportSummaryOutputDto.class))
        )),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        ))
    })
    ResponseEntity<ReportSummaryOutputDto> getReport();

}
