package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.services.TestCaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TestCaseServiceImpl implements TestCaseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TestCase createTestCase(TestCase testCase) {
        entityManager.persist(testCase);
        return testCase;
    }

    @Override
    @Transactional
    public TestCase updateTestCase(TestCase testCase) {
        return entityManager.merge(testCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(Long id) {
        TestCase testCase = getTestCaseById(id);
        if (testCase != null) {
            entityManager.remove(testCase);
        }
    }

    @Override
    public TestCase getTestCaseById(Long id) {
        return entityManager.find(TestCase.class, id);
    }

    @Override
    public List<TestCase> getAllTestCases() {
        return entityManager.createQuery("SELECT t FROM TestCase t", TestCase.class).getResultList();
    }

    @Override
    public List<TestCase> getTestCasesByRequirement(Long requirementId) {
        return entityManager.createQuery("SELECT t FROM TestCase t WHERE t.requirement.id = :reqId", TestCase.class)
                .setParameter("reqId", requirementId)
                .getResultList();
    }
}