/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author andre
 */

@Scope(value = "request")
@Component
public class Calendario implements Serializable {
    
    @RequestMapping("/home")
    public String getCalendar() {
        return "views/home";
    }
    
}
