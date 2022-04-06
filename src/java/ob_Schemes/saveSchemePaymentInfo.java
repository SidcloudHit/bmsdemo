/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Common.CommonMethod;
import Security.SecurityClass;
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
import validation.DataTypeValidator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "saveSchemePaymentInfo", urlPatterns = {"/saveSchemePaymentInfo"})
public class saveSchemePaymentInfo extends HttpServlet {

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
        HttpSession session = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Common.CommonMethod cm = new CommonMethod();
            errorlog log = new errorlog();
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            int edit_payment_info = 0, dept_id = 0, schemeId = 0, status = 0, tot_textbox = 0, tot_dropdown = 0;
            String scheme_code = "", amount_fixed = "", scheme_periodicity = "", scheme_yearly_frequency = "", payment_mode = "", scheme_available = "",
                    from_date = "", to_date = "";
            String scheme_id = "", benefit_type = "", scheme_publish = "", scheme_specific_info = "";
            String pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "",
                    ac_holder_name = "", debit_tran_limit = "";

            String scheme_commodity[] = null;
            String subsidy_amount[] = null;
            String effective_from[] = null;
            String cond_param[] = null;
            String store_column[] = null;
            JSONArray cond_param_json = null;
            JSONObject cond_paramObj = new JSONObject();

            JSONArray scheme_commodity_json = null;
            JSONObject scheme_commodityObj = new JSONObject();
            String category = "";
            int beneficiary_financial_status = 0, beneficiary_category = 0;

            JSONArray scheme_feature_json = null;
            JSONObject scheme_featureObj = new JSONObject();

            JSONArray textbox_array_json = null;
            JSONObject textbox_Obj = new JSONObject();
            JSONArray dropdown_array_json = null;
            JSONObject dropdown_Obj = new JSONObject();
            CallableStatement cs = null;
            CallableStatement cs1 = null;
            CallableStatement cs2 = null;
            CallableStatement cs3 = null;
            CallableStatement cs4 = null;
            CallableStatement cs5 = null;

            if (request.getParameter("scheme_id") != null && !"".equals(request.getParameter("scheme_id"))) {
                scheme_id = request.getParameter("scheme_id").trim();
                dept_id = cm.getDepartmentId(scheme_id);
                scheme_code = cm.getSchemeCodeFromDraft(scheme_id);
            }
            if (request.getParameter("edit_payment_info") != null && !"".equals(request.getParameter("edit_payment_info"))) {
                edit_payment_info = Integer.parseInt(request.getParameter("edit_payment_info"));
            }

