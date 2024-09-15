package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.Requirement;
import com.example.ipwa02_07.entities.TestCase;
import com.example.ipwa02_07.entities.TestRun;
import com.example.ipwa02_07.services.RequirementService;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.TestRunService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class StatisticsBean implements Serializable {

    @Inject
    private RequirementService requirementService;

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestRunService testRunService;

    public long getTotalRequirements() {
        return requirementService.countRequirements(null);
    }

    public Map<Requirement.Status, Long> getRequirementsByStatus() {
        return requirementService.getAllRequirements().stream()
                .collect(Collectors.groupingBy(Requirement::getStatus, Collectors.counting()));
    }

    public Map<Requirement.Priority, Long> getRequirementsByPriority() {
        return requirementService.getAllRequirements().stream()
                .collect(Collectors.groupingBy(Requirement::getPriority, Collectors.counting()));
    }

    public long getTotalTestCases() {
        return testCaseService.getAllTestCases().size();
    }


    public double getAverageTestCasesPerRequirement() {
        long totalRequirements = getTotalRequirements();
        long totalTestCases = getTotalTestCases();
        return totalRequirements == 0 ? 0 : (double) totalTestCases / totalRequirements;
    }

    public long getTotalTestRuns() {
        return testRunService.getAllTestRuns().size();
    }

    public Map<TestRun.TestRunStatus, Long> getTestRunsByStatus() {
        return testRunService.getAllTestRuns().stream()
                .collect(Collectors.groupingBy(TestRun::getStatus, Collectors.counting()));
    }

    public double getAverageTestCasesPerTestRun() {
        List<TestRun> testRuns = testRunService.getAllTestRuns();
        if (testRuns.isEmpty()) {
            return 0;
        }
        long totalTestCases = testRuns.stream()
                .mapToLong(tr -> tr.getTestCases().size())
                .sum();
        return (double) totalTestCases / testRuns.size();
    }
}