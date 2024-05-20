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

    public ScheduleDto updateSchedule(ScheduleDto scheduleDto) {
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
        return mapToScheduleDto(schedule);
    }

    public Schedule getSchedule(Long id) {
        return findScheduleById(id);
    }

    public List<Schedule> getGroupSchedulesByLogin(String login) throws RuntimeException {
        var user = userService.getUser(login);
        var groupSchedules = user.getGroup().getSchedulesList();
        if(groupSchedules != null)
        {
            return groupSchedules;
        } else {
            throw new RuntimeException("Schedules not found");
        }
    }

    public static ScheduleDto mapToScheduleDto(Schedule schedule) {
        return new ScheduleDto(schedule.getId(), schedule.getBaseSlotLength(), schedule.getName(), schedule.getTag(), schedule.getGroup().getName(), schedule.getTimeSlotList().stream().map(ts -> TimeSlotService.mapToTimeSlotDto(ts)).collect(Collectors.toList()));
    }


    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).
                orElseThrow(() -> new ScheduleServiceException("Schedule not found"));
    }
}
