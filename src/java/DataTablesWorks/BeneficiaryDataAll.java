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
 * @author NIC-Arindam
 */
@WebServlet(name = "BeneficiaryDataAll", urlPatterns = {"/BeneficiaryDataAll"})
public class BeneficiaryDataAll extends HttpServlet {

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
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Common.CommonMethod cm = new CommonMethod();
            Connection con = null;
            int distId = 0, subdivId = 0, officeId = 0, makerOfficeId = 0, checkerOfficeId = 0, blockId = 0, gpId = 0, storeId = 0, schemeId = 0, instl = 0, status = 0, roleId = 0;

            int ddlbeneficiaryvalidation = 0;
            String schemeCode = "", finYr = "", gpList = "", batchId = "";
            boolean noLand = false;

            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                schemeId = Integer.parseInt(scheme.split("~")[0]);
                schemeCode = scheme.split("~")[1];
            }

            if (request.getParameter("distId") != null && !request.getParameter("distId").equals("")) {
                distId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("distId")));
            }
            if (request.getParameter("subdivId") != null && !request.getParameter("subdivId").equals("") && !request.getParameter("subdivId").equals("-1")) {
                subdivId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("subdivId")));
            }
            if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
            }
            if (request.getParameter("blockId") != null && !request.getParameter("blockId").equals("") && !request.getParameter("blockId").equals("-1")) {
                blockId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("blockId")));
            }
            if (request.getParameter("gpId") != null && !request.getParameter("gpId").equals("") && !request.getParameter("gpId").equals("-1")) {
                gpId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("gpId")));
            }
            if (request.getParameter("storeId") != null && !request.getParameter("storeId").equals("") && !request.getParameter("storeId").equals("-1")) {
                storeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("storeId")));
                gpList = cm.getGpListUnderVLWStore(storeId);
            }

            String sql = "";

            /**
             * **************************Mapping Beneficiary With Scheme *****************************************************************************************************
             */
            if (request.getParameter("map") != null) {
                sql = "SELECT beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(gender,'') AS gender, COALESCE(mobile,'') AS mobile, "
                        + "COALESCE(bank_account,'') AS bank_account, COALESCE(ifsc,'') AS ifsc FROM dbt_beneficiary "
                        + "WHERE beneficiary_id  NOT IN (SELECT beneficiary_id FROM dbt_scheme_beneficiary_selection WHERE scheme_id = " + schemeId + " ) AND dist_code='" + distId + "'";

                if (blockId > 0) {
                    sql += "AND block_code = '" + blockId + "' ";
                }

                if (gpId > 0) {
                    sql += "AND ward_code = '" + gpId + "' ";
                }

                sql += " AND subdiv_code ='" + subdivId + "' ORDER BY beneficiary_id ASC";
            }
            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Initiation of DBT By beneficiary - Maker
             *
             * Selection*****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtin") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtin")));

                if (type == 1) {
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }

                    if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                        noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                    }

                    if (!finYr.equals("") && !schemeCode.equals("")) {
                        try {
                            String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                            CallableStatement cs = null;
                            if (con == null || con.isClosed()) {
                                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                            }
                            cs = con.prepareCall("{call transaction.transaction_table_create(?)}");
                            cs.setString(1, tabName);
                            cs.execute();

                        } catch (SQLException e) {

                        }
                    }

                    sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " /*INNER JOIN rc_member b ON a.rc_mem = b.member_id */
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "WHERE a.status = 9 AND a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (officeId > 0) {
                        sql += "AND a.office_id = " + officeId + " ";
                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }

//                    sql += "AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  "
//                            + "AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> ''  "
//                            + "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name  "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "WHERE a.tempid NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";
//
////                    if (subdivId > 0) {
////                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
////                    }
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
                    sql += "AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  "
                            + "AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> ''  "
                            + "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                } else if (type == 2) {
                    int itemCat = 0, item = 0;
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }
                    if (request.getParameter("itemCat") != null && !request.getParameter("itemCat").equals("")) {
                        itemCat = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("itemCat")));
                    }
                    if (request.getParameter("item") != null && !request.getParameter("item").equals("") && !request.getParameter("item").equals("-1")) {
                        item = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("item")));
                    }

                    if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                        noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                    }

