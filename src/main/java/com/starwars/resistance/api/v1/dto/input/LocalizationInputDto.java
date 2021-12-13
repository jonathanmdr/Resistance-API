package com.starwars.resistance.api.v1.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LocalizationInputDto {

    @NotNull
    private Integer latitude;

    @NotNull
    private Integer longitude;

    @NotBlank
    private String baseName;

    public LocalizationInputDto() { }

    public LocalizationInputDto(final Integer latitude, final Integer longitude, final String baseName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.baseName = baseName;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(final Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(final Integer longitude) {
        this.longitude = longitude;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(final String baseName) {
        this.baseName = baseName;
    }

}
