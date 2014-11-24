/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author André
 */
public enum TipoBusca {

    TERMO("termo"),
    INTERESSADO("interessado"),
    PERIODO("periodo"),
    REGIONAL("regional");

    private final String value;

    private TipoBusca(String value) {
        this.value = value;
    }

    public String getValue() {
        return LocaleBean.getMessage(value);
    }
    
    public static List<TipoBusca> getValues() {
        return Arrays.asList(TipoBusca.values());
    }
};
