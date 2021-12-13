package com.starwars.resistance.api.v1.dto.input;

import javax.validation.constraints.NotBlank;

public class TraitorReportInputDto {

    @NotBlank
    private String rebelReporterId;

    @NotBlank
    private String rebelReportedId;

    public TraitorReportInputDto() { }

    public TraitorReportInputDto(final String rebelReporterId, final String rebelReportedId) {
        this.rebelReporterId = rebelReporterId;
        this.rebelReportedId = rebelReportedId;
    }

    public String getRebelReporterId() {
        return rebelReporterId;
    }

    public void setRebelReporterId(final String rebelReporterId) {
        this.rebelReporterId = rebelReporterId;
    }

    public String getRebelReportedId() {
        return rebelReportedId;
    }

    public void setRebelReportedId(final String rebelReportedId) {
        this.rebelReportedId = rebelReportedId;
    }

}
