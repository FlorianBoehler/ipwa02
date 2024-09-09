package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestRunTestCase;
import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.entities.TestRunUser;
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

    @Override
    public TestRun getTestRunWithTestCasesAndUsers(Long id) {
        return em.createQuery(
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
        TestRun testRun = em.find(TestRun.class, testRunId);
        if (testRun == null) {
            return null;
        }

        User user = em.find(User.class, userId);
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
            em.persist(testRunUser);
        }

        return testRun;
    }

    @Override
    @Transactional
    public TestRun removeUserFromTestRun(Long testRunId, Long userId) {
        TestRun testRun = em.find(TestRun.class, testRunId);
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
            em.remove(testRunUserToRemove);
        }

        return testRun;
    }

    @Override
    public List<User> getUsersForTestRun(Long testRunId) {
        return em.createQuery(
                        "SELECT u FROM User u JOIN u.testRunUsers tru WHERE tru.testRun.id = :testRunId", User.class)
                .setParameter("testRunId", testRunId)
                .getResultList();
    }

}