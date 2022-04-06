/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.CaptchaGen;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * @author Banti
 */
@WebServlet(name = "forgotChangePassword", urlPatterns = {"/forgotChangePassword"})
public class forgotChangePassword extends HttpServlet {

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
        HttpSession session = request.getSession(true);
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String user_id = "", user_email = "", mobile = "", dob = "", old_password = "", user_password = "", message = "";
            CommonMethod cm = new CommonMethod();
            String cptimg2 = "";
            int uType = 0;

             // session = request.getSession();
             if (session.getAttribute("captcha_solution") != null) {
                cptimg2 = (String) session.getAttribute("captcha_solution");
            }
            //String cptimg2 = (String) session.getAttribute(captchagen.CAPTCHA_KEY);
            String cptval2 = request.getParameter("txtLoginCaptcha");
            if (!cptimg2.equalsIgnoreCase(cptval2)) {
                message = "Wrong Verification Code";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("index.jsp");
                return;
            }

            if (request.getParameter("btnChangePassword") != null) {
                // <editor-fold defaultstate="collapsed" desc="Server Side Validation check methods. Click on the + sign on the left to edit the code.">
                /* server side validation part start */

                if (request.getParameter("user_email") != null) {
                    if (request.getParameter("user_email").trim().length() > 0) {
                        user_email = request.getParameter("user_email");
                        user_email = SecurityClass.killchar(user_email);
                    } else {
                        // user_name = "";
                        message = "Some error occured ! Please try again.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassChange.jsp");
                        return;
                    }
                } else {
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassChange.jsp");
                    return;
                }

                if (request.getParameter("txtPwd") != null) {
                    if (request.getParameter("txtPwd").trim().length() > 0) {
                        user_password = request.getParameter("txtPwd");
                        user_password = SecurityClass.killchar(user_password);
                    } else {
                        // pwd = "";
                        message = "Password can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassChange.jsp");
                        return;
                    }
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassChange.jsp");
                    return;
                }

                if (request.getParameter("txtCnfPwd") != null) {
                    if (request.getParameter("txtCnfPwd").trim().length() > 0) {
                        if (!user_password.equals(SecurityClass.killchar(request.getParameter("txtCnfPwd")))) {
                            // password != confirm password;
                            message = "Password and Confirm Password are not same .";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("User/forgotPassChange.jsp");
                            return;
                        }
                    } else {
                        // email = "";
                        message = "Password can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassChange.jsp");
                        return;
                    }
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassChange.jsp");
                    return;
                }

                /* server side validation part end */
                // </editor-fold>
                if (resetPassword(user_email, user_password)) {
                    message = "Password Updated";
//                    session.invalidate();
//                    session = request.getSession(true);
                   // session.setAttribute("pwdexp", "");
                   // session.setAttribute("pwdexpday", "");
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("User/forgotPassChange.jsp");

//                        else if (uType == 2) {
//                            response.sendRedirect("Verification/changePassword.jsp");
//                        } else if (uType == 3) {
//                            response.sendRedirect("MISUser/changePassword.jsp");
//                        }
                } else {
                    message = "Password change failed";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");

                    response.sendRedirect("User/forgotPassChange.jsp");

//                        else if (uType == 2) {
//                            response.sendRedirect("Verification/changePassword.jsp");
//                        } else if (uType == 3) {
//                            response.sendRedirect("MISUser/changePassword.jsp");
//                        }
                    return;
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

    public boolean resetPassword(String user_email, String user_password) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        
        String tabName = "";

//        if (uType == 0) {
//            tabName = "mas_user_account";
//        } else if (uType == 1 || uType == 2) {
        tabName = "bms_user.mas_dept_users";
//        }

        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            SecurityClass sc = new SecurityClass();
            String hex_password = sc.gethexString(user_password);

            ps = con.prepareStatement("UPDATE " + tabName + " SET user_password = '" + hex_password + "', remarks = '"+user_password+"' WHERE emp_email = '" + user_email + "'");
           // ps1 = con.prepareStatement("INSERT INTO public.tbl_password_log(email, reason, user_password,entry_datetime)VALUES ('" + user_email + "','Forgot Password change' , '" + hex_password + "',CURRENT_TIMESTAMP)");
            int i = ps.executeUpdate();

            if (i > 0) {
                stat = true;
                
               // ps1.executeUpdate();
            }
        } catch (Exception ex) {
            Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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

        return stat;
    }
}
