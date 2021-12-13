package com.starwars.resistance.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("complaints")
public class TraitorReport {

    @Id
    private CompositeKey id;
    private String rebelReporterId;
    private String rebelReportedId;

    public TraitorReport() { }

    public TraitorReport(final CompositeKey id, final String rebelReporterId, final String rebelReportedId) {
        this.id = id;
        this.rebelReporterId = rebelReporterId;
        this.rebelReportedId = rebelReportedId;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(final CompositeKey id) {
        this.id = id;
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

    public static class CompositeKey {

        private String rebelReporterId;
        private String rebelReportedId;

        public CompositeKey() { }

        public CompositeKey(final String rebelReporterId, final String rebelReportedId) {
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

}
