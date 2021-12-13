package com.starwars.resistance.api.v1.dto.input;

import com.starwars.resistance.core.common.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

public class RebelInventoryInputDto {

    @NotBlank
    private String rebelId;

    @NotEmpty
    private Map<Item, Integer> inventory;

    public RebelInventoryInputDto() { }

    public RebelInventoryInputDto(final String rebelId, final Map<Item, Integer> inventory) {
        this.rebelId = rebelId;
        this.inventory = inventory;
    }

    public String getRebelId() {
        return rebelId;
    }

    public void setRebelId(final String rebelId) {
        this.rebelId = rebelId;
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(final Map<Item, Integer> inventory) {
        this.inventory = inventory;
    }

}
