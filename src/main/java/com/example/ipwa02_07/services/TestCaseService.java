package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.User;
import java.util.List;

public interface TestCaseService {
    void createTestCase(TestCase testCase);
    TestCase getTestCase(Long id);
    List<TestCase> getAllTestCases();
    List<TestCase> getTestCasesByTester(User tester);
    void updateTestCase(TestCase testCase);
    void deleteTestCase(Long id);
    List<TestCase> getTestCasesByTestRun(Long testRunId);
    boolean hasRelatedTestResults(Long testCaseId);
    List<TestCase> getAllTestCasesNotAssignedToTestRun();
}