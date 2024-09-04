package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "test_run_user")
public class TestRunUser implements Serializable {

    @EmbeddedId
    private TestRunTestCaseId id = new TestRunTestCaseId();

    @ManyToOne
    @MapsId("testRunId")
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id")
    private TestCase testCase;

    // Constructors, Getters, Setters
    public TestRunUser() {}

    public TestRunUser(TestRun testRun, User user) {
        this.testRun = testRun;
        this.user = user;
        this.id = new TestRunUser(testRun.getId(), user.getId());
    }

    // Getters and setters
    public TestRunUserId getId() {
        return id;
    }

    public void setId(TestRunUserId id) {
        this.id = id;
    }

    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestRunTestCase)) return false;
        TestRunTestCase that = (TestRunTestCase) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}