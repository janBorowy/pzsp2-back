package pl.pzsp2back.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Runner {
    public Result runAmpl() {
        Map<Long, Integer> timeSlotsPrices = new HashMap<>();
        Map<String, Long> vUp = new HashMap<>();
        Map<String, Long> vDown = new HashMap<>();

        try {
            // Uruchomienie komendy ampl
            ProcessBuilder processBuilder = new ProcessBuilder("ampl");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            System.out.println("uruchomiono ampl");
            // Odczytanie wyników z ampl
            String line;

            // Wybór optymalizatora w amplu
            process.getOutputStream().write("option solver minos;\n".getBytes());
            process.getOutputStream().flush();
            System.out.println("wybrano optymalizator");

            // Wybór pliku .mod i pliku .dat - odpowiednia ścieżka do pliku
            process.getOutputStream().write("model maxsloty.mod;\n".getBytes());
            process.getOutputStream().flush();
            process.getOutputStream().write("data maxsloty.dat;\n".getBytes());
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
                    if (value == 1){
                        vDown.put(login, timeSlotId);
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
                    if (value == 1){
                        vUp.put(login, timeSlotId);
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
