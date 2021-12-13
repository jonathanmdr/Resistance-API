package com.starwars.resistance.domain.service;

import com.starwars.resistance.domain.event.NewTraitorIdentifiedEvent;
import com.starwars.resistance.domain.exception.BusinessException;
import com.starwars.resistance.domain.exception.EntityNotFoundException;
import com.starwars.resistance.domain.model.Rebel;
import com.starwars.resistance.domain.model.TraitorReport;
import com.starwars.resistance.domain.repository.TraitorReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraitorReportServiceTest {

    private RebelService rebelService;
    private TraitorReportRepository traitorReportRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    private TraitorReportService subject;

    @BeforeEach
    void setup() {
        rebelService = mock(RebelService.class);
        traitorReportRepository = mock(TraitorReportRepository.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);

        subject = new TraitorReportService(rebelService, traitorReportRepository, applicationEventPublisher);
    }

    @Test
    void givenAFirstReportOfOneRebel_whenCallSave_thenReturnSaveReport() {
        Rebel reporter = new Rebel();
        reporter.setId(UUID.randomUUID().toString());

        Rebel reported = new Rebel();
        reported.setId(UUID.randomUUID().toString());

        when(rebelService.findById(anyString()))
            .thenReturn(reporter)
            .thenReturn(reported);

        TraitorReport expected = new TraitorReport(
            new TraitorReport.CompositeKey(reporter.getId(), reported.getId()),
            reporter.getId(),
            reported.getId()
        );

        when(traitorReportRepository.save(any(TraitorReport.class))).thenReturn(expected);
        when(traitorReportRepository.countByRebelReportedId(reported.getId())).thenReturn(0);

        subject.save(expected);

        verify(traitorReportRepository, times(1)).save(any(TraitorReport.class));
        verify(applicationEventPublisher, never()).publishEvent(any(NewTraitorIdentifiedEvent.class));
    }

    @Test
    void givenAThirdReportOfOneRebel_whenCallSave_thenReturnSaveReportAndTriggerNewTraitorIdentifiedEvent() {
        Rebel reporter = new Rebel();
        reporter.setId(UUID.randomUUID().toString());

        Rebel reported = new Rebel();
        reported.setId(UUID.randomUUID().toString());

        when(rebelService.findById(anyString()))
            .thenReturn(reporter)
            .thenReturn(reported);

        TraitorReport expected = new TraitorReport(
            new TraitorReport.CompositeKey(reporter.getId(), reported.getId()),
            reporter.getId(),
            reported.getId()
        );

        when(traitorReportRepository.save(any(TraitorReport.class))).thenReturn(expected);
        when(traitorReportRepository.countByRebelReportedId(reported.getId())).thenReturn(3);

        subject.save(expected);

        verify(traitorReportRepository, times(1)).save(any(TraitorReport.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(NewTraitorIdentifiedEvent.class));
    }

    @Test
    void givenAReportWithInexistentReporterRebel_whenCallSave_thenThrowsEntityNotFoundException() {
        Rebel reporter = new Rebel();
        reporter.setId(UUID.randomUUID().toString());

        Rebel reported = new Rebel();
        reported.setId(UUID.randomUUID().toString());

        when(rebelService.findById(anyString()))
            .thenThrow(new EntityNotFoundException(String.format("Rebel with id '%s' not found", reporter.getId())));

        TraitorReport expected = new TraitorReport(
            new TraitorReport.CompositeKey(reporter.getId(), reported.getId()),
            reporter.getId(),
            reported.getId()
        );

        assertThrows(
            EntityNotFoundException.class,
            () -> subject.save(expected),
            String.format("Rebel with id '%s' not found", reporter.getId())
        );

        verify(traitorReportRepository, never()).save(any(TraitorReport.class));
        verify(applicationEventPublisher, never()).publishEvent(any(NewTraitorIdentifiedEvent.class));
    }

    @Test
    void givenAReportWithInexistentReportedRebel_whenCallSave_thenThrowsEntityNotFoundException() {
        Rebel reporter = new Rebel();
        reporter.setId(UUID.randomUUID().toString());

        Rebel reported = new Rebel();
        reported.setId(UUID.randomUUID().toString());

        when(rebelService.findById(anyString()))
            .thenReturn(reporter)
            .thenThrow(new EntityNotFoundException(String.format("Rebel with id '%s' not found", reported.getId())));

        TraitorReport expected = new TraitorReport(
            new TraitorReport.CompositeKey(reporter.getId(), reported.getId()),
            reporter.getId(),
            reported.getId()
        );

        assertThrows(
            EntityNotFoundException.class,
            () -> subject.save(expected),
            String.format("Rebel with id '%s' not found", reported.getId())
        );

        verify(traitorReportRepository, never()).save(any(TraitorReport.class));
        verify(applicationEventPublisher, never()).publishEvent(any(NewTraitorIdentifiedEvent.class));
    }

    @Test
    void givenAReportWithReporterAndReportedRebelHasSameIds_whenCallSave_thenThrowsBusinessException() {
        final String id = UUID.randomUUID().toString();

        Rebel reporter = new Rebel();
        reporter.setId(id);

        Rebel reported = new Rebel();
        reported.setId(id);

        when(rebelService.findById(anyString()))
            .thenReturn(reporter)
            .thenReturn(reported);

        TraitorReport expected = new TraitorReport(
            new TraitorReport.CompositeKey(reporter.getId(), reported.getId()),
            reporter.getId(),
            reported.getId()
        );

        assertThrows(
            BusinessException.class,
            () -> subject.save(expected),
            "Reporter rebel ID can not be equal to reported rebel ID"
        );

        verify(traitorReportRepository, never()).save(any(TraitorReport.class));
        verify(applicationEventPublisher, never()).publishEvent(any(NewTraitorIdentifiedEvent.class));
    }

}