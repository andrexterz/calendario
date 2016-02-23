/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.InteressadoDao;
import br.ufg.calendario.models.Interessado;
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
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
@Component
@Scope(value = "session")
public class InteressadoBean implements Serializable {

    @Autowired
    private transient InteressadoDao interessadoDao;

    private Interessado interessado;

    private Interessado itemSelecionado;
    
    private String termo;

    private final LazyDataModel<Interessado> interessados;

    public InteressadoBean() {
        interessado = new Interessado();
        itemSelecionado = null;
        termo = null;
        interessados = new LazyDataModel<Interessado>() {

            private List<Interessado> data;

            @Override
            public Interessado getRowData(String rowKey) {
                for (Interessado inter : data) {
                    if (inter.getId().equals(Long.parseLong(rowKey))) {
                        return inter;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Interessado inter) {
                return inter.getId().toString();
            }

            @Override
            public List<Interessado> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                data = interessadoDao.listar(first, pageSize, sortField, sortOrder.name(), filters);
                setPageSize(pageSize);
                if (termo == null || termo.isEmpty()) {
                    setRowCount(interessadoDao.rowCount());
                } else {
                    setRowCount(interessadoDao.rowCount(termo));
                }
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
        interessado = new Interessado();
    }
    
    public void editar () {
        if (itemSelecionado != null) {
            interessado = itemSelecionado;
        }
    }

    public void salvar() {
        FacesMessage msg;
        boolean saveStatus;
        if (interessado.getId() == null) {
            saveStatus = interessadoDao.adicionar(interessado);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemSalvo"));
        } else {
            saveStatus = interessadoDao.atualizar(interessado);
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
        if (itemSelecionado != null) {
            interessadoDao.excluir(itemSelecionado);
            itemSelecionado = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemExcluido"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroExcluir"));
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Interessado getInteressado() {
        return interessado;
    }

    public void setInteressado(Interessado interessado) {
        this.interessado = interessado;
    }

    public Interessado getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Interessado itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }
    
    public LazyDataModel<Interessado> getInteressados() {
        return interessados;
    }
    
    public List<Interessado> getAllInteressados() {
        return interessadoDao.listar();
    }
}
