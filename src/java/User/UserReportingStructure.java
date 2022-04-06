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
@WebServlet(name = "UserReportingStructure", urlPatterns = {"/UserReportingStructure"})
public class UserReportingStructure extends HttpServlet {

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
            int edit = 0, dept = 0, is_main_pg = 0, map_id = 0;
            String message = "", reportee = "", reporter = "" ;
            CommonMethod cm = new CommonMethod();
            edit = Integer.parseInt(request.getParameter("hdnEdit"));
            map_id = Integer.parseInt(request.getParameter("hdnReportId"));
           // map_id = 1;
            
            
            if (request.getParameter("ddlDepartment") != null) {
                if (request.getParameter("ddlDepartment").trim().length() > 0) {
                    dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));

                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/userReportingStructure.jsp");
                    return;
                }
            } else {
                message = "Department can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/userReportingStructure.jsp");
                return;
            }
            if (request.getParameter("ddlUserReportee") != null) {
                if (request.getParameter("ddlUserReportee").trim().length() > 0) {
                    reportee = SecurityClass.killchar(request.getParameter("ddlUserReportee"));
                } else {
                    message = "Reportee can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/userReportingStructure.jsp");
                    return;
                }
            } else {
                message = "Reportee can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/userReportingStructure.jsp");
                return;
            }
            if (request.getParameter("ddlUserReporter") != null) {
                if (request.getParameter("ddlUserReporter").trim().length() > 0) {
                    reporter = SecurityClass.killchar(request.getParameter("ddlUserReporter"));
                } else {
                    message = "Reporter can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/userReportingStructure.jsp");
                    return;
                }
            } else {
                message = "Reporter can not be blank.";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("Admin/userReportingStructure.jsp");
                return;
            }

            
            
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query = "";
            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                con.setAutoCommit(false);

                if (edit == 0) {
                    query = "INSERT INTO map_admin_user_reporting_structure(department_id, user_id_reportee, user_id_reporter)\n"
                            + "    VALUES (?, ?, ?);";
                    ps = con.prepareStatement(query);
                    int i = 1;
                    ps.setInt(i++, dept);
                    ps.setString(i++, reportee);
                    ps.setString(i++, reporter);
                } else if (edit == 1) {
                    query = "UPDATE map_admin_user_reporting_structure\n"
                            + "   SET department_id=?, user_id_reportee=?, user_id_reporter=?  WHERE map_id = ?";
                    ps = con.prepareStatement(query);
                    int i = 1;
                    ps.setInt(i++, dept);
                    ps.setString(i++, reportee);
                    ps.setString(i++, reporter);
                    
                    ps.setInt(i++, map_id);
                }
                if (ps.executeUpdate() > 0) {
                    con.commit();
                    message = "Data saved Successfully";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("Admin/userReportingStructure.jsp");
                } else {
                    message = "Error in saving";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/userReportingStructure.jsp");
                    return;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                response.sendRedirect("Admin/userReportingStructure.jsp");
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
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/userReportingStructure.jsp");
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
