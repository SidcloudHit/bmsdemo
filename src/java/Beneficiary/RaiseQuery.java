/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beneficiary;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "RaiseQuery", urlPatterns = {"/RaiseQuery"})
public class RaiseQuery extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession();
            String message = "";

            int deptId = 0, roleId = 0, officeId = 0, checkerOffice = 0;
            String userId = "", userName = "", userDes = "";
            String applicationId = "", applicantName = "", schemeCode = "", schemeName = "", actionRemarks = "";
            int schemeId = 0, action = 0;

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
                schemeId = Integer.parseInt(loginObj.getUserScheme());
                schemeName = loginObj.getUserSchemeName();

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }

            if (request.getParameter("raiseQuery") != null) {
                if (request.getParameter("applicationId") != null && !request.getParameter("applicationId").equals("")) {
                    applicationId = request.getParameter("applicationId");
                }

                if (request.getParameter("applicantName") != null && !request.getParameter("applicantName").equals("")) {
                    applicantName = request.getParameter("applicantName");
                }

                if (request.getParameter("schemeId") != null && !request.getParameter("schemeId").equals("")) {
                    String[] scheme = request.getParameter("schemeId").split("~");
                    //schemeId = Integer.parseInt(scheme[0]);
                    schemeCode = scheme[1];
                }

//                if (request.getParameter("schemeName") != null && !request.getParameter("schemeName").equals("")) {
//                    schemeName = request.getParameter("schemeName");
//                }
                if (request.getParameter("action") != null && !request.getParameter("action").equals("")) {
                    action = Integer.parseInt(request.getParameter("action"));
                }

                if (request.getParameter("actionRemarks") != null && !request.getParameter("actionRemarks").equals("")) {
                    actionRemarks = request.getParameter("actionRemarks");
                }
                
                if (request.getParameter("checkerOffice") != null && !request.getParameter("checkerOffice").equals("")) {
                    checkerOffice = Integer.parseInt(request.getParameter("checkerOffice"));
                }

                int result = raiseQuery(applicationId, applicantName, schemeId, schemeCode, schemeName, action, actionRemarks, userId, officeId, checkerOffice);

                out.print(result);
                out.flush();
                out.close();

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

    public int raiseQuery(String applicationId, String applicantName, int schemeId, String schemeCode, String schemeName, int action, String actionRemarks, String userId, int officeId, int checkerOffice) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            String query = "";
            int i = 1;
            ps = con.prepareCall("{call citizen.disapprove_application(?,?,?,?,?,?,?,?,?,?)}");
            ps.setString(i++, applicationId);
            ps.setString(i++, applicantName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, schemeCode);
            ps.setString(i++, schemeName);
            ps.setInt(i++, action);
            ps.setString(i++, actionRemarks);
            ps.setString(i++, userId);
            ps.setInt(i++, officeId);
            ps.setInt(i++, checkerOffice);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
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
        return result;
    }

}
