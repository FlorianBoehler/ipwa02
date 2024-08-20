package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "testcases")
public class TestCase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titel;

    @Column(nullable = false, length = 1000)
    private String beschreibung;

    @Column(length = 1000)
    private String voraussetzungen;

    @Column(nullable = false, length = 1000)
    private String erwartetesResultat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anforderung_id", nullable = false)
    private Requirement anforderung;

    // Konstruktoren, Getter und Setter

    public TestCase() {}

    public TestCase(String titel, String beschreibung, String voraussetzungen, String erwartetesResultat, Requirement anforderung) {
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.voraussetzungen = voraussetzungen;
        this.erwartetesResultat = erwartetesResultat;
        this.anforderung = anforderung;
    }
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    // Getter und Setter für alle Felder
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

    public String getVoraussetzungen() {
        return voraussetzungen;
    }

    public void setVoraussetzungen(String voraussetzungen) {
        this.voraussetzungen = voraussetzungen;
    }

    public String getErwartetesResultat() {
        return erwartetesResultat;
    }

    public void setErwartetesResultat(String erwartetesResultat) {
        this.erwartetesResultat = erwartetesResultat;
    }

    public Requirement getAnforderung() {
        return anforderung;
    }

    public void setAnforderung(Requirement anforderung) {
        this.anforderung = anforderung;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }
}