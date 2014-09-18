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
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author andre
 */
@Component
@Scope(value = "request")
public class CalendarioBean implements Serializable {

    private final LazyDataModel eventos;

    public CalendarioBean() {
        eventos = new LazyDataModel() {

            @Override
            public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                return super.load(first, pageSize, sortField, sortOrder, filters); //To change body of generated methods, choose Tools | Templates.
            }
        };

    }

    public LazyDataModel getEventosRecentes() {
        return eventos;
    }
}
