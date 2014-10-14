/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author André Luiz Fernandes Ribeiro Barca (andrexterz@gmail.com)
 */

@Entity
public class Evento extends Base {

    public Evento() {
    }

    public Evento(String assunto, Date inicio, Date termino, String descricao, Calendario calendario, Set<Regional> regional, Set<Interessado> interessado) {
        this.assunto = assunto;
        this.inicio = inicio;
        this.termino = termino;
        this.descricao = descricao;
        this.calendario = calendario;
        this.regional = regional;
        this.interessado = interessado;
    }
    
    
    @NotNull   
    @Column
    private String assunto;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicio;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date termino;
    
    @NotNull
    @Column
    private String descricao;
    
    @NotNull
    @ManyToOne
    private Calendario calendario;
    
    
    @NotNull
    @ManyToMany
    @JoinTable(name = "evento_regional", joinColumns = {@JoinColumn(name = "evento_id")}, inverseJoinColumns = {@JoinColumn(name = "regional_id")})
    private Set<Regional> regional;
    
    @NotNull
    @ManyToMany
    @JoinTable(name = "evento_interessado", joinColumns = {@JoinColumn(name = "evento_id")}, inverseJoinColumns = {@JoinColumn(name = "interessado_id")})
    private Set<Interessado> interessado;

    /**
     * @return the assunto
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * @param assunto the assunto to set
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    /**
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the termino
     */
    public Date getTermino() {
        return termino;
    }

    /**
     * @param termino the termino to set
     */
    public void setTermino(Date termino) {
        this.termino = termino;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the calendario
     */
    public Calendario getCalendario() {
        return calendario;
    }

    /**
     * @param calendario the calendario to set
     */
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    /**
     * @return the regional
     */
    public Set<Regional> getRegional() {
        return regional;
    }

    /**
     * @param regional the regional to set
     */
    public void setRegional(Set<Regional> regional) {
        this.regional = regional;
    }

    /**
     * @return the interessado
     */
    public Set<Interessado> getInteressado() {
        return interessado;
    }

    /**
     * @param interessado the interessado to set
     */
    public void setInteressado(Set<Interessado> interessado) {
        this.interessado = interessado;
    }
}
