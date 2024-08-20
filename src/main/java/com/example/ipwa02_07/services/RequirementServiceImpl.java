package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.Requirement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RequirementServiceImpl implements RequirementService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void createRequirement(Requirement requirement) {
        em.persist(requirement);
    }

    @Override
    public Requirement getRequirement(Long id) {
        return em.find(Requirement.class, id);
    }

    @Override
    public List<Requirement> getAllRequirements() {
        return em.createQuery("SELECT r FROM Requirement r", Requirement.class).getResultList();
    }

    @Transactional
    @Override
    public void updateRequirement(Requirement requirement) {
        em.merge(requirement);
    }

    @Transactional
    @Override
    public void deleteRequirement(Long id) {
        Requirement requirement = getRequirement(id);
        if (requirement != null) {
            em.remove(requirement);
        }
    }

    @Override
    public List<Requirement> getRequirementsByPriority(Requirement.Prioritaet priority) {
        return em.createQuery("SELECT r FROM Requirement r WHERE r.prioritaet = :priority", Requirement.class)
                .setParameter("priority", priority)
                .getResultList();
    }

    @Override
    public List<Requirement> getRequirements(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        StringBuilder queryBuilder = new StringBuilder("SELECT r FROM Requirement r WHERE 1=1");

        // Apply filters
        filterBy.forEach((field, filter) -> {
            queryBuilder.append(" AND r.").append(field).append(" LIKE :").append(field);
        });

        // Apply sorting
        if (!sortBy.isEmpty()) {
            queryBuilder.append(" ORDER BY ");
            queryBuilder.append(sortBy.values().stream()
                    .map(sortMeta -> "r." + sortMeta.getField() + " " + (sortMeta.getOrder() == SortOrder.ASCENDING ? "ASC" : "DESC"))
                    .collect(Collectors.joining(", ")));
        }

        TypedQuery<Requirement> query = em.createQuery(queryBuilder.toString(), Requirement.class);

        // Set filter parameters
        filterBy.forEach((field, filter) -> {
            query.setParameter(field, "%" + filter.getFilterValue() + "%");
        });

        // Apply pagination
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public int countRequirements() {
        return ((Long) em.createQuery("SELECT COUNT(r) FROM Requirement r").getSingleResult()).intValue();
    }
}