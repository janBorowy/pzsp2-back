package pl.pzsp2back.mapper;

import java.util.Map;

public class Result {
    private Map<Long, Integer> timeSlotsPrices; // timeSlotID, new Price
    private Map<String, Long> vDown; // Offer owner login, timeslotID - zmienil stan na 'wolne'
    private Map<String, Long> vUp; // Offer owner login, timeslotID - zmienil stan na 'praca'

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Result {\n");

        sb.append("  timeSlotsPrices:\n");
        for (Map.Entry<Long, Integer> entry : timeSlotsPrices.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("  vDown:\n");
        for (Map.Entry<String, Long> entry : vDown.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("  vUp:\n");
        for (Map.Entry<String, Long> entry : vUp.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("}");

        return sb.toString();
    }
}