package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
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
            for (TestCase testCase : testRun.getTestCases()) {
                testCase.setTestRun(null);
            }
            em.remove(testRun);
        }
    }

    @Override
    public TestRun getTestRunById(Long id) {
        return em.find(TestRun.class, id);
    }

    @Override
    public TestRun getTestRunWithTestCases(Long id) {
        return em.createQuery("SELECT tr FROM TestRun tr LEFT JOIN FETCH tr.testCases WHERE tr.id = :id", TestRun.class)
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
        TestCase testCase = em.find(TestCase.class, testCaseId);

        if (testRun != null && testCase != null) {
            testCase.setTestRun(testRun);
            testRun.getTestCases().add(testCase);
            em.merge(testRun);
        }

        return testRun;
    }

    @Override
    @Transactional
    public TestRun removeTestCaseFromTestRun(Long testRunId, Long testCaseId) {
        TestRun testRun = em.find(TestRun.class, testRunId);
        TestCase testCase = em.find(TestCase.class, testCaseId);

        if (testRun != null && testCase != null) {
            testRun.getTestCases().remove(testCase);
            testCase.setTestRun(null);

            em.merge(testRun);
            em.merge(testCase);
            em.flush();

            return testRun;
        }
        return null;
    }

    @Override
    public boolean hasRelatedTestCases(Long testRunId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(tc) FROM TestCase tc WHERE tc.testRun.id = :testRunId", Long.class);
        query.setParameter("testRunId", testRunId);
        return query.getSingleResult() > 0;
    }

}