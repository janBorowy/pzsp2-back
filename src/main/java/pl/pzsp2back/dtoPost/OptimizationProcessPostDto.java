package pl.pzsp2back.dtoPost;

import java.time.LocalDateTime;

public record OptimizationProcessPostDto(LocalDateTime offerAcceptanceDeadline, LocalDateTime optimizationTime) {
}
