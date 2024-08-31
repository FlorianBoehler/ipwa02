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
    private String title;
    private String description;
    private String prerequisites;
    private String expectedResult;
    private Long selectedRequirementId;

    public void addTestCase() {
        Requirement requirement = requirementService.getRequirement(selectedRequirementId);
        TestCase newTestCase = new TestCase(title, description, prerequisites, expectedResult, requirement);
        testCaseService.createTestCase(newTestCase);
        clearFields();
    }

    public List<TestCase> getAllTestCases() {
        return testCaseService.getAllTestCases();
    }

    public void editTestCase(TestCase testCase) {
        this.id = testCase.getId();
        this.title = testCase.getTitle();
        this.description = testCase.getDescription();
        this.prerequisites = testCase.getPrerequisites();
        this.expectedResult = testCase.getExpectedResult();
        this.selectedRequirementId = testCase.getRequirement().getId();
    }

    public void deleteTestCase(TestCase testCase) {
        testCaseService.deleteTestCase(testCase.getId());
    }

    public List<SelectItem> getRequirementOptions() {
        List<Requirement> requirements = requirementService.getAllRequirements();
        return requirements.stream()
                .map(r -> new SelectItem(r.getId(), r.getTitle()))
                .collect(Collectors.toList());
    }

    private void clearFields() {
        id = null;
        title = "";
        description = "";
        prerequisites = "";
        expectedResult = "";
        selectedRequirementId = null;
    }

    // Getters and Setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public Long getSelectedRequirementId() {
        return selectedRequirementId;
    }

    public void setSelectedRequirementId(Long selectedRequirementId) {
        this.selectedRequirementId = selectedRequirementId;
    }
}