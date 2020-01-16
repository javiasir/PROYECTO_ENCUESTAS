/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encuestas.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import encuestas.ejb.PreguntaFacade;
import encuestas.ejb.RespuestaFacade;
import encuestas.entity.Pregunta;
import encuestas.entity.Respuesta;

/**
 *
 * @author luis
 */
@WebServlet(name = "ServletGuardarPregunta", urlPatterns = {"/ServletGuardarPregunta"})
public class ServletGuardarPregunta extends HttpServlet {

    @EJB
    private RespuestaFacade respuestaFacade;

    @EJB
    private PreguntaFacade preguntaFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String es_multiple = request.getParameter("tipo");
        String id_encuesta = request.getParameter("id_encuesta");
        
        Pregunta pregunta = this.preguntaFacade.find(new Integer(id));
        pregunta.setTituloPregunta(titulo);
        pregunta.setEsMultiple(Boolean.valueOf(es_multiple));
        
        if(!pregunta.getEsMultiple()){
            List<Respuesta> respuestas = pregunta.getRespuestaList();
            for(Respuesta respuesta : respuestas){
                
                this.respuestaFacade.remove(respuesta);
            }
            
            Respuesta si = new Respuesta(new Integer("1"));
            Respuesta no = new Respuesta(new Integer("1"));
            si.setIdPregunta(pregunta);
            si.setRespuesta("Sí");
            no.setIdPregunta(pregunta);
            no.setRespuesta("No");
            
            this.respuestaFacade.create(si);
            this.respuestaFacade.create(no);
            
            List<Respuesta> lista_respuesta = new ArrayList<>();
            lista_respuesta.add(no);
            lista_respuesta.add(si);
            
            
            pregunta.setRespuestaList(lista_respuesta);
        }
        
        this.preguntaFacade.edit(pregunta);
        
        RequestDispatcher rd = request.getRequestDispatcher("ServletRedireccionarPreguntas?id="+id_encuesta);
        rd.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
