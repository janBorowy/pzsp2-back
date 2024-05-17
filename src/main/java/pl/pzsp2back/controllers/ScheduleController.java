package pl.pzsp2back.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.services.ScheduleService;



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
            ScheduleDto scheduleDto = ScheduleService.mapToScheduleDto(schedules.get(0));

            return ResponseEntity.ok(scheduleDto);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
