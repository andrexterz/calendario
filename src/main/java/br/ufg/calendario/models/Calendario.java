/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca Luiz Fernandes Ribeiro Barca (andrexterz@gmail.com)
 */

@Entity
public class Calendario extends Base {

    public Calendario() {
        ativo = false;
    }

    public Calendario(Integer ano, boolean ativo) {
        this.ano = ano;
        this.ativo = ativo;
    }
    
    @Column(unique = true)
    @NotNull
    private Integer ano;
    
    @NotNull
    private boolean ativo;

    /**
     * @return the ano
     */
    public Integer getAno() {
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(Integer ano) {
        this.ano = ano;
    }

    /**
     * @return the ativo
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * @param ativo the ativo to set
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    
}