//                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile, b.scheme_id, scheme_codification, "
//                            + "scheme_name, rationcard_memberid "
//                            + "FROM dbt_beneficiary a INNER JOIN dbt_scheme_beneficiary_selection b ON a.beneficiary_id = b.beneficiary_id "
//                            + "INNER JOIN master_scheme c ON b.scheme_id = c.id WHERE scheme_id=" + schemeId + " "
//                            + "AND block_code = '" + blockId + "' "
//                            + "AND ward_code ='" + gpId + "' "
//                            + "AND b.beneficiary_id NOT IN (SELECT beneficiary_id FROM tran_dbt_scheme_beneficiary_kind "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + "  AND status > 0 ) ";
//
//                    if (!noLand) {
//                        sql += "AND b.beneficiary_id IN (SELECT beneficiary_id FROM dbt_beneficiary_land_info)";
//                    }
//
//                    sql += " ORDER BY a.beneficiary_id LIMIT 500";
                    sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + " " + schemeId + " AS scheme_id, a.scheme_code, COALESCE(a.scheme_name,'') AS scheme_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " /*INNER JOIN rc_member b ON a.rc_mem = b.member_id */
                            + "WHERE a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }

                    sql += "UNION "
                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
                            + "" + schemeId + " AS scheme_id, a.scheme_code, a.scheme_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
                            + "WHERE a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Initiation of DBT to beneficiary - Approver
             *
             * Selection*****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtappdirect") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtappdirect")));
                int workflow = 2;
                String is_calculated = "";
                if (type == 1) {
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }
                    if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                        checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                    }

                    if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                        noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                    }
                    if (request.getParameter("ddlbeneficiaryvalidation") != null && !request.getParameter("ddlbeneficiaryvalidation").equals("")) {
                        ddlbeneficiaryvalidation = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddlbeneficiaryvalidation")));
                    }
                    if (request.getParameter("is_calculated") != null && !request.getParameter("is_calculated").equals("")) {
                        is_calculated = Security.SecurityClass.killchar(request.getParameter("is_calculated"));
                    }
                    if (!finYr.equals("") && !schemeCode.equals("")) {
                        try {
                            String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                            CallableStatement cs = null;
                            if (con == null || con.isClosed()) {
                                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                            }
                            cs = con.prepareCall("{call transaction.transaction_table_create(?)}");
                            cs.setString(1, tabName);
                            cs.execute();

                        } catch (SQLException e) {

                        }
                    }
                    String childOfficeList = "";

                    if ("1".equals(is_calculated)) {
                        //sql += "AND a.eligible_dbt_amount::integer > 0 ";

                        sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, COALESCE(a.ben_mobile,'') AS mobile, d.amount AS amount, " //COALESCE(b.mobile_no,'')
                                + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                                + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  "
                                + "COALESCE(a.gp_ward_name,'') AS gp_ward_name, COALESCE(eligible_dbt_amount,'0') AS eligible_dbt_amount,"
                                + "COALESCE(a.bank_account_validation_status,'') AS bank_account_validation_status,a.bank_account_validation_requested,a.bank_account_validation_status_code,  "
                                + "COALESCE(c.beneficiary_name_match_status,'') AS beneficiary_name_match_status,  "
                                + "COALESCE(c.beneficiary_name_as_per_bank,'') AS beneficiary_name_as_per_bank  "
                                + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a  "
                                + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " d ON a.rc_mem = d.beneficiary_id "
                                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                + "LEFT JOIN beneficiary.bank_account_validation c ON c.office_id = a.office_id  AND c.scheme_id=a.scheme_id "
                                + "AND c.scheme_code=a.scheme_code AND c.rc_mem=a.rc_mem "
                                + "WHERE a.status = 9 AND source_code <> 3 AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> '' "
                                //+ "AND a.rc_mem IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                                + "AND d.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND d.amount::integer > 0   ";

                        if (ddlbeneficiaryvalidation == 2) {
                            sql += "AND a.bank_account_validation_status = 'Success' ";
                        }
                        if (ddlbeneficiaryvalidation == 3) {
                            sql += "AND a.bank_account_validation_status_code = 1 ";
                        }

                        if (checkerOfficeId > 0) {
                            childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                            if (childOfficeList.length() > 1) {
                                sql += "AND a.office_id IN (" + childOfficeList + ") ";
                            }
                        }

//                    else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        if (childOfficeList.length() > 1) {
//                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                        }
//                    }
                        sql += " UNION ";
                        sql += "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, COALESCE(a.ben_mobile,'') AS mobile, d.amount AS amount, " //COALESCE(b.mobile_no,'')
                                + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                                + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  "
                                + "COALESCE(a.gp_ward_name,'') AS gp_ward_name, COALESCE(eligible_dbt_amount,'0') AS eligible_dbt_amount,  "
                                + "COALESCE(a.bank_account_validation_status,'') AS bank_account_validation_status,a.bank_account_validation_requested,a.bank_account_validation_status_code,  "
                                + "COALESCE(c.beneficiary_name_match_status,'') AS beneficiary_name_match_status,  "
                                + "COALESCE(c.beneficiary_name_as_per_bank,'') AS beneficiary_name_as_per_bank  "
                                + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a  "
                                + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " d ON a.rc_mem = d.beneficiary_id "
                                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                + "LEFT JOIN beneficiary.bank_account_validation c ON c.office_id = a.office_id AND c.scheme_id=a.scheme_id "
                                + "AND c.scheme_code=a.scheme_code AND c.rc_mem=a.rc_mem "
                                + "WHERE a.status = 9 AND source_code = 3 AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> '' "
                                //+ "AND a.rc_mem IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                                + "AND d.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND d.amount::integer > 0   ";

                        if (ddlbeneficiaryvalidation == 2) {
                            sql += "AND a.bank_account_validation_status = 'Success' ";
                        }
                        if (ddlbeneficiaryvalidation == 3) {
                            sql += "AND a.bank_account_validation_status_code = 1 ";
                        }
                        if (checkerOfficeId > 0) {
                            sql += "AND a.office_id = " + checkerOfficeId + " ";
                        }

//                    else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                    }
                        sql += "ORDER BY rc_mem "; //LIMIT 500
                    }
                    if ("2".equals(is_calculated)) {
                        //sql += "AND (a.eligible_dbt_amount::integer = 0 OR a.eligible_dbt_amount IS NULL) ";
                        sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, COALESCE(a.ben_mobile,'') AS mobile, 0 AS amount, " //COALESCE(b.mobile_no,'')
                                + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                                + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  "
                                + "COALESCE(a.gp_ward_name,'') AS gp_ward_name, COALESCE(eligible_dbt_amount,'0') AS eligible_dbt_amount,"
                                + "COALESCE(a.bank_account_validation_status,'') AS bank_account_validation_status,a.bank_account_validation_requested,a.bank_account_validation_status_code,  "
                                + "COALESCE(c.beneficiary_name_match_status,'') AS beneficiary_name_match_status,  "
                                + "COALESCE(c.beneficiary_name_as_per_bank,'') AS beneficiary_name_as_per_bank  "
                                + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                + "LEFT JOIN beneficiary.bank_account_validation c ON c.office_id = a.office_id  AND c.scheme_id=a.scheme_id "
                                + "AND c.scheme_code=a.scheme_code AND c.rc_mem=a.rc_mem "
                                + "WHERE a.status = 9 AND source_code <> 3 AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> '' "
                                + "AND a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                                + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND amount::integer > 0 )  ";

                        if (ddlbeneficiaryvalidation == 2) {
                            sql += "AND a.bank_account_validation_status = 'Success' ";
                        }
                        if (ddlbeneficiaryvalidation == 3) {
                            sql += "AND a.bank_account_validation_status_code = 1 ";
                        }

                        if (checkerOfficeId > 0) {
                            childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                            if (childOfficeList.length() > 1) {
                                sql += "AND a.office_id IN (" + childOfficeList + ") ";
                            }
                        }

//                    else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        if (childOfficeList.length() > 1) {
//                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                        }
//                    }
                        sql += " UNION ";
                        sql += "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, COALESCE(a.ben_mobile,'') AS mobile, 0 AS amount, " //COALESCE(b.mobile_no,'')
                                + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                                + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  "
                                + "COALESCE(a.gp_ward_name,'') AS gp_ward_name, COALESCE(eligible_dbt_amount,'0') AS eligible_dbt_amount,  "
                                + "COALESCE(a.bank_account_validation_status,'') AS bank_account_validation_status,a.bank_account_validation_requested,a.bank_account_validation_status_code,  "
                                + "COALESCE(c.beneficiary_name_match_status,'') AS beneficiary_name_match_status,  "
                                + "COALESCE(c.beneficiary_name_as_per_bank,'') AS beneficiary_name_as_per_bank  "
                                + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                + "LEFT JOIN beneficiary.bank_account_validation c ON c.office_id = a.office_id AND c.scheme_id=a.scheme_id "
                                + "AND c.scheme_code=a.scheme_code AND c.rc_mem=a.rc_mem "
                                + "WHERE a.status = 9 AND source_code = 3 AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> '' "
                                + "AND a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                                + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND amount::integer > 0 )  ";

                        if (ddlbeneficiaryvalidation == 2) {
                            sql += "AND a.bank_account_validation_status = 'Success' ";
                        }
                        if (ddlbeneficiaryvalidation == 3) {
                            sql += "AND a.bank_account_validation_status_code = 1 ";
                        }
                        if (checkerOfficeId > 0) {
                            sql += "AND a.office_id = " + checkerOfficeId + " ";
                        }

//                    else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                    }
                        sql += "ORDER BY rc_mem "; //LIMIT 500
                    }
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                } else if (type == 2) {
                    int itemCat = 0, item = 0;
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }
                    if (request.getParameter("itemCat") != null && !request.getParameter("itemCat").equals("")) {
                        itemCat = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("itemCat")));
                    }
                    if (request.getParameter("item") != null && !request.getParameter("item").equals("") && !request.getParameter("item").equals("-1")) {
                        item = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("item")));
                    }

                    if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                        noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                    }

