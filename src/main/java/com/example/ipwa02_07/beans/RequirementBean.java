package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.Requirement;
import com.example.ipwa02_07.services.RequirementService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class RequirementBean implements Serializable {

    @Inject
    private RequirementService requirementService;

    private Long id;
    private String titel;
    private String beschreibung;
    private Requirement.Prioritaet prioritaet;
    private Requirement.Status status;

    public void saveOrUpdateRequirement() {
        if (id == null) {
            // This is a new requirement
            Requirement newRequirement = new Requirement(titel, beschreibung, prioritaet, status);
            requirementService.createRequirement(newRequirement);
        } else {
            // This is an existing requirement that needs to be updated
            Requirement existingRequirement = requirementService.getRequirement(id);
            if (existingRequirement != null) {
                existingRequirement.setTitel(titel);
                existingRequirement.setBeschreibung(beschreibung);
                existingRequirement.setPrioritaet(prioritaet);
                existingRequirement.setStatus(status);
                requirementService.updateRequirement(existingRequirement);
            }
        }
        clearFields();
    }

    public List<Requirement> getAllRequirements() {
        return requirementService.getAllRequirements();
    }

    private void clearFields() {
        id = null;
        titel = "";
        beschreibung = "";
        prioritaet = null;
        status = null;
    }

    public void editRequirement(Requirement requirement) {
        this.id = requirement.getId();
        this.titel = requirement.getTitel();
        this.beschreibung = requirement.getBeschreibung();
        this.prioritaet = requirement.getPrioritaet();
        this.status = requirement.getStatus();
    }

    public void deleteRequirement(Requirement requirement) {
        requirementService.deleteRequirement(requirement.getId());
    }

    public List<SelectItem> getPriorityOptions() {
        return Arrays.stream(Requirement.Prioritaet.values())
                .map(p -> new SelectItem(p, p.name()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getStatusOptions() {
        return Arrays.stream(Requirement.Status.values())
                .map(s -> new SelectItem(s, s.name()))
                .collect(Collectors.toList());
    }

    // Getter and Setter
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

    public Requirement.Prioritaet getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(Requirement.Prioritaet prioritaet) {
        this.prioritaet = prioritaet;
    }

    public Requirement.Status getStatus() {
        return status;
    }

    public void setStatus(Requirement.Status status) {
        this.status = status;
    }
}
