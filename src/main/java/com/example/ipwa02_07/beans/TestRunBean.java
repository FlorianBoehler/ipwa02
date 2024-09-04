package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestRun.TestRunStatus;
import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.services.TestRunService;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.entities.TestRunTestCase;
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

    public String saveTestRun() {
        if (testRun.getId() == null) {
            // This is a new TestRun
            testRun = testRunService.saveTestRun(testRun);
            // Now add all selected TestCases
            for (TestCase testCase : selectedTestCases) {
                testRunService.addTestCaseToTestRun(testRun.getId(), testCase.getId());
            }
            selectedTestCases.clear(); // Clear the list after saving
        } else {
            // This is an existing TestRun, use previous logic
            testRun = testRunService.saveTestRun(testRun);
        }
        clearForm();
        loadTestRuns();
        return null;
    }

    public void editTestRun(TestRun testRun) {
        this.testRun = testRunService.getTestRunWithTestCases(testRun.getId());
    }

    public void deleteTestRun(TestRun testRun) {
        testRunService.deleteTestRun(testRun.getId());
        loadTestRuns();
    }

    public void clearForm() {
        testRun = new TestRun();
    }

    public List<TestRun.TestRunStatus> getStatusOptions() {
        return List.of(TestRun.TestRunStatus.values());
    }



    // Methods to handle TestCases in TestRun
    public void addTestCaseToTestRun(TestCase testCase) {
        if (testRun.getId() == null) {
            // For new TestRuns, we'll just keep track of selected TestCases
            if (!selectedTestCases.contains(testCase)) {
                selectedTestCases.add(testCase);
            }
        } else {
            // For existing TestRuns, use the previous logic
            TestRun updatedTestRun = testRunService.addTestCaseToTestRun(testRun.getId(), testCase.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case added to Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add Test Case to Test Run"));
            }
        }
    }

    public void removeTestCaseFromTestRun(Object testCaseOrTestRunTestCase) {
        TestCase testCase;
        if (testCaseOrTestRunTestCase instanceof TestCase) {
            testCase = (TestCase) testCaseOrTestRunTestCase;
        } else if (testCaseOrTestRunTestCase instanceof TestRunTestCase) {
            testCase = ((TestRunTestCase) testCaseOrTestRunTestCase).getTestCase();
        } else {
            // Handle error case
            return;
        }

        if (testRun.getId() == null) {
            // For new TestRuns, just remove from our list of selected TestCases
            selectedTestCases.remove(testCase);
        } else {
            // For existing TestRuns, use the previous logic
            TestRun updatedTestRun = testRunService.removeTestCaseFromTestRun(testRun.getId(), testCase.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case removed from Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to remove Test Case from Test Run"));
            }
        }
    }

    private TestRun selectedTestRun;


    public TestRun getSelectedTestRun() {
        return selectedTestRun;
    }

    public void setSelectedTestRun(TestRun selectedTestRun) {
        this.selectedTestRun = selectedTestRun;
    }

    // Getters and Setters for testRun, testRuns, availableTestCases
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


}