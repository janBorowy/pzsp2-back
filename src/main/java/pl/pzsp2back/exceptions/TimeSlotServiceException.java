package pl.pzsp2back.exceptions;

import pl.pzsp2back.services.TimeSlotService;

public class TimeSlotServiceException extends RuntimeException {
    public TimeSlotServiceException(String message) {
        super(message);
    }
}
