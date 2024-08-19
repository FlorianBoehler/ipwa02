package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_cases")
public class TestCase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column
    private String prerequisites;

    @Column(nullable = false)
    private String expectedResult;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_tester_id")
    private User assignedTester;

    // @ManyToOne
    // @JoinColumn(name = "requirement_id")
    // private Requirement requirement;

    @Column
    private LocalDateTime executionDate;

    // Constructors, getters, and setters

    public TestCase() {}

    // Enum for Test Status
    public enum TestStatus {
        NOT_STARTED,
        IN_PROGRESS,
        PASSED,
        FAILED
    }

    // Getters and setters (omitted for brevity)
}