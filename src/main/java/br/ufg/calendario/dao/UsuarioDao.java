/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import br.ufg.calendario.models.PerfilEnum;
import br.ufg.calendario.models.Usuario;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
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
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("login", login));
        return (Usuario) criteria.uniqueResult();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Usuario.class);
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
            criteria.addOrder(Order.asc("id"));
        }
        if (filters != null && !filters.isEmpty()) {
            for (String key : filters.keySet()) {
                if (key.equals("termo")) {
                    criteria.add(
                            Restrictions.or(Restrictions.like("nome", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase(),
                                    Restrictions.like("login", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase()
                            )
                    );
                }

                if (key.equals("perfil")) {
                    PerfilEnum p = (PerfilEnum) filters.get(key);
                    criteria.add(Restrictions.eq("perfil", p));
                }

            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<Usuario> resultado = criteria.list();
        return resultado;
    }

    @Transactional(readOnly = true)
    public int rowCount() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    @Transactional(readOnly = true)
    public int rowCount(Map<String, Object> filters) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        for (String key : filters.keySet()) {
            if (key.equals("nome")) {
                criteria.add(Restrictions.like("nome", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase());
            }

            if (key.equals("login")) {
                criteria.add(Restrictions.like("login", filters.get(key).toString(), MatchMode.ANYWHERE).ignoreCase());
            }

            if (key.equals("perfil")) {
                PerfilEnum p = (PerfilEnum) filters.get(key);
                criteria.add(Restrictions.eq("perfil", p));
            }
        }
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

}
