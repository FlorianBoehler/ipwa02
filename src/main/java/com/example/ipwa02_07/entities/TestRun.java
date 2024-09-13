package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

@Entity
@Table(name = "test_runs")
public class TestRun implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "custom_id", unique = true, nullable = false)
    private String customId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestRunStatus status;

    @OneToMany(mappedBy = "testRun", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<TestRunTestCase> testRunTestCases = new HashSet<>();


    public TestRun() {}

    // Enum for Test Run Status
    public enum TestRunStatus {
        PLANNED,
        IN_PROGRESS,
        COMPLETED
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TestRunStatus getStatus() {
        return status;
    }

    public void setStatus(TestRunStatus status) {
        this.status = status;
    }

    public Set<TestRunTestCase> getTestRunTestCases() {
        return testRunTestCases;
    }

    public void setTestRunTestCases(Set<TestRunTestCase> testRunTestCases) {
        this.testRunTestCases = testRunTestCases;
    }

    public void addTestRunTestCase(TestCase testCase) {
        TestRunTestCase testRunTestCase = new TestRunTestCase(this, testCase);
        if (!testRunTestCases.contains(testRunTestCase)) {
            testRunTestCases.add(testRunTestCase);
            testCase.getTestRunTestCases().add(testRunTestCase);
        }
    }

    public void removeTestRunTestCase(TestCase testCase) {
        for (Iterator<TestRunTestCase> iterator = testRunTestCases.iterator(); iterator.hasNext(); ) {
            TestRunTestCase testRunTestCase = iterator.next();
            if (testRunTestCase.getTestRun().equals(this) &&
                    testRunTestCase.getTestCase().equals(testCase)) {
                iterator.remove();
                testCase.getTestRunTestCases().remove(testRunTestCase);
                testRunTestCase.setTestRun(null);
                testRunTestCase.setTestCase(null);
            }
        }
    }
}