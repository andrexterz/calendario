/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

/**
 *
 * @author André
 */
public enum TipoBusca {

    ASSUNTO("assunto"),
    DESCRICAO("descricao"),
    INTERESSADO("interessado"),
    PERIODO("periodo"),
    REGIONAL("regional");

    private final String value;

    private TipoBusca(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
};
