package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.mockito.internal.verification.Times;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.exceptions.TimeSlotServiceException;
import pl.pzsp2back.orm.*;

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


    public TimeSlot getTimeSlot(Long id) {
        TimeSlot timeslot = findTimeSlotById(id);
        return timeslot;
    }


    @Transactional
    public TimeSlot createTimeSlot(TimeSlotDto timeslotDto) {
        if (!validateTimeslotDto(timeslotDto)) {
            throw new TimeSlotServiceException("Not enough data to create timeslot");
        }

        List<User> users = userService.findUsersByLogin(timeslotDto.users().stream().map(u -> u.login()).collect(Collectors.toList()));

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
            throw new TimeSlotServiceException("Timeslot already exist! You can use PUT method to update slot with given id instead. Timeslot ID: " + existingSlot.getId());

        return timeslotRepository.save(new TimeSlot(null, timeslotDto.startTime(), timeslotDto.baseSlotQuantity(), lastMarketPrice, schedule, users, null));
    }

    public TimeSlot addUserToTimeSlot(TimeSlotDto timeslotDto, TimeSlot timeSlotToUpdate) {

        if(timeslotDto.lastMarketPrice() != null && !timeslotDto.lastMarketPrice().equals(timeSlotToUpdate.getLastMarketPrice())) {
            throw new TimeSlotServiceException("Timeslot already exist, but last market price are different. Use update instead.");
        }
        if( timeslotDto.users() == null || timeslotDto.users().isEmpty() ) {
            throw new TimeSlotServiceException("Timeslot already exist. Cannot add users because not given.");
        }

        List<User> usersToAdd = userService.findUsersByLogin(timeslotDto.users().stream().map( u -> u.login()).collect(Collectors.toList()));
        Set<User> currUsers = new HashSet<>(timeSlotToUpdate.getUsers());
        currUsers.addAll(usersToAdd);

        List<User> updatedUsers = new ArrayList<>(currUsers);

        timeSlotToUpdate.setUsers(updatedUsers);
        return timeslotRepository.save(timeSlotToUpdate);
    }

    public TimeSlot updateTimeSlot(TimeSlotDto timeslotDto, Long id) {
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
            List<User> users = userService.findUsersByLogin(timeslotDto.users().stream().map( u -> u.login()).collect(Collectors.toList()));
            timeslot.setUsers(users);
        }

        //not implemented changing schedule for timeslot
        return timeslotRepository.save(timeslot);
    }

    @Transactional
    public TimeSlot deleteTimeSlot(Long id) {
        TimeSlot timeslot = findTimeSlotById(id);
        timeslotRepository.delete(timeslot);
        return timeslot;
    }

    public boolean ifSameGroup(String login, Long timeslotId) {
        User user = userService.getUser(login);
        TimeSlot ts = getTimeSlot(timeslotId);

        return user.getGroup().getId().equals(ts.getSchedule().getGroup().getId());
    }

    private TimeSlot findTimeSlotById(Long id) {
        return timeslotRepository.findById(id).orElseThrow(
                () -> new TimeSlotServiceException("Timeslot not found with given id: " + id)
        );
    }


    public boolean ifTimeSlotWorker(String login, Long id) {
        var ts = getTimeSlot(id);
        return ts.getUsers().stream().anyMatch(u -> u.getLogin().equals(login));
    }
}
