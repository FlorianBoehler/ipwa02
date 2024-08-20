package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.Requirement;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public interface RequirementService {
    List<Requirement> getRequirements(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);
    int countRequirements();
    void createRequirement(Requirement requirement);
    Requirement getRequirement(Long id);
    List<Requirement> getAllRequirements();
    void updateRequirement(Requirement requirement);
    void deleteRequirement(Long id);
    List<Requirement> getRequirementsByPriority(Requirement.Prioritaet priority);
}