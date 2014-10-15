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
                System.out.println("");
                data = calendarioDao.listar(first, pageSize, "ano", "DESCENDING", null);
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
        System.out.println("objeto criado");
        calendario = new Calendario();
    }

    public void editar() {
        calendario = itemSelecionado;
    }

    public void salvar() {
        if (calendario.getId() == null) {
            calendarioDao.adicionar(calendario);
        } else {
            calendarioDao.atualizar(calendario);
        }
    }

    public void excluir() {
        System.out.println("excluindo registro: " + itemSelecionado.getId());
        calendarioDao.excluir(itemSelecionado);
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
