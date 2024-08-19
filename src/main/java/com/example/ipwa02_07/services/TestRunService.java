package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestRun;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

public interface TestRunService {
    TestRun createTestRun(TestRun testRun);
    TestRun updateTestRun(TestRun testRun);
    void deleteTestRun(Long id);
    TestRun getTestRunById(Long id);
    List<TestRun> getAllTestRuns();
}

