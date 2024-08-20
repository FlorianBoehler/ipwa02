package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.Requirement;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.RequirementService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class TestCaseBean implements Serializable {

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private RequirementService requirementService;

    private Long id;
    private String titel;
    private String beschreibung;
    private String voraussetzungen;
    private String erwartetesResultat;
    private Long selectedRequirementId;

    public void addTestCase() {
        Requirement requirement = requirementService.getRequirement(selectedRequirementId);
        TestCase newTestCase = new TestCase(titel, beschreibung, voraussetzungen, erwartetesResultat, requirement);
        testCaseService.createTestCase(newTestCase);
        clearFields();
    }

    public List<TestCase> getAllTestCases() {
        return testCaseService.getAllTestCases();
    }

    public void editTestCase(TestCase testCase) {
        this.id = testCase.getId();
        this.titel = testCase.getTitel();
        this.beschreibung = testCase.getBeschreibung();
        this.voraussetzungen = testCase.getVoraussetzungen();
        this.erwartetesResultat = testCase.getErwartetesResultat();
        this.selectedRequirementId = testCase.getAnforderung().getId();
    }

    public void deleteTestCase(TestCase testCase) {
        testCaseService.deleteTestCase(testCase.getId());
    }

    public List<SelectItem> getRequirementOptions() {
        List<Requirement> requirements = requirementService.getAllRequirements();
        return requirements.stream()
                .map(r -> new SelectItem(r.getId(), r.getTitel()))
                .collect(Collectors.toList());
    }

    private void clearFields() {
        id = null;
        titel = "";
        beschreibung = "";
        voraussetzungen = "";
        erwartetesResultat = "";
        selectedRequirementId = null;
    }

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

    public Long getSelectedRequirementId() {
        return selectedRequirementId;
    }

    public void setSelectedRequirementId(Long selectedRequirementId) {
        this.selectedRequirementId = selectedRequirementId;
    }
}