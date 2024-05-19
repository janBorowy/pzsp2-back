package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.GroupedTimeSlotDto;
import pl.pzsp2back.dto.ShortTimeSlotInfoDto;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.exceptions.TimeSlotServiceException;
import pl.pzsp2back.orm.Schedule;
import pl.pzsp2back.orm.TimeSlot;
import pl.pzsp2back.orm.TimeslotRepository;
import pl.pzsp2back.orm.User;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TimeSlotService {

    private final TimeslotRepository timeslotRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;
    private boolean validateTimeslotDto(TimeSlotDto timeSlotDto) {
        if (timeSlotDto.startTime() == null || timeSlotDto.baseSlotQuantity() == null || timeSlotDto.userLogin() == null) {
            return false;
        }

        return !(timeSlotDto.baseSlotQuantity() >= 0);
    }


    public TimeSlotDto getTimeSlot(Long id) {
        TimeSlot timeslot = findTimeSlotById(id);
        return mapToTimeSlotDto(timeslot);
    }


    public TimeSlotDto createTimeSlot(TimeSlotDto timeslotDto) {
        if (!validateTimeslotDto(timeslotDto)) {
            throw new TimeSlotServiceException("Not enough data to create timeslot");
        }

        User user = userService.getUser(timeslotDto.userLogin());
        List<Schedule> schedules = user.getGroup().getSchedulesList();

        TimeSlot ts = timeslotRepository.save(new TimeSlot(null, timeslotDto.startTime(), timeslotDto.baseSlotQuantity(), -1, user, schedules.get(0), null));
        return TimeSlotService.mapToTimeSlotDto(ts);
    }

    public TimeSlotDto updateTimeSlot(TimeSlotDto timeslotDto) {
        if (timeslotDto.id() == null) {
            throw new TimeSlotServiceException("Id is null. It is obligatory to update timeslot.");
        }

        var timeslot = findTimeSlotById(timeslotDto.id());
        if(timeslotDto.startTime() != null){
            timeslot.setStartTime(timeslotDto.startTime());
        }
        if(timeslotDto.baseSlotQuantity() != null) {
            timeslot.setBaseSlotQuantity(timeslotDto.baseSlotQuantity());
        }
        if(timeslotDto.lastMarketPrice() != null) {
            timeslot.setLastMarketPrice(timeslotDto.lastMarketPrice());
        }
        if(timeslotDto.userLogin() != null && !timeslot.getUser().getLogin().equals(timeslotDto.userLogin())) {
            User user = userService.getUser(timeslotDto.userLogin());
            timeslot.setUser(user);
        }
        //not implemented changing schedule for timeslot
        timeslot = timeslotRepository.save(timeslot);
        return mapToTimeSlotDto(timeslot);
    }

    public TimeSlotDto deleteTimeSlot(Long id) {
        TimeSlot timeslot = findTimeSlotById(id);
        timeslotRepository.delete(timeslot);
        return mapToTimeSlotDto(timeslot);
    }

    private TimeSlot findTimeSlotById(Long id) {
        return timeslotRepository.findById(id).orElseThrow(
                () -> new TimeSlotServiceException("Timeslot not found with given id: " + id)
        );
    }


    public static TimeSlotDto mapToTimeSlotDto(TimeSlot timeslot) {
        return new TimeSlotDto(timeslot.getId(), timeslot.getStartTime(), timeslot.getBaseSlotQuantity(), timeslot.getLastMarketPrice(), timeslot.getUser().getLogin(), timeslot.getSchedule().getId());
    }

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
