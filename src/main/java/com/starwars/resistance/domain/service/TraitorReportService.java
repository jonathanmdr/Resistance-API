package com.starwars.resistance.domain.service;

import com.starwars.resistance.domain.event.NewTraitorIdentifiedEvent;
import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.model.TraitorReport;
import com.starwars.resistance.domain.repository.TraitorReportRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class TraitorReportService {

    private static final int MAX_TRAITOR_REPORTS_CAN_BE_IGNORED = 3;

    private final RebelService rebelService;
    private final TraitorReportRepository traitorReportRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TraitorReportService(
        final RebelService rebelService,
        final TraitorReportRepository traitorReportRepository,
        final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.rebelService = rebelService;
        this.traitorReportRepository = traitorReportRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void save(final TraitorReport traitorReport) {
        validateRebelExists(traitorReport);
        validateReporterAndReportedAreSameRebel(traitorReport);

        traitorReportRepository.save(traitorReport);

        reportTraitorEventIfNecessary(traitorReport);
    }

    private void validateRebelExists(final TraitorReport traitorReport) {
        rebelService.findById(traitorReport.getRebelReporterId());
        rebelService.findById(traitorReport.getRebelReportedId());
    }

    private void validateReporterAndReportedAreSameRebel(final TraitorReport traitorReport) {
        if (Objects.equals(traitorReport.getRebelReporterId(), traitorReport.getRebelReportedId()))
            throw new BusinessException("Reporter rebel ID can not be equal to reported rebel ID");
    }

    private void reportTraitorEventIfNecessary(final TraitorReport traitorReport) {
        int rebelScore = getTraitorScoreByRebelId(traitorReport.getRebelReportedId());

        if (rebelScore >= MAX_TRAITOR_REPORTS_CAN_BE_IGNORED)
            applicationEventPublisher.publishEvent(new NewTraitorIdentifiedEvent(this, traitorReport.getRebelReportedId()));
    }

    private Integer getTraitorScoreByRebelId(final String id) {
        return traitorReportRepository.countByRebelReportedId(id);
    }

}
