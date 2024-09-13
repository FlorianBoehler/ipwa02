package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestRunTestCase;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

import java.time.LocalDateTime;

@ApplicationScoped
public class TestRunServiceImpl implements TestRunService {

    @PersistenceContext
    private EntityManager em;

    private long lastUsedNumber = 0;

    @PostConstruct
    public void initializeLastUsedNumber() {
        int currentYear = LocalDateTime.now().getYear();
        TypedQuery<Long> query = em.createQuery(
                "SELECT MAX(CAST(SUBSTRING(t.customId, 10) AS long)) FROM TestRun t WHERE t.customId LIKE CONCAT('TRN-', :year, '-%')",
                Long.class
        );
        query.setParameter("year", currentYear);
        Long highestNumber = query.getSingleResult();
        lastUsedNumber = highestNumber != null ? highestNumber : 0L;
    }

    private synchronized String generateCustomId() {
        int year = LocalDateTime.now().getYear();
        lastUsedNumber++;
        return String.format("TRN-%d-%05d", year, lastUsedNumber);
    }

    @Override
    @Transactional
    public TestRun saveTestRun(TestRun testRun) {
        if (testRun.getId() == null) {
            if (testRun.getCustomId() == null) {
                testRun.setCustomId(generateCustomId());
            }
            em.persist(testRun);
        } else {
            testRun = em.merge(testRun);
        }
        return testRun;
    }

    @Override
    @Transactional
    public void deleteTestRun(Long id) {
        TestRun testRun = getTestRunById(id);
        if (testRun != null) {
            em.remove(testRun);
        }
    }

    @Override
    public TestRun getTestRunById(Long id) {
        return em.find(TestRun.class, id);
    }

    @Override
    public TestRun getTestRunWithTestCases(Long id) {
        return em.createQuery("SELECT tr FROM TestRun tr LEFT JOIN FETCH tr.testRunTestCases WHERE tr.id = :id", TestRun.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<TestRun> getAllTestRuns() {
        return em.createQuery("SELECT t FROM TestRun t", TestRun.class).getResultList();
    }

    @Override
    @Transactional
    public TestRun addTestCaseToTestRun(Long testRunId, Long testCaseId) {
        TestRun testRun = em.find(TestRun.class, testRunId);
        if (testRun == null) {
            return null;
        }

        TestCase testCase = em.find(TestCase.class, testCaseId);
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
            em.persist(testRunTestCase);
        }

        return testRun;
    }

    @Override
    @Transactional
    public TestRun removeTestCaseFromTestRun(Long testRunId, Long testCaseId) {
        TestRun testRun = em.find(TestRun.class, testRunId);
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
            em.remove(testRunTestCaseToRemove);
        }

        return testRun;
    }
}