/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.dao;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */

@Lazy(false)
@Component
public class HibernateIndexer {

    @Autowired
    private SessionFactory sessionFactory;
    private final Logger logger = Logger.getLogger("FullTextSession");
    
    private void HibernateIndexer() throws InterruptedException {
        FullTextSession fullTextSession = Search.getFullTextSession(this.sessionFactory.getCurrentSession());
        fullTextSession.createIndexer().startAndWait();
        logger.info("\n\n\n\n\nindexer started!\n\n\n\n\n");
        System.out.println("starting indexer...");
    }
}
