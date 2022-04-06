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
@WebServlet(name = "deptOnboardedlist", urlPatterns = {"/deptOnboardedlist"})
public class deptOnboardedlist extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            Connection con = null;
            CallableStatement cs = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String dept_str_list = "";
            int deptid = 0;
            String flag = "";
            if (request.getParameter("flag") != null && !"".equals(request.getParameter("flag"))) {
                flag = request.getParameter("flag");
            }
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            errorlog log=new errorlog();
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                if (flag != "") {
                    cs = con.prepareCall("{call department_onboarding.getOnboardedDeptList(?,?)}");
                    cs.setString(1, flag);
                    cs.registerOutParameter(2, Types.VARCHAR);
                    cs.execute();
                    dept_str_list = cs.getString(2);
                } else {
                    cs = con.prepareCall("{call department_onboarding.getOnboardedDeptList(?)}");
                    cs.registerOutParameter(1, Types.VARCHAR);
                    cs.execute();
                    dept_str_list = cs.getString(1);
                }

                if (dept_str_list == null) {
                    dept_str_list = "[{\"department_name\":\"No Data Found\",\"status\":\"\"}]";
                }
                jsarr = new JSONArray(dept_str_list);
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
