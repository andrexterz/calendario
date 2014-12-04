/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.UsuarioDao;
import br.ufg.calendario.models.Usuario;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author andre
 */
@Component
@Scope(value = "session")
public class UsuarioBean {

    private Usuario usuario;
    private Usuario itemSelecionado;
    private Usuario sessionUsuario;
    private boolean autenticado;

    @Autowired
    private transient UsuarioDao usuarioDao;

    public UsuarioBean() {
        usuario = null;
    }

    public void autentica() throws NoSuchAlgorithmException {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> loginParameters = context.getExternalContext().getRequestParameterMap();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String login = loginParameters.get("loginForm:login");
        messageDigest.update(loginParameters.get("loginForm:senha").getBytes());
        String senha = new BigInteger(1, messageDigest.digest()).toString(16);
        sessionUsuario = usuarioDao.buscarPorLogin(login);
        if (sessionUsuario != null && sessionUsuario.getSenha().equals(senha)) {
            autenticado = true;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isAutenticado() {
        return autenticado;
    }
}
