
![Group 1](https://github.com/user-attachments/assets/ef223552-32b3-4815-a96a-10977768d6dc)

Sim Broker ist eine mobile Spiel-App, die im Rahmen meiner Abschlussarbeit für das Modul „Kotlin mit Jetpack Compose“ entwickelt wurde. Die App bietet einen spielerischen Einstieg in die Welt des Kryptowährungshandels – völlig risikofrei und realitätsnah durch die Anbindung an echte Kursdaten.
Mit virtuellem Startkapital und variablen Spielparametern simuliert Sim Broker den Handel mit bekannten Kryptowährungen. Die App kombiniert modernes UI/UX mit einer sauberen Architektur, um eine realistische, gleichzeitig aber leicht zugängliche Spielerfahrung zu ermöglichen.

# Features

## PortfolioScreen
- Übersicht aller aktiven Investments 
- Detailansicht pro Coin inkl. Wochenchart & Transaktionshistorie 
- Coins als Favoriten markieren 
- Schnellzugriff auf Kauf-/Verkaufsfunktionen

## CoinScreen
- Liste aller Kryptowährungen aus der API
- Interaktive Wochencharts je Coin 
- Echtzeit-Datenabruf alle X Sekunden 
- Suchfunktion für schnellen Zugriff

## Buy/Sell Screen
- Informationen zur ausgewählten Kryptowährung 
- Kaufen/Verkaufen über festen Betrag oder Coin-Anteile 
- Berechnung inkl. dynamischer Transaktionsgebühr

## Account Screen
- Übersicht über verfügbares & investiertes Kapital 
- Auswahl der Spielstufen (Easy, Medium, Pro, Custom)
- Anpassung von Startkapital und Gebühren 
- Kontoaufladung im Custom-Modus 
- Persönliches Spieler-Ranking

# Technologien & Architektur

# Technologien
- Kotlin als Hauptprogrammiersprache der App 
- Jetpack Compose, ein modernes, deklaratives UI-Toolkit von Android 
- MVVM, Architekturpattern für klare Trennung von UI und Logik 
- Room, lokale Datenbank zur Speicherung von Transaktionen & User-Daten 
- DataStore, für persistenten Zugriff auf Einstellungen (z.B. Schwierigkeitsgrad, Gebühren)
- Dependency Injection (DI), strukturierte Abhängigkeitsverwaltung mit z.B. Koil 
- API zum Abruf von Echtzeitkursen über eine externe Schnittstelle 
- Mock-Daten für Offline-Support durch vorgefertigte Beispiel-Daten

# Hinweise zur API-Nutzung

Die API "Coinranking" zur Kursabfrage steht nur im Rahmen der Prüfungsphase zur Verfügung. Sollte die Schnittstelle nicht erreichbar sein, kann mit Mockdaten gespielt werden, um exemplarisch die Funktionen der App testen. Das Einbinden eines eigenen API Keys erfolgt ansonsten über die local.properties mit (API_KEY = "your key").


## Screens App-Stand: 02.05.2025
![image 22](https://github.com/user-attachments/assets/4cb8370f-d576-429a-8988-084ebacd7722)

---

<p xmlns:cc="http://creativecommons.org/ns#" xmlns:dct="http://purl.org/dc/terms/">Code by <a rel="cc:attributionURL dct:creator" property="cc:attributionName" href="https://github.com/N-E-O-N-E">Markus Wirtz</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/?ref=chooser-v1" target="_blank" rel="license noopener noreferrer" style="display:inline-block;">CC BY-NC-SA 4.0<img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/cc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/by.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/nc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/sa.svg?ref=chooser-v1" alt=""></a></p>
