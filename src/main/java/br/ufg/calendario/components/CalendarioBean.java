/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.CalendarioDao;
import br.ufg.calendario.models.Arquivo;
import br.ufg.calendario.models.Calendario;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
@Component
@Scope(value = "session")
public class CalendarioBean implements Serializable {

    @Autowired
    private transient CalendarioDao calendarioDao;

    @Autowired
    private transient Validator validator;

    private Calendario calendario;
    private Calendario itemSelecionado;
    private final LazyDataModel<Calendario> calendarios;

    public CalendarioBean() {
        calendario = new Calendario();
        itemSelecionado = null;
        calendarios = new LazyDataModel<Calendario>() {

            private List<Calendario> data;

            @Override
            public Calendario getRowData(String rowKey) {
                for (Calendario cal : data) {
                    if (cal.getId().equals(Long.parseLong(rowKey))) {
                        return cal;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Calendario cal) {
                return cal.getId().toString();
            }

            @Override
            public List<Calendario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                data = calendarioDao.listar(first, pageSize, sortField, sortOrder.name(), filters);
                setPageSize(pageSize);
                setRowCount(calendarioDao.rowCount());

                if (data.size() > pageSize) {
                    try {
                        return data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        return data.subList(first, first + (data.size() % pageSize));
                    }
                } else {
                    return data;
                }
            }
        };

    }

    public void adicionar() {
        calendario = new Calendario();
    }

    public void editar() {
        if (itemSelecionado != null) {
            calendario = itemSelecionado;
        }
    }

    public void salvar() {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg;
        boolean saveStatus = false;
        Set<ConstraintViolation<Calendario>> errors = validator.validate(calendario);
        if (errors.isEmpty()) {
            if (calendario.getId() == null) {
                saveStatus = calendarioDao.adicionar(calendario);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemSalvo"));
            } else {
                saveStatus = calendarioDao.atualizar(calendario);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemAtualizado"));
            }
            if (!saveStatus) {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroSalvar"));
            }
            context.addMessage(null, msg);
        } else {
            for (ConstraintViolation<Calendario> violations : errors) {
                String errorMessage = String.format("%s: %s", violations.getPropertyPath().toString(), violations.getMessage());
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", errorMessage);
                context.addMessage(null, msg);
            }
        }

        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
    }

    public void excluir() {
        FacesMessage msg;
        if (calendarioDao.excluir(itemSelecionado)) {
            itemSelecionado = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemExcluido"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroExcluir"));
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void selecionaItem(SelectEvent event) {
        itemSelecionado = (Calendario) event.getObject();
    }

    public void handleArquivoListener(FileUploadEvent event) {
        if (calendario.getArquivo() != null) {
            calendario.getArquivo().setNomeArquivo(event.getFile().getFileName());
            calendario.getArquivo().setConteudo(event.getFile().getContents());
            calendario.getArquivo().setMimetype(event.getFile().getContentType());
        } else {
            Arquivo arquivo = new Arquivo();
            arquivo.setNomeArquivo(event.getFile().getFileName());
            arquivo.setConteudo(event.getFile().getContents());
            arquivo.setMimetype(event.getFile().getContentType());
            calendario.setArquivo(arquivo);
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

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Calendario getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Calendario itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public LazyDataModel<Calendario> getCalendarios() {
        return calendarios;
    }

    public List<Calendario> getCalendarioList() {
        return calendarioDao.listar();
    }
}
