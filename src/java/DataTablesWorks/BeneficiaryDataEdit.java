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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.eclipse.persistence.expressions.ExpressionOperator.currentDate;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "BeneficiaryDataEdit", urlPatterns = {"/BeneficiaryDataEdit"})
public class BeneficiaryDataEdit extends HttpServlet {

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
            Common.CommonMethod cm = new CommonMethod();
            Connection con = null;
            int distId = 0, subdivId = 0, officeId = 0, makerOfficeId = 0, checkerOfficeId = 0, blockId = 0, gpId = 0, storeId = 0, schemeId = 0, instl = 0, status = 0;
            String schemeCode = "", finYr = "", gpList = "", batchId = "";
            boolean noLand = false;
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String today = formatter.format(currentDate);

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
             * **************************Mapping Beneficiary With Scheme
             * *****************************************************************************************************
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
             * ************************Maker Beneficiary List Freezing
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtrptmben") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtrptmben")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, "
                            + "COALESCE(a.bank_account_number,'') AS bank_account,COALESCE(a.bank_name,'') AS bank_name,COALESCE(a.bank_abbr,'') AS bank_abbr, "
                            + "COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "WHERE  a.status >= " + status + " "; //AND source_code IN (1,2) 

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
//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
//                   
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Beneficiary Account Validation
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtbensch") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtbensch")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, COALESCE(a.ben_mobile,'') AS mobile, " //COALESCE(b.mobile_no,'')
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, b.office_name , COALESCE(a.district,'') AS district, \n"
                            + "COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  COALESCE(a.gp_ward_name,'') AS gp_ward_name  "
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                            + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                            + "WHERE  a.status = " + status + " AND bank_account_validation_requested = false AND bank_account_validation_status_code = 0";

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

//                    if (officeId > 0) {
//                        sql += "AND a.office_id = " + officeId + " ";
//                    }
                    sql += "ORDER BY rc_mem "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            /**
             * **************************************************************************************************************************************************************
             */
            /**
             * ************************Beneficiary Account Pushed For
             * Validation
             *
             * *****************************************************************************************************************************************************
             */
            if (request.getParameter("dbtbenpsh") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtbenpsh")));

                if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("batchId") != null && !request.getParameter("batchId").equals("")) {
                    batchId = Security.SecurityClass.killchar(request.getParameter("batchId"));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (type == 1) {
                    sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name,  COALESCE(a.ben_mobile,'') AS mobile, \n"
                            + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, \n"
                            + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name ,COALESCE(a.bank_account_validation_status,'') AS validation_status, \n"
                            + "COALESCE(a.district,'') AS district, COALESCE(a.subdivision,'') AS subdivision, COALESCE(a.block_mc_np_adc_nonadc,'') AS block_mc_np_adc_nonadc,  \n"
                            + "COALESCE(a.gp_ward_name,'') AS gp_ward_name , COALESCE(c.account_valid_status,'Pending') as account_valid_status ,\n"
                            + "COALESCE(c.beneficiary_name_match_status,'') as beneficiary_name_match_status,\n"
                            + "COALESCE(c.beneficiary_name_as_per_bank ,'') as beneficiary_name_as_per_bank\n"
                            + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   "
                            //+ "LEFT JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id   \n"
                            + "LEFT JOIN  beneficiary.bank_account_validation c on c.rc_mem = a.rc_mem AND a.office_id = c.office_id \n"
                            + "WHERE  a.status = " + status + " AND a.bank_account_validation_requested = true AND c.batch_id = '" + batchId + "' ";

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

                    sql += " ORDER BY account_valid_status "; //LIMIT 500

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

            }

            if (request.getParameter("dbtbenbatch") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtbenbatch")));

                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (type == 1) {
                    sql = "SELECT batch_id, DATE(req_time) as req_time,(REPLACE(REPLACE((a.request_details::json)->>'summary','[',''),']','')::json)->>'total_beneficiary_count' AS ben_count "
                            + "FROM beneficiary.tbl_account_validation_push_details a WHERE a.office_id IS NOT NULL AND (REPLACE(REPLACE((ack_deatils::json)->>'summary','[',''),']','')::json)->>'status' = 'success' ";

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

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }

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
