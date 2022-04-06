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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "ManageRoleBasedOffice", urlPatterns = {"/ManageRoleBasedOffice"})
public class ManageRoleBasedOffice extends HttpServlet {

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
            int edit = 0, deptId = 0, groupId = 0, officeId = 0, parentOfficeId = 0;
            String message = "", dept_name = "", jurisdiction = "", officeName = "", lastMileAddress = "";
            int distId = 0, subDivId = 0, urbRurBodyId = 0, wardGpId = 0;
            Boolean lgdMapping = false;

            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;

            CommonMethod cm = new CommonMethod();
            if (request.getParameter("btnSubmit") != null) {
                edit = Integer.parseInt(request.getParameter("hdnEdit"));
                officeId = Integer.parseInt(request.getParameter("hdnOfficeId"));
                if (request.getParameter("ddlDepartment") != null) {
                    if (request.getParameter("ddlDepartment").trim().length() > 0) {
                        deptId = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlDepartment")));
                    } else {
                        message = "Department can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                        return;
                    }
                } else {
                    message = "Department can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                    return;
                }
//                if (request.getParameter("ddlGroup") != null) {
//                    if (request.getParameter("ddlGroup").trim().length() > 0) {
//                        groupId = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlGroup")));
//                    } else {
//                        message = "Group can not be blank.";
//                        session.setAttribute("RegMsg", message);
//                        session.setAttribute("type", "danger");
//                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
//                        return;
//                    }
//                } else {
//                    message = "Group can not be blank.";
//                    session.setAttribute("RegMsg", message);
//                    session.setAttribute("type", "danger");
//                    response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
//                    return;
//                }

                if (request.getParameter("txtOfficeName") != null) {
                    if (request.getParameter("txtOfficeName").trim().length() > 0) {
                        officeName = SecurityClass.killchar(request.getParameter("txtOfficeName"));
                    } else {
                        message = "Office can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                        return;
                    }
                } else {
                    message = "Office can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                    return;
                }

                /* if (request.getParameter("ddlParentOffice") != null) {
                    if (request.getParameter("ddlParentOffice").trim().length() > 0) {
                        parentOfficeId = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlParentOffice")));
                    } else {
                        message = "Parent office can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                        return;
                    }
                } else {
                    message = "Parent office can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                    return;
                }*/
                if (request.getParameter("ddlDistrict1") != null) {
                    distId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddlDistrict1")));
                }

                if (request.getParameter("ddlSubDiv1") != null) {
                    subDivId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddlSubDiv1")));
                }

                if (request.getParameter("ddlUrbRurBody") != null) {
                    urbRurBodyId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddlUrbRurBody")));
                }

                if (request.getParameter("ddlWardGp") != null) {
                    wardGpId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddlWardGp")));
                }

                if (request.getParameter("txtLastMileAddress") != null) {
                    if (request.getParameter("txtLastMileAddress").trim().length() > 0) {
                        lastMileAddress = SecurityClass.killchar(request.getParameter("txtLastMileAddress"));
                    } else {
                        message = "Last Mile Address can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                        return;
                    }
                } else {
                    message = "Last Mile Address can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                    return;
                }

                if (request.getParameter("LGDMapping") != null) {
                    lgdMapping = true;
                } else {
                    lgdMapping = false;
                }

                Connection con = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    if (edit == 0) {
                        query = "INSERT INTO bms_user.mas_dept_users_group_based_office(department_id, office_name, lgd_mapping_required, \n"
                                + "            dist_code, subdiv_code, block_code, ward_code, last_mile_address)\n"
                                + "    VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, deptId);
//                        ps.setInt(i++, groupId);
                        ps.setString(i++, officeName);
                        //ps.setInt(i++, parentOfficeId);
                        ps.setBoolean(i++, lgdMapping);
                        ps.setInt(i++, distId);
                        ps.setInt(i++, subDivId);
                        ps.setInt(i++, urbRurBodyId);
                        ps.setInt(i++, wardGpId);
                        ps.setString(i++, lastMileAddress);
                    } else if (edit == 1) {
                        query = "UPDATE bms_user.mas_dept_users_group_based_office\n"
                                + " SET office_name=?, lgd_mapping_required=?, dist_code=?, subdiv_code=?, block_code=?, ward_code=?, last_mile_address=? \n"
                                + "  WHERE department_id = ? AND office_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;

                        ps.setString(i++, officeName);
                        ps.setBoolean(i++, lgdMapping);
                        ps.setInt(i++, distId);
                        ps.setInt(i++, subDivId);
                        ps.setInt(i++, urbRurBodyId);
                        ps.setInt(i++, wardGpId);
                        ps.setString(i++, lastMileAddress);

                        ps.setInt(i++, deptId);
//                        ps.setInt(i++, groupId);
                        ps.setInt(i++, officeId);
                    }
                    if (ps.executeUpdate() > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    response.sendRedirect("Admin/manageRoleBasedOffice.jsp");
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

            } else if (request.getParameter("officeList") != null) {
                int dept_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
               // int group_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                Connection con = null;
                String sql = "SELECT offc.office_id, offc.office_name, "
                        + " offc.group_id, offc.department_id, department_name "
                        + " FROM bms_user.mas_dept_users_group_based_office AS offc \n"
                        + " INNER JOIN mas_department AS dept ON offc.department_id = dept.department_id \n"
                        //+ " INNER JOIN bms_user.mas_dept_users_group AS  usrGrp ON usrGrp.group_id = offc.group_id \n"
                        //+ " INNER JOIN mas_role_based_office AS offc2 ON offc2.office_id = offc.office_id \n"
                        + " WHERE offc.department_id = " + dept_id ;

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
            } else if (request.getParameter("MapUserOffice") != null) {
                int i = 0, result = 0;
                int dept_id = Integer.parseInt(request.getParameter("deptId"));
                int group_id = Integer.parseInt(request.getParameter("groupId"));
                int office_id = Integer.parseInt(request.getParameter("officeId"));
                int update = Integer.parseInt(request.getParameter("MapUserOffice"));
                String user_id = request.getParameter("userId");
                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                //String[] userids = null;
                // int result = 0;
                //  String myJsonData = request.getParameter("json");

                try {
                    result = cm.officeUserMapping(dept_id, group_id, office_id, user_id, update);
                    //  jsarr1 = new JSONArray(myJsonData);
                    // //userids = new String[jsarr1.length()];
                    //jsobj1.put("dbtBenList", jsarr1);
                    // for (int n = 0; n < jsarr1.length(); n++) {
                    // jsobj1 = jsarr1.getJSONObject(n);

                    //userids[i] = jsobj1.getString("user_id");
                    // result = cm.userGroupMapping(deptId, groupId, userids[i]);
                    // i++;
                    // }
                    //msg += "var result = \'" + result + "\';";
                } catch (Exception ex) {
                    ex.getMessage();
                }

                out.print(result);
                out.flush();
                out.close();

            } else if (request.getParameter("checkUser") != null) {
                String returnData = "";
                Connection con = null;
                int office_id = Integer.parseInt(request.getParameter("officeId"));
                // String user_id = request.getParameter("userId");
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String sql = "SELECT map.user_id, emp_name FROM bms_user.map_dept_users_group_based_office_users map INNER JOIN bms_user.mas_dept_users u ON map.user_id = u.user_id WHERE office_id = " + office_id;
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);
                    // ps = con.prepareStatement("SELECT user_id FROM bms_user.map_dept_users_group_based_office_users WHERE office_id = " + office_id);
                    // rs = ps.executeQuery();
                    if (rset.next()) {
                        json = new JSONObject();

                        json.put("user_id", rset.getString("user_id"));
                        json.put("emp_name", rset.getString("emp_name"));
                        returnData = json.toString();

                    } else {
//                        json = new JSONObject();
//
//                        
//                        returnData = json.toString();
                        returnData = "";
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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
                out.print(returnData);
                out.flush();
                out.close();
            } else if (request.getParameter("mappedList") != null) {
                int dept_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int group_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                Connection con = null;
                //String sql = "SELECT id,scheme_codification, scheme_name, COALESCE(component_name,'') AS component_name, department_id FROM master_scheme WHERE status = '1' AND map_status = '0' AND department_id = " + deptId;
                String sql = "SELECT map.user_id, emp_name, map.office_id, office_name FROM bms_user.map_dept_users_group_based_office_users map \n"
                        + "INNER JOIN bms_user.mas_dept_users_group_based_office  o ON o.office_id = map.office_id\n"
                        + "INNER JOIN bms_user.mas_dept_users u ON map.user_id = u.user_id WHERE map.department_id=" + dept_id;

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
