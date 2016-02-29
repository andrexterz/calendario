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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.codehaus.jackson.map.ObjectMapper;
import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
@Component
@Scope(value = "session")
public class HomeBean implements Serializable {

    @Autowired
    private transient EventoDao eventoDao;

    @Autowired(required = true)
    private transient CalendarioDao calendarioDao;

    @Autowired(required = true)
    private transient RegionalDao regionalDao;

    @Autowired(required = true)
    private transient InteressadoDao interessadoDao;

    @Autowired(required = true)
    private transient ConfigBean configBean;

    @Autowired(required = true)
    private transient EventoBean eventoBean;

    private Calendario calendario;
    private Date dataSelecionada;
    private List<Evento> eventos;
    Map<Integer, List<String>> highlightDays;
    private int anoSelecionado;

    public HomeBean() {
        calendario = null;
        dataSelecionada = new Date();
        eventos = new ArrayList<>();
        highlightDays = new HashMap();
    }

    @PostConstruct
    private void init() {
        setCalendario(calendarioDao.buscarAtivo());
        getDataSelecionada();
        processaEventosSelecionados();
        anoSelecionado = getCalendario().getAno();
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

    public void processaEventosSelecionados() {
        eventos = eventoDao.listar(calendario, getDataSelecionada());
    }

    public List<Evento> getEventosSelecionados() {
        return eventos;
    }

    public int getNumeroEventosSelecionados() {
        if (eventos != null) {
            return eventos.size();
        }
        return 0;
    }

    public boolean isEventoSelecionado() {
        if (eventos != null) {
            return eventos.size() > 0;
        }
        return false;
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
        this.dataSelecionada = dataSelecionada;
    }

    public Date getPrimeiraDataEvento() {
        if (calendario != null) {
            return eventoDao.buscarPrimeiroDia(getCalendario());
        }
        return null;
    }

    public Date getUltimaDataEvento() {
        if (calendario != null) {
            return eventoDao.buscarUltimoDia(getCalendario());
        }
        return null;
    }

    public List<String> getAssunto() {
        try {
            return eventoDao.listarAssunto(calendario);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
        return null;
    }

    public void processHighLightDays() {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat(configBean.getISODateFormat());
        for (int i = 1; i <= 12; i++) {
            List<String> dates = new ArrayList<>();
            highlightDays.put(i, dates);
        }
        if (calendario != null) {
            for (Evento e : eventoDao.listar(calendario)) {
                Date inicio = e.getInicio();
                Date termino = e.getTermino();
                Date date;
                cal.setTime(inicio);
                while (cal.getTime().before(termino) || cal.getTime().equals(termino)) {
                    date = cal.getTime();
                    List<String> currentDateList = highlightDays.get(cal.get(Calendar.MONTH) + 1);
                    String strDate = formatter.format(date);
                    if (!currentDateList.contains(strDate)) {
                        currentDateList.add(formatter.format(date));
                        System.out.println("added date: " + strDate);
                    }
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
        }
    }

    @ResponseBody
    public String getHighlightDays() {
        try {
            return new ObjectMapper().writeValueAsString(highlightDays);
        } catch (IOException e) {
            System.out.println("erro ao interpretar <json object>");
        }
        return null;
    }

    public void changeCalendar(DateViewChangeEvent event) {
        anoSelecionado = event.getYear();
    }

    public void valueMesListener(ActionEvent event) {
        eventoBean.limpaFiltro();
        Mes mes = Mes.valueOf((String) event.getComponent().getAttributes().get("mes"));
        SimpleDateFormat formatter = new SimpleDateFormat(configBean.getISODateFormat());
        Calendar calendar = new GregorianCalendar(LocaleBean.getInstance().getLocale());
        calendar.set(Calendar.YEAR, anoSelecionado);
        calendar.set(Calendar.MONTH, mes.getMes());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        eventoBean.setBuscaDataInicio(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        eventoBean.setBuscaDataTermino(calendar.getTime());
        System.out.format("Mes de %s, %s - %s\n", mes.name(),
                formatter.format(eventoBean.getBuscaDataInicio()),
                formatter.format(eventoBean.getBuscaDataTermino())
        );
    }

    public boolean isHomeLocation() {
        String[] viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId().split("/");
        return viewId[viewId.length - 1].equals("home.xhtml");
    }
}
