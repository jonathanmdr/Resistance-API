package com.starwars.resistance.api.v1.controller;

import com.starwars.resistance.api.v1.controller.openapi.RebelItemExchangeOpenApi;
import com.starwars.resistance.api.v1.dto.input.ItemExchangeInputDto;
import com.starwars.resistance.domain.service.ItemExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/exchanges", produces = MediaType.APPLICATION_JSON_VALUE)
public class RebelItemExchangeController implements RebelItemExchangeOpenApi {

    private final ItemExchangeService itemExchangeService;

    public RebelItemExchangeController(final ItemExchangeService itemExchangeService) {
        this.itemExchangeService = itemExchangeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void exchange(@RequestBody @Valid ItemExchangeInputDto itemExchangeInputDto) {
        itemExchangeService.exchange(itemExchangeInputDto);
    }

}
