package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ApplicationScoped
public class TestCaseServiceImpl implements TestCaseService {
    @PersistenceContext
    private EntityManager em;
    private long lastUsedNumber = 0;

    @PostConstruct
    public void initializeLastUsedNumber() {
        int currentYear = LocalDateTime.now().getYear();
        TypedQuery<Long> query = em.createQuery(
                "SELECT MAX(CAST(SUBSTRING(t.customId, 10) AS long)) FROM TestCase t WHERE t.customId LIKE CONCAT('TCA-', :year, '-%')",
                Long.class
        );
        query.setParameter("year", currentYear);
        Long highestNumber = query.getSingleResult();
        lastUsedNumber = highestNumber != null ? highestNumber : 0L;
    }

    @Override
    @Transactional
    public void createTestCase(TestCase testCase) {
        if (testCase.getCustomId() == null) {
            testCase.setCustomId(generateCustomId());
        }
        em.persist(testCase);
    }

    private synchronized String generateCustomId() {
        int year = LocalDateTime.now().getYear();
        lastUsedNumber++;
        return String.format("TCA-%d-%05d", year, lastUsedNumber);
    }

    @Override
    public TestCase getTestCase(Long id) {
        return em.find(TestCase.class, id);
    }

    @Override
    public List<TestCase> getAllTestCases() {
        return em.createQuery("SELECT t FROM TestCase t", TestCase.class).getResultList();
    }

    @Override
    public List<TestCase> getTestCasesByTester(User tester) {
        return em.createQuery("SELECT tc FROM TestCase tc WHERE tc.assignedUser = :tester", TestCase.class)
                .setParameter("tester", tester)
                .getResultList();
    }

    @Override
    @Transactional
    public void updateTestCase(TestCase testCase) {
        em.merge(testCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(Long id) {
        TestCase testCase = getTestCase(id);
        if (testCase != null) {
            if (testCase.getTestRun() != null) {
                testCase.getTestRun().getTestCases().remove(testCase);
                testCase.setTestRun(null);
            }
            em.remove(testCase);
        }
    }

    @Override
    public List<TestCase> getTestCasesByTestRun(Long testRunId) {
        return em.createQuery("SELECT tc FROM TestCase tc WHERE tc.testRun.id = :testRunId", TestCase.class)
                .setParameter("testRunId", testRunId)
                .getResultList();
    }

    @Override
    public boolean hasRelatedTestResults(Long testCaseId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(tr) FROM TestResult tr WHERE tr.testCase.id = :testCaseId", Long.class);
        query.setParameter("testCaseId", testCaseId);
        return query.getSingleResult() > 0;
    }

    @Override
    public List<TestCase> getAllTestCasesNotAssignedToTestRun() {
        return em.createQuery("SELECT tc FROM TestCase tc WHERE tc.testRun IS NULL", TestCase.class)
                .getResultList();
    }


}