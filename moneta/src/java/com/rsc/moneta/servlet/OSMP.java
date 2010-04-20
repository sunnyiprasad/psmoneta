package com.rsc.moneta.servlet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.rsc.moneta.module.InputHandler;
import com.rsc.moneta.module.inputhandler.InputHandlerConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sulic
 */
public class OSMP extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            String login = this.getInitParameter("login");
            String password = this.getInitParameter("password");
            String pem = this.getInitParameter("pem");
            String ip = this.getInitParameter("ip");
            String handlerClass = this.getInitParameter("handler");            
            InputHandlerConfig config = new InputHandlerConfig();
            config.setLogin(login);
            config.setPassword(password);
            config.setIp(ip);
            config.setHandler(handlerClass);
            config.setPem(pem);
            try {
                InputHandler handler = (InputHandler) this.getClass().getClassLoader().loadClass(handlerClass).newInstance();
                handler.setConfig(config);
                String xml = handler.check(request.getParameterMap());
                out.write(xml);
            } catch (ClassNotFoundException exception) {
                out.write("Cannot find class handler");
                exception.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(OSMP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
