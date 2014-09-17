/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author andre
 */

@Component
@Scope(value = "request")
public class CalendarioBean implements Serializable {
    private final List eventos;
    public CalendarioBean() {
        eventos = new ArrayList<Object>();
        for (int i = 0;i <= 30; i++) {
            List evt = new ArrayList<Object>();
            evt.add("Evento: " + i);
            evt.add(Calendar.getInstance().getTime());
            evt.add(Calendar.getInstance().getTime());
            evt.add("description " + i);
            evt.add("coordenador, aluno, docente");
            eventos.add(evt);
        }
    }
    
    public List<Object> getEventosRecentes() {
        return eventos;
    }
}
