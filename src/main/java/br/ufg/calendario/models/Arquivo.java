/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 *
 * @author andre
 */

@Entity
public class Arquivo extends Base {

    public Arquivo() {
    }

    public Arquivo(String nomeArquivo, byte[] conteudo, String mimetype) {
        this.nomeArquivo = nomeArquivo;
        this.conteudo = conteudo;
        this.mimetype = mimetype;
    }
    
    @NotNull
    @Column
    private String nomeArquivo;
    
    @NotNull
    @Lob
    private byte[] conteudo;

    @NotNull
    @Column
    private String mimetype;

    /**
     * @return the nomeArquivo
     */
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    /**
     * @param nomeArquivo the nomeArquivo to set
     */
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    /**
     * @return the conteudo
     */
    public byte[] getConteudo() {
        return conteudo;
    }

    /**
     * @param conteudo the conteudo to set
     */
    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    /**
     * @return the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * @param mimetype the mimetype to set
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
    
}
