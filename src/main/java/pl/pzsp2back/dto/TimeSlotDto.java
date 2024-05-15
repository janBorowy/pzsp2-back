package pl.pzsp2back.dto;

import java.time.LocalDateTime;

public record TimeSlotDto(Long id, LocalDateTime startTime, Integer baseSlotQuantity, Integer lastMarketPrice, String userLogin, Long scheduleId) {
}
