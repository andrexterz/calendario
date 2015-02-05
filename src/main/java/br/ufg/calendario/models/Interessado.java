/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import javax.persistence.Entity;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */

@Entity
public class Interessado extends Base {

    public Interessado() {
        sigla = null;
        nome = null;
    }

    public Interessado(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }
    
    private String sigla;
    
    private String nome;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}
