package com.starwars.resistance.api.v1.dto.output;

import com.starwars.resistance.core.common.model.Gender;

public class RebelSummaryOutputDto {

    private String id;
    private String name;
    private Integer age;
    private Gender gender;

    public RebelSummaryOutputDto() { }

    public RebelSummaryOutputDto(final String id, final String name, final Integer age, final Gender gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
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

}
