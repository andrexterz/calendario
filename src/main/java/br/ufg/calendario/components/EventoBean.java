/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import br.ufg.calendario.dao.EventoDao;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Regional;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
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
    private final List<Evento> eventos;
    
    public EventoBean() {
        
        evento = new Evento();
        itemSelecionado = null;
        eventos = new ArrayList<>();
    }
    
    public void adicionar() {
        evento = new Evento();
    }
    
    public void salvar() {
        //inserir validador
        //implementar adicionar/atualizar evento ao salvar
        for (Regional r: evento.getRegional()) {
            System.out.println("Regional: " +  r.getNome());
        }
        
        for (Interessado i: evento.getInteressado()) {
            System.out.println("Interessado: " + i.getNome());
        }
        
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
    
    public List<Evento> getEventos() {
       return eventos;
    }
}
