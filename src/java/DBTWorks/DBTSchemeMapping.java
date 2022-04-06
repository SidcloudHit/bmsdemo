/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "DBTSchemeMapping", asyncSupported = true, urlPatterns = {"/DBTSchemeMapping"})
public class DBTSchemeMapping extends HttpServlet {

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
            String msg = "";
            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];
                String myJsonData = request.getParameter("json");
                myJsonData = myJsonData.replace("[", "");
                myJsonData = myJsonData.replace("]", "");
                String[] bids = myJsonData.split(",");

                Integer[] benids = new Integer[bids.length];

                for (int i = 0; i < bids.length; i++) {
                    benids[i] = Integer.parseInt(bids[i]);
                }
                try {
                    CallableStatement cs = null;
                    Connection con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    Array dArray = con.createArrayOf("BIGINT", benids);
                    cs = con.prepareCall("{call map_scheme_beneficiary(?,?)}");
                    cs.setArray(1, dArray);
                    cs.setInt(2, schemeId);
                    if (cs.execute()) {
                        ResultSet rs = cs.getResultSet();
                        while (rs.next()) {
                            msg = rs.getString(1);//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            out.print(msg);
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
