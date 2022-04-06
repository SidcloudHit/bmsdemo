/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notification;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ob_Department.pwdMgmt;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "resendSms", urlPatterns = {"/resendSms"})
public class resendSms extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            //Get User Deatils From tbl_notification table
            String user_id = "";
            String loc_emp_mobile = "", loc_emp_email = "", message = "", uid = "", loc_user_pwd = "", status="";
            if (request.getParameter("user_id") != null && request.getParameter("user_id") != "") {
                user_id = request.getParameter("user_id");
            }
            pwdMgmt pwd = new pwdMgmt();
            String user_details = pwd.getUserDetails(user_id);
            loc_emp_mobile = user_details.split("~")[0];
            loc_emp_email = user_details.split("~")[1];
            uid = user_details.split("~")[2];
            message = user_details.split("~")[3];
            status = user_details.split("~")[4];
            loc_user_pwd = "bms123";
            if (status.equals("1")) {
                if (!"".equals(loc_emp_mobile) && !"".equals(loc_emp_email)) {
                    String smsmsg = "You are registered successfully in bms.tripura.gov.in, Your USER ID is " + loc_emp_mobile + " and PASSWORD is " + loc_user_pwd + " to login. Please change your password. - BMS Portal Tripura";
                    try {
                        notify ntify = new notify();
                        ntify.notifysms(message, loc_emp_mobile, "1007161338744086219", "1001640530000017041");
                        ntify.notifyemail(message, loc_emp_email, "Registration in the BMS Portal");
                        // int updateUserDetails = smsmgmt.updateUserDetailsAfterSmsSend(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                out.println("2");
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
