/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import java.io.Serializable;
import org.springframework.stereotype.Component;

/**
 *
 * @author andre
 */

@Component
public class Calendario implements Serializable {
    
    public String getCalendar() {
        return "meu calendário";
    }
    
}
