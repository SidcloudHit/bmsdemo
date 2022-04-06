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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.NullPointerException;

/**
 *
 * @author Nibedita
 */
@WebServlet(name = "ManageUserRoleURL", urlPatterns = {"/ManageUserRoleURL"})
public class ManageUserRoleUrl extends HttpServlet {

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
            int edit = 0, editHeader = 0, dept = 0, user_role = 0, url = 0, is_active = 0, priority = 0, map_id = 0, scheme_id = 0, header_id = 0;
            int deptId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";
            String message = "";
            CommonMethod cm = new CommonMethod();

            //userId = request.getParameter("hdnUserId");
            // map_id = 1;
            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                //deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }

            if (request.getParameter("btnSubmit") != null) {

                if (request.getParameter("hdnEdit") != null) {
                    edit = Integer.parseInt(request.getParameter("hdnEdit"));
                }

                if (edit == 1) {
                    if (request.getParameter("hdnEditHeader") != null) {
                        editHeader = Integer.parseInt(request.getParameter("hdnEditHeader"));
                    }
                    if (request.getParameter("hdnMapId") != null) {
                        map_id = Integer.parseInt(request.getParameter("hdnMapId"));
                    }
                }
//                if (request.getParameter("hdnMapId") != null) {
//                    int mapping_id = Integer.parseInt(request.getParameter("hdnMapId"));
//                }
                if (request.getParameter("ddlDepartment") != null) {
                    if (request.getParameter("ddlDepartment").trim().length() > 0) {
                        dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));

                    } else {
                        message = "Department can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                        return;
                    }
                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                    return;
                }
                if (request.getParameter("ddlScheme") != null) {
                    if (request.getParameter("ddlScheme").trim().length() > 0) {
                        scheme_id = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlScheme")));//.split("~")[0]

                    } else {
                        message = "Scheme can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                        return;
                    }
                } else {
                    message = "Scheme can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                    return;
                }
                if (request.getParameter("ddlRole") != null) {
                    if (request.getParameter("ddlRole").trim().length() > 0) {
                        user_role = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlRole")));
                    } else {
                        message = "Role can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                        return;
                    }
                } else {
                    message = "Role can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                    return;
                }

                if (request.getParameter("ddlHeaders") != null) {
                    if (request.getParameter("ddlHeaders").trim().length() > 0) {
                        header_id = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlHeaders")));
                    } else {
                        message = "URL Header can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                        return;
                    }
                } else {
                    message = "URL Header can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                    return;
                }

                if (!(editHeader == 1)) {
                    if (request.getParameter("ddlURL") != null) {
                        if (request.getParameter("ddlURL").trim().length() > 0) {
                            url = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlURL")));
                        } else {
                            message = "URL can not be blank.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                            return;
                        }
                    } else {
                        message = "URL can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                        return;
                    }
                }

                //            if (request.getParameter("radMainPage") != null) {
                //                if (request.getParameter("radMainPage").trim().length() > 0) {
                //                    is_main_pg = Integer.parseInt(SecurityClass.killchar(request.getParameter("radMainPage")));
                //                }
                //            }
                if (request.getParameter("radActive") != null) {
                    if (request.getParameter("radActive").trim().length() > 0) {
                        is_active = Integer.parseInt(SecurityClass.killchar(request.getParameter("radActive")));
                    }
                }

                if (request.getParameter("txtPriority") != null) {
                    if (request.getParameter("txtPriority").trim().length() > 0) {
                        priority = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtPriority")));
                    }
                }

                Connection con = null;
                PreparedStatement ps = null;
                PreparedStatement ps1 = null;
                ResultSet rs = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    con.setAutoCommit(false);

                    int headerUrlMapped = checkHeaderMapping(scheme_id, user_role, header_id, con);

                    if (headerUrlMapped == 0) {
                        //INSERT header Url Mapping with active = 1
                        query = "INSERT INTO map_scheme_role_url(scheme_id, role_id, url_id, is_active, priority_no, mapping_time, mapped_by)\n"
                                + "    VALUES (?, ?, ?, ?, ?, now(), ?);";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, scheme_id);
                        ps.setInt(i++, user_role);
                        ps.setInt(i++, header_id);
                        ps.setInt(i++, 1);
                        ps.setInt(i++, priority);
                        // ps.setTimestamp(i++, new java.sql.Timestamp(new java.util.Date().getTime()));
                        ps.setString(i++, userId);
                    } else if (headerUrlMapped == 1) {
                        //UPDATE isactive = 1

                        query = "UPDATE map_scheme_role_url SET  is_active = ?  WHERE  scheme_id = ? AND  role_id = ? AND url_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, 1);
                        ps.setInt(i++, scheme_id);
                        ps.setInt(i++, user_role);
                        ps.setInt(i++, header_id);
                    }

                    if (edit == 0) {

                        query = "INSERT INTO map_scheme_role_url(scheme_id, role_id, url_id, is_active, priority_no, mapping_time, mapped_by)\n"
                                + "    VALUES (?, ?, ?, ?, ?, now(), ?);";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, scheme_id);
                        ps.setInt(i++, user_role);
                        ps.setInt(i++, url);
                        ps.setInt(i++, is_active);
                        ps.setInt(i++, priority);
                        // ps.setTimestamp(i++, new java.sql.Timestamp(new java.util.Date().getTime()));
                        ps.setString(i++, userId);

                    } else if (edit == 1) {
                        query = "UPDATE map_scheme_role_url SET  is_active = ?, priority_no = ?  WHERE mapping_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, is_active);
                        ps.setInt(i++, priority);

                        ps.setInt(i++, map_id);
                    }
                    if (ps.executeUpdate() > 0) {
                        con.commit();
                        con.setAutoCommit(true);
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageUserRoleUrl.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    con.rollback();
                    con.setAutoCommit(true);
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("Admin/manageUserRoleUrl.jsp");
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

            if (request.getParameter("urlRoleList") != null) {
                roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                int schemeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("schemeId"))); //.split("~")[0]

                String sql = "SELECT  u.mapping_id, s.scheme_name, a.url, a.url_name, r.role_name,  u.is_active, u.priority_no, a.is_header  \n"
                        + "FROM map_scheme_role_url AS u  \n"
                        + "INNER JOIN master_scheme AS s ON u.scheme_id=s.id\n"
                        + "INNER JOIN mas_application_url AS a  ON u.url_id=a.url_id\n"
                        + "INNER JOIN bms_user.mas_user_roles AS r ON u.role_id=r.role_id "
                        + "WHERE u.scheme_id = " + schemeId + " AND u.role_id = " + roleId;

