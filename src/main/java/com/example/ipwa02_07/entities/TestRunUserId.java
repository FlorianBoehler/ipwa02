package com.example.ipwa02_07.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class TestRunUserId implements Serializable {

    @Column(name = "test_run_id")
    private Long testRunId;

    @Column(name = "user_id")
    private Long userId;

    // Constructors
    public TestRunUserId() {}

    public TestRunUserId(Long testRunId, Long userId) {
        this.testRunId = testRunId;
        this.userId = userId;
    }

    // Getters and setters
    public Long getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRunUserId that = (TestRunUserId) o;
        return Objects.equals(testRunId, that.testRunId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testRunId, userId);
    }
}