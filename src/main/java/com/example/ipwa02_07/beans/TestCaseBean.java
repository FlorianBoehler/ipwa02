package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.Requirement;
import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.entities.User.UserRole;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.RequirementService;
import com.example.ipwa02_07.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.model.SelectItem;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class TestCaseBean implements Serializable {

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private RequirementService requirementService;

    @Inject
    private UserService userService;

    private Long id;
    private String title;
    private String description;
    private String prerequisites;
    private String expectedResult;
    private Long selectedRequirementId;
    private Long selectedUserId;
    private List<TestCase> testCases;

    @PostConstruct
    public void init() {
        loadTestCases();
    }

    public void loadTestCases() {
        testCases = testCaseService.getAllTestCases();
    }

    public void saveOrUpdateTestCase() {
        Requirement requirement = requirementService.getRequirement(selectedRequirementId);
        User assignedUser = selectedUserId != null ? userService.getUserById(selectedUserId) : null;

        if (id == null) {
            TestCase newTestCase = new TestCase(title, description, prerequisites, expectedResult, requirement);
            newTestCase.setAssignedUser(assignedUser);
            testCaseService.createTestCase(newTestCase);
        } else {
            TestCase existingTestCase = testCaseService.getTestCase(id);
            if (existingTestCase != null) {
                existingTestCase.setTitle(title);
                existingTestCase.setDescription(description);
                existingTestCase.setPrerequisites(prerequisites);
                existingTestCase.setExpectedResult(expectedResult);
                existingTestCase.setRequirement(requirement);
                existingTestCase.setAssignedUser(assignedUser);
                testCaseService.updateTestCase(existingTestCase);
            }
        }
        clearFields();
        loadTestCases();
    }

    public List<TestCase> getAllTestCases() {
        return testCases;
    }

    public void editTestCase(TestCase testCase) {
        this.id = testCase.getId();
        this.title = testCase.getTitle();
        this.description = testCase.getDescription();
        this.prerequisites = testCase.getPrerequisites();
        this.expectedResult = testCase.getExpectedResult();
        this.selectedRequirementId = testCase.getRequirement().getId();
        this.selectedUserId = testCase.getAssignedUser() != null ? testCase.getAssignedUser().getId() : null;
    }

    public void deleteTestCase(TestCase testCase) {
        testCaseService.deleteTestCase(testCase.getId());
        loadTestCases();
    }

    public List<SelectItem> getRequirementOptions() {
        List<Requirement> requirements = requirementService.getAllRequirements();
        return requirements.stream()
                .map(r -> new SelectItem(r.getId(), r.getTitle()))
                .collect(Collectors.toList());
    }

    public void clearFields() {
        id = null;
        title = "";
        description = "";
        prerequisites = "";
        expectedResult = "";
        selectedRequirementId = null;
        selectedUserId = null;
    }

    public List<SelectItem> getUserOptions() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .filter(user -> user.getRole() == UserRole.TESTER)
                .map(user -> new SelectItem(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }

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

    public Long getSelectedRequirementId() {
        return selectedRequirementId;
    }

    public void setSelectedRequirementId(Long selectedRequirementId) {
        this.selectedRequirementId = selectedRequirementId;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}