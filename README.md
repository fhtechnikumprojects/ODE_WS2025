## WoBimIch

## Gruppenmitglieder*innen
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


- Ziel PP3: 
  - Should have Features implementieren
  - Should have Feature testen
  - Nice to have Feature implementieren
  - Nice to have Feature testen


- Ziel PP4: 
  - Overkill Features implementieren
  - Overkill Features testen
  - Kommentare hinzufügen/ergänzen/überarbeiten von Funktionen/Klassen
  - readme.md - Datei überarbeiten
  - java doc erstellen

## Projektbeschreibung
WoBimIch ist ein Smart Mirror, der die fünf nächstgelegensten Haltestellen der Wiener Linien anhand des angegebenen Standords anzeigt. 
Dafür soll die API der Wiener Linien verwendet werden. Ähnlich wie die Wien Mobil App der Wiener Linien.

Die Anwendung nutzt zwei verschiedene öffentliche APIs, um die benötigten Informationen zu ermitteln:
 1. Address-API der Stadt Wien: Benutzer*in gibt einen Straßennamen oder einen Straßennamen mit Hausnummer ein. Die API wandelt diese Adresse in geografische Koordinaten (Längengrad und Breitengrad) um.
 2. Wiener Linien Echtzeit-Monitoring API: Mit den Koordinaten wird zunächst die nächstgelegene Haltestelle ermittelt. Anschließend werden für diese Haltestelle Abfahrtszeiten, Linieninformationen und Verkehrsmitteltypen (Bus, Straßenbahn, U-Bahn) über die Echtzeit-API abgerufen.

Ein wichtiger Aspekt bei der Nutzung der Wiener Linien API ist der Unterschied zwischen Haltestellen und Haltepunkten:
- Haltestelle: Eine Haltestelle ist der allgemeine Standort, an dem Fahrgäste auf Fahrzeuge warten. 
Eine Haltestelle kann mehrere Linien bedienen und dient als übergeordneter Ort, der im Fahrplan und in der App als Name angezeigt wird (z. B. „Stephansplatz“ oder „Westbahnhof“).


- Haltepunkt: Ein Haltepunkt ist ein konkreter Punkt innerhalb einer Haltestelle, an dem eine bestimmte Linie in eine bestimmte Richtung hält. Haltepunkte sind wichtig für die Anzeige von Abfahrtszeiten und Richtungen, da unterschiedliche Linien oder Richtungen an verschiedenen Stellen derselben Haltestelle abfahren können.

Durch die Unterscheidung von Haltestellen und Haltepunkten kann WoBimIch nicht nur die nächstgelegenen Haltestellen anzeigen, sondern auch Informationen zu Abfahrtszeiten, Linien und Richtungen liefern.


## Funktionen

- **Standorteingabe** über Eingabefeld (Suche wid mit Timestamp gespeichert)
- Anzeige von **fünf nächstgelegenen Haltestellen** ausgehend vom eingegebener Standorteingabe
- Anzeige von **Abfahrts-/Linieninformationen**
- **Filter** nach Verkehrsmittel (Bus, Straßenbahn, U-Bahn)
- **Favoriten** speichern und wieder entfernen
- **Fun-Facts** geben zufällige Fakten 
- **Light/Dark** Themes anwendbar

## Projektstruktur

##### src/main/java/org.example.project_wobimich
- **api**          → APIs (AddressAPIClient, APIClient, RealTimeMonitirAPIClient, APIException)
- **dto**          → AddressDTO, RealTimeMonitorDTO
- **model**        → Klassen (FunFact, LineStation, Location, Station)
- **service**      → AddressLookupService, FavoriteService
- **ui**           → WobimichView, WobimichController
- **utils**        → FunFactUtils, LineStationUtils
- WobimichApplication

##### resources/org.example.project_wobimich/data
- wl-fun-facts.json
- wl-ogd-haltepunkte.json
- wl-ogd-haltestellen.json
- wl-ogd-linien.json

## Verwendete APIs & Quellen

- Adressen von Wien API
- Wiener Linien API
- JsonFiles im Projekt
  - Haltestellen von Wiener Linien
  - Haltepunkte von Wiener Linien
  - Fakten über Wiener Linien

## Starten der Anwendung
`WobimichApplication` (org.example.project_wobimich.WobimichApplication) ausführen






