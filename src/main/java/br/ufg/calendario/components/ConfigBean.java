/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */

@Component
@Scope(value = "singleton")
public class ConfigBean {
    
    @Autowired
    private transient SessionFactory sessionFactory;
    
    @Autowired
    @Qualifier(value = "calendarioSettings")
    private Properties properties;

    @Transactional(readOnly = true)
    public void reindexLucene() throws InterruptedException {
        System.out.println("starting indexer...");
        Session session = sessionFactory.getCurrentSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();
     }
    
    public String getFileLimit() {
        return properties.getProperty("fileLimit");
    }
    
    public String getSizeLimit() {
        return properties.getProperty("sizeLimit");
    }
    
    public String getAllowTypes() {
        return properties.getProperty("allowTypes");
    }
    
    public String getDateFormat() {
        return properties.getProperty("dateFormat");
    }
    
    
    public String getISODateFormat() {
        return properties.getProperty("ISODateFormat");
    }
    
    
    public String getRegexSplitter() {
        return properties.getProperty("regexSplitter");
    }
    
    public char getDelimiter() {
        return properties.getProperty("csvDelimiter").charAt(0);
    }
    
    public String getCalendarAllowTypes() {
        return properties.getProperty("calendarAllowTypes");
    }
    
    
}
