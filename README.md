# PZSP2 - Aukcja slotów - backend

## Cel Projektu
System pozwalający na zarządzanie grafikiem, w który zaangażowane
jest wiele podmiotów. Pozwala na tworzenie, przeglądanie i modyfikację istniejącego
grafiku. Dostępna jest funkcjonalność optymalizacji grafiku pod kątem zadowolenia
uczestników. W tym celu uczestnicy składają w systemie transakcje wymiany terminami,
które system przegląda i dostosowuje grafik. Dostęp do aplikacji odbywa się
poprzez stronę internetową.

## Docker
```
cd docker
docker compose up -d
```
Wejdź do psql:
```
docker exec -it postgres sh -c "psql -d timeTrader -U admin -W"
```


## Możliwe problemy do napotkania
- pgAdmin uruchamia się w tle i trzeba wyłączyć jego proces
- trzeba ustawić/stworzyć nowy schemat `public` w bazie danych timeTrader
- dodać plik .env do główego folderu i wstawić w niego np. `JWT_SECRET='5pAq6zRyX8bC3dV2wS7gN1mK9jF0hL4tUoP6iBvE3nG8xZaQrY7cW2fA'` 