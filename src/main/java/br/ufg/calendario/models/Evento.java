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
    
    @NotNull   
    @Column
    private String titulo;
    
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
}
