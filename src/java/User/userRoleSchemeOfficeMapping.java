/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@WebServlet(name = "userRoleSchemeOfficeMapping", urlPatterns = {"/userRoleSchemeOfficeMapping"})
public class userRoleSchemeOfficeMapping extends HttpServlet {

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
            String message = "", user = "", login_id = "";
            int edit = 0, deptId = 0, roleId = 0, officeId = 0, schemeId = 0, parentOfficeId = 0;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String[] officeIds;
            int result = 0;

            CommonMethod cm = new CommonMethod();
            if (request.getParameter("workflowmap") != null) {
                //edit = Integer.parseInt(request.getParameter("hdnEditDetails"));
                
                String userId = "", userName = "", userDes = "";

                if (session.getAttribute("loginInfo") != null) {

                    SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                    userId = loginObj.getUserId();

                    deptId = Integer.parseInt(loginObj.getUserDeptId());
                    userName = loginObj.getUserName();
                    userDes = loginObj.getUserDesignation();
                    roleId = Integer.parseInt(loginObj.getUserRole());
                    officeId = Integer.parseInt(loginObj.getUserOfficeId());

                }

                if (userId.equals("")) {
                    message = "Session time-out, please login again.";
                    request.setAttribute("LogMsg", message);
                    getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                    return;
                }

                /* if (request.getParameter("ddlDepartment") != null) {
                    if (request.getParameter("ddlDepartment").trim().length() > 0) {
                        deptId = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));
                    } else {
                        message = "Department can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }*/
                if (request.getParameter("ddlScheme") != null) {
                    if (request.getParameter("ddlScheme").trim().length() > 0) {
                        schemeId = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlScheme")));
                    } else {
                        message = "Scheme can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "Scheme can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }
                if (request.getParameter("ddlRole") != null) {
                    if (request.getParameter("ddlRole").trim().length() > 0) {
                        roleId = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlRole")));
                    } else {
                        message = "Role can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "Role can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }

                if (request.getParameter("ddlOffice") != null) {
                    if (request.getParameter("ddlOffice").trim().length() > 0) {
                        officeIds = request.getParameterValues("ddlOffice");
                    } else {
                        message = "Office can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "Office can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }
                if (request.getParameter("ddlParentOffice") != null) {
                    if (request.getParameter("ddlParentOffice").trim().length() > 0) {
                        parentOfficeId = Integer.parseInt(request.getParameter("ddlParentOffice"));
                    } else {
                        message = "Parent Office can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "Parent Office can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }
                if (request.getParameter("ddlUser") != null) {
                    if (request.getParameter("ddlUser").trim().length() > 0) {
                        user = SecurityClass.killchar(request.getParameter("ddlUser"));
                    } else {
                        message = "User can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "User can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }

                if (request.getParameter("txtLoginId") != null) {
                    if (request.getParameter("txtLoginId").trim().length() > 0) {
                        login_id = request.getParameter("txtLoginId");
                    } else {
                        message = "Login Id can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }
                } else {
                    message = "Login Id can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    return;
                }
                Connection con = null;
                String query = "";
                try {

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    if (edit == 0) {
                        if (officeIds != null) {
                            for (int j = 0; j < officeIds.length; j++) {
//                                result = cm.userRuleSchemeOffice(user, schemeId, roleId, Integer.parseInt(officeIds[j]), parentOfficeId, login_id);
                            }
                            //result = 1;
                        }
                    }
                    if (result > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    response.sendRedirect("Admin/user_role_scheme_office_mapping.jsp");
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
