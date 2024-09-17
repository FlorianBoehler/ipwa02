package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.Requirement;
import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.entities.User.UserRole;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.RequirementService;
import com.example.ipwa02_07.services.UserService;
import com.example.ipwa02_07.services.TestRunService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.model.SelectItem;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class TestCaseBean implements Serializable {

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private RequirementService requirementService;

    @Inject
    private UserService userService;

    @Inject
    private TestRunService testRunService;

    @Inject
    private LoginBean loginBean;

    @Inject
    private PageAccessBean pageAccessBean;

    @Inject
    private FacesContext facesContext;

    private Long id;
    private String title;
    private String description;
    private String prerequisites;
    private String expectedResult;
    private Long selectedRequirementId;
    private Long selectedUserId;
    private List<TestCase> allTestCases;
    private List<TestCase> filteredTestCases;
    private Long testRunId;

    @PostConstruct
    public void init() {
        loadAllTestCases();
        loadFilteredTestCases();
    }

    public void loadAllTestCases() {
        allTestCases = testCaseService.getAllTestCases();
    }

    public void loadFilteredTestCases() {
        if (pageAccessBean.canViewAllTestCases()) {
            filteredTestCases = testCaseService.getAllTestCases();
        } else {
            User currentUser = loginBean.getCurrentUser();
            if (currentUser != null) {
                filteredTestCases = testCaseService.getTestCasesByTester(currentUser);
            } else {
                filteredTestCases = new ArrayList<>();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No user logged in", null));
            }
        }
    }

    public void saveOrUpdateTestCase() {
        Requirement requirement = requirementService.getRequirement(selectedRequirementId);
        User assignedUser = selectedUserId != null ? userService.getUserById(selectedUserId) : null;

        if (id == null) {
            TestCase newTestCase = new TestCase(title, description, prerequisites, expectedResult, requirement);
            newTestCase.setAssignedUser(assignedUser);
            if (testRunId != null) {
                TestRun testRun = testRunService.getTestRunById(testRunId);
                newTestCase.setTestRun(testRun);
            }
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
                if (testRunId != null) {
                    TestRun testRun = testRunService.getTestRunById(testRunId);
                    existingTestCase.setTestRun(testRun);
                } else {
                    existingTestCase.setTestRun(null);
                }
                testCaseService.updateTestCase(existingTestCase);
            }
        }
        clearFields();
        loadAllTestCases();
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (testCaseService.hasRelatedTestResults(testCase.getId())) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot delete test case with related test result."));
            return;
        }
        testCaseService.deleteTestCase(testCase.getId());
        loadAllTestCases();
        facesContext.addMessage(null, new FacesMessage("Test case deleted successfully"));
    }

    public List<SelectItem> getRequirementOptions() {
        List<Requirement> requirements = requirementService.getAllRequirements();
        return requirements.stream()
                .map(r -> new SelectItem(r.getId(), r.getTitle()))
                .collect(Collectors.toList());
    }

    public String clearFields() {
        this.id = null;
        this.title = "";
        this.description = "";
        this.prerequisites = "";
        this.expectedResult = "";
        this.selectedRequirementId = null;
        this.selectedUserId = null;
        return "";
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

    public List<TestCase> getAllTestCases() {
        return allTestCases;
    }

    public List<TestCase> getFilteredTestCases() {
        return filteredTestCases;
    }

    public void setAllTestCases(List<TestCase> allTestCases) {
        this.allTestCases = allTestCases;
    }

    public void setFilteredTestCases(List<TestCase> filteredTestCases) {
        this.filteredTestCases = filteredTestCases;
    }


    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}