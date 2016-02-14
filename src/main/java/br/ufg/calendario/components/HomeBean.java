/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.CalendarioDao;
import br.ufg.calendario.dao.EventoDao;
import br.ufg.calendario.dao.InteressadoDao;
import br.ufg.calendario.dao.RegionalDao;
import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
@Component
@Scope(value = "request")
public class HomeBean implements Serializable {

    @Autowired
    private transient EventoDao eventoDao;

    @Autowired(required = true)
    private transient CalendarioDao calendarioDao;

    @Autowired(required = true)
    private transient RegionalDao regionalDao;

    @Autowired(required = true)
    private transient InteressadoDao interessadoDao;

    private Calendario calendario;
    private Date dataSelecionada;
    private int numeroEventosSelecionados;

    public HomeBean() {
        calendario = null;
        dataSelecionada = null;
        numeroEventosSelecionados = 0;
    }

    @PostConstruct
    private void init() {
        this.setCalendario(calendarioDao.buscarAtivo());
        System.out.println("calendario ativo: " + getCalendario());
        if (dataSelecionada == null) {
            dataSelecionada = new Date();
        }
    }

    public StreamedContent getArquivoCalendario() {
        DefaultStreamedContent content = new DefaultStreamedContent();
        try {
            InputStream inputStream = new ByteArrayInputStream(calendario.getArquivo().getConteudo());
            content.setName(calendario.getArquivo().getNomeArquivo());
            content.setContentType(calendario.getArquivo().getMimetype());
            content.setStream(inputStream);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return content;
    }

    public List<Evento> getEventosSelecionados() throws ParseException {
        List<Evento> eventos = eventoDao.listar(getDataSelecionada(), calendario);
        numeroEventosSelecionados = eventos.size();
        return eventos;
    }
    
    public int getNumeroEventosSelecionados() {
        return numeroEventosSelecionados;
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

    public Date getDataSelecionada() {
        return dataSelecionada;
    }

    public void setDataSelecionada(Date dataSelecionada) {
        System.out.println("Data selecionada: " + dataSelecionada.toString());
        this.dataSelecionada = dataSelecionada;
    }
    
    public Date getPrimeiraDataEvento() {
        return eventoDao.buscarPrimeiroDia(getCalendario());
    }
    
    public Date getUltimaDataEvento() {
        return eventoDao.buscarUltimoDia(getCalendario());
    }

    public List<String> getAssunto() {
        try {
            return eventoDao.listarAssunto(calendario);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
        return null;
    }

    public boolean isHomeLocation() {
        String[] viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId().split("/");
        return viewId[viewId.length - 1].equals("home.xhtml");
    }
}
