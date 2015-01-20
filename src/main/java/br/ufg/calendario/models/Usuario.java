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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
@Entity
public class Usuario extends Base {

    public Usuario() {
        nome = null;
        login = null;
        email = null;
        senha = null;
        perfil = new ArrayList<>();
        ativo = false;
    }

    public Usuario(String nome, String login, String email, String senha, List<PerfilEnum> perfil, Boolean ativo) {
        this.nome = nome;
        this.login = login;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    @NotNull
    @Size(min = 5, max = 128)
    private String nome;
    
    @NotNull
    @Size(min = 3, max= 16)
    private String login;
    
    @NotNull
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    
    @NotNull
    private String senha;

    @Column(columnDefinition = "boolean default false")
    @NotNull
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

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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

    public void removePerfil(PerfilEnum p) {
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
    
    public boolean isAdministrador() {
        return perfil.contains(PerfilEnum.ADMINISTRADOR);
    }
    
    public boolean isRevisor() {
        return perfil.contains(PerfilEnum.REVISOR);
    }
    
    public boolean isEditor() {
        return perfil.contains(PerfilEnum.EDITOR);
    }

}
