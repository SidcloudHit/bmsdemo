/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import log.errorlog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "SchemePreviewDetails", urlPatterns = {"/SchemePreviewDetails"})
public class SchemePreviewDetails extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        CallableStatement cs = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray jsarr = null;
        JSONObject jsobj = new JSONObject();

        String scheme_id = "", scheme_details = "";
        if (request.getParameter("scheme_id") != null && !"".equals(request.getParameter("scheme_id"))) {
            scheme_id = SecurityClass.killchar(request.getParameter("scheme_id"));
        }
        Common.CommonMethod cm = new Common.CommonMethod();
        errorlog log = new errorlog();
        //  ddlbank = cm.getBank("-Please Select-");
//            try {
//                cs = con.prepareCall("{call getSchemePreviewData(?,?)}");
//                cs.setInt(1, Integer.parseInt(scheme_id));
//                cs.registerOutParameter(2, Types.VARCHAR);
//                cs.execute();
//                scheme_details = cs.getString(2);
//                jsarr = new JSONArray(scheme_details);
//                jsobj.put("aaData", jsarr);
//            } catch (NumberFormatException | SQLException | JSONException e) {
//
//            }

        String department_name = "", scheme_code = "", scheme_name = "", beneficiary_financial_status = "", description = "", beneficiary_category = "", scheme_data_source = "",
                scheme_features = "", scheme_periodicity = "", scheme_frequency = "", amount_fixed = "", amount = "", payment_mode = "", required_doc_poi = "", required_doc_poa = "", required_doc_scheme = "",
                commodity_name = "", subsidy_amt = "", effective_from = "", commodity_matrix = "", launch_date = "", launch_fy = "", active_upto = "", scheme_benefit_type = "";

        String payment_name = "", pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "", ac_holder_name = "", debit_tran_limit = "";
        String bsp_maker_dept = "", bsp_checker_dept = "", bsp_approver_dept = "", pp_maker_dept = "", pp_checker_dept = "", pp_approver_dept = "";
        if (!"".equals(scheme_id)) {

            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                String query = "Select date(a.probable_launch_date) as launch_date,a.launch_fy,date(a.active_upto) as active_upto,\n"
                        + "a.scheme_codification,a.department_name,a.scheme_name,a.beneficiary_financial_status,a.description,\n"
                        + "a.benefit_type,a.scheme_data_source,b.type_name,c.* from scheme_onboarding.scheme_ob_basic_info_draft as a \n"
                        + "INNER JOIN master_benefit_type as b ON b.id=a.benefit_type \n"
                        + "LEFT JOIN scheme_onboarding.scheme_ob_payment_info_pfms_details_draft as c ON c.scheme_id=a.sl_no where a.sl_no=" + scheme_id;
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                if (rs.next()) {
                    department_name = rs.getString("department_name");
                    scheme_code = rs.getString("scheme_codification");
                    scheme_name = rs.getString("scheme_name");
                    beneficiary_financial_status = rs.getString("beneficiary_financial_status");
                    scheme_data_source = rs.getString("scheme_data_source");
                    if (rs.getString("description") != null) {
                        description = rs.getString("description");
                    }
                    beneficiary_category = cm.getSchemeWisebeneficiaryCategoryName(scheme_id);
                    scheme_features = cm.getSchemeFeatures(scheme_id);
                    scheme_benefit_type = rs.getString("type_name");
                    launch_date = rs.getString("launch_date");
                    launch_fy = rs.getString("launch_fy");
                    if (rs.getString("active_upto") != null) {
                        active_upto = rs.getString("active_upto");
                    }

                    String scheme_config_details = cm.getSchemePaymentInfo(scheme_id);
                    scheme_periodicity = scheme_config_details.split("~")[0];
                    scheme_frequency = scheme_config_details.split("~")[1];
                    amount_fixed = scheme_config_details.split("~")[2];
                    payment_mode = scheme_config_details.split("~")[3];
                    if (amount_fixed.equals("Y")) {
                        amount = cm.getFixedAmount(scheme_id);
                    }
                    switch (payment_mode) {
                        case "1":
                            payment_name = "CTOS(Treasury)";
                            break;
                        case "2":
                            payment_name = "PFMS";
                            pfms_scheme_code = rs.getString("pfms_scheme_code");
                            agency_name = rs.getString("agency_name");
                            agency_code = rs.getString("agency_code");
                            bank_name = rs.getString("bank_name");
                            bank_ac_no = rs.getString("bank_ac_no");
                            ifsc_code = rs.getString("ifsc_code");
                            ac_holder_name = rs.getString("ac_holder_name");
                            debit_tran_limit = rs.getString("debit_tran_limit");
                            break;
                        case "3":
                            payment_name = "NACH";
                            break;
                        default:
                            payment_name = "e-RUPI";
                            break;
                    }
                    commodity_matrix = cm.getSchemeCommodityList(scheme_id);
                    required_doc_poi = cm.getrequireddocListpoi(scheme_id);
                    required_doc_poa = cm.getrequireddocListpoa(scheme_id);
                    required_doc_scheme = cm.getrequireddocListscheme(scheme_id);

                    bsp_maker_dept = cm.getSchemeMakerBspInfo(scheme_id);
                    bsp_checker_dept = cm.getSchemeCheckerBspInfo(scheme_id);
                    bsp_approver_dept = cm.getSchemeApproverBspInfo(scheme_id);
//                    bsp_maker_dept = bsp_info.split("~")[0];
//                    bsp_checker_dept = bsp_info.split("~")[1];
//                    bsp_approver_dept = bsp_info.split("~")[2];

                    String pp_info = cm.getSchemePPInfo(scheme_id);
                    pp_maker_dept = pp_info.split("~")[0];
                    pp_checker_dept = pp_info.split("~")[1];
                    pp_approver_dept = pp_info.split("~")[2];
                }
            } catch (Exception e) {
                e.printStackTrace();
                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
            }
        }
        scheme_details += "<button class=\"btn btn-default\" onclick=\"printDiv('printableArea')\"><i class=\"fa fa-print\" aria-hidden=\"true\" style=\"    font-size: 17px;\"> Print</i></button>";

        scheme_details += "<h3>Basic Information</h3><table class='table table-sm table-striped table-bordered small' style = 'width:100%'>";
        scheme_details += "<tr> <td>Scheme Name</td><td>" + scheme_name + "</td></tr><tr><td>Scheme Code</td><td>" + scheme_code + "</td></tr><tr>";
        scheme_details += "<td>Launch Date</td><td>" + launch_date + "</td></tr><tr><td>Launch Financial Year</td><td>" + launch_fy + "</td></tr>";
        scheme_details += "<tr><td>Active Upto</td><td>" + active_upto + "</td></tr><tr><td>Benefit Type</td><td>" + scheme_benefit_type + "</td></tr><tr>";
        scheme_details += "<td>Beneficiary Category</td><td>" + beneficiary_category + "</td></tr><tr><td>Beneficiary Financial Status</td><td>";
        switch (beneficiary_financial_status) {
            case "1":
                scheme_details += "APL";
                break;
            case "2":
                scheme_details += "BPL";
                break;
            default:
                scheme_details += "APL and BPL Both";
                break;
        }
        scheme_details += "</td></tr><tr><td>Scheme Data Source</td><td>";
        switch (scheme_data_source) {
            case "1":
                scheme_details += "Department";
                break;
            case "2":
                scheme_details += "Citizen";
                break;
            default:
                scheme_details += "Department and Citizen Both";
                break;
        }
        scheme_details += "</td></tr><tr><td>Scheme Description</td><td>" + description + "</td></tr><tr><td>Scheme Feature</td><td>" + scheme_features + "</td></tr>";
        scheme_details += "<tr> <td> Proof of Identity Documents</td > <td> " + required_doc_poi + " </td> </tr> <tr> <td> Proof of Address Documents</td > <td> " + required_doc_poa + " </td> </tr>";
        scheme_details += "<tr> <td> Scheme Specific Documents</td > <td> " + required_doc_scheme + " </td> </tr></table>";

        scheme_details += "<h3>Payment Information</h3><table class='table table-sm table-striped table-bordered small' style='width:100%'><tr><td>Scheme Periodicity</td><td>";
        switch (scheme_periodicity) {
            case "1":
                scheme_details += "Yearly";
                break;
            case "2":
                scheme_details += "Half yearly";
                break;
            case "3":
                scheme_details += "Triannually";
                break;
            case "4":
                scheme_details += "Quarterly";
                break;
            case "6":
                scheme_details += "Bimonthly";
                break;
            case "12":
                scheme_details += "Monthly";
                break;
            case "26":
                scheme_details += "Biweekly";
                break;
            default:
                scheme_details += "Weekly";
                break;
        }
        scheme_details += "</td></tr><tr><td>Scheme Yearly Frequency</td> <td>";
        switch (scheme_frequency) {
            case "1":
                scheme_details += "Once in a Year";
                break;
            case "2":
                scheme_details += "Once in every 2 year";
                break;
            case "3":
                scheme_details += "Once in every 3 Year";
                break;
            case "100":
                scheme_details += "Once in a lifetime";
                break;
            case "200":
                scheme_details += "As and when required";
                break;
            default:
                scheme_details += "Not Applicable";
                break;
        }
        scheme_details += "</td> </tr> ";
        if (!amount.equals("")) {
            scheme_details += " <tr> <td> Amount </td> <td> " + amount + "</td> </tr>";
        }
        if (!commodity_matrix.equals("")) {
            scheme_details += " <tr> <td> Commodity List</td> <td>" + commodity_matrix + "</td> </tr> ";
        }
        scheme_details += "<tr> <td> Payment Mode</td > <td> " + payment_name + " </td> </tr> ";
        if (payment_mode.equals("2")) {
            scheme_details += "<tr> <td> PFMS Scheme Code</td > <td> " + pfms_scheme_code + " </td> </tr> <tr> <td> Agency Name</td > <td> " + agency_name + " </td> </tr>";
            scheme_details += "<tr> <td> Agency Code</td> <td> " + agency_code + " </td> </tr> <tr> <td> Bank Name</td> <td> " + bank_name + " </td> </tr>";
            scheme_details += " <tr> <td> Bank Account Number</td> <td> " + bank_ac_no + " </td> </tr> <tr> <td> IFSC Code</td> <td> " + ifsc_code + " </td> </tr>";
            scheme_details += " <tr> <td> Account Holder's Name</td> <td>" + ac_holder_name + "</td></tr><tr> <td> Maximum Amount of Debit Transaction </td > <td> " + debit_tran_limit + " </td> </tr>";
        }
        scheme_details += "</table><h3>Stake Holder Department</h3><table class='table table-sm table-striped table-bordered small' style='width:100%'><tr><td colspan='2'>Beneficiary Selection Process</td></tr>";
        scheme_details += "<tr><td>Maker Department</td><td> " + bsp_maker_dept + " </td> </tr> ";
        scheme_details += "<tr><td> Checker Department</td > <td> " + bsp_checker_dept + " </td> </tr> ";
        scheme_details += "<tr><td> Approver Department</td > <td> " + bsp_approver_dept + " </td> </tr> </table>";
