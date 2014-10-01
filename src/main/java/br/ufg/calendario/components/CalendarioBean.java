/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Mes;
import br.ufg.calendario.models.Regional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
@Scope(value = "session")
public class CalendarioBean implements Serializable {

    private final LazyDataModel<Evento> eventos;
    private Mes mes;
    private Date dataInicio;
    private Date dataTermino;

    public CalendarioBean() {
        dataInicio = Calendar.getInstance().getTime();
        dataTermino = Calendar.getInstance().getTime();

        //just test (remove after datamodel is defined)        
        final List<Evento> datasource = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            Evento evt = new Evento(
                    "Evento n. " + i,
                    Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime(),
                    "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos. Lorem Ipsum sobreviveu não só a cinco séculos, como também ao salto para a editoração eletrônica, permanecendo essencialmente inalterado. Se popularizou na década de 60, quando a Letraset lançou decalques contendo passagens de Lorem Ipsum, e mais recentemente quando passou a ser integrado a softwares de editoração eletrônica como Aldus PageMaker. Evento: " + i, new Calendario(),
                    new ArrayList<Regional>(),
                    new ArrayList<Interessado>());
            evt.setId(Long.valueOf(i));
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

    public List<Mes> getMeses() {
        return Arrays.asList(Mes.values());
    }

    public void checkDate() {
        if (dataTermino.before(dataInicio)) {
            setDataTermino(dataInicio);
        }
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

}
