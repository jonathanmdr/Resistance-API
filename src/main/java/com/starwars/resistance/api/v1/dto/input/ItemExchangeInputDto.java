package com.starwars.resistance.api.v1.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ItemExchangeInputDto {

    @NotNull
    @Valid
    private RebelInventoryInputDto rebelSource;

    @NotNull
    @Valid
    private RebelInventoryInputDto rebelTarget;

    public ItemExchangeInputDto() { }

    public ItemExchangeInputDto(final RebelInventoryInputDto rebelSource, final RebelInventoryInputDto rebelTarget) {
        this.rebelSource = rebelSource;
        this.rebelTarget = rebelTarget;
    }

    public RebelInventoryInputDto getRebelSource() {
        return rebelSource;
    }

    public void setRebelSource(final RebelInventoryInputDto rebelSource) {
        this.rebelSource = rebelSource;
    }

    public RebelInventoryInputDto getRebelTarget() {
        return rebelTarget;
    }

    public void setRebelTarget(final RebelInventoryInputDto rebelTarget) {
        this.rebelTarget = rebelTarget;
    }

}
