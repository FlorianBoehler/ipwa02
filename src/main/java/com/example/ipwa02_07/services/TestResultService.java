package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestResult;
import com.example.ipwa02_07.entities.TestCase;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface TestResultService {
    List<TestResult> getTestResults(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);
    int countTestResults();
    void createTestResult(TestResult testResult);
    TestResult getTestResult(Long id);
    List<TestResult> getAllTestResults();
    void updateTestResult(TestResult testResult);
    void deleteTestResult(Long id);
    List<TestResult> getTestResultsByStatus(TestResult.Status status);
    TestResult getTestResultForTestCase(TestCase testCase);
    boolean hasTestResultForTestCase(TestCase testCase);
}