package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.services.TestRunService;
import com.example.ipwa02_07.services.TestCaseService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Named
@SessionScoped
public class TestRunBean implements Serializable {

    @Inject
    private TestRunService testRunService;

    @Inject
    private TestCaseService testCaseService;

    private TestRun testRun = new TestRun();
    private List<TestRun> testRuns;
    private List<TestCase> availableTestCases;
    private List<TestCase> selectedTestCases = new ArrayList<>();
    private List<TestCase> testCases;

    @PostConstruct
    public void init() {
        loadTestRuns();
        loadAvailableTestCases();
    }

    public void loadTestRuns() {
        testRuns = testRunService.getAllTestRuns();
    }

    public void loadAvailableTestCases() {
        availableTestCases = testCaseService.getAllTestCases();
    }

    public void refreshTestCases() {
        loadAvailableTestCases();
    }

    public void saveTestRun() {
        if (testRun.getId() == null) {
            // This is a new TestRun
            testRun = testRunService.saveTestRun(testRun);
            for (TestCase testCase : selectedTestCases) {
                testRunService.addTestCaseToTestRun(testRun.getId(), testCase.getId());
            }
        } else {
            // This is an existing TestRun
            testRun = testRunService.saveTestRun(testRun);
        }
        clearForm();
        loadTestRuns();
    }


    public void editTestRun(TestRun testRun) {
        this.testRun = testRunService.getTestRunWithTestCases(testRun.getId());
    }


    public void deleteTestRun(TestRun testRun) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (testRunService.hasRelatedTestCases(testRun.getId())) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot delete test run with related test cases."));
            return;
        }
        testRunService.deleteTestRun(testRun.getId());
        loadTestRuns();
        facesContext.addMessage(null, new FacesMessage("Test run deleted successfully"));
    }

    public boolean isTestCaseAlreadyAdded(TestCase testCase) {
        if (testRun.getId() == null) {
            // This is a new TestRun, check the selectedTestCases list
            return selectedTestCases.stream()
                    .anyMatch(tc -> tc.getId().equals(testCase.getId()));
        } else {
            // This is an existing TestRun, check the testCases list
            return testRun.getTestCases().stream()
                    .anyMatch(tc -> tc.getId().equals(testCase.getId()));
        }
    }


    public void clearForm() {
        testRun = new TestRun();
        selectedTestCases.clear();
    }

    public List<TestRun.TestRunStatus> getStatusOptions() {
        return List.of(TestRun.TestRunStatus.values());
    }

    // Methods to handle TestCases in TestRun
    public void addTestCaseToTestRun(TestCase testCase) {
        if (testRun.getId() == null) {
            if (!selectedTestCases.contains(testCase)) {
                selectedTestCases.add(testCase);
            }
        } else {
            TestRun updatedTestRun = testRunService.addTestCaseToTestRun(testRun.getId(), testCase.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case added to Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add Test Case to Test Run"));
            }
        }
    }

    public void removeTestCaseFromTestRun(TestCase testCase) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (testRun.getId() == null) {
            selectedTestCases.remove(testCase);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case removed from selection"));
        } else {
            if (testCaseService.hasRelatedTestResults(testCase.getId())) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot remove Test Case with related Test Results"));
                return;
            }

            TestRun updatedTestRun = testRunService.removeTestCaseFromTestRun(testRun.getId(), testCase.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case removed from Test Run"));
            } else {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to remove Test Case from Test Run"));
            }
        }
    }

    private TestRun selectedTestRun;

    // Getters and Setters
    public TestRun getSelectedTestRun() {
        return selectedTestRun;
    }

    public void setSelectedTestRun(TestRun selectedTestRun) {
        this.selectedTestRun = selectedTestRun;
    }

    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    public List<TestRun> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRun> testRuns) {
        this.testRuns = testRuns;
    }

    public List<TestCase> getAvailableTestCases() {
        return availableTestCases;
    }

    public void setAvailableTestCases(List<TestCase> availableTestCases) {
        this.availableTestCases = availableTestCases;
    }

    public List<TestCase> getSelectedTestCases() {
        return selectedTestCases;
    }

    public void setSelectedTestCases(List<TestCase> selectedTestCases) {
        this.selectedTestCases = selectedTestCases;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
}