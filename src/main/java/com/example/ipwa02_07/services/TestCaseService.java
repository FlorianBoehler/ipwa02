package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TestCaseService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createTestCase(TestCase testCase) {
        em.persist(testCase);
    }

    public TestCase getTestCase(Long id) {
        return em.find(TestCase.class, id);
    }

    public List<TestCase> getAllTestCases() {
        return em.createQuery("SELECT t FROM TestCase t", TestCase.class).getResultList();
    }

    @Transactional
    public void updateTestCase(TestCase testCase) {
        em.merge(testCase);
    }

    @Transactional
    public void deleteTestCase(Long id) {
        TestCase testCase = getTestCase(id);
        if (testCase != null) {
            em.remove(testCase);
        }
    }

    public List<TestCase> getTestCasesByRequirement(Long requirementId) {
        return em.createQuery("SELECT t FROM TestCase t WHERE t.anforderung.id = :requirementId", TestCase.class)
                .setParameter("requirementId", requirementId)
                .getResultList();
    }
}