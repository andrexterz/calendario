/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import javax.persistence.Entity;
import org.hibernate.search.annotations.Field;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca Luiz Fernandes Ribeiro Barca (andrexterz@gmail.com)
 */

@Entity
public class Regional extends Base {

    public Regional() {
        codigo = null;
        nome = null;
    }

    public Regional(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }
    
    private String codigo;

    @Field
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
