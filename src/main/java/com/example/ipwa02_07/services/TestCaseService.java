package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import java.util.List;

public interface TestCaseService {
    void createTestCase(TestCase testCase);
    TestCase getTestCase(Long id);
    List<TestCase> getAllTestCases();
    void updateTestCase(TestCase testCase);
    void deleteTestCase(Long id);
    List<TestCase> getTestCasesByRequirement(Long requirementId);
}