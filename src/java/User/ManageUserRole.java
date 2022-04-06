/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "ManageUserRole", urlPatterns = {"/ManageUserRole"})
public class ManageUserRole extends HttpServlet {

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
            int edit = 0, dept = 0, role_id = 0;
            String message = "", role_name = "", jurisdiction = "";

            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;

            CommonMethod cm = new CommonMethod();

            if (request.getParameter("btnAddRole") != null) {

                edit = Integer.parseInt(request.getParameter("hdnEditRole"));

                role_id = Integer.parseInt(request.getParameter("hdnRoleId"));

                if (request.getParameter("txtRoleName") != null) {
                    if (request.getParameter("txtRoleName").trim().length() > 0) {
                        role_name = SecurityClass.killchar(request.getParameter("txtRoleName"));
                    } else {
                        message = "Role Name can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRole.jsp");
                        return;
                    }
                } else {
                    message = "Role Name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRole.jsp");
                    return;
                }
                if (request.getParameter("ddlDepartment") != null) {
                    if (request.getParameter("ddlDepartment").trim().length() > 0) {
                        dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));

                    } else {
                        message = "Department can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRole.jsp");
                        return;
                    }
                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRole.jsp");
                    return;
                }

                if (request.getParameter("ddlJurisdiction") != null) {
                    if (request.getParameter("ddlJurisdiction").trim().length() > 0) {
                        jurisdiction = SecurityClass.killchar(request.getParameter("ddlJurisdiction"));

                    } else {
                        message = "Jurisdiction can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRole.jsp");
                        return;
                    }
                } else {
                    message = "Jurisdiction can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRole.jsp");
                    return;
                }

                Connection con = null;
                //PreparedStatement ps = null;
                //PreparedStatement ps1 = null;
                //ResultSet rs = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    //con.setAutoCommit(false);

                    if (edit == 0) {
                        query = "INSERT INTO mas_admin_user_role(role_name, department_id, role_level)\n"
                                + "    VALUES (?, ?, ?);";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setString(i++, role_name);
                        ps.setInt(i++, dept);
                        ps.setString(i++, jurisdiction);
                    } else if (edit == 1) {
                        query = "UPDATE mas_admin_user_role\n"
                                + " SET  role_name = ?, department_id = ?, role_level = ?  WHERE role_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;

                        ps.setString(i++, role_name);
                        ps.setInt(i++, dept);
                        ps.setString(i++, jurisdiction);

                        ps.setInt(i++, role_id);
                    }
                    if (ps.executeUpdate() > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageRole.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRole.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("Admin/manageRole.jsp");
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
                
            } else if (request.getParameter("mapRoleScheme") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                Connection con = null;
                String roleLevel = cm.getRoleLevel(roleId);
                //String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, department_id FROM master_scheme WHERE status = '1' AND map_status = '0' AND department_id = " + deptId;
                String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, a.department_id, \n"
                        + " (SELECT role_level FROM mas_admin_user_role b INNER JOIN admin_user_role_scheme_mapping c ON b.role_id = c.role_id WHERE scheme_id = a.id AND role_level = '"+ roleLevel  +"') AS level, \n"
                        + " (SELECT type FROM admin_user_role_scheme_mapping b INNER JOIN mas_admin_user_role c ON b.role_id = c.role_id WHERE scheme_id = a.id AND role_level = '"+ roleLevel  +"') AS type \n"
                        + " FROM master_scheme a \n"
                        + " WHERE a.department_id =" + deptId;

                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(ManageUserRole.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            } else if (request.getParameter("mappedRoleScheme") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                Connection con = null;
                //String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, department_id FROM master_scheme WHERE status = '1' AND map_status = '0' AND department_id = " + deptId;
                String sql = "SELECT scheme_name,department_name,role_name FROM admin_user_role_scheme_mapping map\n"
                        + "INNER JOIN master_scheme s ON map.scheme_id = s.id \n"
                        + "INNER JOIN mas_admin_user_role r ON r.role_id = map.role_id \n"
                        + " WHERE map.role_id=" + roleId + " AND map.dept_id=" +deptId;

                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(ManageUserRole.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/manageRole.jsp");
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
