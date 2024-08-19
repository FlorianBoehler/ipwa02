package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import java.util.List;

public interface TestCaseService {
    TestCase createTestCase(TestCase testCase);
    TestCase updateTestCase(TestCase testCase);
    void deleteTestCase(Long id);
    TestCase getTestCaseById(Long id);
    List<TestCase> getAllTestCases();
    List<TestCase> getTestCasesByRequirement(Long requirementId);
}