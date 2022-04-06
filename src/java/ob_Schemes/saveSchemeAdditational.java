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
import java.sql.Types;
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

/**
 *
 * @author Administrator
 */
@WebServlet(name = "saveSchemeAdditational", urlPatterns = {"/saveSchemeAdditational"})
public class saveSchemeAdditational extends HttpServlet {

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
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            int edit_additational_info = 0, schemeId = 0, status = 0, tot_textbox = 0, tot_dropdown = 0, tot_datefield = 0, data_entry_from_dept = 0, data_entry_from_citizen = 0, data_entry_from = 0;
            String amount_fixed = "", scheme_periodicity = "", scheme_yearly_frequency = "", payment_mode = "", scheme_available = "",
                    from_date = "", to_date = "";
            String scheme_id = "", benefit_type = "", scheme_publish = "", scheme_specific_info = "";
            String pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "",
                    ac_holder_name = "", debit_tran_limit = "";

            String prefilled_list[] = null;
            String required_document_scheme[] = null;
            JSONArray required_document_scheme_json = null;
            JSONObject required_documentschemeObj = new JSONObject();
            JSONArray scheme_commodity_json = null;
            JSONObject scheme_commodityObj = new JSONObject();
            String category = "";
            int beneficiary_financial_status = 0, beneficiary_category = 0;

            JSONArray prefilled_list_json = null;
            JSONObject prefilled_listObj = new JSONObject();

            JSONArray textbox_array_json = null;
            JSONObject textbox_Obj = new JSONObject();
            JSONArray datefield_array_json = null;
            JSONObject datefield_Obj = new JSONObject();

            JSONArray dropdown_array_json = null;
            JSONObject dropdown_Obj = new JSONObject();
            CallableStatement cs = null;
            CallableStatement cs1 = null;
            CallableStatement cs2 = null;
            CallableStatement cs3 = null;
            CallableStatement cs4 = null;
            CallableStatement cs5 = null;
            CallableStatement cs6 = null;

            if (request.getParameter("edit_additational_info") != null && !"".equals(request.getParameter("edit_additational_info"))) {
                edit_additational_info = Integer.parseInt(request.getParameter("edit_additational_info").trim());
            }
            if (request.getParameter("additational_scheme_id") != null && !"".equals(request.getParameter("additational_scheme_id"))) {
                scheme_id = request.getParameter("additational_scheme_id").trim();
            }
            if (request.getParameter("benefit_type") != null && !"".equals(request.getParameter("benefit_type"))) {
                benefit_type = request.getParameter("benefit_type");
            }

            if (request.getParameterValues("scheme_specific_info") != null && !"".equals(request.getParameter("scheme_specific_info"))) {
                scheme_specific_info = request.getParameter("scheme_specific_info");

            }

