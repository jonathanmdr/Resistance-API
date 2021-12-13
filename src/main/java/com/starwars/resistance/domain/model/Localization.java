package com.starwars.resistance.domain.model;

public class Localization {

    private Integer latitude;
    private Integer longitude;
    private String baseName;

    public Localization() { }

    public Localization(final Integer latitude, final Integer longitude, final String baseName) {
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
