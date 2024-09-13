package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestCase;
import java.util.List;

public interface TestRunService {
    TestRun saveTestRun(TestRun testRun);
    void deleteTestRun(Long id);
    TestRun getTestRunById(Long id);
    TestRun getTestRunWithTestCases(Long id);
    List<TestRun> getAllTestRuns();

    TestRun addTestCaseToTestRun(Long testRunId, Long testCaseId);
    TestRun removeTestCaseFromTestRun(Long testRunId, Long testCaseId);
}