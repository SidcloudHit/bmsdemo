/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Department;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import log.errorlog;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "userList", urlPatterns = {"/userList"})
public class userList extends HttpServlet {

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
            String user_type = "", dept_id = "";
            if (request.getParameter("user_type") != null && !"".equals(request.getParameter("user_type"))) {
                user_type = request.getParameter("user_type");
            }
            if (request.getParameter("dept_id") != null && !"".equals(request.getParameter("dept_id"))) {
                dept_id = request.getParameter("dept_id");
            }
            errorlog log=new errorlog();
            Connection con = null;
            CallableStatement cs = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String user_str_list = "";
            int deptid = 0;
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                cs = con.prepareCall("{call department_onboarding.getUserList(?,?,?)}");
                cs.setString(1, user_type);
                cs.setString(2, dept_id);
                cs.registerOutParameter(3, Types.VARCHAR);

                cs.execute();
                user_str_list = cs.getString(3);
                if (user_str_list == null) {
                    user_str_list = "[{\"emp_name\":\"\",\"emp_designation\":\"No Data Found\",\"department_name\":\"\"}]";
                }
                jsarr = new JSONArray(user_str_list);
                jsobj.put("aaData", jsarr);
            } catch (Exception e) {
                e.printStackTrace();
                 int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
            }
            out.print(jsobj);
            out.flush();
            out.close();
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
