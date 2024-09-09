package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.Requirement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.MatchMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RequirementServiceImpl implements RequirementService {

    @PersistenceContext
    private EntityManager em;

    private long lastUsedNumber = 0;

    @PostConstruct
    public void initializeLastUsedNumber() {
        int currentYear = LocalDateTime.now().getYear();
        TypedQuery<Long> query = em.createQuery(
                "SELECT MAX(CAST(SUBSTRING(r.customId, 10) AS long)) FROM Requirement r WHERE r.customId LIKE CONCAT('REQ-', :year, '-%')",
                Long.class
        );
        query.setParameter("year", currentYear);
        Long highestNumber = query.getSingleResult();
        lastUsedNumber = highestNumber != null ? highestNumber : 0L;
    }

    @Transactional
    @Override
    public void createRequirement(Requirement requirement) {
        requirement.setCustomId(generateCustomId());
        em.persist(requirement);
    }

    private synchronized String generateCustomId() {
        int year = LocalDateTime.now().getYear();
        lastUsedNumber++;
        return String.format("REQ-%d-%05d", year, lastUsedNumber);
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
        if (requirement.getCustomId() == null) {
            requirement.setCustomId(generateCustomId());
        }
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
    public List<Requirement> getRequirementsByPriority(Requirement.Priority priority) {
        return em.createQuery("SELECT r FROM Requirement r WHERE r.priority = :priority", Requirement.class)
                .setParameter("priority", priority)
                .getResultList();
    }

    @Override
    public List<Requirement> getRequirements(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Requirement> cq = cb.createQuery(Requirement.class);
        Root<Requirement> root = cq.from(Requirement.class);

        cq.select(root);

        // Apply filters
        if (filterBy != null && !filterBy.isEmpty()) {
            List<Predicate> predicates = buildPredicates(cb, root, filterBy);
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }
        }

        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            List<Order> orders = buildOrders(cb, root, sortBy);
            cq.orderBy(orders);
        }

        TypedQuery<Requirement> query = em.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int countRequirements(Map<String, FilterMeta> filterBy) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Requirement> root = cq.from(Requirement.class);

        cq.select(cb.count(root));

        // Apply filters
        if (filterBy != null && !filterBy.isEmpty()) {
            List<Predicate> predicates = buildPredicates(cb, root, filterBy);
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }
        }

        return em.createQuery(cq).getSingleResult().intValue();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Requirement> root, Map<String, FilterMeta> filterBy) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
            String field = entry.getKey();
            FilterMeta filter = entry.getValue();
            if (filter.getFilterValue() != null) {
                if (filter.getMatchMode() == MatchMode.CONTAINS) {
                    predicates.add(cb.like(cb.lower(root.get(field)), "%" + filter.getFilterValue().toString().toLowerCase() + "%"));
                } else if (filter.getMatchMode() == MatchMode.EQUALS) {
                    predicates.add(cb.equal(root.get(field), filter.getFilterValue()));
                }
                // Add more conditions for other match modes as needed
            }
        }

        return predicates;
    }

    private List<Order> buildOrders(CriteriaBuilder cb, Root<Requirement> root, Map<String, SortMeta> sortBy) {
        return sortBy.entrySet().stream()
                .map(e -> e.getValue().getOrder() == SortOrder.ASCENDING
                        ? cb.asc(root.get(e.getKey()))
                        : cb.desc(root.get(e.getKey())))
                .collect(Collectors.toList());
    }
}