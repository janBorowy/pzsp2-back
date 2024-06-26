package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.exceptions.TimeSlotServiceException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.services.TimeSlotService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeSlot(@PathVariable("id") Long id) {
        try {
            TimeSlotDto timeslot = timeSlotService.getTimeSlot(id);
            return ResponseEntity.ok(timeslot);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTimeSlot(@Valid @RequestBody TimeSlotDto timeslotDto) {
        try {
            var newTimeSlotDto = timeSlotService.createTimeSlot(timeslotDto);
            return new ResponseEntity<>(newTimeSlotDto, HttpStatus.CREATED);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserServiceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> createTimeSlots(@Valid @RequestBody List<TimeSlotDto> timeSlotDtos) {
        try {
            List<TimeSlotDto> newTimeSlotDtos = new ArrayList<>();
            for(var ts : timeSlotDtos)
            {
                newTimeSlotDtos.add(timeSlotService.createTimeSlot(ts));
            }
            return new ResponseEntity<>(newTimeSlotDtos, HttpStatus.CREATED);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTimeSlot(@RequestBody TimeSlotDto timeslotDto, @PathVariable("id") Long id) {
        try {
            var newTimeSlotDto = timeSlotService.updateTimeSlot(timeslotDto, id);
            return ResponseEntity.ok(newTimeSlotDto);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeSlot(@PathVariable("id") Long id) {
        try {
            var deletedTimeSlot = timeSlotService.deleteTimeSlot(id);
            return ResponseEntity.ok(deletedTimeSlot);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
