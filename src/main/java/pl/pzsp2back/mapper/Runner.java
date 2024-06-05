package pl.pzsp2back.mapper;

import org.antlr.v4.runtime.misc.MultiMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {
    public Result runAmpl(Long groupId) {
        Map<Long, Integer> timeSlotsPrices = new HashMap<>();
        MultiMap<Long, String> vUp = new MultiMap<>();
        MultiMap<Long, String> vDown = new MultiMap<>();

        try {
            // Uruchomienie komendy ampl
            ProcessBuilder processBuilder = new ProcessBuilder("ampl");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            System.out.println("uruchomiono ampl");
            // Odczytanie wyników z ampl
            String line;

            // reset ampla
            process.getOutputStream().write("reset;\n".getBytes());
            process.getOutputStream().flush();
            System.out.println("zresetowano ampl");

            // Wybór optymalizatora w amplu
            process.getOutputStream().write("option solver minos;\n".getBytes());
            process.getOutputStream().flush();
            System.out.println("wybrano optymalizator");

            // Wybór pliku .mod i pliku .dat - odpowiednia ścieżka do pliku
            process.getOutputStream().write("model maxsloty.mod;\n".getBytes());
            process.getOutputStream().flush();
            process.getOutputStream().write(("data " + groupId + "_data.dat;\n").getBytes());
            process.getOutputStream().flush();

            // Optymalizacja i odczyt wyników
            process.getOutputStream().write("solve;\n".getBytes());
            process.getOutputStream().flush();

            line = reader.readLine();
            System.out.println(line);
            line = reader.readLine();
            System.out.println(line);

            System.out.println("optymalizacja zakończona");

            // Odczytanie wyników z ampl
            System.out.println("Wyniki");
            // ceny timeslotow
            process.getOutputStream().write("display ogrBilans;\n".getBytes());
            process.getOutputStream().flush();

            line = reader.readLine();
            while (line.strip() != "") {
                System.out.println(line.strip());
                String[] parts = line.strip().split("\\s+");
                if (parts.length == 2) {
                    Long timeSlotId = Long.parseLong(parts[0].replaceAll("'", ""));
                    Integer price = Integer.parseInt(parts[1]);
                    timeSlotsPrices.put(timeSlotId, price);
                }
                line = reader.readLine();
            }

            process.getOutputStream().write("display vDown;\n".getBytes());
            process.getOutputStream().flush();

            line = reader.readLine();
            while (line.strip() != "") {
                System.out.println(line.strip());
                String[] parts = line.strip().split("\\s+");
                if (parts.length == 3) {
                    String login = parts[0].replaceAll("'", "");
                    Long timeSlotId = Long.parseLong(parts[1].replaceAll("'", ""));
                    Integer value = Integer.parseInt(parts[2]);
                    if (value == 1) {
                        List<String> logins = vDown.get(timeSlotId);
                        if (logins == null) {
                            logins = new ArrayList<>();
                            vDown.put(timeSlotId, logins);
                        }
                        logins.add(login);
                        vDown.put(timeSlotId, logins);
                    }
                }
                line = reader.readLine();
            }

            process.getOutputStream().write("display vUp;\n".getBytes());
            process.getOutputStream().flush();

            line = reader.readLine();
            while (line.strip() != "") {
                System.out.println(line.strip());
                String[] parts = line.strip().split("\\s+");
                if (parts.length == 3) {
                    String login = parts[0].replaceAll("'", "");
                    Long timeSlotId = Long.parseLong(parts[1].replaceAll("'", ""));
                    Integer value = Integer.parseInt(parts[2]);
                    if (value == 1) {
                        List<String> logins = vUp.get(timeSlotId);
                        if (logins == null) {
                            logins = new ArrayList<>();
                            vUp.put(timeSlotId, logins);
                        }
                        logins.add(login);
                        vUp.put(timeSlotId, logins);
                    }
                }
                line = reader.readLine();
            }

            process.getOutputStream().close();
            process.waitFor();
            process.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Result res = new Result(timeSlotsPrices, vDown, vUp);
        return res;
    }

}