//                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile, b.scheme_id, scheme_codification, "
//                            + "scheme_name, rationcard_memberid "
//                            + "FROM dbt_beneficiary a INNER JOIN dbt_scheme_beneficiary_selection b ON a.beneficiary_id = b.beneficiary_id "
//                            + "INNER JOIN master_scheme c ON b.scheme_id = c.id WHERE scheme_id=" + schemeId + " "
//                            + "AND block_code = '" + blockId + "' "
//                            + "AND ward_code ='" + gpId + "' "
//                            + "AND b.beneficiary_id NOT IN (SELECT beneficiary_id FROM tran_dbt_scheme_beneficiary_kind "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + "  AND status > 0 ) ";
//
//                    if (!noLand) {
//                        sql += "AND b.beneficiary_id IN (SELECT beneficiary_id FROM dbt_beneficiary_land_info)";
//                    }
//
//                    sql += " ORDER BY a.beneficiary_id LIMIT 500";
                    sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + " " + schemeId + " AS scheme_id, a.scheme_code, COALESCE(a.scheme_name,'') AS scheme_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " /*INNER JOIN rc_member b ON a.rc_mem = b.member_id */
                            + "WHERE a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }

                    sql += "UNION "
                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
                            + "" + schemeId + " AS scheme_id, a.scheme_code, a.scheme_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
                            + "WHERE a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Esign DBT Payment to beneficiary - Approver
             *
             * Selection*****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtappdirectesign") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtappdirectesign")));
                int workflow = 2;
                if (type == 1) {
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }
                    if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                        checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                    }

                    if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                        noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                    }

                    if (!finYr.equals("") && !schemeCode.equals("")) {
                        try {
                            String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                            CallableStatement cs = null;
                            if (con == null || con.isClosed()) {
                                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                            }
                            cs = con.prepareCall("{call transaction.transaction_table_create(?)}");
                            cs.setString(1, tabName);
                            cs.execute();

                        } catch (SQLException e) {

                        }
                    }
                    String childOfficeList = "";

                    sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, 0 AS amount, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  "
                            + "COALESCE(a.gp_ward_name,'') AS gp_ward_name, COALESCE(eligible_dbt_amount,'0') AS eligible_dbt_amount  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "WHERE a.status = 9 AND source_code <> 3 AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> '' "
                            + "AND a.rc_mem IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND status = 14 )  ";

                    if (checkerOfficeId > 0) {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                        if (childOfficeList.length() > 1) {
                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
                        }
                    } else {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                        if (childOfficeList.length() > 1) {
                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
                        }
                    }

                    sql += " UNION ";
                    sql += "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, 0 AS amount, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  "
                            + "COALESCE(a.gp_ward_name,'') AS gp_ward_name, COALESCE(eligible_dbt_amount,'0') AS eligible_dbt_amount  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "WHERE a.status = 9 AND source_code = 3 AND a.bank_account_number IS NOT NULL AND a.bank_account_number <> ''  AND a.ifsc_code IS NOT NULL AND a.ifsc_code <> '' "
                            + "AND a.rc_mem IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND status = 14 )  ";

                    if (checkerOfficeId > 0) {
                        sql += "AND a.office_id = " + checkerOfficeId + " ";
                    } else {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                } else if (type == 2) {
                    int itemCat = 0, item = 0;
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }
                    if (request.getParameter("itemCat") != null && !request.getParameter("itemCat").equals("")) {
                        itemCat = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("itemCat")));
                    }
                    if (request.getParameter("item") != null && !request.getParameter("item").equals("") && !request.getParameter("item").equals("-1")) {
                        item = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("item")));
                    }

                    if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                        noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                    }