//                String sql = "select scheme_id,  url_id, role_id,  is_active, priority_no from map_scheme_role_url";
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

            if (request.getParameter("urlRoleEdit") != null) {
                //roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                map_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlRoleEdit")));

                String sql = "SELECT  s.url_id, u.header_id, s.is_active, s.priority_no\n"
                        + "FROM map_scheme_role_url AS s \n"
                        + "INNER JOIN mas_application_url AS u ON s.url_id=u.url_id\n"
                        + "WHERE s.mapping_id=" + map_id;

//                String sql = "select scheme_id,  url_id, role_id,  is_active, priority_no from map_scheme_role_url";
                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(ManageUserRole.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj.toString());
                out.flush();
                out.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/manageUserRoleUrl.jsp");
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

    public int checkHeaderMapping(int schemeId, int roleId, int headerUrlId, Connection con) throws SQLException {
        int stat = 0;
        String sql = "SELECT COUNT(*)  FROM map_scheme_role_url WHERE  scheme_id = " + schemeId + " AND  role_id = " + roleId + " AND url_id = " + headerUrlId + "";
        PreparedStatement ps2 = con.prepareStatement(sql);
        ResultSet rs3 = ps2.executeQuery();
        while (rs3.next()) {
            stat = rs3.getInt("count");
        }
        return stat;
    }
}
