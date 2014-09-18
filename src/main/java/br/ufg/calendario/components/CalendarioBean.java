/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Regional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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

    private final LazyDataModel<Evento> eventos;

    public CalendarioBean() {
        final List<Evento> datasource = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            Evento evt = new Evento(
                    "Evento: " + i,
                    Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime(),
                    "Descrição " + i, new Calendario(),
                    new ArrayList<Regional>(),
                    new ArrayList<Interessado>());
            datasource.add(evt);
        }

        eventos = new LazyDataModel<Evento>() {
            @Override
            public Evento getRowData(String rowKey) {
                //modifievt este metodo quando o banco estiver funcionando
                for (Object evt : datasource) {
                    if (((Evento) evt).getId().equals(Long.parseLong(rowKey))) {
                        return (Evento) evt;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Evento evento) {
                return evento.getId();
            }

            @Override
            public List<Evento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Evento> data = new ArrayList<>();
                //filter
                for (Object evt : datasource) {
                    boolean match = true;

                    if (filters != null) {
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                            try {
                                String filterProperty = it.next();
                                Object filterValue = filters.get(filterProperty);
                                String fieldValue = String.valueOf(evt.getClass().getField(filterProperty).get(evt));

                                if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } catch (Exception e) {
                                match = false;
                            }
                        }
                    }

                    if (match) {
                        data.add((Evento) evt);
                    }
                }
                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                if (dataSize > pageSize) {
                    try {
                        System.out.println("paginating...");
                        return data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        return data.subList(first, first + (dataSize % pageSize));
                    }
                } else {
                    return data;
                }
            }
        };

    }

    public LazyDataModel<Evento> getEventosRecentes() {
        return eventos;
    }
}
