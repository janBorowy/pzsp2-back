package pl.pzsp2back.mapper;

import java.util.Map;

public class Result {
    private Map<Long, Integer> timeSlotsPrices; // timeSlotID, new Price
    private Map<String, Long> vDown; // User login, timeslotID - zmienil stan na 'wolne'
    private Map<String, Long> vUp; // User login, timeslotID - zmienil stan na 'praca'

    public Result(Map<Long, Integer> timeSlotsPrices, Map<String, Long> vDown, Map<String, Long> vUp) {
        this.timeSlotsPrices = timeSlotsPrices;
        this.vDown = vDown;
        this.vUp = vUp;
    }

    public Map<Long, Integer> getTimeSlotsPrices() {
        return timeSlotsPrices;
    }

    public void setTimeSlotsPrices(Map<Long, Integer> timeSlotsPrices) {
        this.timeSlotsPrices = timeSlotsPrices;
    }
    public Map<String, Long> getvDown() {
        return vDown;
    }

    public Map<String, Long> getvUp() {
        return vUp;
    }

    public void setvDown(Map<String, Long> vDown) {
        this.vDown = vDown;
    }

    public void setvUp(Map<String, Long> vUp) {
        this.vUp = vUp;
    }
}