            if (request.getParameter("benefit_type") != null && !"".equals(request.getParameter("benefit_type"))) {
                benefit_type = request.getParameter("benefit_type");
            }
            if (request.getParameter("scheme_periodicity") != null && !"".equals(request.getParameter("scheme_periodicity"))) {
                boolean scheme_periodicity_is_valid = DataTypeValidator.isNumeric(request.getParameter("scheme_periodicity"));
                if (scheme_periodicity_is_valid) {
                    scheme_periodicity = SecurityClass.killchar(request.getParameter("scheme_periodicity"));
                } else {
                    out.print("Scheme Periodicity value not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("scheme_yearly_frequency") != null && !"".equals(request.getParameter("scheme_yearly_frequency"))) {
                boolean scheme_yearly_frequency_is_valid = DataTypeValidator.isNumeric(request.getParameter("scheme_yearly_frequency"));
                if (scheme_yearly_frequency_is_valid) {
                    scheme_yearly_frequency = SecurityClass.killchar(request.getParameter("scheme_yearly_frequency"));
                } else {
                    out.print("Scheme yearly frequency value not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("amount_fixed") != null && !"".equals(request.getParameter("amount_fixed"))) {
                amount_fixed = SecurityClass.killchar(request.getParameter("amount_fixed"));
            }
            if (request.getParameter("payment_mode") != null && !"".equals(request.getParameter("payment_mode"))) {
                boolean payment_mode_is_valid = DataTypeValidator.isNumeric(request.getParameter("payment_mode"));
                if (payment_mode_is_valid) {
                    payment_mode = SecurityClass.killchar(request.getParameter("payment_mode"));
                } else {
                    out.print("Payment mode value changed.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (payment_mode.equals("2")) {

                if (request.getParameter("pfms_scheme_code") != null && !"".equals(request.getParameter("pfms_scheme_code"))) {
                    pfms_scheme_code = request.getParameter("pfms_scheme_code");
                }
                if (request.getParameter("agency_name") != null && !"".equals(request.getParameter("agency_name"))) {
                    agency_name = request.getParameter("agency_name");
                }
                if (request.getParameter("agency_code") != null && !"".equals(request.getParameter("agency_code"))) {
                    agency_code = SecurityClass.killchar(request.getParameter("agency_code"));
                }
                if (request.getParameter("bank_name") != null && !"".equals(request.getParameter("bank_name"))) {
                    bank_name = SecurityClass.killchar(request.getParameter("bank_name"));
                }
                if (request.getParameter("bank_ac_no") != null && !"".equals(request.getParameter("bank_ac_no"))) {
                    bank_ac_no = SecurityClass.killchar(request.getParameter("bank_ac_no"));
                }
                if (request.getParameter("ifsc_code") != null && !"".equals(request.getParameter("ifsc_code"))) {
                    ifsc_code = SecurityClass.killchar(request.getParameter("ifsc_code"));
                }
                if (request.getParameter("ac_holder_name") != null && !"".equals(request.getParameter("ac_holder_name"))) {
                    ac_holder_name = SecurityClass.killchar(request.getParameter("ac_holder_name"));
                }
                if (request.getParameter("debit_transaction_limit") != null && !"".equals(request.getParameter("debit_transaction_limit"))) {
                    debit_tran_limit = SecurityClass.killchar(request.getParameter("debit_transaction_limit"));
                }
            }

            if (request.getParameterValues("scheme_commodity") != null && !"".equals(request.getParameterValues("scheme_commodity"))) {
                scheme_commodity = request.getParameterValues("scheme_commodity");
            }
            if (request.getParameterValues("subsidy_amount") != null && !"".equals(request.getParameterValues("subsidy_amount"))) {
                subsidy_amount = request.getParameterValues("subsidy_amount");
            }

            if (request.getParameterValues("effective_from") != null && !"".equals(request.getParameter("effective_from"))) {
                boolean effective_from_is_valid = DataTypeValidator.isValidDate(request.getParameter("effective_from"), "yyyy-dd-mm");
                if (effective_from_is_valid) {
                    effective_from = request.getParameterValues("effective_from");
                } else {
                    out.print("Effective From date not valid.");
                    out.flush();
                    out.close();
                    return;
                }

            }
            if (request.getParameterValues("cond_param") != null && !"".equals(request.getParameterValues("cond_param"))) {
                cond_param = request.getParameterValues("cond_param");
            }
            if (request.getParameterValues("store_column") != null && !"".equals(request.getParameterValues("store_column"))) {
                store_column = request.getParameterValues("store_column");
            }

            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                String check_scheme_is_present = cm.check_scheme_is_present_using_scheme_id(dept_id, scheme_id);
                if (edit_payment_info == 0) {
                    if ("1".equals(benefit_type)) {
                        if ("Y".equals(amount_fixed)) {
                            String query = "SELECT scheme_ob_basic_info_draft.beneficiary_financial_status,scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category,master_beneficiary_category.category\n"
                                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft ON \n"
                                    + "scheme_ob_bsic_info_beneficiary_category_draft.scheme_id=scheme_ob_basic_info_draft.sl_no\n"
                                    + "LEFT JOIN master_beneficiary_category on master_beneficiary_category.id= scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category "
                                    + " WHERE scheme_ob_basic_info_draft.sl_no = '" + scheme_id + "'";

                            try {
                                ps = con.prepareStatement(query);

                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                                        beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                                    }
                                    if (!"".equals(rs.getString("beneficiary_category"))) {
                                        beneficiary_category = rs.getInt("beneficiary_category");
                                    }
                                    if (!"".equals(rs.getString("category"))) {
                                        category = rs.getString("category");
                                    }
                                    cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info_cash_data(?,?,?,?)}");
                                    cs1.setInt(1, Integer.parseInt(scheme_id));
                                    cs1.setInt(2, beneficiary_category);
                                    switch (beneficiary_financial_status) {
                                        case 1:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                                cs1.setInt(4, 0);
                                            }
                                            break;
                                        case 2:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, 0);
                                                cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            }
//                                        if (request.getParameter(category + "_bpl") != null && !"".equals(request.getParameter(category + "_bpl"))) {
//                                            cs1.setInt(3, 0);
//                                            cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_bpl"))));
//                                        }
                                            break;
                                        default:
                                            cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            break;
                                    }
                                    if (cs1.execute() == true) {
                                        status = 1;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }
                        if ("N".equals(amount_fixed)) {
                            String unit_name = "", rate = "", calc_method_type_name = "";
                            int calc_method_type_id = 0, unit_id = 0;
                            JSONObject calc_method_param_value_json = new JSONObject();
                            JSONArray textbox_array = new JSONArray();
                            JSONArray textbox_mainobj = new JSONArray();
                            if (request.getParameter("payment_method") != null && !"".equals(request.getParameter("payment_method"))) {
                                calc_method_type_id = Integer.parseInt(request.getParameter("payment_method").split("~")[0]);
                                calc_method_type_name = request.getParameter("payment_method").split("~")[1];
                            }
                            if (calc_method_type_id == 1) {
                                if (cond_param != null) {
                                    String cond_param_array = "";
                                    cond_param_array += "[";
                                    for (int k = 0; k < cond_param.length; k++) {
                                        if (k == 0) {
                                            cond_param_array += "{ 'cond_param_id':" + cond_param[k].split("~")[0] + " ,'cond_param_name':" + cond_param[k].split("~")[1] + " , 'store_column_id':" + store_column[k].split("~")[0] + ",'store_column_name':" + store_column[k].split("~")[1] + " }";
                                        } else {
                                            cond_param_array += ",{ 'cond_param_id':" + cond_param[k].split("~")[0] + " ,'cond_param_name':" + cond_param[k].split("~")[1] + " , 'store_column_id':" + store_column[k].split("~")[0] + ",'store_column_name':" + store_column[k].split("~")[1] + " }";
                                        }
                                    }
                                    cond_param_array += "]";

                                    cond_param_json = new JSONArray(cond_param_array);
                                    calc_method_param_value_json.put("calc_method_param_json_array", cond_param_json);
                                }
                            } else {

                                if (request.getParameter("rate") != null && !"".equals(request.getParameter("rate"))) {
                                    rate = request.getParameter("rate");
                                }
                                if (request.getParameter("unit") != null && !"".equals(request.getParameter("unit"))) {
                                    unit_id = Integer.parseInt(request.getParameter("unit").split("~")[0]);
                                    unit_name = request.getParameter("unit").split("~")[1];
                                }

                                JSONObject lablobj = new JSONObject();
                                JSONArray mainjson = new JSONArray();
                                lablobj.put("rate_per_unit", rate);
                                lablobj.put("unit_id", unit_id);
                                lablobj.put("unit_name", unit_name);
                                mainjson.put(lablobj);

                                calc_method_param_value_json.put("calc_method_param_json_array", mainjson.toString());
                            }
//                            try {
//                                con.setAutoCommit(false);
//                                int delete_prev_data_status = cm.deletePrevMethodData(Integer.parseInt(scheme_id), con);
//                                String query = "INSERT INTO scheme_onboarding.scheme_ob_payment_configuration_draft(scheme_id, calc_method_type, calc_method_param_value_json) "
//                                        + "VALUES (?, ?, ?)";
//
//                                ps = con.prepareStatement(query);
//                                ps.setInt(1, Integer.parseInt(scheme_id));
//                                ps.setInt(2, calc_method_type_id);
//                                ps.setString(3, calc_method_param_value_json.toString());
//                                ps.executeUpdate();
//
//                                con.commit();
//                                con.setAutoCommit(true);
//                            } catch (SQLException e) {
//                                con.rollback();
//                                e.printStackTrace();
//                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
//                            }
                            try {

                                cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_configuration(?,?,?::json)}");
                                cs1.setInt(1, Integer.parseInt(scheme_id));
                                cs1.setInt(2, calc_method_type_id);
                                cs1.setString(3, calc_method_param_value_json.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }
                            } catch (SQLException e) {
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                            }

                        }
                    } else if ("2".equals(benefit_type)) {
                        if (scheme_commodity != null) {
                            String scheme_commodity_array = "";
                            scheme_commodity_array += "[";
                            for (int k = 0; k < scheme_commodity.length; k++) {
                                if (k == 0) {
                                    scheme_commodity_array += "{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                } else {
                                    scheme_commodity_array += ",{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                }
                            }
                            scheme_commodity_array += "]";

                            try {
                                scheme_commodity_json = new JSONArray(scheme_commodity_array);
                                scheme_commodityObj.put("scheme_commodity_json_array", scheme_commodity_json);

                                cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info_kind_data(?,?::json)}");
                                cs1.setInt(1, Integer.parseInt(scheme_id));
                                cs1.setString(2, scheme_commodityObj.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }
                            } catch (SQLException e) {
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                            } catch (JSONException ex) {
                                Logger.getLogger(saveSchemeAdditational.class.getName()).log(Level.SEVERE, null, ex);
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }
                    } else {
                        if ("Y".equals(amount_fixed)) {
                            String query = "SELECT scheme_ob_basic_info_draft.beneficiary_financial_status,scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category,master_beneficiary_category.category\n"
                                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft ON \n"
                                    + "scheme_ob_bsic_info_beneficiary_category_draft.scheme_id=scheme_ob_basic_info_draft.sl_no\n"
                                    + "LEFT JOIN master_beneficiary_category on master_beneficiary_category.id= scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category "
                                    + " WHERE scheme_ob_basic_info_draft.sl_no = '" + scheme_id + "'";

                            try {
                                ps = con.prepareStatement(query);

                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                                        beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                                    }
                                    if (!"".equals(rs.getString("beneficiary_category"))) {
                                        beneficiary_category = rs.getInt("beneficiary_category");
                                    }
                                    if (!"".equals(rs.getString("category"))) {
                                        category = rs.getString("category");
                                    }
                                    cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info_cash_data(?,?,?,?)}");
                                    cs1.setInt(1, Integer.parseInt(scheme_id));
                                    cs1.setInt(2, beneficiary_category);
                                    switch (beneficiary_financial_status) {
                                        case 1:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                                cs1.setInt(4, 0);
                                            }
                                            break;
                                        case 2:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, 0);
                                                cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            }
//                                            if (request.getParameter(category + "_bpl") != null && !"".equals(request.getParameter(category + "_bpl"))) {
//                                                cs1.setInt(3, 0);
//                                                cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_bpl"))));
//                                            }
                                            break;
                                        default:
                                            cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            break;
                                    }
                                    if (cs1.execute() == true) {
                                        status = 1;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }

                        if (scheme_commodity != null) {
                            String scheme_commodity_array = "";
                            scheme_commodity_array += "[";
                            for (int k = 0; k < scheme_commodity.length; k++) {
                                if (k == 0) {
                                    scheme_commodity_array += "{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                } else {
                                    scheme_commodity_array += ",{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                }
                            }
                            scheme_commodity_array += "]";

                            try {
                                scheme_commodity_json = new JSONArray(scheme_commodity_array);
                                scheme_commodityObj.put("scheme_commodity_json_array", scheme_commodity_json);

                                cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info_kind_data(?,?::json)}");
                                cs1.setInt(1, Integer.parseInt(scheme_id));
                                cs1.setString(2, scheme_commodityObj.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }
                            } catch (SQLException e) {
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                            } catch (JSONException ex) {
                                Logger.getLogger(saveSchemeAdditational.class.getName()).log(Level.SEVERE, null, ex);
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }
                    }
                    String query_set = "", child_id = "";
                    int cnt = 0;
                    int count = 1;
                    //  String id = "", uploaded_by = "", name = "", guardians_name = "", dob = "", gender = "", caste = "", pid = "", pan = "", rc_mem = "", bank_name = "", ifsc = "", district_name = "", subdivision_name = "", block_name = "", gp_name = "", last_mile_address = "";
                    String bank_account_no = "";
                    String amount = "", postrcvalidation_fullname = "", postrcvalidation_duplicate_in_rc = "", postrcvalidation_missing_in_rc = "", postrcvalidation_rc_member_id = "", match_percentage = "";
                    int benef_name_matchresult_dist_leven = 0;
                    String excel_dist_name = "", excel_subdiv_name = "", excel_block_name = "", uploader_name = "", main_query_set = "", hv = "";
                    String mem_id_str_list = "";
                    JSONArray jsarr = null;
                    JSONObject jsobj = new JSONObject();

                    try {
                        cs = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info(?,?,?,?,?)}");
                        cs.setInt(1, Integer.parseInt(scheme_id));
                        cs.setString(2, scheme_periodicity);
                        cs.setString(3, scheme_yearly_frequency);
                        cs.setString(4, amount_fixed);
                        cs.setString(5, payment_mode);
//                cs.setString(6, scheme_available);
//                cs.setString(7, from_date);
//                cs.setString(8, to_date.equals("")?null:to_date);
                        if (cs.execute() == true) {
                            status = 1;
                            if (payment_mode.equals("2")) {
                                cs3 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info_pfms_details(?,?,?,?,?,?,?,?,?)}");
                                cs3.setInt(1, Integer.parseInt(scheme_id));
                                cs3.setString(2, pfms_scheme_code);
                                cs3.setString(3, agency_name);
                                cs3.setString(4, agency_code);
                                cs3.setString(5, bank_name);
                                cs3.setString(6, bank_ac_no);
                                cs3.setString(7, ifsc_code);
                                cs3.setString(8, ac_holder_name);
                                cs3.setString(9, debit_tran_limit);
                                cs3.execute();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                    }

                    if (status > 0) {
                        out.print(scheme_id);
                    }
                } else {
                    if ("1".equals(benefit_type)) {
                        if ("Y".equals(amount_fixed)) {
                            String query = "SELECT scheme_ob_basic_info_draft.beneficiary_financial_status,scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category,master_beneficiary_category.category\n"
                                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft ON \n"
                                    + "scheme_ob_bsic_info_beneficiary_category_draft.scheme_id=scheme_ob_basic_info_draft.sl_no\n"
                                    + "LEFT JOIN master_beneficiary_category on master_beneficiary_category.id= scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category "
                                    + " WHERE scheme_ob_basic_info_draft.sl_no = '" + scheme_id + "'";

                            try {
                                ps = con.prepareStatement(query);

                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                                        beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                                    }
                                    if (!"".equals(rs.getString("beneficiary_category"))) {
                                        beneficiary_category = rs.getInt("beneficiary_category");
                                    }
                                    if (!"".equals(rs.getString("category"))) {
                                        category = rs.getString("category");
                                    }
                                    cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_info_cash_data(?,?,?,?)}");
                                    cs1.setInt(1, Integer.parseInt(scheme_id));
                                    cs1.setInt(2, beneficiary_category);
                                    switch (beneficiary_financial_status) {
                                        case 1:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                                cs1.setInt(4, 0);
                                            }
                                            break;
                                        case 2:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, 0);
                                                cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            }
//                                        if (request.getParameter(category + "_bpl") != null && !"".equals(request.getParameter(category + "_bpl"))) {
//                                            cs1.setInt(3, 0);
//                                            cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_bpl"))));
//                                        }
                                            break;
                                        default:
                                            cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));

                                            //cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_bpl"))));
                                            // cs1.setInt(3, 0);
                                            // cs1.setInt(4, 0);
                                            break;
                                    }
                                    if (cs1.execute() == true) {
                                        status = 1;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }
                        if ("N".equals(amount_fixed)) {
                            String unit_name = "", rate = "", calc_method_type_name = "";
                            int calc_method_type_id = 0, unit_id = 0;
                            JSONObject calc_method_param_value_json = new JSONObject();
                            JSONArray textbox_array = new JSONArray();
                            JSONArray textbox_mainobj = new JSONArray();
                            if (request.getParameter("payment_method") != null && !"".equals(request.getParameter("payment_method"))) {
                                calc_method_type_id = Integer.parseInt(request.getParameter("payment_method").split("~")[0]);
                                calc_method_type_name = request.getParameter("payment_method").split("~")[1];
                            }
                            if (calc_method_type_id == 1) {
                                if (cond_param != null) {
                                    String cond_param_array = "";
                                    cond_param_array += "[";
                                    for (int k = 0; k < cond_param.length; k++) {
                                        if (!"".equals(cond_param[k])) {
                                           // int check_data_existence = cm.getConditationDataExistence(Integer.parseInt(scheme_id), Integer.parseInt(cond_param[k].split("~")[0]), Integer.parseInt(store_column[k].split("~")[0]));
                                           // if (check_data_existence == 0) {
                                                if (k == 0) {
                                                    cond_param_array += "{ 'cond_param_id':" + cond_param[k].split("~")[0] + " ,'cond_param_name':" + cond_param[k].split("~")[1] + " , 'store_column_id':" + store_column[k].split("~")[0] + ",'store_column_name':" + store_column[k].split("~")[1] + " }";
                                                } else {
                                                    cond_param_array += ",{ 'cond_param_id':" + cond_param[k].split("~")[0] + " ,'cond_param_name':" + cond_param[k].split("~")[1] + " , 'store_column_id':" + store_column[k].split("~")[0] + ",'store_column_name':" + store_column[k].split("~")[1] + " }";
                                                }
                                           // }
//                                        else {
//                                                out.print("Duplicate Data Not Allowed");
//                                            }
                                        }
                                    }
                                    cond_param_array += "]";

                                    cond_param_json = new JSONArray(cond_param_array);
                                    calc_method_param_value_json.put("calc_method_param_json_array", cond_param_json);

//                                        cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_payment_info_kind_data(?,?::json)}");
//                                        cs1.setInt(1, Integer.parseInt(scheme_id));
//                                        cs1.setString(2, scheme_commodityObj.toString());
//                                        if (cs1.execute() == true) {
//                                            status = 1;
//                                        }
//                                    } catch (SQLException e) {
//                                        int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
//                                    } catch (JSONException ex) {
//                                        Logger.getLogger(saveSchemeAdditational.class.getName()).log(Level.SEVERE, null, ex);
//                                        int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
//                                    }
                                }
                            } else {

                                if (request.getParameter("rate") != null && !"".equals(request.getParameter("rate"))) {
                                    rate = request.getParameter("rate");
                                }
                                if (request.getParameter("unit") != null && !"".equals(request.getParameter("unit"))) {
                                    unit_id = Integer.parseInt(request.getParameter("unit").split("~")[0]);
                                    unit_name = request.getParameter("unit").split("~")[1];
                                }

                                JSONObject lablobj = new JSONObject();
                                JSONArray mainjson = new JSONArray();
                                lablobj.put("rate_per_unit", rate);
                                lablobj.put("unit_id", unit_id);
                                lablobj.put("unit_name", unit_name);
                                mainjson.put(lablobj);

                                calc_method_param_value_json.put("calc_method_param_json_array", mainjson.toString());
                            }
                            try {

                                cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_configuration(?,?,?::json)}");
                                cs1.setInt(1, Integer.parseInt(scheme_id));
                                cs1.setInt(2, calc_method_type_id);
                                cs1.setString(3, calc_method_param_value_json.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }
                            } catch (SQLException e) {
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                            }
                        }
                    } else if ("2".equals(benefit_type)) {
                        if (scheme_commodity != null) {
                            String scheme_commodity_array = "";
                            scheme_commodity_array += "[";
                            for (int k = 0; k < scheme_commodity.length; k++) {
                                if (k == 0) {
                                    scheme_commodity_array += "{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                } else {
                                    scheme_commodity_array += ",{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                }
                            }
                            scheme_commodity_array += "]";

                            try {
                                scheme_commodity_json = new JSONArray(scheme_commodity_array);
                                scheme_commodityObj.put("scheme_commodity_json_array", scheme_commodity_json);

                                cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_info_kind_data(?,?::json)}");
                                cs1.setInt(1, Integer.parseInt(scheme_id));
                                cs1.setString(2, scheme_commodityObj.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }
                            } catch (SQLException e) {
                            } catch (JSONException ex) {
                                Logger.getLogger(saveSchemeAdditational.class.getName()).log(Level.SEVERE, null, ex);
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }
                    } else {
                        if ("Y".equals(amount_fixed)) {
                            String query = "SELECT scheme_ob_basic_info_draft.beneficiary_financial_status,scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category,master_beneficiary_category.category\n"
                                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft ON \n"
                                    + "scheme_ob_bsic_info_beneficiary_category_draft.scheme_id=scheme_ob_basic_info_draft.sl_no\n"
                                    + "LEFT JOIN master_beneficiary_category on master_beneficiary_category.id= scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category "
                                    + " WHERE scheme_ob_basic_info_draft.sl_no = '" + scheme_id + "'";

                            try {
                                ps = con.prepareStatement(query);

                                rs = ps.executeQuery();
                                while (rs.next()) {

                                    if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                                        beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                                    }
                                    if (!"".equals(rs.getString("beneficiary_category"))) {
                                        beneficiary_category = rs.getInt("beneficiary_category");
                                    }
                                    if (!"".equals(rs.getString("category"))) {
                                        category = rs.getString("category");
                                    }
                                    cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_info_cash_data(?,?,?,?)}");
                                    cs1.setInt(1, Integer.parseInt(scheme_id));
                                    cs1.setInt(2, beneficiary_category);
                                    switch (beneficiary_financial_status) {
                                        case 1:
                                            if (request.getParameter(category + "_apl") != null && !"".equals(request.getParameter(category + "_apl"))) {
                                                cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                                cs1.setInt(4, 0);
                                            }
                                            break;
                                        case 2:
                                            if (request.getParameter(category + "_bpl") != null && !"".equals(request.getParameter(category + "_bpl"))) {
                                                cs1.setInt(3, 0);
                                                cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_bpl"))));
                                            }
                                            break;
                                        default:
                                            cs1.setInt(3, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_apl"))));
                                            cs1.setInt(4, Integer.parseInt(SecurityClass.killchar(request.getParameter(category + "_bpl"))));
                                            break;
                                    }
                                    if (cs1.execute() == true) {
                                        status = 1;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }

                        if (scheme_commodity != null) {
                            String scheme_commodity_array = "";
                            scheme_commodity_array += "[";
                            for (int k = 0; k < scheme_commodity.length; k++) {
                                if (k == 0) {
                                    scheme_commodity_array += "{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                } else {
                                    scheme_commodity_array += ",{ 'scheme_commodity':" + scheme_commodity[k] + " , 'subsidy_amount':" + subsidy_amount[k] + " , 'effective_from':" + effective_from[k] + " }";
                                }
                            }
                            scheme_commodity_array += "]";

                            try {
                                scheme_commodity_json = new JSONArray(scheme_commodity_array);
                                scheme_commodityObj.put("scheme_commodity_json_array", scheme_commodity_json);

                                cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_info_kind_data(?,?::json)}");
                                cs1.setInt(1, Integer.parseInt(scheme_id));
                                cs1.setString(2, scheme_commodityObj.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }
                            } catch (SQLException e) {
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                            } catch (JSONException ex) {
                                Logger.getLogger(saveSchemeAdditational.class.getName()).log(Level.SEVERE, null, ex);
                                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                            }
                        }
                    }
                    String query_set = "", child_id = "";
                    int cnt = 0;
                    int count = 1;
                    //  String id = "", uploaded_by = "", name = "", guardians_name = "", dob = "", gender = "", caste = "", pid = "", pan = "", rc_mem = "", bank_name = "", ifsc = "", district_name = "", subdivision_name = "", block_name = "", gp_name = "", last_mile_address = "";
                    String bank_account_no = "";
                    String amount = "", postrcvalidation_fullname = "", postrcvalidation_duplicate_in_rc = "", postrcvalidation_missing_in_rc = "", postrcvalidation_rc_member_id = "", match_percentage = "";
                    int benef_name_matchresult_dist_leven = 0;
                    String excel_dist_name = "", excel_subdiv_name = "", excel_block_name = "", uploader_name = "", main_query_set = "", hv = "";
                    String mem_id_str_list = "";
                    JSONArray jsarr = null;
                    JSONObject jsobj = new JSONObject();

                    try {
                        String check_prev_payment_mode = cm.check_prev_payment_mode(Integer.parseInt(scheme_id));
                        cs = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_info(?,?,?,?,?)}");
                        cs.setInt(1, Integer.parseInt(scheme_id));
                        cs.setString(2, scheme_periodicity);
                        cs.setString(3, scheme_yearly_frequency);
                        cs.setString(4, amount_fixed);
                        cs.setString(5, payment_mode);
//                cs.setString(6, scheme_available);
//                cs.setString(7, from_date);
//                cs.setString(8, to_date.equals("")?null:to_date);
                        if (cs.execute() == true) {
                            status = 1;
                            if (payment_mode.equals("2") && check_prev_payment_mode.equals("2")) {
                                cs3 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_payment_info_pfms_details(?,?,?,?,?,?,?,?,?)}");
                                cs3.setInt(1, Integer.parseInt(scheme_id));
                                cs3.setString(2, pfms_scheme_code);
                                cs3.setString(3, agency_name);
                                cs3.setString(4, agency_code);
                                cs3.setString(5, bank_name);
                                cs3.setString(6, bank_ac_no);
                                cs3.setString(7, ifsc_code);
                                cs3.setString(8, ac_holder_name);
                                cs3.setString(9, debit_tran_limit);
                                cs3.execute();
                            } else if (!payment_mode.equals("2") && check_prev_payment_mode.equals("2")) {
                                cs3 = con.prepareCall("{call scheme_onboarding.delete_prev_scheme_ob_payment_info_pfms_details(?)}");
                                cs3.setInt(1, Integer.parseInt(scheme_id));
                                cs3.execute();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                    }
                    if (status > 0) {
                        out.print(scheme_id);
                    }
                }
            } catch (Exception e) {
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
            out.flush();
            out.close();
        }

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
            Logger.getLogger(saveSchemePaymentInfo.class.getName()).log(Level.SEVERE, null, ex);
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
