package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestResult;
import com.example.ipwa02_07.entities.TestCase;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class TestResultServiceImpl implements TestResultService {

    @PersistenceContext
    private EntityManager em;

    private long lastUsedNumber = 0;

    @PostConstruct
    public void initializeLastUsedNumber() {
        int currentYear = LocalDateTime.now().getYear();
        TypedQuery<Long> query = em.createQuery(
                "SELECT MAX(CAST(SUBSTRING(tr.customId, 10) AS long)) FROM TestResult tr WHERE tr.customId LIKE CONCAT('TRS-', :year, '-%')",
                Long.class
        );
        query.setParameter("year", currentYear);
        Long highestNumber = query.getSingleResult();
        lastUsedNumber = highestNumber != null ? highestNumber : 0L;
    }

    private synchronized String generateCustomId() {
        int year = LocalDateTime.now().getYear();
        lastUsedNumber++;
        return String.format("TRS-%d-%05d", year, lastUsedNumber);
    }

    @Transactional
    @Override
    public void createTestResult(TestResult testResult) {
        if (testResult.getCustomId() == null) {
            testResult.setCustomId(generateCustomId());
        }
        em.persist(testResult);
    }

    @Override
    public TestResult getTestResult(Long id) {
        return em.find(TestResult.class, id);
    }

    @Override
    public List<TestResult> getAllTestResults() {
        return em.createQuery("SELECT tr FROM TestResult tr", TestResult.class).getResultList();
    }

    @Transactional
    @Override
    public void updateTestResult(TestResult testResult) {
        em.merge(testResult);
    }

    @Transactional
    @Override
    public void deleteTestResult(Long id) {
        TestResult testResult = getTestResult(id);
        if (testResult != null) {
            em.remove(testResult);
        }
    }

    @Override
    public List<TestResult> getTestResults(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        StringBuilder queryBuilder = new StringBuilder("SELECT tr FROM TestResult tr WHERE 1=1");

        // Apply filters
        filterBy.forEach((field, filter) -> {
            queryBuilder.append(" AND tr.").append(field).append(" LIKE :").append(field);
        });

        // Apply sorting
        if (!sortBy.isEmpty()) {
            queryBuilder.append(" ORDER BY ");
            queryBuilder.append(sortBy.values().stream()
                    .map(sortMeta -> "tr." + sortMeta.getField() + " " + (sortMeta.getOrder() == SortOrder.ASCENDING ? "ASC" : "DESC"))
                    .collect(Collectors.joining(", ")));
        }

        TypedQuery<TestResult> query = em.createQuery(queryBuilder.toString(), TestResult.class);

        // Set filter parameters
        filterBy.forEach((field, filter) -> {
            query.setParameter(field, "%" + filter.getFilterValue() + "%");
        });

        // Apply pagination
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public int countTestResults() {
        return ((Long) em.createQuery("SELECT COUNT(tr) FROM TestResult tr").getSingleResult()).intValue();
    }

    @Override
    public TestResult getTestResultForTestCase(TestCase testCase) {
        TypedQuery<TestResult> query = em.createQuery(
                "SELECT tr FROM TestResult tr WHERE tr.testCase = :testCase ORDER BY tr.executionDate DESC",
                TestResult.class
        );
        query.setParameter("testCase", testCase);
        query.setMaxResults(1);
        List<TestResult> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public boolean hasTestResultForTestCase(TestCase testCase) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(tr) FROM TestResult tr WHERE tr.testCase = :testCase",
                Long.class
        );
        query.setParameter("testCase", testCase);
        return query.getSingleResult() > 0;
    }
}