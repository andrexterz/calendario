/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import javax.persistence.Entity;

/**
 *
 * @author André Luiz Fernandes Ribeiro Barca (andrexterz@gmail.com)
 */

@Entity
public class Regional extends Base {
    
    private String codigo;

    private String nome;

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
}
