/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Department;

import Common.CommonMethod;
import Security.CaptchaGen;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import log.errorlog;

/**
 *
 * @author Nibedita
 */
@WebServlet(name = "saveDeptOnboardingData", urlPatterns = {"/saveDeptOnboardingData"})
@MultipartConfig(maxFileSize = 4194304)	// upload file's size up to 4MB

public class saveDeptOnboardingData extends HttpServlet {

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
            int status = 0, editable = 0, user_role = 0, user_office = 0, dept_id = 0, district = 0;
            String user_id = "", cptimg2 = "", empName1 = "", email1 = "", mobile1 = "", empCode1 = "", empDesignation1 = "", hrmsCode1 = "", empName2 = "", email2 = "", mobile2 = "", empCode2 = "", empDesignation2 = "", hrmsCode2 = "",
                    created_by = "", dob1 = "", dob2 = "", rcMemId = "", user_password = "", managed_by = "", login_id = "";

            String message = "", hex_password = "";

            CommonMethod cm = new CommonMethod();
            errorlog log=new errorlog();
            String cptval2 = request.getParameter("txtLoginCaptcha");
            if (session.getAttribute(CaptchaGen.CAPTCHA_KEY) != null) {
                cptimg2 = (String) session.getAttribute(CaptchaGen.CAPTCHA_KEY);
            }
            if (!cptimg2.equalsIgnoreCase(cptval2)) {
                status = 2;
                response.sendRedirect("department/dept_onboarding.jsp");
                return;
            }
            if (request.getParameter("hdnEdit") != null && !"".equals(request.getParameter("hdnEdit"))) {
                editable = Integer.parseInt(request.getParameter("hdnEdit"));
            }
            if (request.getParameter("dept") != null && !"".equals(request.getParameter("dept"))) {
                dept_id = Integer.parseInt(request.getParameter("dept"));
            }
            if (request.getParameter("txtEmpName1") != null && !"".equals(request.getParameter("txtEmpName1"))) {
                empName1 = request.getParameter("txtEmpName1");
                empName1 = SecurityClass.killchar(empName1);
            }
            if (request.getParameter("txtEmpName2") != null && !"".equals(request.getParameter("txtEmpName2"))) {
                empName2 = request.getParameter("txtEmpName2");
                empName2 = SecurityClass.killchar(empName2);
            }
            if (request.getParameter("txtEmpCode1") != null && !"".equals(request.getParameter("txtEmpCode1"))) {
                empCode1 = request.getParameter("txtEmpCode1");
                empCode1 = SecurityClass.killchar(empCode1);
            }
            if (request.getParameter("txtEmpCode2") != null && !"".equals(request.getParameter("txtEmpCode2"))) {
                empCode2 = request.getParameter("txtEmpCode2");
                empCode2 = SecurityClass.killchar(empCode2);
            }
            if (request.getParameter("txtEmpDesignation1") != null && !"".equals(request.getParameter("txtEmpDesignation1"))) {
                empDesignation1 = request.getParameter("txtEmpDesignation1");
                empDesignation1 = SecurityClass.killchar(empDesignation1);
            }
            if (request.getParameter("txtEmpDesignation2") != null && !"".equals(request.getParameter("txtEmpDesignation2"))) {
                empDesignation2 = request.getParameter("txtEmpDesignation2");
                empDesignation2 = SecurityClass.killchar(empDesignation2);
            }
            if (request.getParameter("txtEmail1") != null && !"".equals(request.getParameter("txtEmail1"))) {
                if (cm.emailValidator(request.getParameter("txtEmail1"))) {
                    email1 = request.getParameter("txtEmail1");
                    email1 = SecurityClass.killchar(email1);
                }
            }
            if (request.getParameter("txtEmail2") != null && !"".equals(request.getParameter("txtEmail2"))) {
                if (cm.emailValidator(request.getParameter("txtEmail2"))) {
                    email2 = request.getParameter("txtEmail2");
                    email2 = SecurityClass.killchar(email2);
                }
            }
            if (request.getParameter("txtMobile1") != null && !"".equals(request.getParameter("txtMobile1"))) {
                if (cm.mobileValidator(request.getParameter("txtMobile1"))) {
                    mobile1 = request.getParameter("txtMobile1");
                    mobile1 = SecurityClass.killchar(mobile1);
                }
            }
            if (request.getParameter("txtMobile2") != null && !"".equals(request.getParameter("txtMobile2"))) {
                if (cm.mobileValidator(request.getParameter("txtMobile2"))) {
                    mobile2 = request.getParameter("txtMobile2");
                    mobile2 = SecurityClass.killchar(mobile2);
                }
            }
            if (request.getParameter("txtDateOfBirth1") != null && !"".equals(request.getParameter("txtDateOfBirth1"))) {
                if (cm.checkDateGtOrEqToCurrDate(request.getParameter("txtDateOfBirth1"), 1, 0, 1)) {
                    dob1 = request.getParameter("txtDateOfBirth1");
                    dob1 = SecurityClass.killchar(dob1);
                }
            }
            if (request.getParameter("txtDateOfBirth2") != null && !"".equals(request.getParameter("txtDateOfBirth2"))) {
                if (cm.checkDateGtOrEqToCurrDate(request.getParameter("txtDateOfBirth2"), 1, 0, 1)) {
                    dob2 = request.getParameter("txtDateOfBirth2");
                    dob2 = SecurityClass.killchar(dob2);
                }
            }
            if (request.getParameter("txtHrmsCode1") != null && !"".equals(request.getParameter("txtHrmsCode1"))) {
                hrmsCode1 = request.getParameter("txtHrmsCode1");
                hrmsCode1 = SecurityClass.killchar(hrmsCode1);
            }
            if (request.getParameter("txtHrmsCode2") != null && !"".equals(request.getParameter("txtHrmsCode2"))) {

                hrmsCode2 = request.getParameter("txtHrmsCode2");
                hrmsCode2 = SecurityClass.killchar(hrmsCode2);
            }

