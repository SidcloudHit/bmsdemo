/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
import log.errorlog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "saveSchemePaymentConfigurationInfo", urlPatterns = {"/saveSchemePaymentConfigurationInfo"})
public class saveSchemePaymentConfigurationInfo extends HttpServlet {

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
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Common.CommonMethod cm = new CommonMethod();
            errorlog log = new errorlog();
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            int status = 0;
            String scheme_id = "", calc_method_type = "", scheme_publish = "";

            CallableStatement cs = null;
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0, result = 0;
            String userId = "", userName = "", userDes = "";
            JSONObject calc_method_param_value_json = new JSONObject();
            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                if (loginObj.getUserRole() != null && loginObj.getUserRole() != "") {
                    roleId = Integer.parseInt(loginObj.getUserRole());
                }
                if (loginObj.getUserOfficeId() != null && loginObj.getUserOfficeId() != "") {
                    officeId = Integer.parseInt(loginObj.getUserOfficeId());
                }

            }

            if (request.getParameter("scheme_id") != null && !"".equals(request.getParameter("scheme_id"))) {
                scheme_id = request.getParameter("scheme_id").trim();
            }
            if (request.getParameter("method") != null && !"".equals(request.getParameter("method"))) {
                calc_method_type = request.getParameter("method").trim();
            }

            JSONArray textbox_array = new JSONArray();
            JSONArray textbox_mainobj = new JSONArray();
            String rate = request.getParameter("rate");
            String unit = request.getParameter("unit");

            JSONObject lablobj = new JSONObject();
            JSONArray mainjson = new JSONArray();
            lablobj.put("rate", rate);
            lablobj.put("unit", unit);
            mainjson.put(lablobj);

            calc_method_param_value_json.put("calc_method_param_json_array", mainjson.toString());

            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                String query = "INSERT INTO scheme_onboarding.scheme_ob_payment_configuration(scheme_id, calc_method_type, calc_method_param_value_json) "
                        + "VALUES (?, ?, ?)";

                ps = con.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(scheme_id));
                ps.setString(2, calc_method_type);
                ps.setString(3, calc_method_param_value_json.toString());

                if (ps.executeUpdate() > 0) {

                    result = 1;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                    }
                    con = null;
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                    }
                }
                if (ps1 != null) {
                    try {
                        ps1.close();
                    } catch (SQLException ex) {
                    }
                }
            }

            if (status > 0) {
                out.print(result);
            }

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
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(saveSchemePaymentConfigurationInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(saveSchemePaymentConfigurationInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
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
