package pl.pzsp2back.dto;

import org.springframework.stereotype.Component;
import pl.pzsp2back.orm.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
    public OptimizationProcessDto toDto( OptimizationProcess optimizationProcess) {
        if (optimizationProcess != null) {
            return new OptimizationProcessDto(optimizationProcess.getId(), optimizationProcess.getTimestamp(), optimizationProcess.getOfferAcceptanceDeadline(), optimizationProcess.getOptimizationTime(), optimizationProcess.getSchedule().getId(), toShortDto(optimizationProcess.getProcessOwner()));
        } else {
            return null;
        }
    }

    public ScheduleDto toDto( Schedule schedule) {
        return new ScheduleDto(schedule.getId(), schedule.getBaseSlotLength(), schedule.getName(), schedule.getTag(), schedule.getGroup().getName(), schedule.getTimeSlotList().stream().map(ts -> toDto(ts)).collect(Collectors.toList()));
    }

    public ScheduleDto toDto( Schedule schedule, User user) {
        return new ScheduleDto(schedule.getId(), schedule.getBaseSlotLength(), schedule.getName(), schedule.getTag(), schedule.getGroup().getName(), schedule.getTimeSlotList().stream().map(ts -> toDto(ts, user)).collect(Collectors.toList()));
    }

    public TimeSlotDto toDto(TimeSlot timeSlot) {
        if (timeSlot != null) {
            return new TimeSlotDto(timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getBaseSlotQuantity(), timeSlot.getLastMarketPrice(), timeSlot.getUsers().size(), null, timeSlot.getUsers().stream().map(u -> toShortDto(u)).collect(Collectors.toList()), timeSlot.getSchedule().getId());
        } else {
            return null;
        }
    }

    public TimeSlotDto toDto(TimeSlot timeSlot, User user) {
        if (timeSlot != null) {
            return new TimeSlotDto(timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getBaseSlotQuantity(), timeSlot.getLastMarketPrice(), timeSlot.getUsers().size(), timeSlot.getUsers().stream().anyMatch(u -> u.getLogin().equals(user.getLogin())), new ArrayList<>(), timeSlot.getSchedule().getId());
        } else {
            return null;
        }
    }

    public TradeOfferDto toDto( TradeOffer tradeOffer) {
        if (tradeOffer != null) {
            return new TradeOfferDto(tradeOffer.getId(), tradeOffer.getPrice(), tradeOffer.getTimestamp(), toShortDto(tradeOffer.getOfferOwner()), toDto(tradeOffer.getTimeslot()), toDto(tradeOffer.getOptimizationProcess()), tradeOffer.getIfWantOffer(), tradeOffer.getStatus());
        } else {
            return null;
        }
    }

    public UserDto toDto( User user) {
        if (user != null) {
            return new UserDto(user.getLogin(), user.getPassword(), user.getIfAdmin(), user.getBalance(), user.getEmail(), user.getName(), user.getSurname(), user.getGroup().getId());
        } else {
            return null;
        }
    }


    public TradeDto toDto( Trade trade) {
        if (trade != null) {
            return new TradeDto(trade.getId(), trade.getFinalPrice(), trade.getTimestamp(), toDto(trade.getOptimizationProcess()), toDto(trade.getSellerOffer()), toDto(trade.getBuyerOffer()));
        } else {
            return null;
        }
    }

    public UserShortDto toShortDto( User user) {
        if (user != null) {
            return new UserShortDto(user.getLogin(), user.getName(), user.getSurname());
        } else {
            return null;
        }
    }

}
