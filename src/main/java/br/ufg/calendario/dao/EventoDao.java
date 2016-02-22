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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
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
 * @author Andre Luiz Fernandes Ribeiro Barca Luiz Fernandes Ribeiro Barca
 * (andrexterz@gmail.com)
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
    public boolean excluir(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.createQuery("delete from Evento e where e.calendario.id = :id")
                    .setLong("id", calendario.getId()).executeUpdate();
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
    public Date buscarPrimeiroDia(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        criteria.createAlias("calendario", "c");
        criteria.add(Restrictions.eq("c.ano", calendario.getAno()));
        criteria.setProjection(Projections.min("inicio"));
        Date data = (Date) criteria.uniqueResult();
        return data;
    }

    @Transactional(readOnly = true)
    public Date buscarUltimoDia(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        criteria.createAlias("calendario", "c");
        criteria.add(Restrictions.eq("c.ano", calendario.getAno()));
        criteria.setProjection(Projections.max("termino"));
        Date data = (Date) criteria.uniqueResult();
        return data;
    }

    @Transactional(readOnly = true)
    public List<Evento> listar(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Evento.class);
        criteria.createAlias("calendario", "c");
        criteria.add(Restrictions.eq("c.ano", calendario.getAno()));
        criteria.addOrder(Order.asc("inicio"));
        return criteria.list();
    }

    @Transactional(readOnly = true)
    public List<Evento> listar(Calendario calendario, Date data) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery("select e from Evento e where e.calendario.id = :id and :data >= e.inicio and :data <= e.termino order by e.inicio asc");
            query.setLong("id", calendario.getId());
            query.setDate("data", data);
            return query.list();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return null;
        }
    }

    private List<Long> buscarTermo(Session session, String termo) {
        String[] words = termo.split("\\s");
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        QueryBuilder queryBuilder = fullTextSession
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Evento.class).get();
        org.apache.lucene.search.Query searchQuery;
        if (words.length > 1) {
            searchQuery = queryBuilder.phrase()
                    .withSlop(5)
                    .onField("assunto").andField("descricao")
                    .sentence(termo).createQuery();
        } else {
            searchQuery = queryBuilder.keyword()
                    .fuzzy()
                    .withThreshold(0.7f)
                    .onFields("assunto", "descricao")
                    .matching(termo)
                    .createQuery();
        }
        org.hibernate.search.FullTextQuery query = fullTextSession.createFullTextQuery(searchQuery, Evento.class);
        query.setProjection("id");
        List<Long> result = new ArrayList<>();
        for (Object obj : query.list()) {
            result.add((Long) ((Object[]) obj)[0]);
        }
        return result;
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
            criteria.addOrder(Order.asc("inicio"));
        }
        if (filters != null && !filters.isEmpty()) {
            for (String key : filters.keySet()) {
                if (key.equals("termo")) {
                    List foundList = buscarTermo(session, filters.get("termo").toString());
                    if (foundList.size() > 0) {
                        criteria.add(Restrictions.in("id", foundList));
                    } else {
                        criteria.add(Restrictions.or(
                                Restrictions.like("assunto", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase(),
                                Restrictions.like("descricao", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase()));
                    }
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
                    criteria.add(
                            Restrictions.or(Restrictions.between("inicio", periodo.get("inicio"), periodo.get("termino")))
                            .add(Restrictions.between("termino", periodo.get("inicio"), periodo.get("termino")))
                    );
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
    public List<String> listarAssunto(Calendario calendario) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("select distinct e.assunto from Evento e where e.calendario.ativo=:calendarioAtivo order by e.assunto asc")
                .setBoolean("calendarioAtivo", calendario.isAtivo())
                .list();
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
