/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.CalendarioDao;
import br.ufg.calendario.models.Calendario;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
 * @author andre
 */
@Component
@Scope(value = "session")
public class CalendarioBean implements Serializable {

    @Autowired
    transient private CalendarioDao calendarioDao;

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
        FacesMessage msg;
        boolean res;
        if (calendario.getId() == null) {
            res = calendarioDao.adicionar(calendario);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemSalvo"));
        } else {
            res = calendarioDao.atualizar(calendario);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemAtualizado"));
        }
        if (!res) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroSalvar"));
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
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
}
