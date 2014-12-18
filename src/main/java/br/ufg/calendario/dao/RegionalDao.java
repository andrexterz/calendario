/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Regional;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
public class RegionalDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public boolean adicionar(Regional regional) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(regional);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean atualizar(Regional regional) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(regional);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean excluir(Regional regional) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(regional);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Regional buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Regional) session.get(Regional.class, id);
    }

    @Transactional(readOnly = true)
    public List<Regional> listar(String termo) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Regional.class);
        criteria.add(Restrictions.like("nome", termo, MatchMode.ANYWHERE).ignoreCase())
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Transactional(readOnly = true)
    public List<Regional> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Regional.class);
        return criteria.list();
    }

    @Transactional(readOnly = true)
    public List<Regional> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Regional.class);
        if ((sortField != null && !sortField.isEmpty()) && (sortOrder != null && !sortOrder.isEmpty())) {
            if (sortOrder.equals("ASCENDING")) {
                criteria.addOrder(Order.asc(sortField));
            }
            if (sortOrder.equals("DESCENDING")) {
                criteria.addOrder(Order.desc(sortField));
            }
        }
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                criteria.add(Restrictions.eq(filter.getKey(), filter.getValue()));
            }
        }
        return criteria.list();
    }

}
