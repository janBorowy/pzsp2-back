package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.DtoMapper;
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.exceptions.ScheduleServiceException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.services.ScheduleService;



@RestController
@AllArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{login}")
    public ResponseEntity<?> getGroupSchedule(@PathVariable("login") String login) {
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("Login not specified");
        }
        try {
            var schedule = scheduleService.getGroupScheduleByLogin(login);
            ScheduleDto scheduleDto = dtoMapper.toDto(schedule);

            return ResponseEntity.ok(scheduleDto);
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        try {
            ScheduleDto updatedScheduleDto = dtoMapper.toDto(scheduleService.updateSchedule(scheduleDto));
            return ResponseEntity.ok(updatedScheduleDto);
        } catch (ScheduleServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
