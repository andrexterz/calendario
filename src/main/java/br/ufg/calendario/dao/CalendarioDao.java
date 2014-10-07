/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Calendario;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        return false;
    }
    
    @Transactional(readOnly = true)
    public List<Calendario> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Calendario.class);
        return criteria.list();
    }

}
