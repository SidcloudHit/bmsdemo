/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
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
import log.errorlog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "SchemeSpecificInfoHtml", urlPatterns = {"/SchemeSpecificInfoHtml"})
public class SchemeSpecificInfoHtml extends HttpServlet {

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
            String result = "";
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query_set = "", scheme_code = "", doc_id = "", type = "", uemail = "";
            int scheme_id = 0;
            if (request.getParameter("scheme_id") != null && request.getParameter("scheme_id") != "") {
                scheme_id = Integer.parseInt(request.getParameter("scheme_id"));
            }
//            if (request.getParameter("scheme_code") != null && request.getParameter("scheme_code") != "") {
//                scheme_code = request.getParameter("scheme_code");
//            }
            errorlog log = new errorlog();
            String tabNamedata = "scheme_onboarding.scheme_ob_additional_info_draft";

            try {
                int cnt = 0;
                int count = 1;
                int option_counter_count = 0;
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                String query = "SELECT * from " + tabNamedata + " WHERE reference_id = '" + scheme_id + "' order by priority,field_id";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                //  result +=" <h4>Scheme Specific Information</h4>";

                result += " <div class=\"form-row\">";
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
                                String valid = "", is_number = "";
                                if (required.equals("1")) {
                                    valid = "required";
                                }

                                result += " <div class=\"form-group col-md-6 " + valid + "\">";
                                result += "<label for=\"scheme_code\" class=\"control-label\">" + label + " :</label>";
                                result += "<input type=\"hidden\" name=textboxlabel_" + id + "  value='" + label + "' />";
                                if (num_valid.equals("2")) {
                                    is_number = "number";
                                    result += "<input type=\"text\" " + valid + " name=textbox_" + id + "  maxlength=" + maximum_length + " class=\"form-control\" onkeypress=\"return IsNumeric(event);\" />";

                                } else if (num_valid.equals("3")) {
                                    is_number = "decimal";
                                    result += "<input type=\"text\" " + valid + " name=textbox_" + id + "  maxlength=" + maximum_length + " class=\"form-control\" onkeypress=\"return IsDecimal(event);\" />";
                                }
                                else{
                                    result += "<input type=\"text\" " + valid + " name=textbox_" + id + "  maxlength=" + maximum_length + " class=\"form-control\"/>";
                                }
                                result += "</div>";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
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
                                String valid = "";
                                if (required.equals("1")) {
                                    valid = "required";
                                }
                                result += " <div class=\"form-group col-md-6 " + valid + "\">";
                                result += "<label for=\"scheme_code\" class=\"control-label\">" + label + " :</label>";
                                result += "<input type=\"hidden\" name=datefieldlabel_" + id + "  value='" + label + "' />";
                                result += "<input type=\"date\" name=datefield_" + id + "  class=\"form-control\" />";
                                result += "</div>";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                        }
                    } else {
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
                                JSONArray option_counter = new JSONArray(tmpOBJ.optString("option_counter"));
                                String valid = "";
                                if (required.equals("1")) {
                                    valid = "required";
                                }
                                result += " <div class=\"form-group col-md-6 " + valid + "\">";
                                result += "<label for=\"scheme_code\" class=\"control-label\">" + label + " :</label>";
                                result += "<input type=\"hidden\" name=dropdownlabel_" + id + "  value='" + label + "' />";
                                result += "<select name=dropdown_" + id + "  " + valid + " class=\"custom-select mr-sm-2\" >";
                                result += "<option value=''>Please Select</option>";
                                for (int k = 0; k < option_counter.length(); k++) {

                                    JSONObject option_counterobj = new JSONObject(option_counter.getJSONObject(k).toString());
                                    result += "<option value=" + option_counterobj.optString("option_value" + k) + ">" + option_counterobj.optString("option_name" + k) + "</option>";
                                }
                                result += "</select></div>";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
                        }
                    }
//result +="</div>";
//                    result += "<div class=\"row\" id=" + sl_no + ">";
//
//                    result += "<div class=\"col-md-4 mt-2\">" + document_name + "</div>";
//
//                    result += "<div class=\"col-md-4 mt-2\"> <input type=\"file\" class=\"form-input\" id=" + doc_id + "_file name=" + doc_id + "_file value=\"\" /></div>";
//                   

                    count++;
                }
                result += "</div>";
//                if (cnt < 1) {
//                    result += "<div class=\"row\"><div class=\"col-md-12\">No Data Found</div></div>";
//                }
//            result += "</tbody></table>";
//            result +="<button type=\"submit\" name=\"submit\" value=\"1\" class=\"btn btn-primary\" />Save</button>&nbsp;<a href=\"mappingDetails.jsp\" class=\"btn btn-primary\">View All Existing Target</a>";
            } catch (SQLException ex) {
                Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
                int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
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
            //  return result;
            out.print(result);
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
