package com.starwars.resistance.domain.service;

import com.starwars.resistance.api.v1.dto.input.ItemExchangeInputDto;
import com.starwars.resistance.core.common.model.Item;
import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.model.Rebel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ItemExchangeService {

    private final RebelService rebelService;

    public ItemExchangeService(final RebelService rebelService) {
        this.rebelService = rebelService;
    }

    @Transactional
    public void exchange(ItemExchangeInputDto itemExchangeInputDto) {
        Rebel rebelSource = getRebelById(itemExchangeInputDto.getRebelSource().getRebelId());
        Rebel rebelTarget = getRebelById(itemExchangeInputDto.getRebelTarget().getRebelId());

        validateRulesForExchange(rebelSource, rebelTarget, itemExchangeInputDto);

        Integer rebelSourceItemsScore = getItemsScore(itemExchangeInputDto.getRebelSource().getInventory());
        Integer rebelTargetItemsScore = getItemsScore(itemExchangeInputDto.getRebelTarget().getInventory());

        if (isNotValidScoreToExchange(rebelSourceItemsScore, rebelTargetItemsScore))
            throw new BusinessException("Items can not be exchanged because the score is not equal between source and target");

        Map<Item, Integer> rebelSourceInventory = rebelSource.getInventory();
        Map<Item, Integer> rebelTargetInventory = rebelTarget.getInventory();

        applyExchange(
            itemExchangeInputDto.getRebelSource().getInventory(),
            rebelSourceInventory,
            rebelTargetInventory
        );

        applyExchange(
            itemExchangeInputDto.getRebelTarget().getInventory(),
            rebelTargetInventory,
            rebelSourceInventory
        );

        rebelSource.setInventory(rebelSourceInventory);
        rebelTarget.setInventory(rebelTargetInventory);

        rebelService.saveAll(Arrays.asList(rebelSource, rebelTarget));
    }

    public Integer getItemsScore(final Map<Item, Integer> inventory) {
        final AtomicInteger sum = new AtomicInteger();

        inventory.forEach(
            (item, amount) -> sum.addAndGet(item.getScore() * amount)
        );

        return sum.get();
    }

    private Rebel getRebelById(final String id) {
        return rebelService.findById(id);
    }

    private void validateRulesForExchange(final Rebel rebelSource, final Rebel rebelTarget, final ItemExchangeInputDto itemExchangeInputDto) {
        final String source = "source";
        final String target = "target";

        validateItemExistsOnInventory(source, rebelSource.getInventory(), itemExchangeInputDto.getRebelSource().getInventory());
        validateItemExistsOnInventory(target, rebelTarget.getInventory(), itemExchangeInputDto.getRebelTarget().getInventory());

        validateItemAmountIsValid(source, rebelSource.getInventory(), itemExchangeInputDto.getRebelSource().getInventory());
        validateItemAmountIsValid(target, rebelTarget.getInventory(), itemExchangeInputDto.getRebelTarget().getInventory());
    }

    private void validateItemExistsOnInventory(final String origin, final Map<Item, Integer> rebelInventory, final Map<Item, Integer> rebelInventoryToExchange) {
        rebelInventoryToExchange.forEach((item, amount) -> {
            if (!rebelInventory.containsKey(item))
                throw new BusinessException(String.format("One or more items declared for exchange does not exist in the rebel %s inventory", origin));
        });
    }

    private void validateItemAmountIsValid(final String origin, final Map<Item, Integer> rebelInventory, final Map<Item, Integer> rebelInventoryToExchange) {
        rebelInventoryToExchange.forEach((item, amount) -> {
            if (rebelInventory.get(item) < amount)
                throw new BusinessException(String.format("One or more items declared for exchange does not have the amount informed in the rebel %s inventory", origin));
        });
    }

    private boolean isNotValidScoreToExchange(Integer sourceScore, Integer targetScore) {
        return !isValidScoreToExchange(sourceScore, targetScore);
    }

    private boolean isValidScoreToExchange(Integer sourceScore, Integer targetScore) {
        return Objects.equals(sourceScore, targetScore);
    }

    private void applyExchange(final Map<Item, Integer> itemsToExchange, final Map<Item, Integer> sourceInventory, final Map<Item, Integer> targetInventory) {
        itemsToExchange.forEach((item, amount) -> {
            adjustSourceInventory(sourceInventory, item, amount);
            adjustTargetInventory(targetInventory, item, amount);
        });
    }

    private void adjustSourceInventory(final Map<Item, Integer> sourceInventory, final Item item, final Integer amount) {
        if (sourceInventory.get(item) - amount == 0) {
            sourceInventory.remove(item);
            return;
        }

        int newAmount = sourceInventory.get(item) - amount;
        sourceInventory.put(item, newAmount);
    }

    private void adjustTargetInventory(final Map<Item, Integer> targetInventory, final Item item, final Integer amount) {
        targetInventory.computeIfPresent(
            item,
            (key, value) -> targetInventory.get(key) + amount
        );

        targetInventory.put(item, amount);
    }

}
