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
public class EventoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public boolean adicionar(Evento evento) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(evento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean atualizar(Evento evento) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(evento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean excluir(Evento evento) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(evento);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        if ((sortField != null && !sortField.isEmpty())  && (sortOrder != null && !sortOrder.isEmpty())) {
            if (sortOrder.equals("ASCENDING")) {
                criteria.addOrder(Order.asc(sortField));
            } if (sortOrder.equals("DESCENDING")) {
                criteria.addOrder(Order.desc(sortField));
            }
        }
        if (filters != null && !filters.isEmpty()) {
            //assunto || descricao || (dataInicio && dataTermino) || interessado || regional
        }
        return criteria.list();
    }
}
