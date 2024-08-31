package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "testcases")
public class TestCase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(length = 1000)
    private String prerequisites;

    @Column(nullable = false, length = 1000)
    private String expectedResult;

    @ManyToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private Requirement requirement;

    // Constructors, Getters and Setters

    public TestCase() {}

    public TestCase(String title, String description, String prerequisites, String expectedResult, Requirement requirement) {
        this.title = title;
        this.description = description;
        this.prerequisites = prerequisites;
        this.expectedResult = expectedResult;
        this.requirement = requirement;
    }

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    // Getters and Setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }
}