package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.services.TestRunService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class TestRunBean implements Serializable {

    @Inject
    private TestRunService testRunService;

    private TestRun testRun = new TestRun();
    private List<TestRun> testRuns;

    @PostConstruct
    public void init() {
        loadTestRuns();
    }

    public void loadTestRuns() {
        testRuns = testRunService.getAllTestRuns();
    }

    public String saveTestRun() {
        if (testRun.getId() == null) {
            testRunService.createTestRun(testRun);
        } else {
            testRunService.updateTestRun(testRun);
        }
        clearForm();
        loadTestRuns();
        return null;
    }

    public void editTestRun(TestRun testRun) {
        this.testRun = testRun;
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

    // Getter and Setter for testRun
    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    // Getter and Setter for testRuns
    public List<TestRun> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRun> testRuns) {
        this.testRuns = testRuns;
    }
}