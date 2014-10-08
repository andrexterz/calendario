/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.components;

import br.ufg.calendario.dao.RegionalDao;
import br.ufg.calendario.models.Regional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author André
 */


@Component
@Scope(value = "session")
public class RegionalBean {
    
    
    @Autowired
    RegionalDao regionalDao;
   
    private Regional regional;
    private Regional itemSelecionado;

    public RegionalBean() {
        regional = new Regional();
        itemSelecionado = null;
    }
    
    public void adicionar() {
        regional = new Regional();
    }
    
    
    public void salvar() {
        //adicionar validador
        regionalDao.adicionar(regional);
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
}
