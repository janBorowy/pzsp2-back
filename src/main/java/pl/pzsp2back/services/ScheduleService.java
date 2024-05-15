package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.exceptions.ScheduleServiceException;
import pl.pzsp2back.orm.Schedule;
import pl.pzsp2back.orm.ScheduleRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;


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


    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).
                orElseThrow(() -> new ScheduleServiceException("Schedule not found"));
    }
}