//                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile, b.scheme_id, scheme_codification, "
//                            + "scheme_name, rationcard_memberid "
//                            + "FROM dbt_beneficiary a INNER JOIN dbt_scheme_beneficiary_selection b ON a.beneficiary_id = b.beneficiary_id "
//                            + "INNER JOIN master_scheme c ON b.scheme_id = c.id WHERE scheme_id=" + schemeId + " "
//                            + "AND block_code = '" + blockId + "' "
//                            + "AND ward_code ='" + gpId + "' "
//                            + "AND b.beneficiary_id NOT IN (SELECT beneficiary_id FROM tran_dbt_scheme_beneficiary_kind "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + "  AND status > 0 ) ";
//
//                    if (!noLand) {
//                        sql += "AND b.beneficiary_id IN (SELECT beneficiary_id FROM dbt_beneficiary_land_info)";
//                    }
//
//                    sql += " ORDER BY a.beneficiary_id LIMIT 500";
                    sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + " " + schemeId + " AS scheme_id, a.scheme_code, COALESCE(a.scheme_name,'') AS scheme_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " /*INNER JOIN rc_member b ON a.rc_mem = b.member_id */
                            + "WHERE a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }

                    sql += "UNION "
                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
                            + "" + schemeId + " AS scheme_id, a.scheme_code, a.scheme_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
                            + "WHERE a.rc_mem NOT IN (SELECT beneficiary_id FROM transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + "  "
                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND item_id = " + item + " AND no_land_allowed = " + noLand + " AND status > 0 )  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Maker MIS Report / Freezing for payment
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptm") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptm")));
                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name , '' AS bank_name, amount, b.office_name , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
                    //AND no_land_allowed = " + noLand + "  
                    if (officeId > 0) {
                        sql += "AND a.office_id = " + officeId + " ";
                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (status > 0) {
                        sql += "AND c.status = " + status + " ";
                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
//                    //AND no_land_allowed = " + noLand + " 
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND c.status = " + status + " ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Maker Beneficiary List Freezing
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptmben") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmben")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            //                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE  a.status = " + status + " AND source_code IN (1,2) "; //AND  financial_year = '" + finYr + "' AND  installment = " + instl + "
                    //AND no_land_allowed = " + noLand + "  

                    if (officeId > 0) {
                        sql += "AND a.office_id = " + officeId + " ";
                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
//                    //AND no_land_allowed = " + noLand + " 
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND c.status = " + status + " ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************ Maker Beneficiary List from Citizen Freezing
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptmcit") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmcit")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name,"
                            + "COALESCE(a.online_application_id,'') AS appl_id  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            //+ "INNER JOIN map_maker_to_lgd_gp c ON c.lgd_gp_code = a.lgd_gp_code AND c.lgd_block_code = a.lgd_block_code "
                            + "WHERE  a.status = " + status + " AND source_code = 3 "; //AND  financial_year = '" + finYr + "' AND  installment = " + instl + "
                    //AND no_land_allowed = " + noLand + "  

                    if (officeId > 0) {
                        sql += "AND a.office_id = " + officeId + " ";
                    }

                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Map Recommendation File with Beneficiary
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("maprecomben") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("maprecomben")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            //+ "INNER JOIN tran_scheme_beneficiary_status_mapping c ON a.rc_mem = c.beneficiary_id  "
                            //                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE  a.recomendation_list_id IS NULL AND a.status >= " + status + " "; //AND  financial_year = '" + finYr + "' AND  installment = " + instl + " //c.scheme_code = '" + schemeCode + "' AND 
                    //AND no_land_allowed = " + noLand + "  

                    if (officeId > 0) {
                        sql += "AND a.office_id = " + officeId + " ";
                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
//                    //AND no_land_allowed = " + noLand + " 
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND c.status = " + status + " ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Checker Beneficiary List Freezing
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptmverben") != null) {
                int workflow = 1;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmverben")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE (d.recomendation_file_id ,0) AS recomendation_file_id , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            //                            + "INNER JOIN tran_scheme_beneficiary_status_mapping c ON a.rc_mem = c.beneficiary_id  "
                            + "LEFT JOIN tbl_recommendation_file_list d ON a.recomendation_list_id = d.list_id  "
                            //                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE  a.status = " + status + " AND source_code IN (1,2) "; //AND  financial_year = '" + finYr + "' AND  installment = " + instl + "
                    //AND no_land_allowed = " + noLand + "  
                    String childOfficeList = "";
                    if (checkerOfficeId > 0) {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);

                        if (childOfficeList.length() > 1) {
                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
                        }
                    } else {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                        if (childOfficeList.length() > 1) {
                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
                        }
                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
//                    //AND no_land_allowed = " + noLand + " 
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND c.status = " + status + " ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Checker Beneficiary List Freezing Citizen Request
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptmverbencit") != null) {
                int workflow = 1;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmverbencit")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE (d.recomendation_file_id ,0) AS recomendation_file_id , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name, "
                            + "COALESCE(a.online_application_id,'') AS appl_id  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            //+ "LEFT JOIN map_maker_to_lgd_gp c ON c.lgd_gp_code = a.lgd_gp_code AND c.lgd_block_code = a.lgd_block_code "
                            + "LEFT JOIN tbl_recommendation_file_list d ON a.recomendation_list_id = d.list_id "
                            + "WHERE  a.status = " + status + " AND source_code = 3 ";
                    //AND no_land_allowed = " + noLand + "  
                    String childOfficeList = "";
                    if (checkerOfficeId > 0) {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                        sql += "AND a.office_id = " + checkerOfficeId + " ";
                    } else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                        sql += "AND a.office_id = " + officeId + " ";
                    }

                    sql += "ORDER BY entrydate ASC "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            if (request.getParameter("dbtrptmverbenFreezzecit") != null) {
                int workflow = 1;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmverbenFreezzecit")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE (d.recomendation_file_id ,0) AS recomendation_file_id , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name, "
                            + "COALESCE(a.online_application_id,'') AS appl_id  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            //+ "LEFT JOIN map_maker_to_lgd_gp c ON c.lgd_gp_code = a.lgd_gp_code AND c.lgd_block_code = a.lgd_block_code "
                            + "LEFT JOIN tbl_recommendation_file_list d ON a.recomendation_list_id = d.list_id "
                            + "WHERE  a.status = " + status + " AND source_code = 3 ";
                    //AND no_land_allowed = " + noLand + "  
                    String childOfficeList = "";
                    if (checkerOfficeId > 0) {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                        sql += "AND a.office_id = " + checkerOfficeId + " ";
                    } else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                        sql += "AND a.office_id = " + officeId + " ";
                    }

                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            if (request.getParameter("dbtrptmverbenDefectivecit") != null) {
                int workflow = 1;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmverbenDefectivecit")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (request.getParameter("roleId") != null && !request.getParameter("roleId").equals("")) {
                    roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                }
                if (type == 1) {
//                    sql = "select application_id,applicant_name,action_taken,trim(remarks) as remarks, b.office_name,"
//                            + "(select mobile from citizen.registration where id=applicant_id::integer) as mobile,"
//                            + "date(entry_datetime) as entrydate \n"
//                            + "from citizen.tbl_raised_queries "
//                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON tbl_raised_queries.checker_office_id = b.office_id "
//                            + " where responded_by_applicant=false AND scheme_id = " + schemeId + "  ";

                    sql = "SELECT a.online_application_id,b.application_id,b.applicant_name,b.action_taken,trim(b.remarks) as remarks, c.office_name,"
                            + "(select mobile from citizen.registration where id=b.applicant_id::integer) as mobile,"
                            + "date(b.entry_datetime) as entrydate from beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a "
                            + "INNER JOIN citizen.tbl_raised_queries b ON b.application_id=a.online_application_id "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office c ON c.office_id = a.office_id "
                            + "WHERE b.responded_by_applicant=false ";

                    //AND no_land_allowed = " + noLand + "  
                    if (roleId == 4) {
                        sql += "AND (a.status=13 OR a.status=14 OR a.status=15 OR a.status=16 ) ";
                    } else {
                        sql += "AND (a.status=13 OR a.status=14 OR a.status=15 OR a.status=16 ) ";
                    }
                    String childOfficeList = "";
                    if (checkerOfficeId != 0 || officeId != 0) {
                        if (officeId > 0 && checkerOfficeId == 0) {
                            sql += "AND b.checker_office_id = " + officeId + " ";
                        } else if (checkerOfficeId > 0) {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                            sql += "AND b.checker_office_id = " + checkerOfficeId + " ";
                        }
//                        else {
////                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
////                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
//                            sql += "AND tbl_raised_queries.entry_office_id = " + officeId + " ";
//                        }

                    }
                    sql += "order by b.entry_datetime "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            if (request.getParameter("dbtrptmverbenEsigncit") != null) {
                int workflow = 1;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmverbenEsigncit")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name, COALESCE (d.recomendation_file_id ,0) AS recomendation_file_id , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name, "
                            + "COALESCE(a.online_application_id,'') AS appl_id  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a  "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            //+ "LEFT JOIN map_maker_to_lgd_gp c ON c.lgd_gp_code = a.lgd_gp_code AND c.lgd_block_code = a.lgd_block_code "
                            + "LEFT JOIN tbl_recommendation_file_list d ON a.recomendation_list_id = d.list_id "
                            + "WHERE  a.status = " + status + "  "; //AND source_code <> 3 //removed for the time being
                    //AND no_land_allowed = " + noLand + "  
                    String childOfficeList = "";

                    if (checkerOfficeId > 0 || officeId > 0) {
                        if (checkerOfficeId > 0) {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                            sql += "AND a.office_id = " + checkerOfficeId + " ";
                        } else {
//                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
//                        sql += "AND c.office_id IN (" + childOfficeList + ") ";
                            sql += "AND a.office_id = " + officeId + " ";
                        }
                    }

                    sql += " order by entrydate "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }
            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Maker RC Verification
             *
             * Selection*****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrcver") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrcver")));
                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
                            + "WHERE a.status = 9 AND c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
                    //AND no_land_allowed = " + noLand + " 
                    if (officeId > 0) {
                        sql += "AND a.office_id = " + officeId + " ";
                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (status > 0) {
                        sql += "AND c.status = " + status + " ";
                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ******************************Verification and forwarding of selected DBT Beneficiaries for Approval - Checker
             *
             * **************************************************************************************************************************************************************
             */
            if (request.getParameter("dbtver") != null) {
                int workflow = 2;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtver")));

                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }

                if (request.getParameter("makerOfficeId") != null && !request.getParameter("makerOfficeId").equals("") && !request.getParameter("makerOfficeId").equals("-1")) {
                    makerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("makerOfficeId")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {

//                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile, "
//                            + "COALESCE(bank_account,'') AS bank_account, COALESCE(ifsc,'') AS ifsc, b.scheme_id, "
//                            + "scheme_codification, scheme_name, COALESCE(rationcard_memberid, '') AS rationcard_memberid, COALESCE(bank_name,'') AS bank_name, amount   "
//                            + "FROM dbt_beneficiary a INNER JOIN dbt_scheme_beneficiary_selection b ON a.beneficiary_id = b.beneficiary_id \n"
//                            + "INNER JOIN master_scheme c ON b.scheme_id = c.id "
//                            + "INNER JOIN tran_dbt_scheme_beneficiary_payment d ON b.beneficiary_id = d.beneficiary_id AND b.scheme_id = d.scheme_id "
//                            + "WHERE dist_code='" + distId + "' ";
//
//                    if (subdivId > 0) {
//                        sql += "AND subdiv_code = '" + subdivId + "' ";
//                    }
//
//                    if (blockId > 0) {
//                        sql += "AND block_code = '" + blockId + "' ";
//                    }
//
//                    if (gpId > 0) {
//                        sql += "AND ward_code = '" + gpId + "' ";
//                    }
//
//                    sql += "AND d.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND d.status = 1 LIMIT 500";
//
//
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE a.status = 9 AND c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND c.status = 11   ";
//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    String childOfficeList = "";
                    if (makerOfficeId > 0) {
                        sql += "AND a.office_id = " + makerOfficeId + " ";
                    } else {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND c.status = 11  ";
//
////                    if (subdivId > 0) {
////                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
////                    }
//                    if (makerOfficeId > 0) {
//                        sql += "AND a.office_id = " + makerOfficeId + " ";
//                    } else {
//                        childOfficeList = cm.getChildOfficeList(officeId, 1);
//                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                } else if (type == 2) {

//                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile,COALESCE(b.ward_code,'') AS ward_code,\n"
//                            + "COALESCE(rationcard_memberid, '') AS rationcard_memberid ,a.scheme_id, scheme_codification, scheme_name,\n"
//                            + "STRING_AGG( a.item_id||'-'|| d.item_name||'-'|| a.item_unit||'-'|| a.item_quantity||'-'|| a.item_provisioned_rate_per_unit,'~'  ORDER BY d.item_id) AS items\n"
//                            //+ "STRING_AGG( a.item_id||'-'|| a.item_quantity,'~'  ORDER BY d.item_id) AS items1 "
//                            + "FROM tran_dbt_scheme_beneficiary_kind a \n"
//                            + "INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id\n"
//                            + "INNER JOIN master_scheme c ON a.scheme_id = c.id\n"
//                            + "INNER JOIN master_dbt_items d ON d.item_id = a.item_id "
//                            + "WHERE dist_code='" + distId + "' AND subdiv_code ='" + subdivId + "' "
//                            + "AND block_code = '" + blockId + "' "
//                            + "AND ward_code ='" + gpId + "' "
//                            + "AND a.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND a.status = 1  "
//                            + "GROUP BY a.beneficiary_id,beneficiary_name,father_name,mobile,b.ward_code,a.scheme_id,rationcard_memberid,scheme_codification,scheme_name LIMIT 500";
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "" + schemeId + " AS scheme_id, a.scheme_code, COALESCE(a.scheme_name,'') AS scheme_name,  "
                            + "STRING_AGG( c.item_id||'-'|| d.item_name||'-'|| c.item_unit||'-'|| c.item_quantity||'-'|| c.item_provisioned_rate_per_unit,'~'  ORDER BY d.item_id) AS items\n "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "INNER JOIN master_dbt_items d ON d.item_id = c.item_id "
                            + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND c.status = 1   ";
//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }
                    sql += "GROUP BY a.rc_mem,full_name,guardian_name,mobile,c.scheme_id ,scheme_code,scheme_name ";

                    sql += "UNION "
                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "" + schemeId + " AS scheme_id, a.scheme_code, COALESCE(a.scheme_name,'') AS scheme_name,  "
                            + "STRING_AGG( c.item_id||'-'|| d.item_name||'-'|| c.item_unit||'-'|| c.item_quantity||'-'|| c.item_provisioned_rate_per_unit,'~'  ORDER BY d.item_id) AS items\n "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
                            + "INNER JOIN transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
                            + "INNER JOIN master_dbt_items d ON d.item_id = c.item_id "
                            + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND c.status = 1  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (storeId > 0) {
                        sql += "AND a.lgd_gp_code IN (" + gpList + ") ";
                    }

                    sql += "GROUP BY a.tempid,full_name,guardian_name,mobile,c.scheme_id ,scheme_code,scheme_name ";

                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                }

            }
            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************MIS report for - verifier
             *
             * Selection*****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptver") != null) {
                int workflow = 2;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptver")));
                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name  , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE a.status = 9 AND c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
                    //AND no_land_allowed = " + noLand + "  
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
                    String childOfficeList = "";
                    childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                    sql += "AND a.office_id IN (" + childOfficeList + ") ";
                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
                    if (status > 0) {
                        sql += "AND c.status = " + status + " ";
                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
//                    //AND no_land_allowed = " + noLand + " 
////                    if (officeId > 0) {
////                        sql += "AND a.office_id = " + officeId + " ";
////                    }
//                    childOfficeList = cm.getChildOfficeList(officeId, 1);
//                    sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
//                    if (status > 0) {
//                        sql += "AND c.status = " + status + " ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ******************************Approval and forwarding of selected DBT Beneficiaries - Approver
             *
             * **************************************************************************************************************************************************************
             */
            if (request.getParameter("dbtapp") != null) {
                int workflow = 2;
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtapp")));

                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("makerOfficeId") != null && !request.getParameter("makerOfficeId").equals("") && !request.getParameter("makerOfficeId").equals("-1")) {
                    makerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("makerOfficeId")));
                }

                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {

//                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile, "
//                            + "COALESCE(bank_account,'') AS bank_account, COALESCE(ifsc,'') AS ifsc, b.scheme_id, "
//                            + "scheme_codification, scheme_name, COALESCE(rationcard_memberid, '') AS rationcard_memberid, COALESCE(bank_name,'') AS bank_name, amount   "
//                            + "FROM dbt_beneficiary a INNER JOIN dbt_scheme_beneficiary_selection b ON a.beneficiary_id = b.beneficiary_id \n"
//                            + "INNER JOIN master_scheme c ON b.scheme_id = c.id "
//                            + "INNER JOIN tran_dbt_scheme_beneficiary_payment d ON b.beneficiary_id = d.beneficiary_id AND b.scheme_id = d.scheme_id "
//                            + "WHERE dist_code='" + distId + "' ";
//
//                    if (subdivId > 0) {
//                        sql += "AND subdiv_code = '" + subdivId + "' ";
//                    }
//
//                    if (blockId > 0) {
//                        sql += "AND block_code = '" + blockId + "' ";
//                    }
//
//                    if (gpId > 0) {
//                        sql += "AND ward_code = '" + gpId + "' ";
//                    }
//
//                    sql += "AND d.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND d.status = 2  LIMIT 500";
//
//
                    sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name  , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.rc_mem = c.beneficiary_id "
                            + "WHERE a.status = 9 AND c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND c.status = 13  ";

//                    if (subdivId > 0) {
//                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
//                    }
                    String childOfficeList = "";
                    if (makerOfficeId > 0) {
                        sql += "AND a.office_id = " + makerOfficeId + " ";
                    } else if (checkerOfficeId > 0) {
                        childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
                    }

                    if (blockId > 0) {
                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
                    }
                    if (gpId > 0) {
                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
                    }
//                    sql += "UNION "
//                            + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, amount, b.office_name   "
//                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                            + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                            + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                            + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND c.status = 13  ";
//
////                    if (subdivId > 0) {
////                        sql += "AND a.lgd_subdiv_code = '" + subdivId + "'  ";
////                    }
//                    if (makerOfficeId > 0) {
//                        sql += "AND a.office_id = " + makerOfficeId + " ";
//                    } else if (checkerOfficeId > 0) {
//                        childOfficeList = cm.getChildOfficeList(checkerOfficeId, 1);
//                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
//                    }
//                    if (blockId > 0) {
//                        sql += "AND a.lgd_block_code = '" + blockId + "' ";
//                    }
//                    if (gpId > 0) {
//                        sql += "AND a.lgd_gp_code = '" + gpId + "' ";
//                    }
                    sql += "ORDER BY rc_mem LIMIT 500";

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

                } else if (type == 2) {

                    sql = "SELECT a.beneficiary_id, beneficiary_name, COALESCE(father_name,'') AS father_name, COALESCE(mobile,'') AS mobile,COALESCE(b.ward_code,'') AS ward_code,\n"
                            + "COALESCE(rationcard_memberid, '') AS rationcard_memberid ,a.scheme_id, scheme_codification, scheme_name,\n"
                            + "STRING_AGG( a.item_id||'-'|| d.item_name||'-'|| a.item_unit||'-'|| a.item_quantity||'-'|| a.item_provisioned_rate_per_unit,'~'  ORDER BY d.item_id) AS items\n"
                            //+ "STRING_AGG( a.item_id||'-'|| a.item_quantity,'~'  ORDER BY d.item_id) AS items1 "
                            + "FROM tran_dbt_scheme_beneficiary_kind a \n"
                            + "INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id\n"
                            + "INNER JOIN master_scheme c ON a.scheme_id = c.id\n"
                            + "INNER JOIN master_dbt_items d ON d.item_id = a.item_id "
                            + "WHERE dist_code='" + distId + "' AND subdiv_code ='" + subdivId + "' "
                            + "AND block_code = '" + blockId + "' "
                            + "AND ward_code ='" + gpId + "' "
                            + "AND a.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND no_land_allowed = " + noLand + " AND a.status = 2  "
                            + "GROUP BY a.beneficiary_id,beneficiary_name,father_name,mobile,b.ward_code,a.scheme_id,rationcard_memberid,scheme_codification,scheme_name LIMIT 500";

                }

            }
            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ****************************DBT Status Report
             *
             * View**********************************************************************************************************************************************************
             */
            if (request.getParameter("benrpt") != null) {
                String tabName = "", tabName1 = "", rcMem = "", benName = "";

                tabName = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";

                if (request.getParameter("rcMem") != null && !request.getParameter("rcMem").equals("")) {
                    rcMem = Security.SecurityClass.killchar(request.getParameter("rcMem"));
                }
                if (request.getParameter("benName") != null && !request.getParameter("benName").equals("")) {
                    benName = Security.SecurityClass.killchar(request.getParameter("benName"));
                }

                if (request.getParameter("status") != null && !request.getParameter("status").equals("") && !request.getParameter("status").equals("0")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }

                if ((!request.getParameter("status").equals("") && status == 0) || (!request.getParameter("status").equals("") && status == 3)) {
                    //no status selected
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "' ";
                    }
                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }

                    sql += "UNION ";

                    sql += "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName + "_temp a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }
                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }
                } else if (!request.getParameter("status").equals("") && status < 2) {
                    //0 status selected
                    sql = "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName + "_temp a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2 AND a.status = " + status + " ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                } else if (!request.getParameter("status").equals("") && status >= 2 && status != 3) {

                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "'";
                    }

                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }
                }
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }

            
            
            if (request.getParameter("benstatusrpt") != null) {
                String tabName = "", tabName1 = "",tabName2 = "", rcMem = "", benName = "";

                tabName = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                tabName2 = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";

                if (request.getParameter("rcMem") != null && !request.getParameter("rcMem").equals("")) {
                    rcMem = Security.SecurityClass.killchar(request.getParameter("rcMem"));
                }
                if (request.getParameter("benName") != null && !request.getParameter("benName").equals("")) {
                    benName = Security.SecurityClass.killchar(request.getParameter("benName"));
                }

                if (request.getParameter("status") != null && !request.getParameter("status").equals("") && !request.getParameter("status").equals("0")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }

                if ((!request.getParameter("status").equals("") && status == 3)) {
                    //no status selected
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                           // + "LEFT JOIN " + tabName + " d"
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            
                            + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "' ";
                    }
                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }

                    sql += "UNION ";

                    sql += "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName + "_temp a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }
                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }
                } else if (!request.getParameter("status").equals("") && status < 2) {
                    //0 status selected
                    sql = "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName + "_temp a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2 AND a.status = " + status + " ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                } else if (!request.getParameter("status").equals("") && status >= 2 && status != 3) {

                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "'";
                    }

                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }
                }
                else{
                     sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  ";
                            
                          //  + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "' ";
                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }

                    sql += "UNION ";

                    sql += "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName2 + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }
                }
                
                sql += " ORDER BY status_name, rc_mem ";
                
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }
            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ****************************DBT Status Report 2
             *
             * View**********************************************************************************************************************************************************
             */
            if (request.getParameter("benstatusrpt") != null) {
                String tabName = "", tabName1 = "", tabName2 = "", rcMem = "", benName = "";

                tabName = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                tabName2 = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";

                if (request.getParameter("rcMem") != null && !request.getParameter("rcMem").equals("")) {
                    rcMem = Security.SecurityClass.killchar(request.getParameter("rcMem"));
                }
                if (request.getParameter("benName") != null && !request.getParameter("benName").equals("")) {
                    benName = Security.SecurityClass.killchar(request.getParameter("benName"));
                }

                if (request.getParameter("status") != null && !request.getParameter("status").equals("") && !request.getParameter("status").equals("0")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }

                if ((!request.getParameter("status").equals("") && status == 3)) {
                    //no status selected
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            // + "LEFT JOIN " + tabName + " d"
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "' ";
                    }
                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }

                    sql += "UNION ";

                    sql += "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName + "_temp a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }
                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }
                } else if (!request.getParameter("status").equals("") && status < 2) {
                    //0 status selected
                    sql = "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName + "_temp a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2 AND a.status = " + status + " ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                } else if (!request.getParameter("status").equals("") && status >= 2 && status != 3) {

                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status > 0  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "'";
                    }

                    if (status > 0) {
                        sql += "AND a.status = " + status + " ";
                    }
                } else {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.district,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name "
                            + "FROM " + tabName + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  ";

                    //  + "WHERE  a.status > 0  ";
                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }

                    if (!rcMem.equals("")) {
                        sql += "AND a.rc_mem = '" + rcMem + "' ";
                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }

                    sql += "UNION ";

                    sql += "SELECT a.tempid AS rc_mem,2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, c.status_name, COALESCE(a.postrcvalidation_distlgdname,'') AS district,a.bank_account_number,a.ifsc_code, \n"
                            + "COALESCE(a.postrcvalidation_subdivlgdname,'') AS subdivision, COALESCE(a.postrcvalidation_blocklgdname,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.postrcvalidation_gplgdname,'') AS gp_ward_name "
                            + "FROM " + tabName2 + " a   "
                            + "INNER JOIN master_beneficiary_status_code c ON a.status = c.status_code  "
                            + "WHERE  a.status <> 2  ";

                    if (!benName.equals("")) {
                        sql += "AND a.full_name ILIKE '%" + benName + "%' ";
                    }
