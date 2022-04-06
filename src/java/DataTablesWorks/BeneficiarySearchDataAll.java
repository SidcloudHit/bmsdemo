/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTablesWorks;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "BeneficiarySearchDataAll", urlPatterns = {"/BeneficiarySearchDataAll"})
public class BeneficiarySearchDataAll extends HttpServlet {

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
       response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Common.CommonMethod cm = new CommonMethod();
            Connection con = null;
            int distId = 0, subdivId = 0, officeId = 0, makerOfficeId = 0, checkerOfficeId = 0, blockId = 0, gpId = 0, storeId = 0, schemeId = 0, instl = 0, status = 0;
            String schemeCode = "", finYr = "", gpList = "", batchId = "",benList="";
            boolean noLand = false;

            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                schemeId = Integer.parseInt(scheme.split("~")[0]);
                schemeCode = scheme.split("~")[1];
            }

            if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
            }
            


          
            if (request.getParameter("benapplstatus") != null) {
                String tabName = "", tabName1 = "", tabName2 = "", tabName3 = "", rcMem = "", benName = "", appl = "", mobile = "",roleId="";
                CallableStatement cs = null;

                if (request.getParameter("rcMem") != null && !request.getParameter("rcMem").equals("")) {
                    rcMem = Security.SecurityClass.killchar(request.getParameter("rcMem"));
                }
                if (request.getParameter("appl") != null && !request.getParameter("appl").equals("")) {
                    appl = Security.SecurityClass.killchar(request.getParameter("appl"));
                }
                if (request.getParameter("benName") != null && !request.getParameter("benName").equals("")) {
                    benName = Security.SecurityClass.killchar(request.getParameter("benName"));
                }
                if (request.getParameter("mobile_no") != null && !request.getParameter("mobile_no").equals("")) {
                    mobile = Security.SecurityClass.killchar(request.getParameter("mobile_no"));
                }
                 if (request.getParameter("roleId") != null && !request.getParameter("roleId").equals("")) {
                    roleId = Security.SecurityClass.killchar(request.getParameter("roleId"));
                }
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    cs = con.prepareCall("{call beneficiary.searchBeneficiaryData(?,?,?,?,?,?,?,?,?)}");
                    cs.setInt(1, schemeId);
                    cs.setString(2, roleId);
                    cs.setInt(3, officeId);
                    cs.setString(4, rcMem);
                    cs.setString(5, appl);
                    cs.setString(6, benName);
                    cs.setString(7, mobile);
                    cs.setString(8, schemeCode);
                    cs.registerOutParameter(9, Types.VARCHAR);
                    cs.execute();
                    benList = cs.getString(9);
                   
                } catch (Exception e) {
                    e.getMessage();
                }
            if (benList == null) {
                    benList = "[{\"rc_mem\":\"\",\"full_name\":\"\",\"guardian_name\":\"\",\"email\":\"No Data Found\",\"district\":\"\",\"subdivision\":\"\",\"block_mc_np_adc_nonadc\":\"\",\"gp_ward_name\":\"\",\"mobile\":\"\",\"office\":\"\",\"status_name\":\"\"}]";
                }
