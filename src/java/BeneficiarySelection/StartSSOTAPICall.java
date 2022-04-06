/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
@WebServlet(name = "StartSSOTAPICall", urlPatterns = {"/StartSSOTAPICall"})
public class StartSSOTAPICall extends HttpServlet {

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
            HttpSession session = request.getSession();
            CommonMethod cm = new CommonMethod();
            String userName = "", message = "", userId = "";
            int deptId = 0, officeId = 0, roleId = 0, schemeId = 0, installment = 0, batchId = 0;
            String fy = "", benificiary_count = "", scheme = "";
            //String user_id = "";
            String dept = "", dept_name = "", installment_name = "", scheme_name = "", schemeCode = "";
            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();
                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
                schemeId = Integer.parseInt(loginObj.getUserScheme());
            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }

            if (request.getParameter("rcvalidatefailedcasesapprover") != null) {
                if (request.getParameter("fy") != null) {
                    fy = SecurityClass.killchar(request.getParameter("fy"));
                }
                if (request.getParameter("installment") != null) {
                    installment = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("installment")));
                    //installment = SecurityClass.killchar(request.getParameter("installment"));
                }
                if (request.getParameter("installment_name") != null) {
                    installment_name = SecurityClass.killchar(request.getParameter("installment_name"));
                }

                schemeCode = cm.getSchemeCode(schemeId);
                String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump";

                Connection con = null;
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                CompletableFutureDemo CF = new CompletableFutureDemo();

                CF.ApiCallableFutureFailedCases(tabName, schemeId, schemeCode, fy, installment, batchId, deptId, dept_name, installment_name, userId); //, con

                out.print("API Call Initiated");

            }

            if (request.getParameter("rcvalidatefailedcasesmaker") != null) {
                if (request.getParameter("fy") != null) {
                    fy = SecurityClass.killchar(request.getParameter("fy"));
                }
                if (request.getParameter("installment") != null) {
                    installment = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("installment")));
                    //installment = SecurityClass.killchar(request.getParameter("installment"));
                }
                if (request.getParameter("installment_name") != null) {
                    installment_name = SecurityClass.killchar(request.getParameter("installment_name"));
                }

                schemeCode = cm.getSchemeCode(schemeId);
                String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump";

                Connection con = null;
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                CompletableFutureDemo CF = new CompletableFutureDemo();

                CF.ApiCallableFutureFailedCasesOfficeWise(tabName, schemeId, schemeCode, fy, installment, batchId, deptId, dept_name, installment_name, userId, officeId); //, con

                out.print("API Call Initiated");

            }
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
