package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.Requirement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class RequirementService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createRequirement(Requirement requirement) {
        em.persist(requirement);
    }

    public Requirement getRequirement(Long id) {
        return em.find(Requirement.class, id);
    }

    public List<Requirement> getAllRequirements() {
        return em.createQuery("SELECT r FROM Requirement r", Requirement.class).getResultList();
    }

    @Transactional
    public void updateRequirement(Requirement requirement) {
        em.merge(requirement);
    }

    @Transactional
    public void deleteRequirement(Long id) {
        Requirement requirement = getRequirement(id);
        if (requirement != null) {
            em.remove(requirement);
        }
    }

    public List<Requirement> getRequirementsByPriority(Requirement.Prioritaet priority) {
        return em.createQuery("SELECT r FROM Requirement r WHERE r.prioritaet = :priority", Requirement.class)
                .setParameter("priority", priority)
                .getResultList();
    }
}