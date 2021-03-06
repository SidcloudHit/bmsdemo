/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import static BeneficiarySelection.saveMatchdata.tableExist;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.time.Clock.system;
import java.util.Base64;
import java.util.Enumeration;
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
 * @author acer
 */
@WebServlet(name = "UpdateRCResponse", urlPatterns = {"/UpdateRCResponse"})
public class UpdateRCResponse extends HttpServlet {

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

        String responseStatus = "";
        HttpSession session = request.getSession();
        String userName = "", message = "", userId = "";
        int deptId = 0, officeId = 0, roleId = 0;

        if (session.getAttribute("loginInfo") != null) {

            SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

            userId = loginObj.getUserId();
            deptId = Integer.parseInt(loginObj.getUserDeptId());
            userName = loginObj.getUserName();
            roleId = Integer.parseInt(loginObj.getUserRole());
            officeId = Integer.parseInt(loginObj.getUserOfficeId());
        }

        if (userId.equals("")) {
            message = "Session time-out, please login again.";
            request.setAttribute("LogMsg", message);
            getServletContext().getRequestDispatcher("/Logout").forward(request, response);
            return;
        }
        //AdminUser usr = new AdminUser(userId);

        try (PrintWriter out = response.getWriter()) {

            String msg = "", appkey = "";
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            CallableStatement cs = null;
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String reqst = SecurityClass.stripXSS(request.getParameter("reqnumber"));
            String txt_ref = SecurityClass.stripXSS(request.getParameter("txn_ref_no"));
            String status = SecurityClass.stripXSS(request.getParameter("status"));
            String timestamp = SecurityClass.stripXSS(request.getParameter("tdate"));
            String uniqueid = SecurityClass.stripXSS(request.getParameter("uniqueid"));
            String errormsg = SecurityClass.stripXSS(request.getParameter("errormsg"));
            JSONObject req2 = RequestToJson(request);

            if ("success".equals(SecurityClass.stripXSS(request.getParameter("status")))) {

                //reqnumber + "|" + txn_ref_no + "|" + status + "|" + tdate 
                msg = reqst + "|" + txt_ref + "|" + status + "|" + timestamp;
                String sql = "SELECT * FROM public.tbl_integration_info where app_name_flag='rcverify'";
                ps1 = con.prepareStatement(sql);
                rs = ps1.executeQuery();
                while (rs.next()) {
                    appkey = rs.getString("unique_key");
                }
                Common.CommonMethod deocm = new Common.CommonMethod();
                String hashResponse = deocm.createToken(msg, appkey);

                String hashGet = request.getParameter("hash");
                // out.println("HasthGet  -"+hashGet);
                // out.println(" <br/>hashResponse -"+hashResponse);
                if (hashResponse.equals(hashGet)) {
                    responseStatus = "Hash Match";
                    //do success code here

                    JSONObject jsonObj = new JSONObject(URLDecoder.decode(req2.toString(), StandardCharsets.UTF_8.toString()));
                    JSONObject jsonObjDB = new JSONObject();

                    String address_lgd_gp_vc_ward_code = "", district = "", district_code = "", subdiv = "", subdiv_code = "", block = "", block_code = "", ward_gp = "", ward_gp_code = "";
                    address_lgd_gp_vc_ward_code = jsonObj.getString("address_lgd_gp_vc_ward_code");
                    String schemeCode = jsonObj.getString("additional_param1");
                    int schemeId = Integer.parseInt(jsonObj.getString("additional_param3"));
                    if (address_lgd_gp_vc_ward_code != null && address_lgd_gp_vc_ward_code != "") {
                        sql = "SELECT dist_name,dist_code,  subdiv_name,subdiv_code,  block_name, block_code, block_bac_name, gp_vc_ward_name, gp_vc_ward_code FROM lgd.master_dist_sd_block_bac_gp where gp_vc_ward_code  = ?";
                        ps = con.prepareStatement(sql);
                        ps.setInt(1, Integer.parseInt(address_lgd_gp_vc_ward_code));
                        rs = ps.executeQuery();

                        while (rs.next()) {
                            district = rs.getString("dist_name");
                            district_code = rs.getString("dist_code");
                            subdiv = rs.getString("subdiv_name");
                            subdiv_code = rs.getString("subdiv_code");
                            block = rs.getString("block_name");
                            block_code = rs.getString("block_code");
                            ward_gp = rs.getString("gp_vc_ward_name");
                            ward_gp_code = rs.getString("gp_vc_ward_code");
                        }
                    }

                    jsonObjDB.put("postrcvalidation_fullname", jsonObj.getString("fullname"));
                    jsonObjDB.put("full_name", jsonObj.getString("fullname"));
                    jsonObjDB.put("father_name", jsonObj.getString("guardian_name"));
                    jsonObjDB.put("postrcvalidation_distlgdname", district);
                    jsonObjDB.put("postrcvalidation_subdivlgdname", subdiv);
                    jsonObjDB.put("postrcvalidation_blocklgdname", block);
                    jsonObjDB.put("postrcvalidation_gplgdname", ward_gp);
                    jsonObjDB.put("gender", jsonObj.getString("gender"));
                    jsonObjDB.put("dob", jsonObj.getString("dob"));
                    jsonObjDB.put("caste", jsonObj.getString("caste"));
                    jsonObjDB.put("bank_account_number", jsonObj.getString("bank_accno"));
                    jsonObjDB.put("ifsc_code", jsonObj.getString("ifscode"));
                    jsonObjDB.put("pid", "");
                    jsonObjDB.put("scheme_name", jsonObj.getString("additional_param2"));
                    jsonObjDB.put("scheme_code", jsonObj.getString("additional_param1"));
                    jsonObjDB.put("postrcvalidation_distlgdcode", district_code);
                    jsonObjDB.put("postrcvalidation_subdivlgdcode", subdiv_code);
                    jsonObjDB.put("postrcvalidation_blocklgdcode", block_code);
                    jsonObjDB.put("postrcvalidation_gplgdcode", ward_gp_code);
                    jsonObjDB.put("postrcvalidation_duplicate_in_rc", "false");
                    jsonObjDB.put("postrcvalidation_missing_in_rc", "false");
                    jsonObjDB.put("postrcvalidation_rc_no", jsonObj.getString("rationcard_memberid").substring(0, 12));
                    jsonObjDB.put("postrcvalidation_rc_member_id", jsonObj.getString("rationcard_memberid"));
                    jsonObjDB.put("entryby", userId);
                    jsonObjDB.put("office_id", String.valueOf(officeId));
                    jsonObjDB.put("source_code", "2");

                    String query = "update rc_verification set status= ?, full_respose_req=?,txtrefno = ?, response_date= CURRENT_TIMESTAMP where id=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, status);
                    ps.setString(2, URLDecoder.decode(req2.toString(), StandardCharsets.UTF_8.toString()));
                    ps.setString(3, txt_ref);
                    ps.setInt(4, Integer.parseInt(uniqueid));
                    int update = ps.executeUpdate();
                       
                    String tabNamedata = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                    boolean exist_table = tableExist("beneficiary", tabNamedata);
                    int isdublicaterc = 0;
                    String benUploadby = "";
                    if (exist_table) {
//                        query = "SELECT b.emp_name, a.sl_no,a.rc_mem, a.entryby, a.office_id, a.entrydate, a.status, a.source_code \n"
//                                + "FROM beneficiary." + tabNamedata + " a,bms_user.mas_dept_users b \n"
//                                + "where a.entryby = b.user_id and a.rc_mem = ?";
//                        ps = con.prepareStatement(query);
//                        ps.setString(1, jsonObj.getString("rationcard_memberid"));
//                        rs = ps.executeQuery();
//
//                        while (rs.next()) {
//                            benUploadby = rs.getString("emp_name");
//                            isdublicaterc++;
//                        }
//                    }
//                    if (isdublicaterc > 0) {
//                        responseStatus = "This RC Member id is already uploaded. [Uploaded by " + benUploadby + "]";
//
//                    } else {
//                        int x = 0;
//                        cs = con.prepareCall("{call beneficiary.insert_beneficiary_selection_to_data(?,?,?,?::json)}");
//                        cs.setInt(++x, 0);
//                        cs.setString(++x, "");
//                        cs.setString(++x, tabNamedata);
//                        cs.setString(++x, jsonObjDB.toString());
//                        cs.execute();

                        int x = 0;
                        cs = con.prepareCall("{call beneficiary.update_beneficiary_selection_to_data(?,?,?,?::json)}");
                        
                        cs.setInt(++x, 0);
                        cs.setString(++x, "");
                        cs.setString(++x, tabNamedata);
                        cs.setString(++x, jsonObjDB.toString());
                        out.print(cs);
                        cs.execute();
                       
                    }
                   

                } else {
                    responseStatus = "Hash Not Match [" + errormsg + "]";
                    //do hash not match code here                   
                    String query = "update rc_verification set status= ?, full_respose_req=?,txtrefno = ?,error_msg = ?, response_date= CURRENT_TIMESTAMP where id=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, status); // status= success
                    ps.setString(2, req2.toString());
                    ps.setString(3, txt_ref);
                    ps.setString(4, "hash not match");
                    ps.setInt(5, Integer.parseInt(uniqueid));
                    int update = ps.executeUpdate();

                }
            } else {
                //do fail code here
                responseStatus = "Request Unsuccessfull [" + errormsg + "]";
                if (uniqueid != null) {
                    String query = "update rc_verification set status= ?, full_respose_req=?,txtrefno = ?,error_msg= ?, response_date= CURRENT_TIMESTAMP where id=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, status);
                    ps.setString(2, req2.toString());
                    ps.setString(3, txt_ref);
                    ps.setString(4, errormsg);
                    ps.setInt(5, Integer.parseInt(uniqueid));
                    int update = ps.executeUpdate();
                }
            }
            response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp?txt_ref=" + Base64.getUrlEncoder().encodeToString(txt_ref.getBytes("utf-8")) + "&reqs=" + Base64.getUrlEncoder().encodeToString(responseStatus.getBytes("utf-8")));

        } catch (SQLException ex) {
            Logger.getLogger(SaveRCResponse.class.getName()).log(Level.SEVERE, null, ex);
            responseStatus = "Request Unsuccessfull";
        } catch (Exception ex) {
            Logger.getLogger(SaveRCResponse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //  response.sendRedirect("DEO/beneficiaryDataEntry.jsp?reqs=" + Base64.getUrlEncoder().encodeToString(responseStatus.getBytes("utf-8")));
        }

    }

    String RequestToString(HttpServletRequest request) throws Exception {
        Enumeration en = request.getParameterNames();
        String str = "";
        while (en.hasMoreElements()) {
            String paramName = (String) en.nextElement();
            String paramValue = request.getParameter(paramName);
            str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
        }
        if (str.length() > 0) {
            str = str.substring(1);
        }
        return str;
    }

    JSONObject RequestToJson(HttpServletRequest request) throws Exception {
        Enumeration en = request.getParameterNames();
        String str = "[{";
        while (en.hasMoreElements()) {
            String paramName = (String) en.nextElement();
            String paramValue = request.getParameter(paramName);
            str = str + "\"" + paramName + "\" : \"" + URLEncoder.encode(paramValue, "UTF-8") + "\",";
        }
        str = str + "}]";

        JSONArray jsonArr = new JSONArray(str);
        JSONObject jsonObj = null;
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonObj = jsonArr.getJSONObject(i);

        }
        return jsonObj;
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
