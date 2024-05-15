package pl.pzsp2back.dto;

import java.util.List;

public record ScheduleDto(Long id, Integer slotLength, String name, String tag, String groupName, List<TimeSlotDto> timeSlots) {
}
