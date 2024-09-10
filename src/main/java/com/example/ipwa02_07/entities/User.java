package com.example.ipwa02_07.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> assignedTestCases = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestRunUser> testRunUsers = new HashSet<>();

    // Constructors
    public User() {}

    public User(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, UserRole role, boolean active) {
        this(username, password, email, role);
        this.active = active;
    }

    // Copy constructor
    public User(User other) {
        this.id = other.id;
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.role = other.role;
        this.active = other.active;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return active == user.active &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, role, active);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }

    public Set<TestRunUser> getTestRunUsers() {
        return testRunUsers;
    }

    public void setTestRunUsers(Set<TestRunUser> testRunUsers) {
        this.testRunUsers = testRunUsers;
    }

    // Helper methods for managing the bidirectional relationship with TestRunUser
    public void addTestRunUser(TestRun testRun) {
        TestRunUser testRunUser = new TestRunUser(testRun, this);
        testRunUsers.add(testRunUser);
        testRun.getTestRunUsers().add(testRunUser);
    }

    public void removeTestRunUser(TestRun testRun) {
        TestRunUser testRunUser = testRunUsers.stream()
                .filter(tru -> tru.getTestRun().equals(testRun) && tru.getUser().equals(this))
                .findFirst()
                .orElse(null);
        if (testRunUser != null) {
            testRunUsers.remove(testRunUser);
            testRun.getTestRunUsers().remove(testRunUser);
            testRunUser.setTestRun(null);
            testRunUser.setUser(null);
        }
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<TestCase> getAssignedTestCases() {
        return assignedTestCases;
    }

    public void setAssignedTestCases(List<TestCase> assignedTestCases) {
        this.assignedTestCases = assignedTestCases;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Helper methods for managing the bidirectional relationship
    public void addTestCase(TestCase testCase) {
        assignedTestCases.add(testCase);
        testCase.setAssignedUser(this);
    }

    public void removeTestCase(TestCase testCase) {
        assignedTestCases.remove(testCase);
        testCase.setAssignedUser(null);
    }

    // Enum for User Roles
    public enum UserRole {
        REQUIREMENTS_ENGINEER,
        TEST_MANAGER,
        TEST_CREATOR,
        TESTER,
        ADMIN
    }


}