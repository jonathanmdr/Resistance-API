package com.starwars.resistance.domain.service;

import com.starwars.resistance.core.common.model.Item;
import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.exception.EntityNotFoundException;
import com.starwars.resistance.domain.model.Rebel;
import com.starwars.resistance.domain.repository.RebelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RebelServiceTest {

    private RebelRepository rebelRepository;
    private RebelService subject;

    @BeforeEach
    void setup() {
        rebelRepository = mock(RebelRepository.class);

        subject = new RebelService(rebelRepository);
    }

    @Test
    void givenTwoRebelsRegistered_whenCallFindAll_thenReturnListWithTwoRebels() {
        List<Rebel> expected = List.of(
            new Rebel(),
            new Rebel()
        );

        when(rebelRepository.findAll()).thenReturn(expected);

        List<Rebel> actual = subject.findAll();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(actual).hasSize(2);
        verify(rebelRepository, times(1)).findAll();
    }

    @Test
    void givenEmptyRebelsCollections_whenCallFindAll_thenReturnEmptyList() {
        when(rebelRepository.findAll()).thenReturn(Collections.emptyList());

        List<Rebel> actual = subject.findAll();

        assertThat(actual).isEmpty();
        verify(rebelRepository, times(1)).findAll();
    }

    @Test
    void givenTwoRebelsRegisteredWithOneTraitor_whenCallFindAllWithoutTraitors_thenReturnListWithOneRebel() {
        List<Rebel> expected = List.of(
            new Rebel()
        );

        when(rebelRepository.findAllByTraitor(Boolean.FALSE)).thenReturn(expected);

        List<Rebel> actual = subject.findAllWithoutTraitors();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(actual).hasSize(1);
        verify(rebelRepository, times(1)).findAllByTraitor(Boolean.FALSE);
    }

    @Test
    void givenEmptyRebelsCollections_whenCallFindAllWithoutTraitors_thenReturnEmptyList() {
        when(rebelRepository.findAllByTraitor(Boolean.FALSE)).thenReturn(Collections.emptyList());

        List<Rebel> actual = subject.findAllWithoutTraitors();

        assertThat(actual).isEmpty();
        verify(rebelRepository, times(1)).findAllByTraitor(Boolean.FALSE);
    }

    @Test
    void givenAExistentRebel_whenCallFindById_thenReturnSameRebel() {
        String id = UUID.randomUUID().toString();
        Rebel expected = new Rebel();
        expected.setId(id);

        when(rebelRepository.findByIdAndTraitor(id, Boolean.FALSE)).thenReturn(Optional.of(expected));

        Rebel actual = subject.findById(id);

        assertThat(actual).isEqualTo(expected);
        verify(rebelRepository, times(1)).findByIdAndTraitor(id, Boolean.FALSE);
    }

    @Test
    void givenAInexistentRebel_whenCallFindById_thenThrowsEntityNotFoundException() {
        String id = UUID.randomUUID().toString();

        when(rebelRepository.findByIdAndTraitor(id, Boolean.FALSE)).thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> subject.findById(id),
            String.format("Rebel with id '%s' not found", id)
        );

        verify(rebelRepository, times(1)).findByIdAndTraitor(id, Boolean.FALSE);
    }

    @Test
    void givenACommonRebel_whenCallSave_thenReturnSameRebelPersisted() {
        String id = UUID.randomUUID().toString();

        Rebel expected = new Rebel();
        expected.setId(id);

        when(rebelRepository.save(any(Rebel.class))).thenReturn(expected);

        Rebel rebel = new Rebel();
        rebel.setId(id);
        rebel.setInventory(Collections.emptyMap());

        Rebel actual = subject.save(rebel);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(rebelRepository, times(1)).save(any(Rebel.class));
    }

    @Test
    void givenARebelWithNegativeAmountItems_whenCallSave_thenThrowsBusinessException() {
        Rebel rebel = new Rebel();
        rebel.setInventory(Map.of(Item.FOOD, -1));

        assertThrows(
            BusinessException.class,
            () -> subject.save(rebel),
            "Amount informed to FOOD must be higher or equal to 1"
        );

        verify(rebelRepository, never()).save(any(Rebel.class));
    }

    @Test
    void givenTwoCommonRebels_whenCallSaveAll_thenReturnListWithSameRebelsPersisted() {
        String idOne = UUID.randomUUID().toString();
        Rebel expectedOne = new Rebel();
        expectedOne.setId(idOne);

        String idTwo = UUID.randomUUID().toString();
        Rebel expectedTwo = new Rebel();
        expectedTwo.setId(idTwo);

        List<Rebel> expected = Arrays.asList(expectedOne, expectedTwo);

        when(rebelRepository.saveAll(anyList())).thenReturn(expected);

        List<Rebel> actual = subject.saveAll(Collections.emptyList());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(actual).hasSize(2);
        verify(rebelRepository, times(1)).saveAll(anyList());
    }

    @Test
    void givenTwoRebelsWithNegativeAmountItems_whenCallSaveAll_thenThrowsBusinessException() {
        Rebel expectedOne = new Rebel();
        expectedOne.setInventory(Map.of(Item.FOOD, 1));

        Rebel expectedTwo = new Rebel();
        expectedTwo.setInventory(Map.of(Item.FOOD, 0));

        assertThrows(
            BusinessException.class,
            () -> subject.saveAll(Arrays.asList(expectedOne, expectedTwo)),
            "Amount informed to FOOD must be higher or equal to 1"
        );

        verify(rebelRepository, never()).saveAll(anyList());
    }

}