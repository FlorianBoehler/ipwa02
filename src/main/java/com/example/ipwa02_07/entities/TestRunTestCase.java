package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "test_run_test_cases")
public class TestRunTestCase implements Serializable {

    @EmbeddedId
    private TestRunTestCaseId id = new TestRunTestCaseId();

    @ManyToOne
    @MapsId("testRunId")
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @ManyToOne
    @MapsId("testCaseId")
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;

    // Constructors, Getters, Setters
    public TestRunTestCase() {}

    public TestRunTestCase(TestRun testRun, TestCase testCase) {
        this.testRun = testRun;
        this.testCase = testCase;
        this.id = new TestRunTestCaseId(testRun.getId(), testCase.getId());
    }

    // Getters and setters
    public TestRunTestCaseId getId() {
        return id;
    }

    public void setId(TestRunTestCaseId id) {
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