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
 * @author acer
 */
@WebServlet(name = "WorkflowMapping", urlPatterns = {"/WorkflowMapping"})
public class WorkflowMapping extends HttpServlet {

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
            String message = "", user = "", loginId = "", admin_login_id = "";
            int edit = 0, deptId = 0, schemeId = 0, roleId = 0, workflow = 0, groupId = 0, officeId = 0, pDeptId = 0, pGroupId = 0, pOfficeId = 0;
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
//                    officeId = Integer.parseInt(loginObj.getUserOfficeId());

                }

                if (userId.equals("")) {
                    message = "Session time-out, please login again.";
                    request.setAttribute("LogMsg", message);
                    getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                    return;
                }

                deptId = Integer.parseInt(SecurityClass.killchar(request.getParameter("deptId")));
                schemeId = Integer.parseInt(SecurityClass.killchar(request.getParameter("schemeId")));

                workflow = Integer.parseInt(SecurityClass.killchar(request.getParameter("workflow")));
                groupId = Integer.parseInt(SecurityClass.killchar(request.getParameter("groupId")));
                officeId = Integer.parseInt(SecurityClass.killchar(request.getParameter("officeId")));

                //officeIds = SecurityClass.killchar(request.getParameter("officeId")).toString().split(",");
                pDeptId = Integer.parseInt(SecurityClass.killchar(request.getParameter("pDeptId")));
                pGroupId = Integer.parseInt(SecurityClass.killchar(request.getParameter("pGroupId")));
                pOfficeId = Integer.parseInt(SecurityClass.killchar(request.getParameter("pOfficeId")));

                roleId = cm.getSchemeGroupMappedRoleId(schemeId, groupId);

                loginId = SecurityClass.killchar(request.getParameter("loginId"));

                if (edit == 0) {
//                        if (officeIds != null) {
//                            for (int j = 0; j < officeIds.length; j++) {
                    result = userGroupSchemeOffice(deptId, schemeId, workflow, groupId, roleId, officeId, pDeptId, pGroupId, pOfficeId, loginId);
//                            }
//                            //result = 1;
//                        }
                }
                out.print(result);

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

    public int userGroupSchemeOffice(int deptId, int schemeId, int workflow, int groupId, int roleId, int officeId, int pDeptId, int pGroupId, int pOfficeId, String loginId) {

        int stat = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "INSERT INTO bms_user.map_admin_workflow( "
                + " scheme_id, mapping_type, department_id, group_id, role_id, office_id, parent_department_id, parent_group_id, parent_office_id, login_id) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            int i = 1;

            ps.setInt(i++, schemeId);
            ps.setInt(i++, workflow);
            ps.setInt(i++, deptId);
            ps.setInt(i++, groupId);
            ps.setInt(i++, roleId);
            ps.setInt(i++, officeId);
            ps.setInt(i++, pDeptId);
            ps.setInt(i++, pGroupId);
            ps.setInt(i++, pOfficeId);
            ps.setString(i++, loginId);
            //ps.setInt(i++, 1);
            //rs = ps.executeQuery();
            if (ps.executeUpdate() > 0) {
                stat = 1;
            }
        } catch (SQLException ex) {
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
        return stat;
    }
}
