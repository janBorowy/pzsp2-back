package pl.pzsp2back.dto;

import java.time.LocalDateTime;

public record OptimizationProcessDto(Long id, LocalDateTime timestamp, LocalDateTime offerAcceptanceDeadline, LocalDateTime optimizationTime, Long scheduleId, UserShortDto ownerUser) {
}
