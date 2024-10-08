package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.Requirement;
import com.example.ipwa02_07.services.RequirementService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.model.SelectItem;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.LazyDataModel;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class RequirementBean implements Serializable {

    @Inject
    private RequirementService requirementService;

    private Long id;
    private String title;
    private String description;
    private Requirement.Priority priority;
    private Requirement.Status status;
    private LazyDataModel<Requirement> lazyModel;


    public LazyDataModel<Requirement> getLazyModel() {
        if (lazyModel == null) {
            lazyModel = new LazyDataModel<Requirement>() {
                @Override
                public List<Requirement> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                    return requirementService.getRequirements(first, pageSize, sortBy, filterBy);
                }

                @Override
                public int count(Map<String, FilterMeta> filterBy) {
                    return requirementService.countRequirements(filterBy);
                }
            };
        }
        return lazyModel;
    }

   public void clearFields() {
        id = null;
        title = "";
        description = "";
        priority = null;
        status = null;
    }

    public void saveOrUpdateRequirement() {
        if (id == null) {
            // This is a new requirement
            Requirement newRequirement = new Requirement(title, description, priority, status);
            requirementService.createRequirement(newRequirement);
        } else {
            // This is an existing requirement that needs to be updated
            Requirement existingRequirement = requirementService.getRequirement(id);
            if (existingRequirement != null) {
                existingRequirement.setTitle(title);
                existingRequirement.setDescription(description);
                existingRequirement.setPriority(priority);
                existingRequirement.setStatus(status);
                requirementService.updateRequirement(existingRequirement);
            }
        }
        clearFields();
    }

    public void editRequirement(Requirement requirement) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        this.id = requirement.getId();
        this.title = requirement.getTitle();
        this.description = requirement.getDescription();
        this.priority = requirement.getPriority();
        this.status = requirement.getStatus();
        facesContext.validationFailed();
    }

    public void deleteRequirement(Requirement requirement) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (requirementService.hasRelatedTestCases(requirement.getId())) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot delete requirement with related test cases."));
            return;
        }
        requirementService.deleteRequirement(requirement.getId());
        facesContext.addMessage(null, new FacesMessage("Requirement deleted successfully"));
    }

    public List<SelectItem> getPriorityOptions() {
        return Arrays.stream(Requirement.Priority.values())
                .map(p -> new SelectItem(p, p.name()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getStatusOptions() {
        return Arrays.stream(Requirement.Status.values())
                .map(s -> new SelectItem(s, s.name()))
                .collect(Collectors.toList());
    }

    // Getter and Setter methods
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

    public Requirement.Priority getPriority() {
        return priority;
    }

    public void setPriority(Requirement.Priority priority) {
        this.priority = priority;
    }

    public Requirement.Status getStatus() {
        return status;
    }

    public void setStatus(Requirement.Status status) {
        this.status = status;
    }

}