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
 * @author NIC-Arindam
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/ChangePassword"})
public class ChangePassword extends HttpServlet {

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
        HttpSession session = request.getSession(true);
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String user_id = "", email = "", mobile = "", dob = "", old_password = "", user_password = "", message = "";
            CommonMethod cm = new CommonMethod();
            String cptimg2 = "";
            int uType = 0;

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
                response.sendRedirect("login.jsp");
                return;
            }

            if (request.getParameter("btnChangePassword") != null) {
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
                        response.sendRedirect("User/changePassword.jsp");
                        return;
                    }
                } else {
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/changePassword.jsp");
                    return;
                }

                if (request.getParameter("txtUType") != null) {
                    if (request.getParameter("txtUType").trim().length() > 0) {
                        uType = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtUType")));
                    } else {
                        // user_name = "";
                        message = "Some error occured ! Please try again.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/changePassword.jsp");
                        return;
                    }
                } else {
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/changePassword.jsp");
                    return;
                }

                if (request.getParameter("txtOldPwd") != null) {
                    if (request.getParameter("txtOldPwd").trim().length() > 0) {
                        old_password = request.getParameter("txtOldPwd");
                        old_password = SecurityClass.killchar(old_password);
                    } else {
                        // pwd = "";
                        message = "Old Password can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/changePassword.jsp");
                        return;
                    }
                } else {
                    message = "Old Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/changePassword.jsp");
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

                /* server side validation part end */
                // </editor-fold>
                if (isOldPasswordMatch(user_id, old_password, uType)) {
                    if (resetPassword(user_id, user_password, uType)) {

                        String login_url = "";
                        if (uType == 1 || uType == 2) {
                            login_url = request.getContextPath() + "/adminLogin.jsp";
                        } else if (uType == 4 || uType == 5 || uType == 6) {
                            login_url = request.getContextPath() + "/login.jsp";
                        }
                        session.invalidate();
                        session = request.getSession(true);
                        message = "Password changed successfully. Click <a href=" + login_url + ">here</a> for Login";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
//                        if (uType == 1) {
//                            response.sendRedirect("Admin/changePassword.jsp");
//                        } else if (uType > 1) {
                        response.sendRedirect("login.jsp");
//                        }

//                        else if (uType == 2) {
//                            response.sendRedirect("Verification/changePassword.jsp");
//                        } else if (uType == 3) {
//                            response.sendRedirect("MISUser/changePassword.jsp");
//                        }
                    } else {
                        message = "Password change failed";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
//                        if (uType == 1) {
//                            response.sendRedirect("Admin/changePassword.jsp");
//                        } else if (uType > 1) {
                        response.sendRedirect("User/changePassword.jsp");
//                        }

//                        else if (uType == 2) {
//                            response.sendRedirect("Verification/changePassword.jsp");
//                        } else if (uType == 3) {
//                            response.sendRedirect("MISUser/changePassword.jsp");
//                        }
                        return;
                    }
                } else {
                    message = "Old password does not match";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
//                    if (uType == 1) {
//                        response.sendRedirect("Admin/changePassword.jsp");
//                    } else if (uType > 1) {
                    response.sendRedirect("User/changePassword.jsp");
//                    }

//                    else if (uType == 2) {
//                        response.sendRedirect("Verification/changePassword.jsp");
//                    } else if (uType == 3) {
//                        response.sendRedirect("MISUser/changePassword.jsp");
//                    }
                    return;
                }

            }

            if (request.getParameter("btnForgotPwdVerify") != null) {
                // <editor-fold defaultstate="collapsed" desc="Server Side Validation check methods. Click on the + sign on the left to edit the code.">
                /* server side validation part start */
                if (request.getParameter("txtUserRegNo") != null) {
                    if (request.getParameter("txtUserRegNo").trim().length() > 0) {
                        user_id = request.getParameter("txtUserRegNo");
                        user_id = SecurityClass.killchar(user_id);
                    } else {
                        // user_name = "";
                        message = "Registration Number can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassword.jsp");
                        return;
                    }
                } else {
                    message = "Registration Number can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
                }

                if (request.getParameter("txtEmail") != null) {
                    if (request.getParameter("txtEmail").trim().length() > 0) {
                        if (cm.emailValidator(request.getParameter("txtEmail"))) {
                            email = request.getParameter("txtEmail");
                            email = SecurityClass.killchar(email);
                        } else {
                            // email = invalid email;
                            message = "Email ID is not valid.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("User/forgotPassword.jsp");
                            return;
                        }
                    } else {
                        // email = "";
                        message = "Email ID can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassword.jsp");
                        return;
                    }
                } else {
                    message = "Email ID can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
                }

                if (request.getParameter("txtMobile") != null) {
                    if (request.getParameter("txtMobile").trim().length() > 0) {
                        if (cm.mobileValidator(request.getParameter("txtMobile"))) {
                            mobile = request.getParameter("txtMobile");
                            mobile = SecurityClass.killchar(mobile);
                        } else {
                            // mobile = invalid mobile;
                            message = "Mobile Number is not valid.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("User/forgotPassword.jsp");
                            return;
                        }
                    } else {
                        // mobile = "";
                        message = "Mobile Number can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassword.jsp");
                        return;
                    }
                } else {
                    message = "Mobile Number can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
                }

                if (request.getParameter("txtDateOfBirth") != null) {
                    if (request.getParameter("txtDateOfBirth").trim().length() > 0) {
                        if (cm.checkDateGtOrEqToCurrDate(request.getParameter("txtDateOfBirth"), 1, 0, 1)) {
                            dob = request.getParameter("txtDateOfBirth");
                            dob = SecurityClass.killchar(dob);
                        } else {
                            // dob >= today;
                            message = "Date of Birth cannot be equal to or greater than today.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("User/forgotPassword.jsp");
                            return;
                        }
                    } else {
                        // dob = "";
                        message = "Date of Birth can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/forgotPassword.jsp");
                        return;
                    }
                } else {
                    message = "Date of Birth can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
                }


                /* server side validation part end */
                // </editor-fold> 
                if (verifyForgotPassword(user_id, email, mobile, dob)) {
                    message = user_id;
                    session.setAttribute("uId", message);
                    response.sendRedirect("User/resetPassword.jsp");
                } else {
                    message = "Provided informations does not match with our record";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
                }
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

                if (request.getParameter("txtUType") != null) {
                    if (request.getParameter("txtUType").trim().length() > 0) {
                        uType = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtUType")));
                    } else {
                        // user_name = "";
                        message = "Some error occured ! Please try again.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/changePassword.jsp");
                        return;
                    }
                } else {
                    message = "Some error occured ! Please try again.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/changePassword.jsp");
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
                        response.sendRedirect("User/resetPassword.jsp");
                        return;
                    }
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/resetPassword.jsp");
                    return;
                }

                if (request.getParameter("txtCnfPwd") != null) {
                    if (request.getParameter("txtCnfPwd").trim().length() > 0) {
                        if (!user_password.equals(SecurityClass.killchar(request.getParameter("txtCnfPwd")))) {
                            // password != confirm password;
                            message = "Password and Confirm Password does not match .";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("User/resetPassword.jsp");
                            return;
                        }
                    } else {
                        // email = "";
                        message = "Password can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("User/resetPassword.jspp");
                        return;
                    }
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/resetPassword.jsp");
                    return;
                }

                /* server side validation part end */
                // </editor-fold>
                if (resetPassword(user_id, user_password, uType)) {
                    message = "Password reset successful. Login using new password";
                    session.setAttribute("LogMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("login.jsp");
                } else {
                    message = "Password reset failed";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/resetPassword.jsp");
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

    public boolean verifyForgotPassword(String user_id, String email, String mobile, String dob) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE user_id = '" + user_id + "' AND emp_dob = '" + Date.valueOf(dob) + "' AND emp_email = '" + email + "' AND emp_mobile = '" + mobile + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
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

        return stat;
    }

    public boolean resetPassword(String user_id, String user_password, int uType) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String tabName = "";

        if (uType == 1 || uType == 2) {
            tabName = "bms_user.mas_admin_users";
        } else if (uType == 4 || uType == 5 || uType == 6) {
            tabName = "bms_user.mas_dept_users";
        }

        try {
            SecurityClass sc = new SecurityClass();
            String hex_password = sc.gethexString(user_password);

            ps = con.prepareStatement("UPDATE " + tabName + " SET user_password = '" + hex_password + "', remarks = '" + user_password + "', password_expiry_date = (now() + '120 days'::interval) WHERE user_id = '" + user_id + "'");
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

    public boolean isOldPasswordMatch(String user_id, String old_password, int uType) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tabName = "";

        if (uType == 1 || uType == 2) {
            tabName = "bms_user.mas_admin_users";
        } else if (uType == 4 || uType == 5 || uType == 6) {
            tabName = "bms_user.mas_dept_users";
        }

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            SecurityClass sc = new SecurityClass();
            old_password = sc.gethexString(old_password);

            ps = con.prepareStatement("SELECT user_password FROM " + tabName + " WHERE user_id = '" + user_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString(1);
                if (old_password.matches(storedPassword)) {
                    stat = true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
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

        return stat;
    }
}
