package com.starwars.resistance.domain.repository;

import com.starwars.resistance.domain.model.Rebel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RebelRepository extends MongoRepository<Rebel, String> {

    Optional<Rebel> findByIdAndTraitor(final String id, final boolean traitor);
    List<Rebel> findAllByTraitor(final boolean traitor);

}
