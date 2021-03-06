/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;

/**
 *
 * @author acer
 */
public class CommonMethod {

    public String getFinacialYear(int prev_fy, int future_fy) throws JSONException {
        String finYear = "";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int future_cnt = 0, prev_cnt = 1;
        for (int i = 1; i <= prev_fy; i++) {
            prev_cnt = prev_cnt + 1;
            if (i > 1) {
                finYear += "<option value=\"" + (year - prev_cnt) + "-" + (year - i) + "\">" + (year - prev_cnt) + "-" + (year - i) + "</option>";
            } else {
                finYear += "<option value=\"" + (year - prev_cnt) + "-" + (year - i) + "\">" + (year - prev_cnt) + "-" + (year - i) + "</option>";
            }

            //prev_cnt++;
        }
        if (month <= 3) {

            finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
        } else {

            finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
        }
        for (int i = 1; i <= future_fy; i++) {
            if (i > 1) {
                finYear += "<option value=\"" + (year + future_cnt) + "-" + (year + i) + "\">" + (year + future_cnt) + "-" + (year + i) + "</option>";
            } else {
                finYear += "<option value=\"" + (year + future_cnt) + "-" + (year + i) + "\">" + (year + future_cnt) + "-" + (year + i) + "</option>";
            }

            future_cnt++;
        }

        return finYear;
    }

    public String getSelectedFinancialyear(int prev_fy, int future_fy, String launch_fy) throws JSONException {
        String finYear = "";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int cnt = 0, prev_cnt = 1;
        for (int i = 1; i <= prev_fy; i++) {
            prev_cnt = prev_cnt + 1;
            if (i > 1) {
                if (launch_fy.equals((year - prev_cnt) + "-" + (year - i))) {
                    finYear += "<option value=\"" + (year - prev_cnt) + "-" + (year - i) + "\" selected =\"selected\">" + (year - prev_cnt) + "-" + (year - i) + "</option>";
                } else {
                    finYear += "<option value=\"" + (year - prev_cnt) + "-" + (year - i) + "\">" + (year - prev_cnt) + "-" + (year - i) + "</option>";
                }
            } else {
                if (launch_fy.equals((year - prev_cnt) + "-" + (year - i))) {
                    finYear += "<option value=\"" + (year - prev_cnt) + "-" + (year - i) + "\" selected =\"selected\">" + (year - prev_cnt) + "-" + (year - i) + "</option>";
                } else {
                    finYear += "<option value=\"" + (year - prev_cnt) + "-" + (year - i) + "\">" + (year - prev_cnt) + "-" + (year - i) + "</option>";
                }
            }

        }
        if (month <= 3) {

            if (launch_fy.equals((year - 1) + "-" + year)) {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
            } else {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
            }

        } else {

            if (launch_fy.equals((year - 1) + "-" + year)) {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
            } else {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
            }

        }
        for (int i = 1; i <= future_fy; i++) {
            if (i > 1) {
                if (launch_fy.equals((year + cnt) + "-" + (year + i))) {
                    finYear += "<option value=\"" + (year + cnt) + "-" + (year + i) + "\" selected =\"selected\">" + (year + cnt) + "-" + (year + i) + "</option>";
                } else {
                    finYear += "<option value=\"" + (year + cnt) + "-" + (year + i) + "\">" + (year + cnt) + "-" + (year + i) + "</option>";
                }
            } else {
                if (launch_fy.equals((year + cnt) + "-" + (year + i))) {
                    finYear += "<option value=\"" + (year + cnt) + "-" + (year + i) + "\" selected =\"selected\">" + (year + cnt) + "-" + (year + i) + "</option>";
                } else {
                    finYear += "<option value=\"" + (year + cnt) + "-" + (year + i) + "\">" + (year + cnt) + "-" + (year + i) + "</option>";
                }
            }

            cnt++;
        }

        return finYear;
    }

    public String getBasicInfoData(String scheme_id) throws JSONException {
        String child_id = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String department_name = "", scheme_code = "", scheme_name = "", component_name = "", beneficiary_financial_status = "", description = "", beneficiary_category = "",
                scheme_features = "", scheme_periodicity = "", scheme_frequency = "", amount_fixed = "", amount = "", payment_mode = "", required_doc_poi = "", required_doc_poa = "", required_doc_scheme = "",
                commodity_name = "", subsidy_amt = "", effective_from = "", commodity_matrix = "", launch_date = "", launch_fy = "", active_upto = "", scheme_type = "",
                scheme_data_source = "", last_date_for_verification = "", last_date_for_approval = "";
        JSONObject jsonObjDB = new JSONObject();
        JSONObject basic_ob_jsonObj = new JSONObject();
        JSONArray scheme_basic_ob_array = new JSONArray();
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "Select a.scheme_data_source,date(a.probable_launch_date) as launch_date,a.launch_fy,date(a.active_upto) as active_upto,"
                    + "DATE(last_date_for_verification) as last_date_for_verification,DATE(last_date_for_approval) as last_date_for_approval, "
                    + "a.scheme_codification,a.scheme_type,a.department_name,a.scheme_name,a.component_name,a.beneficiary_financial_status,a.description,a.benefit_type,"
                    + "b.type_name from scheme_onboarding.scheme_ob_basic_info_draft as a INNER JOIN master_benefit_type as b ON b.id=a.benefit_type where a.sl_no=" + scheme_id;
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                department_name = rs.getString("department_name");
                scheme_code = rs.getString("scheme_codification");
                scheme_name = rs.getString("scheme_name");
                component_name = rs.getString("component_name");
                if (rs.getString("beneficiary_financial_status") != null) {
                    beneficiary_financial_status = rs.getString("beneficiary_financial_status");
                }
                if (rs.getString("description") != null) {
                    description = rs.getString("description");
                }
                beneficiary_category = getSchemeWisebeneficiaryCategory(scheme_id);
                scheme_features = getSchemeFeatures(scheme_id);
                if (rs.getString("beneficiary_financial_status") != null) {
                    launch_date = rs.getString("launch_date");
                }
                if (rs.getString("launch_fy") != null) {
                    launch_fy = rs.getString("launch_fy");
                }
                active_upto = rs.getString("active_upto") == null ? "" : rs.getString("active_upto");
                last_date_for_verification = rs.getString("last_date_for_verification") == null ? "" : rs.getString("last_date_for_verification");
                last_date_for_approval = rs.getString("last_date_for_approval") == null ? "" : rs.getString("last_date_for_approval");
                if (rs.getString("scheme_type") != null) {
                    scheme_type = rs.getString("scheme_type");
                }
                required_doc_poi = getrequireddocListPoi(scheme_id);
                required_doc_poa = getrequireddocListPoa(scheme_id);
                required_doc_scheme = getrequireddocListScheme(scheme_id);
                if (rs.getString("scheme_data_source") != null) {
                    scheme_data_source = rs.getString("scheme_data_source");
                }
                jsonObjDB.put("department_name", department_name);
                jsonObjDB.put("scheme_code", scheme_code);
                jsonObjDB.put("scheme_name", scheme_name);
                jsonObjDB.put("component_name", component_name);
                jsonObjDB.put("beneficiary_financial_status", beneficiary_financial_status);
                jsonObjDB.put("description", description);
                jsonObjDB.put("beneficiary_category", beneficiary_category);
                jsonObjDB.put("scheme_features", scheme_features);
                jsonObjDB.put("launch_date", launch_date);
                jsonObjDB.put("launch_fy", launch_fy);
                if (active_upto.equals("")) {
                    jsonObjDB.put("active_upto", "");
                } else {
                    jsonObjDB.put("active_upto", active_upto);
                }
                if (last_date_for_verification.equals("")) {
                    jsonObjDB.put("last_date_for_verification", "");
                } else {
                    jsonObjDB.put("last_date_for_verification", last_date_for_verification);
                }
                if (last_date_for_approval.equals("")) {
                    jsonObjDB.put("last_date_for_approval", "");
                } else {
                    jsonObjDB.put("last_date_for_approval", last_date_for_approval);
                }

                jsonObjDB.put("scheme_type", scheme_type);
                jsonObjDB.put("scheme_data_source", scheme_data_source);
                jsonObjDB.put("required_doc_poi", required_doc_poi);
                jsonObjDB.put("required_doc_poa", required_doc_poa);
                jsonObjDB.put("required_doc_scheme", required_doc_scheme);
//                scheme_basic_ob_array.put(jsonObjDB);
//                basic_ob_jsonObj.put("basic_ob_json",scheme_basic_ob_array);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return jsonObjDB.toString();
    }

