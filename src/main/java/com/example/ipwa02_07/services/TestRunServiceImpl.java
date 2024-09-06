package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestRunTestCase;
import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.entities.TestRunUser;
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

    @Override
    public TestRun getTestRunWithTestCasesAndUsers(Long id) {
        return entityManager.createQuery(
                        "SELECT DISTINCT tr FROM TestRun tr " +
                                "LEFT JOIN FETCH tr.testRunTestCases " +
                                "LEFT JOIN FETCH tr.testRunUsers " +
                                "WHERE tr.id = :id", TestRun.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    @Transactional
    public TestRun addUserToTestRun(Long testRunId, Long userId) {
        TestRun testRun = entityManager.find(TestRun.class, testRunId);
        if (testRun == null) {
            return null;
        }

        User user = entityManager.find(User.class, userId);
        if (user == null) {
            return testRun;
        }

        // Check if the association already exists
        boolean associationExists = testRun.getTestRunUsers().stream()
                .anyMatch(tru -> tru.getUser().getId().equals(userId));

        if (!associationExists) {
            TestRunUser testRunUser = new TestRunUser(testRun, user);
            testRun.getTestRunUsers().add(testRunUser);
            user.getTestRunUsers().add(testRunUser);
            entityManager.persist(testRunUser);
        }

        return testRun;
    }

    @Override
    @Transactional
    public TestRun removeUserFromTestRun(Long testRunId, Long userId) {
        TestRun testRun = entityManager.find(TestRun.class, testRunId);
        if (testRun == null) {
            return null;
        }

        TestRunUser testRunUserToRemove = testRun.getTestRunUsers().stream()
                .filter(tru -> tru.getUser().getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (testRunUserToRemove != null) {
            testRun.getTestRunUsers().remove(testRunUserToRemove);
            testRunUserToRemove.getUser().getTestRunUsers().remove(testRunUserToRemove);
            entityManager.remove(testRunUserToRemove);
        }

        return testRun;
    }

    @Override
    public List<User> getUsersForTestRun(Long testRunId) {
        return entityManager.createQuery(
                        "SELECT u FROM User u JOIN u.testRunUsers tru WHERE tru.testRun.id = :testRunId", User.class)
                .setParameter("testRunId", testRunId)
                .getResultList();
    }

}