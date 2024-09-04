package com.example.ipwa02_07.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class TestRunTestCaseId implements Serializable {

    @Column(name = "test_run_id")
    private Long testRunId;

    @Column(name = "test_case_id")
    private Long testCaseId;

    // Constructors
    public TestRunTestCaseId() {}

    public TestRunTestCaseId(Long testRunId, Long testCaseId) {
        this.testRunId = testRunId;
        this.testCaseId = testCaseId;
    }

    // Getters and setters
    public Long getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRunTestCaseId that = (TestRunTestCaseId) o;
        return Objects.equals(testRunId, that.testRunId) &&
                Objects.equals(testCaseId, that.testCaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testRunId, testCaseId);
    }
}