    public String getUserDetails(String email) {
        String mobile_no = "", user_id = "", uemail = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT emp_mobile,user_id,emp_email FROM bms_user.mas_dept_users WHERE (emp_email = '" + email + "' or emp_mobile = '" + email + "')");
            rs = ps.executeQuery();
            if (rs.next()) {
                mobile_no = rs.getString("emp_mobile");
                user_id = rs.getString("user_id");
                uemail = rs.getString("emp_email");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return mobile_no + "~" + user_id + "~" + uemail;
    }

    public String checkOtp(String email, String emailotp, String mobileotp) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT *  FROM citizen.tbl_otp where email=? AND email_otp=? AND mobile_otp=?");
            ps.setString(1, email);
            ps.setString(2, emailotp);
            ps.setString(3, mobileotp);

            rs = ps.executeQuery();
            if (rs.next()) {
                result = "ok";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getonboarded_dept_id_list() {
        String child_id = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "Select dept_id from department_onboarding.nodal_officer_data where status='1'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (child_id.trim().length() > 0) {
                    child_id = child_id + "," + "'" + rs.getString("dept_id") + "'";
                } else {
                    child_id = "'" + rs.getString("dept_id") + "'";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return child_id;
    }

    public int deletePrevMethodData(int scheme_id, Connection con) {
        int status = 0;
        //Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("DELETE FROM scheme_onboarding.scheme_ob_payment_configuration_draft WHERE scheme_id=" + scheme_id);
            if (ps.execute()) {
                status = 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };

        }
        return status;
    }

    public int DeletePrevdata(int scheme_id, String field_type) {
        int status = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("DELETE FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE reference_id=" + scheme_id + " AND field_type='" + field_type + "'");
            if (ps.execute()) {
                status = 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return status;
    }

    public String getPrefilledTblLabel(int id) {
        String label = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT listname from public.meta_prfilled_list \n"
                    + "  where id=" + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                label = rs.getString("listname");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return label;
    }

    public String[] getPrefilledTblOptionName(String table_name) {
        //String option_name[] = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> option_name = new ArrayList<String>();
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT name from public." + table_name + " \n"
                    + "  where status=1");
            rs = ps.executeQuery();
            while (rs.next()) {
                option_name.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        String[] nameArr = new String[option_name.size()];
        nameArr = option_name.toArray(nameArr);

        return nameArr;
    }

    public String[] getPrefilledTblOptionValue(String table_name) {
        //String option_name[] = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> option_value = new ArrayList<String>();
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT id||'~'||name as opt_value from public." + table_name + " \n"
                    + "  where status=1");
            rs = ps.executeQuery();
            while (rs.next()) {
                option_value.add(rs.getString("opt_value"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        String[] nameArr = new String[option_value.size()];
        nameArr = option_value.toArray(nameArr);

        return nameArr;
    }

    public String getSchemeWisebeneficiaryCategoryName(String scheme_id) {
        String beneficiary_category = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT c.beneficiary_category,d.category FROM scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft as c"
                    + " INNER JOIN master_beneficiary_category as d on d.id=c.beneficiary_category WHERE c.scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (beneficiary_category.trim().length() > 0) {
                    beneficiary_category = beneficiary_category + "," + rs.getString("category") + "";
                } else {
                    beneficiary_category = "" + rs.getString("category") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return beneficiary_category;
    }

    public String getrequireddocListpoi(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_name FROM master_document Inner Join scheme_onboarding.scheme_ob_bsic_info_required_document_draft on scheme_ob_bsic_info_required_document_draft.document_id=master_document.id "
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id='" + scheme_id + "' AND scheme_ob_bsic_info_required_document_draft.document_type=1");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_name") + "";
                } else {
                    req_doc = "" + rs.getString("document_name") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getrequireddocListpoa(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_name FROM master_document Inner Join scheme_onboarding.scheme_ob_bsic_info_required_document_draft on scheme_ob_bsic_info_required_document_draft.document_id=master_document.id "
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id='" + scheme_id + "' AND scheme_ob_bsic_info_required_document_draft.document_type=2");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_name") + "";
                } else {
                    req_doc = "" + rs.getString("document_name") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getrequireddocListscheme(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_name FROM master_document Inner Join scheme_onboarding.scheme_ob_bsic_info_required_document_draft on scheme_ob_bsic_info_required_document_draft.document_id=master_document.id "
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id='" + scheme_id + "' AND scheme_ob_bsic_info_required_document_draft.document_type=4");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_name") + "";
                } else {
                    req_doc = "" + rs.getString("document_name") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getSchemeFeatures(String scheme_id) {
        String scheme_feature = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_feature FROM scheme_onboarding.scheme_ob_bsic_info_feature_draft"
                    + " WHERE scheme_ob_bsic_info_feature_draft.scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (scheme_feature.trim().length() > 0) {
                    scheme_feature = scheme_feature + "," + "" + rs.getString("scheme_feature") + "";
                } else {
                    scheme_feature = "" + rs.getString("scheme_feature") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return scheme_feature;
    }

    public String getSchemeWisebeneficiaryCategory(String scheme_id) {
        String beneficiary_category = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT c.beneficiary_category FROM scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft as c"
                    + " INNER JOIN master_beneficiary_category as d on d.id=c.beneficiary_category WHERE c.scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (beneficiary_category.trim().length() > 0) {
                    beneficiary_category = beneficiary_category + "," + rs.getString("beneficiary_category") + "";
                } else {
                    beneficiary_category = "" + rs.getString("beneficiary_category") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return beneficiary_category;
    }

    public int getDepartmentId(String scheme_id) {
        int dept_id = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT department_id  FROM scheme_onboarding.scheme_ob_basic_info_draft WHERE sl_no = '" + scheme_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (!"".equals(rs.getInt("department_id"))) {
                    dept_id = rs.getInt("department_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return dept_id;
    }

    public String getPfmsPaymentData(String scheme_id) {
        String pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "",
                ac_holder_name = "", debit_tran_limit = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT pfms_scheme_code, agency_name, agency_code, bank_name, bank_ac_no, ifsc_code, ac_holder_name, debit_tran_limit\n"
                    + "	FROM scheme_onboarding.scheme_ob_payment_info_pfms_details_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                pfms_scheme_code = rs.getString("pfms_scheme_code");
                agency_name = rs.getString("agency_name");
                agency_code = rs.getString("agency_code");
                bank_name = rs.getString("bank_name");

                bank_ac_no = rs.getString("bank_ac_no");
                ifsc_code = rs.getString("ifsc_code");
                ac_holder_name = rs.getString("ac_holder_name");
                debit_tran_limit = rs.getString("debit_tran_limit");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return pfms_scheme_code + "~" + agency_name + "~" + agency_code + "~" + bank_name + "~" + bank_ac_no + "~" + ifsc_code + "~" + ac_holder_name + "~" + debit_tran_limit;
    }

    public String check_prev_payment_mode(int scheme_id) {
        String payment_mode = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT payment_mode FROM scheme_onboarding.scheme_ob_payment_info_draft WHERE scheme_id = " + scheme_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (!"".equals(rs.getString("payment_mode"))) {
                    payment_mode = rs.getString("payment_mode");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return payment_mode;
    }
    public int getConditationDataExistence(int scheme_id,int cond_param_id,int store_column_id) {
        int cnt = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT count(*) as cnt FROM scheme_onboarding.scheme_ob_payment_configuration_draft "
                    + "WHERE scheme_id = " + scheme_id+" AND calc_method_param_id="+cond_param_id+" OR calc_method_param_store_column_id="+store_column_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (!"".equals(rs.getString("cnt"))) {
                    cnt = rs.getInt("cnt");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return cnt;
    }

    public String getSchemeCodeFromDraft(String scheme_id) {
        String scheme_code = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_codification as scheme_code FROM scheme_onboarding.scheme_ob_basic_info_draft WHERE sl_no = '" + scheme_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (!"".equals(rs.getString("scheme_code"))) {
                    scheme_code = rs.getString("scheme_code");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return scheme_code;
    }

    public String getSchemeDataSource(int scheme_id) {
        String scheme_data_source = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_data_source FROM master_scheme WHERE id = " + scheme_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (!"".equals(rs.getString("scheme_data_source"))) {
                    scheme_data_source = rs.getString("scheme_data_source");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return scheme_data_source;
    }

    public String getSchemeCode(int scheme_id) {
        String scheme_code = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_codification as scheme_code FROM master_scheme WHERE id = " + scheme_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (!"".equals(rs.getString("scheme_code"))) {
                    scheme_code = rs.getString("scheme_code");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return scheme_code;
    }

    public String check_scheme_is_present_using_scheme_id(int dept_id, String scheme_id) {
        String status = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT sl_no as scheme_id FROM scheme_onboarding.scheme_ob_basic_info_draft WHERE sl_no = '" + scheme_id + "'  AND department_id='" + dept_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("scheme_id") > 0) {
                    status = rs.getString("scheme_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return status;
    }

    public String check_scheme_is_present(int dept_id, String scheme_code, String scheme_name, String component_name) {
        String status = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT sl_no as scheme_id FROM scheme_onboarding.scheme_ob_basic_info_draft "
                    + "WHERE scheme_codification = '" + scheme_code + "' AND scheme_name = '" + scheme_name + "'"
                    + " AND component_name='" + component_name + "' AND department_id='" + dept_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("scheme_id") > 0) {
                    status = rs.getString("scheme_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return status;
    }

    public String getDeptOnboardingStatus(int dept_id) {
        String status = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT status FROM department_onboarding.nodal_officer_data  Where dept_id=" + dept_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                status = rs.getString("status");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return status;
    }

    public JSONObject RequestToJson(HttpServletRequest request) throws Exception {
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

    public int getTotTextboxCount(String scheme_id) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT count(*) as cnt FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id= '" + scheme_id + "' AND field_type='textbox'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getBenApplStatus(String tabname, String schemeCode, String rcMem, String appl, String benName) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT count(*) as cnt FROM " + tabname + " WHERE scheme_code='" + schemeCode + "' ";
            if (!benName.equals("")) {
                query += "AND full_name ILIKE '%" + benName + "%' ";
            }

            if (!rcMem.equals("")) {
                query += "AND rc_mem = '" + rcMem + "'  ";
            }
            if (!appl.equals("")) {
                query += "AND online_application_id = '" + appl + "' ";
            }
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getBenApplStatus_inEnrollment(String tabname, String schemeCode, String rcMem, String appl, String benName, String mobile) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT application_id FROM " + tabname + " WHERE scheme_code='" + schemeCode + "' ";
            if (!benName.equals("")) {
                query += "AND fullname ILIKE '%" + benName + "%' ";
            }

            if (!rcMem.equals("")) {
                query += "AND rc_member_id = '" + rcMem + "'  ";
            }
            if (!appl.equals("")) {
                query += "AND application_id = '" + appl + "' ";
            }
            if (!mobile.equals("")) {
                query += "AND mobile_no = '" + mobile + "' ";
            }
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("application_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getBenApplStatus_inDraft(String tabname, String schemeCode, String rcMem, String appl, String benName, String mobile) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT count(*) as cnt FROM " + tabname + " WHERE scheme_code='" + schemeCode + "' ";
            if (!benName.equals("")) {
                query += "AND fullname ILIKE '%" + benName + "%' ";
            }

            if (!rcMem.equals("")) {
                query += "AND rc_member_id = '" + rcMem + "'  ";
            }
            if (!appl.equals("")) {
                query += "AND application_id = '" + appl + "' ";
            }
            if (!mobile.equals("")) {
                query += "AND mobile_no = '" + mobile + "' ";
            }
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getBenApplStatus_inArchive(String tabname, String schemeCode, String rcMem, String appl, String benName, String mobile) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT count(*) as cnt FROM " + tabname + " WHERE scheme_code='" + schemeCode + "' ";
            if (!benName.equals("")) {
                query += "AND fullname ILIKE '%" + benName + "%' ";
            }

            if (!rcMem.equals("")) {
                query += "AND rc_member_id = '" + rcMem + "'  ";
            }
            if (!appl.equals("")) {
                query += "AND application_id = '" + appl + "' ";
            }
            if (!mobile.equals("")) {
                query += "AND mobile_no = '" + mobile + "' ";
            }
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getTotDateFieldCount(String scheme_id) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT count(*) as cnt FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id= '" + scheme_id + "' AND field_type='datefield'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getTotPriorityCount(String scheme_id) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT MAX(priority) as cnt FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id= '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getMasterPriorityList(String scheme_id) throws java.sql.SQLException {
        String child_id = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT priority FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id= '" + scheme_id + "' AND field_type='prefilled'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (child_id.trim().length() > 0) {
                    child_id = child_id + "," + "" + rs.getString("priority") + "";
                } else {
                    child_id = "," + rs.getString("priority") + "";
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return child_id;
    }

    public int getTotDropdownCount(String scheme_id) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT count(*) as cnt FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id= '" + scheme_id + "' AND field_type='Dropdown'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getSpecific_info_textbox_html(String scheme_id) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            int option_counter_count = 0;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT sl_no,field_id, field_type, field_json FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id= '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            //result += " <div class=\"form-row\">";
            while (rs.next()) {
                if ("textbox".equals(rs.getString("field_type"))) {
                    String textbox_json = rs.getString("field_json");
                    int id = rs.getInt("sl_no");
                    try {
                        JSONObject jsonObj = new JSONObject(textbox_json);
                        JSONArray details = new JSONArray(jsonObj.get("textbox_json_array").toString());

                        for (int j = 0; j < details.length(); j++) {
                            JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());
                            String label = tmpOBJ.optString("label");
                            String maximum_length = tmpOBJ.optString("maximum_length");
                            String required = tmpOBJ.optString("required");
                            String num_valid = tmpOBJ.optString("num_valid");
                            String priority = tmpOBJ.optString("priority");
                            String valid = "", is_number = "", is_decimal = "";
                            if (required.equals("1")) {
                                valid = "checked";
                            }
                            if (num_valid.equals("2")) {
                                is_number = "checked";
                            }
                            if (num_valid.equals("3")) {
                                is_decimal = "checked";
                            }

                            result += " <div class=\"form-group col-md-12 removeclass_textbox" + id + "\"><div class=\"input-group\"><div class=\"form-group col-md-3 required\"><input type=\"hidden\" name=\"textbox_priority" + id + "\" value=\"" + priority + "\"><label for=\"installment\" class=\"control-label\">Text Field Label :</label>";
                            result += "<input type=\"hidden\" name=\"textbox_id\" value=\"" + id + "\"><input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"textbox_lebel" + id + "\" value=\"" + label + "\"><span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                            result += "</div><div class=\"form-group col-md-3 required\"><label for=\"installment\" class=\"control-label\">Maximum Length :</label>";
                            result += "<input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"textbox_length" + id + "\" value=\"" + maximum_length + "\"><span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                            result += "</div><div class=\"form-group col-md-2 required\"><label for=\"periodicity\" class=\"control-label\">Required:</label>";
                            result += "<input type=\"checkbox\" class=\"form-check\" value=\"1\" name=\"textbox_valid" + id + "\"" + valid + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                            result += "</div>";
                            result += "<div class=\"form-group col-md-1 required\"><label for=\"periodicity\" class=\"control-label\">Numeric:</label><input type=\"radio\" class=\"form-check\" value=\"2\" name=\"textbox_numericvalid" + id + "\"" + is_number + "><span id=\"lblinstallmentError\" style=\"color: red\"></span></div>";
                            result += "<div class=\"form-group col-md-1 required\"><label for=\"periodicity\" class=\"control-label\">Decimal:</label><input type=\"radio\" class=\"form-check\" value=\"3\" name=\"textbox_numericvalid" + id + "\"" + is_decimal + "><span id=\"lblinstallmentError\" style=\"color: red\"></span></div>";
                            result += "<div class=\"input-group-btn col-md-2\"><br><button class=\"btn btn-danger mr-1\" type=\"button\" onclick=\"remove_TextboxConfig_fields(" + id + ");\"><span class=\"minus\">-</span> </button></div></div></div>";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if ("datefield".equals(rs.getString("field_type"))) {
                    String datefield_json = rs.getString("field_json");
                    int id = rs.getInt("sl_no");
                    try {
                        JSONObject jsonObj = new JSONObject(datefield_json);
                        JSONArray details = new JSONArray(jsonObj.get("datefield_json_array").toString());

                        for (int j = 0; j < details.length(); j++) {
                            JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());
                            String label = tmpOBJ.optString("label");
                            String required = tmpOBJ.optString("required");
                            String priority = tmpOBJ.optString("priority");
                            String valid = "";
                            if (required.equals("1")) {
                                valid = "checked";
                            }
                            result += " <div class=\"form-group col-md-12 removeclass_datefield" + id + "\"><div class=\"input-group\"><input type=\"hidden\" name=\"datefield_priority" + id + "\" value=\"" + priority + "\"><div class=\"form-group col-md-3 required\"><label for=\"installment\" class=\"control-label\">Date Field Label :</label>";
                            result += "<input type=\"hidden\" name=\"datefield_id\" value=\"" + id + "\"><input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"datefield_lebel" + id + "\" value=\"" + label + "\"><span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                            result += "</div>";
                            result += "<div class=\"form-group col-md-2 required\"><label for=\"periodicity\" class=\"control-label\">Required:</label>";
                            result += "<input type=\"checkbox\" class=\"form-check\" value=\"1\" name=\"datefield_valid" + id + "\"" + valid + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                            result += "</div><div class=\"input-group-btn col-md-2\"><br><button class=\"btn btn-danger mr-1\" type=\"button\" onclick=\"remove_datefieldConfig_fields(" + id + ");\"><span class=\"minus\">-</span> </button></div></div></div>";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if ("Dropdown".equals(rs.getString("field_type"))) {
                    String dropdown_json = rs.getString("field_json");
                    int id = rs.getInt("sl_no");

                    try {
                        JSONObject jsonObj = new JSONObject(dropdown_json);
                        JSONArray details = new JSONArray(jsonObj.get("dropdown_json_array").toString());
                        for (int j = 0; j < details.length(); j++) {
                            option_counter_count++;
                            JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());
                            String label = tmpOBJ.optString("label");
                            String required = tmpOBJ.optString("required");
                            String priority = tmpOBJ.optString("priority");
                            JSONArray option_counter = new JSONArray(tmpOBJ.optString("option_counter"));
                            String valid = "";
                            if (required.equals("1")) {
                                valid = "checked";
                            }
//                            divtest.innerHTML = '<div class="form-group col-md-2 required"><label for="periodicity" class="control-label">Required:</label><input type="checkbox" class="form-check" value="1" name="dropdown_valid'
//                             + dropdown + '"><span id="lblinstallmentError" style="color: red"></span></div> <div class="input-group-btn col-md-2"><br/><button class="btn btn-success mr-1" type="button"  onclick="adddropdownConfigoption('
//                             + dropdown + ');"> <span class="plus">+</span> </button><button class="btn btn-danger" type="button" onclick="remove_dropdown_fields('
//                             + dropdown + ');"><span class="minus">-</span> </button></button></div> </div></div></div><div class="clear"></div>'
//                            ;

                            result += " <div class=\"form-group col-md-12 removeclass_dropdown" + id + "\"><div class=\"input-group\"><input type=\"hidden\" name=\"dropdown_priority" + id + "\" value=\"" + priority + "\"><div class=\"form-group col-md-4 required\"> <label for=\"installment\" class=\"control-label\">Field Label :</label>";
                            result += "<input type=\"hidden\" name=\"dropdown_id\" value=\"" + id + "\"><input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"dropdown_field_lebel" + id + "\"  value=" + label + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span></div>";
                            result += "<div class=\"col-md-8\" id=\"scheme_specific_dropdown_field_option" + id + "\">";

                            for (int k = 0; k < option_counter.length(); k++) {

                                JSONObject option_counterobj = new JSONObject(option_counter.getJSONObject(k).toString());
                                if (k == 0) {
                                    result += "<div class=\"input-group\"><div class=\"form-group col-md-4 required\"><label for=\"installment\" class=\"control-label\">Option Name :</label>";
                                    result += "<input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"option_name" + id + "\" value=" + option_counterobj.optString("option_name" + k) + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span></div>";
                                    result += "<div class=\"form-group col-md-4 required\"> <label for=\"periodicity\" class=\"control-label\">Option Value:</label><input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"option_value" + id + "\" value=" + option_counterobj.optString("option_value" + k) + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span> </div>";
                                    result += "<div class=\"form-group col-md-2 required\"><label for=\"periodicity\" class=\"control-label\">Required:</label>";
                                    result += "<input type=\"checkbox\" class=\"form-check\" value=\"1\" name=\"textbox_valid_" + id + "\" " + valid + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                                    result += "</div><div class=\"input-group-btn col-md-2\"><br><button class=\"btn btn-danger mr-1\" type=\"button\" onclick=\"remove_dropdown_fields(" + id + ");\"><span class=\"minus\">-</span> </button></div></div>";
                                } else {
                                    result += "<div class=\"form-group removeclass_option" + k + "\"><div class=\"input-group\"><div class=\"form-group col-md-4 required\"><label for=\"installment\" class=\"control-label\">Option Name :</label>";
                                    result += "<input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"option_name" + id + "\" value=" + option_counterobj.optString("option_name" + k) + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span></div>";
                                    result += "<div class=\"form-group col-md-4 required\"> <label for=\"periodicity\" class=\"control-label\">Option Value:</label><input autocomplete=\"off\" type=\"text\" class=\"form-control\" name=\"option_value" + id + "\" value=" + option_counterobj.optString("option_value" + k) + " ><span id=\"lblinstallmentError\" style=\"color: red\"></span> </div>";
                                    result += "<div class=\"input-group-btn col-md-2\"><br><button class=\"btn btn-danger mr-1\" type=\"button\" onclick=\"remove_dropdown_fields_option(" + k + ");\"><span class=\"minus\">-</span> </button></div></div>";
                                }

                            }
                            result += "</div></div></div></div>";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getdocument_list_html_preview(int scheme_id, int doc_type) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", document_name = "", doc_id = "", type = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT master_document.document_name,master_document.id As doc_id from master_document INNER JOIN scheme_onboarding.scheme_ob_bsic_info_required_document_draft ON \n"
                    + "scheme_ob_bsic_info_required_document_draft.document_id=master_document.id\n"
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id = '" + scheme_id + "' and scheme_ob_bsic_info_required_document_draft.document_type = '" + doc_type + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            switch (doc_type) {
                case 1:
                    result += "<label for=\"scheme_code\" class=\"control-label  font-weight-bold\">Proof of Identity :</label>";
                    break;
                case 2:
                    result += "<label for=\"scheme_code\" class=\"control-label  font-weight-bold\">Proof of Address :</label>";
                    break;
                default:
                    result += "<label for=\"scheme_code\" class=\"control-label  font-weight-bold\">Scheme Specific Document :</label>";
                    break;
            }
            while (rs.next()) {
                cnt = 1;
                int sl_no = count;
                String category = "";
                int beneficiary_financial_status = 0, beneficiary_category = 0;
                if (!"".equals(rs.getString("document_name"))) {
                    document_name = rs.getString("document_name");
                }
//                if (!"".equals(rs.getString("type"))) {
//                    type = rs.getString("type");
//                }
                if (!"".equals(rs.getString("doc_id"))) {
                    doc_id = rs.getString("doc_id");
                }

                result += "<div class=\"row\" id=" + sl_no + ">";

                result += "<div class=\"col-md-4 mt-2\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Only pdf file Allowed\">" + document_name + "</div>";

                //result += "<div class=\"col-md-4 mt-2\"> <input type=\"file\" class=\"form-input\" id=" + doc_id + "_file_" + doc_type + " required=\"required\"  name=" + doc_id + "_file_" + doc_type + " value=\"\" accept=\"image/jpeg,image/gif,image/png,application/pdf,image/x-eps\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Only pdf & image file Allowed\"/></div>";
                result += "<div class=\"col-md-4 mt-2\"> </div>";

                result += "</div>";
                count++;
            }
            if (cnt < 1) {
                result += "<div class=\"row\"><div class=\"col-md-12\">No Data Found</div></div>";
            }
//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getdocument_list_html(int scheme_id) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", document_name = "", doc_id = "", type = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT master_document.document_name,master_document.type,master_document.id As doc_id from master_document INNER JOIN required_document ON \n"
                    + "required_document.document_id=master_document.id\n"
                    + " WHERE required_document.scheme_id = '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            result += "<label for=\"scheme_code\" class=\"control-label\">Scheme Specific Document List :</label>";
            while (rs.next()) {
                cnt = 1;
                int sl_no = count;
                String category = "";
                int beneficiary_financial_status = 0, beneficiary_category = 0;
                if (!"".equals(rs.getString("document_name"))) {
                    document_name = rs.getString("document_name");
                }
                if (!"".equals(rs.getString("type"))) {
                    type = rs.getString("type");
                }
                if (!"".equals(rs.getString("doc_id"))) {
                    doc_id = rs.getString("doc_id");
                }

                result += "<div class=\"row\" id=" + sl_no + ">";

                result += "<div class=\"col-md-4 mt-2\">" + document_name + "</div>";

                result += "<div class=\"col-md-4 mt-2\"> <input type=\"file\" required=\"required\" class=\"form-input\" id=" + doc_id + "_file name=" + doc_id + "_file value=\"\" /></div>";

                result += "</div>";
                count++;
            }
            if (cnt < 1) {
                result += "<div class=\"row\"><div class=\"col-md-12\">No Data Found</div></div>";
            }
//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getcat_financial_status_html(int scheme_id, String amount_cond) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "";
        try {
            int cnt = 0;
            int count = 1;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT scheme_ob_basic_info_draft.beneficiary_financial_status,scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category,master_beneficiary_category.category\n"
                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft ON \n"
                    + "scheme_ob_bsic_info_beneficiary_category_draft.scheme_id=scheme_ob_basic_info_draft.sl_no\n"
                    + "LEFT JOIN master_beneficiary_category on master_beneficiary_category.id= scheme_ob_bsic_info_beneficiary_category_draft.beneficiary_category  WHERE scheme_ob_basic_info_draft.sl_no= '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                cnt = 1;
                int sl_no = count;
                String category = "";
                int beneficiary_financial_status = 0, beneficiary_category = 0;
                if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                    beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                }
                if (!"".equals(rs.getString("beneficiary_category"))) {
                    beneficiary_category = rs.getInt("beneficiary_category");
                }
                if (!"".equals(rs.getString("category"))) {
                    category = rs.getString("category");
                }

                result += "<div class=\"row\" id=" + sl_no + ">";

                if (amount_cond.equals("Y")) {
                    result += "<div class=\"col-md-4 mt-2\">" + category + "</div>";
                    switch (beneficiary_financial_status) {

                        case 1:
                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Amount\" value=\"\" /></div>";
                            break;
                        case 2:
                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Amount\" value=\"\" /></div>";
                            break;
                        default:
                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Amount\" value=\"\" /></div>";
                            //result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL\" value=\"\" /></div>";
                            break;
                    }
                }
                //  else {

//                    switch (beneficiary_financial_status) {
//                        case 1:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Ceiling Amount\" value=\"\" /></div>";
//                            break;
//                        case 2:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"Ceiling Amount\" value=\"\" /></div>";
//                            break;
//                        default:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Ceiling Amount\" value=\"\" /></div>";
//                            //result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" required=\"required\"   autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL Ceiling Amount\" value=\"\" /></div>";
//                            break;
                // }
//                    switch (beneficiary_financial_status) {
//                        case 1:
//                            result += "<div class=\"col-md-4 mt-2\"> <a href=\"payment_configuration.jsp?data="+scheme_id+"\" target=\"_blank\"/>Payment Configuration</a></div>";
//                            break;
//                        case 2:
//                            result += "<div class=\"col-md-4 mt-2\"> <a href=\"payment_configuration.jsp?data="+scheme_id+"\" target=\"_blank\"/>Payment Configuration</a></div>";
//                            break;
//                        default:
//                            result += "<div class=\"col-md-4 mt-2\"> <a href=\"payment_configuration.jsp?data="+scheme_id+"\" target=\"_blank\"/>Payment Configuration</a></div>";
//                            break;
//                    }
//                   
//                }
                result += "</div>";
                count++;
            }
            if (amount_cond.equals("Y")) {
                if (cnt < 1) {
                    result += "<div class=\"row\"><div class=\"col-md-12\">No Data Found</div></div>";
                }
            }

//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String fillDropdown(String query) throws java.sql.SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            //    options_string += "<option value=\"\">" + defaultValue + "</option>";

            while (res.next()) {

                options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\">" + res.getString(2) + "</option>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public String fillDropdown(String query, String defaultValue) throws java.sql.SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            options_string += "<option value=\"\">" + defaultValue + "</option>";

            while (res.next()) {

                options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\">" + res.getString(2) + "</option>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public String fillSelectedCategoryDropdown(String query, String data) throws java.sql.SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String[] dataarray = data.split(",");
        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                boolean is_match = false;
                for (int i = 0; i < dataarray.length; i++) {

                    if (res.getString(1) == null ? data == null : res.getString(1).equals(dataarray[i])) { //.split("~")[0]
                        is_match = true;
                    }
                }
                if (is_match == true) {
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\" selected =\"selected\">" + res.getString(2) + "</option>";
                } else {
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\">" + res.getString(2) + "</option>";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public String fillSelectedPrefilledDropdown(String query, String data) throws java.sql.SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String[] dataarray = data.split(",");
        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                boolean is_match = false;
                for (String dataarray1 : dataarray) {
                    if (res.getString(1).split("~")[0] == null ? data == null : res.getString(1).split("~")[0].equals(dataarray1)) {
                        //.split("~")[0]
                        is_match = true;
                    }
                }
                if (is_match == true) {
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\" selected =\"selected\">" + res.getString(2) + "</option>";
                } else {
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\">" + res.getString(2) + "</option>";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public String fillSelectedDropdown(String query, String data, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            options_string += "<option value=\"\">" + defaultValue + "</option>";

            while (res.next()) {
                if (res.getString(1) == null ? data == null : res.getString(1).equals(data)) { //.split("~")[0]
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\" selected =\"selected\">" + res.getString(2) + "</option>";
                } else {
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\">" + res.getString(2) + "</option>";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public String fillSelectedCaicMethodDropdown(String query, String data, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            options_string += "<option value=\"\">" + defaultValue + "</option>";

            while (res.next()) {
                if (res.getString(1) == null ? data == null : res.getString(1).split("~")[0].equals(data)) { //.split("~")[0]
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\" selected =\"selected\">" + res.getString(2) + "</option>";
                } else {
                    options_string += "<option value=\"" + res.getString(1) + "\" title =\"" + res.getString(2) + "\">" + res.getString(2) + "</option>";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public JSONArray getResultJSON(String sqlstr) throws SQLException, Exception {
        Connection con = null;
        ResultSet rs = null;
        Converter convtr = new Converter();
        JSONArray jsonarr = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            rs = con.prepareStatement(sqlstr).executeQuery();
            jsonarr = convtr.convertToJSON(rs);
            rs.close();
            con.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con.isClosed() == false) {
                con.close();
            }
        }
        return jsonarr;
    }

    public JSONArray getResultJSON(String sqlstr, Connection con) throws SQLException, Exception {

        ResultSet rs = null;
        Converter convtr = new Converter();
        JSONArray jsonarr = null;
        try {
            rs = con.prepareStatement(sqlstr).executeQuery();
            jsonarr = convtr.convertToJSON(rs);
            rs.close();
            con.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con.isClosed() == false) {
                con.close();
            }
        }
        return jsonarr;
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean mobileValidator(String mobile) {
        Pattern pattern;
        Matcher matcher;
        String MOBILE_PATTERN = "[4-9]{1}[0-9]{9}";
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public boolean loginIdValidator(String loginId) {
        Pattern pattern;
        Matcher matcher;
        String LOGIN_ID_PATTERN = "^[^-\\s][A-Za-z0-9]{1}[A-Za-z0-9@#$]{6,19}$";
        pattern = Pattern.compile(LOGIN_ID_PATTERN);
        matcher = pattern.matcher(loginId);
        return matcher.matches();
    }

    public boolean PINValidator(String pin) {
        Pattern pattern;
        Matcher matcher;
        String MOBILE_PATTERN = "[1-9]{1}[0-9]{5}";
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(pin);
        return matcher.matches();
    }

    public boolean isNumeric(String str) {
        return str.matches("^(?:\\d+(?:\\.{1}\\d+)?)$");
    }

    public boolean checkEmailExists(String uId, String email) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE user_id = '" + uId + "' AND emp_email = '" + email + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean checkEmailExists(String email) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE emp_email = '" + email + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean checkMobileExists(String uId, String mobile) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE user_id = '" + uId + "' AND emp_mobile = '" + mobile + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean checkMobileExists(String mobile) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE emp_mobile = '" + mobile + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean checkLoginIdExists(String uId, String loginId) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE user_id = '" + uId + "' AND login_id = '" + loginId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean checkLoginIdExists(String loginId) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM bms_user.mas_dept_users WHERE login_id = '" + loginId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean checkDateGtOrEqToCurrDate(String dt1, int gt, int eq, int fr) {
        boolean ans = false;
        java.text.SimpleDateFormat dtformat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date cdt = new Date();
        String cdtstr = dtformat.format(cdt);
        if (fr == 1) {
            dt1 = changeDateFormat(dt1, "-", "/");
        }

        try {
            java.util.Date dt2 = dtformat.parse(dt1);
            cdt = dtformat.parse(cdtstr);
            if (gt == 1) {
                if (cdt.after(dt2) == true) {
                    ans = true;
                }
            }
            if (eq == 1) {
                if (cdt.equals(dt2) == true) {
                    ans = true;
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ans;
    }

    /* change date string format from d-m-y to y-m-d format or vice versa*/
    public String changeDateFormat(String date1, String exp, String exp2) {
        String date2[] = new String[3];
        String date3 = "";

        date2 = date1.split(exp);
        date3 = date2[2] + exp2 + date2[1] + exp2 + date2[0];

        return date3;
    }

    public String getbeneficiaryCategory(String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, category	FROM public.master_beneficiary_category where status='1'";
        return fillDropdown(query, defaultValue);
    }

    public String getbeneficiarySelectedCategory(String data) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, category FROM public.master_beneficiary_category where status='1'";
        return fillSelectedCategoryDropdown(query, data);
    }

    public String getPrefilledList() throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id||'~'||ref_table_name, listname FROM public.meta_prfilled_list where status='1'";
        return fillDropdown(query);
    }

    public String getDocumentpoi() throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where (document_type=1 OR document_type=3) AND status='1'";
        return fillDropdown(query);
    }

    public String getSelectedPrefilledList(String data) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id||'~'||ref_table_name, listname FROM public.meta_prfilled_list where status='1'";
        return fillSelectedPrefilledDropdown(query, data);
    }

    public String getSelectedDocumentpoi(String data) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where (document_type=1 OR document_type=3) AND status='1'";
        return fillSelectedCategoryDropdown(query, data);
    }

    public String getDocumentpoa() throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where (document_type=2 OR document_type=3) AND status='1'";
        return fillDropdown(query);
    }

    public String getDocumentscheme() throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where document_type=4 AND status='1'";
        return fillDropdown(query);
    }

    public String getSelectedDocumentscheme(String data) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where document_type=4 AND status='1'";
        return fillSelectedCategoryDropdown(query, data);
    }

    public String getSelectedDocumentpoa(String data) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where (document_type=2 OR document_type=3) AND status='1'";
        return fillSelectedCategoryDropdown(query, data);
    }

    public String getbeneficiaryCategory() throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, category	FROM public.master_beneficiary_category where status='1'";
        return fillDropdown(query);
    }

    public String getDocument() throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, document_name FROM public.master_document where status='1'";
        return fillDropdown(query);
    }

    public String getPeriodicity(String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT value, text FROM public.scheme_periodicity ORDER BY value ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getPeriodicity(String data, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT value, text FROM public.scheme_periodicity ORDER BY value ASC";
        return fillSelectedDropdown(query, data, defaultValue);
    }

    public String getCommodityList(String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT commodity_code, commodity_name FROM public.master_commodity_list ORDER BY commodity_id ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getSelectedCommodityList(String data, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT commodity_code, commodity_name FROM public.master_commodity_list ORDER BY commodity_id ASC";
        return fillSelectedDropdown(query, data, defaultValue);
    }

    public String getSchemeType(String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, type_name FROM public.master_scheme_type WHERE status='1' AND id !=2 ORDER BY id ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getSchemeType(String scheme_id, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, type_name FROM public.master_scheme_type WHERE status='1' AND id !=2  ORDER BY id ASC";
        return fillSelectedDropdown(query, scheme_id, defaultValue);
    }

    public String getSelectedDepartment(String dept_id, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT department_id, department_name FROM public.mas_department where (department_type='1' OR department_type='2') AND is_onboarded=1 ORDER BY department_name ASC";
        return fillSelectedDropdown(query, dept_id, defaultValue);
    }

    public String getBeneficiaryType(String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id, category  FROM master_beneficiary_category WHERE status = '1' ORDER BY id ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getOnBoardingDepartment(String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT department_id, department_name FROM public.mas_department where department_type='1' ORDER BY department_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getOnBoardingDepartment(String dept_id, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT department_id, department_name FROM public.mas_department where department_type='1' AND department_id='" + dept_id + "' ORDER BY department_name ASC";
        return fillSelectedDropdown(query, dept_id, defaultValue);

    }

    public String getDistrict(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT lgd_district_code, district_name FROM lgd.tbl_district ORDER BY district_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getCalculationType(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT type_id||'~'||type_desc, type_desc FROM master_payment_calculation_type";
        return fillDropdown(query, defaultValue);
    }

    public String getUnitDropdownHtml(int method, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT param_id||'~'||param_name, param_name FROM master_payment_calculation_params where param_type=" + method;
        return fillDropdown(query, defaultValue);
    }

    public String getUnitDropdownHtml(int method, String data, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT param_id||'~'||param_name, param_name FROM master_payment_calculation_params where param_type=" + method;
        return fillSelectedCaicMethodDropdown(query, data, defaultValue);
    }

    public String getCalculationType(String method, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT type_id||'~'||type_desc, type_desc FROM master_payment_calculation_type";
        return fillSelectedCaicMethodDropdown(query, method, defaultValue);
    }

    public String getBeneficiaryStatus(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT status_code, status_name FROM master_beneficiary_status_code ORDER BY status_code ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getDistrict(int distId, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT lgd_district_code, district_name FROM lgd.tbl_district WHERE lgd_district_code = '" + distId + "'";
        return fillDropdown(query, defaultValue);
    }

    public String getOffice(int officeId, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT office_id, office_name FROM bms_user.mas_dept_users_group_based_office WHERE  office_id = '" + officeId + "'";
        return fillDropdown(query, defaultValue);
    }

    public String getHeaderUrls(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT url_id, url_name FROM mas_application_url WHERE  is_header = 1";
        return fillDropdown(query, defaultValue);
    }

    public String getChildOffice(int schemeId, int workflow, int officeId, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT a.office_id, b.office_name FROM bms_user.map_admin_workflow a "
                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                + "WHERE  parent_office_id = " + officeId + " AND scheme_id = " + schemeId + "  AND  (mapping_type = " + workflow + " OR mapping_type = 3) order by b.office_name";
        return fillDropdown(query, defaultValue);
    }

    public String getChildOffice(int schemeId, int workflow, int officeId, String ben_office_id, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT a.office_id, b.office_name FROM bms_user.map_admin_workflow a "
                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                + "WHERE  parent_office_id = " + officeId + " AND scheme_id = " + schemeId + "  AND  (mapping_type = " + workflow + " OR mapping_type = 3) order by b.office_name";
        return fillSelectedDropdown(query, ben_office_id, defaultValue);
    }

    public String getDeprtment(int deptId, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT department_id, department_name FROM mas_department ";
        if (deptId > 0) {
            query += "WHERE  department_id = " + deptId;
        }
        return fillDropdown(query, defaultValue);
    }

    public String getLocation(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id, location FROM lgd.tbl_location ORDER BY location ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getSchemes(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public String getSchemes(String defaultValue, int schemeId) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE id = " + schemeId + " AND status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public String getScheme(String defaultValue, int schemeId) throws java.sql.SQLException {
        String query = "SELECT id, scheme_name FROM master_scheme WHERE id = " + schemeId + " AND status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public String getDeptUserSchemes(int dept_id, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification||'~'||scheme_name, scheme_name FROM master_scheme "
                + "INNER JOIN scheme_onboarding.scheme_ob_sh_dept_bsp ON scheme_ob_sh_dept_bsp.scheme_id=master_scheme.onboarding_id AND (scheme_ob_sh_dept_bsp.maker_dept_id=" + dept_id + " OR "
                + "scheme_ob_sh_dept_bsp.checker_dept_id=" + dept_id + " OR \n"
                + " scheme_ob_sh_dept_bsp.approver_dept_id=" + dept_id + ") "
                + "WHERE master_scheme.status = '1' AND master_scheme.onboarding_id is Not Null GROUP BY  master_scheme.id";
        return fillDropdown(query, defaultValue);
    }

    public String getRegSchemes(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public String getCashSchemes(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE benefit_type IN (1,5) AND status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public String getKindSchemes(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE benefit_type IN (2,5) AND status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public String getCashSchemes(String defaultValue, int deptId) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE benefit_type IN (1,5) AND status = '1' AND department_id= " + deptId + " ORDER BY id";
        return fillDropdown(query, defaultValue);
    }

    public String getKindSchemes(String defaultValue, int deptId) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE benefit_type IN (2,5) AND status = '1' AND department_id= " + deptId;
        return fillDropdown(query, defaultValue);
    }

    public String getDbtItemCategories(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT item_category_id, item_category  FROM master_dbt_item_category WHERE active=1 ORDER BY item_category;";
        return fillDropdown(query, defaultValue);
    }

    public JSONArray getSchemePublishedData(int schemeId) throws Exception {
        String sql = "SELECT date(a.active_upto) as active_upto,date(a.last_date_for_verification) as last_date_for_verification,"
                + "date(a.last_date_for_approval) as last_date_for_approval FROM scheme_onboarding.scheme_ob_basic_info a WHERE a.sl_no =" + schemeId;
        return getResultJSON(sql);
    }

    public JSONArray getBenCountReport(int scheme_id, int office_id) throws Exception {
        String sql = "SELECT * FROM misreports.tbl_schemewise_stat WHERE schemeid =" + scheme_id + " AND office_id=" + office_id;
        return getResultJSON(sql);
    }

    public JSONArray getBenCountReport(int scheme_id) throws Exception {
        String sql = "SELECT schemecode,schemeid,SUM(applied_benef_count) as applied_benef_count,SUM(verified_benef_count) as verified_benef_count,\n"
                + "SUM(approved_benef_count)  as approved_benef_count,SUM(dept_rt_applied_benef_count) as dept_rt_applied_benef_count,\n"
                + "SUM(online_rt_applied_benef_count) as online_rt_applied_benef_count,\n"
                + "SUM(defectivebychecker_benef_count) as defectivebychecker_benef_count,\n"
                + "SUM(defectivebyapprover_benef_count) as defectivebyapprover_benef_count,\n"
                + "SUM(rejectbychecker_benef_count) as rejectbychecker_benef_count,\n"
                + "SUM(rejectbyapprover_benef_count) as rejectbyapprover_benef_count\n"
                + "FROM misreports.tbl_schemewise_stat WHERE schemeid =" + scheme_id + " group by schemecode,schemeid";

//        String sql = "SELECT schemecode,schemeid,SUM(applied_benef_count) as applied_benef_count,SUM(verified_benef_count) as verified_benef_count,\n"
//                + "SUM(approved_benef_count)  as approved_benef_count,SUM(dept_rt_applied_benef_count) as dept_rt_applied_benef_count,\n"
//                + "SUM(online_rt_applied_benef_count) as online_rt_applied_benef_count\n"
//                + "FROM misreports.tbl_schemewise_stat WHERE schemeid =" + scheme_id + " group by schemecode,schemeid";
        return getResultJSON(sql);
    }

    public String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            is.setEncoding("UTF-8");
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSchemeName(int schemeId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_name FROM master_scheme WHERE id = '" + schemeId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getDepartmentName(int deptId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT department_name FROM mas_department WHERE department_id = '" + deptId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getDepartment(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT department_id, department_name FROM public.mas_department ORDER BY department_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getSchemesDept(int dept_id, String defaultValue) throws java.sql.SQLException {
        String query = "SELECT scheme.scheme_id, master.scheme_name FROM bms_user.map_dept_users_group_schemes scheme INNER JOIN master_scheme master ON scheme.department_id = master.department_id WHERE scheme.department_id = " + dept_id + "";
        return fillDropdown(query, defaultValue);

    }

    public String getOnboardedDepartment(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT department_id, department_name FROM public.mas_department WHERE is_onboarded=1 ORDER BY department_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getParamdropdown(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT param_id||'~'||param_name as param_id, param_name FROM public.master_payment_calculation_params "
                + "WHERE status=1 AND param_type=1 ORDER BY param_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getStoreColumn(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT column_id||'~'||store_column_name as column_id, store_column_name"
                + " FROM public.master_payment_params_store_column WHERE status=1 ";
        return fillDropdown(query, defaultValue);
    }

    public String getSelectedStoreColumn(String column_id, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT column_id||'~'||store_column_name as column_id, store_column_name FROM public.master_payment_params_store_column WHERE status=1 "
                + " ORDER BY store_column_name ASC";
        return fillSelectedCaicMethodDropdown(query, column_id, defaultValue);
    }

    public String getSelectedParam(String param_id, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT param_id||'~'||param_name as param_id, param_name FROM public.master_payment_calculation_params WHERE status=1 "
                + "AND param_type=1 ORDER BY param_name ASC";
        return fillSelectedCaicMethodDropdown(query, param_id, defaultValue);
    }

    public String getDepartmentListForOnboarding(String child_id, String defaultValue) throws java.sql.SQLException {
        String query = "";
        if (!"".equals(child_id)) {
            query = "SELECT department_id, department_name FROM public.mas_department where department_id NOT IN(" + child_id + ") ORDER BY department_name ASC";
        } else {
            query = "SELECT department_id, department_name FROM public.mas_department ORDER BY department_name ASC";
        }
        return fillDropdown(query, defaultValue);
    }

    public String getJurisdiction(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id, level FROM public.master_jurisdiction where status=1";
        return fillDropdown(query, defaultValue);
    }

    public int getSchemePeriodicity(int schemeId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_fy_wise_frequency FROM master_scheme WHERE id = '" + schemeId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                result = Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getSchemeAmount(int schemeId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_dbt_amount FROM master_scheme WHERE id = " + schemeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getOfficeName(int officeId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = '" + officeId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getSchemeDbtType(int schemeId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT benefit_type FROM master_scheme WHERE id = '" + schemeId + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getDbtItemList() {
        String result = "";

        return result;
    }

    public String getAdditionalIdType(String defaultValue) throws Exception {
        String query = "SELECT additional_id_type_id, additional_id_type_name FROM master_additional_id_type";
        return fillDropdown(query, defaultValue);
    }

    public String getIrrigationCat(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id, category FROM master_irrigation ORDER BY id ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getCropType(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT crop_type_id, crop_type_name FROM master_crop_type ORDER BY crop_type_id ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getCropSeason(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT season_id, season_name FROM master_season ORDER BY season_id ASC";
        return fillDropdown(query, defaultValue);
    }

    public JSONArray getCropDetails(String gp, String fyear) throws Exception {
        String sql = "SELECT cropdbt_id,dc.ward_code,dc.fyear,crop_type_name,mc.crop_name,season_name,crop_area FROM dbt_crop_details AS dc "
                + "INNER JOIN master_crop_name AS mc ON dc.crop_name = mc.crop_id "
                + "INNER JOIN master_crop_type AS ct ON dc. crop_type = ct.crop_type_id "
                + "INNER JOIN master_season AS ms ON dc.crop_season = ms.season_id "
                + "WHERE ward_code  = '" + gp + "' AND fyear = '" + fyear + "' ";
        return getResultJSON(sql);
    }

    public JSONArray getCropDetails(String label, String label_code, String fyear) throws Exception {
        String sql = "SELECT crop_type_name,mc.crop_name,season_name,SUM(crop_area) AS crop_area FROM dbt_crop_details AS dc "
                + "INNER JOIN master_crop_name AS mc ON dc.crop_name = mc.crop_id "
                + "INNER JOIN master_crop_type AS ct ON dc. crop_type = ct.crop_type_id "
                + "INNER JOIN master_season AS ms ON dc.crop_season = ms.season_id ";
        if (label.equals("b")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report WHERE block_code = '" + label_code + "') ";
        } else if (label.equals("sd")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report WHERE subdiv_code = '" + label_code + "') ";
        } else if (label.equals("d")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report WHERE dist_code = '" + label_code + "') ";
        } else if (label.equals("s")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report ) ";
        }

        sql += "AND fyear = '" + fyear + "' GROUP BY crop_type_name,mc.crop_name,season_name "
                + "ORDER BY crop_type_name,mc.crop_name,season_name";
        return getResultJSON(sql);

        // alternate dynamic query part SELECT ward_code FROM agri_basic_report WHERE block_code = COALESCE(null,block_code) AND subdiv_code = COALESCE(null,subdiv_code) AND dist_code = COALESCE(null,dist_code)
    }

    public JSONArray getIrrigationDetails(String gp, String fyear) throws Exception {
        String sql = "SELECT irrigation_id,di.ward_code,di.fyear,category,irrigation_no,irrigation_area,  "
                + "CASE WHEN irrigation_type = 1 THEN 'Public' ELSE 'Private' END AS irrigation_type FROM dbt_irrigation_details AS di "
                + "INNER JOIN master_irrigation AS mi ON di.irrigation_cat = mi.id "
                + "WHERE ward_code  = '" + gp + "' AND fyear = '" + fyear + "' ";
        return getResultJSON(sql);
    }

    public JSONArray getIrrigationDetails(String label, String label_code, String fyear) throws Exception {
        String sql = "SELECT CASE WHEN irrigation_type = 1 THEN 'Public' ELSE 'Private' END AS irrigation_type, category, "
                + "SUM(irrigation_no) AS irrigation_no,SUM(irrigation_area) AS irrigation_area  FROM dbt_irrigation_details AS di "
                + "INNER JOIN master_irrigation AS mi ON di.irrigation_cat = mi.id ";
        if (label.equals("b")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report WHERE block_code = '" + label_code + "') ";
        } else if (label.equals("sd")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report WHERE subdiv_code = '" + label_code + "') ";
        } else if (label.equals("d")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report WHERE dist_code = '" + label_code + "') ";
        } else if (label.equals("s")) {
            sql += "WHERE ward_code IN (SELECT ward_code FROM agri_basic_report ) ";
        }

        sql += " AND fyear = '" + fyear + "'  GROUP BY irrigation_type,category ORDER BY irrigation_type,category";
        return getResultJSON(sql);
    }

    public JSONArray getDrilledDownData(String fYear, String rcNo) throws Exception {

        String sql = "";
        if (fYear.equals("") && rcNo.equals("")) {
            sql = "SELECT * FROM tran_dbt_drilled_down_data";
        } else if (!fYear.equals("") && rcNo.equals("")) {
            if (fYear.equals("2019-2020")) {
                sql = "SELECT * FROM tran_dbt_drilled_down_data_fy_19_20";
            } else if (fYear.equals("2020-2021")) {
                sql = "SELECT * FROM tran_dbt_drilled_down_data_fy_20_21";
            }
        } else if (fYear.equals("") && !rcNo.equals("")) {
            sql = "SELECT c.dist_code,'0' as parent_code,(SELECT district_name  FROM lgd.tbl_district WHERE lgd_district_code = c.dist_code ) AS dist_name, "
                    + "	(SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) AS ben_count, "
                    + "	(SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) AS ben_tran, "
                    + "	(SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) AS amount  "
                    + "	FROM dbt_beneficiary c WHERE dist_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "'   GROUP BY dist_code  "
                    + "	HAVING (SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) > 0  "
                    + "	UNION "
                    + "	SELECT c.subdiv_code,c.dist_code as parent_code,(SELECT subdivision_name  FROM lgd.tbl_subdivision WHERE lgd_subdivision_code = c.subdiv_code) AS subdiv_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE subdiv_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' GROUP BY subdiv_code,c.dist_code   "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION "
                    + "	SELECT c.block_code,c.subdiv_code as parent_code,(SELECT block_name  FROM lgd.master_district_subdiv_block WHERE block_code = c.block_code::integer ) AS block_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE block_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND block_code <> '0' GROUP BY block_code,c.subdiv_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION  "
                    + "	SELECT c.ward_code,c.block_code as parent_code,(SELECT gp_vc_ward_name FROM lgd.master_dist_sd_block_bac_gp WHERE gp_vc_ward_code = c.ward_code::integer ) AS ward_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE TRIM(ward_code) IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND ward_code <> '0' GROUP BY ward_code,c.block_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	ORDER BY parent_code, dist_name";
        } else if (!fYear.equals("") && !rcNo.equals("")) {
            sql = "SELECT c.dist_code,'0' as parent_code,(SELECT district_name  FROM lgd.tbl_district WHERE lgd_district_code = c.dist_code ) AS dist_name, "
                    + "	(SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) AS ben_count, "
                    + "	(SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) AS ben_tran, "
                    + "	(SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) AS amount  "
                    + "	FROM dbt_beneficiary c WHERE dist_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "'   GROUP BY dist_code  "
                    + "	HAVING (SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) > 0  "
                    + "	UNION "
                    + "	SELECT c.subdiv_code,c.dist_code as parent_code,(SELECT subdivision_name  FROM lgd.tbl_subdivision WHERE lgd_subdivision_code = c.subdiv_code) AS subdiv_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE subdiv_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' GROUP BY subdiv_code,c.dist_code   "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION "
                    + "	SELECT c.block_code,c.subdiv_code as parent_code,(SELECT block_name  FROM lgd.master_district_subdiv_block WHERE block_code = c.block_code::integer ) AS block_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE block_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND block_code <> '0' GROUP BY block_code,c.subdiv_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION  "
                    + "	SELECT c.ward_code,c.block_code as parent_code,(SELECT gp_vc_ward_name FROM lgd.master_dist_sd_block_bac_gp WHERE gp_vc_ward_code = c.ward_code::integer ) AS ward_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE TRIM(ward_code) IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND ward_code <> '0' GROUP BY ward_code,c.block_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	ORDER BY parent_code, dist_name";

        }

        return getResultJSON(sql);
    }

    public JSONArray getDrilledDownData1(String fYear, String rcNo) throws Exception {

        String sql = "";
        if (fYear.equals("") && rcNo.equals("")) {
            sql = "SELECT * FROM tran_dbt_drilled_down_data_temp";
        } else if (!fYear.equals("") && rcNo.equals("")) {
            if (fYear.equals("2019-2020")) {
                sql = "SELECT * FROM tran_dbt_drilled_down_data_fy_19_20";
            } else if (fYear.equals("2020-2021")) {
                sql = "SELECT * FROM tran_dbt_drilled_down_data_fy_20_21_temp";
            }
        } else if (fYear.equals("") && !rcNo.equals("")) {
            sql = "SELECT c.dist_code,'0' as parent_code,(SELECT district_name  FROM lgd.tbl_district WHERE lgd_district_code = c.dist_code ) AS dist_name, "
                    + "	(SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) AS ben_count, "
                    + "	(SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) AS ben_tran, "
                    + "	(SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) AS amount  "
                    + "	FROM dbt_beneficiary c WHERE dist_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "'   GROUP BY dist_code  "
                    + "	HAVING (SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code AND b.ration_card_no = '" + rcNo + "' ) > 0  "
                    + "	UNION "
                    + "	SELECT c.subdiv_code,c.dist_code as parent_code,(SELECT subdivision_name  FROM lgd.tbl_subdivision WHERE lgd_subdivision_code = c.subdiv_code) AS subdiv_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE subdiv_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' GROUP BY subdiv_code,c.dist_code   "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION "
                    + "	SELECT c.block_code,c.subdiv_code as parent_code,(SELECT block_name  FROM lgd.master_district_subdiv_block WHERE block_code = c.block_code::integer ) AS block_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE block_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND block_code <> '0' GROUP BY block_code,c.subdiv_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION  "
                    + "	SELECT c.ward_code,c.block_code as parent_code,(SELECT gp_vc_ward_name FROM lgd.master_dist_sd_block_bac_gp WHERE gp_vc_ward_code = c.ward_code::integer ) AS ward_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE TRIM(ward_code) IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND ward_code <> '0' GROUP BY ward_code,c.block_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	ORDER BY parent_code, dist_name";
        } else if (!fYear.equals("") && !rcNo.equals("")) {
            sql = "SELECT c.dist_code,'0' as parent_code,(SELECT district_name  FROM lgd.tbl_district WHERE lgd_district_code = c.dist_code ) AS dist_name, "
                    + "	(SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) AS ben_count, "
                    + "	(SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) AS ben_tran, "
                    + "	(SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) AS amount  "
                    + "	FROM dbt_beneficiary c WHERE dist_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "'   GROUP BY dist_code  "
                    + "	HAVING (SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.dist_code = c.dist_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "' ) > 0  "
                    + "	UNION "
                    + "	SELECT c.subdiv_code,c.dist_code as parent_code,(SELECT subdivision_name  FROM lgd.tbl_subdivision WHERE lgd_subdivision_code = c.subdiv_code) AS subdiv_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE subdiv_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' GROUP BY subdiv_code,c.dist_code   "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.subdiv_code = c.subdiv_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION "
                    + "	SELECT c.block_code,c.subdiv_code as parent_code,(SELECT block_name  FROM lgd.master_district_subdiv_block WHERE block_code = c.block_code::integer ) AS block_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE block_code IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND block_code <> '0' GROUP BY block_code,c.subdiv_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.block_code = c.block_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	UNION  "
                    + "	SELECT c.ward_code,c.block_code as parent_code,(SELECT gp_vc_ward_name FROM lgd.master_dist_sd_block_bac_gp WHERE gp_vc_ward_code = c.ward_code::integer ) AS ward_name, "
                    + "	( SELECT COUNT(DISTINCT b.beneficiary_id) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_count, "
                    + "	( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS ben_tran, "
                    + "	( SELECT SUM(amount) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') AS amount  "
                    + "	FROM dbt_beneficiary c WHERE TRIM(ward_code) IS NOT NULL AND c.ration_card_no = '" + rcNo + "' AND ward_code <> '0' GROUP BY ward_code,c.block_code  "
                    + "	HAVING ( SELECT COUNT(*) FROM tran_dbt_scheme_beneficiary_payment a INNER JOIN dbt_beneficiary b ON a.beneficiary_id = b.beneficiary_id WHERE b.ward_code = c.ward_code  AND a.financial_year = '" + fYear + "' AND b.ration_card_no = '" + rcNo + "') > 0  "
                    + "	ORDER BY parent_code, dist_name";

        }

        return getResultJSON(sql);
    }

    public JSONArray getDrilledDownBenFamily(String gpCode, String fYear) throws Exception {

        String sql = "";
        if (fYear.equals("")) {
            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
                    + "FROM dbt_beneficiary a "
                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
                    + "WHERE ward_code = '" + gpCode + "' AND approved ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
        } else if (!fYear.equals("")) {
            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
                    + "FROM dbt_beneficiary a "
                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
                    + "WHERE ward_code = '" + gpCode + "' AND approved AND b.financial_year = '" + fYear + "' ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
        }

        return getResultJSON(sql);
    }

    public JSONArray getDrilledDownBen(String fYear, String rcNo) throws Exception {

        String sql = "";
        if (fYear.equals("") && !rcNo.equals("")) {
            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
                    + "FROM dbt_beneficiary a "
                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
                    + "WHERE approved AND a.ration_card_no = '" + rcNo + "' ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
        } else if (!fYear.equals("") && !rcNo.equals("")) {
            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
                    + "FROM dbt_beneficiary a "
                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
                    + "WHERE approved AND b.financial_year = '" + fYear + "' AND a.ration_card_no = '" + rcNo + "' ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
        }

        return getResultJSON(sql);
    }

//    public JSONArray getDrilledDownBen(String gpCode, String fYear, String rcNo) throws Exception {
//
//        String sql = "";
//        if (fYear.equals("") && rcNo.equals("")) {
//            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
//                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
//                    + "FROM dbt_beneficiary a "
//                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
//                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
//                    + "WHERE ward_code = '" + gpCode + "' AND approved ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
//        } else if (!fYear.equals("") && rcNo.equals("")) {
//            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
//                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
//                    + "FROM dbt_beneficiary a "
//                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
//                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
//                    + "WHERE ward_code = '" + gpCode + "' AND approved AND b.financial_year = '" + fYear + "' ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
//        } else if (fYear.equals("") && !rcNo.equals("")) {
//            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
//                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
//                    + "FROM dbt_beneficiary a "
//                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
//                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
//                    + "WHERE ward_code = '" + gpCode + "' AND approved AND a.ration_card_no = '" + rcNo + "' ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
//        } else if (!fYear.equals("") && !rcNo.equals("")) {
//            sql = "SELECT (SELECT scheme_name  FROM master_scheme WHERE id = b.scheme_id ) AS scheme_name,a.beneficiary_name,COALESCE(a.gender,'') AS gender,a.head_of_family,"
//                    + "a.hof_rc_member_id, c.hof_name,COALESCE(a.caste,'') AS category,a.rationcard_memberid,b.amount,ward_code AS parent_code, b.financial_year, b.installment "
//                    + "FROM dbt_beneficiary a "
//                    + "INNER JOIN tran_dbt_scheme_beneficiary_payment b ON a.beneficiary_id = b.beneficiary_id "
//                    + "INNER JOIN rc_hof_data c ON a.ration_card_no = c.ration_card_no "
//                    + "WHERE ward_code = '" + gpCode + "' AND approved AND b.financial_year = '" + fYear + "' AND a.ration_card_no = '" + rcNo + "' ORDER BY a.hof_rc_member_id,a.beneficiary_name,b.financial_year, b.installment;";
//        }
//
//        return getResultJSON(sql);
//    }
    public String getChildOfficeList(int schemeId, int workflow, int parentOfficeId, int level) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        String query = "";

        if (level == 1) {
            query = "SELECT office_id FROM bms_user.map_admin_workflow WHERE  parent_office_id = " + parentOfficeId + " AND scheme_id = " + schemeId + "  AND  (mapping_type = " + workflow + " OR mapping_type = 3)";
        }
//        else if (level == 2) {
//            query = "SELECT office_id FROM mas_Bas_based_office WHERE parent_office_id IN "
//                    + "( SELECT office_id FROM mas_role_based_office WHERE parent_office_id = " + parentOfficeId + ")";
//        }

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (i > 0) {
                    result += ",";
                }
                result += rs.getString(1);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getGpListUnderVLWStore(int storeId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT lgd_gp_code FROM lgd.vlwstore_gpmapping WHERE store_code = " + storeId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (i > 0) {
                    result += ",";
                }
                result += "'" + rs.getString(1) + "'";
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getVLWStoreForGp(int gpId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //int i = 0;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT store_code FROM lgd.vlwstore_gpmapping WHERE lgd_gp_code = '" + gpId + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getURL(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT url_id, url FROM public.mas_application_url ORDER BY url ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getParentOffice(String defaultValue, int deptId) throws java.sql.SQLException {
        String query = "SELECT office_id, office_name  FROM bms_user.mas_dept_users_group_based_office WHERE department_id = " + deptId + " ORDER BY office_id ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getUser(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT user_id, emp_name FROM bms_user.mas_dept_users ORDER BY emp_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public int uploadFile(InputStream file, String uId, String fileName, Long fileSize, String fileType) {
        int fid = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "INSERT INTO mas_files( user_id, file_name, file_mime, file_size, file_data) VALUES (?, ?, ?, ?, ?) RETURNING fid;";

        con = new DBCon.DBSource().connectToAgriDbtFilesDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, uId);
            ps.setString(2, fileName);
            ps.setString(3, fileType);
            ps.setLong(4, fileSize);
            ps.setBinaryStream(5, file, fileSize);
            rs = ps.executeQuery();
            if (rs.next()) {
                fid = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return fid;
    }

    public int uploadEsignFile(String file, String uId, String fileName, Long fileSize, String fileType) {
        int fid = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "INSERT INTO mas_files( user_id, file_name, file_mime, file_size, file_data) VALUES (?, ?, ?, ?, ?) RETURNING fid;";

        con = new DBCon.DBSource().connectToBMSEsignFilesDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, uId);
            ps.setString(2, fileName);
            ps.setString(3, fileType);
            ps.setLong(4, fileSize);
            ps.setString(5, file);
            rs = ps.executeQuery();
            if (rs.next()) {
                fid = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return fid;
    }

    public String getEsignFile(int fId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSEsignFilesDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT file_data FROM mas_files WHERE fid = " + fId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getRoleLevel(int roleId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT role_level FROM mas_admin_user_role WHERE role_id = " + roleId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getSchemePaymentInfo(String scheme_id) {
        String scheme_periodicity = "", scheme_frequency = "", amount_foxed = "", payment_mode = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM scheme_onboarding.scheme_ob_payment_info_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                scheme_periodicity = rs.getString("scheme_periodicity");
                scheme_frequency = rs.getString("scheme_yearly_frequency");
                amount_foxed = rs.getString("amount_fixed");
                payment_mode = rs.getString("payment_mode");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return scheme_periodicity + "~" + scheme_frequency + "~" + amount_foxed + "~" + payment_mode;
    }

    public String getFixedAmountHtml(String scheme_id, String amount_cond) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "", apl_amount = "", bpl_amount = "";
        int beneficiary_financial_status = 0, beneficiary_category = 0;
        String category = "";
        try {
            int cnt = 0;
            int count = 1;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT a.beneficiary_financial_status,b.beneficiary_category,c.category,d.apl_amount,d.bpl_amount\n"
                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft as a INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft as b ON \n"
                    + "b.scheme_id=a.sl_no\n"
                    + "Inner JOIN master_beneficiary_category as c on c.id= b.beneficiary_category\n"
                    + "Inner JOIN scheme_onboarding.scheme_ob_payment_info_cash_draft as d on d.scheme_id= a.sl_no AND d.beneficiary_category=b.beneficiary_category"
                    + " WHERE a.sl_no = '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                cnt = 1;
                int sl_no = count;

                if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                    beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                }
                if (!"".equals(rs.getString("beneficiary_category"))) {
                    beneficiary_category = rs.getInt("beneficiary_category");
                }
                if (!"".equals(rs.getString("category"))) {
                    category = rs.getString("category");
                }
                if (!"".equals(rs.getString("apl_amount"))) {
                    apl_amount = rs.getString("apl_amount");
                }
                if (!"".equals(rs.getString("bpl_amount"))) {
                    bpl_amount = rs.getString("bpl_amount");
                }

                result += "<div class=\"row\" id=" + sl_no + ">";

//                switch (beneficiary_financial_status) {
//                    case 1:
//                        result += "<div class=\"col-md-4 mt-2\">APL: " + apl_amount + "</div>";
//                        break;
//                    case 2:
//                        result += "<div class=\"col-md-4 mt-2\">BPL: " + bpl_amount + "</div>";
//                        break;
//                    default:
//                        result += "<div class=\"col-md-4 mt-2\">APL: " + apl_amount + "</div>";
//                        result += "<div class=\"col-md-4 mt-2\">BPL: " + bpl_amount + "</div>";
//                        break;
//                }
                if (amount_cond.equals("Y")) {
                    result += "<div class=\"col-md-4 mt-2\">" + category + "</div>";
                    switch (beneficiary_financial_status) {
                        case 1:
                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Amount\" value=" + apl_amount + " /></div>";
                            break;
                        case 2:
                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Amount\" value=" + bpl_amount + " /></div>";
                            break;
                        default:
                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Amount\" value=" + apl_amount + " /></div>";
                            //  result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL\" value=" + bpl_amount + " /></div>";
                            break;
                    }
                }
//                else {
//                    switch (beneficiary_financial_status) {
//                        case 1:
//                            result += "<div class=\"col-md-4 mt-2\"> <a href=\"payment_configuration.jsp?data=" + scheme_id + "\" target=\"_blank\"/>Payment Configuration</a></div>";
//                            break;
//                        case 2:
//                            result += "<div class=\"col-md-4 mt-2\"> <a href=\"payment_configuration.jsp?data=" + scheme_id + "\" target=\"_blank\"/>Payment Configuration</a></div>";
//                            break;
//                        default:
//                            result += "<div class=\"col-md-4 mt-2\"> <a href=\"payment_configuration.jsp?data=" + scheme_id + "\" target=\"_blank\"/>Payment Configuration</a></div>";
//                            break;

//                        case 1:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"Ceiling Amount\" value=" + apl_amount + " /></div>";
//                            break;
//                        case 2:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL Ceiling Amount\" value=" + bpl_amount + " /></div>";
//                            break;
//                        default:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"APL Ceiling Amount\" value=" + apl_amount + " /></div>";
//                          //  result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL Ceiling Amount\" value=" + bpl_amount + " /></div>";
//                            break;
                //   }
                // }
                result += "</div>";
                count++;
            }
            if (amount_cond.equals("Y")) {
                if (cnt < 1) {
                    result += "<div class=\"row\"><div class=\"col-md-12\">No Data Found</div></div>";
//              switch (beneficiary_financial_status) {
//                        case 1:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"APL Ceiling Amount\" value=" + apl_amount + " /></div>";
//                            break;
//                        case 2:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL Ceiling Amount\" value=" + bpl_amount + " /></div>";
//                            break;
//                        default:
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\" autocomplete=\"off\" class=\"form-control\" id=" + category + "_apl name=" + category + "_apl placeholder=\"APL Ceiling Amount\" value=" + apl_amount + " /></div>";
//                            result += "<div class=\"col-md-4 mt-2\"> <input type=\"text\"  autocomplete=\"off\" class=\"form-control\" id=" + category + "_bpl name=" + category + "_bpl placeholder=\"BPL Ceiling Amount\" value=" + bpl_amount + " /></div>";
//                            break;
//                    }
                }
            }
//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getPaymentConfigHtml(String scheme_id) throws java.sql.SQLException, JSONException, ClassNotFoundException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String unit_name = "", rate = "", unit_id = "", calc_method_type = "", ddlCalcType = "", calc_method_param_value_json = "", ddlParamdropdown = "", ddlStoreColumndropdown = "";
        String category = "", calc_method_param_name = "", calc_method_param_store_column_name = "", cond_param_list = null;
        String[] param_list = null;
        try {
            int cnt = 0, calc_method_param_id = 0, calc_method_param_store_column_id = 0;
            int count = 1;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT * from scheme_onboarding.scheme_ob_payment_configuration_draft WHERE scheme_id = '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                cnt = 1;
                int sl_no = count;

                if (!"".equals(rs.getString("calc_method_type"))) {
                    calc_method_type = rs.getString("calc_method_type");
                }
                if ((!"".equals(rs.getString("calc_method_param_id"))) && (rs.getString("calc_method_param_id") != null)) {
                    calc_method_param_id = rs.getInt("calc_method_param_id");
                }
                if (!"".equals(rs.getString("calc_method_param_name"))) {
                    calc_method_param_name = rs.getString("calc_method_param_name");
                }
                if ((!"".equals(rs.getString("calc_method_param_store_column_id"))) && (rs.getString("calc_method_param_store_column_id") != null)) {
                    calc_method_param_store_column_id = rs.getInt("calc_method_param_store_column_id");
                }
                if (!"".equals(rs.getString("calc_method_param_store_column_name"))) {
                    calc_method_param_store_column_name = rs.getString("calc_method_param_store_column_name");
                }
                if ((!"".equals(rs.getString("rate_unit_field_json"))) && (rs.getString("rate_unit_field_json") != null)) {
                    calc_method_param_value_json = rs.getString("rate_unit_field_json");
                }
                if ("1".equals(calc_method_type)) {
                    ddlCalcType = getCalculationType(calc_method_type, "-Please Select-");
                    result += "<div class=\"form-row\">";
                    result += "<div class=\"form-group col-md-6 required\">";
                    result += "<label for=\"dept\" class=\"control-label\" >Payment Method :</label>";
                    result += "<select class=\"custom-select mr-sm-2\" required=\"\" name=\"payment_method\" id=\"payment_method\" onchange=\"checkMethodData(this.value)\">";
                    result += ddlCalcType;
                    result += "</select>";
                    result += "</div></div> <div class=\"form-row\" id=\"method1\"><div class=\"col-md-12\" id=\"param_field\">";
                    cond_param_list = getSchemecondParamHtml(scheme_id);
                    if (!"".equals(cond_param_list)) {
                        param_list = cond_param_list.split(",");
                    }

                    for (int i = 0; i < param_list.length; i++) {
                        ddlParamdropdown = getSelectedParam(param_list[i].split("~")[0], "-Please Select-");
                        ddlStoreColumndropdown = getSelectedStoreColumn(param_list[i].split("~")[1], "-Please Select-");
                        if (i == 0) {
                            result += "<div class=\"input-group\"><div class=\"form-group col-md-4 required\">";
                            result += "<label for=\"scheme\" class=\"control-label\">Conditional Parameter :</label>";
                            result += "<select class=\"custom-select mr-sm-2\" id=\"cond_param\" name=\"cond_param\" required=\"\">";
                            result += ddlParamdropdown + "</select></div><div class=\"form-group col-md-4 required\">";
                            result += "<label for=\"scheme\" class=\"control-label\">Store Parameter Value In :</label>";
                            result += "<select class=\"custom-select mr-sm-2\" id=\"store_column\" name=\"store_column\" required=\"\">";
                            result += ddlStoreColumndropdown + "</select></div><div class=\"input-group-btn col-md-1\"><br/>";
                            result += "<button class=\"btn btn-success\" type=\"button\"  onclick=\"param_field();\"> ";
                            result += "<span class=\"plus\">+</span> </button></div></div>";
                            result += "<span id=\"lblinstallmentError\" style=\"color: red\"></span>";
                        } else {
                            result += " <div class=\"form-group removeclass" + i + "\"><div class=\"input-group mt-2\">";
                            result += "<div class=\"input-group\"><div class=\"form-group col-md-4 required\">";
                            result += "<label for=\"scheme\" class=\"control-label\">Conditional Parameter :</label>";
                            result += "<select class=\"custom-select mr-sm-2\" id=\"cond_param\" name=\"cond_param\" required=\"\">";
                            result += ddlParamdropdown + "</select></div>";
                            result += "<div class=\"form-group col-md-4 required\">";
                            result += "<label for=\"scheme\" class=\"control-label\">Store Parameter Value In :</label>";
                            result += "<select class=\"custom-select mr-sm-2\" id=\"store_column\" name=\"store_column\" required=\"\">";
                            result += ddlStoreColumndropdown + "</select></div>";
                            result += "<div class=\"input-group-btn col-md-1\"><br/>";
                            result += "<button class=\"btn btn-danger\" type=\"button\"  onclick=\"remove_param_field(" + i + ");\">  ";
                            result += "<span class=\"minus\">-</span> </button></div></div></div></div>";
                        }
                    }
                   result += "</div></div>"; 
                } else {
                    JSONObject obj = new JSONObject(calc_method_param_value_json);
                    JSONArray details = new JSONArray(obj.get("calc_method_param_json_array").toString());

                    for (int j = 0; j < details.length(); j++) {
                        JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());
                        unit_id = tmpOBJ.optString("unit_id");
                        unit_name = tmpOBJ.optString("unit_name");
                        rate = tmpOBJ.optString("rate_per_unit");
                    }

                    ddlCalcType = getCalculationType(calc_method_type, "-Please Select-");
                    String getUnitDropdownHtml = getUnitDropdownHtml(Integer.parseInt(calc_method_type), unit_id, "-Please Select-");
                    result += "<div class=\"form-row d\">";
                    result += "<div class=\"form-group col-md-6 required\">";
                    result += "<label for=\"dept\" class=\"control-label\" >Payment Method :</label>";
                    result += "<select class=\"custom-select mr-sm-2\" required=\"\" name=\"payment_method\" id=\"payment_method\" onchange=\"checkMethodData(this.value)\">";
                    result += ddlCalcType;
                    result += "</select>";
                    result += "</div></div>";
                    result += "<div class=\"form-row\" id=\"method2\">";
                    result += "<div class=\"form-group col-md-6 required\">";
                    result += "<label for=\"dept\" class=\"control-label\" >Rate :</label>";
                    result += "<input type=\"text\" name=\"rate\" id=\"rate\" onkeypress=\"return IsNumeric(event);\" class=\"form-control\" value=" + rate + ">";
                    result += "<span id=\"lblError\" style=\"color: red\"></span>";
                    result += "</div>";
                    result += " <div class=\"form-group col-md-6 required\">";
                    result += "<label for=\"dept\" class=\"control-label\" >Unit :</label>";
                    result += "<select class=\"custom-select mr-sm-2\" id=\"unit\" required=\"\" name=\"unit\">";
                    result += getUnitDropdownHtml;
                    result += "</select>";
                    result += "</div>";
                    result += "</div> ";
                }
            }
//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getFixedAmount(String scheme_id) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        String query_set = "", child_id = "", uid = "", uemail = "", apl_amount = "", bpl_amount = "";
        try {
            int cnt = 0;
            int count = 1;
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            String query = "SELECT a.beneficiary_financial_status,b.beneficiary_category,c.category,d.apl_amount,d.bpl_amount\n"
                    + "FROM scheme_onboarding.scheme_ob_basic_info_draft as a INNER JOIN scheme_onboarding.scheme_ob_bsic_info_beneficiary_category_draft as b ON \n"
                    + "b.scheme_id=a.sl_no\n"
                    + "Inner JOIN master_beneficiary_category as c on c.id= b.beneficiary_category\n"
                    + "Inner JOIN scheme_onboarding.scheme_ob_payment_info_cash_draft as d on d.scheme_id= a.id AND d.beneficiary_category=b.beneficiary_category"
                    + " WHERE a.sl_no = '" + scheme_id + "'";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                cnt = 1;
                int sl_no = count;
                String category = "";
                int beneficiary_financial_status = 0, beneficiary_category = 0;
                if (!"".equals(rs.getString("beneficiary_financial_status"))) {
                    beneficiary_financial_status = rs.getInt("beneficiary_financial_status");
                }
                if (!"".equals(rs.getString("beneficiary_category"))) {
                    beneficiary_category = rs.getInt("beneficiary_category");
                }
                if (!"".equals(rs.getString("category"))) {
                    category = rs.getString("category");
                }
                if (!"".equals(rs.getString("apl_amount"))) {
                    apl_amount = rs.getString("apl_amount");
                }
                if (!"".equals(rs.getString("bpl_amount"))) {
                    bpl_amount = rs.getString("bpl_amount");
                }

                result += "<div class=\"row\" id=" + sl_no + ">";

                result += "<div class=\"col-md-4 mt-2\">" + category + "</div>";
                switch (beneficiary_financial_status) {
                    case 1:
                        result += "<div class=\"col-md-4 mt-2\">APL: " + apl_amount + "</div>";
                        break;
                    case 2:
                        result += "<div class=\"col-md-4 mt-2\">BPL: " + bpl_amount + "</div>";
                        break;
                    default:
                        result += "<div class=\"col-md-4 mt-2\">APL: " + apl_amount + "</div>";
                        result += "<div class=\"col-md-4 mt-2\">BPL: " + bpl_amount + "</div>";
                        break;
                }
                result += "</div>";
                count++;
            }
            if (cnt < 1) {
                result += "<div class=\"row\"><div class=\"col-md-12\">No Data Found</div></div>";
            }
//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getSchemeCommodityList(String scheme_id) {
        String commodity_list = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_id,commodity_name, subsidy_amount, effective_from FROM scheme_onboarding.scheme_ob_payment_info_kind_draft INNER JOIN master_commodity_list \n"
                    + "On master_commodity_list.commodity_code=scheme_ob_payment_info_kind_draft.commodity_code WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (commodity_list.trim().length() > 0) {
                    commodity_list = commodity_list + "<br/>" + "<strong>Commodity Name:</strong> " + rs.getString("commodity_name") + "&nbsp;<strong>Subsidy Amount:</strong> " + rs.getString("subsidy_amount") + "&nbsp;<strong>Effective From:</strong> " + rs.getString("effective_from") + "";
                } else {
                    commodity_list = "<strong>Commodity Name:</strong> " + rs.getString("commodity_name") + "&nbsp;<strong>Subsidy Amount:</strong> " + rs.getString("subsidy_amount") + "&nbsp;<strong>Effective From:</strong> " + rs.getString("effective_from") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return commodity_list;
    }

    public String getSchemestakeholderDeptHtml(String scheme_id) {
        String bsp_dept = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {

            ps = con.prepareStatement("SELECT maker_dept_id, maker_dept_name, maker_jurisdiction_level, checker_dept_id, checker_dept_name, checker_jurisdiction_level, approver_dept_id, approver_dept_name, approver_jurisdiction_level\n"
                    + "	FROM scheme_onboarding.scheme_ob_sh_dept_bsp_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (bsp_dept.trim().length() > 0) {
                    bsp_dept = bsp_dept + "," + rs.getString("maker_dept_id") + "~" + rs.getString("checker_dept_id") + "";
                } else {
                    bsp_dept = "" + rs.getString("maker_dept_id") + "~" + rs.getString("checker_dept_id") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return bsp_dept;
    }

    public String getSchemecondParamHtml(String scheme_id) {
        String param_list = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {

            ps = con.prepareStatement("SELECT scheme_id, calc_method_type, calc_method_param_id, calc_method_param_name, calc_method_param_store_column_id, calc_method_param_store_column_name\n"
                    + "	FROM scheme_onboarding.scheme_ob_payment_configuration_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (param_list.trim().length() > 0) {
                    param_list = param_list + "," + rs.getString("calc_method_param_id") + "~" + rs.getString("calc_method_param_store_column_id") + "";
                } else {
                    param_list = "" + rs.getString("calc_method_param_id") + "~" + rs.getString("calc_method_param_store_column_id") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return param_list;
    }

    public String getSchemeCommodityListHtml(String scheme_id) {
        String commodity_list = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {

            ps = con.prepareStatement("SELECT scheme_id,scheme_ob_payment_info_kind_draft.commodity_code, subsidy_amount, effective_from FROM scheme_onboarding.scheme_ob_payment_info_kind_draft INNER JOIN master_commodity_list \n"
                    + "On master_commodity_list.commodity_code=scheme_ob_payment_info_kind_draft.commodity_code WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (commodity_list.trim().length() > 0) {
                    commodity_list = commodity_list + "," + rs.getString("commodity_code") + "~" + rs.getString("subsidy_amount") + "~" + rs.getString("effective_from") + "";
                } else {
                    commodity_list = "" + rs.getString("commodity_code") + "~" + rs.getString("subsidy_amount") + "~" + rs.getString("effective_from") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return commodity_list;
    }

    public String getrequireddocListList(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_name FROM master_document Inner Join required_document on required_document.document_id=master_document.id"
                    + " WHERE required_document.scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_name") + "";
                } else {
                    req_doc = "" + rs.getString("document_name") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getPrefilledListId(String scheme_id) {
        String prefilled_id = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT field_id FROM scheme_onboarding.scheme_ob_additional_info_draft WHERE scheme_ob_additional_info_draft.reference_id='" + scheme_id + "' AND field_type='prefilled'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (prefilled_id.trim().length() > 0) {
                    prefilled_id = prefilled_id + "," + "" + rs.getString("field_id") + "";
                } else {
                    prefilled_id = "" + rs.getString("field_id") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return prefilled_id;
    }

    public String getrequireddocListPoi(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_id FROM master_document Inner Join scheme_onboarding.scheme_ob_bsic_info_required_document_draft on scheme_ob_bsic_info_required_document_draft.document_id=master_document.id "
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id='" + scheme_id + "' AND scheme_ob_bsic_info_required_document_draft.document_type=1");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_id") + "";
                } else {
                    req_doc = "" + rs.getString("document_id") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getrequireddocListPoa(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_id FROM master_document Inner Join scheme_onboarding.scheme_ob_bsic_info_required_document_draft on scheme_ob_bsic_info_required_document_draft.document_id=master_document.id "
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id='" + scheme_id + "' AND scheme_ob_bsic_info_required_document_draft.document_type=2");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_id") + "";
                } else {
                    req_doc = "" + rs.getString("document_id") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getrequireddocListScheme(String scheme_id) {
        String req_doc = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT document_id FROM master_document Inner Join scheme_onboarding.scheme_ob_bsic_info_required_document_draft on scheme_ob_bsic_info_required_document_draft.document_id=master_document.id "
                    + " WHERE scheme_ob_bsic_info_required_document_draft.scheme_id='" + scheme_id + "' AND scheme_ob_bsic_info_required_document_draft.document_type=4");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (req_doc.trim().length() > 0) {
                    req_doc = req_doc + "," + "" + rs.getString("document_id") + "";
                } else {
                    req_doc = "" + rs.getString("document_id") + "";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return req_doc;
    }

    public String getSchemeMakerBspInfo(String scheme_id) {
        String maker_dept = "", checker_dept = "", approver_dept = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT maker_dept_name FROM scheme_onboarding.scheme_ob_sh_dept_bsp_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();

            while (rs.next()) {
                if (maker_dept.trim().length() > 0) {
                    maker_dept = maker_dept + "," + "" + rs.getString("maker_dept_name") + "";
                } else {
                    maker_dept = "" + rs.getString("maker_dept_name") + "";
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return maker_dept;
    }

    public String getSchemeCheckerBspInfo(String scheme_id) {
        String maker_dept = "", checker_dept = "", approver_dept = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT checker_dept_name FROM scheme_onboarding.scheme_ob_sh_dept_bsp_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();

            while (rs.next()) {
                if (checker_dept.trim().length() > 0) {
                    checker_dept = checker_dept + "," + "" + rs.getString("checker_dept_name") + "";
                } else {
                    checker_dept = "" + rs.getString("checker_dept_name") + "";
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return checker_dept;
    }

    public String getSchemeApproverBspInfo(String scheme_id) {
        String maker_dept = "", checker_dept = "", approver_dept = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT approver_dept_name FROM scheme_onboarding.scheme_ob_sh_dept_bsp_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();

            if (rs.next()) {
                approver_dept = rs.getString("approver_dept_name");

            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return approver_dept;
    }

    public String getSchemePPInfo(String scheme_id) {
        String maker_dept = "", checker_dept = "", approver_dept = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM scheme_onboarding.scheme_ob_sh_dept_pp_draft WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                maker_dept = rs.getString("maker_dept_name");
                checker_dept = rs.getString("checker_dept_name");
                approver_dept = rs.getString("approver_dept_name");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return maker_dept + "~" + checker_dept + "~" + approver_dept;
    }

    public String getRoleName(int schemeId, int groupId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        String sql = "SELECT role_name FROM bms_user.map_dept_users_group_schemes map INNER JOIN bms_user.mas_user_roles r ON r.role_id = map.role_id WHERE scheme_id = " + schemeId + " AND group_id = " + groupId;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getRoleName(int roleId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        String sql = "SELECT role_name FROM bms_user.mas_user_roles WHERE role_id = " + roleId;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getSchemeGroupMappedRoleId(int schemeId, int groupId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT role_id FROM bms_user.map_dept_users_group_schemes map WHERE scheme_id = " + schemeId + " AND group_id = " + groupId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String[][] getroleList(int deptId) {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] roleList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT role_id, role_name, role.department_id, role_level, department_name\n"
                + "  FROM mas_admin_user_role as role inner join mas_department as dept on role.department_id = dept.department_id "
                + "  WHERE role.department_id =" + deptId;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                roleList = new String[rowCount][5];
            }
            while (rset.next()) {
                roleList[row][0] = rset.getString("role_id");
                roleList[row][1] = rset.getString("role_name");
                roleList[row][2] = rset.getString("department_name");
                roleList[row][3] = rset.getString("role_level");
                roleList[row][4] = rset.getString("department_id");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return roleList;
    }

    public String[][] getUrlList() {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] UrlList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT url_id, url, url_name, is_header, is_active, url_desc FROM mas_application_url";
        //  + "  FROM mas_admin_user_role as role inner join mas_department as dept on role.department_id = dept.department_id";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); //ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                UrlList = new String[rowCount][6];
            }
            while (rset.next()) {
                UrlList[row][0] = rset.getString("url_id");
                UrlList[row][1] = rset.getString("url");
                UrlList[row][2] = rset.getString("url_name");
                UrlList[row][3] = rset.getString("is_header");
                UrlList[row][4] = rset.getString("is_active");
                //UrlList[row][5] = rset.getString("url_desc");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return UrlList;
    }

    public String[][] getUserList(int deptId, int roleId) {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] UserList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT user_id, user_login_id, user_password, user_name, user_role, COALESCE(user_email,'') AS user_email, user_mobile, \n"
                + "   user_department_id, user_designation, \n"
                + "   department_name, role_name, COALESCE(rc_mem,'') AS user_rc_member_id \n"
                + " FROM mas_admin_users_account AS acc "
                + " INNER JOIN mas_department as dept ON acc.user_department_id = dept.department_id \n"
                + " INNER JOIN mas_admin_user_role AS usrRole ON usrRole.role_id = acc.user_role "
                + " WHERE is_blocked = FALSE ";
        if (roleId > 1) {
            sql += "AND acc.user_department_id = " + deptId;
        }

        if (deptId > 1) {
            sql += "AND acc.user_role = " + roleId;
        }

        sql += "ORDER BY  acc.user_department_id ASC, user_role ASC, user_id";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                UserList = new String[rowCount][15];
            }
            while (rset.next()) {
                UserList[row][0] = rset.getString("user_id");
                UserList[row][1] = rset.getString("user_name");
                UserList[row][2] = rset.getString("user_login_id");
                UserList[row][3] = rset.getString("user_email");
                UserList[row][4] = rset.getString("user_mobile");
                UserList[row][5] = rset.getString("department_name");
                UserList[row][6] = rset.getString("user_designation");
                //UserList[row][7] = rset.getString("hrms_code");
                //UserList[row][8] = rset.getString("rc_mem");
                //UserList[row][5] = rset.getString("user_gender");
                //UserList[row][6] = rset.getString("user_alt_mobile");
                /* UserList[row][4] = rset.getString("department_name");
                UserList[row][5] = rset.getString("role_name");
                UserList[row][6] = rset.getString("user_designation");
                UserList[row][7] = rset.getString("user_login_id");*/

//                UserList[row][9] = rset.getString("address1_road_landmark");
//                UserList[row][10] = rset.getString("address1_city_town_village");
//                UserList[row][11] = rset.getString("address1_po");
//                UserList[row][12] = rset.getString("address1_district");
//                UserList[row][13] = rset.getString("address1_pin");
//                UserList[row][14] = rset.getString("user_photo_fid");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return UserList;
    }

    public String[][] getUserList() {
        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] UserList = null;

        int rowCount = 0, row = 0;

        /* String sql = "SELECT user_id, user_login_id, user_password, user_name, user_role, COALESCE(user_email,'') AS user_email, user_mobile, \n"
                + "       user_gender, user_alt_mobile, user_department_id, user_designation, \n"
                + "       address1_road_landmark, address1_city_town_village, address1_po, \n"
                + "       address1_district, address1_pin, user_photo_fid, department_name, role_name, COALESCE(user_rc_member_id,'') AS user_rc_member_id \n"
                + " FROM mas_admin_user_account AS acc "
                + " INNER JOIN mas_department as dept ON acc.user_department_id = dept.department_id \n"
                + " INNER JOIN mas_admin_user_role AS usrRole ON usrRole.role_id = acc.user_role "
                + " WHERE is_blocked = FALSE ";
        if (roleId > 1) {
            sql += "AND acc.user_department_id = " + deptId;
        }

        if (deptId > 1) {
            sql += "AND acc.user_role = " + roleId;
        }

        sql += "ORDER BY  acc.user_department_id ASC, user_role ASC, user_id";*/
        String sql = "SELECT * FROM mas_dept_user;";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                UserList = new String[rowCount][15];
            }
            while (rset.next()) {
                UserList[row][0] = rset.getString("user_id");
                UserList[row][1] = rset.getString("emp_name");
                UserList[row][2] = rset.getString("emp_code");
                UserList[row][3] = rset.getString("emp_designation");
                UserList[row][4] = rset.getString("emp_email");
                UserList[row][5] = rset.getString("emp_mobile");
                UserList[row][6] = rset.getString("emp_dob");
                UserList[row][7] = rset.getString("hrms_code");
                UserList[row][8] = rset.getString("rc_mem");
                //UserList[row][5] = rset.getString("user_gender");
                //UserList[row][6] = rset.getString("user_alt_mobile");
                /* UserList[row][4] = rset.getString("department_name");
                UserList[row][5] = rset.getString("role_name");
                UserList[row][6] = rset.getString("user_designation");
                UserList[row][7] = rset.getString("user_login_id");*/

//                UserList[row][9] = rset.getString("address1_road_landmark");
//                UserList[row][10] = rset.getString("address1_city_town_village");
//                UserList[row][11] = rset.getString("address1_po");
//                UserList[row][12] = rset.getString("address1_district");
//                UserList[row][13] = rset.getString("address1_pin");
//                UserList[row][14] = rset.getString("user_photo_fid");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return UserList;
    }

    public String[][] getUrlRoleList() {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] UrlRoleList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT map_id, roleUrl.department_id, roleUrl.role_id, roleUrl.url_id, is_main_pg, role_name, department_name, url_name FROM map_admin_user_role_url AS roleUrl \n"
                + "                 INNER JOIN mas_department AS dept ON roleUrl.department_id = dept.department_id \n"
                + "                 INNER JOIN bms_user.mas_user_roles AS  usrRole ON usrRole.role_id = roleUrl.role_id \n"
                + "                 INNER JOIN mas_application_url as url ON url.url_id = roleUrl.url_id";
        //  + "  FROM mas_admin_user_role as role inner join mas_department as dept on role.department_id = dept.department_id";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                UrlRoleList = new String[rowCount][6];
            }
            while (rset.next()) {
                UrlRoleList[row][0] = rset.getString("map_id");
                UrlRoleList[row][1] = rset.getString("department_name");
                UrlRoleList[row][2] = rset.getString("role_name");
                UrlRoleList[row][3] = rset.getString("url_name");
                UrlRoleList[row][4] = rset.getString("is_main_pg");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return UrlRoleList;
    }

    public String[][] getUrlRoleList(int deptId, int roleId) {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] UrlRoleList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT map_id, roleUrl.department_id, roleUrl.role_id, roleUrl.url_id, is_main_pg, roleUrl.is_active, roleUrl.priority_no, role_name, department_name, url_name "
                + " FROM map_admin_user_role_url AS roleUrl \n"
                + " INNER JOIN mas_department AS dept ON roleUrl.department_id = dept.department_id \n"
                + " INNER JOIN bms_user.mas_user_roles AS  usrRole ON usrRole.role_id = roleUrl.role_id \n"
                + " INNER JOIN mas_application_url as url ON url.url_id = roleUrl.url_id"
                + " WHERE roleUrl.department_id = " + deptId + " AND roleUrl.role_id=" + roleId;
        //  + "  FROM mas_admin_user_role as role inner join mas_department as dept on role.department_id = dept.department_id";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                UrlRoleList = new String[rowCount][7];
            }
            while (rset.next()) {
                UrlRoleList[row][0] = rset.getString("map_id");
                UrlRoleList[row][1] = rset.getString("department_name");
                UrlRoleList[row][2] = rset.getString("role_name");
                UrlRoleList[row][3] = rset.getString("url_name");
                UrlRoleList[row][4] = rset.getString("is_main_pg");
                UrlRoleList[row][5] = rset.getString("is_active");
                UrlRoleList[row][6] = rset.getString("priority_no");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return UrlRoleList;
    }

    public String[][] getOfficeList(int deptId, int groupId) {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] officeList = null;

        int rowCount = 0, row = 0;

        /*String sql = "SELECT offc.office_id, offc.office_name, "
                + "  role_name, department_name "
                + " FROM mas_role_based_office AS offc \n"
                + " INNER JOIN mas_department AS dept ON offc.dept_id = dept.department_id \n"
                + " INNER JOIN mas_admin_user_role AS  usrRole ON usrRole.role_id = offc.role_id \n"
                + " INNER JOIN mas_role_based_office AS offc2 ON offc2.office_id = offc.office_id \n"
                + " WHERE offc.dept_id = " + deptId + " AND offc.role_id=" + roleId;*/
        //  + "  FROM mas_admin_user_role as role inner join mas_department as dept on role.department_id = dept.department_id";
        String sql = "SELECT offc.office_id, offc.office_name, "
                + " group_name, department_name "
                + " FROM bms_user.mas_dept_users_group_based_office AS offc \n"
                + " INNER JOIN mas_department AS dept ON offc.department_id = dept.department_id \n"
                + " INNER JOIN bms_user.mas_dept_users_group AS  usrGrp ON usrGrp.group_id = offc.group_id \n"
                //+ " INNER JOIN mas_role_based_office AS offc2 ON offc2.office_id = offc.office_id \n"
                + " WHERE offc.department_id = " + deptId + " AND offc.group_id=" + groupId;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                officeList = new String[rowCount][5];
            }
            while (rset.next()) {
                officeList[row][0] = rset.getString("office_id");
                officeList[row][1] = rset.getString("department_name");
                officeList[row][2] = rset.getString("group_name");
                officeList[row][3] = rset.getString("office_name");
                //officeList[row][4] = rset.getString("parent_office_name");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return officeList;
    }

    public String[][] getReoprtList() {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] reoprtList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT map_id, report.department_id, user_id_reportee, user_id_reporter, department_name \n"
                + "                FROM map_admin_user_reporting_structure AS report INNER JOIN mas_department AS dept ON report.department_id = dept.department_id";
        //  + "  FROM mas_admin_user_role as role inner join mas_department as dept on role.department_id = dept.department_id";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                reoprtList = new String[rowCount][4];
            }
            while (rset.next()) {
                reoprtList[row][0] = rset.getString("map_id");
                reoprtList[row][1] = rset.getString("department_name");
                reoprtList[row][2] = rset.getString("user_id_reportee");
                reoprtList[row][3] = rset.getString("user_id_reporter");

                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return reoprtList;
    }

//    public JSONArray getAllowedUrls(int deptId, int userRole) throws Exception {
//        String sql = "SELECT a.url_id, url, target, url_name, header_id "
//                + " FROM map_admin_user_role_url a "
//                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
//                + " WHERE a.is_active = 1 AND b.is_header = 0 AND b.is_active = 1 AND department_id = " + deptId + " AND role_id = " + userRole + " "
//                + " ORDER BY priority_no ASC";
//        return getResultJSON(sql);
//    }
    /*public JSONArray getAllowedHeaders(int deptId, int userRole) throws Exception {
        String sql = "SELECT a.url_id, url, target, url_name "
                + " FROM map_admin_user_role_url a "
                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
                + " WHERE a.is_active = 1 AND b.is_header = 1 AND b.is_active = 1 AND department_id = " + deptId + " "
                + " AND role_id = " + userRole + " "
                + " ORDER BY priority_no ASC";
        return getResultJSON(sql);
    }*/
    public JSONArray getAllowedHeaders(int userRole) throws Exception {
        String sql = "SELECT a.url_id, url, target, url_name "
                + " FROM map_admin_user_role_url a "
                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
                + " WHERE a.is_active = 1 AND b.is_header = 1 AND b.is_active = 1" // AND department_id = " + deptId + " 
                + " AND role_id = " + userRole + " "
                + " ORDER BY priority_no ASC";
        return getResultJSON(sql);
    }

    public JSONArray getAllowedHeaders(int userRole, int schemeId) throws Exception {
        String sql = "SELECT a.url_id, url, target, url_name "
                + " FROM map_scheme_role_url a "
                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
                + " WHERE a.is_active = 1 AND b.is_header = 1 AND b.is_active = 1" // AND department_id = " + deptId + " 
                + " AND role_id = " + userRole + " AND scheme_id = " + schemeId + " "
                + " ORDER BY priority_no ASC";
        return getResultJSON(sql);
    }

    /*public JSONArray getAllowedUrls(int deptId, int userRole, int headerId) throws Exception {
        String sql = "SELECT a.url_id, url, target, url_name, header_id, COALESCE(url_desc,'') AS url_desc "
                + " FROM map_admin_user_role_url a "
                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
                + " WHERE a.is_active = 1 AND b.is_header = 0 AND b.is_active = 1 AND department_id = " + deptId + " "
                + " AND role_id = " + userRole + " AND header_id = " + headerId + ""
                + " ORDER BY priority_no ASC";
        return getResultJSON(sql);
    }*/
    public JSONArray getAllowedUrls(int userRole, int headerId) throws Exception {
        String sql = "SELECT a.url_id, url, target, url_name, header_id, COALESCE(url_desc,'') AS url_desc "
                + " FROM map_admin_user_role_url a "
                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
                + " WHERE a.is_active = 1 AND b.is_header = 0 AND b.is_active = 1  " //AND department_id = " + deptId + "
                + " AND role_id = " + userRole + " AND header_id = " + headerId + ""
                + " ORDER BY priority_no ASC";
        return getResultJSON(sql);
    }

    public JSONArray getAllowedUrls(int userRole, int schemeId, int headerId) throws Exception {
        String sql = "SELECT a.url_id, url, target, url_name, header_id, COALESCE(url_desc,'') AS url_desc "
                + " FROM map_scheme_role_url a "
                + " INNER JOIN mas_application_url b ON a.url_id = b.url_id "
                + " WHERE a.is_active = 1 AND b.is_header = 0 AND b.is_active = 1  " //AND department_id = " + deptId + "
                + " AND role_id = " + userRole + " AND header_id = " + headerId + " AND scheme_id = " + schemeId + " "
                + " ORDER BY priority_no ASC";
        return getResultJSON(sql);
    }

    public String getUserSessionDetails(String userId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        String sql = "SELECT emp_name, emp_department_id, emp_designation FROM bms_user.mas_dept_users WHERE user_id = '" + userId + "'";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1) + "#" + rs.getString(2) + "#" + rs.getString(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public JSONArray getUserJuridictiondetailsAgri(String userId, int deptId) throws Exception {
        String sql = "SELECT dist_code, subdiv_code, store_id, store_type_id  FROM map_admin_user_store_mapping WHERE user_id='" + userId + "' AND department_id = " + deptId;
        return getResultJSON(sql);
    }

    public JSONArray getUserOfficeDetails(String userId, int deptId) throws Exception {
        String sql = "SELECT a.user_office , COALESCE((SELECT DISTINCT b.lgd_district_code FROM map_maker_to_lgd_gp b WHERE b.office_id = a.user_office AND b.dept_id = a.user_department_id),'0')::integer AS dist_code FROM mas_admin_user_account a WHERE user_id ='" + userId + "' AND user_department_id = " + deptId;
        return getResultJSON(sql);
    }

    public JSONArray getCitizenDocument(String applId) throws Exception {
        String sql = "SELECT document_id, document_name,fid\n"
                + "  FROM citizen.tbl_citizen_file_upload a\n"
                + "  LEFT JOIN master_document b ON a.document_id = b.id\n"
                + "  WHERE apllication_id ='" + applId + "'";
        return getResultJSON(sql);
    }

    public String getUserRoleLevel(int userRole) {
        String level = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT role_level FROM mas_admin_user_role WHERE role_id = " + userRole);
            rs = ps.executeQuery();
            if (rs.next()) {
                level = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return level;
    }

    public int getUrlId(String url) {
        int id = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT url_id  FROM mas_application_url WHERE url = '" + url + "' ");
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return id;
    }

    public boolean isUrlAllowed(int deptId, int userRole, int url_id) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS count FROM map_admin_user_role_url WHERE url_id = " + url_id + " AND department_id = " + deptId + " AND role_id = " + userRole);
            rs = ps.executeQuery();
            if (rs.next()) {
                stat = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean isUrlAllowed(SessionPackage.LoginClass loginObj, int url_id) {
        boolean stat = false;
        int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
        String userId = "", userName = "", userDes = "";

        userId = loginObj.getUserId();
        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        officeId = Integer.parseInt(loginObj.getUserOfficeId());

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("");
            rs = ps.executeQuery();
            if (rs.next()) {
                stat = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    //////changes from banti start
    public String getslnoforapicall(String tabname, int officeId) {
        String ans = "";
        String ansmin = "", ansmax = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT slno  FROM beneficiary." + tabname + " WHERE isverify=0::boolean AND office_id = " + officeId + " AND isapicall IS NULL ORDER BY slno";
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        JSONArray jsarr = null;
        try {
            jsarr = getResultJSON(sql, con);
        } catch (Exception ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        ans = jsarr.toString();
        return ans;
    }

    public int getExcelDataUploadBatchId() {
        int batchid = 0;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("select nextval('seq_excel_upload_batch') as batchseq");
            rs = ps.executeQuery();
            if (rs.next()) {
                batchid = rs.getInt("batchseq");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return batchid;
    }

    public String getUserScheme(String userId, String dept_id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String user_scheme = "", user_scheme_fy = "";
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT scheme_codification FROM public.master_scheme  where department_id=? ORDER BY id");
            ps.setInt(1, Integer.parseInt(dept_id));

            rs = ps.executeQuery();
            while (rs.next()) {
                if (user_scheme.trim().length() > 0) {
                    user_scheme = user_scheme + "," + "'" + rs.getString("scheme_codification") + "'";
                } else {
                    user_scheme = "'" + rs.getString("scheme_codification") + "'";

                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return user_scheme;
    }

    public String getUserSchemesData(String scheme, String dept_id, String user_scheme_code, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "";
        if (user_scheme_code.trim().length() > 0) {
            query = "SELECT id||'~'||scheme_codification||'~'||benefit_type||'~'||scheme_name||concat(CASE WHEN NULLIF(component_name ,'') IS NOT NULL \n"
                    + "THEN ' (' || component_name || ') ' ELSE ''END),scheme_name||concat(CASE WHEN NULLIF(component_name ,'') IS NOT NULL \n"
                    + "THEN ' (' || component_name || ') ' ELSE ''END) FROM master_scheme WHERE status = '1' AND scheme_codification in (" + user_scheme_code + ") AND department_id='" + dept_id + "'  Order by id";
        } else {

            query = "SELECT id||'~'||scheme_codification||'~'||benefit_type||'~'||scheme_name||concat(CASE WHEN NULLIF(component_name ,'') IS NOT NULL \n"
                    + "THEN ' (' || component_name || ') ' ELSE ''END),scheme_name||concat(CASE WHEN NULLIF(component_name ,'') IS NOT NULL \n"
                    + "THEN ' (' || component_name || ') ' ELSE ''END) FROM master_scheme WHERE status = '1'  AND department_id='" + dept_id + "'  Order by id";
        }
        return fillSelectedDropdown(query, scheme.split("~")[0], defaultValue);
    }

    public String dataCount(int schemeId, String schemeCode, String userId) throws SQLException, ClassNotFoundException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS uploaded,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data WHERE status>=2 AND entryby='" + userId + "' AND source_code = 1) AS validated,\n"
                //+ " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump WHERE isverify AND isapicall='Y' AND entryby='" + userId + "' ) AS validated,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp WHERE postrcvalidation_rc_member_id IS NOT NULL AND postrcvalidation_rc_member_id <> '' AND status=-1 AND entryby='" + userId + "' AND source_code = 1) AS name_mismatch,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp WHERE (postrcvalidation_rc_member_id IS NULL OR postrcvalidation_rc_member_id = '') AND status=0 AND entryby='" + userId + "' AND source_code = 1) AS missing,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp WHERE postrcvalidation_rc_member_id IS NOT NULL AND postrcvalidation_rc_member_id <> ''AND status=0 AND entryby='" + userId + "' AND source_code = 1) AS pending,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp WHERE status=1 AND entryby='" + userId + "' AND source_code = 1) AS pendingch,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp WHERE status=3 AND entryby='" + userId + "' AND source_code = 1) AS disapproved\n"
                + " FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump a WHERE entryby='" + userId + "'";

        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getInt("uploaded") + "~" + rs.getInt("validated") + "~" + rs.getInt("name_mismatch") + "~" + rs.getInt("missing")
                        + "~" + rs.getInt("disapproved") + "~" + rs.getInt("pending") + "~" + rs.getInt("pendingch");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String dataCount(int schemeId, String schemeCode, int entryOfficeId) throws SQLException, ClassNotFoundException {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS uploaded,\n"
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump WHERE isapicall = 'Y' AND office_id=" + entryOfficeId + ") AS apicalled, "
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data WHERE status>=2 AND office_id=" + entryOfficeId + " AND source_code = 1) AS validated,\n"
                //Name Mismatch                
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp "
                + "  WHERE postrcvalidation_rc_member_id IS NOT NULL AND postrcvalidation_rc_member_id <> '' " //AND postrcvalidation_duplicate_in_rc ='false' AND postrcvalidation_missing_in_rc='false' 
                + "  AND is_duplicate IS NULL AND status=-1 AND office_id=" + entryOfficeId + " AND source_code = 1) AS name_mismatch,\n"
                //RC Not Found 
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp "
                + "  WHERE (postrcvalidation_rc_member_id IS NULL OR postrcvalidation_rc_member_id = '') "
                + "  AND is_duplicate IS NULL AND status = -1 AND office_id=" + entryOfficeId + " AND source_code = 1) AS missing,\n" //postrcvalidation_missing_in_rc='true' AND 
                //Name Mismatch - Kept Pending 
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp "
                + "  WHERE postrcvalidation_rc_member_id IS NOT NULL AND postrcvalidation_rc_member_id <> '' " //AND postrcvalidation_duplicate_in_rc ='false' AND postrcvalidation_missing_in_rc='false' 
                + "  AND is_duplicate IS NULL AND status=0 AND office_id=" + entryOfficeId + " AND source_code = 1) AS pending,\n"
                //Pending at Checker Level  
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp "
                + "  WHERE status=1 AND is_duplicate IS NULL AND office_id=" + entryOfficeId + " AND source_code = 1) AS pendingch,\n"
                //Disapproved by Checker
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp "
                + "  WHERE status=3 AND office_id=" + entryOfficeId + " AND source_code = 1) AS disapproved,\n" //AND is_duplicate IS NULL 
                //Probable Duplicate
                + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp "
                + "  WHERE is_duplicate IS NOT NULL AND office_id=" + entryOfficeId + " AND source_code = 1) AS duplicate\n"
                
                + " FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump a WHERE office_id=" + entryOfficeId + "";

        con = new DBCon.DBSource().connectToBMSDB().getConnection();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {

                result = rs.getInt("uploaded") + "~" + rs.getInt("validated") + "~" + rs.getInt("name_mismatch") + "~" + rs.getInt("missing")
                        + "~" + rs.getInt("disapproved") + "~" + rs.getInt("pending") + "~" + rs.getInt("pendingch") + "~" + rs.getInt("apicalled") + "~" + rs.getInt("duplicate");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int getTmpDataStatus(String userId, String dept_id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int status = 0;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT count(*) as tmpdata FROM legacy_data.tbl_legacy_data_upload_batch where uploaded_by_user=? And dept_id=? AND confirm_discard IS NULL");
            ps.setString(1, userId);
            ps.setInt(2, Integer.parseInt(dept_id));

            rs = ps.executeQuery();
            while (rs.next()) {
                status = rs.getInt("tmpdata");

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return status;
    }

    public String getUserSchemes(String dept_id, String user_scheme_code, String defaultValue) throws java.sql.SQLException, ClassNotFoundException {
        String query = "SELECT id||'~'||scheme_codification||'~'||benefit_type||'~'||scheme_name||concat(CASE WHEN NULLIF(component_name ,'') IS NOT NULL \n"
                + "THEN ' (' || component_name || ') ' ELSE ''END),scheme_name||concat(CASE WHEN NULLIF(component_name ,'') IS NOT NULL \n"
                + "THEN ' (' || component_name || ') ' ELSE ''END) FROM master_scheme WHERE status = '1' AND scheme_codification in(" + user_scheme_code + ") AND department_id='" + dept_id + "' Order by scheme_name";
        return fillDropdown(query, defaultValue);
    }

//    public String getCashSchemes(String defaultValue, int department_id) throws java.sql.SQLException {
//        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme WHERE department_id=" + department_id + " and benefit_type IN (1,5) AND status = '1'";
//        return fillDropdown(query, defaultValue);
//    }
    /////changes from banti end
    public String createToken(String message, String secret) {
        String hash = "";
        try {
            // String secret = secret;
            // String message = message;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));

        } catch (Exception e) {
            System.out.println("Error");
        }
        return hash;

    }

    public String getSchemesData(String defaultValue, int department_id) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification||'~'||scheme_name, scheme_name FROM master_scheme WHERE department_id=" + department_id + "  AND status = '1' ORDER BY id";
        return fillDropdown(query, defaultValue);
    }

    public int getListRecomFile(int listId, int schemeId) {
        int fId = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT recomendation_file_id FROM tbl_recommendation_file_list WHERE list_id = ? AND scheme_id = ? ");
            ps.setInt(1, listId);
            ps.setInt(1, schemeId);
            rs = ps.executeQuery();
            while (rs.next()) {
                fId = rs.getInt("recomendation_file_id");

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return fId;
    }

    public String getSchemeConfigDetails(String scheme_id) {
        String scheme_periodicity = "", scheme_frequency = "", amount_foxed = "", payment_mode = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM scheme_config WHERE scheme_id='" + scheme_id + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                scheme_periodicity = rs.getString("scheme_periodicity");
                scheme_frequency = rs.getString("scheme_yearly_frequency");
                amount_foxed = rs.getString("amount_fixed");
                payment_mode = rs.getString("payment_mode");
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return scheme_periodicity + "~" + scheme_frequency + "~" + amount_foxed + "~" + payment_mode;
    }

    public JSONArray getCitizenApplInfo(String applId) throws Exception {
        String sql = "SELECT * FROM citizen.registration a INNER JOIN citizen.scheme_enrollment b ON b.email = a.email WHERE b.application_id ='" + applId + "'";
        return getResultJSON(sql);
    }

    public JSONArray getCitizenApplInfoQuery(String applId) throws Exception {
        String sql = "SELECT * FROM citizen.registration a INNER JOIN citizen.tbl_raised_queries b ON b.email = a.email WHERE b.application_id ='" + applId + "' "
                + " AND b.responded_by_applicant=FALSE LIMIT 1";
        return getResultJSON(sql);
    }

    public JSONArray getCitizenApplQueryInfo(String applId) throws Exception { //, int officeId, String userId
        String sql = "SELECT action_taken, remarks, DATE(entry_datetime) AS entry_datetime, COALESCE(CAST(DATE(response_datetime) AS VARCHAR(100)),'') AS response_datetime  "
                + "FROM citizen.tbl_raised_queries WHERE application_id ='" + applId + "' ORDER BY id ASC"; //AND entry_office_id = " + officeId + " AND entry_by ='" + userId + "'
        return getResultJSON(sql);
    }

    public JSONArray getBeneficiaryRcmem(String tabname, String applId) throws Exception {
        String sql = "SELECT rc_mem FROM " + tabname + " Where online_application_id ='" + applId + "'";
        return getResultJSON(sql);
    }

    public String getBankName(String bankid) {
        String bank_name = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT name FROM master_bank_name WHERE id = " + Integer.parseInt(bankid));
            rs = ps.executeQuery();
            while (rs.next()) {
                bank_name = rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return bank_name;
    }

    public boolean userManagedBy(String mobile, String uId, String managedBy) {
        int fid = 0;
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "INSERT INTO mas_admin_user_manage(user_id, user_managed_by) VALUES (?, ?)";// RETURNING user_id;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, uId);
            ps.setString(2, managedBy);

            //rs = ps.executeQuery();
            if (ps.executeUpdate() > 0) {
                //fid = rs.getInt(1);
                stat = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public int userGroupMapping(int deptId, int groupId, String userId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null;
        //String query = "INSERT INTO admin_user_role_scheme_mapping(dept_id, role_id, scheme_id, type) VALUES (?, ?, ?, ?)";// RETURNING user_id;
        String query = "INSERT INTO bms_user.map_dept_users_group_users(department_id, group_id, user_id) VALUES (?, ?, ?)";

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, deptId);
            ps.setInt(2, groupId);
            ps.setString(3, userId);

            //  rs = ps.executeQuery();
            if (ps.executeUpdate() > 0) {
//                String query1 = "UPDATE master_scheme\n"
//                        + " SET  map_status = 1 WHERE id = ?";
//                ps1 = con.prepareStatement(query1);
//                int i = 1;
//                ps1.setInt(i++, schemeId);
//                if (ps1.executeUpdate() > 0) {
                result = 1;
            } else {
                result = 0;
            }

            //}
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int officeUserMapping(int deptId, int groupId, int officeId, String userId, int update) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null;
        String query = "";
        if (update == 0) {
            query = "INSERT INTO bms_user.map_dept_users_group_based_office_users(department_id, group_id, office_id, user_id) VALUES (?, ?, ?, ?)";
        } else if (update == 1) {
            query = "UPDATE bms_user.map_dept_users_group_based_office_users\n"
                    + "   SET user_id=? WHERE office_id = ?";
        }

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            if (update == 0) {
                ps.setInt(1, deptId);
                ps.setInt(2, groupId);
                ps.setInt(3, officeId);
                ps.setString(4, userId);
            } else if (update == 1) {
                ps.setString(1, userId);
                ps.setInt(2, officeId);
            }

            //  rs = ps.executeQuery();
            if (ps.executeUpdate() > 0) {
                result = 1;
            } else {
                result = 0;
            }

            //}
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int check_prev_entered_data(int deptId, int groupId, int roleId, int schemeId, int type) {
        int fid = 0;
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null;
        //String query = "INSERT INTO admin_user_role_scheme_mapping(dept_id, role_id, scheme_id, type) VALUES (?, ?, ?, ?)";// RETURNING user_id;
        String query = "Select count(*) as cnt from bms_user.map_dept_users_group_schemes where department_id=? AND group_id=? AND role_id=? AND scheme_id=? AND mapping_type=?";// RETURNING user_id;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, deptId);
            ps.setInt(2, groupId);
            ps.setInt(3, roleId);
            ps.setInt(4, schemeId);
            ps.setInt(5, type);
            rs = ps.executeQuery();
            if (rs.next()) {

                result = rs.getInt("cnt");
            }

            //}
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public int roleSchemeMapping(int deptId, int groupId, int roleId, int schemeId, int type) {
        int fid = 0;
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null;
        //String query = "INSERT INTO admin_user_role_scheme_mapping(dept_id, role_id, scheme_id, type) VALUES (?, ?, ?, ?)";// RETURNING user_id;
        String query = "INSERT INTO bms_user.map_dept_users_group_schemes(department_id, group_id, role_id, scheme_id, mapping_type) VALUES (?, ?, ?, ?, ?)";// RETURNING user_id;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, deptId);
            ps.setInt(2, groupId);
            ps.setInt(3, roleId);
            ps.setInt(4, schemeId);
            ps.setInt(5, type);

            //  rs = ps.executeQuery();
            if (ps.executeUpdate() > 0) {
//                String query1 = "UPDATE master_scheme\n"
//                        + " SET  map_status = 1 WHERE id = ?";
//                ps1 = con.prepareStatement(query1);
//                int i = 1;
//                ps1.setInt(i++, schemeId);
//                if (ps1.executeUpdate() > 0) {
                result = 1;
            } else {
                result = 0;
            }

            //}
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return result;
    }

    public String getOffice(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT office_id, office_name FROM bms_user.mas_dept_users_group_based_office ORDER BY office_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getUserList(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT DISTINCT user_id, emp_name FROM bms_user.mas_dept_users u ORDER BY emp_name ASC";
        //String query = "SELECT user_id, user_name FROM public.mas_admin_user ORDER BY user_name ASC";
        return fillDropdown(query, defaultValue);
    }

    public String getRoleBasedSchemes(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT id||'~'||scheme_codification, scheme_name FROM master_scheme ms INNER JOIN admin_user_role_scheme_mapping rs ON ms.id=rs.scheme_id WHERE status = '1'";
        return fillDropdown(query, defaultValue);
    }

    public boolean adminCheckMobileExists(String mobile) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM mas_dept_user WHERE user_mobile = '" + mobile + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public boolean adminCheckEmailExists(String email) {
        boolean stat = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT COUNT(*) AS nou FROM mas_dept_user WHERE user_email = '" + email + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("nou") > 0) {
                    stat = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return stat;
    }

    public String[][] getMappingUserList(String userId) {

        Connection con = null;
        Statement smt = null;
        ResultSet rset = null;
        String result = "";
        String[][] userList = null;

        int rowCount = 0, row = 0;

        String sql = "SELECT map.sl_no, map.user_id, user_name, dept.department_name, role_name, scheme_name, office_name, map.scheme_id, map.office_id   from admin_user_role_scheme_office_mapping AS map \n"
                + "INNER JOIN mas_dept_user AS u ON map.user_id = u.user_id\n"
                + "INNER JOIN mas_department AS dept ON map.dept_id = dept.department_id\n"
                + "INNER JOIN mas_admin_user_role AS  usrRole ON usrRole.role_id = map.role_id\n"
                + "INNER JOIN master_scheme AS  scheme ON map.scheme_id = scheme.id\n"
                + "INNER JOIN mas_role_based_office AS  off ON map.office_id = off.office_id\n"
                + "WHERE map.user_id = '" + userId + "'";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            smt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rset = smt.executeQuery(sql);
            if (rset.last()) {
                rowCount = rset.getRow();
                rset.beforeFirst();
                userList = new String[rowCount][10];
            }
            while (rset.next()) {
                userList[row][0] = rset.getString("sl_no");
                userList[row][1] = rset.getString("user_id");
                userList[row][2] = rset.getString("user_name");
                userList[row][3] = rset.getString("department_name");
                userList[row][4] = rset.getString("role_name");
                userList[row][5] = rset.getString("scheme_name");
                userList[row][6] = rset.getString("office_name");
                userList[row][7] = rset.getString("scheme_id");
                userList[row][8] = rset.getString("office_id");
                row++;
            }

            int i = 1;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (smt != null) {
                    smt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return userList;
    }

    public String getRoleBasedSchemeList(String defaultValue, int dept_id) throws java.sql.SQLException {
        String query = "SELECT id, scheme_name FROM master_scheme ms "
                //+ " INNER JOIN admin_user_role_scheme_mapping rs ON ms.id=rs.scheme_id\n "
                //+ " WHERE dept_id = "+ dept_id;                
                + " WHERE maker_ben_dept = " + dept_id + " OR checker_ben_dept = " + dept_id + " OR approver_ben_dept = " + dept_id + " "
                + " OR maker_pay_dept = " + dept_id + " OR checker_pay_dept = " + dept_id + " OR approver_pay_dept=" + dept_id;
        return fillDropdown(query, defaultValue);
    }

    public String getDepartmentAbbr(int dept_id) {
        String department_abbr = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "SELECT department_abbr from mas_department where department_id=" + dept_id;
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                department_abbr = rs.getString("department_abbr");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return department_abbr;
    }

    public String getdept_roleid_list(int scheme_id, int dept_id) {
        String child_id = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "SELECT scheme_onboarding.getcheckerrole(" + scheme_id + "," + dept_id + ") as checker_role, scheme_onboarding.getmakerrole(" + scheme_id + "," + dept_id + ") as maker_role,"
                    + "scheme_onboarding.getapproverrole(" + scheme_id + "," + dept_id + ") as approver_role";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                child_id = "'" + rs.getString("approver_role") + "','" + rs.getString("checker_role") + "','" + rs.getString("maker_role") + "'";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return child_id;
    }

    public int getOnboardedSchemeId(int scheme_id) throws java.sql.SQLException {
        int onboarding_id = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT onboarding_id FROM public.master_scheme WHERE master_scheme.id=" + scheme_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                onboarding_id = rs.getInt("onboarding_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return onboarding_id;
    }

    public String getRole(String child_id, String defaultValue) throws java.sql.SQLException {
        String query = "";
        if (!"".equals(child_id)) {
            query = "SELECT role_id, role_name FROM bms_user.mas_user_roles where role_id IN(" + child_id + ")";
        } else {
            query = "SELECT role_id, role_name FROM bms_user.mas_user_roles WHERE role_type = 4";
        }
        return fillDropdown(query, defaultValue);
    }

    public String getRole(String defaultValue) throws java.sql.SQLException {
        String query = "SELECT role_id, role_name FROM bms_user.mas_user_roles WHERE role_type = 4 ";
        return fillDropdown(query, defaultValue);
    }

    public String getOfficeTypeDropdown(String defaultValue) throws java.sql.SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = null;
        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            stmt = con.createStatement();
            query = "SELECT office_type_id, office_type FROM public.master_office_type";
            res = stmt.executeQuery(query);
            options_string += "<option value=\"\">" + defaultValue + "</option>";

            while (res.next()) {

                options_string += "<option value=\"" + res.getString("office_type_id") + "\">" + res.getString("office_type") + "</option>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }

    public String getRunByDropdown(String defaultValue) throws java.sql.SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = null;
        String options_string = "";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            stmt = con.createStatement();
            query = "SELECT run_by_id, run_by FROM public.master_office_governby";
            res = stmt.executeQuery(query);
            options_string += "<option value=\"\">" + defaultValue + "</option>";

            while (res.next()) {

                options_string += "<option value=\"" + res.getString("run_by_id") + "\">" + res.getString("run_by") + "</option>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return options_string;
    }
}
