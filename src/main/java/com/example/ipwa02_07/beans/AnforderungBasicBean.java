package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.Anforderung;
import com.example.ipwa02_07.services.AnforderungService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class AnforderungBasicBean implements Serializable {

    @Inject
    private AnforderungService anforderungService;

    private String titel;
    private String beschreibung;
    private Anforderung.Prioritaet prioritaet;
    private Anforderung.Status status;

    public void addAnforderung() {
        Anforderung neueAnforderung = new Anforderung(titel, beschreibung, prioritaet, status);
        anforderungService.createAnforderung(neueAnforderung);
        clearFields();
    }

    public List<Anforderung> getAlleAnforderungen() {
        return anforderungService.getAlleAnforderungen();
    }

    private void clearFields() {
        titel = "";
        beschreibung = "";
        prioritaet = null;
        status = null;
    }

    // Getter und Setter bleiben unverändert
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

    public Anforderung.Prioritaet getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(Anforderung.Prioritaet prioritaet) {
        this.prioritaet = prioritaet;
    }

    public Anforderung.Status getStatus() {
        return status;
    }

    public void setStatus(Anforderung.Status status) {
        this.status = status;
    }
}