package com.starwars.resistance.domain.service;

import com.starwars.resistance.api.v1.dto.output.ReportSummaryOutputDto;
import com.starwars.resistance.core.common.model.Item;
import com.starwars.resistance.domain.model.Rebel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final RebelService rebelService;
    private final ItemExchangeService itemExchangeService;

    public ReportService(final RebelService rebelService, final ItemExchangeService itemExchangeService) {
        this.rebelService = rebelService;
        this.itemExchangeService = itemExchangeService;
    }

    public ReportSummaryOutputDto generateAndGet() {
        List<Rebel> rebels = rebelService.findAll();

        Double traitorsPercent = getTraitorsPercent(rebels);
        Double rebelsPercent = getRebelsPercent(rebels);

        Map<Item, Double> itemsAverageByRebel = Map.of(
            Item.WEAPON, getAverageOfResourceByRebel(rebels, Item.WEAPON),
            Item.AMMUNITION, getAverageOfResourceByRebel(rebels, Item.AMMUNITION),
            Item.WATER, getAverageOfResourceByRebel(rebels, Item.WATER),
            Item.FOOD, getAverageOfResourceByRebel(rebels, Item.FOOD)
        );

        Integer lostScore =  getScoreAffectedByTraitors(rebels);

        return new ReportSummaryOutputDto(
            traitorsPercent,
            rebelsPercent,
            itemsAverageByRebel,
            lostScore
        );
    }

    private double getTraitorsPercent(final List<Rebel> rebels) {
        return getPercent(rebels, Filter.TRAITORS_ONLY);
    }

    private double getRebelsPercent(final List<Rebel> rebels) {
        return getPercent(rebels, Filter.REBELS_ONLY);
    }

    private double getPercent(final List<Rebel> rebels, final Filter filter) {
        return rebels.stream()
            .filter(filter::apply)
            .count() / (double) rebels.size() * 100;
    }

    private Double getAverageOfResourceByRebel(final List<Rebel> rebels, final Item item) {
        List<Rebel> rebelsOnly = rebels.stream()
            .filter(Filter.REBELS_ONLY::apply)
            .collect(Collectors.toList());

        int itemAmount = rebelsOnly.stream()
            .filter(rebel -> rebel.getInventory().containsKey(item))
            .mapToInt(rebel -> rebel.getInventory().get(item))
            .sum();

        return itemAmount / (double) rebelsOnly.size();
    }

    private Integer getScoreAffectedByTraitors(final List<Rebel> rebels) {
        List<Rebel> traitorsOnly = rebels.stream()
            .filter(Filter.TRAITORS_ONLY::apply)
            .collect(Collectors.toList());

        return traitorsOnly.stream()
            .mapToInt(traitor -> itemExchangeService.getItemsScore(traitor.getInventory()))
            .sum();
    }

    enum Filter {

        TRAITORS_ONLY(Rebel::isTraitor),
        REBELS_ONLY(Rebel::isNotTraitor);

        private final Predicate<Rebel> predicate;

        Filter(final Predicate<Rebel> predicate) {
            this.predicate = predicate;
        }

        public boolean apply(final Rebel rebel) {
            return predicate.test(rebel);
        }

    }

}
