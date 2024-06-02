package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.DtoMapper;
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.exceptions.ScheduleServiceException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.ScheduleService;
import pl.pzsp2back.services.UserService;


@RestController
@AllArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserService userService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{login}")
    public ResponseEntity<?> getGroupSchedule(@AuthenticationPrincipal User requesterUser, @PathVariable("login") String login) {
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("Login not specified");
        }
        try {
            var schedule = scheduleService.getGroupScheduleByLogin(login);
            ScheduleDto scheduleDto;
            if (requesterUser.getIfAdmin() && userService.ifSameGroup(requesterUser.getLogin(), login)) {
                scheduleDto = dtoMapper.toDto(schedule);
                return ResponseEntity.ok(scheduleDto);
            } else if (requesterUser.getLogin().equals(login)) {
                scheduleDto = dtoMapper.toDto(schedule, requesterUser);
                return ResponseEntity.ok(scheduleDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateSchedule(@AuthenticationPrincipal User requesterUser, @Valid @RequestBody ScheduleDto scheduleDto) {
        try {
            if (requesterUser.getIfAdmin()) {
                ScheduleDto updatedScheduleDto = dtoMapper.toDto(scheduleService.updateSchedule(scheduleDto));
                return ResponseEntity.ok(updatedScheduleDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
        } catch (ScheduleServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
