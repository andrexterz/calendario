/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.CalendarioDao;
import br.ufg.calendario.dao.EventoDao;
import br.ufg.calendario.dao.InteressadoDao;
import br.ufg.calendario.models.Calendario;
import br.ufg.calendario.models.Evento;
import br.ufg.calendario.models.Interessado;
import br.ufg.calendario.models.Regional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.Validator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */
@Component
@Scope(value = "session")
public class EventoBean {

    @Autowired
    private Validator validator;

    @Autowired
    private transient EventoDao eventoDao;

    @Autowired
    private transient InteressadoDao interessadoDao;

    @Autowired
    private transient CalendarioDao calendarioDao;

    @Autowired
    private transient ConfigBean configBean;

    private Evento evento;
    private Evento itemSelecionado;
    private Calendario calendario;
    private Regional selecaoRegional;
    private Interessado selecaoInteressado;
    private final LazyDataModel<Evento> eventos;
    private List<Evento> eventosImportados;
    private TipoBusca tipoBusca;

    //assunto | descricao
    private String termoBusca;
    private Regional buscaRegional;
    private Interessado buscaInteressado;
    private Date buscaDataInicio;
    private Date buscaDataTermino;

    public EventoBean() {
        evento = new Evento();
        itemSelecionado = null;
        calendario = null;
        System.out.println("calendario default: " +  calendario);
        selecaoRegional = null;
        selecaoInteressado = null;
        eventosImportados = new ArrayList<>();
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
                    System.out.println("interessado: "  + getBuscaInteressado().getNome());
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
                data = eventoDao.listar(first, pageSize, sortField, sortOrder == null? null: sortOrder.name(), filters);
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

    public void adicionar() {
        evento = new Evento();
    }

    public void editar() {
        if (itemSelecionado != null) {
            evento = itemSelecionado;
        }

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
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
    }

    public void excluir() {
        FacesMessage msg;
        boolean saveStatus = eventoDao.excluir(itemSelecionado);
        if (saveStatus) {
            itemSelecionado = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemExcluido"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroExcluir"));
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
    }

    public void uploadEvento(FileUploadEvent event) {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        FacesMessage msg;
        boolean saveStatus = false;
        UploadedFile arquivo = event.getFile();
        try {
            InputStream arquivoReader = arquivo.getInputstream();
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            Reader reader = new InputStreamReader(arquivoReader, decoder);
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withDelimiter(configBean.getDelimiter()));
            SimpleDateFormat dateFormatter = new SimpleDateFormat(configBean.getDateFormat());
            for (Entry<String, Integer> entry : parser.getHeaderMap().entrySet()) {
                System.out.format("header: %s - %d\n", entry.getKey(), entry.getValue());
            }
            Integer ano;
            Calendario cal = null;
            for (CSVRecord record : parser) {
                //adicionar entidade calendario (select box) na tela importar eventos.
                Date dataInicio = dateFormatter.parse(record.get(0));
                Date dataTermino = dateFormatter.parse(record.get(1));
                String assunto = record.get(2);
                String descricao = record.get(3);
                String[] interessadoArray = record.get(4).split(configBean.getRegexSplitter());
                ano = Integer.parseInt(record.get(5));
                if (cal == null) {
                    cal = calendarioDao.buscar(ano);
                }
                Set<Interessado> interessadoList = new HashSet();
                for (String interessado : interessadoArray) {
                    interessadoList.addAll(interessadoDao.listar(interessado));
                }
                Evento evt = new Evento(assunto, dataInicio, dataTermino, descricao, cal, null, interessadoList, false);
                //dividir string interessado em array ou list e depois fazer busca por entidade com mesmo nome.
                eventosImportados.add(evt);
            }
        } catch (IOException | ParseException e) {
            System.out.println("erro: " + e.getMessage());
        }
        System.out.println("arquivo enviado: " + arquivo.getFileName());
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("arquivoEnviado"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
    }

    public void importaEvento() {
        FacesMessage msg;
        boolean saveStatus = eventoDao.adicionar(eventosImportados);
        if (saveStatus) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("arquivoImportado"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroImportacao"));
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void limpaEventosImportados() {
        eventosImportados.clear();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("listaExcluida"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void limpaFiltro() {
        setTermoBusca(null);
        setBuscaDataInicio(null);
        setBuscaDataTermino(null);
    }

    public void checkDate(SelectEvent event) {
        if (evento.getTermino().before((Date) event.getObject())) {
            evento.setTermino((Date) event.getObject());
        }
    }
    
    public void checkDateBusca(SelectEvent event) {
        Date selectedDate = (Date) event.getObject();
        if (getBuscaDataTermino() != null && getBuscaDataTermino().before(selectedDate)) {
            setBuscaDataTermino(selectedDate);
        }
    }
    
    
    public void adicionaRegional() {
        evento.addRegional(getSelecaoRegional());
    }

    public void removeRegional() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        Regional regional = (Regional) requestMap.get("regional");
        evento.removeRegional(regional);
    }

    public void adicionaInteressado() {
        evento.addInteressado(getSelecaoInteressado());
    }

    public void removeInteressado() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        Interessado interessado = (Interessado) requestMap.get("interessado");
        evento.removeInteressado(interessado);
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

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        System.out.println("calendario alterado: " + calendario.getAno());
        this.calendario = calendario;
    }
    
    public LazyDataModel<Evento> getEventos() {
        return eventos;
    }

    public Regional getSelecaoRegional() {
        return selecaoRegional;
    }

    public void setSelecaoRegional(Regional selecaoRegional) {
        this.selecaoRegional = selecaoRegional;
    }

    public Interessado getSelecaoInteressado() {
        return selecaoInteressado;
    }

    public void setSelecaoInteressado(Interessado selecaoInteressado) {
        this.selecaoInteressado = selecaoInteressado;
    }

    public List<Evento> getEventosImportados() {
        return eventosImportados;
    }

    public TipoBusca getTipoBusca() {
        return tipoBusca;
    }

    public void setTipoBusca(TipoBusca tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public List<TipoBusca> getTipoBuscaList() {
        return TipoBusca.getValues();
    }

    public String getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }

    public Regional getBuscaRegional() {
        return buscaRegional;
    }

    public void setBuscaRegional(Regional buscaRegional) {
        this.buscaRegional = buscaRegional;
    }

    public Interessado getBuscaInteressado() {
        return buscaInteressado;
    }

    public void setBuscaInteressado(Interessado buscaInteressado) {
        this.buscaInteressado = buscaInteressado;
    }
    
    public Date getBuscaDataInicio() {
        return buscaDataInicio;
    }

    public void setBuscaDataInicio(Date buscaDataInicio) {
        this.buscaDataInicio = buscaDataInicio;
    }

    public Date getBuscaDataTermino() {
        return buscaDataTermino;
    }

    public void setBuscaDataTermino(Date buscaDataTermino) {
        this.buscaDataTermino = buscaDataTermino;
    }
    
}
