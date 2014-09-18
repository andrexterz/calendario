/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.dao;

import br.ufg.calendario.models.Evento;
import java.util.List;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author André
 */
public class EventoDao  extends LazyDataModel<Evento> {
    
    private List<Evento> datasource;
    
}
