package pl.pzsp2back.dto;

import pl.pzsp2back.orm.User;

import java.time.LocalDateTime;
import java.util.List;

public record TimeSlotDto(Long id, LocalDateTime startTime, Integer baseSlotQuantity, Integer lastMarketPrice, Integer numberOfUsers,  Boolean isUserSlot, List<UserShortDto> users, Long scheduleId) {
}
