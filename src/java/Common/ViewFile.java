/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author acer
 */
@WebServlet(name = "ViewFile", urlPatterns = {"/ViewFile"})
public class ViewFile extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession session = request.getSession();
            String ctype = "", user_id = "", message = "", fileUid = "";
            int fId = 0;
            byte[] imgBytes = null;
            /*if (session.getAttribute("user_id") != null) {
               user_id = session.getAttribute("user_id").toString();
            } else {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }*/
            //user_id = "test123456";
            
            if (request.getParameter("fId") != null) {
                fId = Integer.parseInt(request.getParameter("fId"));
            }

            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            con = new DBCon.DBSource().connectToAgriDbtFilesDB().getConnection();
            try {
                ps = con.prepareStatement("SELECT user_id, file_name, file_mime, file_size, file_status, file_data, file_timestamp  FROM mas_files WHERE fid = " + fId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    //fileUid = rs.getString("user_id");
                    //if(fileUid.equals(user_id)){
                        ctype = rs.getString("file_mime");
                        imgBytes = rs.getBytes("file_data"); 
                    //}                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(ViewFile.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
            }

//            ctype = app.getQual_marksheet_ctype();
//            imgBytes = app.getQual_marksheet();
            response.setContentType(ctype);
            response.getOutputStream().write(imgBytes);

            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
