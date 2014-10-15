/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
        session.save(calendario);
        return false;
    }

    @Transactional
    public boolean atualizar(Calendario calendario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(calendario);
            return false;
        } catch (Exception e) {
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
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Calendario buscar(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Calendario) session.get(Calendario.class, id);
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
            //ano || ativo/inativo
        }
        return criteria.list();
    }
}
