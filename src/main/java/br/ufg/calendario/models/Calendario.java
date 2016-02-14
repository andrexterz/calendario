/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.models;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca Luiz Fernandes Ribeiro Barca
 * (andrexterz@gmail.com)
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

    @OneToOne(cascade = CascadeType.ALL)
    private Arquivo arquivo;

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

    /**
     * @return the arquivo
     */
    public Arquivo getArquivo() {
        return arquivo;
    }

    /**
     * @param arquivo the arquivo to set
     */
    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Date getPrimeiroDiaDoAno() {
        GregorianCalendar calendario = new GregorianCalendar();
        calendario.set(GregorianCalendar.YEAR,getAno());
        calendario.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
        calendario.set(GregorianCalendar.DATE, calendario.getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
        return calendario.getTime();
    }

    public Date getUltimoDiaDoAno() {
        GregorianCalendar calendario = new GregorianCalendar();
        calendario.set(GregorianCalendar.YEAR, getAno());
        calendario.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
        calendario.set(GregorianCalendar.DATE, calendario.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        return calendario.getTime();
    }

}
