package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.GroupedTimeSlotDto;
import pl.pzsp2back.dto.ShortTimeSlotInfoDto;
import pl.pzsp2back.orm.TimeSlot;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TimeSlotService {
    public static List<GroupedTimeSlotDto> mapToGroupedTimeSlotsDto(List<TimeSlot> timeslots)
    {
        timeslots.sort(Comparator.comparing(TimeSlot::getStartTime));
        List<GroupedTimeSlotDto> grupedTimeSlotDtos =  new ArrayList<>();
        LocalDateTime lastStartTime = timeslots.get(0).getStartTime();
        Integer slotsQuantity = timeslots.get(0).getBaseSlotQuantity();
        Integer marketPrice = timeslots.get(0).getLastMarketPrice();
        long idCounter = 1;
        List<ShortTimeSlotInfoDto> shortTimeSlotInfoDtos = new ArrayList<>();
        for(var ts : timeslots)
        {
            if(!ts.getStartTime().isEqual(lastStartTime) || !ts.getBaseSlotQuantity().equals(slotsQuantity))
            {
                grupedTimeSlotDtos.add(new GroupedTimeSlotDto(idCounter, lastStartTime, slotsQuantity, marketPrice, shortTimeSlotInfoDtos.size(), false, shortTimeSlotInfoDtos));
                lastStartTime = ts.getStartTime();
                slotsQuantity = ts.getBaseSlotQuantity();
                marketPrice = ts.getLastMarketPrice();
                idCounter++;
                shortTimeSlotInfoDtos = new ArrayList<>();
            }
            shortTimeSlotInfoDtos.add(new ShortTimeSlotInfoDto(ts.getId(), ts.getUser().getUsername(), ts.getUser().getName(), ts.getUser().getSurname()));
        }
        grupedTimeSlotDtos.add(new GroupedTimeSlotDto(idCounter, lastStartTime, slotsQuantity, marketPrice, shortTimeSlotInfoDtos.size(), false, shortTimeSlotInfoDtos));
        return grupedTimeSlotDtos;
    }
}
