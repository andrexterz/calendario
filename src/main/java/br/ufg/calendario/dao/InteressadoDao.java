/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Interessado;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
public class InteressadoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public boolean adicionar(Interessado interessado) {
        Session session = this.sessionFactory.getCurrentSession();
        try {
            session.save(interessado);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean atualizar(Interessado interessado) {
        Session session = this.sessionFactory.getCurrentSession();
        try {
            session.update(interessado);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean excluir(Interessado interessado) {
        Session session = this.sessionFactory.getCurrentSession();
        try {
            session.delete(interessado);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public Interessado buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Interessado) session.get(Interessado.class, id);
    }    

    @Transactional(readOnly = true)
    public List<Interessado> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Interessado.class);
        return criteria.list();
    }
    
    @Transactional(readOnly = true)
    public List<Interessado> listar(String termo) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Interessado.class);
        criteria.add(Restrictions.or(
                Restrictions.like("sigla", termo, MatchMode.ANYWHERE).ignoreCase(),
                Restrictions.like("nome", termo, MatchMode.ANYWHERE).ignoreCase()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
    
    @Transactional(readOnly = true)
    public List<Interessado> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Interessado.class);
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
}
