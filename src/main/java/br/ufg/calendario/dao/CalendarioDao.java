/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Calendario;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author André
 */
@Repository
@Scope(value = "singleton")
public class CalendarioDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Transactional
    public boolean adicionar(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(calendario);
            if (calendario.isAtivo() == true) {
                disableOthers(session, calendario);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        } 
    }

    @Transactional
    public boolean atualizar(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(calendario);
            if (calendario.isAtivo() == true) {
                disableOthers(session, calendario);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean excluir(Calendario calendario) {
        Session session = this.sessionFactory.getCurrentSession();
        try {
            session.delete(calendario);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Calendario buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Calendario) session.get(Calendario.class, id);
    }

    @Transactional(readOnly = true)
    public Calendario buscar(Integer ano) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Calendario.class);
        criteria.add(Restrictions.eq("ano", ano));
        Calendario calendario = (Calendario) criteria.uniqueResult();
        return calendario;
     }

    @Transactional(readOnly = true)
    public List<Calendario> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Calendario.class);
        return criteria.list();
    }

    @Transactional(readOnly = true)
    public List<Calendario> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Calendario.class);
        criteria.setFirstResult(first);
        criteria.setMaxResults(pageSize);
        if ((sortField != null && !sortField.isEmpty()) && (sortOrder != null && !sortOrder.isEmpty())) {
            if (sortOrder.equals("ASCENDING")) {
                criteria.addOrder(Order.asc(sortField));
            }
            if (sortOrder.equals("DESCENDING")) {
                criteria.addOrder(Order.desc(sortField));
            }
        }
        if (filters != null && !filters.isEmpty()) {
            for (Entry<String, Object> filter: filters.entrySet()) {
                criteria.add(Restrictions.eq(filter.getKey(), filter.getValue()));
            }
        }
        return criteria.list();
    }
    
    @Transactional(readOnly = true)
    public int rowCount() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Calendario.class);
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    private void disableOthers(Session session, Calendario calendario) {
        Criteria criteria = session.createCriteria(Calendario.class);
        List<Calendario> calendarioList = criteria.list();
        int counter = 0;
        for (Calendario c : calendarioList) {
            if (!Objects.equals(c.getId(), calendario.getId())) {
                c.setAtivo(false);
                session.save(c);
            }
            if (++counter % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
    }
}
