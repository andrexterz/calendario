/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

/**
 *
 * @author andre
 */
public enum Mes {
    JANEIRO(0),
    FEVEREIRO(1),
    MARCO(2),
    ABRIL(3),
    MAIO(4),
    JUNHO(5),
    JULHO(6),
    AGOSTO(7),
    SETEMBRO(8),
    OUTUBRO(9),
    NOVEMBRO(10),
    DEZEMBRO(11);
    
    private int mes;
    
    private Mes(int mes) {
        this.mes = mes;
    }
    
    public int getMes() {
        return mes;
    }
    
}
