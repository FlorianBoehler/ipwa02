package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface TestCaseService {
    void createTestCase(TestCase testCase);
    TestCase getTestCase(Long id);
    List<TestCase> getAllTestCases();
    void updateTestCase(TestCase testCase);
    void deleteTestCase(Long id);
    List<TestCase> getAllTestCasesSorted(Map<String, SortMeta> sortBy);
}