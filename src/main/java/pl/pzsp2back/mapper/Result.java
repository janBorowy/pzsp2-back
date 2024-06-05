package pl.pzsp2back.mapper;



import org.antlr.v4.runtime.misc.MultiMap;

import java.util.Collection;
import java.util.Map;

public class Result {
    private Map<Long, Integer> timeSlotsPrices; // timeSlotID, new Price
    private MultiMap<Long, String> vDown; // Offer owner login, timeslotID - zmienil stan na 'wolne'
    private MultiMap<Long, String> vUp; // Offer owner login, timeslotID - zmienil stan na 'praca'

    public Result(Map<Long, Integer> timeSlotsPrices, MultiMap<Long, String> vDown, MultiMap<Long, String> vUp) {
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

    public MultiMap<Long, String> getvDown() {
        return vDown;
    }

    public MultiMap<Long, String> getvUp() {
        return vUp;
    }

    public void setvDown(MultiMap<Long, String> vDown) {
        this.vDown = vDown;
    }

    public void setvUp(MultiMap<Long, String> vUp) {
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
        for (Long key : vDown.keySet()) {
            for (String value : vDown.get(key)) {
                sb.append("    ").append(value).append(": ").append(key).append("\n");
            }
        }

        sb.append("  vUp:\n");
        for (Long key : vUp.keySet()) {
            for (String value : vUp.get(key)) {
                sb.append("    ").append(value).append(": ").append(key).append("\n");
            }
        }

        sb.append("}");

        return sb.toString();
    }
}