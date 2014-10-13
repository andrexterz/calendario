/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import br.ufg.calendario.dao.InteressadoDao;
import br.ufg.calendario.models.Interessado;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */

@Component
@Scope(value = "session")
public class InteressadoBean implements Serializable {
    
    @Autowired
    transient InteressadoDao interessadoDao;
    
    private Interessado interessado;
    
    private Interessado itemSelecionado;
    
    
    public InteressadoBean() {
        interessado = new Interessado();
    }
    
    public void adicionar() {
        interessado = new Interessado();
    }
    
    public void salvar() {
        //inserir validador
        //implementar adicionar/atualizar evento ao salvar
        interessadoDao.adicionar(interessado);
    }

    public Interessado getInteressado() {
        return interessado;
    }

    public void setInteressado(Interessado interessado) {
        this.interessado = interessado;
    }

    public Interessado getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Interessado itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
