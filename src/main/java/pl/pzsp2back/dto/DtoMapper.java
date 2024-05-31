package pl.pzsp2back.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.pzsp2back.orm.*;

import java.util.stream.Collectors;

@Component
public class DtoMapper {
    public OptimizationProcessDto toDto(@NotNull OptimizationProcess optimizationProcess) {
        return new OptimizationProcessDto(optimizationProcess.getId(), optimizationProcess.getTimestamp(), optimizationProcess.getOfferAcceptanceDeadline(), optimizationProcess.getOptimizationTime(), optimizationProcess.getSchedule().getId(), toShortDto(optimizationProcess.getProcessOwner()));
    }

    public ScheduleDto toDto(@NotNull Schedule schedule) {
        return new ScheduleDto(schedule.getId(), schedule.getBaseSlotLength(), schedule.getName(), schedule.getTag(), schedule.getGroup().getName(), schedule.getTimeSlotList().stream().map(ts -> toDto(ts)).collect(Collectors.toList()));
    }

    public TimeSlotDto toDto(@NotNull TimeSlot timeSlot) {
        return new TimeSlotDto(timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getBaseSlotQuantity(), timeSlot.getLastMarketPrice(), timeSlot.getUsers().size(), null, timeSlot.getUsers().stream().map(u -> toShortDto(u)).collect(Collectors.toList()), timeSlot.getSchedule().getId());
    }

    public TradeOfferDto toDto(@NotNull TradeOffer tradeOffer) {
        return new TradeOfferDto(tradeOffer.getId(), tradeOffer.getPrice(), tradeOffer.getTimestamp(), toShortDto(tradeOffer.getOfferOwner()), toDto(tradeOffer.getTimeslot()), toDto(tradeOffer.getOptimizationProcess()), tradeOffer.getIfWantOffer(), tradeOffer.getIsActive());
    }

    public UserDto toDto(@NotNull User user) {
        return new UserDto(user.getLogin(), user.getPassword(), user.getIfAdmin(), user.getBalance(), user.getEmail(), user.getName(), user.getSurname(), user.getGroup().getId());
    }

    public UserShortDto toShortDto(@NotNull User user) {
        return new UserShortDto(user.getLogin(), user.getName(), user.getSurname());
    }

}
