package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.DtoMapper;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.exceptions.TimeSlotServiceException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.TimeSlotService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeSlot(@AuthenticationPrincipal User requesterUser, @PathVariable("id") Long id) {
        try {
            if ((timeSlotService.ifSameGroup(requesterUser.getLogin(), id) && requesterUser.getIfAdmin()) || timeSlotService.ifTimeSlotWorker(requesterUser.getLogin(), id)) {
                TimeSlotDto timeslot = dtoMapper.toDto(timeSlotService.getTimeSlot(id));
                return ResponseEntity.ok(timeslot);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTimeSlot(@AuthenticationPrincipal User requesterUser, @Valid @RequestBody TimeSlotDto timeslotDto) {
        try {
            if (requesterUser.getIfAdmin()){
                var newTimeSlotDto = dtoMapper.toDto(timeSlotService.createTimeSlot(timeslotDto));
                return new ResponseEntity<>(newTimeSlotDto, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserServiceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> createTimeSlots(@AuthenticationPrincipal User requesterUser, @Valid @RequestBody List<TimeSlotDto> timeSlotDtos) {
        try {
            if (requesterUser.getIfAdmin()) {

                List<TimeSlotDto> newTimeSlotDtos = new ArrayList<>();
                for (var ts : timeSlotDtos) {
                    newTimeSlotDtos.add(dtoMapper.toDto(timeSlotService.createTimeSlot(ts)));
                }
                return new ResponseEntity<>(newTimeSlotDtos, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTimeSlot(@AuthenticationPrincipal User requesterUser, @RequestBody TimeSlotDto timeslotDto, @PathVariable("id") Long id) {
        try {
            if (requesterUser.getIfAdmin()) {

                var newTimeSlotDto = dtoMapper.toDto(timeSlotService.updateTimeSlot(timeslotDto, id));
                return ResponseEntity.ok(newTimeSlotDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeSlot(@AuthenticationPrincipal User requesterUser, @PathVariable("id") Long id) {
        try {
            if ((timeSlotService.ifSameGroup(requesterUser.getLogin(), id) && requesterUser.getIfAdmin())) {
                var deletedTimeSlot = dtoMapper.toDto(timeSlotService.deleteTimeSlot(id));
                return ResponseEntity.ok(deletedTimeSlot);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
