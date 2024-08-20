package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.services.RequirementService;
import com.example.ipwa02_07.services.TestCaseService;
import com.example.ipwa02_07.services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class DashboardBean {

    @Inject
    private RequirementService requirementService;

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private UserService userService;



    public int getTotalRequirements() {
        return requirementService.getAllRequirements().size();
    }

    public int getTotalTestCases() {
        return testCaseService.getAllTestCases().size();
    }


}