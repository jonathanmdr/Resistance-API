package com.starwars.resistance.api.v1.controller.openapi;

import com.starwars.resistance.api.v1.dto.input.LocalizationInputDto;
import com.starwars.resistance.api.v1.dto.output.ApiErrorDto;
import com.starwars.resistance.api.v1.dto.output.RebelOutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface LocalizationOpenApi {

    @Operation(summary = "Does make update localization of one rebel")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Localization updated with success", content = @Content(
            schema = @Schema(implementation = RebelOutputDto.class)
        )),
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
    ResponseEntity<RebelOutputDto> updateLocalization(String id, LocalizationInputDto localizationInputDto);

}
