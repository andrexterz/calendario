/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Interessado;
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
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<Interessado> listar() {
                Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Interessado.class);
        return criteria.list();
    }

}
