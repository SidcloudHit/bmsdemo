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
@WebServlet(name = "ManageDeptUserGroup", urlPatterns = {"/ManageDeptUserGroup"})
public class ManageDeptUserGroup extends HttpServlet {

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
            int edit = 0, dept = 0, group_id = 0;
            String message = "", group_name = "",dept_abbr="", jurisdiction = "";

            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;

            CommonMethod cm = new CommonMethod();

            if (request.getParameter("btnAddDeptGroup") != null) {

                edit = Integer.parseInt(request.getParameter("hdnEditDeptGroup"));

                group_id = Integer.parseInt(request.getParameter("hdnGroupId"));
                if (request.getParameter("dept_abbr") != null) {
                    if (request.getParameter("dept_abbr").trim().length() > 0) {
                        dept_abbr = SecurityClass.killchar(request.getParameter("dept_abbr"));
                    }
                }

                if (request.getParameter("txtGroupName") != null) {
                    if (request.getParameter("txtGroupName").trim().length() > 0) {
                        group_name = SecurityClass.killchar(request.getParameter("txtGroupName"));
                    } else {
                        message = "Group Name can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                        return;
                    }
                } else {
                    message = "Group Name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                    return;
                }
                if (request.getParameter("ddlDepartment") != null) {
                    if (request.getParameter("ddlDepartment").trim().length() > 0) {
                        dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));

                    } else {
                        message = "Department can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                        return;
                    }
                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                    return;
                }

                /*    if (request.getParameter("ddlJurisdiction") != null) {
                    if (request.getParameter("ddlJurisdiction").trim().length() > 0) {
                        jurisdiction = SecurityClass.killchar(request.getParameter("ddlJurisdiction"));

                    } else {
                        message = "Jurisdiction can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                        return;
                    }
                } else {
                    message = "Jurisdiction can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                    return;
                }*/
                Connection con = null;
                //PreparedStatement ps = null;
                //PreparedStatement ps1 = null;
                //ResultSet rs = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    //con.setAutoCommit(false);

                    if (edit == 0) {
                        query = "INSERT INTO bms_user.mas_dept_users_group(department_id, group_name)\n"
                                + "    VALUES (?, ?);";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, dept);
                        ps.setString(i++, dept_abbr+"_"+group_name);
                    } else if (edit == 1) {
                        query = "UPDATE bms_user.mas_dept_users_group\n"
                                + " SET  group_name = ?, department_id = ? WHERE group_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;

                        ps.setString(i++, dept_abbr+"_"+group_name);
                        ps.setInt(i++, dept);

                        ps.setInt(i++, group_id);
                    }
                    if (ps.executeUpdate() > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageDeptUserGroup.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("Admin/manageDeptUserGroup.jsp");
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
            } else if (request.getParameter("deptUserList") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int groupId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                Connection con = null;
                String sql = "SELECT user_id, emp_name, u.emp_department_id FROM bms_user.mas_dept_users u \n"
                        + "INNER JOIN mas_department dept ON u.emp_department_id = dept.department_id \n"
                        + "INNER JOIN bms_user.mas_dept_users_group grp ON u.emp_department_id = grp.department_id \n"
                        + "WHERE u.emp_department_id = " + deptId + " AND u.user_id NOT IN (SELECT user_id FROM bms_user.map_dept_users_group_users)";

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
            } else if (request.getParameter("mapRoleScheme") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                int workId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("workflow")));
                Connection con = null;
                //String roleLevel = cm.getRoleLevel(roleId);
                //String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, department_id FROM master_scheme WHERE status = '1' AND map_status = '0' AND department_id = " + deptId;
                String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, a.department_id, \n"
                        + " (SELECT role_name FROM bms_user.mas_user_roles b INNER JOIN bms_user.map_dept_users_group_schemes c ON b.role_id = c.role_id WHERE scheme_id = a.id AND b.role_id = " + roleId + ") AS role_name \n"
                        // + " (SELECT type FROM admin_user_role_scheme_mapping b INNER JOIN mas_admin_user_role c ON b.role_id = c.role_id WHERE scheme_id = a.id AND role_level = '"+ roleLevel  +"') AS type \n"
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
                int groupId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                
                Connection con = null;
                //String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, department_id FROM master_scheme WHERE status = '1' AND map_status = '0' AND department_id = " + deptId;
                String sql = "SELECT scheme_name,department_name,role_name FROM bms_user.map_dept_users_group_schemes map\n"
                        + "INNER JOIN master_scheme s ON map.scheme_id = s.id \n"
                        + "INNER JOIN bms_user.mas_user_roles r ON r.role_id = map.role_id \n"
                        + "WHERE map.group_id=" + groupId + " AND map.department_id=" + deptId;

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
            } else if (request.getParameter("mappedUserList") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int groupId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                Connection con = null;
                String sql = "SELECT mapping_id, map.department_id, department_name, map.group_id, group_name, map.user_id, emp_name FROM bms_user.map_dept_users_group_users map \n"
                        + " INNER JOIN  mas_department dept ON map.department_id = dept.department_id\n"
                        + " INNER JOIN bms_user.mas_dept_users_group grp ON map.group_id = grp.group_id  \n"
                        + " INNER JOIN bms_user.mas_dept_users u ON u.user_id = map.user_id \n"
                        + " WHERE map.status = 1 AND map.group_id=" + groupId + " AND map.department_id=" +deptId;

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

}
