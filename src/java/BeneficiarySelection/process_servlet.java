/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author acer
 */
@WebServlet(name = "process_servlet", urlPatterns = {"/process_servlet"})
public class process_servlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            
            
            String operationtype = request.getParameter("operationtype");
            Connection con = null;
            PreparedStatement ps = null;
            CallableStatement cs = null;
            if (operationtype.equals("lgddetails")) {
                String result = "null";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    try {
                        
                        String query = "SELECT to_json(array_agg(lgd_details)) as lgd_details_json FROM (SELECT distinct dist_code, dist_name, subdiv_code, subdiv_name, block_code, block_name  FROM lgd.master_dist_sd_block_bac_gp) lgd_details;";
                        ps = con.prepareStatement(query);
//                        ps.setString(1, chequeno);
//                        ps.setString(2, bankname);
                        ResultSet rs = null;
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            result = rs.getString("lgd_details_json");
                        }
                        rs.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    out.print(result);
                    try {
                        if (con != null) {
                            con.close();
                        }
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (Exception e) {

                    }
                }
            }
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
