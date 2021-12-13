package com.starwars.resistance.api.v1.controller.openapi;

import com.starwars.resistance.api.v1.dto.input.RebelInputDto;
import com.starwars.resistance.api.v1.dto.output.ApiErrorDto;
import com.starwars.resistance.api.v1.dto.output.RebelOutputDto;
import com.starwars.resistance.api.v1.dto.output.RebelSummaryOutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RebelOpenApi {

    @Operation(summary = "Does find all rebels registered")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Found all with success", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = RebelSummaryOutputDto.class))
        )),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        ))
    })
    ResponseEntity<List<RebelSummaryOutputDto>> findAll();

    @Operation(summary = "Does find one rebel by ID")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Found one with success", content = @Content(
            schema = @Schema(implementation = RebelOutputDto.class)
        )),
        @ApiResponse(responseCode = "404", description = "Rebel ID not found", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        ))
    })
    ResponseEntity<RebelOutputDto> findById(String id);

    @Operation(summary = "Does save a new rebel")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Found one with success", content = @Content(
            schema = @Schema(implementation = RebelOutputDto.class)
        )),
        @ApiResponse(responseCode = "400", description = "Payload is invalid", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        ))
    })
    ResponseEntity<RebelOutputDto> save(RebelInputDto rebelInputDto);

}