            if (request.getParameterValues("tot_textbox") != null && !"".equals(request.getParameter("tot_textbox"))) {
                tot_textbox = Integer.parseInt(request.getParameter("tot_textbox"));

            }
            if (request.getParameterValues("tot_datefield") != null && !"".equals(request.getParameter("tot_datefield"))) {
                tot_datefield = Integer.parseInt(request.getParameter("tot_datefield"));

            }
            if (request.getParameterValues("tot_dropdown") != null && !"".equals(request.getParameter("tot_dropdown"))) {
                tot_dropdown = Integer.parseInt(request.getParameter("tot_dropdown"));

            }
            if (request.getParameterValues("prefilled_list") != null && !"".equals(request.getParameter("prefilled_list"))) {
                prefilled_list = request.getParameterValues("prefilled_list");

            }
            if (request.getParameterValues("required_document_scheme") != null && !"".equals(request.getParameter("required_document_scheme"))) {
                required_document_scheme = request.getParameterValues("required_document_scheme");

            }
            errorlog log = new errorlog();
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                // String scheme_code = cm.getSchemeCode(Integer.parseInt(scheme_id));
                if (edit_additational_info == 0) {
                    try {

                        if (tot_textbox != 0) {

                            for (int i = 1; i <= tot_textbox; i++) {
                                JSONArray textbox_array = new JSONArray();
                                JSONArray textbox_mainobj = new JSONArray();
                                String textbox_priority = request.getParameter("textbox_priority" + i);
                                String textbox_label = request.getParameter("textbox_lebel" + i);
                                String maximum_length = request.getParameter("textbox_length" + i);
                                String required = request.getParameter("textbox_valid" + i) == null ? "" : request.getParameter("textbox_valid" + i);
                                String is_numeric = request.getParameter("textbox_numericvalid" + i) == null ? "" : request.getParameter("textbox_numericvalid" + i);
                                JSONObject textboxlablobj = new JSONObject();
                                JSONArray maintextbox = new JSONArray();
                                textboxlablobj.put("label", textbox_label);
                                textboxlablobj.put("required", required);
                                textboxlablobj.put("num_valid", is_numeric);
                                textboxlablobj.put("maximum_length", maximum_length);
                                textboxlablobj.put("priority", textbox_priority);
                                maintextbox.put(textboxlablobj);

                                //textbox_mainobj.put(maintextbox);
                                //  textbox_array.put(maintextbox);
                                textbox_Obj.put("textbox_json_array", maintextbox.toString());
                                if (!"".equals(textbox_label) && !"".equals(maximum_length) && textbox_label != null && maximum_length != null) {
                                    int x = 0;
                                    cs4 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_additional_info(?,?,?::json,?)}");
                                    cs4.setInt(++x, Integer.parseInt(scheme_id));
                                    cs4.setString(++x, "textbox");
                                    cs4.setString(++x, textbox_Obj.toString());
                                    cs4.setInt(++x, Integer.parseInt(textbox_priority));
                                    if (cs4.execute()) {
                                        status = 1;
                                    }
                                }
                            }

                        }
                        if (tot_datefield != 0) {

                            for (int i = 1; i <= tot_datefield; i++) {
                                JSONArray datefield_array = new JSONArray();
                                JSONArray datefield_mainobj = new JSONArray();
                                String datefield_priority = request.getParameter("datefield_priority" + i);
                                String datefield_label = request.getParameter("datefield_lebel" + i);
                                String required = request.getParameter("datefield_valid" + i) == null ? "" : request.getParameter("datefield_valid" + i);
                                JSONObject datefieldlablobj = new JSONObject();
                                JSONArray maintextbox = new JSONArray();
                                datefieldlablobj.put("label", datefield_label);
                                datefieldlablobj.put("required", required);
                                datefieldlablobj.put("priority", datefield_priority);
                                maintextbox.put(datefieldlablobj);

                                //textbox_mainobj.put(maintextbox);
                                //  textbox_array.put(maintextbox);
                                datefield_Obj.put("datefield_json_array", maintextbox.toString());
                                if (!"".equals(datefield_label) && datefield_label != null) {
                                    int x = 0;
                                    cs6 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_additional_info(?,?,?::json,?)}");
                                    cs6.setInt(++x, Integer.parseInt(scheme_id));
                                    cs6.setString(++x, "datefield");
                                    cs6.setString(++x, datefield_Obj.toString());
                                    cs6.setInt(++x, Integer.parseInt(datefield_priority));
                                    if (cs6.execute()) {
                                        status = 1;
                                    }
                                }
                            }

                        }
                        if (tot_dropdown != 0) {
                            //  JSONArray option = new JSONArray();

                            for (int l = 1; l <= tot_dropdown; l++) {
                                JSONArray option = new JSONArray();
                                String dropdown_priority = request.getParameter("dropdown_priority" + l);
                                String dropdown_field_label = request.getParameter("dropdown_field_lebel" + l);
                                String option_name[] = request.getParameterValues("option_name" + l);
                                String option_value[] = request.getParameterValues("option_value" + l);

                                JSONObject lablobj = new JSONObject();
                                lablobj.put("label", dropdown_field_label);
                                lablobj.put("priority", dropdown_priority);
                                lablobj.put("required", request.getParameter("dropdown_valid" + l) == null ? "" : request.getParameter("dropdown_valid" + l));

                                JSONArray mainoption = new JSONArray();
                                try {
                                    for (int option_name_count = 0; option_name_count < option_name.length; option_name_count++) {
                                        JSONObject optionobj = new JSONObject();
                                        optionobj.put("option_name" + option_name_count, option_name[option_name_count]);
                                        optionobj.put("option_value" + option_name_count, option_value[option_name_count]);
                                        mainoption.put(optionobj);

                                    }
                                } catch (Exception e) {

                                }
                                lablobj.put("option_counter", mainoption);
                                option.put(lablobj);
                                System.out.print(option.toString());
                                dropdown_Obj.put("dropdown_json_array", option);
                                if (!"".equals(dropdown_field_label) && option_name.length > 0 && option_value.length > 0) {
                                    int x = 0;
                                    cs5 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_additional_info(?,?,?::json,?)}");
                                    cs5.setInt(++x, Integer.parseInt(scheme_id));
                                    cs5.setString(++x, "Dropdown");
                                    cs5.setString(++x, dropdown_Obj.toString());
                                    cs5.setInt(++x, Integer.parseInt(dropdown_priority));
                                    if (cs5.execute()) {
                                        status = 1;
                                    }
                                }
                            }

                        }
                        if (prefilled_list != null) {
                            JSONArray option = new JSONArray();
                            for (int k = 0; k < prefilled_list.length; k++) {
                                String dropdown_field_label = cm.getPrefilledTblLabel(Integer.parseInt(prefilled_list[k].split("~")[0]));
                                String option_name[] = cm.getPrefilledTblOptionName(prefilled_list[k].split("~")[1]);
                                String option_value[] = cm.getPrefilledTblOptionValue(prefilled_list[k].split("~")[1]);
                                String[] master_priority = request.getParameter("master_priority").split(",");
                                String priority = master_priority[k + 1];

                                JSONObject lablobj = new JSONObject();
                                lablobj.put("label", dropdown_field_label);
                                lablobj.put("required", 1);
                                lablobj.put("priority", priority);

                                JSONArray mainoption = new JSONArray();
                                try {
                                    for (int option_name_count = 0; option_name_count < option_name.length; option_name_count++) {
                                        JSONObject optionobj = new JSONObject();
                                        optionobj.put("option_name" + option_name_count, option_name[option_name_count]);
                                        optionobj.put("option_value" + option_name_count, option_value[option_name_count]);
                                        mainoption.put(optionobj);

                                    }
                                } catch (Exception e) {

                                }
                                lablobj.put("option_counter", mainoption);
                                option.put(lablobj);
                                System.out.print(option.toString());
                                dropdown_Obj.put("dropdown_json_array", option);
                                if (!"".equals(dropdown_field_label) && option_name.length > 0 && option_value.length > 0) {
                                    int x = 0;
                                    cs3 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_additional_info_prefilled_data(?,?,?,?::json,?)}");
                                    cs3.setInt(++x, Integer.parseInt(scheme_id));
                                    cs3.setString(++x, "prefilled");
                                    cs3.setString(++x, prefilled_list[k].split("~")[0]);
                                    cs3.setString(++x, dropdown_Obj.toString());
                                    cs3.setInt(++x, Integer.parseInt(priority));
                                    if (cs3.execute()) {
                                        status = 1;
                                    }
                                }
                            }

