/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import javax.persistence.Entity;

/**
 *
 * @author André
 */

@Entity
public class Interessado extends Base {
   
    private String nome;

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}
