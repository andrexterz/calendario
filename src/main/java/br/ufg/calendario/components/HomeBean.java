/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.CalendarioDao;
import br.ufg.calendario.dao.EventoDao;
import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Regional;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */
@Component
@Scope(value = "session")
public class HomeBean implements Serializable {

    @Autowired
    private transient EventoDao eventoDao;

    @Autowired(required = true)
    private transient CalendarioDao calendarioDao;

    private Calendario calendario;
    private final LazyDataModel<Evento> eventos;
    private TipoBusca tipoBusca;
    private String termoBusca;
    private Regional buscaRegional;
    private Interessado buscaInteressado;
    private Date buscaDataInicio;
    private Date buscaDataTermino;

    public HomeBean() {
        calendario = null;
        tipoBusca = TipoBusca.TERMO;
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
                //reset primefaces filter
                filters = new HashMap();
                setPageSize(pageSize);
                if (getCalendario() != null) {
                    filters.put("calendario", getCalendario());
                }
                if (getTermoBusca() != null && !getTermoBusca().isEmpty()) {
                    System.out.println("termo: " + getTermoBusca());
                    filters.put("termo", getTermoBusca());
                }
                if (getBuscaDataInicio() != null && getBuscaDataTermino() != null) {
                    Map periodo = new HashMap();
                    periodo.put("inicio", getBuscaDataInicio());
                    periodo.put("termino", getBuscaDataTermino());
                    filters.put("periodo", periodo);

                }

                if (getBuscaInteressado() != null) {
                    filters.put("interessado", getBuscaInteressado());
                }

                if (getBuscaRegional() != null) {
                    filters.put("regional", getBuscaRegional());
                }

                if (!filters.isEmpty()) {
                    setRowCount(eventoDao.rowCount(filters));
                } else {
                    setRowCount(eventoDao.rowCount());
                }
                if (getTermoBusca() != null && !termoBusca.isEmpty()) {
                    data = eventoDao.listar(first, pageSize, getTermoBusca());
                } else {
                    data = eventoDao.listar(first, pageSize, sortField, sortOrder == null ? null : sortOrder.name(), filters);
                }

                if (data.size() > pageSize) {
                    try {
                        data = data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        data = data.subList(first, first + (data.size() % pageSize));
                    }
                }
                return data;
            }
        };
    }

    @PostConstruct
    private void init() {
        this.setCalendario(calendarioDao.buscarAtivo());
        System.out.println("calendario ativo: " + getCalendario());
    }

    public void limpaFiltro() {
        setTermoBusca(null);
        setBuscaDataInicio(null);
        setBuscaDataTermino(null);
        setBuscaInteressado(null);
        setBuscaRegional(null);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("filtroVazio"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void checkDateBusca(SelectEvent event) {
        Date selectedDate = (Date) event.getObject();
        if (getBuscaDataTermino() != null && getBuscaDataTermino().before(selectedDate)) {
            setBuscaDataTermino(selectedDate);
        }
    }

    public void resetDateBusca() {
        setBuscaDataInicio(null);
        setBuscaDataTermino(null);
    }

    public boolean isDateBuscaValid() {
        return (getBuscaDataInicio() != null && getBuscaDataTermino() != null);
    }

    /**
     * @return the calendario
     */
    public Calendario getCalendario() {
        return calendario;
    }

    /**
     * @param calendario the calendario to set
     */
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    /**
     * @return the eventos
     */
    public LazyDataModel<Evento> getEventos() {
        return eventos;
    }

    /**
     * @return the tipoBusca
     */
    public TipoBusca getTipoBusca() {
        return tipoBusca;
    }

    /**
     * @param tipoBusca the tipoBusca to set
     */
    public void setTipoBusca(TipoBusca tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public List<TipoBusca> getTipoBuscaList() {
        return TipoBusca.getValues();
    }

    /**
     * @return the termoBusca
     */
    public String getTermoBusca() {
        return termoBusca;
    }

    /**
     * @param termoBusca the termoBusca to set
     */
    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }

    /**
     * @return the buscaRegional
     */
    public Regional getBuscaRegional() {
        return buscaRegional;
    }

    /**
     * @param buscaRegional the buscaRegional to set
     */
    public void setBuscaRegional(Regional buscaRegional) {
        this.buscaRegional = buscaRegional;
    }

    /**
     * @return the buscaInteressado
     */
    public Interessado getBuscaInteressado() {
        return buscaInteressado;
    }

    /**
     * @param buscaInteressado the buscaInteressado to set
     */
    public void setBuscaInteressado(Interessado buscaInteressado) {
        this.buscaInteressado = buscaInteressado;
    }

    /**
     * @return the buscaDataInicio
     */
    public Date getBuscaDataInicio() {
        return buscaDataInicio;
    }

    /**
     * @param buscaDataInicio the buscaDataInicio to set
     */
    public void setBuscaDataInicio(Date buscaDataInicio) {
        this.buscaDataInicio = buscaDataInicio;
    }

    /**
     * @return the buscaDataTermino
     */
    public Date getBuscaDataTermino() {
        return buscaDataTermino;
    }

    /**
     * @param buscaDataTermino the buscaDataTermino to set
     */
    public void setBuscaDataTermino(Date buscaDataTermino) {
        this.buscaDataTermino = buscaDataTermino;
    }

}
