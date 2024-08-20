package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requirements")
public class Requirement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(length = 1000)
    private String beschreibung;

    @Enumerated(EnumType.STRING)
    private Prioritaet prioritaet;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "erstellungs_datum", nullable = false)
    private LocalDateTime erstellungsDatum;

    @OneToMany(mappedBy = "anforderung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases = new ArrayList<>();

    // Konstruktoren
    public Requirement() {
    }

    public Requirement(String titel, String beschreibung, Prioritaet prioritaet, Status status) {
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.prioritaet = prioritaet;
        this.status = status;
        this.erstellungsDatum = LocalDateTime.now();
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Prioritaet getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(Prioritaet prioritaet) {
        this.prioritaet = prioritaet;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getErstellungsDatum() {
        return erstellungsDatum;
    }

    public void setErstellungsDatum(LocalDateTime erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    // Hilfsmethoden für die Verwaltung der Beziehung
    public void addTestCase(TestCase testCase) {
        testCases.add(testCase);
        testCase.setAnforderung(this);
    }

    public void removeTestCase(TestCase testCase) {
        testCases.remove(testCase);
        testCase.setAnforderung(null);
    }

    // Enums für Priorität und Status
    public enum Prioritaet {
        MUST, SHOULD, COULD
    }

    public enum Status {
        NEU, IN_BEARBEITUNG, ABGESCHLOSSEN
    }

    // Optional: toString Methode für einfachere Debugging
    @Override
    public String toString() {
        return "Anforderung{" +
                "id=" + id +
                ", titel='" + titel + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                ", prioritaet=" + prioritaet +
                ", status=" + status +
                ", erstellungsDatum=" + erstellungsDatum +
                '}';
    }
}