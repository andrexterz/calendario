/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author André
 */
@Resource
@Scope(value = "singleton")
public class HibernateIndexer {

    @Autowired
    private SessionFactory sessionFactory;

    private HibernateIndexer() throws InterruptedException {
        //FullTextSession fullTextSession = Search.getFullTextSession(this.sessionFactory.getCurrentSession());
        //fullTextSession.createIndexer().startAndWait();
    }
}
