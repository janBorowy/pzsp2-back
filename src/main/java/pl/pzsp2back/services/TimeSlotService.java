package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.dto.UserShortDto;
import pl.pzsp2back.exceptions.TimeSlotServiceException;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeSlotService {

    private final TimeslotRepository timeslotRepository;
    private final UserService userService;

    private boolean validateTimeslotDto(TimeSlotDto timeSlotDto) {
        if (timeSlotDto.startTime() == null || timeSlotDto.baseSlotQuantity() == null || timeSlotDto.users() == null) {
            return false;
        }
        return true;
    }


    public TimeSlotDto getTimeSlot(Long id) {
        TimeSlot timeslot = findTimeSlotById(id);
        return mapToTimeSlotDto(timeslot);
    }


    public TimeSlotDto createTimeSlot(TimeSlotDto timeslotDto) {
        if (!validateTimeslotDto(timeslotDto)) {
            throw new TimeSlotServiceException("Not enough data to create timeslot");
        }

        List<User> users = userService.getUsers(timeslotDto.users().stream().map(u -> u.userName()).collect(Collectors.toList()));

        if (!User.ifUsersHaveSameGroup(users))
        {
            throw new TimeSlotServiceException("Users have different groups!");
        }

        int lastMarketPrice = -1;
        if(timeslotDto.lastMarketPrice() != null && timeslotDto.lastMarketPrice() >= 0 ) {
            lastMarketPrice = timeslotDto.lastMarketPrice();
        }

        List<Schedule> schedules = users.get(0).getGroup().getSchedulesList();
        if(schedules.isEmpty() || schedules.size() > 1) {
            throw new TimeSlotServiceException("Group has not assigned schedule or has more than 1 schedule!");
        }
        Schedule schedule = schedules.get(0);

        TimeSlot existingSlot = timeslotRepository.findByScheduleAndStartTimeAndAndBaseSlotQuantity(schedule, timeslotDto.startTime(), timeslotDto.baseSlotQuantity());

        if(existingSlot!=null)
            throw new TimeSlotServiceException("Timeslot already exist! You can use PUT method with given id instead. Timeslot ID: " + existingSlot.getId());

        TimeSlot ts = timeslotRepository.save(new TimeSlot(null, timeslotDto.startTime(), timeslotDto.baseSlotQuantity(), lastMarketPrice, schedule, users, null));
        return TimeSlotService.mapToTimeSlotDto(ts);
    }

    public TimeSlotDto addUserToTimeSlot(TimeSlotDto timeslotDto, TimeSlot timeSlotToUpdate) {

        if(timeslotDto.lastMarketPrice() != null && !timeslotDto.lastMarketPrice().equals(timeSlotToUpdate.getLastMarketPrice())) {
            throw new TimeSlotServiceException("Timeslot already exist, but last market price are different. Use update instead.");
        }
        if( timeslotDto.users() == null || timeslotDto.users().isEmpty() ) {
            throw new TimeSlotServiceException("Timeslot already exist. Cannot add users because not given.");
        }

        List<User> usersToAdd = userService.getUsers(timeslotDto.users().stream().map( u -> u.userName()).collect(Collectors.toList()));
        Set<User> currUsers = new HashSet<>(timeSlotToUpdate.getUsers());
        currUsers.addAll(usersToAdd);

        List<User> updatedUsers = new ArrayList<>(currUsers);

        timeSlotToUpdate.setUsers(updatedUsers);
        timeSlotToUpdate = timeslotRepository.save(timeSlotToUpdate);

        return mapToTimeSlotDto(timeSlotToUpdate);
    }

    public TimeSlotDto updateTimeSlot(TimeSlotDto timeslotDto, Long id) {
        if (id == null) {
            throw new TimeSlotServiceException("Id is null. It is obligatory to update timeslot.");
        }
        var timeslot = findTimeSlotById(id);

        if(timeslotDto.startTime() != null){
            timeslot.setStartTime(timeslotDto.startTime());
        }

        if(timeslotDto.baseSlotQuantity() != null) {
            timeslot.setBaseSlotQuantity(timeslotDto.baseSlotQuantity());
        }
        if(timeslotDto.lastMarketPrice() != null) {
            timeslot.setLastMarketPrice(timeslotDto.lastMarketPrice());
        }
        if( timeslotDto.users() != null ) {
            List<User> users = userService.getUsers(timeslotDto.users().stream().map( u -> u.userName()).collect(Collectors.toList()));
            timeslot.setUsers(users);
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
        return new TimeSlotDto(timeslot.getId(), timeslot.getStartTime(), timeslot.getBaseSlotQuantity(), timeslot.getLastMarketPrice(), timeslot.getUsers().size(), null, timeslot.getUsers().stream().map(u -> UserService.mapToUserShortDto(u)).collect(Collectors.toList()), timeslot.getSchedule().getId());
    }

    public static TimeSlotDto mapToTimeSlotDto(TimeSlot timeslot, String userNameFromRequest) {
        var shortUsersDtos = timeslot.getUsers().stream().map(u -> UserService.mapToUserShortDto(u)).collect(Collectors.toList());
        boolean isUserSlot = shortUsersDtos.stream().anyMatch(u -> u.userName().equals(userNameFromRequest));
        return new TimeSlotDto(timeslot.getId(), timeslot.getStartTime(), timeslot.getBaseSlotQuantity(), timeslot.getLastMarketPrice(), timeslot.getUsers().size(), isUserSlot, shortUsersDtos, timeslot.getSchedule().getId());
    }

//    public static List<GroupedTimeSlotDto> mapToGroupedTimeSlotsDto(List<TimeSlot> timeslots)
//    {
//        timeslots.sort(Comparator.comparing(TimeSlot::getStartTime));
//        List<GroupedTimeSlotDto> grupedTimeSlotDtos =  new ArrayList<>();
//        LocalDateTime lastStartTime = timeslots.get(0).getStartTime();
//        Integer slotsQuantity = timeslots.get(0).getBaseSlotQuantity();
//        Integer marketPrice = timeslots.get(0).getLastMarketPrice();
//        long idCounter = 1;
//        List<ShortTimeSlotInfoDto> shortTimeSlotInfoDtos = new ArrayList<>();
//        for(var ts : timeslots)
//        {
//            if(!ts.getStartTime().isEqual(lastStartTime) || !ts.getBaseSlotQuantity().equals(slotsQuantity))
//            {
//                grupedTimeSlotDtos.add(new GroupedTimeSlotDto(idCounter, lastStartTime, slotsQuantity, marketPrice, shortTimeSlotInfoDtos.size(), false, shortTimeSlotInfoDtos));
//                lastStartTime = ts.getStartTime();
//                slotsQuantity = ts.getBaseSlotQuantity();
//                marketPrice = ts.getLastMarketPrice();
//                idCounter++;
//                shortTimeSlotInfoDtos = new ArrayList<>();
//            }
//            shortTimeSlotInfoDtos.add(new ShortTimeSlotInfoDto(ts.getId(), ts.getUser().getUsername(), ts.getUser().getName(), ts.getUser().getSurname()));
//        }
//        grupedTimeSlotDtos.add(new GroupedTimeSlotDto(idCounter, lastStartTime, slotsQuantity, marketPrice, shortTimeSlotInfoDtos.size(), false, shortTimeSlotInfoDtos));
//        return grupedTimeSlotDtos;
//    }

}
