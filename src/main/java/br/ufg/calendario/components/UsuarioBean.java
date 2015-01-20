/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.calendario.components;

import br.ufg.calendario.dao.UsuarioDao;
import br.ufg.calendario.models.PerfilEnum;
import br.ufg.calendario.models.Usuario;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
public class UsuarioBean implements Serializable {

    private String termoBusca;
    private PerfilEnum buscaPerfil;
    private Usuario usuario;
    private PerfilEnum selecaoPerfil;
    private Usuario itemSelecionado;
    private Usuario sessionUsuario;
    private boolean autenticado;
    private final LazyDataModel<Usuario> usuarios;

    @Autowired
    private transient UsuarioDao usuarioDao;

    @Autowired
    Validator validator;

    public UsuarioBean() {
        usuario = null;
        itemSelecionado = null;
        sessionUsuario = null;
        autenticado = false;
        usuarios = new LazyDataModel<Usuario>() {
            private List<Usuario> data;

            @Override
            public Object getRowKey(Usuario object) {
                return object.getId().toString();
            }

            @Override
            public Usuario getRowData(String rowKey) {
                for (Usuario u : data) {
                    if (u.getId().toString().equals(rowKey)) {
                        return u;
                    }
                }
                return null;
            }

            @Override
            public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                //reset primefaces filter
                filters = new HashMap();
                setPageSize(pageSize);

                if (getTermoBusca() != null && !getTermoBusca().isEmpty()) {
                    System.out.println("termo: " + getTermoBusca());
                    filters.put("termo", getTermoBusca());
                }

                if (getBuscaPerfil() != null) {
                    filters.put("perfil", getBuscaPerfil());
                }

                if (!filters.isEmpty()) {
                    setRowCount(usuarioDao.rowCount(filters));
                } else {
                    setRowCount(usuarioDao.rowCount());
                }
                data = usuarioDao.listar(first, pageSize, sortField, sortOrder == null ? null : sortOrder.name(), filters);
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

    public String autentica() throws NoSuchAlgorithmException {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> loginParameters = context.getExternalContext().getRequestParameterMap();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String login = loginParameters.get("loginForm:login");
        messageDigest.update(loginParameters.get("loginForm:senha").getBytes());
        String senha = new BigInteger(1, messageDigest.digest()).toString(16);
        sessionUsuario = usuarioDao.buscarPorLogin(login);
        //if (sessionUsuario != null && sessionUsuario.getSenha().equals(senha))
        if (true) {
            autenticado = true;
            return "/views/admin/usuarios?faces-redirect=true";
        } else {
            return null;
        }
    }

    public String cadastrar() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> cadastroParameters = context.getExternalContext().getRequestParameterMap();
        String nome = cadastroParameters.get("cadastraUsuarioForm:nomeUsuario");
        String login = cadastroParameters.get("cadastraUsuarioForm:loginUsuario");
        String email = cadastroParameters.get("cadastraUsuarioForm:emailUsuario");
        String senhaA = cadastroParameters.get("cadastraUsuarioForm:senhaUsuarioA");
        String senhaB = cadastroParameters.get("cadastraUsuarioForm:senhaUsuarioB");
        System.out.format("nome: %s,\n login: %s,\nsenhaA: %s,\nsenhaB: %s\n", nome, login, senhaA, senhaB);
        FacesMessage msg;
        boolean result = false;// return result? "/views/cadastroEfetivado": "/views/cadastraUsuario";
        boolean usuarioExiste = usuarioDao.buscarPorLogin(login.trim()) != null;
        Usuario u = new Usuario(nome, login, email, senhaA, null, false);
        Set<ConstraintViolation<Usuario>> errors = validator.validate(u);
        if (usuarioExiste) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", LocaleBean.getMessage("usuarioIndisponivel"));
            context.addMessage(null, msg);
        } else {
            if (errors.isEmpty()) {
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("cadastroEfetivado"));
                context.addMessage(null, msg);
            } else {
                for (ConstraintViolation<Usuario> v : errors) {
                    String errorMessage = String.format("%s: %s", v.getPropertyPath().toString(), v.getMessage());
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", errorMessage);
                    context.addMessage(null, msg);
                }
            }
        }
        return "/views/cadastraUsuario";
    }

    public void adicionar() {
        usuario = new Usuario();
        setSelecaoPerfil(null);
    }

    public void editar() {
        if (itemSelecionado != null) {
            usuario = itemSelecionado;
            setSelecaoPerfil(null);
        }
    }

    public void salvar() {
        //inserir validador
        FacesMessage msg;
        boolean saveStatus;
        if (usuario.getId() == null) {
            saveStatus = usuarioDao.adicionar(usuario);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemSalvo"));
        } else {
            saveStatus = usuarioDao.atualizar(usuario);
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
        boolean saveStatus = usuarioDao.excluir(itemSelecionado);
        if (saveStatus) {
            itemSelecionado = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", LocaleBean.getMessage("itemExcluido"));
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "info", LocaleBean.getMessage("erroExcluir"));
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("resultado", saveStatus);
    }

    public void adicionaPerfil() {
        System.out.println("perfil: " + getSelecaoPerfil());
        if (!usuario.getPerfil().contains(getSelecaoPerfil()) && getSelecaoPerfil() != null) {
            usuario.addPerfil(getSelecaoPerfil());
        }
    }

    public void removePerfil() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        PerfilEnum perfil = (PerfilEnum) requestMap.get("perfil");
        usuario.removePerfil(perfil);
    }

    public String getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }

    public PerfilEnum getBuscaPerfil() {
        return buscaPerfil;
    }

    public void setBuscaPerfil(PerfilEnum buscaPerfil) {
        this.buscaPerfil = buscaPerfil;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PerfilEnum getSelecaoPerfil() {
        return selecaoPerfil;
    }

    public void setSelecaoPerfil(PerfilEnum selecaoPerfil) {
        this.selecaoPerfil = selecaoPerfil;
    }

    public Usuario getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(Usuario itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public Usuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(Usuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public LazyDataModel<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<PerfilEnum> getPerfilList() {
        return Arrays.asList(PerfilEnum.values());
    }

}
