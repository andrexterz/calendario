/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Evento;
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
}
