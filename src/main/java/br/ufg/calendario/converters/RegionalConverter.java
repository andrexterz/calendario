/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.converters;

import br.ufg.calendario.dao.RegionalDao;
import br.ufg.calendario.models.Regional;
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
public class RegionalConverter implements Converter{

    @Autowired
    private RegionalDao regionalDao;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Regional regional = regionalDao.buscarPorId(Long.parseLong(value));
            return regional;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Regional obj = (Regional) value;
        if (obj != null && obj.getId() != null) {
            return Long.toString(obj.getId());
        } else {
            return null;
        }
    }
}
