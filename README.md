<p xmlns:cc="http://creativecommons.org/ns#" xmlns:dct="http://purl.org/dc/terms/">Code by <a rel="cc:attributionURL dct:creator" property="cc:attributionName" href="https://github.com/N-E-O-N-E">Markus Wirtz</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/?ref=chooser-v1" target="_blank" rel="license noopener noreferrer" style="display:inline-block;">CC BY-NC-SA 4.0<img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/cc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/by.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/nc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/sa.svg?ref=chooser-v1" alt=""></a></p>

---

![Group 1](https://github.com/user-attachments/assets/ef223552-32b3-4815-a96a-10977768d6dc)

Sim Broker ist eine kleine App, die im Rahmen meiner Abschlussarbeit für das Modul "Kotlin mit Jetpack Compose" programmiert wird. Sie vermittelt spielerisch erste Erfahrungen mit dem Handel von Kryptowährungen. Mithilfe eines virtuellen Guthabens und der Anbindung externer Kursdaten über eine Schnittstelle wirkt das Handelserlebnis nahezu realistisch. Nutzer können aus einer Auswahl bekannter Kryptowährungen wählen, Gewinne erzielen und Verluste erleben – ganz ohne Risiko. Die Spieleinstellungen erlauben vordefinierte Level (Easy, Medium, Pro) mit vordefiniertem Startkapital und einer niedrigen oder hohen Transaktionsgebühr. Die Gebühr lässt sich im Anschluss immer noch verändern, ebenso das Guthaben, welches man bis zu einem Maximalbetrag aufladen kann. Sollte die API keine Daten liefern - da diese nur für den Zeitrasum der Prüfungsphase altiv ist - kann mit sog. Mockdaten exemplarisch gespielt werden. 

# Kernfunktionen

## PortfolioScreen
- Übersicht aktiver Investments mit der Möglichkeit Details (Charts, Transaktionen pro Coin) anzuzeigen
- Auch hier kann mit Klick auf den Coin die View zum Kauf/Verkauf anzegeit werden.

## CoinScreen
- Auflistung der Coins aus API mit Pagination (segmentiertes Laden der Daten)
- Schnelle Suchfunktion über alle verfügbaren Kryptowährungen
- Counter der alle X Sekunden die Daten neu Abruft

## Buy/Sell Screen
- Anzeige relevanter Informationen zur gewählten Kryptowährung
- Möglichkeit zum Kauf und Verkauf der ausgewählten Kryptowährung
- Kauf / Verkauf über Betrag X oder Anteil Coins

## Account Screen
- Übersicht von verfügbarem und investiertem Kapital
- Einstellungen der Spielschwierigkeit (Startkapital und Gebühren)
- Aufladen des Kontos bis zu einem Betrag X möglich

# Grafische Aufbereitung der App-Idee

## Erste Skizzen der Idee
![image 22](https://github.com/user-attachments/assets/ee67c7e2-cb64-42d5-ba2d-b8fb8f85fe70)

## Erste Low-Fidelity der App
![image 23](https://github.com/user-attachments/assets/80488e54-49e1-4a7c-ae7b-f54870dafcd8)

## UI der App kann im Light- und im Dark-Theme dargestellt werden
![image 24](https://github.com/user-attachments/assets/9fc46b80-2c13-4428-aa2a-afd64a74ee46)

## App-Preview: Stand 04.04.2025 ( ~ 70 % )
![AppPreview](https://github.com/user-attachments/assets/dce4c9ff-04f2-4df3-9954-72dbeff535e6)

---

<p xmlns:cc="http://creativecommons.org/ns#" xmlns:dct="http://purl.org/dc/terms/">Code by <a rel="cc:attributionURL dct:creator" property="cc:attributionName" href="https://github.com/N-E-O-N-E">Markus Wirtz</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/?ref=chooser-v1" target="_blank" rel="license noopener noreferrer" style="display:inline-block;">CC BY-NC-SA 4.0<img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/cc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/by.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/nc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/sa.svg?ref=chooser-v1" alt=""></a></p>
