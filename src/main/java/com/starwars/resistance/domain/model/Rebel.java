package com.starwars.resistance.domain.model;

import com.starwars.resistance.core.common.model.Gender;
import com.starwars.resistance.core.common.model.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "rebels")
public class Rebel {

    @Id
    private String id;
    private String name;
    private Integer age;
    private Gender gender;
    private Localization localization;
    private Map<Item, Integer> inventory;
    private boolean traitor;

    public Rebel() { }

    public Rebel(
        final String id,
        final String name,
        final Integer age,
        final Gender gender,
        final Localization localization,
        final Map<Item, Integer> inventory,
        final boolean traitor
    ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.localization = localization;
        this.inventory = inventory;
        this.traitor = traitor;
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

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(final Localization localization) {
        this.localization = localization;
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(final Map<Item, Integer> inventory) {
        this.inventory = inventory;
    }

    public boolean isTraitor() {
        return traitor;
    }

    public void setTraitor(final boolean traitor) {
        this.traitor = traitor;
    }

    public boolean isNotTraitor() {
        return !isTraitor();
    }

}
