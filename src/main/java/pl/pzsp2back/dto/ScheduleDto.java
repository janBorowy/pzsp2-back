package pl.pzsp2back.dto;

import java.util.List;

public record ScheduleDto(Long id, Integer slotLength, String scheduleName, String scheduleTag, String groupName, List<TimeSlotDto> timeSlots) {
}
