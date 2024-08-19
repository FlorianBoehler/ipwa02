package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.services.TestCaseService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class TestCaseBean implements Serializable {

    @Inject
    private TestCaseService testCaseService;

    private TestCase testCase = new TestCase();
    private List<TestCase> testCases;

    public String createTestCase() {
        testCaseService.createTestCase(testCase);
        testCase = new TestCase();
        return "testCaseList?faces-redirect=true";
    }

    public void loadTestCases() {
        testCases = testCaseService.getAllTestCases();
    }

    // Getters and setters for testCase and testCases
    // Other methods for update, delete, etc.
}