package pl.pzsp2back.mapper;

import pl.pzsp2back.orm.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class Mapper {

    public void mapDataFile(OptimizationProcess optimizationProcess){
        List<TradeOffer> validOffers = optimizationProcess.getTradeOffersList()
                .stream()
                .filter(TradeOffer::getIsActive)
                .collect(Collectors.toList());
        List<String> allLoginsSorted = optimizationProcess.getProcessOwner().getGroup()
                .getUsersList()
                .stream()
                .map(User::getLogin)
                .sorted()
                .collect(Collectors.toList());
        List<Long> offerIds = validOffers.stream()
                .map(TradeOffer::getId)
                .collect(Collectors.toList());
        List<Long> uniqueSortedTimeslotIds = validOffers.stream()
                .map(TradeOffer::getTimeslot)
                .map(TimeSlot::getId)
                .collect(Collectors.toSet())
                .stream()
                .sorted()
                .collect(Collectors.toList());
        List<TimeSlot> timeslots = validOffers.stream()
                .map(TradeOffer::getTimeslot)
                .collect(Collectors.toList());
        List<TradeOffer> wantDownOffers = validOffers.stream()
                .filter(offer -> offer.getTimeslot().getUsers().stream()
                        .map(User::getLogin)
                        .anyMatch(login -> login.equals(offer.getOfferOwner().getUsername())))
                .filter(offer -> offer.getIfWantOffer())
                .collect(Collectors.toList());
        List<String> wantDownUniqueLogins = wantDownOffers.stream()
                .map(TradeOffer::getOfferOwner)
                .map(User::getLogin)
                .sorted()
                .collect(Collectors.toList());
        List<TradeOffer> canDownOffers = validOffers.stream()
                .filter(offer -> offer.getTimeslot().getUsers().stream()
                        .map(User::getLogin)
                        .anyMatch(login -> login.equals(offer.getOfferOwner().getUsername())))
                .filter(offer -> !offer.getIfWantOffer())
                .collect(Collectors.toList());
        List<TradeOffer> canUpOffers = validOffers.stream()
                .filter(offer -> offer.getTimeslot().getUsers().stream()
                        .map(User::getLogin)
                        .noneMatch(login -> login.equals(offer.getOfferOwner().getUsername())))
                .filter(offer -> !offer.getIfWantOffer())
                .collect(Collectors.toList());
        List<String> canUpUniqueLogins = canUpOffers.stream()
                .map(TradeOffer::getOfferOwner)
                .map(User::getLogin)
                .sorted()
                .collect(Collectors.toList());

        File file_handler = createDateFile(optimizationProcess.getProcessOwner().getGroup().getId());

        try {
            writeWorkers(file_handler, allLoginsSorted);
        } catch (IOException e) {
            System.out.println("Error during mapping workers");
            e.printStackTrace();
        }

        try {
            writeTimeSlots(file_handler, uniqueSortedTimeslotIds);
        } catch (IOException e) {
            System.out.println("Error during mapping time slots");
            e.printStackTrace();
        }

        try {
            writeTradeOfferts(file_handler, wantDownUniqueLogins);
        } catch (IOException e) {
            System.out.println("Error during mapping trade offers");
            e.printStackTrace();
        }

        try {
            writeSchedule(file_handler, allLoginsSorted, uniqueSortedTimeslotIds, timeslots);
        } catch (IOException e) {
            System.out.println("Error during mapping schedule");
            e.printStackTrace();
        }

        try {
            writeWantDown(file_handler, wantDownUniqueLogins, uniqueSortedTimeslotIds, wantDownOffers);
        } catch (IOException e) {
            System.out.println("Error during mapping want down offers");
            e.printStackTrace();
        }

        try {
            writeCanDown(file_handler, allLoginsSorted, uniqueSortedTimeslotIds, canDownOffers);
        } catch (IOException e) {
            System.out.println("Error during mapping can down offers");
            e.printStackTrace();
        }

        try {
            writeCanUp(file_handler, allLoginsSorted, uniqueSortedTimeslotIds, canUpOffers);
        } catch (IOException e) {
            System.out.println("Error during mapping can up offers");
            e.printStackTrace();
        }

        try {
            writeCenaWant(file_handler, wantDownOffers, wantDownUniqueLogins);
        } catch (IOException e) {
            System.out.println("Error during mapping want price");
            e.printStackTrace();
        }

        try {
            writeCenaCan(file_handler, canUpOffers, canUpUniqueLogins);
        } catch (IOException e) {
            System.out.println("Error during mapping can price");
            e.printStackTrace();
        }

        try {
            writeMinLSlotow(file_handler, allLoginsSorted);
        } catch (IOException e) {
            System.out.println("Error during mapping minLSlotow");
            e.printStackTrace();
        }

    }
    public File createDateFile(Long groupId) {
        String filename = groupId + "_data.dat";
        File file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return file;
    }

    public void writeWorkers(File handler, List<String> logins) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("Pracownicy := ");
        for (int i = 0; i < logins.size(); i++) {
            writer.write("\"" + logins.get(i) + "\"");
            if (i < logins.size() - 1) {
                writer.write(", ");
            }
        }
        writer.write(";\n");
        writer.close();
    }

    public void writeTimeSlots(File handler, List<Long> uniqueTimeslotIds) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("T := ");
        String ids = uniqueTimeslotIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        writer.write(ids);
        writer.write(";\n");
        writer.close();
    }

    public void writeTradeOfferts(File handler, List<String> wantDownLogins) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
    writer.write("OfertyWant := ");
    for (int i = 0; i < wantDownLogins.size(); i++) {
        writer.write("\"" + wantDownLogins.get(i) + "\"");
        if (i < wantDownLogins.size() - 1) {
            writer.write(", ");
        }
    }
    writer.write(";\n");
    writer.close();
    }

    public void writeSchedule(File handler, List<String> logins, List<Long> slotsIds, List<TimeSlot> timeslots) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param v0:\n    ");
        String ids = slotsIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        writer.write(ids);
        writer.write(":=\n");

        // Write the schedule for each login
        for (String login : logins) {
            writer.write("\"" + login + "\" ");
            for (Long slot : slotsIds) {
                Optional<TimeSlot> t = timeslots.stream()
                        .filter(ts -> ts.getId().equals(slot))
                        .findFirst();
                if (t.get().getUsers().stream()
                        .map(User::getUsername)
                        .anyMatch(userLogin -> userLogin.equals(login))){
                    writer.write("1 ");
                } else {
                    writer.write("0 ");
                }
            }
            writer.write("\n");
        }
        writer.write(";\n\n");
        writer.close();
    }

    public void writeWantDown(File handler, List<String> wantDownLogins, List<Long> slotsIds, List<TradeOffer> wantDownOffers) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param WantDown:\n    ");
        String ids = slotsIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        writer.write(ids);
        writer.write(":=\n");

        for (String offerOwnerLogin : wantDownLogins) {
            writer.write("\"" + offerOwnerLogin + "\" ");
            for (Long slotId : slotsIds) {
                List<Long> userOfferSlotsId = wantDownOffers.stream()
                        .filter(ts -> ts.getOfferOwner().getUsername().equals(offerOwnerLogin))
                        .map(TradeOffer::getTimeslot)
                        .map(TimeSlot::getId)
                        .collect(Collectors.toList());
                if (userOfferSlotsId.contains(slotId)){
                    writer.write("1 ");
                } else {
                    writer.write("0 ");
                }
            }
            writer.write("\n");
        }
        writer.write(";\n\n");
        writer.close();
    }

    public void writeCanDown(File handler, List<String> logins, List<Long> slotsIds, List<TradeOffer> canDownOffers) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param CanDown:\n    ");
        String ids = slotsIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        writer.write(ids);
        writer.write(":=\n");

        for (String offerOwnerLogin : logins) {
            writer.write("\"" + offerOwnerLogin + "\" ");
            for (Long slotId : slotsIds) {
                boolean hasTradeOfferWithTimeslotAndUserLogin = canDownOffers.stream()
                        .anyMatch(offer -> offer.getTimeslot().getId().equals(slotId) &&
                                offer.getOfferOwner().getUsername().equals(offerOwnerLogin));
                if (hasTradeOfferWithTimeslotAndUserLogin){
                    writer.write("1 ");
                } else {
                    writer.write("0 ");
                }
            }
            writer.write("\n");
        }
        writer.write(";\n\n");
        writer.close();
    }

    public void writeCanUp(File handler, List<String> logins, List<Long> slotsIds, List<TradeOffer> canUpOffers) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param CanUp:\n    ");
        String ids = slotsIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        writer.write(ids);
        writer.write(":=\n");

        for (String offerOwnerLogin : logins) {
            writer.write("\"" + offerOwnerLogin + "\" ");
            for (Long slotId : slotsIds) {
                boolean hasTradeOfferWithTimeslotAndUserLogin = canUpOffers.stream()
                        .anyMatch(offer -> offer.getTimeslot().getId().equals(slotId) &&
                                offer.getOfferOwner().getUsername().equals(offerOwnerLogin));
                if (hasTradeOfferWithTimeslotAndUserLogin){
                    writer.write("1 ");
                } else {
                    writer.write("0 ");
                }
            }
            writer.write("\n");
        }
        writer.write(";\n\n");
        writer.close();
    }

    public void writeCenaWant(File handler, List<TradeOffer> wantDownOffers, List<String> uniqueWantDownLogins) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param cenaWant:=\n");
        for(String login: uniqueWantDownLogins){
            writer.write("\"" + login + "\" ");
            Optional<TradeOffer> firstMatchingOffer = wantDownOffers.stream()
                    .filter(offer -> offer.getOfferOwner().getLogin().equals(login))
                    .findFirst();
            Integer price =  firstMatchingOffer.map(TradeOffer::getPrice)
                    .orElse(0);
            writer.write(price + "\n");
        }
        writer.write(";\n\n");
    }

    public void writeCenaCan(File handler, List<TradeOffer> canUpOffers, List<String> uniquecanUpLogins) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param cenaCan:=\n");
        for(String login: uniquecanUpLogins){
            writer.write("\"" + login + "\" ");
            Optional<TradeOffer> firstMatchingOffer = canUpOffers.stream()
                    .filter(offer -> offer.getOfferOwner().getLogin().equals(login))
                    .findFirst();
            Integer price =  firstMatchingOffer.map(TradeOffer::getPrice)
                    .orElse(0);
            writer.write(price + "\n");
        }
        writer.write(";\n\n");
        writer.close();
    }

    public void writeMinLSlotow(File handler, List<String> uniqueLogins) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("param minLSlotow:=\n");
        for(String login: uniqueLogins){
            writer.write("\"" + login + "\" ");
            writer.write("1\n");
        }
        writer.write(";\n\n");
        writer.close();
    }

}
