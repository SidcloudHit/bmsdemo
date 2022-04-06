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
import java.sql.Date;
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
@WebServlet(name = "setPassword", urlPatterns = {"/setPassword"})
public class setPassword extends HttpServlet {

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
        HttpSession session = request.getSession(true);
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String uType = "", user_id = "", email = "", mobile = "", dob = "", old_password = "", user_password = "", message = "";
            CommonMethod cm = new CommonMethod();
            String cptimg2 = "";

            // session = request.getSession();
            if (session.getAttribute(CaptchaGen.CAPTCHA_KEY) != null) {
                cptimg2 = (String) session.getAttribute(CaptchaGen.CAPTCHA_KEY);
            }
            //String cptimg2 = (String) session.getAttribute(captchagen.CAPTCHA_KEY);
            String cptval2 = request.getParameter("txtLoginCaptcha");

            if (!cptimg2.equalsIgnoreCase(cptval2)) {
                message = "Wrong Verification Code";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                if ("admin_user".equals(uType)) {
                    response.sendRedirect("adminLogin.jsp");
                } else {
                    response.sendRedirect("login.jsp");
                }
                return;
            }

            // <editor-fold defaultstate="collapsed" desc="Server Side Validation check methods. Click on the + sign on the left to edit the code.">
            /* server side validation part start */
            if (request.getParameter("txtUid") != null) {
                if (request.getParameter("txtUid").trim().length() > 0) {
                    user_id = request.getParameter("txtUid");
                    user_id = SecurityClass.killchar(user_id);
                } else {
                    // user_name = "";
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/setPassword.jsp");
                    return;
                }
            } else {
                message = "Some error occured ! Please try again.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("User/setPassword.jsp");
                return;
            }

            if (request.getParameter("txtUType") != null) {
                if (request.getParameter("txtUType").trim().length() > 0) {
                    uType = SecurityClass.killchar(request.getParameter("txtUType"));
                } else {
                    // user_name = "";
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/setPassword.jsp");
                    return;
                }
            } else {
                message = "Some error occured ! Please try again.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("User/setPassword.jsp");
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
                    response.sendRedirect("User/changePassword.jsp");
                    return;
                }
            } else {
                message = "Password can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("User/changePassword.jsp");
                return;
            }

            if (request.getParameter("txtCnfPwd") != null) {
                if (request.getParameter("txtCnfPwd").trim().length() > 0) {
                    if (!user_password.equals(SecurityClass.killchar(request.getParameter("txtCnfPwd")))) {
                        // password != confirm password;
                        message = "Password and Confirm Password does not match .";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/changePassword.jsp");
                        return;
                    }
                } else {
                    // email = "";
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/changePassword.jsp");
                    return;
                }
            } else {
                message = "Password can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("User/changePassword.jsp");
                return;
            }

            if (request.getParameter("btnResetPassword") != null) {
                // <editor-fold defaultstate="collapsed" desc="Server Side Validation check methods. Click on the + sign on the left to edit the code.">
                /* server side validation part start */
                if (request.getParameter("txtUid") != null) {
                    if (request.getParameter("txtUid").trim().length() > 0) {
                        user_id = request.getParameter("txtUid");
                        user_id = SecurityClass.killchar(user_id);
                    } else {
                        // user_name = "";
                        message = "Some error occured ! Please try again.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassword.jsp");
                        return;
                    }
                } else {
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
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
                        response.sendRedirect("User/setPassword.jsp");
                        return;
                    }
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/setPassword.jsp");
                    return;
                }

                if (request.getParameter("txtCnfPwd") != null) {
                    if (request.getParameter("txtCnfPwd").trim().length() > 0) {
                        if (!user_password.equals(SecurityClass.killchar(request.getParameter("txtCnfPwd")))) {
                            // password != confirm password;
                            message = "Password and Confirm Password does not match .";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("User/setPassword.jsp");
                            return;
                        }
                    } else {
                        // email = "";
                        message = "Password can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/setPassword.jspp");
                        return;
                    }
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/setPassword.jsp");
                    return;
                }

                /* server side validation part end */
                // </editor-fold>
                if (resetPassword(user_id, user_password, uType)) {
                    String login_url="";
                    if ("admin_user".equals(uType)) {
                        login_url=request.getContextPath() + "/adminLogin.jsp";
                    } else {
                         login_url=request.getContextPath() + "/login.jsp";
                    }
                    session.invalidate();
                    session = request.getSession(true);
                    message = "Password reset is successful. Click <a href=" + login_url + ">here</a> for Login ";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("User/setPassword.jsp");
                    return;

                } else {
                    message = "Password reset failed";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/setPassword.jsp");
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

    public boolean resetPassword(String user_id, String user_password, String uType) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String tabName = "";

//        if (uType == 0) {
//            tabName = "mas_user_account";
//        } else if (uType == 1 || uType == 2) {
        if ("admin_user".equals(uType)) {
            tabName = "bms_user.mas_admin_users";
        } else {
            tabName = "bms_user.mas_dept_users";
        }

        try {
            SecurityClass sc = new SecurityClass();
            String hex_password = sc.gethexString(user_password);

            ps = con.prepareStatement("UPDATE " + tabName + " SET user_password = '" + hex_password + "', remarks = '" + user_password + "', password_expiry_date = (now() + '120 days'::interval),first_login=1 WHERE user_id = '" + user_id + "'");
            int i = ps.executeUpdate();
            if (i > 0) {
                stat = true;
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
