/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.Usuario;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author andre
 */
@Repository
@Scope(value = "singleton")
public class UsuarioDao {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public boolean adicionar(Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.clear();
            session.save(usuario);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean atualizar(Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.clear();
            session.update(usuario);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional
    public boolean excluir(Usuario usuario) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(usuario);
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.clear();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.get(Usuario.class, id);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorLogin(String login) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return null;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        return null;
    }

    @Transactional(readOnly = true)
    public int rowCount() {
        return 0;
    }

    @Transactional(readOnly = true)
    public int rowCount(Map<String, Object> filters) {
        return 0;
    }

}
