package com.starwars.resistance.domain.service;

import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.exception.EntityNotFoundException;
import com.starwars.resistance.domain.model.Rebel;
import com.starwars.resistance.domain.repository.RebelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RebelService {

    private final RebelRepository rebelRepository;

    public RebelService(final RebelRepository rebelRepository) {
        this.rebelRepository = rebelRepository;
    }

    @Transactional(readOnly = true)
    public List<Rebel> findAll() {
        return rebelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Rebel> findAllWithoutTraitors() {
        return rebelRepository.findAllByTraitor(Boolean.FALSE);
    }

    @Transactional(readOnly = true)
    public Rebel findById(final String id) {
        return rebelRepository.findByIdAndTraitor(id, Boolean.FALSE)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Rebel with id '%s' not found", id)));
    }

    @Transactional
    public Rebel save(Rebel rebel) {
        validateItemsAmount(rebel);
        return rebelRepository.save(rebel);
    }

    @Transactional
    public List<Rebel> saveAll(List<Rebel> rebels) {
        rebels.forEach(this::validateItemsAmount);
        return rebelRepository.saveAll(rebels);
    }

    private void validateItemsAmount(final Rebel rebel) {
        if (Objects.nonNull(rebel.getInventory())) {
            rebel.getInventory().forEach((item, amount) -> {
                if (amount <= 0)
                    throw new BusinessException(String.format("Amount informed to %s must be higher or equal to 1", item));
            });
        }
    }

}
