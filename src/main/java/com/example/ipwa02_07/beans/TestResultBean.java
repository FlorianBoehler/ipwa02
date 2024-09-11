package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.TestResult;
import com.example.ipwa02_07.entities.TestCase;

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

    private TestCase currentTestCase;
    private TestResult currentTestResult;
    private Long id;
    private TestResult.Status status;
    private String result;
    private String comment;
    private LocalDateTime executionDate;
    private Long filteredTestCaseId;
    private Long selectedTestCaseId;
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
        if (currentTestResult == null) {
            currentTestResult = new TestResult();
        }
        currentTestResult.setTestCase(currentTestCase);
        currentTestResult.setStatus(status);
        currentTestResult.setResult(result);
        currentTestResult.setComment(comment);

        if (currentTestResult.getId() == null) {
            testResultService.createTestResult(currentTestResult);
        } else {
            testResultService.updateTestResult(currentTestResult);
        }

        clearFields();
        // You might want to refresh the testCaseTable here
    }

    public List<TestResult> getAllTestResults() {
        return testResultService.getAllTestResults();
    }

    public void clearFields() {
        status = null;
        result = "";
        comment = "";
    }

    public void editTestResult(TestCase testCase) {
        this.currentTestCase = testCase;
        this.currentTestResult = testResultService.getTestResultForTestCase(testCase);
        if (this.currentTestResult != null) {
            this.status = currentTestResult.getStatus();
            this.result = currentTestResult.getResult();
            this.comment = currentTestResult.getComment();
        } else {
            // Handle the case where there's no TestResult for this TestCase
            // This shouldn't happen if the UI is correctly showing edit/create buttons
            clearFields();
        }
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

    public void prepareNewTestResult(TestCase testCase) {
        this.currentTestCase = testCase;
        this.currentTestResult = new TestResult();
        this.currentTestResult.setTestCase(testCase);
        clearFields();
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

    public boolean hasTestResult(TestCase testCase) {
        return testResultService.hasTestResultForTestCase(testCase);
    }

}