//                int check_ben_status = 0, check_ben_status_enroll_draft = 0, check_ben_status_enroll_archive = 0;
//                String check_ben_status_enroll = "";
//                try {
//                    if (mobile.equals("")) {
//                        check_ben_status = cm.getBenApplStatus(tabName, schemeCode, rcMem, appl, benName);
//                    }
//                    if (check_ben_status == 0) {
//                        check_ben_status_enroll = cm.getBenApplStatus_inEnrollment(tabName1, schemeCode, rcMem, appl, benName, mobile);
//                    }
//                    if (check_ben_status_enroll.equals("") && check_ben_status == 0) {
//                        check_ben_status_enroll_draft = cm.getBenApplStatus_inDraft(tabName2, schemeCode, rcMem, appl, benName, mobile);
//                    }
//                    if (check_ben_status_enroll.equals("") && check_ben_status == 0 && check_ben_status_enroll_draft == 0) {
//                        check_ben_status_enroll_archive = cm.getBenApplStatus_inArchive(tabName3, schemeCode, rcMem, appl, benName, mobile);
//                    }
//                } catch (SQLException e) {
//                    e.getMessage();
//                }
//                if (check_ben_status > 0 || (!check_ben_status_enroll.equals(""))) {
//                    //no status selected
//                    sql = "SELECT "
//                            + "(SELECT email FROM citizen.registration WHERE id = a.entryby::integer) AS email,"
//                            + "(SELECT mobile FROM citizen.registration WHERE id = a.entryby::integer) AS mobile,"
//                            + "(SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = a.office_id) AS office,"
//                            + "a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name," + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district, \n"
//                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
//                            + "FROM " + tabName + " a   "
//                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
//                            + "WHERE  scheme_code='" + schemeCode + "' ";
//
//                    if (!benName.equals("")) {
//                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
//                    }
//
//                    if (!rcMem.equals("")) {
//                        sql += "AND a.rc_mem = '" + rcMem + "' ";
//                    }
//                    if (!appl.equals("")) {
//                        sql += "AND a.online_application_id = '" + appl + "' ";
//                    }
//                    if (!check_ben_status_enroll.equals("")) {
//                        sql += "AND a.online_application_id = '" + check_ben_status_enroll + "' ";
//                    }
//                } else if (check_ben_status_enroll_draft > 0) {
//                    sql += "SELECT (SELECT email FROM citizen.registration WHERE email = " + tabName2 + ".email) AS email,"
//                            + "(SELECT mobile FROM citizen.registration WHERE email = " + tabName2 + ".email) AS mobile,"
//                            + " (SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = " + tabName2 + ".office_id) AS office,"
//                            + "application_id as rc_mem, fullname as full_name, guardian_name as guardian_name, COALESCE(district,'') AS district, \n"
//                            + "COALESCE(subdiv,'') AS subdivision, COALESCE(block,'') AS block_mc_np_adc_nonadc,  \n"
//                            + "COALESCE(gp,'') AS gp_ward_name,'Draft' as status_name from " + tabName2 + " where scheme_id=" + schemeId + " ";
//
//                    if (!benName.equals("")) {
//                        sql += "AND fullname ILIKE '%" + benName + "%' ";
//                    }
//
//                    if (!rcMem.equals("")) {
//                        sql += "AND rc_member_id = '" + rcMem + "' ";
//                    }
//                    if (!appl.equals("")) {
//                        sql += "AND application_id = '" + appl + "' ";
//                    }
//                    if (!mobile.equals("")) {
//                        sql += "AND mobile_no = '" + mobile + "' ";
//                    }
//                    sql += " order by entry_datetime DESC limit 1";
//                } else {
//                    sql += "SELECT (SELECT email FROM citizen.registration WHERE email = " + tabName3 + ".email) AS email,"
//                            + "(SELECT mobile FROM citizen.registration WHERE email = " + tabName3 + ".email) AS mobile,"
//                            + " (SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = " + tabName3 + ".office_id) AS office,"
//                            + "application_id as rc_mem, fullname as full_name, guardian_name as guardian_name, COALESCE(district,'') AS district, \n"
//                            + "COALESCE(subdiv,'') AS subdivision, COALESCE(block,'') AS block_mc_np_adc_nonadc,  \n"
//                            + "COALESCE(gp,'') AS gp_ward_name,'Rejected' as status_name from " + tabName3 + " where scheme_id=" + schemeId + " ";
//
//                    if (!benName.equals("")) {
//                        sql += "AND fullname ILIKE '%" + benName + "%' ";
//                    }
//
//                    if (!rcMem.equals("")) {
//                        sql += "AND rc_member_id = '" + rcMem + "' ";
//                    }
//                    if (!appl.equals("")) {
//                        sql += "AND application_id = '" + appl + "' ";
//                    }
//                    if (!mobile.equals("")) {
//                        sql += "AND mobile_no = '" + mobile + "' ";
//                    }
//                }
                
            }
          
            /**
             * **************************************************************************************************************************************************************
             */
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            try {
                
                jsarr = new JSONArray(benList);
                jsobj.put("aaData", jsarr);
            } catch (Exception ex) {
                Logger.getLogger(BeneficiaryDataAll.class.getName()).log(Level.SEVERE, null, ex);
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
