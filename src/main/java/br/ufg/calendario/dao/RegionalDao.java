/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Regional;
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
public class RegionalDao {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public boolean adicionar(Regional regional) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(regional);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean atualizar(Regional regional) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(regional);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean excluir(Regional regional) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(regional);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Regional buscar(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Regional) session.get(Regional.class, id);
    }
        
    
    @Transactional(readOnly = true)
    public List<Regional> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Regional.class);
        return criteria.list();
    }

}