//                    if (status > 0) {
//                        sql += "AND a.status = " + status + " ";
//                    }
                }

                sql += " ORDER BY status_name, rc_mem ";

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }
            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ****************************DBT Summary Report
             *
             * View**********************************************************************************************************************************************************
             */
            if (request.getParameter("bensumrptchk") != null) {
                String tabName = "", tabName1 = "", fromDate = "", toDate = "";

                tabName = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                tabName1 = "beneficiary.tran_" + schemeId + "_" + schemeCode.toLowerCase() + "_beneficiary_status_mapping";

                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("fromDate") != null && !request.getParameter("fromDate").equals("")) {
                    fromDate = request.getParameter("fromDate");
                }

                if (request.getParameter("toDate") != null && !request.getParameter("toDate").equals("")) {
                    toDate = request.getParameter("toDate");
                }

                if (!fromDate.equals("") && !toDate.equals("")) {
                    sql = "SELECT (SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = a.office_id) AS office, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + " WHERE office_id = a.office_id AND scheme_id = " + schemeId + " AND DATE(entrydate) >= '" + fromDate + "' AND DATE(entrydate) <= '" + toDate + "' ) AS total_beneficiary,\n"
                            + "(SELECT COUNT(*) FROM " + tabName + " m WHERE m.status >= 7 AND m.status <= 9 AND office_id = a.office_id AND scheme_id = " + schemeId + " AND DATE(n.verified_on) >= '" + fromDate + "' "
                            + "AND DATE(n.verified_on) <= '" + toDate + "' ) AS verified_beneficiary, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + " m WHERE m.status = 9 AND office_id = a.office_id AND scheme_id = " + schemeId + " AND DATE(n.approved_on) >= '" + fromDate + "' "
                            + "AND DATE(n.approved_on) <= '" + toDate + "' ) AS approved_beneficiary, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + "  WHERE (m.status = 13 OR m.status = 15) AND scheme_id = " + schemeId + " AND office_id = a.office_id AND scheme_id = " + schemeId + " \n"
                            + "AND DATE(entrydate) >= '" + fromDate + "' AND DATE(entrydate) <= '" + toDate + "' ) AS defective_beneficiary, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + "  WHERE (m.status = 14 OR m.status = 16) AND scheme_id = " + schemeId + " "
                            + "AND office_id = a.office_id AND scheme_id = " + schemeId + " AND DATE(entrydate) >= '" + fromDate + "' AND DATE(entrydate) <= '" + toDate + "'  ) AS rejected_beneficiary  \n"
                            + "FROM " + tabName + " a WHERE a.status >=2 and a.office_id<>0 ";
                } else {
                    sql = "SELECT (SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = a.office_id) AS office, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + " WHERE office_id = a.office_id AND scheme_id = " + schemeId + "  ) AS total_beneficiary,\n"
                            + "(SELECT COUNT(*) FROM " + tabName + " m WHERE m.status >= 7 AND m.status <= 9 AND scheme_id = " + schemeId + " AND office_id = a.office_id ) AS verified_beneficiary, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + " m WHERE m.status = 9 AND scheme_id = " + schemeId + " AND office_id = a.office_id ) AS approved_beneficiary, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + " m WHERE (m.status = 13 OR m.status = 15) AND scheme_id = " + schemeId + " AND office_id = a.office_id ) AS defective_beneficiary, \n"
                            + "(SELECT COUNT(*) FROM " + tabName + " m WHERE (m.status = 14 OR m.status = 16) AND scheme_id = " + schemeId + " AND office_id = a.office_id ) AS rejected_beneficiary  \n"
                            + "FROM " + tabName + " a WHERE a.status >=2 and a.office_id<>0 ";
                }

                if (checkerOfficeId > 0) {
                    sql += "AND a.office_id = " + checkerOfficeId + "";
                }

                sql += "GROUP BY office_id ORDER BY office";

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }

            /**
             * **************************************************************************************************************************************************************
             */
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            try {
                jsarr = cm.getResultJSON(sql, con);
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
