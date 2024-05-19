package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.exceptions.TimeSlotServiceException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.services.TimeSlotService;

@AllArgsConstructor
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    public ResponseEntity<?> postTimeSlot(@Valid @RequestBody TimeSlotDto timeslotDto) {
        try {
            var newTimeSlotDto = timeSlotService.createTimeSlot(timeslotDto);
            return new ResponseEntity<>(newTimeSlotDto, HttpStatus.CREATED);
        } catch (TimeSlotServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
