/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import log.errorlog;
import org.json.JSONArray;
import org.json.JSONObject;
import validation.DataTypeValidator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "saveSchemeBasicInfo", urlPatterns = {"/saveSchemeBasicInfo"})
@MultipartConfig
public class saveSchemeBasicInfo extends HttpServlet {

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
            Scheme_ob_method ob=new Scheme_ob_method();
            validation.DataTypeValidator dtv = new validation.DataTypeValidator();
            errorlog log = new errorlog();
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            int schemeId = 0, status = 0;
            String schemeCode = "", scheme_name = "",component_name="", dept_name = "", scheme_code = "", launch_date = "", launch_fy = "", scheme_type = "", edit_basic_info = "",
                    active_upto = "", scheme_desc = "", verification_last_date = "", approval_last_date = "";
            int dept = 0, scheme_benefit = 0, beneficiary_fy_status = 0, beneficiary_fy_status_apl = 0, beneficiary_fy_status_bpl = 0, data_entry_from_dept = 0, data_entry_from_citizen = 0, data_entry_from = 0;
            String beneficiary_category[] = null;
            String scheme_feature[] = null;
            String other_category[] = null;
            String required_document_poi[] = null;
            String required_document_poa[] = null;
            JSONArray beneficiary_category_json = null;
            JSONObject beneficiary_categoryObj = new JSONObject();
            JSONArray scheme_feature_json = null;
            JSONObject scheme_featureObj = new JSONObject();

            JSONArray other_category_json = null;
            JSONObject other_categoryObj = new JSONObject();

            JSONArray required_document_poa_json = null;
            JSONObject required_documentpoaObj = new JSONObject();
            JSONArray required_document_poi_json = null;
            JSONObject required_documentpoiObj = new JSONObject();
            if (request.getParameter("edit_basic_info") != null && !"".equals(request.getParameter("edit_basic_info"))) {
                boolean edit_basic_info_is_valid = DataTypeValidator.isNumeric(request.getParameter("edit_basic_info"));
                if (edit_basic_info_is_valid) {
                    edit_basic_info = SecurityClass.killchar(request.getParameter("edit_basic_info"));
                } else {
                    out.print("Edit value changed by user.");
                    out.flush();
                    out.close();
                    return;
                }
            }

