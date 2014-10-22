/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.EventoDao;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Regional;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.Validator;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */
@Component
public class EventoBean {

    @Autowired
    private Validator validator;

    @Autowired
    transient private EventoDao eventoDao;

    private Evento evento;
    private Evento itemSelecionado;
    private final LazyDataModel<Evento> eventos;

    public EventoBean() {

        evento = new Evento();
        itemSelecionado = null;
        eventos = new LazyDataModel<Evento>() {

            private List<Evento> data;

            @Override
            public Object getRowKey(Evento object) {
                return object.getId().toString();
            }

            @Override
            public Evento getRowData(String rowKey) {
                for (Evento evt : data) {
                    if (evt.getId().toString().equals(rowKey)) {
                        return evt;
                    }
                }
                return null;
            }

            @Override
            public List<Evento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                data = eventoDao.listar(first, pageSize, null, null, null);
                setPageSize(pageSize);
                setRowCount(data.size());
                if (data.size() > pageSize) {
                    try {
                        return data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        return data.subList(first, first + (data.size() % pageSize));
                    }
                }
                return data;
            }
        };
    }

    public void adicionar() {
        evento = new Evento();
    }

    public void salvar() {
        //inserir validador
        FacesMessage msg;
        boolean saveStatus;
        if (evento.getId() == null) {
            saveStatus = eventoDao.adicionar(evento);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemSalvo"));
        } else {
            saveStatus = eventoDao.atualizar(evento);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemAtualizado"));
        }
        if (!saveStatus) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroSalvar"));
        }
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Evento getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Evento itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public LazyDataModel<Evento> getEventos() {
        return eventos;
    }
}
