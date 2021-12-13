package com.starwars.resistance.domain.event;

import org.springframework.context.ApplicationEvent;

public class NewTraitorIdentifiedEvent extends ApplicationEvent {

    private final String traitorId;

    public NewTraitorIdentifiedEvent(final Object source, final String traitorId) {
        super(source);
        this.traitorId = traitorId;
    }

    public String getTraitorId() {
        return this.traitorId;
    }

}
