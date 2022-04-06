/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.AjaxServlet;
import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import notification.notify;

/**
 *
 * @author Nibedita
 */
@WebServlet(name = "ManageDeptWiseUser", urlPatterns = {"/ManageDeptWiseUser"})
@MultipartConfig(maxFileSize = 4194304)	// upload file's size up to 4MB

public class ManageDeptWiseUser extends HttpServlet {

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
            HttpSession session = request.getSession();
            int edit = 0, editable = 0, user_role = 0, user_office = 0, district = 0;
            String user_id = "", admin_user_id = "", empName = "", gender = "", email = "", mobile = "",
                    empCode = "", empDesignation = "", hrmsCode = "",
                    created_by = "", dob = "", rcMemId = "", user_password = "", managed_by = "", loginId = "";

            String message = "", hex_password = "";

            CommonMethod cm = new CommonMethod();
            edit = Integer.parseInt(request.getParameter("hdnEdit"));
            int prev_edit = Integer.parseInt(request.getParameter("hdnPrevEdit"));

            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0, groupId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                //officeId = Integer.parseInt(loginObj.getUserOfficeId());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }
            if (edit == 1) {
                //this user's info getting edited
                user_id = request.getParameter("hdnUserId");

            }
            if (request.getParameter("hdnUserGroupId") != null && !"".equals(request.getParameter("hdnUserGroupId"))) {
                groupId = Integer.parseInt(request.getParameter("hdnUserGroupId"));
            }
            //deptId = Integer.parseInt(request.getParameter("hdnDeptId"));
            if (request.getParameter("txtEmpName") != null) {
                if (request.getParameter("txtEmpName").trim().length() > 0) {
                    empName = request.getParameter("txtEmpName");
                    empName = SecurityClass.killchar(empName);
                } else {
                    message = "Employee Name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    return;
                }
            } else {
                message = "Employee Name can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                return;
            }
            if (request.getParameter("txtEmpCode") != null) {
                if (request.getParameter("txtEmpCode").trim().length() > 0) {
                    empCode = request.getParameter("txtEmpCode");
                    empCode = SecurityClass.killchar(empCode);
                } else {
                    message = "Employee Code can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    return;
                }
            } else {
                message = "Employee Code can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                return;
            }

            if (request.getParameter("txtEmpDesignation") != null) {
                if (request.getParameter("txtEmpDesignation").trim().length() > 0) {
                    empDesignation = request.getParameter("txtEmpDesignation");
                    empDesignation = SecurityClass.killchar(empDesignation);
                } else {
                    message = "Employee Designation can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    return;
                }
            } else {
                message = "Employee Designation can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                return;
            }

            if (request.getParameter("txtEmail") != null) {
                if (request.getParameter("txtEmail").trim().length() > 0) {
                    if (cm.emailValidator(request.getParameter("txtEmail"))) {

                        /*   if ((edit == 0) && (!cm.checkEmailExists(request.getParameter("txtEmail")))) {
                            email = request.getParameter("txtEmail");
                            email = SecurityClass.killchar(email);
                        } else if (edit == 1) {
                            if (cm.checkEmailExists(user_id, request.getParameter("txtEmail"))) {
                                email = request.getParameter("txtEmail");
                                email = SecurityClass.killchar(email);
                            } else {
                                if (!cm.checkEmailExists(request.getParameter("txtEmail")) && cm.emailValidator(request.getParameter("txtEmail"))) {
                                    email = request.getParameter("txtEmail");
                                    email = SecurityClass.killchar(email);
                                } else {
                                    message = "Either Email ID already exists or Email ID is not valid.";
                                    session.setAttribute("RegMsg", message);
                                    session.setAttribute("type", "danger");
                                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                                    return;
                                }
                            }

                        } else {
                            message = "Email ID already exists.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                            return;
                        }*/
                        email = request.getParameter("txtEmail");
                        email = SecurityClass.killchar(email);

                    } else {
                        message = "Email ID is not valid.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                        return;
                    }
                } else {
//                    message = "Email ID can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
                }
            }
//            else {
//                message = "Email ID can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }
            if (request.getParameter("txtMobile") != null) {
                if (request.getParameter("txtMobile").trim().length() > 0) {
                    if (cm.mobileValidator(request.getParameter("txtMobile"))) {
                        /*  if ((edit == 0) && (!cm.checkMobileExists(request.getParameter("txtMobile")))) {
                            mobile = request.getParameter("txtMobile");
                            mobile = SecurityClass.killchar(mobile);
                        } else if (edit == 1) {
                            if (cm.checkMobileExists(user_id, request.getParameter("txtMobile"))) {
                                mobile = request.getParameter("txtMobile");
                                mobile = SecurityClass.killchar(mobile);
                            } else {*/
 /*    if (!cm.checkMobileExists(request.getParameter("txtMobile")) && cm.mobileValidator(request.getParameter("txtMobile"))) {
                                    mobile = request.getParameter("txtMobile");
                                    mobile = SecurityClass.killchar(mobile);
                                } else {
                                    message = "Either Mobile Number already exists or Mobile Number is not valid.";
                                    session.setAttribute("RegMsg", message);
                                    session.setAttribute("type", "danger");
                                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                                    return;
                                }

                            //}*/
                        mobile = request.getParameter("txtMobile");
                        mobile = SecurityClass.killchar(mobile);

                    } else {
                        message = "Mobile Number is not valid.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                        return;
                    }
                } else {
                    // mobile = invalid mobile;
                    message = "Mobile Number is not valid.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    return;
                }
            } else {
                // mobile = "";
//                    message = "Mobile Number can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageAdminUsers.jsp");
//                    return;
            }
            /* } */
//            else {
//                message = "Mobile Number can not be blank.";
//                session.setAttribute("RegMsg", message);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("Admin/manageAdminUsers.jsp");
//                return;
//            }

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
                        response.sendRedirect("login.jsp");
                        return;
                    }
                } else {
                    // dob = "";
                    message = "Date of Birth can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    return;
                }
            } else {
                message = "Date of Birth can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                return;
            }
            if (request.getParameter("txtHrmsCode") != null) {
                if (request.getParameter("txtHrmsCode").trim().length() > 0) {
                    hrmsCode = request.getParameter("txtHrmsCode");
                    hrmsCode = SecurityClass.killchar(hrmsCode);
                } else {
                    message = "HRMS Code can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    return;
                }
            } else {
                message = "HRMS Code can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                return;
            }

            if (request.getParameter("txtLoginId") != null) {
                if (request.getParameter("txtLoginId").trim().length() > 0) {
                    loginId = SecurityClass.killchar(request.getParameter("txtLoginId"));
                }
            }

