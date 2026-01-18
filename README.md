## WoBimIch

## Gruppenmitglieder
- Thanathan Wongkantharaphai (ic24b123@technikum-wien.at)
- Andreas Zenz (ic23b002@technikum-wien.at)
- Kaur Paramjit (ic24b118@technikum-wien.at)

## Meilensteine
- Ziel PP1: 
  - Projekt aufsetzen (GitHub-Repository anlegen) und sicherstellen, dass jedes Gruppenmitglied Zugang auf das Repository auf GitHub hat 
  - Ordnerstruktur festlegen
  - notwendige Klassen definieren
  - recherchieren und einlesen zu den notwendigen API's
  - Sketch zur GUI entwerfen
  - Grundgerüst für die Implementierung der Features implementieren
    - Klassen und Funktionen
    - Klassen für API-Abfragen
    - Statische Daten ins Projekt einbinden
    
- Ziel PP2: 
  - Must have Features implementieren
  - Must have Features testen
  - 
- Ziel PP3: 
  - Should have Features implementieren
  - Should have Feature testen
  - Nice to have Feature implementieren
  - Nice to have Feature testen
  
- Ziel PP4: 
  - Overkill Features implementieren
  - Overkill Features testen
  - Javadoc-Datei erstellen
  - readme.md - Datei überarbeiten

## Projektbeschreibung
WoBimIch ist ein Smart Mirror, der die nächstgelegensten Haltestellen mit den Linien anhand des angegebenen Standords anzeigt. 
Dafür soll die API der Wiener Linien verwendet werden. Ähnlich wie die Wien Mobil App der Wiener Linien

## Funktionen

- **Standorteingabe** über Eingabefeld (Suche wid mit Timestamp gespeichert)
- Anzeige passender **Haltestellen**
- Anzeige von **Abfahrts-/Linieninformationen**
- **Flter** nach Verkehrsmittel (Bus, Straßenbahn, U-Bahn)
- **Favoriten** speichern und wieder entfernen
- **Fun-Facts** geben zufällige Fakten 
- **Light/Dark** Themes anwendbar

## Projektstruktur

##### src/main/java/org.example.project_wobimich
- **api**          → APIs (AddressAPIClient, APIClient, RealTimeMonitirAPIClient)
- **dto**          → AddressDTO, RealTimeMonitorDTO
- **model**        → Klassen (FunFact, LineStation, Location, LocationHistoryLogger, LocationLogEntry, Station)
- **service**      → AddressLookupService, FavoriteService
- **ui**           → WobimichUI
- **utils**        → APIException, HelloController, Launcher, WobimichApplication

##### resources/org.example.project_wobimich/data
- jsonFiles: wl-fun-facts.json (FunFact Daten), wl-ogd-haltepunkte.json (Haltepunkte), wl-ogd-haltestellen.json (Haltstellen), wl-ogd-linie.json (Linien)
- jsonFiles: favorites.json (Favorites werden gespeichert), search_history.json (gesuchte Standorte werden mit Timestamp gespeichert)
- Bilder (Logo: Wobimich.png)

## Verwendete APIs & Quellen

- Wiener Linien API
- JsonFiles im Projekt

## Fehlerbehandlungen/Exception Handling

### IOException = implementiert mit try-catch:

- **logSearch** → Speichern der Suchhistorie in search-history.json
- **logLocation** → Fehler beim Lesen/Schreiben 
- **saveFavorites** → Speichern vo Favoriten in favorites.txt
- **loadFavorites** → Laden der Favoriten aus favorites.txt

### Service-Anfragen/Netzwerkfehler - implementiert mit setOnFailed

- **handleSearch** → Fehlermeldung bei ungültiger Adresse
- **handleStationSelection** → Fehelermeldung bei fehlenden Abfahrtszeiten
- **Doppelklick auf Favoriten** → Fehlermeldung bei Verbindungsproblemen

## Starten des Projektes

## Voraussetzungen
- Java JDK 17 oder höher
- JavaFX installiert und korrekt konfiguriert
- IDE (z. B. IntelliJ IDEA oder Eclipse)

## Projekt starten
1. Projekt aus dem Repository klonen
2. Projekt in der IDE öffnen
3. Sicherstellen, dass JavaFX als Library eingebunden ist
4. Die Klasse `WobimichApplication` ausführen

## Startklasse
org.example.project_wobimich.WobimichApplication




