package com.starwars.resistance.api.v1.dto.input;

import com.starwars.resistance.core.common.model.Gender;
import com.starwars.resistance.core.common.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class RebelInputDto {

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer age;

    @NotNull
    private Gender gender;

    @Valid
    @NotNull
    private LocalizationInputDto localization;

    @NotNull
    @NotEmpty
    private Map<Item, Integer> inventory;

    public RebelInputDto() { }

    public RebelInputDto(
        final String name,
        final Integer age,
        final Gender gender,
        final LocalizationInputDto localization,
        final Map<Item, Integer> inventory
    ) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.localization = localization;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public LocalizationInputDto getLocalization() {
        return localization;
    }

    public void setLocalization(final LocalizationInputDto localization) {
        this.localization = localization;
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(final Map<Item, Integer> inventory) {
        this.inventory = inventory;
    }

}
