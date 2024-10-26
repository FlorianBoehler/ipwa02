```mermaid
classDiagram
    class Anforderungen {
        +AnforderungID: PK
        +CustomID: string
        +Titel: string
        +Beschreibung: string
        +Priorität: string
        +Status: string
        +Erstellungsdatum: date
    }
    
    class Testfall {
        +TestfallID: PK
        +AnforderungID: FK
        +TestlaufID: FK
        +ZugeordneterTesterID: FK
        +CustomID: string
        +Titel: string
        +Beschreibung: string
        +Vorbedingungen: string
        +Erwartetes Ergebnis: string   
    }
    
    class Testlauf {
        +TestlaufID: PK
        +CustomID: string
        +Titel: string
        +Beschreibung: string
        +Startdatum: date
        +Enddatum: date
        +Status: string
    }
    
    class Benutzer {
        +BenutzerID: PK
        +Benutzername: string
        +Email: string
        +Rolle: string
        +Passwort: string
        +Status: boolean
    }
    
    class Testergebnis {
        +TestergebnisID: PK
        +TestfallID: FK
        +CustomID: string
        +Tatsächliches Ergebnis: string
        +Kommentar: string
        +Status: string
        +Ausführungsdatum: date
    }
    
    Anforderungen "1" -- "0..*" Testfall
    Testfall "0..*" -- "1" Testlauf
    Testfall "1" -- "0..*" Testergebnis
    Benutzer "1" -- "0..*" Testfall