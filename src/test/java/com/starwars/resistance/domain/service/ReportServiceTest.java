package com.starwars.resistance.domain.service;

import com.starwars.resistance.api.v1.dto.output.ReportSummaryOutputDto;
import com.starwars.resistance.core.common.model.Item;
import com.starwars.resistance.domain.model.Rebel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    private RebelService rebelService;
    private ItemExchangeService itemExchangeService;
    private ReportService subject;

    @BeforeEach
    void setup() {
        rebelService = mock(RebelService.class);
        itemExchangeService = mock(ItemExchangeService.class);

        subject = new ReportService(rebelService, itemExchangeService);
    }

    @Test
    void givenAnyData_whenCallGenerateAndGet_thenGenerateAndReturnReport() {
        Rebel rebelOne = createRebel();
        Rebel rebelTwo = createRebel();
        Rebel rebelThree = createRebel();

        Rebel traitor = createRebel();
        traitor.setTraitor(Boolean.TRUE);

        List<Rebel> allRebels =  Arrays.asList(rebelOne, rebelTwo, rebelThree, traitor);

        when(rebelService.findAll()).thenReturn(allRebels);

        when(itemExchangeService.getItemsScore(anyMap())).thenReturn(18);

        ReportSummaryOutputDto expected = new ReportSummaryOutputDto(
            25.0,
            75.0,
            Map.of(
                Item.AMMUNITION, 0.0,
                Item.FOOD, 10.0,
                Item.WEAPON, 2.0,
                Item.WATER, 0.0
            ),
            18
        );

        ReportSummaryOutputDto actual = subject.generateAndGet();

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private Rebel createRebel() {
        Rebel rebel = new Rebel();
        rebel.setId(UUID.randomUUID().toString());
        rebel.setInventory(
            Map.of(
                Item.WEAPON, 2,
                Item.FOOD, 10
            )
        );

        return rebel;
    }

}