//          scheme_details += "<table class='table table-sm table-striped table-bordered small' style='width:100%'><tr><td colspan='2'>Payment Process</td></tr>";
//        scheme_details += "<tr><td>Maker Department</td><td> " + pp_maker_dept + " </td> </tr> ";
//        scheme_details += "<tr><td> Checker Department</td > <td> " +pp_checker_dept + " </td> </tr> ";
//        scheme_details += "<tr><td> Approver Department</td > <td> " + pp_approver_dept + " </td> </tr> </table>";
        scheme_details += "<h4>Citizen Portal Form Preview</h4 >";
        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold'>Name :</label>";
        scheme_details += "<input type='text' required class='form-control' name='name' id='name' value='' /></div>";

        scheme_details += "<div class='form-group col-md-6 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >Father/Guardian's Name :</label>";
        scheme_details += "<input type='text' required class='form-control' name='guardian_name' id='guardian_name' value='' /></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'>";
        scheme_details += "<label for='scheme' class='control-label font-weight-bold'>Mother's Name :</label>";
        scheme_details += "<input type='text' required  class='form-control' id='mother_name' name='mother_name' value='' /></div>";
        scheme_details += "<div class='form-group col-md-6 required'>";
        scheme_details += "<label for='dept' class='control-label font-weight-bold' >Gender :(M/F)</label>";
        scheme_details += "<input type='text' required class='form-control' id='gender' name='gender' maxlength='1' value='' /></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold'>Social Category :</label>";
        scheme_details += "<select name='social_category' required='' class='form-control' id='social_category'> ";
        scheme_details += "<option value=''>Please Select</option><option value='SC'>SC</option><option value='ST'>ST</option><option value='OBC'>OBC</option><option value='Others'>Others</option></select></div>";

        scheme_details += "<div class='form-group col-md-6 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >Date of Birth :</label>";
        scheme_details += "<input type='date' required class='form-control ' name='dob' value='<%=dob%>' id='dob' /></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold'>District :</label>";
        scheme_details += "<input type='text' required class='form-control' name='dist_name' value='' id='dist_name' /></div>";

        scheme_details += "<div class='form-group col-md-6 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >Sub-Division :</label>";
        scheme_details += "<input type='text' class='form-control' name='subdiv_name' id='subdiv_name' value='' /></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-4 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold'>Block :</label>";
        scheme_details += "<input type='text' required class='form-control' name='block_name' id='block_name' value='' /></div>";

        scheme_details += "<div class='form-group col-md-4 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >GP/Ward/VC :</label>";
        scheme_details += "<input type='text' required class='form-control' name='ward_name' id='ward_name' value='' /></div>";

        scheme_details += "<div class='form-group col-md-4 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >PIN Code :</label>";
        scheme_details += "<input type='text' required class='form-control' name='pincode' id='pincode' value='' /></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold'>Bank Account Number :</label>";
        scheme_details += "<input type='text' required class='form-control' name='bank_ac_no' id='bank_ac_no' value='' /></div>";

        scheme_details += "<div class='form-group col-md-6 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >Name as per Bank Passbook :</label>";
        scheme_details += "<input type='text' required class='form-control' name='name_as_per_passbook' id='name_as_per_passbook' value='' /></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold'>Bank Name :</label>";
        scheme_details += "<select name='bank_name' required='' id='bank_name' class='custom-select'><%=ddlbank%></select></div>";

        scheme_details += "<div class='form-group col-md-6 required'>";
        scheme_details += "<label for='dept' class='control-label  font-weight-bold' >IFSC Code :</label>";
        scheme_details += "<input type='text' required class='form-control' id='ifsc_code' name='ifsc_code' value='' /></div></div>";

        scheme_details += "<div class='form-group col-md-4 required'>";
        scheme_details += "<label for='scheme' class='control-label  font-weight-bold' data-toggle='tooltip' data-placement='top' title='Only image file Allowed'>Photograph :</label>";
        scheme_details += "<input type='file' required class='form-control' name='personal_photo' value='' id='personal_photo' accept='image/jpeg,image/png' data-toggle='tooltip' data-placement='top' title='Only image file Allowed'/>";
        scheme_details += "</div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'><div id='documnet_poi' class='document'></div><span id='lblError' style='color: red'></span></div>";

        scheme_details += "<div class='form-group col-md-6 required'><div id='documnet_poa' class='document'></div><span id='lblError' style='color: red'></span></div></div>";

        scheme_details += "<div class='form-row'><div class='form-group col-md-6 required'><div id='documnet_scheme' class='document'></div><span id='lblError' style='color: red'></span></div></div>";

        scheme_details += "<div id = 'title' style = 'display:none;' >";
        scheme_details += "<h4>Additional Information</h4 > </div> <div id = 'specific_info'> </div> </div>";
        out.print(scheme_details);
        out.flush();
        out.close();
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
