package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "test_run_users")
public class TestRunUser implements Serializable {

    @EmbeddedId
    private TestRunUserId id = new TestRunUserId();

    @ManyToOne
    @MapsId("testRunId")
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, Getters, Setters
    public TestRunUser() {}

    public TestRunUser(TestRun testRun, User user) {
        this.testRun = testRun;
        this.user = user;
        this.id = new TestRunUserId(testRun.getId(), user.getId());
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestRunUser)) return false;
        TestRunUser that = (TestRunUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}