//            if (request.getParameter("txtRcMem") != null) {
//                if (request.getParameter("txtRcMem").trim().length() > 0) {
//                    rcMemId = request.getParameter("txtRcMem");
//                    rcMemId = SecurityClass.killchar(rcMemId);
//
//                } else {
//                    // user_name = "";
////                    message = "PIN can not be blank.";
////                    session.setAttribute("RegMsg", message);
////                    session.setAttribute("type", "danger");
////                    response.sendRedirect("Admin/manageAdminUsers.jsp");
////                    return;
//                }
//            }
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
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                int myInt = sr.nextInt(1000000);
                user_password = String.format("%06d", myInt);

            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            SecurityClass sc = new SecurityClass();
            hex_password = sc.gethexString(user_password);
            Connection con = null;
            CallableStatement cs = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query = "";
            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                //con.setAutoCommit(false);
                if (prev_edit == 1) {
                    user_id = request.getParameter("hdnUserId");

                    if (cm.userManagedBy(mobile, user_id, admin_user_id)) {

                        message = "User created successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/ManageDeptWiseUser.jsp");
                        return;
                    }
                } else if (prev_edit == 0) {
                    if (edit == 0) {
                        int i = 1;
                        cs = con.prepareCall("{call bms_user.insert_mas_dept_users(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                        cs.setString(i++, user_id);
                        cs.setString(i++, hex_password);
                        cs.setString(i++, empName);
                        cs.setString(i++, empCode);
                        cs.setInt(i++, deptId);
                        cs.setString(i++, empDesignation);
                        cs.setString(i++, email);
                        cs.setString(i++, mobile);
                        cs.setDate(i++, java.sql.Date.valueOf(dob));
                        cs.setString(i++, hrmsCode);
                        cs.setString(i++, rcMemId);
                        cs.setString(i++, userId);
                        cs.setString(i++, user_password);
                        cs.setString(i++, loginId);
                        cs.setInt(i++, groupId);
                        cs.setInt(i++, 1);
                        if(cs.execute()){
                            if (edit == 0) {
                            message = "User created successfully , password is " + user_password;
                        } else if (edit == 1) {
                            message = "User details updated successfully";
                        }

                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp"); 
                        }
                         else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                        return;
                    }

                    }

                        //cm.userGroupMapping(deptId, groupId, user_id);
                       
//                    } else {
//                        message = "Error in saving";
//                        session.setAttribute("RegMsg", message);
//                        session.setAttribute("type", "danger");
//                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
//                        return;
//                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                response.sendRedirect("Admin/manageDeptUserGroup.jsp");
            } 
//        finally {
//                try {
//                    if (rs != null) {
//                        rs.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                };
//                try {
//                    if (ps != null) {
//                        ps.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                };
//                try {
//                    if (con != null) {
//                        con.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                };
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/manageDeptUserGroup.jsp");
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
            String query = "SELECT bms_user.generate_user_id()";
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