            /*if (request.getParameter("txtUserPwd") != null) {
                if (request.getParameter("txtUserPwd").trim().length() > 0) {
                    user_password = request.getParameter("txtUserPwd");
                    user_password = SecurityClass.killchar(user_password);
                } else {
                    message = "Password can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageAdminUsers.jsp.jsp");
                    return;
                }
            } else {
                message = "Password can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/manageAdminUsers.jsp.jsp");
                return;
            }*/
//            try {
//                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//                int myInt = sr.nextInt(1000000);
//                user_password = String.format("%06d", myInt);
//
//            } catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
//            }
            pwdMgmt pwd = new pwdMgmt();
            user_password=pwd.generateDefaultPassword();
//            user_password = "bms123";
            SecurityClass sc = new SecurityClass();
            hex_password = sc.gethexString(user_password);
            Connection con = null;
            CallableStatement cs = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query = "";
            ob_dept_dal dept_dal = new ob_dept_dal();
            try {

                if (editable == 0) {
                    status = dept_dal.saveDeptOnboardingData(user_id, dept_id, empName1, empCode1, empDesignation1, email1, mobile1, dob1, hrmsCode1, hex_password, empName2, empCode2, empDesignation2, email2, mobile2, dob2, hrmsCode2, user_password);

                } else {
                    status = dept_dal.updateDeptOnboardingData(dept_id, empName1, empCode1, empDesignation1, email1, mobile1, dob1, hrmsCode1, hex_password, empName2, empCode2, empDesignation2, email2, mobile2, dob2, hrmsCode2, user_password);

                }
            } catch (Exception ex) {
                status = 3;
                ex.printStackTrace();
                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                response.sendRedirect("department/dept_onboarding.jsp");
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
            out.print(status);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            //int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
            response.sendRedirect("department/dept_onboarding.jsp");
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

    public String generateUserId() {
        String uId = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        try {
            String query = "SELECT generate_user_id()";
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);

            //int i = 0;
            while (res.next()) {
                uId = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (stmt != null) {
                    stmt.close();
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
        return uId;
    }

}
