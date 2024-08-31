package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestRunTestCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TestRunServiceImpl implements TestRunService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TestRun saveTestRun(TestRun testRun) {
        if (testRun.getId() == null) {
            entityManager.persist(testRun);
        } else {
            testRun = entityManager.merge(testRun);
        }
        return testRun;
    }

    @Override
    @Transactional
    public void deleteTestRun(Long id) {
        TestRun testRun = getTestRunById(id);
        if (testRun != null) {
            entityManager.remove(testRun);
        }
    }

    @Override
    public TestRun getTestRunById(Long id) {
        return entityManager.find(TestRun.class, id);
    }

    @Override
    public TestRun getTestRunWithTestCases(Long id) {
        return entityManager.createQuery("SELECT tr FROM TestRun tr LEFT JOIN FETCH tr.testRunTestCases WHERE tr.id = :id", TestRun.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<TestRun> getAllTestRuns() {
        return entityManager.createQuery("SELECT t FROM TestRun t", TestRun.class).getResultList();
    }

    @Override
    @Transactional
    public TestRun addTestCaseToTestRun(Long testRunId, Long testCaseId) {
        TestRun testRun = entityManager.find(TestRun.class, testRunId);
        if (testRun == null) {
            return null;
        }

        TestCase testCase = entityManager.find(TestCase.class, testCaseId);
        if (testCase == null) {
            return testRun;
        }

        // Check if the association already exists
        boolean associationExists = testRun.getTestRunTestCases().stream()
                .anyMatch(trtc -> trtc.getTestCase().getId().equals(testCaseId));

        if (!associationExists) {
            TestRunTestCase testRunTestCase = new TestRunTestCase(testRun, testCase);
            testRun.getTestRunTestCases().add(testRunTestCase);
            testCase.getTestRunTestCases().add(testRunTestCase);
            entityManager.persist(testRunTestCase);
        }

        return testRun;
    }

    @Override
    @Transactional
    public TestRun removeTestCaseFromTestRun(Long testRunId, Long testCaseId) {
        TestRun testRun = entityManager.find(TestRun.class, testRunId);
        if (testRun == null) {
            return null;
        }

        TestRunTestCase testRunTestCaseToRemove = testRun.getTestRunTestCases().stream()
                .filter(trtc -> trtc.getTestCase().getId().equals(testCaseId))
                .findFirst()
                .orElse(null);

        if (testRunTestCaseToRemove != null) {
            testRun.getTestRunTestCases().remove(testRunTestCaseToRemove);
            testRunTestCaseToRemove.getTestCase().getTestRunTestCases().remove(testRunTestCaseToRemove);
            entityManager.remove(testRunTestCaseToRemove);
        }

        return testRun;
    }
}