package pl.pzsp2back.mapper;

import pl.pzsp2back.orm.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapper {

    public void mapDataFile(OptimizationProcess optimizationProcess){
        List<TradeOffer> validOffers = optimizationProcess.getTradeOffersList()
                .stream()
                .filter(tradeOffer -> tradeOffer.getStatus() == OfferStatus.ACTIVE)
                .collect(Collectors.toList());
        List<String> usersOffers = optimizationProcess.getProcessOwner().getGroup()
                .getUsersList()
                .stream()
                .map(User::getLogin)
                .sorted()
                .collect(Collectors.toList());
        List<Long> offerIds = validOffers.stream()
                .map(TradeOffer::getId)
                .collect(Collectors.toList());

        File file_handler = createDateFile(optimizationProcess.getProcessOwner().getGroup().getId());

        try {
            writeWorkers(file_handler, usersOffers);
        } catch (IOException e) {
            System.out.println("Error during mapping workers");
            e.printStackTrace();
        }

        try {
            writeTimeSlots(file_handler, validOffers);
        } catch (IOException e) {
            System.out.println("Error during mapping time slots");
            e.printStackTrace();
        }

        try {
            writeTradeOfferts(file_handler, validOffers);
        } catch (IOException e) {
            System.out.println("Error during mapping trade offers");
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

    public void writeTimeSlots(File handler, List<TradeOffer> validOffers) throws IOException {
        Set<Long> uniqueTimeslotIds = validOffers.stream()
                .map(TradeOffer::getTimeslot)
                .map(TimeSlot::getId)
                .collect(Collectors.toSet());

        BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
        writer.write("T := ");
        String ids = uniqueTimeslotIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        writer.write(ids);
        writer.write(";\n");
        writer.close();
    }

    public void writeTradeOfferts(File handler, List<TradeOffer> validOffers) throws IOException {
    List<String> distinctOfferOwnerLogins = validOffers.stream()
            .filter(offer -> !offer.getIfWantOffer())
            .map(TradeOffer::getOfferOwner)
            .map(User::getLogin)
            .sorted()
            .collect(Collectors.toList());

    BufferedWriter writer = new BufferedWriter(new FileWriter(handler));
    writer.write("OfertyWant := ");
    for (int i = 0; i < distinctOfferOwnerLogins.size(); i++) {
        writer.write("\"" + distinctOfferOwnerLogins.get(i) + "\"");
        if (i < distinctOfferOwnerLogins.size() - 1) {
            writer.write(", ");
        }
    }
    writer.write(";\n");
    writer.close();
    }

    public void writeSchedule(File handler, List<TradeOffer> validOffers) throws IOException {

    }
}
