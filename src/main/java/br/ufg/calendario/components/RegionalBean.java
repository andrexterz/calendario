/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.RegionalDao;
import br.ufg.calendario.models.Regional;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
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
public class RegionalBean implements Serializable{

    @Autowired
    transient RegionalDao regionalDao;

    private Regional regional;
    
    private Regional itemSelecionado;
    
    private final LazyDataModel<Regional> regionais;

    public RegionalBean() {
        regional = new Regional();
        itemSelecionado = null;
        regionais = new LazyDataModel<Regional>() {
            
            private List<Regional> data;

            @Override
            public Regional getRowData(String rowKey) {
                for (Regional reg : data) {
                    if (reg.getId().equals(Long.parseLong(rowKey))) {
                        return reg;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Regional reg) {
                return reg.getId().toString();
            }

            @Override
            public List<Regional> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                data = regionalDao.listar(first, pageSize, sortField, sortOrder.name(), filters);
                setPageSize(pageSize);
                setRowCount(data.size());
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
        regional = new Regional();
    }

    public void editar() {
        if (itemSelecionado != null) {
            regional = itemSelecionado;
        }
    }

    public void salvar() {
        FacesMessage msg;
        boolean saveStatus;
        if (regional.getId() == null) {
            saveStatus = regionalDao.adicionar(regional);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemSalvo"));
        } else {
            saveStatus = regionalDao.atualizar(regional);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemAtualizado"));
        }
        if (!saveStatus) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroSalvar"));
        }
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void excluir() {
        FacesMessage msg;
        if (regionalDao.excluir(itemSelecionado)) {
            itemSelecionado = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemExcluido"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroExcluir"));
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Regional getRegional() {
        return regional;
    }

    public void setRegional(Regional regional) {
        this.regional = regional;
    }

    public Regional getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Regional itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public LazyDataModel<Regional> getRegionais() {
        return regionais;
    }
}
