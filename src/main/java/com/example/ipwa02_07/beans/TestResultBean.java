package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestResult;
import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.services.TestResultService;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.TestRunService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class TestResultBean implements Serializable {

    @Inject
    private TestResultService testResultService;

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestRunService testRunService;

    private Long id;
    private TestResult.Status status;
    private String result;
    private String comment;
    private LocalDateTime executionDate;
    private Long filteredTestCaseId;
    private Long selectedTestCaseId;
    private Long selectedTestRunId;
    private LazyDataModel<TestResult> lazyModel;

    public void setFilteredTestCaseId(Long testCaseId) {
        this.filteredTestCaseId = testCaseId;
    }

    public LazyDataModel<TestResult> getLazyModel() {
        if (lazyModel == null) {
            lazyModel = new LazyDataModel<TestResult>() {
                @Override
                public List<TestResult> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                    if (filteredTestCaseId != null) {
                        filterBy.put("testCase.id", FilterMeta.builder()
                                .field("testCase.id")
                                .filterValue(filteredTestCaseId)
                                .build());
                    }
                    return testResultService.getTestResults(first, pageSize, sortBy, filterBy);
                }

                @Override
                public int count(Map<String, FilterMeta> filterBy) {
                    return testResultService.countTestResults();
                }
            };
        }
        return lazyModel;
    }

    public void saveOrUpdateTestResult() {
        if (selectedTestCaseId == null || selectedTestRunId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select both a Test Case and a Test Run."));
            return;
        }

        TestCase testCase = testCaseService.getTestCase(selectedTestCaseId);
        TestRun testRun = testRunService.getTestRunById(selectedTestRunId);

        if (testCase == null || testRun == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selected Test Case or Test Run not found."));
            return;
        }

        if (status == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select a Status."));
            return;
        }

        try {
            TestResult testResult;
            if (id == null) {
                // This is a new test result
                testResult = new TestResult();
            } else {
                // This is an existing test result that needs to be updated
                testResult = testResultService.getTestResult(id);
                if (testResult == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Test Result not found for update."));
                    return;
                }
            }

            // Set or update the TestResult fields
            testResult.setStatus(status);
            testResult.setResult(result);
            testResult.setComment(comment);
            testResult.setExecutionDate(LocalDateTime.now());
            testResult.setTestCase(testCase);
            testResult.setTestRun(testRun);

            if (id == null) {
                // Create new TestResult
                testResultService.createTestResult(testResult);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Result created successfully."));
            } else {
                // Update existing TestResult
                testResultService.updateTestResult(testResult);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Result updated successfully."));
            }

            clearFields();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occurred while saving the Test Result: " + e.getMessage()));
        }
    }

    public List<TestResult> getAllTestResults() {
        return testResultService.getAllTestResults();
    }

    public void clearFields() {
        id = null;
        status = null;
        result = "";
        comment = "";
        executionDate = null;
        selectedTestCaseId = null;
        selectedTestRunId = null;
    }

    public void editTestResult(TestResult testResult) {
        this.id = testResult.getId();
        this.status = testResult.getStatus();
        this.result = testResult.getResult();
        this.comment = testResult.getComment();
        this.executionDate = testResult.getExecutionDate();
        this.selectedTestCaseId = testResult.getTestCase().getId();
        this.selectedTestRunId = testResult.getTestRun().getId();
    }

    public void deleteTestResult(TestResult testResult) {
        testResultService.deleteTestResult(testResult.getId());
    }

    public List<SelectItem> getStatusOptions() {
        return Arrays.stream(TestResult.Status.values())
                .map(s -> new SelectItem(s, s.name()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getTestCaseOptions() {
        return testCaseService.getAllTestCases().stream()
                .map(tc -> new SelectItem(tc.getId(), tc.getTitle()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getTestRunOptions() {
        return testRunService.getAllTestRuns().stream()
                .map(tr -> new SelectItem(tr.getId(), tr.getName()))
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestResult.Status getStatus() {
        return status;
    }

    public void setStatus(TestResult.Status status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public Long getSelectedTestCaseId() {
        return selectedTestCaseId;
    }

    public void setSelectedTestCaseId(Long selectedTestCaseId) {
        this.selectedTestCaseId = selectedTestCaseId;
    }

    public Long getSelectedTestRunId() {
        return selectedTestRunId;
    }

    public void setSelectedTestRunId(Long selectedTestRunId) {
        this.selectedTestRunId = selectedTestRunId;
    }
}