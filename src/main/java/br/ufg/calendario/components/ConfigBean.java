/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */

@Component
@Scope(value = "singleton")
public class ConfigBean {
    
    @Autowired
    @Qualifier(value = "calendarioSettings")
    private Properties properties;
    
    
    public String getFileLimit() {
        return properties.getProperty("fileLimit");
    }
    
    public String getSizeLimit() {
        return properties.getProperty("sizeLimit");
    }
    
    public String getAllowTypes() {
        return properties.getProperty("allowTypes");
    }
}
