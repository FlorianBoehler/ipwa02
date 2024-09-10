package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
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
    @Transactional
    public void updateTestCase(TestCase testCase) {
        em.merge(testCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(Long id) {
        TestCase testCase = getTestCase(id);
        if (testCase != null) {
            em.remove(testCase);
        }
    }


}