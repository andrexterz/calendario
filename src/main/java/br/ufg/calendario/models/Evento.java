/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.models;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    public Evento(String assunto, Date inicio, Date termino, String descricao, Calendario calendario, List<Regional> regional, List<Interessado> interessado) {
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
    
    @OneToMany
    private List<Regional> regional;
    
    @OneToMany
    private List<Interessado> interessado;

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
    public List<Regional> getRegional() {
        return regional;
    }

    /**
     * @param regional the regional to set
     */
    public void setRegional(List<Regional> regional) {
        this.regional = regional;
    }

    /**
     * @return the interessado
     */
    public List<Interessado> getInteressado() {
        return interessado;
    }

    /**
     * @param interessado the interessado to set
     */
    public void setInteressado(List<Interessado> interessado) {
        this.interessado = interessado;
    }
}
