package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.services.TestRunService;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.UserService;
import com.example.ipwa02_07.entities.TestRunTestCase;
import com.example.ipwa02_07.entities.TestRunUser;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Named
@SessionScoped
public class TestRunBean implements Serializable {

    @Inject
    private TestRunService testRunService;

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private UserService userService;

    private TestRun testRun = new TestRun();
    private List<TestRun> testRuns;
    private List<TestCase> availableTestCases;
    private List<User> availableUsers;
    private List<TestCase> selectedTestCases = new ArrayList<>();
    private List<User> selectedUsers = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadTestRuns();
        loadAvailableTestCases();
        loadAvailableUsers();
    }

    public void loadTestRuns() {
        testRuns = testRunService.getAllTestRuns();
    }

    public void loadAvailableTestCases() {
        availableTestCases = testCaseService.getAllTestCases();
    }

    public void loadAvailableUsers() {
        availableUsers = userService.getAllUsers();
    }

    public void refreshTestCases() {
        loadAvailableTestCases();
    }

    public void refreshUsers() {
        loadAvailableUsers();
    }

    public String saveTestRun() {
        if (testRun.getId() == null) {
            // This is a new TestRun
            testRun = testRunService.saveTestRun(testRun);
            // Now add all selected TestCases and Users
            for (TestCase testCase : selectedTestCases) {
                testRunService.addTestCaseToTestRun(testRun.getId(), testCase.getId());
            }
            for (User user : selectedUsers) {
                testRunService.addUserToTestRun(testRun.getId(), user.getId());
            }
            selectedTestCases.clear();
            selectedUsers.clear();
        } else {
            // This is an existing TestRun
            testRun = testRunService.saveTestRun(testRun);
        }
        clearForm();
        loadTestRuns();
        return null;
    }

    public void editTestRun(TestRun testRun) {
        this.testRun = testRunService.getTestRunWithTestCasesAndUsers(testRun.getId());
    }

    public void deleteTestRun(TestRun testRun) {
        testRunService.deleteTestRun(testRun.getId());
        loadTestRuns();
    }

    public void clearForm() {
        testRun = new TestRun();
        selectedTestCases.clear();
        selectedUsers.clear();
    }

    public List<TestRun.TestRunStatus> getStatusOptions() {
        return List.of(TestRun.TestRunStatus.values());
    }

    // Methods to handle TestCases in TestRun
    public void addTestCaseToTestRun(TestCase testCase) {
        if (testRun.getId() == null) {
            if (!selectedTestCases.contains(testCase)) {
                selectedTestCases.add(testCase);
            }
        } else {
            TestRun updatedTestRun = testRunService.addTestCaseToTestRun(testRun.getId(), testCase.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case added to Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add Test Case to Test Run"));
            }
        }
    }

    public void removeTestCaseFromTestRun(Object testCaseOrTestRunTestCase) {
        TestCase testCase;
        if (testCaseOrTestRunTestCase instanceof TestCase) {
            testCase = (TestCase) testCaseOrTestRunTestCase;
        } else if (testCaseOrTestRunTestCase instanceof TestRunTestCase) {
            testCase = ((TestRunTestCase) testCaseOrTestRunTestCase).getTestCase();
        } else {
            return;
        }

        if (testRun.getId() == null) {
            selectedTestCases.remove(testCase);
        } else {
            TestRun updatedTestRun = testRunService.removeTestCaseFromTestRun(testRun.getId(), testCase.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case removed from Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to remove Test Case from Test Run"));
            }
        }
    }

    // Methods to handle Users in TestRun
    public void addUserToTestRun(User user) {
        if (testRun.getId() == null) {
            if (!selectedUsers.contains(user)) {
                selectedUsers.add(user);
            }
        } else {
            TestRun updatedTestRun = testRunService.addUserToTestRun(testRun.getId(), user.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User added to Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add User to Test Run"));
            }
        }
    }

    public void removeUserFromTestRun(Object userOrTestRunUser) {
        User user;
        if (userOrTestRunUser instanceof User) {
            user = (User) userOrTestRunUser;
        } else if (userOrTestRunUser instanceof TestRunUser) {
            user = ((TestRunUser) userOrTestRunUser).getUser();
        } else {
            return;
        }

        if (testRun.getId() == null) {
            selectedUsers.remove(user);
        } else {
            TestRun updatedTestRun = testRunService.removeUserFromTestRun(testRun.getId(), user.getId());
            if (updatedTestRun != null) {
                this.testRun = updatedTestRun;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User removed from Test Run"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to remove User from Test Run"));
            }
        }
    }

    private TestRun selectedTestRun;

    // Getters and Setters
    public TestRun getSelectedTestRun() {
        return selectedTestRun;
    }

    public void setSelectedTestRun(TestRun selectedTestRun) {
        this.selectedTestRun = selectedTestRun;
    }

    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    public List<TestRun> getTestRuns() {
        return testRuns;
    }

    public void setTestRuns(List<TestRun> testRuns) {
        this.testRuns = testRuns;
    }

    public List<TestCase> getAvailableTestCases() {
        return availableTestCases;
    }

    public void setAvailableTestCases(List<TestCase> availableTestCases) {
        this.availableTestCases = availableTestCases;
    }

    public List<User> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(List<User> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public List<TestCase> getSelectedTestCases() {
        return selectedTestCases;
    }

    public void setSelectedTestCases(List<TestCase> selectedTestCases) {
        this.selectedTestCases = selectedTestCases;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
}