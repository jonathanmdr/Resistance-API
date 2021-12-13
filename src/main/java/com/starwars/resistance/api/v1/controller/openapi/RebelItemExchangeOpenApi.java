package com.starwars.resistance.api.v1.controller.openapi;

import com.starwars.resistance.api.v1.dto.input.ItemExchangeInputDto;
import com.starwars.resistance.api.v1.dto.output.ApiErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface RebelItemExchangeOpenApi {

    @Operation(summary = "Does make exchange of items between rebels")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "204", description = "Exchange applied with success"),
        @ApiResponse(responseCode = "400", description = "Payload is invalid", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        )),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(
            schema = @Schema(implementation = ApiErrorDto.class)
        ))
    })
    void exchange(ItemExchangeInputDto itemExchangeInputDto);

}
