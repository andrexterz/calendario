package br.ufg.calendario.components;


import java.util.ResourceBundle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author André
 */

public class LocaleBean {
    
    private String locale = "pt_BR";
    private static LocaleBean instance = null;

    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public static LocaleBean getInstance() {
        if (instance == null) {
            instance =  new LocaleBean();
        }
        return instance;
    }

    public static String getMessage(String msg) {
        ResourceBundle messages = ResourceBundle.getBundle("br.ufg.calendario.locale.messages");
        return messages.getString(msg);
    }
}