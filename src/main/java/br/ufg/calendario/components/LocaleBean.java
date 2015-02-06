package br.ufg.calendario.components;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
@Component
@Scope(value = "session")
public class LocaleBean implements Serializable {
    public LocaleBean () {
            locale = "pt_BR";
    }
    private String locale;
    private static LocaleBean instance = null;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public static LocaleBean getInstance() {
        if (instance == null) {
            instance = new LocaleBean();
        }
        return instance;
    }

    public static String getMessage(String msg) {
        ResourceBundle messages = ResourceBundle.getBundle(
                "br.ufg.calendario.locale.messages",
                new Locale(getInstance().getLocale()));
        return messages.getString(msg);
    }
}
