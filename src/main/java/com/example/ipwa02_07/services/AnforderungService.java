package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.Anforderung;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AnforderungService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createAnforderung(Anforderung anforderung) {
        em.persist(anforderung);
    }

    public Anforderung getAnforderung(Long id) {
        return em.find(Anforderung.class, id);
    }

    public List<Anforderung> getAlleAnforderungen() {
        return em.createQuery("SELECT a FROM Anforderung a", Anforderung.class).getResultList();
    }

    @Transactional
    public void updateAnforderung(Anforderung anforderung) {
        em.merge(anforderung);
    }

    @Transactional
    public void deleteAnforderung(Long id) {
        Anforderung anforderung = getAnforderung(id);
        if (anforderung != null) {
            em.remove(anforderung);
        }
    }

    public List<Anforderung> getAnforderungenByPrioritaet(Anforderung.Prioritaet prioritaet) {
        return em.createQuery("SELECT a FROM Anforderung a WHERE a.prioritaet = :prioritaet", Anforderung.class)
                .setParameter("prioritaet", prioritaet)
                .getResultList();
    }
}