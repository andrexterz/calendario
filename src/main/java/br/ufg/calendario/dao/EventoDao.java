/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Evento;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
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

    @Transactional(readOnly = true)
    public Evento buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Evento) session.get(Evento.class, id);
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
            criteria.addOrder(Order.asc("inicio"));
        }
        if (filters != null && !filters.isEmpty()) {
            //assunto || descricao || (dataInicio && dataTermino) || interessado || regional
        }
        return criteria.list();
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
        for (String field : filters.keySet()) {
            if (field.equals("assunto")) {
                criteria.add(Restrictions.like(field, filters.get(field).toString(), MatchMode.ANYWHERE).ignoreCase());
            }
            if (field.equals("periodo")) {
                System.out.println("implementar filtro periodo");
            }
            if (field.equals("descricao")) {
                System.out.println("implementar filtro descricao");
            }
            if (field.equals("interessado")) {
                System.out.println("implementar filtro interessado");
            }
            if (field.equals("regional")) {
                System.out.println("implementar filtro regional");
            }
        }
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }
}