//                        prefilled_list_array += "]";
//                        prefilled_list_json = new JSONArray(prefilled_list_array);
//                        prefilled_listObj.put("prefilled_list_json_array", prefilled_list_json);
//                        cs3 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_additional_info_prefilled_list(?,?::json)}");
//                        cs3.setInt(1, schemeId);
//                        cs3.setString(2, prefilled_listObj.toString());
//                        if (cs3.execute() == true) {
//                            status = 1;
//
//                        }
                        }
                        if (required_document_scheme != null) {
                            String required_document_poa_array = "";
                            required_document_poa_array += "[";
                            for (int k = 0; k < required_document_scheme.length; k++) {
                                if (k == 0) {
                                    required_document_poa_array += "{ 'required_document':" + required_document_scheme[k] + " }";
                                } else {
                                    required_document_poa_array += ",{ 'required_document':" + required_document_scheme[k] + " }";
                                }
                            }
                            required_document_poa_array += "]";
                            required_document_scheme_json = new JSONArray(required_document_poa_array);
                            required_documentschemeObj.put("required_document_json_array", required_document_scheme_json);
                            cs3 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_bsic_info_required_document_scheme(?,?::json)}");
                            cs3.setInt(1, Integer.parseInt(scheme_id));
                            cs3.setString(2, required_documentschemeObj.toString());
                            if (cs3.execute() == true) {
                                status = 1;

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
                    try {

                        if (tot_textbox != 0) {
                            int delete_prev_data = cm.DeletePrevdata(Integer.parseInt(scheme_id), "textbox");
                            String[] textbox_id = request.getParameterValues("textbox_id");
                            for (int i = 0; i < tot_textbox; i++) {
                                JSONArray textbox_array = new JSONArray();
                                JSONArray textbox_mainobj = new JSONArray();
                                String textbox_label = "", maximum_length = "", required = "", is_numeric = "", priority = "";
                                if ((textbox_id != null) && (i < textbox_id.length)) {
                                    priority = request.getParameter("textbox_priority" + textbox_id[i]);
                                    textbox_label = request.getParameter("textbox_lebel" + textbox_id[i]);
                                    maximum_length = request.getParameter("textbox_length" + textbox_id[i]);
                                    required = request.getParameter("textbox_valid" + textbox_id[i]) == null ? "" : request.getParameter("textbox_valid" + textbox_id[i]);
                                    is_numeric = request.getParameter("textbox_numericvalid" + textbox_id[i]) == null ? "" : request.getParameter("textbox_numericvalid" + textbox_id[i]);

                                } else {
                                    //  i=1;
                                    for (int k = 1; k <= tot_textbox - i; k++) {
                                        priority = request.getParameter("textbox_priority" + k);
                                        textbox_label = request.getParameter("textbox_lebel" + k);
                                        maximum_length = request.getParameter("textbox_length" + k);
                                        required = request.getParameter("textbox_valid" + k) == null ? "" : request.getParameter("textbox_valid" + k);
                                        is_numeric = request.getParameter("textbox_numericvalid" + k) == null ? "" : request.getParameter("textbox_numericvalid" + k);
                                    }
                                }
                                JSONObject textboxlablobj = new JSONObject();
                                JSONArray maintextbox = new JSONArray();
                                textboxlablobj.put("label", textbox_label);
                                textboxlablobj.put("required", required);
                                textboxlablobj.put("num_valid", is_numeric);
                                textboxlablobj.put("maximum_length", maximum_length);
                                textboxlablobj.put("priority", priority);
                                maintextbox.put(textboxlablobj);

                                //textbox_mainobj.put(maintextbox);
                                //  textbox_array.put(maintextbox);
                                textbox_Obj.put("textbox_json_array", maintextbox.toString());
                                if (!"".equals(textbox_label) && !"".equals(maximum_length) && textbox_label != null && maximum_length != null) {
                                    int x = 0;
                                    cs4 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_additional_info(?,?,?::json,?)}");
                                    cs4.setInt(++x, Integer.parseInt(scheme_id));
                                    cs4.setString(++x, "textbox");
                                    cs4.setString(++x, textbox_Obj.toString());
                                    cs4.setInt(++x, Integer.parseInt(priority));
                                    if (cs4.execute()) {
                                        status = 1;
                                    }
                                }
                            }

                        }
                        if (tot_datefield != 0) {
                            int delete_prev_data = cm.DeletePrevdata(Integer.parseInt(scheme_id), "datefield");
                            String[] datefield_id = request.getParameterValues("datefield_id");
                            for (int i = 0; i < tot_datefield; i++) {
                                JSONArray datefield_array = new JSONArray();
                                JSONArray datefield_mainobj = new JSONArray();
                                String datefield_label = "", required = "", priority = "";
                                if ((datefield_id != null) && (i < datefield_id.length)) {
                                    datefield_label = request.getParameter("datefield_lebel" + datefield_id[i]);
                                    priority = request.getParameter("datefield_priority" + datefield_id[i]);
                                    required = request.getParameter("datefield_valid" + datefield_id[i]) == null ? "" : request.getParameter("datefield_valid" + datefield_id[i]);
                                } else {
                                    //  i=1;
                                    for (int k = 1; k <= tot_datefield - i; k++) {
                                        datefield_label = request.getParameter("datefield_lebel" + k);
                                        priority = request.getParameter("datefield_priority" + k);
                                        required = request.getParameter("datefield_valid" + k) == null ? "" : request.getParameter("datefield_valid" + k);
                                    }
                                }
                                JSONObject datefieldlablobj = new JSONObject();
                                JSONArray maindatefield = new JSONArray();
                                datefieldlablobj.put("label", datefield_label);
                                datefieldlablobj.put("required", required);
                                datefieldlablobj.put("priority", priority);
                                maindatefield.put(datefieldlablobj);

                                //textbox_mainobj.put(maintextbox);
                                //  textbox_array.put(maintextbox);
                                datefield_Obj.put("datefield_json_array", maindatefield.toString());
                                if (!"".equals(datefield_label) && datefield_label != null) {
                                    int x = 0;
                                    cs6 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_additional_info(?,?,?::json,?)}");
                                    cs6.setInt(++x, Integer.parseInt(scheme_id));
                                    cs6.setString(++x, "datefield");
                                    cs6.setString(++x, datefield_Obj.toString());
                                    cs6.setInt(++x, Integer.parseInt(priority));
                                    if (cs6.execute()) {
                                        status = 1;
                                    }
                                }
                            }

                        }
                        if (tot_dropdown != 0) {
                            //  JSONArray option = new JSONArray();
                            int delete_prev_data = cm.DeletePrevdata(Integer.parseInt(scheme_id), "Dropdown");
                            String[] dropdown_id = request.getParameterValues("dropdown_id");
                            for (int l = 0; l < tot_dropdown; l++) {
                                JSONArray option = new JSONArray();
                                JSONArray mainoption = new JSONArray();
                                String dropdown_field_label = "", dropdown_valid = "", priority = "";
                                String option_name[] = null;
                                String option_value[] = null;
                                if (dropdown_id != null && (l < dropdown_id.length)) {
                                    dropdown_field_label = request.getParameter("dropdown_field_lebel" + dropdown_id[l]);
                                    if (dropdown_field_label != null) {
                                        priority = request.getParameter("dropdown_priority" + dropdown_id[l]);
                                        option_name = request.getParameterValues("option_name" + dropdown_id[l]);
                                        option_value = request.getParameterValues("option_value" + dropdown_id[l]);
                                        dropdown_valid = request.getParameter("dropdown_valid" + dropdown_id[l]) == null ? "" : request.getParameter("dropdown_valid" + dropdown_id[l]);
                                    }
                                    try {
                                        if (dropdown_field_label != null) {
                                            for (int option_name_count = 0; option_name_count < option_name.length; option_name_count++) {
                                                JSONObject optionobj = new JSONObject();
                                                optionobj.put("option_name" + option_name_count, option_name[option_name_count]);
                                                optionobj.put("option_value" + option_name_count, option_value[option_name_count]);
                                                mainoption.put(optionobj);
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                } else {
                                    // l=1;
                                    for (int k = 1; k <= tot_dropdown - l; k++) {
                                        dropdown_field_label = request.getParameter("dropdown_field_lebel" + k);
                                        if (dropdown_field_label != null) {
                                            priority = request.getParameter("dropdown_priority" + k);
                                            option_name = request.getParameterValues("option_name" + k);
                                            option_value = request.getParameterValues("option_value" + k);
                                            dropdown_valid = request.getParameter("dropdown_valid" + k) == null ? "" : request.getParameter("dropdown_valid" + k);
                                        }
                                    }
                                    try {
                                        if (dropdown_field_label != null) {
                                            for (int option_name_count = 0; option_name_count < option_name.length; option_name_count++) {
                                                JSONObject optionobj = new JSONObject();
                                                optionobj.put("option_name" + option_name_count, option_name[option_name_count]);
                                                optionobj.put("option_value" + option_name_count, option_value[option_name_count]);
                                                mainoption.put(optionobj);
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                }

                                JSONObject lablobj = new JSONObject();
                                lablobj.put("label", dropdown_field_label);
                                lablobj.put("required", dropdown_valid);
                                lablobj.put("option_counter", mainoption);
                                lablobj.put("priority", priority);
                                option.put(lablobj);
                                System.out.print(option.toString());
                                dropdown_Obj.put("dropdown_json_array", option);
                                if (!"".equals(dropdown_field_label) && option_name.length > 0 && option_value.length > 0 && dropdown_field_label != null) {
                                    int x = 0;
                                    cs5 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_additional_info(?,?,?::json,?)}");
                                    cs5.setInt(++x, Integer.parseInt(scheme_id));
                                    cs5.setString(++x, "Dropdown");
                                    cs5.setString(++x, dropdown_Obj.toString());
                                    cs5.setInt(++x, Integer.parseInt(priority));
                                    if (cs5.execute()) {
                                        status = 1;
                                        mainoption = new JSONArray();
                                        option = new JSONArray();
                                    }
                                }
                            }
                        }
                        if (prefilled_list != null) {
                            int delete_prev_data = cm.DeletePrevdata(Integer.parseInt(scheme_id), "prefilled");

                            for (int k = 0; k < prefilled_list.length; k++) {
                                String dropdown_field_label = cm.getPrefilledTblLabel(Integer.parseInt(prefilled_list[k].split("~")[0]));
                                String option_name[] = cm.getPrefilledTblOptionName(prefilled_list[k].split("~")[1]);
                                String option_value[] = cm.getPrefilledTblOptionValue(prefilled_list[k].split("~")[1]);
                                String[] master_priority = request.getParameter("master_priority").split(",");
                                String priority = master_priority[k + 1];
                                JSONArray option = new JSONArray();
                                JSONObject lablobj = new JSONObject();
                                lablobj.put("label", dropdown_field_label);
                                lablobj.put("required", 1);
                                lablobj.put("priority", priority);

                                JSONArray mainoption = new JSONArray();
                                try {
                                    for (int option_name_count = 0; option_name_count < option_name.length; option_name_count++) {
                                        JSONObject optionobj = new JSONObject();
                                        optionobj.put("option_name" + option_name_count, option_name[option_name_count]);
                                        optionobj.put("option_value" + option_name_count, option_value[option_name_count]);
                                        mainoption.put(optionobj);

                                    }
                                } catch (Exception e) {

                                }
                                lablobj.put("option_counter", mainoption);
                                option.put(lablobj);
                                System.out.print(option.toString());
                                dropdown_Obj.put("dropdown_json_array", option);
                                if (!"".equals(dropdown_field_label) && option_name.length > 0 && option_value.length > 0) {
                                    int x = 0;
                                    cs3 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_additional_info_prefilled_data(?,?,?,?::json,?)}");
                                    cs3.setInt(++x, Integer.parseInt(scheme_id));
                                    cs3.setString(++x, "prefilled");
                                    cs3.setString(++x, prefilled_list[k].split("~")[0]);
                                    cs3.setString(++x, dropdown_Obj.toString());
                                    cs3.setInt(++x, Integer.parseInt(priority));
                                    if (cs3.execute()) {
                                        status = 1;
                                    }
                                }

                            }
                        }
                        if (required_document_scheme != null) {
                            String required_document_poa_array = "";
                            required_document_poa_array += "[";
                            for (int k = 0; k < required_document_scheme.length; k++) {
                                if (k == 0) {
                                    required_document_poa_array += "{ 'required_document':" + required_document_scheme[k] + " }";
                                } else {
                                    required_document_poa_array += ",{ 'required_document':" + required_document_scheme[k] + " }";
                                }
                            }
                            required_document_poa_array += "]";
                            required_document_scheme_json = new JSONArray(required_document_poa_array);
                            required_documentschemeObj.put("required_document_json_array", required_document_scheme_json);
                            cs3 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_bsic_info_required_document_scheme(?,?::json)}");
                            cs3.setInt(1, Integer.parseInt(scheme_id));
                            cs3.setString(2, required_documentschemeObj.toString());
                            if (cs3.execute() == true) {
                                status = 1;

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
            Logger.getLogger(saveSchemeAdditational.class.getName()).log(Level.SEVERE, null, ex);
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
