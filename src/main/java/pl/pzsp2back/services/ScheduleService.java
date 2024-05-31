package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.exceptions.ScheduleServiceException;
import pl.pzsp2back.orm.Schedule;
import pl.pzsp2back.orm.ScheduleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    public Schedule updateSchedule(ScheduleDto scheduleDto) {
        if(scheduleDto.id() == null) {
            throw new ScheduleServiceException("Id not given.");
        }
        Schedule schedule = findScheduleById(scheduleDto.id());

        if(scheduleDto.slotLength() != null){
            schedule.setBaseSlotLength(scheduleDto.slotLength());
        }
        if(scheduleDto.scheduleName() != null) {
            schedule.setName(scheduleDto.scheduleName());
        }
        if(scheduleDto.scheduleTag() != null){
            schedule.setTag(scheduleDto.scheduleTag());
        }
        schedule = scheduleRepository.save(schedule);
        return schedule;
    }

    public Schedule getSchedule(Long id) {
        return findScheduleById(id);
    }
    public Schedule getGroupScheduleByLogin(String login) {
        return getOneSchedule(getGroupSchedulesByLogin(login));
    }

    private List<Schedule> getGroupSchedulesByLogin(String login) throws RuntimeException {
        var user = userService.findUserByLogin(login);
        var groupSchedules = user.getGroup().getSchedulesList();
        if(groupSchedules != null)
        {
            return groupSchedules;
        } else {
            throw new RuntimeException("Schedules not found");
        }
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).
                orElseThrow(() -> new ScheduleServiceException("Schedule not found"));
    }

    public static Schedule getOneSchedule(List<Schedule> schedules) {
        if(schedules.isEmpty()) {
            throw new ScheduleServiceException("Group has not assigned schedule!");
        } else if (schedules.size() >1 ) {
            throw new ScheduleServiceException("Group has assigned more than 1 schedule!");
        } else {
            return schedules.get(0);
        }
    }
}
