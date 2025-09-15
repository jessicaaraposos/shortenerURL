package br.com.jessicaraposo.shortener.infra;

import br.com.jessicaraposo.shortener.entity.UrlMapping;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Stateless
public class UrlMappingRepository {

    @PersistenceContext(unitName = "shortenerPU")
    private EntityManager em;

    public Optional<UrlMapping> findByCodeOrAlias(String token) {
        TypedQuery<UrlMapping> q = em.createQuery(
                "select u from UrlMapping u where u.code = :t or u.alias = :t",
                UrlMapping.class);
        q.setParameter("t", token);
        List<UrlMapping> list = q.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Optional<UrlMapping> findByAlias(String alias) {
        TypedQuery<UrlMapping> q = em.createQuery(
                "select u from UrlMapping u where u.alias = :a", UrlMapping.class);
        q.setParameter("a", alias);
        List<UrlMapping> list = q.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public UrlMapping save(UrlMapping e) {
        if (e.getId() == null) em.persist(e);
        else e = em.merge(e);
        return e;
    }

    public void incrementHits(UrlMapping e) {
        e.setHits(e.getHits() + 1);
        em.merge(e);
    }
}
