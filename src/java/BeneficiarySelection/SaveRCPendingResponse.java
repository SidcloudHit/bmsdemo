/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author FINANCE-PC
 */
@WebServlet(name = "SaveRCPendingResponse", urlPatterns = {"/SaveRCPendingResponse"})
public class SaveRCPendingResponse extends HttpServlet {

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
        String responseStatus = "";
        HttpSession session = request.getSession();
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
        try (PrintWriter out = response.getWriter()) {

            String msg = "", appkey = "", bankacno = "", ifsc_code = "", rationcard_memberid = "", reqst = "", txt_ref = "",
                    status = "", timestamp = "", uniqueid = "", errormsg = "", hashGet = "", fullname = "", guardian_name = "",
                    relation_with_guardian = "", dob = "", gender = "", caste = "", pincode = "", address_lgd_gp_vc_ward_code = "",
                    email = "", mobile = "", schemeCode = "", additional_param2 = "";
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            if (request.getParameter("reqnumber") != null) {
                reqst = SecurityClass.stripXSS(request.getParameter("reqnumber"));
            }
            if (request.getParameter("txn_ref_no") != null) {
                txt_ref = SecurityClass.stripXSS(request.getParameter("txn_ref_no"));
            }
            if (request.getParameter("status") != null) {
                status = SecurityClass.stripXSS(request.getParameter("status"));
            }
            if (request.getParameter("tdate") != null) {
                timestamp = SecurityClass.stripXSS(request.getParameter("tdate"));
            }
            if (request.getParameter("uniqueid") != null) {
                uniqueid = SecurityClass.stripXSS(request.getParameter("uniqueid"));
            }
            if (request.getParameter("errormsg") != null) {
                errormsg = SecurityClass.stripXSS(request.getParameter("errormsg"));
            }
            if (request.getParameter("hash") != null) {
                hashGet = request.getParameter("hash");
            }
            if (request.getParameter("rationcard_memberid") != null) {

                rationcard_memberid = request.getParameter("rationcard_memberid");
            }
            if (request.getParameter("fullname") != null) {
                fullname = request.getParameter("fullname");
            }

            // String fullname_ll = (request.getParameter("fullname_ll") == null || request.getParameter("fullname_ll") == "") ? request.getParameter("fullname_ll") : URLDecoder.decode(request.getParameter("fullname_ll"), StandardCharsets.UTF_8.toString());
            if (request.getParameter("guardian_name") != null) {
                guardian_name = request.getParameter("guardian_name");
            }
            // String guardian_name_ll = (request.getParameter("guardian_name_ll") == null || request.getParameter("guardian_name_ll") == "") ? request.getParameter("guardian_name_ll") : URLDecoder.decode(request.getParameter("guardian_name_ll"), StandardCharsets.UTF_8.toString());

            if (request.getParameter("relation_with_guardian") != null) {
                relation_with_guardian = request.getParameter("relation_with_guardian");
            }
            if (request.getParameter("dob") != null) {
                dob = request.getParameter("dob");
            }
            if (request.getParameter("gender") != null) {
                gender = request.getParameter("gender");
            }
            if (request.getParameter("caste") != null) {
                caste = request.getParameter("caste");
            }
            if (request.getParameter("pincode") != null) {
                pincode = request.getParameter("pincode");
            }
            if (request.getParameter("address_lgd_gp_vc_ward_code") != null) {
                address_lgd_gp_vc_ward_code = SecurityClass.stripXSS(request.getParameter("address_lgd_gp_vc_ward_code"));
            }
            if (request.getParameter("email") != null) {
                email = request.getParameter("email");
            }
            if (request.getParameter("mobile") != null) {
                mobile = request.getParameter("mobile");
            }
            if (request.getParameter("bank_accno") != null) {
                bankacno = Security.SecurityClass.killchar(request.getParameter("bank_accno"));
            }
            if (request.getParameter("ifscode") != null) {
                ifsc_code = Security.SecurityClass.killchar(request.getParameter("ifscode"));
            }

            if (request.getParameter("additional_param1") != null) {
                schemeCode = request.getParameter("additional_param1");
            }
            if (request.getParameter("additional_param2") != null) {
                //additional_param2 = request.getParameter("additional_param2");
                schemeId = Integer.parseInt(request.getParameter("additional_param2"));
            }

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

                // out.println("HasthGet  -"+hashGet);
                // out.println(" <br/>hashResponse -"+hashResponse);
                if (hashResponse.equals(hashGet)) {
                    responseStatus = "";
                    //do success code here

                    String query = "update rc_verification set status= ?, full_respose_req=?,txtrefno = ?, response_date= CURRENT_TIMESTAMP where unique_id=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, status);
                    ps.setString(2, URLDecoder.decode(req2.toString(), StandardCharsets.UTF_8.toString()));
                    ps.setString(3, txt_ref);
                    ps.setString(4, uniqueid);
                    int update = ps.executeUpdate();

                    String district = "", district_code = "", subdiv = "", subdiv_code = "", block = "", block_code = "", ward_gp = "", ward_gp_code = "";
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

                    String scheme_table = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
                    boolean exist_table = false;
                    String query_set = "Select EXISTS(SELECT * FROM information_schema.tables "
                            + " WHERE  table_schema = 'beneficiary' AND    table_name = '" + scheme_table.split("\\.")[1] + "') as tableexist";
                    con = new DBCon.DBSource().connectToBMSDB().getConnection();
                    ps = con.prepareStatement(query_set);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        exist_table = rs.getBoolean("tableexist");
                    }
                    if (exist_table) {
                        String sqlUpd = "";

                        sqlUpd = "UPDATE " + scheme_table + " "
                                + " SET  father_name=?, gender=?, dob=?,"
                                + " caste=?, is_duplicate = NULL,"
                                // + " ifsc_code=?, bank_account_number=?,"
                                + " postrcvalidation_fullname=?, "
                                + " postrcvalidation_rc_member_id=?, "
                                + " postrcvalidation_distlgdcode=?, postrcvalidation_distlgdname=?, "
                                + " postrcvalidation_subdivlgdcode=?, postrcvalidation_subdivlgdname=?, "
                                + " postrcvalidation_blocklgdcode=?, postrcvalidation_blocklgdname=?, "
                                + " postrcvalidation_gplgdcode=?, postrcvalidation_gplgdname=?, "
                                + " postrcvalidation_rc_no=? "
                                //  + " approved_by=?, approved_datetime=?, "
                                + "	WHERE tempid = ?;";

                        ps = con.prepareStatement(sqlUpd);
                        ps.setString(1, guardian_name); // status= success
                        ps.setString(2, gender);
                        ps.setString(3, dob);
                        ps.setString(4, caste);
                        //  ps.setString(5, ifsc_code);
                        //  ps.setString(6, bankacno); 
                        ps.setString(5, fullname);
                        ps.setString(6, rationcard_memberid);
                        ps.setString(7, district_code);
                        ps.setString(8, district);
                        ps.setString(9, subdiv_code);
                        ps.setString(10, subdiv);
                        ps.setString(11, block_code);
                        ps.setString(12, block);
                        ps.setString(13, ward_gp_code);
                        ps.setString(14, ward_gp);
                        ps.setString(15, rationcard_memberid.substring(0, 12));
                        ps.setString(16, uniqueid);
                        update = ps.executeUpdate();

                        sql = " Select full_name from " + scheme_table + " where tempid ='" + uniqueid + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        String full_name_application = "";
                        while (rs.next()) {
                            full_name_application = rs.getString("full_name");

                        }
                        if (fullname.equals(full_name_application)) {
                            String returb = saveMatchdata.copytoData(schemeId,schemeCode, uniqueid, userId);
                            if ("ok".equals(returb)) {
                                responseStatus = "RC Verification Complete. [Full Name 100% match] ";
                            } else {
                                responseStatus = "<span style=\"color:red\">Request Unsuccessfull.[" + returb + "]</span>";
                            }
                        } else {
                            // update status -1. so it will show in match data page
                            String query_upd = "update beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp set status = 1 where tempid= ?";
                            ps = con.prepareStatement(query_upd);
                            ps.setString(1, uniqueid);
                            update = ps.executeUpdate();
                            responseStatus = "RC Verification Complete.<span style=\"color:red\"> [Full Name does not match 100%.Need to be verified by Checker.]</span>";
                        }

                    }
                } else {
                    responseStatus = "Hash Not Match ";
                    //do hash not match code here                   
                    String query = "update rc_verification set status= ?, full_respose_req=?,txtrefno = ?,error_msg = ?, response_date= CURRENT_TIMESTAMP where unique_id=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, status); // status= success
                    ps.setString(2, req2.toString());
                    ps.setString(3, txt_ref);
                    ps.setString(4, "hash not match");
                    ps.setString(5, uniqueid);
                    int update = ps.executeUpdate();

                }
            } else {
                //do fail code here
                responseStatus = "Request Unsuccessfull [Error: " + errormsg + "]";
                if (uniqueid != null && uniqueid != "") {
                    String query = "update rc_verification set status= ?, full_respose_req=?,txtrefno = ?,error_msg= ?, response_date= CURRENT_TIMESTAMP where unique_id=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, status);
                    ps.setString(2, req2.toString());
                    ps.setString(3, txt_ref);
                    ps.setString(4, errormsg);
                    ps.setString(5, uniqueid);
                    int update = ps.executeUpdate();
                }
            }
            response.sendRedirect("BeneficiarySelection/dataValidation.jsp?reqs=" + Base64.getUrlEncoder().encodeToString(responseStatus.getBytes("utf-8")) + "&err=" + Base64.getUrlEncoder().encodeToString(errormsg.getBytes("utf-8")));

        } catch (SQLException ex) {
            Logger.getLogger(SaveRCResponse.class.getName()).log(Level.SEVERE, null, ex);
            responseStatus = "Request Unsuccessfull";
        } catch (Exception ex) {
            Logger.getLogger(SaveRCResponse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //  response.sendRedirect("DEO/dataValidation.jsp?reqs=" + Base64.getUrlEncoder().encodeToString(responseStatus.getBytes("utf-8")));
        }
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
