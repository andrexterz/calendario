/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.dao;

import br.ufg.calendario.models.Usuario;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author andre
 */

@Repository
@Scope(value = "singleton")
public class UsuarioDao {
    
    @Transactional
    public boolean adicionar(Usuario usuario) {
        return false;
    }
    
    @Transactional
    public boolean atualizar(Usuario usuario) {
        return false;
    }
    
    @Transactional
    public boolean excluir(Usuario usuario) {
        return false;
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return null;
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorLogin(String login) {
        return null;
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return null;
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> listar(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        return null;
    }
    
    @Transactional(readOnly = true)
    public int rowCount() {
        return 0;
    }
    
    @Transactional(readOnly = true)
    public int rowCount(Map<String, Object> filters) {
        return 0;
    }
    
}
