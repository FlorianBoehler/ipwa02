package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestRun;
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
    public TestRun createTestRun(TestRun testRun) {
        entityManager.persist(testRun);
        return testRun;
    }

    @Override
    @Transactional
    public TestRun updateTestRun(TestRun testRun) {
        return entityManager.merge(testRun);
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
    public List<TestRun> getAllTestRuns() {
        return entityManager.createQuery("SELECT t FROM TestRun t", TestRun.class).getResultList();
    }
}