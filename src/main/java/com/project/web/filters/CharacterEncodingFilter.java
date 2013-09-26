package com.project.web.filters;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 11/12/12
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class CharacterEncodingFilter implements Filter {

        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
                throws IOException, ServletException {
                req.setCharacterEncoding("UTF-8");
                resp.setCharacterEncoding("UTF-8");
                chain.doFilter(req, resp);
        }

        public void init(FilterConfig filterConfig) throws ServletException {

        }

        public void destroy() {

        }


}