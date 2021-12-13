package com.starwars.resistance.api.v1.dto.output;

import com.starwars.resistance.core.common.model.Gender;
import com.starwars.resistance.core.common.model.Item;

import java.util.Map;

public class RebelOutputDto {

    private String id;
    private String name;
    private Integer age;
    private Gender gender;
    private LocalizationOutputDto localization;
    private Map<Item, Integer> inventory;

    public RebelOutputDto() { }

    public RebelOutputDto(
        final String id,
        final String name,
        final Integer age,
        final Gender gender,
        final LocalizationOutputDto localization,
        final Map<Item, Integer> inventory
                         ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.localization = localization;
        this.inventory = inventory;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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

    public LocalizationOutputDto getLocalization() {
        return localization;
    }

    public void setLocalization(final LocalizationOutputDto localization) {
        this.localization = localization;
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(final Map<Item, Integer> inventory) {
        this.inventory = inventory;
    }

}
