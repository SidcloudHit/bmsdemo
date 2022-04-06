/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
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
import org.json.JSONObject;

/**
 *
 * @author FINANCE-PC
 */
@WebServlet(name = "saveMatchdata", urlPatterns = {"/saveMatchdata"})
public class saveMatchdata extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        Connection con = null;
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String message = "";
        int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
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
        String[] match_check = SecurityClass.killchar(request.getParameter("match_val")).split("~");
        String match_val = match_check[0];
        String slno = match_check[1]; //slno
        String schemeCode = match_check[2].toLowerCase();
        String rc = match_check[3];
        String uniqueidApi = match_check[4]; //tempid
        schemeId = Integer.parseInt(match_check[5]);

        String tempdetailsString = "";

        if ("M".equals(match_val)) {

            String query_upd = "update beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp set status = 1 where sl_no= ?";
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement(query_upd);
            ps.setInt(1, Integer.parseInt(slno));
            int update = ps.executeUpdate();
            if (update == 1) {

                out.print("ok");

            }

        }
        if ("O".equals(match_val)) {
            try {
                String query_upd = "update beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp set status = 0 where sl_no= ?";
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                ps = con.prepareStatement(query_upd);
                ps.setInt(1, Integer.parseInt(slno));
                int update = ps.executeUpdate();
                if (update == 1) {

                    out.print("ok");

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {

                ps.close();

                con.close();
            }
        }

        if ("V".equals(match_val)) {

//            String query_upd = "update beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp set status = 2 where sl_no= ?";
//            con = new DBCon.DBSource().connectToBMSDB().getConnection();
//            ps = con.prepareStatement(query_upd);
//            ps.setInt(1, Integer.parseInt(slno));
//            int update = ps.executeUpdate();
            String returb = saveMatchdata.copytoData(schemeId, schemeCode, uniqueidApi, userId);
            if (returb.equals("ok")) {
                out.print("ok");

            }

        }
        if ("D".equals(match_val)) {

            String query_upd = "update beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp set status = 3 where sl_no= ?";
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement(query_upd);
            ps.setInt(1, Integer.parseInt(slno));
            int update = ps.executeUpdate();
            if (update == 1) {
                out.print("ok");

            }

        }

    }

    public static String copytoData(int schemeId, String schemeCode, String uniqueid, String userId) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        String tempdetailsString = "", responseStatus = "";
        int update = 0;
        try {
            String query_set = "SELECT to_json(array_agg(details)) as tempdetails FROM (SELECT *  FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp where tempid='" + uniqueid + "') details";

            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement(query_set);
            rs = ps.executeQuery();
            while (rs.next()) {
                tempdetailsString = rs.getString("tempdetails");
            }
            JSONArray tempdetails = new JSONArray(tempdetailsString);
            if (tempdetails.length() > 0) {
                JSONObject tempdetailsobj = tempdetails.getJSONObject(0);
                tempdetailsobj.put("source_code", "1");
                String tabNamedata = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                String tabNameDataTemp = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
                try {
                    int x = 0;

                    String tabNamedata2 = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                    boolean exist_table = tableExist("beneficiary", tabNamedata2);
                    int isduplicaterc = 0;
                    String benUploadby = "";
                    if (exist_table) {
                        String query = "SELECT b.emp_name, a.sl_no,a.rc_mem, a.entryby, a.office_id, a.entrydate, a.status, a.source_code \n"
                                + "FROM beneficiary." + tabNamedata + " a,bms_user.mas_dept_users b \n"
                                + "where a.entryby = b.user_id and a.rc_mem = ?";
                        ps = con.prepareStatement(query);
                        ps.setString(1, tempdetailsobj.getString("postrcvalidation_rc_member_id"));
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            benUploadby = rs.getString("emp_name");
                            isduplicaterc++;
                        }
                    }
                    if (isduplicaterc > 0) {
                        responseStatus = "This RC member id is already uploaded. Uploaded by " + benUploadby;
                        String query_upd = "UPDATE beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp SET is_duplicate = '1' WHERE tempid = ?";
                        ps = con.prepareStatement(query_upd);
                        ps.setString(1, uniqueid);
                        ps.executeUpdate();

                    } else {

                        cs = con.prepareCall("{call beneficiary.insert_beneficiary_selection_to_data_from_temp(?,?,?,?,?::json)}");
                        cs.setString(++x, uniqueid);
                        cs.setString(++x, tabNameDataTemp);
                        cs.setString(++x, tabNamedata);
                        cs.setString(++x, userId);
                        cs.setString(++x, tempdetailsobj.toString());
                        cs.execute();

//                        String query_upd = "update beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp set status = 2, matched_by =?, matched_datetime = CURRENT_TIMESTAMP where tempid= ?";
//                        ps = con.prepareStatement(query_upd);
//                        ps.setString(1, userId);
//                        ps.setString(2, uniqueid);
//                        update = ps.executeUpdate();
                        update++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            rs.close();

            ps.close();

            con.close();
        }
        if (update > 0) {
            return ("ok");

        } else if (!responseStatus.equals("")) {
            return responseStatus;
        } else {
            return null;
        }

    }

    public static boolean tableExist(String schema, String tblname) {
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
            Logger.getLogger(saveMatchdata.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(saveMatchdata.class.getName()).log(Level.SEVERE, null, ex);
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
