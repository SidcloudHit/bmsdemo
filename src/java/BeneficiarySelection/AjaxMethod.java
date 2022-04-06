/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
 * @author FINANCE-PC
 */
@WebServlet(name = "AjaxMethod", urlPatterns = {"/AjaxMethod"})
public class AjaxMethod extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String username = "", message = "";
        int deptId = 0, roleId = 0, officeId = 0;
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

        String msg = "";
        String ilistArray = "";
        String ilistIDArray = "";
        Common.CommonMethod cm = new Common.CommonMethod();
        JSONArray jsarr = null;
        JSONObject jsobj = null;

        Connection con = null;
        CallableStatement cs = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        String query_set = "";
        ResultSet rs = null;
        if (request.getParameter("dbtmatch") != null) {
            int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtmatch")));
            String scheme = Security.SecurityClass.killchar(request.getParameter("schemeid"));
            String strength = Security.SecurityClass.killchar(request.getParameter("match_strength"));
            int schemeId = Integer.parseInt(scheme.split("~")[0]);
            String schemeCode = scheme.split("~")[1];
            String scheme_name = cm.getSchemeName(schemeId);
            int cnt = 0;
            int count = 1;
            boolean exist_table = false;

            String scheme_table = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
            String scheme_tablename = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
            exist_table = tableExist("beneficiary", scheme_tablename);
            int match_strength_per = 0;
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                if (exist_table) {
                    if (request.getParameter("match_strength") != "") {
                        match_strength_per = Integer.parseInt(request.getParameter("match_strength"));

                        if (request.getParameter("match_strength").equals("999")) {
                            query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) " //AND postrcvalidation_duplicate_in_rc ='false' AND postrcvalidation_missing_in_rc='false' 
                                    + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "'"
                                    + " AND office_id = " + officeId + " " //entryby='" + userId + "'
                                    + " AND benef_name_matchresult_dist_leven > '5' "
                                    + " AND status = -1 "
                                    + " AND is_duplicate IS NULL order by benef_name_matchresult_dist_leven ";
                        } else {
                            query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) " //AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false' 
                                    + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "'"
                                    + " AND office_id = " + officeId + " " //entryby='" + userId + "'
                                    + " AND benef_name_matchresult_dist_leven = '" + match_strength_per + "' "
                                    + " AND status = -1 "
                                    + " AND is_duplicate IS NULL order by benef_name_matchresult_dist_leven";
                        }

                    } else {
                        query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) " //AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false' 
                                + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' "
                                + " AND office_id = " + officeId + " " //entryby='" + userId + "'
                                + " AND status = -1 "
                                + " AND is_duplicate IS NULL order by benef_name_matchresult_dist_leven ";
                    }

                }

            } catch (Exception ex) {

            }

        }

        if (request.getParameter("dbtvalidate") != null) {
            int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtvalidate")));
            String scheme = Security.SecurityClass.killchar(request.getParameter("schemeid"));
            String validate_rule = Security.SecurityClass.killchar(request.getParameter("validate_type"));
            String match_strength = Security.SecurityClass.killchar(request.getParameter("match_strength"));
            int schemeId = Integer.parseInt(scheme.split("~")[0]);
            String schemeCode = scheme.split("~")[1];
            String scheme_name = cm.getSchemeName(schemeId);
            int cnt = 0;
            int count = 1;
            boolean exist_table = false;

            String scheme_table = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
            String scheme_tablename = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
            exist_table = tableExist("beneficiary", scheme_tablename);
            int match_strength_per = 0;
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                if (exist_table) {
                    if (validate_rule.equals("6")) {
                        if (match_strength != "") {
                            match_strength_per = Integer.parseInt(request.getParameter("match_strength"));
                            if (match_strength.equals("999")) {
                                query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) "
                                        //+ " AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false' "
                                        + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' AND office_id = " + officeId + " AND status = 0 AND benef_name_matchresult_dist_leven > '5' "
                                        + " AND is_duplicate IS NULL order by sl_no ";
                            } else {
                                query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) "
                                        //+ " AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false' "
                                        + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' AND office_id = " + officeId + "  AND status = 0 AND benef_name_matchresult_dist_leven='" + match_strength_per + "' "
                                        + " AND is_duplicate IS NULL order by sl_no ";
                            }
                        } else {
                            query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) "
                                    //+ " AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false' "
                                    + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' AND status = 0 AND office_id = " + officeId + " "
                                    + " AND is_duplicate IS NULL order by sl_no ";
                        }
                    } else if (validate_rule.equals("2")) {
                        query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id ='' OR postrcvalidation_rc_member_id IS NULL ) "
                                + " AND is_duplicate IS NULL AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "'  AND office_id = " + officeId + " " //entryby='" + userId + "'
                                + "AND status = -1 order by sl_no  "; //postrcvalidation_missing_in_rc='true' And 
                    } else if (validate_rule.equals("7")) {
                        query_set = "SELECT * FROM " + scheme_table + " WHERE  scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "'  AND  status = 3  AND office_id = " + officeId + " " //entryby='" + userId + "'
                                + " order by sl_no "; //AND is_duplicate IS NULL 
                    } else if (validate_rule.equals("8")) {
                        query_set = "SELECT * FROM " + scheme_table + " WHERE scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' AND office_id = " + officeId + " " //entryby='" + userId + "'
                                + "AND is_duplicate IS NOT NULL  order by sl_no ";
                    }

                }

            } catch (Exception ex) {

            }

        }
        if (request.getParameter("dbtcheckermatch") != null) {
            int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtcheckermatch")));
            String scheme = Security.SecurityClass.killchar(request.getParameter("schemeid"));
            String strength = Security.SecurityClass.killchar(request.getParameter("match_strength"));
            int schemeId = Integer.parseInt(scheme.split("~")[0]);
            String schemeCode = scheme.split("~")[1];
            String scheme_name = cm.getSchemeName(schemeId);
            int cnt = 0;
            int count = 1;
            boolean exist_table = false;

            String makerOfficeList = cm.getChildOfficeList(schemeId, 1, officeId, 1);

            String scheme_table = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
            String scheme_tablename = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
            exist_table = tableExist("beneficiary", scheme_tablename);
            int match_strength_per = 0;
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                if (exist_table) {
                    if (request.getParameter("match_strength") != "") {
                        match_strength_per = Integer.parseInt(request.getParameter("match_strength"));

                        if (request.getParameter("match_strength").equals("999")) {
                            query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) " //AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false'
                                    + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' "
                                    + " AND office_id IN (" + makerOfficeList + ") "
                                    + " AND benef_name_matchresult_dist_leven > 5 "
                                    + " AND status = 1 "
                                    + " AND is_duplicate IS NULL order by benef_name_matchresult_dist_leven ";
                        } else {
                            query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) " //AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false'
                                    + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' "
                                    + " AND office_id IN (" + makerOfficeList + ") "
                                    + " AND benef_name_matchresult_dist_leven = " + match_strength_per + " "
                                    + " AND status = 1 "
                                    + " AND is_duplicate IS NULL order by benef_name_matchresult_dist_leven";
                        }

                    } else {
                        query_set = "SELECT * FROM " + scheme_table + " WHERE ( postrcvalidation_rc_member_id <> '' AND postrcvalidation_rc_member_id IS NOT NULL ) " //AND postrcvalidation_duplicate_in_rc='false' AND postrcvalidation_missing_in_rc='false'
                                + " AND scheme_id=" + schemeId + " AND scheme_code='" + schemeCode + "' "
                                + " AND office_id IN (" + makerOfficeList + ") "
                                + " AND status = 1 "
                                + " AND is_duplicate IS NULL order by benef_name_matchresult_dist_leven ";
                    }

                }

            } catch (Exception ex) {

            }

        }

        jsobj = new JSONObject();
        try {
            jsarr = cm.getResultJSON(query_set, con);
            if (jsarr == null) {
                jsobj.put("aaData", "[]");
            } else {
                jsobj.put("aaData", jsarr);
            }
        } catch (Exception ex) {
            jsobj.put("aaData", "[]");
            Logger.getLogger(AjaxMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.print(jsobj);
    }

    public boolean tableExist(String schema, String tblname) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exist_table = false;
        try {

            String query = "Select EXISTS(SELECT * FROM information_schema.tables "
                    + " WHERE  table_schema = '" + schema + "' AND    table_name = '" + tblname + "') as tableexist";
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                exist_table = rs.getBoolean("tableexist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AjaxMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exist_table;
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

        } catch (SQLException ex) {
            Logger.getLogger(AjaxMethod.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(AjaxMethod.class.getName()).log(Level.SEVERE, null, ex);
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

        } catch (SQLException ex) {
            Logger.getLogger(AjaxMethod.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(AjaxMethod.class.getName()).log(Level.SEVERE, null, ex);
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