            if (request.getParameter("dept") != null && !"".equals(request.getParameter("dept"))) {
                boolean dept_is_valid = DataTypeValidator.isNumeric(request.getParameter("dept"));
                if (dept_is_valid) {
                    dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("dept")));
                    dept_name = cm.getDepartmentName(dept);
                } else {
                    out.print("Department id is not valid.");
                    out.flush();
                    out.close();
                    return;
                }

            }

            if (request.getParameter("scheme_name") != null && !"".equals(request.getParameter("scheme_name"))) {
                scheme_name = SecurityClass.killchar(request.getParameter("scheme_name"));
            }
            if (request.getParameter("component_name") != null && !"".equals(request.getParameter("component_name"))) {
                component_name = SecurityClass.killchar(request.getParameter("component_name"));
            }
            if (request.getParameter("scheme_code") != null && !"".equals(request.getParameter("scheme_code"))) {
                scheme_code = SecurityClass.killchar(request.getParameter("scheme_code"));
            }
            if (request.getParameter("scheme_type") != null && !"".equals(request.getParameter("scheme_type"))) {
                boolean scheme_type_is_valid = DataTypeValidator.isNumeric(request.getParameter("scheme_type"));
                if (scheme_type_is_valid) {
                    scheme_type = SecurityClass.killchar(request.getParameter("scheme_type"));
                } else {
                    out.print("Scheme type is not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("scheme_benefit") != null && !"".equals(request.getParameter("scheme_benefit"))) {
                boolean scheme_benefit_is_valid = DataTypeValidator.isNumeric(request.getParameter("scheme_benefit"));
                if (scheme_benefit_is_valid) {
                    scheme_benefit = Integer.parseInt(SecurityClass.killchar(request.getParameter("scheme_benefit")));
                } else {
                    out.print("Scheme benefit value not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("launch_date") != null && !"".equals(request.getParameter("launch_date"))) {

                boolean launch_date_is_valid = DataTypeValidator.isValidDate(request.getParameter("launch_date"), "yyyy-dd-mm");
                if (launch_date_is_valid) {
                    launch_date = SecurityClass.killchar(request.getParameter("launch_date"));
                } else {
                    out.print("Launch date not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("fy") != null && !"".equals(request.getParameter("fy"))) {
                launch_fy = SecurityClass.killchar(request.getParameter("fy"));
            }
            if (request.getParameter("active_upto") != null && !"".equals(request.getParameter("active_upto"))) {
                boolean active_upto_is_valid = DataTypeValidator.isValidDate(request.getParameter("active_upto"), "yyyy-dd-mm");
                if (active_upto_is_valid) {
                    active_upto = SecurityClass.killchar(request.getParameter("active_upto"));
                } else {
                    out.print("Application submission date is not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("verification_last_date") != null && !"".equals(request.getParameter("verification_last_date"))) {
                boolean verification_last_date_is_valid = DataTypeValidator.isValidDate(request.getParameter("verification_last_date"), "yyyy-dd-mm");
                if (verification_last_date_is_valid) {
                    verification_last_date = SecurityClass.killchar(request.getParameter("verification_last_date"));
                } else {
                    out.print("Verification last date not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("approval_last_date") != null && !"".equals(request.getParameter("approval_last_date"))) {
                boolean approval_last_date_is_valid = DataTypeValidator.isValidDate(request.getParameter("approval_last_date"), "yyyy-dd-mm");
                if (approval_last_date_is_valid) {
                    approval_last_date = SecurityClass.killchar(request.getParameter("approval_last_date"));
                } else {
                    out.print("Approval Last date not valid.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("beneficiary_fy_status_apl") != null && !"".equals(request.getParameter("beneficiary_fy_status_apl"))) {
                boolean beneficiary_fy_status_apl_is_valid = DataTypeValidator.isNumeric(request.getParameter("beneficiary_fy_status_apl"));
                if (beneficiary_fy_status_apl_is_valid) {
                    beneficiary_fy_status_apl = Integer.parseInt(SecurityClass.killchar(request.getParameter("beneficiary_fy_status_apl")));
                } else {
                    out.print("Beneficiary FY status APL value changed.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("beneficiary_fy_status_bpl") != null && !"".equals(request.getParameter("beneficiary_fy_status_bpl"))) {
                boolean beneficiary_fy_status_bpl_is_valid = DataTypeValidator.isNumeric(request.getParameter("beneficiary_fy_status_bpl"));
                if (beneficiary_fy_status_bpl_is_valid) {
                    beneficiary_fy_status_bpl = Integer.parseInt(SecurityClass.killchar(request.getParameter("beneficiary_fy_status_bpl")));
                } else {
                    out.print("Beneficiary FY status BPL value changed.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (beneficiary_fy_status_apl > 0 && beneficiary_fy_status_bpl == 0) {
                beneficiary_fy_status = 1;
            } else if (beneficiary_fy_status_apl == 0 && beneficiary_fy_status_bpl > 0) {
                beneficiary_fy_status = 2;
            } else {
                beneficiary_fy_status = 3;
            }

            if (request.getParameter("scheme_desc") != null && !"".equals(request.getParameter("scheme_desc"))) {
                scheme_desc = SecurityClass.killchar(request.getParameter("scheme_desc"));
            }
            if (request.getParameterValues("beneficiary_category") != null && !"".equals(request.getParameter("beneficiary_category"))) {
                beneficiary_category = request.getParameterValues("beneficiary_category");

            }
            if (request.getParameterValues("scheme_features") != null && !"".equals(request.getParameter("scheme_features"))) {
                scheme_feature = request.getParameterValues("scheme_features");

            }
            if (request.getParameterValues("other_category") != null && !"".equals(request.getParameter("other_category"))) {
                other_category = request.getParameterValues("other_category");

            }
            if (request.getParameterValues("required_document_poi") != null && !"".equals(request.getParameter("required_document_poi"))) {
                required_document_poi = request.getParameterValues("required_document_poi");

            }
            if (request.getParameterValues("required_document_poa") != null && !"".equals(request.getParameter("required_document_poa"))) {
                required_document_poa = request.getParameterValues("required_document_poa");

            }
            if (request.getParameter("data_entry_from_dept") != null && !"".equals(request.getParameter("data_entry_from_dept"))) {
                boolean data_entry_from_dept_is_valid = DataTypeValidator.isNumeric(request.getParameter("data_entry_from_dept"));
                if (data_entry_from_dept_is_valid) {
                    data_entry_from_dept = Integer.parseInt(SecurityClass.killchar(request.getParameter("data_entry_from_dept")));
                } else {
                    out.print("Department route value changed.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (request.getParameter("data_entry_from_citizen") != null && !"".equals(request.getParameter("data_entry_from_citizen"))) {
                boolean data_entry_from_citizen_is_valid = DataTypeValidator.isNumeric(request.getParameter("data_entry_from_citizen"));
                if (data_entry_from_citizen_is_valid) {
                    data_entry_from_citizen = Integer.parseInt(SecurityClass.killchar(request.getParameter("data_entry_from_citizen")));
                } else {
                    out.print("Citizen route value changed.");
                    out.flush();
                    out.close();
                    return;
                }
            }
            if (data_entry_from_dept > 0 && data_entry_from_citizen == 0) {
                data_entry_from = 1;
            } else if (data_entry_from_dept == 0 && data_entry_from_citizen > 0) {
                data_entry_from = 2;
            } else {
                data_entry_from = 3;
            }
            CallableStatement cs = null;
            CallableStatement cs1 = null;
            CallableStatement cs2 = null;
            CallableStatement cs3 = null;
            CallableStatement cs4 = null;
            String query_set = "", child_id = "";
            int cnt = 0;
            int count = 1;
            String id = "", uploaded_by = "", name = "", guardians_name = "", dob = "", gender = "", caste = "", pid = "", pan = "", rc_mem = "", bank_name = "", ifsc = "", district_name = "", subdivision_name = "", block_name = "", gp_name = "", last_mile_address = "";
            String bank_account_no = "";
            String amount = "", postrcvalidation_fullname = "", postrcvalidation_duplicate_in_rc = "", postrcvalidation_missing_in_rc = "", postrcvalidation_rc_member_id = "", match_percentage = "";
            int benef_name_matchresult_dist_leven = 0;
            String excel_dist_name = "", excel_subdiv_name = "", excel_block_name = "", uploader_name = "", main_query_set = "", hv = "";
            String mem_id_str_list = "";
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                String check_scheme_is_present = cm.check_scheme_is_present(dept, scheme_code, scheme_name,component_name);
                if (check_scheme_is_present.equals("")) {
                    try {
//                        schemeId=ob.insert_scheme_basic_data(scheme_code,scheme_name,component_name,scheme_type,dept,dept_name,launch_date,
//                                launch_fy,active_upto.equals("") ? null : active_upto,scheme_benefit,beneficiary_fy_status,scheme_desc,data_entry_from,
//                                verification_last_date.equals("") ? null : verification_last_date,approval_last_date.equals("") ? null : approval_last_date);
                        cs = con.prepareCall("{call scheme_onboarding.insert_scheme_basic_data(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                        cs.setString(1, scheme_code);
                        cs.setString(2, scheme_name);
                        cs.setString(3, scheme_type);
                        cs.setInt(4, dept);
                        cs.setString(5, dept_name);
                        cs.setString(6, launch_date);
                        cs.setString(7, launch_fy);
                        cs.setString(8, active_upto.equals("") ? null : active_upto);
                        cs.setInt(9, scheme_benefit);
                        cs.setInt(10, beneficiary_fy_status);
                        cs.setString(11, scheme_desc);
                        cs.setInt(12, data_entry_from);
                        cs.setString(13, verification_last_date.equals("") ? null : verification_last_date);
                        cs.setString(14, approval_last_date.equals("") ? null : approval_last_date);
                        cs.setString(15, component_name);
                        cs.registerOutParameter(16, Types.INTEGER);
                        cs.execute();
                        schemeId = cs.getInt(16);
                        if (schemeId > 0) {
                            if (beneficiary_category != null) {
                                String beneficiary_category_array = "";
                                beneficiary_category_array += "[";
                                for (int k = 0; k < beneficiary_category.length; k++) {
                                    if (k == 0) {
                                        beneficiary_category_array += "{ 'beneficiary_category':" + beneficiary_category[k] + " }";
                                    } else {
                                        beneficiary_category_array += ",{ 'beneficiary_category':" + beneficiary_category[k] + " }";
                                    }
                                }
                                beneficiary_category_array += "]";
                                beneficiary_category_json = new JSONArray(beneficiary_category_array);
                                beneficiary_categoryObj.put("beneficiary_category_json_array", beneficiary_category_json);
                                cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_basic_info_beneficiary_category(?,?,?,?::json)}");
                                cs1.setInt(1, schemeId);
                                cs1.setInt(2, dept);
                                cs1.setString(3, scheme_code);
                                cs1.setString(4, beneficiary_categoryObj.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }

                            }
                            if (scheme_feature != null) {
                                String scheme_feature_array = "";
                                scheme_feature_array += "[";
                                for (int k = 0; k < scheme_feature.length; k++) {
                                    if (k == 0) {
                                        scheme_feature_array += "{ 'scheme_feature':" + scheme_feature[k] + " }";
                                    } else {
                                        scheme_feature_array += ",{ 'scheme_feature':" + scheme_feature[k] + " }";
                                    }
                                }
                                scheme_feature_array += "]";
                                scheme_feature_json = new JSONArray(scheme_feature_array);
                                scheme_featureObj.put("scheme_feature_json_array", scheme_feature_json);
                                cs2 = con.prepareCall("{call  scheme_onboarding.insert_scheme_ob_basic_info_feature(?,?,?,?::json)}");

                                cs2.setInt(1, schemeId);
                                cs2.setInt(2, dept);
                                cs2.setString(3, scheme_code);
                                cs2.setString(4, scheme_featureObj.toString());
                                cs2.execute();

                            }
                            if (other_category != null) {
                                String other_category_array = "";
                                other_category_array += "[";
                                for (int k = 0; k < other_category.length; k++) {
                                    if (k == 0) {
                                        other_category_array += "{ 'other_category':" + other_category[k] + " }";
                                    } else {
                                        other_category_array += ",{ 'other_category':" + other_category[k] + " }";
                                    }
                                }
                                other_category_array += "]";
                                other_category_json = new JSONArray(other_category_array);
                                other_categoryObj.put("other_category_json_array", other_category_json);
                                cs4 = con.prepareCall("{call  scheme_onboarding.insert_scheme_ob_basic_info_other_category(?,?,?,?::json)}");

                                cs4.setInt(1, schemeId);
                                cs4.setInt(2, dept);
                                cs4.setString(3, scheme_code);
                                cs4.setString(4, other_categoryObj.toString());
                                cs4.execute();

                            }

                            if (required_document_poi != null) {
                                String required_document_poi_array = "";
                                required_document_poi_array += "[";
                                for (int k = 0; k < required_document_poi.length; k++) {
                                    if (k == 0) {
                                        required_document_poi_array += "{ 'required_document':" + required_document_poi[k] + " }";
                                    } else {
                                        required_document_poi_array += ",{ 'required_document':" + required_document_poi[k] + " }";
                                    }
                                }
                                required_document_poi_array += "]";
                                required_document_poi_json = new JSONArray(required_document_poi_array);
                                required_documentpoiObj.put("required_document_json_array", required_document_poi_json);
                                cs3 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_bsic_info_required_document_poi(?,?::json)}");
                                cs3.setInt(1, schemeId);
                                cs3.setString(2, required_documentpoiObj.toString());
                                if (cs3.execute() == true) {
                                    status = 1;

                                }
                            }

                            if (required_document_poa != null) {
                                String required_document_poa_array = "";
                                required_document_poa_array += "[";
                                for (int k = 0; k < required_document_poa.length; k++) {
                                    if (k == 0) {
                                        required_document_poa_array += "{ 'required_document':" + required_document_poa[k] + " }";
                                    } else {
                                        required_document_poa_array += ",{ 'required_document':" + required_document_poa[k] + " }";
                                    }
                                }
                                required_document_poa_array += "]";
                                required_document_poa_json = new JSONArray(required_document_poa_array);
                                required_documentpoaObj.put("required_document_json_array", required_document_poa_json);
                                cs3 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_bsic_info_required_document_poa(?,?::json)}");
                                cs3.setInt(1, schemeId);
                                cs3.setString(2, required_documentpoaObj.toString());
                                if (cs3.execute() == true) {
                                    status = 1;

                                }
                            }
                            InputStream isFile = null;
                            String valid_file = "true", ctypePdf = "", fileName = "", user_id = "";
                            long fileSize = 0;
                            int fileFid = 0, doc_id = 0;
                            Part filePart = null;
                            filePart = null;
                            //if(request.getParameter("sanction_letter") !=null){
                            filePart = request.getPart("sanction_letter");
                            //  }
                            if (filePart != null) {
                                ctypePdf = filePart.getContentType();
                                fileSize = filePart.getSize();
                                fileName = filePart.getSubmittedFileName();
                                if (fileSize > 0) {
                                    //if (!(ctypeExcel.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                                    if ((!(ctypePdf.equals("application/pdf")))) {
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("alert('Please Upload PDF File only');");
                                        out.println("location='Schemes/scheme_registration.jsp?data=" + scheme_benefit + "~" + schemeId + "'");
                                        out.println("</script>");
                                        valid_file = "false";
                                        return;
                                    }
                                    if ((fileSize / 1024) > 5120) { // filesize in kb
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("alert('Size of file is more than 5120 Kb, Please use smaller size.')");
                                        out.println("location='Schemes/scheme_registration.jsp?data=" + scheme_benefit + "~" + schemeId + "'");
                                        out.println("</script>");
                                        valid_file = "false";
                                        return;
                                    }

                                    isFile = filePart.getInputStream();

                                }
                            }
                            if ("true".equals(valid_file)) {
                                if (isFile != null) {
                                    fileFid = cm.uploadFile(isFile, user_id, fileName, fileSize, ctypePdf);//cm.uploadExcelFile(isExcel, user_id, fileExcelName, fileExcelSize, ctypeExcel);
                                    if (fileFid > 0) {
                                        String query1 = "INSERT INTO scheme_onboarding.tbl_scheme_related_file_upload_draft(\n"
                                                + "	scheme_id, fid, entry_datetime)\n"
                                                + "	VALUES (?, ?, CURRENT_TIMESTAMP);";

                                        try {
                                            ps1 = con.prepareStatement(query1);
                                            ps1.setInt(1, schemeId);
                                            ps1.setInt(2, fileFid);
                                            ps1.executeUpdate();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                    }
                    //  JSONArray memId_json = new JSONArray(mem_id_str_list);
                    if (status > 0) {
                        out.print(schemeId);
                    }
//            out.print(jsobj);

                    out.flush();
                    out.close();
                } else if (edit_basic_info.equals("1") && !"".equals(check_scheme_is_present)) {
                    try {
                        cs = con.prepareCall("{call scheme_onboarding.update_scheme_basic_data(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                        cs.setString(1, scheme_code);
                        cs.setString(2, scheme_name);
                        cs.setString(3, scheme_type);
                        cs.setInt(4, dept);
                        cs.setString(5, dept_name);
                        cs.setString(6, launch_date);
                        cs.setString(7, launch_fy);
                        cs.setString(8, active_upto.equals("") ? null : active_upto);
                        cs.setInt(9, scheme_benefit);
                        cs.setInt(10, beneficiary_fy_status);
                        cs.setString(11, scheme_desc);
                        cs.setInt(12, data_entry_from);
                        cs.setInt(13, Integer.parseInt(check_scheme_is_present));
                        cs.setString(14, verification_last_date.equals("") ? null : verification_last_date);
                        cs.setString(15, approval_last_date.equals("") ? null : approval_last_date);
                        cs.setString(16, component_name);
                        schemeId = Integer.parseInt(check_scheme_is_present);
                        if (cs.execute()) {
                            if (beneficiary_category != null) {
                                String beneficiary_category_array = "";
                                beneficiary_category_array += "[";
                                for (int k = 0; k < beneficiary_category.length; k++) {
                                    if (k == 0) {
                                        beneficiary_category_array += "{ 'beneficiary_category':" + beneficiary_category[k] + " }";
                                    } else {
                                        beneficiary_category_array += ",{ 'beneficiary_category':" + beneficiary_category[k] + " }";
                                    }
                                }
                                beneficiary_category_array += "]";
                                beneficiary_category_json = new JSONArray(beneficiary_category_array);
                                beneficiary_categoryObj.put("beneficiary_category_json_array", beneficiary_category_json);
                                cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_basic_info_beneficiary_category(?,?,?,?::json)}");
                                cs1.setInt(1, schemeId);
                                cs1.setInt(2, dept);
                                cs1.setString(3, scheme_code);
                                cs1.setString(4, beneficiary_categoryObj.toString());
                                if (cs1.execute() == true) {
                                    status = 1;
                                }

                            }
                            if (scheme_feature != null) {
                                String scheme_feature_array = "";
                                scheme_feature_array += "[";
                                for (int k = 0; k < scheme_feature.length; k++) {
                                    if (k == 0) {
                                        scheme_feature_array += "{ 'scheme_feature':" + scheme_feature[k] + " }";
                                    } else {
                                        scheme_feature_array += ",{ 'scheme_feature':" + scheme_feature[k] + " }";
                                    }
                                }
                                scheme_feature_array += "]";
                                scheme_feature_json = new JSONArray(scheme_feature_array);
                                scheme_featureObj.put("scheme_feature_json_array", scheme_feature_json);
                                cs2 = con.prepareCall("{call  scheme_onboarding.update_scheme_ob_basic_info_feature(?,?,?,?::json)}");

                                cs2.setInt(1, schemeId);
                                cs2.setInt(2, dept);
                                cs2.setString(3, scheme_code);
                                cs2.setString(4, scheme_featureObj.toString());
                                cs2.execute();

                            }
                            if (other_category != null) {
                                String other_category_array = "";
                                other_category_array += "[";
                                for (int k = 0; k < other_category.length; k++) {
                                    if (k == 0) {
                                        other_category_array += "{ 'other_category':" + other_category[k] + " }";
                                    } else {
                                        other_category_array += ",{ 'other_category':" + other_category[k] + " }";
                                    }
                                }
                                other_category_array += "]";
                                other_category_json = new JSONArray(other_category_array);
                                other_categoryObj.put("other_category_json_array", other_category_json);
                                cs4 = con.prepareCall("{call  scheme_onboarding.update_scheme_ob_basic_info_other_category(?,?,?,?::json)}");

                                cs4.setInt(1, schemeId);
                                cs4.setInt(2, dept);
                                cs4.setString(3, scheme_code);
                                cs4.setString(4, other_categoryObj.toString());
                                cs4.execute();

                            }

                            if (required_document_poi != null) {
                                String required_document_poi_array = "";
                                required_document_poi_array += "[";
                                for (int k = 0; k < required_document_poi.length; k++) {
                                    if (k == 0) {
                                        required_document_poi_array += "{ 'required_document':" + required_document_poi[k] + " }";
                                    } else {
                                        required_document_poi_array += ",{ 'required_document':" + required_document_poi[k] + " }";
                                    }
                                }
                                required_document_poi_array += "]";
                                required_document_poi_json = new JSONArray(required_document_poi_array);
                                required_documentpoiObj.put("required_document_json_array", required_document_poi_json);
                                cs3 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_bsic_info_required_document_poi(?,?::json)}");
                                cs3.setInt(1, schemeId);
                                cs3.setString(2, required_documentpoiObj.toString());
                                if (cs3.execute() == true) {
                                    status = 1;

                                }
                            }

                            if (required_document_poa != null) {
                                String required_document_poa_array = "";
                                required_document_poa_array += "[";
                                for (int k = 0; k < required_document_poa.length; k++) {
                                    if (k == 0) {
                                        required_document_poa_array += "{ 'required_document':" + required_document_poa[k] + " }";
                                    } else {
                                        required_document_poa_array += ",{ 'required_document':" + required_document_poa[k] + " }";
                                    }
                                }
                                required_document_poa_array += "]";
                                required_document_poa_json = new JSONArray(required_document_poa_array);
                                required_documentpoaObj.put("required_document_json_array", required_document_poa_json);
                                cs3 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_bsic_info_required_document_poa(?,?::json)}");
                                cs3.setInt(1, schemeId);
                                cs3.setString(2, required_documentpoaObj.toString());
                                if (cs3.execute() == true) {
                                    status = 1;

                                }
                            }
                            InputStream isFile = null;
                            String valid_file = "true", ctypePdf = "", fileName = "", user_id = "";
                            long fileSize = 0;
                            int fileFid = 0, doc_id = 0;
                            Part filePart = null;
                            filePart = null;
                            //if(request.getParameter("sanction_letter") !=null){
                            filePart = request.getPart("sanction_letter");
                            //  }
                            if (filePart != null) {
                                ctypePdf = filePart.getContentType();
                                fileSize = filePart.getSize();
                                fileName = filePart.getSubmittedFileName();
                                if (fileSize > 0) {
                                    //if (!(ctypeExcel.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                                    if ((!(ctypePdf.equals("application/pdf")))) {
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("alert('Please Upload PDF File only');");
                                        out.println("location='Schemes/scheme_registration.jsp?data=" + scheme_benefit + "~" + schemeId + "'");
                                        out.println("</script>");
                                        valid_file = "false";
                                        return;
                                    }
                                    if ((fileSize / 1024) > 5120) { // filesize in kb
                                        out.println("<script type=\"text/javascript\">");
                                        out.println("alert('Size of file is more than 5120 Kb, Please use smaller size.')");
                                        out.println("location='Schemes/scheme_registration.jsp?data=" + scheme_benefit + "~" + schemeId + "'");
                                        out.println("</script>");
                                        valid_file = "false";
                                        return;
                                    }

                                    isFile = filePart.getInputStream();

                                }
                            }
                            if ("true".equals(valid_file)) {
                                if (isFile != null) {
                                    fileFid = cm.uploadFile(isFile, user_id, fileName, fileSize, ctypePdf);//cm.uploadExcelFile(isExcel, user_id, fileExcelName, fileExcelSize, ctypeExcel);
                                    if (fileFid > 0) {
                                        String query1 = "UPDATE scheme_onboarding.tbl_scheme_related_file_upload_draft SET fid=" + fileFid + " entry_datetime=CURRENT_TIMESTAMP where scheme_id=" + schemeId;

                                        try {
                                            ps1 = con.prepareStatement(query1);
                                            ps1.executeUpdate();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                    }
                    //  JSONArray memId_json = new JSONArray(mem_id_str_list);
                    if (status > 0) {
                        out.print(schemeId);
                    }

//            out.print(jsobj);
                    out.flush();
                    out.close();
                } else {
                    // out.print("exist~" + check_scheme_is_present);
                    out.print("Secheme Already Exist");
                    out.flush();
                    out.close();
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
