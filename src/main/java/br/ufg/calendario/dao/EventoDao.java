/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Regional;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author André Luiz Fernandes Ribeiro Barca (andrexterz@gmail.com)
 */
@Repository
@Scope(value = "singleton")
public class EventoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public boolean adicionar(Evento evento) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.clear();
            session.save(evento);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean adicionar(List<Evento> eventos) {
        Session session = sessionFactory.getCurrentSession();
        int counter = 0;
        try {
            session.clear();
            for (Evento evt : eventos) {
                session.save(evt);
                if (++counter % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean atualizar(Evento evento) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.clear();
            session.update(evento);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean excluir(Evento evento) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(evento);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean excluir(List<Evento> eventos) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(eventos);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean excluirTodos() {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createQuery("delete from Evento").executeUpdate();
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Evento buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Evento) session.get(Evento.class, id);
    }

    @Transactional(readOnly = true)
    public List<Evento> listar(int first, int pageSize, String termo) {
        Session session = sessionFactory.getCurrentSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        QueryBuilder queryBuilder = fullTextSession
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Evento.class).get();
        org.apache.lucene.search.Query searchQuery;
        String words[] = termo.split("\\s");
        if (words.length > 1) {
            searchQuery = queryBuilder.phrase()
                    .withSlop(5)
                    .onField("assunto").andField("descricao")
                    .sentence(termo)
                    .createQuery();
        } else {
            searchQuery = queryBuilder.keyword()
                    .fuzzy()
                    .withThreshold(0.7f)
                    .onField("assunto").andField("descricao")
                    .matching(termo)
                    .createQuery();
        }
        org.hibernate.Query query = fullTextSession.createFullTextQuery(searchQuery, Evento.class);
        query.setFirstResult(first).setMaxResults(pageSize);
        List result = query.list();
        System.out.println("Result size: " + result.size());
        return result;
    }

    @Transactional(readOnly = true)
    public List<Evento> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        return criteria.list();
    }

    @Transactional(readOnly = true)
    public List<Evento> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        criteria.setFirstResult(first);
        criteria.setMaxResults(pageSize);
        if ((sortField != null && !sortField.isEmpty()) && (sortOrder != null && !sortOrder.isEmpty())) {
            if (sortOrder.equals("ASCENDING")) {
                criteria.addOrder(Order.asc(sortField));
            }
            if (sortOrder.equals("DESCENDING")) {
                criteria.addOrder(Order.desc(sortField));
            }
        } else {
            criteria.addOrder(Order.asc("id"));
        }
        if (filters != null && !filters.isEmpty()) {
            for (String key : filters.keySet()) {
                if (key.equals("termo")) {
                    criteria.add(Restrictions.or(
                            Restrictions.like("assunto", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase(),
                            Restrictions.like("descricao", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase()));
                }

                if (key.equals("interessado")) {
                    Interessado interessado = (Interessado) filters.get(key);
                    System.out.println("interessado: " + interessado.getNome());
                    criteria.createCriteria("interessado")
                            .add(Restrictions.eq("id", interessado.getId()));
                }

                if (key.equals("regional")) {
                    Regional regional = (Regional) filters.get(key);
                    System.out.println("regional: " + regional.getNome());
                    criteria.createCriteria("regional")
                            .add(Restrictions.eq("id", regional.getId()));
                }

                if (key.equals("periodo")) {
                    Map periodo = (Map) filters.get(key);
                    criteria.add(Restrictions.and(Restrictions.ge("inicio", periodo.get("inicio")))
                            .add(Restrictions.le("termino", periodo.get("termino"))));

                }

                if (key.equals("calendario")) {
                    criteria.createAlias("calendario", "c");
                    criteria.add(Restrictions.eq("c.ano", ((Calendario) filters.get(key)).getAno()));
                }

            }
        }
        if (filters == null || !filters.containsKey("calendario")) {
            criteria.createAlias("calendario", "c");
            criteria.add(Restrictions.eq("c.ativo", true));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<Evento> resultado = criteria.list();
        for (Evento evt : resultado) {
            Hibernate.initialize(evt.getInteressado());
            Hibernate.initialize(evt.getRegional());
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public int rowCount() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    @Transactional(readOnly = true)
    public int rowCount(Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        for (String key : filters.keySet()) {
            if (key.equals("assunto")) {
                criteria.add(Restrictions.like(key, filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase());
            }
            if (key.equals("periodo")) {
                Map periodo = (Map) filters.get(key);
                criteria.add(Restrictions.and(Restrictions.ge("inicio", periodo.get("inicio")))
                        .add(Restrictions.le("termino", periodo.get("termino"))));
            }

            if (key.equals("calendario")) {
                criteria.createAlias("calendario", "c");
                criteria.add(Restrictions.eq("c.ano", ((Calendario) filters.get(key)).getAno()));
            }
            if (key.equals("termo")) {
                criteria.add(Restrictions.or(
                        Restrictions.like("assunto", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase(),
                        Restrictions.like("descricao", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase()));
            }
            if (key.equals("interessado")) {
                System.out.println("implementar filtro interessado");
            }
            if (key.equals("regional")) {
                System.out.println("implementar filtro regional");
            }
        }
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }
}
