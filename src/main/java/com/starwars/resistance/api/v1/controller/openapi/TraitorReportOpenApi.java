package com.starwars.resistance.api.v1.controller.openapi;

import com.starwars.resistance.api.v1.dto.input.TraitorReportInputDto;
import com.starwars.resistance.api.v1.dto.output.ApiErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface TraitorReportOpenApi {

    @Operation(summary = "Does make a report of one traitor")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "204", description = "Reported with success"),
        @ApiResponse(responseCode = "400", description = "Payload is invalid", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        )),
        @ApiResponse(responseCode = "404", description = "Rebel ID not found", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        ))
    })
    void reportingTraitor(TraitorReportInputDto traitorReportInputDto);

}
