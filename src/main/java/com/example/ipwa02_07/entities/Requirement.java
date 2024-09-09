package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requirements")
public class Requirement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "custom_id", unique = true, nullable = false)
    private String customId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creationDate", nullable = false)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases = new ArrayList<>();

    // Constructors
    public Requirement() {
    }

    public Requirement(String title, String description, Priority priority, Status status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.creationDate = LocalDateTime.now();
    }

    // Getters and Setters
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    // Helper methods for managing relationships
    public void addTestCase(TestCase testCase) {
        testCases.add(testCase);
        testCase.setRequirement(this);
    }

    public void removeTestCase(TestCase testCase) {
        testCases.remove(testCase);
        testCase.setRequirement(null);
    }

    // Enums for Priority and Status
    public enum Priority {
        MUST, SHOULD, COULD
    }

    public enum Status {
        NEW, IN_PROGRESS, COMPLETED
    }


    // Optional: toString method for easier debugging
    @Override
    public String toString() {
        return "Requirement{" +
                "id=" + id +
                ", customId='" + customId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", creationDate=" + creationDate +
                '}';
    }
}