package com.starwars.resistance.domain.service;

import com.starwars.resistance.api.v1.dto.input.ItemExchangeInputDto;
import com.starwars.resistance.api.v1.dto.input.RebelInventoryInputDto;
import com.starwars.resistance.core.common.model.Item;
import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.exception.EntityNotFoundException;
import com.starwars.resistance.domain.model.Rebel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemExchangeServiceTest {

    private RebelService rebelService;
    private ItemExchangeService subject;

    @BeforeEach
    void setup() {
        rebelService = mock(RebelService.class);

        subject = new ItemExchangeService(rebelService);
    }

    @Test
    void givenATwoRebelToExchangeItems_givenCallExchange_thenUpdateInventoryRebels() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), rebelSource.getInventory());
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), rebelTarget.getInventory());
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        subject.exchange(itemExchangeInputDto);

        verify(rebelService, times(1)).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithMostItemsInYoursInventories_givenCallExchange_thenUpdateInventoryRebels() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), Map.of(Item.WEAPON, 1));
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), Map.of(Item.FOOD, 4));
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        subject.exchange(itemExchangeInputDto);

        verify(rebelService, times(1)).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithExchangeANewItemThatTargetDoNotHave_givenCallExchange_thenUpdateInventoryRebels() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        inventorySource.put(Item.FOOD, 10);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        inventoryTarget.put(Item.AMMUNITION, 4);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), Map.of(Item.FOOD, 3));
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), Map.of(Item.AMMUNITION, 1));
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        subject.exchange(itemExchangeInputDto);

        verify(rebelService, times(1)).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithInexistentRebelSource_givenCallExchange_thenThrowsEntityNotFoundException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenThrow(new EntityNotFoundException(String.format("Rebel with id '%s' not found", rebelSource.getId())));

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), rebelSource.getInventory());
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), rebelTarget.getInventory());
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            EntityNotFoundException.class,
            () -> subject.exchange(itemExchangeInputDto),
            String.format("Rebel with id '%s' not found", rebelSource.getId())
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithInexistentRebelTarget_givenCallExchange_thenThrowsEntityNotFoundException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenThrow(new EntityNotFoundException(String.format("Rebel with id '%s' not found", rebelTarget.getId())));

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), rebelSource.getInventory());
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), rebelTarget.getInventory());
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            EntityNotFoundException.class,
            () -> subject.exchange(itemExchangeInputDto),
            String.format("Rebel with id '%s' not found", rebelTarget.getId())
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithInexistentItemInInventoryRebelSource_givenCallExchange_thenThrowsBusinessException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), Map.of(Item.WATER, 1));
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), rebelTarget.getInventory());
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            BusinessException.class,
            () -> subject.exchange(itemExchangeInputDto),
            "One or more items declared for exchange does not exist in the rebel source inventory"
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithInexistentItemInInventoryRebelTarget_givenCallExchange_thenThrowsBusinessException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), rebelSource.getInventory());
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), Map.of(Item.WATER, 1));
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            BusinessException.class,
            () -> subject.exchange(itemExchangeInputDto),
            "One or more items declared for exchange does not exist in the rebel target inventory"
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithInvalidItemAmountInInventoryRebelSource_givenCallExchange_thenThrowsBusinessException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), Map.of(Item.WEAPON, 3));
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), rebelTarget.getInventory());
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            BusinessException.class,
            () -> subject.exchange(itemExchangeInputDto),
            "One or more items declared for exchange does not have the amount informed in the rebel source inventory"
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithInvalidItemAmountInInventoryRebelTarget_givenCallExchange_thenThrowsBusinessException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 8);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), rebelSource.getInventory());
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), Map.of(Item.FOOD, 9));
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            BusinessException.class,
            () -> subject.exchange(itemExchangeInputDto),
            "One or more items declared for exchange does not have the amount informed in the rebel target inventory"
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    @Test
    void givenATwoRebelToExchangeItemsWithScoreIncompatibleToExchange_givenCallExchange_thenThrowsBusinessException() {
        Rebel rebelSource = createRebel();
        Map<Item, Integer> inventorySource = new ConcurrentHashMap<>();
        inventorySource.put(Item.WEAPON, 2);
        rebelSource.setInventory(inventorySource);

        Rebel rebelTarget = createRebel();
        Map<Item, Integer> inventoryTarget = new ConcurrentHashMap<>();
        inventoryTarget.put(Item.FOOD, 1);
        rebelTarget.setInventory(inventoryTarget);

        when(rebelService.findById(anyString()))
            .thenReturn(rebelSource)
            .thenReturn(rebelTarget);

        RebelInventoryInputDto rebelInventorySource = createRebelInventory(rebelSource.getId(), rebelSource.getInventory());
        RebelInventoryInputDto rebelInventoryTarget = createRebelInventory(rebelTarget.getId(), rebelTarget.getInventory());
        ItemExchangeInputDto itemExchangeInputDto = createItemExchangeInput(rebelInventorySource, rebelInventoryTarget);

        assertThrows(
            BusinessException.class,
            () -> subject.exchange(itemExchangeInputDto),
            "Items can not be exchanged because the score is not equal between source and target"
        );

        verify(rebelService, never()).saveAll(anyList());
    }

    private Rebel createRebel() {
        Rebel rebel = new Rebel();
        rebel.setId(UUID.randomUUID().toString());

        return rebel;
    }

    private RebelInventoryInputDto createRebelInventory(final String rebelId, final Map<Item, Integer> inventory) {
        RebelInventoryInputDto rebelInventory = new RebelInventoryInputDto();
        rebelInventory.setRebelId(rebelId);
        rebelInventory.setInventory(inventory);

        return rebelInventory;
    }

    private ItemExchangeInputDto createItemExchangeInput(final RebelInventoryInputDto rebelInventorySource, final RebelInventoryInputDto rebelInventoryTarget) {
        ItemExchangeInputDto itemExchangeInputDto = new ItemExchangeInputDto();
        itemExchangeInputDto.setRebelSource(rebelInventorySource);
        itemExchangeInputDto.setRebelTarget(rebelInventoryTarget);

        return itemExchangeInputDto;
    }

}