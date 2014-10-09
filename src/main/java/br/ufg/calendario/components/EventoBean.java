/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import br.ufg.calendario.dao.EventoDao;
import br.ufg.calendario.models.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */

@Component
public class EventoBean {
    
    @Autowired
    private EventoDao eventoDao;
    
    private Evento evento;
    private Evento itemSelecionado;

    public EventoBean() {
        
        evento = new Evento();
        itemSelecionado = null;
    }
    
    public void adicionar() {
        evento = new Evento();
    }
    
    public void salvar() {
        //inserir validador
        //implementar adicionar/atualizar evento ao salvar
        eventoDao.adicionar(evento);
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Evento getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Evento itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
