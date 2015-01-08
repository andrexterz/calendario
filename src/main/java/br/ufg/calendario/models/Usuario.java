/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */

@Entity
public class Usuario extends Base {

    public Usuario() {
        nome = null;
        login = null;
        perfil = new ArrayList<>();
        ativo = false;
        senha = null;
    }

    public Usuario(String nome, String login, String senha, List<PerfilEnum> perfil, Boolean ativo) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = ativo;
    }
    
    private String nome;
    
    private String login;
    
    private String senha;
    
    @Column(columnDefinition = "boolean default false")
    private boolean ativo;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<PerfilEnum> perfil;

 
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<PerfilEnum> getPerfil() {
        return perfil;
    }

    public void setPerfil(List<PerfilEnum> perfil) {
        this.perfil = perfil;
    }
    
    public void addPerfil(PerfilEnum p) {
        this.perfil.add(p);
    }
    
    public void removePerfil(PerfilEnum p){
        this.perfil.remove(p);
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
