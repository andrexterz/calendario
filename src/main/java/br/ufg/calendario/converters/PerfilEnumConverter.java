/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.converters;

import br.ufg.calendario.models.PerfilEnum;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author andre
 */
@Component
public class PerfilEnumConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return (value == null || value.isEmpty()? null: PerfilEnum.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        PerfilEnum perfilType = (PerfilEnum) value;
        return (value == null? null: perfilType.toString());
    }
}
