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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    private Regional selecaoRegional;
    private Interessado selecaoInteressado;

    public EventoBean() {

        evento = new Evento();
        itemSelecionado = null;
        selecaoRegional = null;
        selecaoInteressado = null;
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
        if (eventoDao.excluir(itemSelecionado)) {
            itemSelecionado = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemExcluido"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroExcluir"));
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void uploadEvento(FileUploadEvent event) {
        FacesMessage msg;
        boolean saveStatus = false;
        UploadedFile arquivo = event.getFile();
        try {
            InputStream arquivoReader = arquivo.getInputstream();
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            Reader reader = new InputStreamReader(arquivoReader, decoder);
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            for (Entry<String, Integer> entry:parser.getHeaderMap().entrySet()) {
                System.out.format("header: %s - %d\n", entry.getKey(), entry.getValue());
            }
            for (CSVRecord record: parser) {
                for (int i = 0; i < record.size(); i++) {
                    System.out.print(record.get(i).trim() + "\t");
                }
                System.out.println("\n****************************************************************************");
            }
        } catch (IOException e) {
            System.out.println("erro: " + e.getMessage());
        }
        System.out.println("arquivo enviado: " + arquivo.getFileName());
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("arquivoEnviado"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
    }

    public void importaEvento() {
        System.out.println("processar arquivo enviado ao servidor");
    }

    public void checkDate(SelectEvent event) {
        if (evento.getTermino().before((Date) event.getObject())) {
            evento.setTermino((Date) event.getObject());
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
}
