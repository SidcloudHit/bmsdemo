/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchemeManagement;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import log.errorlog;

/**
 *
 * @author acer
 */
@WebServlet(name = "saveSchemeManagementData", urlPatterns = {"/saveSchemeManagementData"})
public class saveSchemeManagementData extends HttpServlet {

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
        HttpSession session = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            String userId = "", userName = "", userDes = "", changeSchemeDate = "", rolewisedatecheck = "";
            if (request.getParameter("changeSchemeDate") != null && !"".equals(request.getParameter("changeSchemeDate"))) {
                changeSchemeDate = SecurityClass.killchar(request.getParameter("changeSchemeDate"));
            }
            if (request.getParameter("rolewisedatecheck") != null && !"".equals(request.getParameter("rolewisedatecheck"))) {
                rolewisedatecheck = SecurityClass.killchar(request.getParameter("rolewisedatecheck"));
            }
            Common.CommonMethod cm = new CommonMethod();
            SchemeManagement.manageSchemes ms = new manageSchemes();
            errorlog log = new errorlog();
            Connection con = null;

            String status = "";
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                if (changeSchemeDate.equals("1")) {
                    int deptId = 0, schemeId = 0;
                    String txtApplDate = "", txtVeriDate = "", txtApprDate = "";
                    if (request.getParameter("scheme_id") != null && !"".equals(request.getParameter("scheme_id"))) {
                        schemeId = Integer.parseInt(request.getParameter("scheme_id"));
                    }
                    if (request.getParameter("dept_id") != null && !"".equals(request.getParameter("dept_id"))) {
                        deptId = Integer.parseInt(request.getParameter("dept_id"));
                    }
                    if (request.getParameter("txtApplDate") != null && !"".equals(request.getParameter("txtApplDate"))) {
                        txtApplDate = SecurityClass.killchar(request.getParameter("txtApplDate"));
                    }
                    if (request.getParameter("txtVeriDate") != null && !"".equals(request.getParameter("txtVeriDate"))) {
                        txtVeriDate = SecurityClass.killchar(request.getParameter("txtVeriDate"));
                    }
                    if (request.getParameter("txtApprDate") != null && !"".equals(request.getParameter("txtApprDate"))) {
                        txtApprDate = SecurityClass.killchar(request.getParameter("txtApprDate"));
                    }
                    int onboarding_id = cm.getOnboardedSchemeId(schemeId);
                    status = ms.changeSchemeManagementDate(onboarding_id, deptId, txtApplDate, txtVeriDate, txtApprDate, con);
                }
                if (rolewisedatecheck.equals("1")) {
                    int deptId = 0, schemeId = 0, role = 0;
                    String scheme_id = "", txtVeriDate = "", txtApprDate = "";
                    if (request.getParameter("scheme_id") != null && !"".equals(request.getParameter("scheme_id"))) {
                        schemeId = Integer.parseInt(request.getParameter("scheme_id"));
                    }
                    if (request.getParameter("dept_id") != null && !"".equals(request.getParameter("dept_id"))) {
                        deptId = Integer.parseInt(request.getParameter("dept_id"));
                    }
                    if (request.getParameter("roleId") != null && !"".equals(request.getParameter("roleId"))) {
                        role = Integer.parseInt(request.getParameter("roleId"));
                        
                    }
                    int onboarding_id = cm.getOnboardedSchemeId(schemeId);
                    status = ms.checkUserWorkingPeriod(onboarding_id, deptId, role, con);
                }
            } catch (Exception e) {
                e.getMessage();
                log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.getMessage();
                }

            }
            out.print(status);
            out.close();
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
