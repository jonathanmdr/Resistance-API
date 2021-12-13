package com.starwars.resistance.domain.listener;

import com.starwars.resistance.domain.event.NewTraitorIdentifiedEvent;
import com.starwars.resistance.domain.model.Rebel;
import com.starwars.resistance.domain.service.RebelService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class NewTraitorIdentifiedListener implements ApplicationListener<NewTraitorIdentifiedEvent> {

    private final RebelService rebelService;

    public NewTraitorIdentifiedListener(final RebelService rebelService) {
        this.rebelService = rebelService;
    }

    @Override
    public void onApplicationEvent(final NewTraitorIdentifiedEvent event) {
        Executors.newSingleThreadExecutor()
            .submit(() -> {
                Rebel rebel = rebelService.findById(event.getTraitorId());
                rebel.setTraitor(Boolean.TRUE);
                rebelService.save(rebel);
            });
    }

}
