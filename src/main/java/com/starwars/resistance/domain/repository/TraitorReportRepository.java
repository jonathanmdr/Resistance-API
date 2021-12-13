package com.starwars.resistance.domain.repository;

import com.starwars.resistance.domain.model.TraitorReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraitorReportRepository extends MongoRepository<TraitorReport, String> {

    Integer countByRebelReportedId(final String id);

}
