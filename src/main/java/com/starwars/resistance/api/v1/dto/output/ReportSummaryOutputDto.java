package com.starwars.resistance.api.v1.dto.output;

import com.starwars.resistance.core.common.model.Item;

import java.util.Map;

public class ReportSummaryOutputDto {

    private Double traitorsPercent;
    private Double rebelsPercent;
    private Map<Item, Double> averageOfResourceByRebel;
    private Integer lostScore;

    public ReportSummaryOutputDto() { }

    public ReportSummaryOutputDto(
        final Double traitorsPercent,
        final Double rebelsPercent,
        final Map<Item, Double> averageOfResourceByRebel,
        final Integer lostScore
    ) {
        this.traitorsPercent = traitorsPercent;
        this.rebelsPercent = rebelsPercent;
        this.averageOfResourceByRebel = averageOfResourceByRebel;
        this.lostScore = lostScore;
    }

    public Double getTraitorsPercent() {
        return traitorsPercent;
    }

    public void setTraitorsPercent(final Double traitorsPercent) {
        this.traitorsPercent = traitorsPercent;
    }

    public Double getRebelsPercent() {
        return rebelsPercent;
    }

    public void setRebelsPercent(final Double rebelsPercent) {
        this.rebelsPercent = rebelsPercent;
    }

    public Map<Item, Double> getAverageOfResourceByRebel() {
        return averageOfResourceByRebel;
    }

    public void setAverageOfResourceByRebel(final Map<Item, Double> averageOfResourceByRebel) {
        this.averageOfResourceByRebel = averageOfResourceByRebel;
    }

    public Integer getLostScore() {
        return lostScore;
    }

    public void setLostScore(final Integer lostScore) {
        this.lostScore = lostScore;
    }

}
