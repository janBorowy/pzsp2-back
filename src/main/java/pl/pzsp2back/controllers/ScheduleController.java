package pl.pzsp2back.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.services.ScheduleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedules/{login}")
    public ResponseEntity<?> getGroupSchedule(@PathVariable("login") String login) {
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("Login not specified");
        }
        try {
            var schedules = scheduleService.getGroupSchedulesByLogin(login);
            List<ScheduleDto> schedulesDtos = schedules.stream().
                    map(s -> {
                        List<TimeSlotDto> timeSlotDtos = s.getTimeSlotList().stream().map( ts -> new TimeSlotDto(ts.getId(), ts.getStartTime(), ts.getBaseSlotQuantity(), ts.getLastMarketPrice(), ts.getUser().getLogin(), null)).collect(Collectors.toList());
                        return new ScheduleDto(s.getId(), s.getBaseSlotLength(), s.getName(), s.getTag(), s.getGroup().getName(), timeSlotDtos);
                    }).
                    collect(Collectors.toList());

            return ResponseEntity.ok(schedulesDtos);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
