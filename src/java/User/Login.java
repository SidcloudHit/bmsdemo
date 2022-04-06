/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.CaptchaGen;
import Security.SecurityClass;
import SessionPackage.LoginClass;
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
 * @author NIC-Arindam
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

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
            CommonMethod cm = new CommonMethod();
            String loginId = "", submittedpwd = "", storedpwd = "", hashpwd = "", msg = "";
            String name = "", userId = "", uDes = "",login_opt = "";
            String cptimg2 = "";
            int uRole = 0, rRole = 0, uDept = 0;
            boolean email = false, regNo = false, mobile = false;

//            if (session.getAttribute(CaptchaGen.CAPTCHA_KEY) != null) {
//                cptimg2 = (String) session.getAttribute(CaptchaGen.CAPTCHA_KEY);
//            }
            //String cptimg2 = (String) session.getAttribute(captchagen.CAPTCHA_KEY);
            // String cptval2 = request.getParameter("txtLoginCaptcha");
//            if (!cptimg2.equalsIgnoreCase(cptval2)) {
//                msg = "Wrong Verification Code";
//                session.setAttribute("LogMsg", msg);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("index.jsp");
//                return;
//            }
            if (session.getAttribute("captcha_solution") != null) {
                cptimg2 = (String) session.getAttribute("captcha_solution");
            }
            String cptval2 = request.getParameter("txtLoginCaptcha");
            login_opt = request.getParameter("login_opt");
            if (!cptimg2.equalsIgnoreCase(cptval2)) {
                msg = "Wrong Answer";
                session.setAttribute("LogMsg", msg);
                session.setAttribute("type", "danger");
                response.sendRedirect("login.jsp");
                return;
            }
            String hashsaltinsession = "";
            hashsaltinsession = (String) session.getAttribute("hashsaltinsession");
            Security.SecurityClass sc = new Security.SecurityClass();
            hashsaltinsession = sc.gethexString(hashsaltinsession.trim().concat("NICCIN"));

            if (request.getParameter("btnLogin") != null) {

                if (request.getParameter("txtUserName") != null) {
                    userId = SecurityClass.killchar(request.getParameter("txtUserName").trim());
                    if (userId.contains("@")) {
                        if (cm.emailValidator(userId)) {
                            email = true;
                        } else {
                            msg = "Please Enter a Valid User ID";
                            session.setAttribute("LogMsg", msg);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("login.jsp");
                            return;
                        }
                    } else if (userId.matches("[0-9]+")) {
                        if (cm.mobileValidator(userId)) {
                            mobile = true;
                        } else {
                            msg = "Please Enter a Valid 10 Digit Mobile Number";
                            session.setAttribute("LogMsg", msg);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("login.jsp");
                            return;
                        }
                    } else {
                        regNo = true;
                    }
                } else {
                    msg = "Please Enter a Valid User ID";
                    session.setAttribute("LogMsg", msg);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("login.jsp");
                    return;
                }

                if (request.getParameter("txtUserPwd") != null) {
                    submittedpwd = request.getParameter("txtUserPwd").trim();

                } else {
                    msg = "Please Enter Password.";
                    session.setAttribute("LogMsg", msg);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("login.jsp");
                    return;
                }

                if (request.getParameter("role") != null) {
                    rRole = Integer.parseInt(SecurityClass.killchar(request.getParameter("role").trim()));
                }
            }

            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String pwdExpDate = "";
            String query = "";

//            if (email) {
//                query = "SELECT user_id, user_password, user_name, user_role, user_department_id, to_char(password_expiry_date,'dd/MM/yyyy') as password_expiry_date FROM mas_admin_user_account WHERE  user_email = ?";
//            } else if (regNo) {
//                query = "SELECT user_id, user_password, user_name, user_role, user_department_id, to_char(password_expiry_date,'dd/MM/yyyy') as password_expiry_date FROM mas_admin_user_account WHERE  user_login_id = ?";
//            }
            if (email) {
                query = "SELECT user_id, user_password, emp_name, emp_department_id, emp_designation, to_char(password_expiry_date,'dd/MM/yyyy') as password_expiry_date FROM bms_user.mas_dept_users WHERE  emp_email = ?";
            } else if (mobile) {
                query = "SELECT user_id, user_password, emp_name, emp_department_id, emp_designation, to_char(password_expiry_date,'dd/MM/yyyy') as password_expiry_date FROM bms_user.mas_dept_users WHERE  emp_mobile = ?";
            } else if (regNo) {
                query = "SELECT user_id, user_password, emp_name, emp_department_id, emp_designation, to_char(password_expiry_date,'dd/MM/yyyy') as password_expiry_date FROM bms_user.mas_dept_users WHERE  login_id = ?";
            }

            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, userId);

                rs = ps.executeQuery();
                if (rs.next()) {
                    storedpwd = rs.getString("user_password");
                    userId = rs.getString("user_id");
                    //uRole = rs.getInt("user_role");
                    uDept = rs.getInt("emp_department_id");
                    name = rs.getString("emp_name");
                    uDes = rs.getString("emp_designation");
                    pwdExpDate = rs.getString("password_expiry_date");
                } else {
                    msg = "Please enter valid User ID and Password.";
                    request.setAttribute("LogMsg", msg);
                    request.setAttribute("type", "danger");
                    getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
                }
                storedpwd = storedpwd.concat(hashsaltinsession);
                hashpwd = sc.gethexString(storedpwd);

                if (hashpwd.equalsIgnoreCase(submittedpwd)) {
                    session.invalidate();
                    session = request.getSession(true);
                    LoginExtraFeatures firstloginstatus = new LoginExtraFeatures();
                    int firstLoginStatus = firstloginstatus.checkFirstLoginStatus_deptuser(userId);
                    if (firstLoginStatus == 1) {
                        SessionPackage.LoginClass loginInfo = new LoginClass();

                        loginInfo.setUserId(userId);
                        loginInfo.setUserDeptId(Integer.toString(uDept));
                        loginInfo.setUserName(name);
                        loginInfo.setUserDesignation(uDes);

//                    session.setAttribute("user_id", userId);
//                    session.setAttribute("user_dept", uDept);
//                    session.setAttribute("user_name", name);
//                    session.setAttribute("user_desig", uDes);
                        session.setAttribute("loginInfo", loginInfo);

                        if (cm.checkDateGtOrEqToCurrDate(pwdExpDate, 1, 1, 0)) {
                            response.sendRedirect("User/changePassword.jsp");
                        } else {
//                        if (uRole == 1) {
//                            response.sendRedirect("Admin/adminHome.jsp");
//                        } else if (uRole > 1) {
                            response.sendRedirect("User/userSettings.jsp");
//                        }
//                        else if (uRole == 3) {
//                            response.sendRedirect("MISUser/misUserHome.jsp");
//                        }

                        }
                    } else {
                        session.invalidate();
                        session = request.getSession(true);
                        session.setAttribute("firstloginuserid", userId);
                        session.setAttribute("userType", "dept_user");
                        response.sendRedirect("User/setPassword.jsp");
                    }
                } else {
                    msg = "Please Enter a Valid User ID and Password.";
                    session.setAttribute("LogMsg", msg);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("login.jsp");

                    return;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("LogMsg", msg);
                session.setAttribute("type", "danger");
                response.sendRedirect("login.jsp");
                return;
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
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            String msg = "Some error occured! Please try again.";
            session.setAttribute("LogMsg", msg);
            session.setAttribute("type", "danger");
            response.sendRedirect("login.jsp");
            return;
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
