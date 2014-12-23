/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.calendario.filters;

import br.ufg.calendario.components.UsuarioBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Andre Luiz Fernandes Ribeiro Barca
 */
public class AuthFilter implements Filter {
    
    private UsuarioBean usuarioBean;
    private final Logger logger = Logger.getLogger("AuthFilter");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String path = httpRequest.getContextPath();
        try {
            usuarioBean = (UsuarioBean) session.getAttribute("usuarioBean");
            if (usuarioBean != null && usuarioBean.isAutenticado()){
                chain.doFilter(request, response);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (NullPointerException e) {
            logger.error(e.getLocalizedMessage());
            
        }
    }

    @Override
    public void destroy() {
    }
    
}
