/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.converters;

import br.ufg.calendario.dao.CalendarioDao;
import br.ufg.calendario.models.Calendario;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */
@Component
public class CalendarioConverter implements Converter {

    @Autowired
    private CalendarioDao calendarioDao;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Calendario calendario = calendarioDao.buscarPorId(Long.parseLong(value));
            return calendario;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Calendario obj = (Calendario) value;
        if (obj != null && obj.getId() != null) {
            return Long.toString(obj.getId());
        } else {
            return null;
        }
    }
}
