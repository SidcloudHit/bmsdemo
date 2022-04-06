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

/**
 *
 * @author Administrator
 */
@WebServlet(name = "saveStackHolderDeptData", urlPatterns = {"/saveStackHolderDeptData"})
public class saveStakeHolderDeptData extends HttpServlet {

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
            int status = 0, tot_textbox = 0, tot_dropdown = 0, data_entry_from_dept = 0, data_entry_from_citizen = 0, data_entry_from = 0;
            String amount_fixed = "", scheme_periodicity = "", scheme_yearly_frequency = "", payment_mode = "", scheme_available = "",
                    from_date = "", to_date = "";
            String scheme_id = "", benefit_type = "", scheme_publish = "", scheme_specific_info = "";
            String pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "",
                    ac_holder_name = "", debit_tran_limit = "";

            String bsp_creation_dept_id[] = null;
            String bsp_verification_dept_id[] = null;
//            String dept_id[] = null;
            JSONArray scheme_commodity_json = null;
            JSONObject scheme_commodityObj = new JSONObject();
            String category = "";
            int beneficiary_financial_status = 0, beneficiary_category = 0;

            JSONArray scheme_bsp_json = null;
            JSONObject scheme_bspObj = new JSONObject();

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
            int dept_id = 0, edit_stake_holder_dept_info = 0, pp_creation_dept_id = 0, pp_verification_dept_id = 0;
            int bsp_creation_jurisdiction_level = 0, bsp_verification_jurisdiction_level = 0, bsp_approval_jurisdiction_level = 0, pp_creation_jurisdiction_level = 0, pp_verification_jurisdiction_level = 0, pp_approval_jurisdiction_level = 0;
            String bsp_creation_dept_name = "", bsp_verification_dept_name = "", pp_creation_dept_id_name = "", pp_verification_dept_name = "", dept_name = "";
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                if (loginObj.getUserRole() != null && loginObj.getUserRole() != "") {
                    roleId = Integer.parseInt(loginObj.getUserRole());
                }
                if (loginObj.getUserOfficeId() != null && loginObj.getUserOfficeId() != "") {
                    officeId = Integer.parseInt(loginObj.getUserOfficeId());
                }

            }
            if (request.getParameter("edit_stake_holder_dept_info") != null && !"".equals(request.getParameter("edit_stake_holder_dept_info"))) {
                edit_stake_holder_dept_info = Integer.parseInt(request.getParameter("edit_stake_holder_dept_info").trim());
            }
            if (request.getParameter("stake_schemeid") != null && !"".equals(request.getParameter("stake_schemeid"))) {
                scheme_id = request.getParameter("stake_schemeid").trim();
            }
            if (request.getParameter("dept_id") != null && !"".equals(request.getParameter("dept_id"))) {
                dept_id = Integer.parseInt(SecurityClass.killchar(request.getParameter("dept_id")));
                dept_name = cm.getDepartmentName(dept_id);
            }
//            if (request.getParameter("bsp_creation_dept") != null && !"".equals(request.getParameter("bsp_creation_dept"))) {
//                bsp_creation_dept_id = Integer.parseInt(request.getParameter("bsp_creation_dept"));
//                bsp_creation_dept_name = cm.getDepartmentName(bsp_creation_dept_id);
//
//            }
//            if (request.getParameter("bsp_verification_dept") != null && !"".equals(request.getParameter("bsp_verification_dept"))) {
//                bsp_verification_dept_id = Integer.parseInt(request.getParameter("bsp_verification_dept"));
//                bsp_verification_dept_name = cm.getDepartmentName(bsp_verification_dept_id);
//
//            }

//            if (request.getParameterValues("bsp_approval_dept") != null && !"".equals(request.getParameterValues("bsp_approval_dept"))) {
//                dept_id = request.getParameterValues("bsp_approval_dept");
//            }
            if (request.getParameterValues("bsp_creation_dept") != null && !"".equals(request.getParameterValues("bsp_creation_dept"))) {
                bsp_creation_dept_id = request.getParameterValues("bsp_creation_dept");
            }

            if (request.getParameterValues("bsp_verification_dept") != null && !"".equals(request.getParameter("bsp_verification_dept"))) {
                bsp_verification_dept_id = request.getParameterValues("bsp_verification_dept");

            }
//          
//
//            if (request.getParameter("pp_creation_dept") != null && !"".equals(request.getParameter("pp_creation_dept"))) {
//                pp_creation_dept_id = Integer.parseInt(request.getParameter("pp_creation_dept"));
//                pp_creation_dept_id_name = cm.getDepartmentName(pp_creation_dept_id);
//
//            }
//            if (request.getParameter("pp_verification_dept") != null && !"".equals(request.getParameter("pp_verification_dept"))) {
//                pp_verification_dept_id = Integer.parseInt(request.getParameter("pp_verification_dept"));
//                pp_verification_dept_name = cm.getDepartmentName(pp_verification_dept_id);
//
//            }
//            
//             if (request.getParameter("bsp_creation_jurisdiction_level") != null && !"".equals(request.getParameter("bsp_creation_jurisdiction_level"))) {
//                bsp_creation_jurisdiction_level = Integer.parseInt(request.getParameter("bsp_creation_jurisdiction_level"));
//
//            }
//             if (request.getParameter("bsp_verification_jurisdiction_level") != null && !"".equals(request.getParameter("bsp_verification_jurisdiction_level"))) {
//                bsp_verification_jurisdiction_level = Integer.parseInt(request.getParameter("bsp_verification_jurisdiction_level"));
//
//            }
//             if (request.getParameter("bsp_approval_jurisdiction_level") != null && !"".equals(request.getParameter("bsp_approval_jurisdiction_level"))) {
//                bsp_approval_jurisdiction_level = Integer.parseInt(request.getParameter("bsp_approval_jurisdiction_level"));
//
//            }

//            if (request.getParameter("pp_creation_jurisdiction_level") != null && !"".equals(request.getParameter("pp_creation_jurisdiction_level"))) {
//                pp_creation_jurisdiction_level = Integer.parseInt(request.getParameter("pp_creation_jurisdiction_level"));
//
//            }
//            if (request.getParameter("pp_verification_jurisdiction_level") != null && !"".equals(request.getParameter("pp_verification_jurisdiction_level"))) {
//                pp_verification_jurisdiction_level = Integer.parseInt(request.getParameter("pp_verification_jurisdiction_level"));
//
//            }
//            if (request.getParameter("pp_approval_jurisdiction_level") != null && !"".equals(request.getParameter("pp_approval_jurisdiction_level"))) {
//                pp_approval_jurisdiction_level = Integer.parseInt(request.getParameter("pp_approval_jurisdiction_level"));
//
//            }
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();

                if (edit_stake_holder_dept_info == 0) {
                    if (bsp_creation_dept_id != null) {
                        String scheme_bsp_array = "";
                        scheme_bsp_array += "[";
                        for (int k = 0; k < bsp_creation_dept_id.length; k++) {
                            bsp_creation_dept_name = cm.getDepartmentName(Integer.parseInt(bsp_creation_dept_id[k]));
                            bsp_verification_dept_name = cm.getDepartmentName(Integer.parseInt(bsp_verification_dept_id[k]));
                            // dept_name = cm.getDepartmentName(Integer.parseInt(dept_id[k]));
                            if (k == 0) {
                                scheme_bsp_array += "{ 'maker_dept_id':" + bsp_creation_dept_id[k] + " ,'maker_dept_name':" + bsp_creation_dept_name + " , 'checker_dept_id':" + bsp_verification_dept_id[k] + ",'checker_dept_name':" + bsp_verification_dept_name + "  , 'approver_dept_id':" + dept_id + ", 'approver_dept_name':" + dept_name + " }";
                            } else {
                                scheme_bsp_array += ", { 'maker_dept_id':" + bsp_creation_dept_id[k] + " ,'maker_dept_name':" + bsp_creation_dept_name + " , 'checker_dept_id':" + bsp_verification_dept_id[k] + ",'checker_dept_name':" + bsp_verification_dept_name + "  , 'approver_dept_id':" + dept_id + ", 'approver_dept_name':" + dept_name + " }";
                            }
                        }
                        scheme_bsp_array += "]";

                        try {

                            scheme_bsp_json = new JSONArray(scheme_bsp_array);
                            scheme_bspObj.put("scheme_bsp_json_array", scheme_bsp_json);

                            cs1 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_sh_dept_bsp_data(?,?,?::json)}");
                            cs1.setInt(1, Integer.parseInt(scheme_id));
                            cs1.setString(2, userId);
                            cs1.setString(3, scheme_bspObj.toString());
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

//            try {
//                con = new DBCon.DBSource().connectToBMSDB().getConnection();
//                cs = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_sh_dept_bsp_data(?,?,?,?,?,?,?,?,?,?,?)}");
//                cs.setInt(1, Integer.parseInt(scheme_id));
//                cs.setInt(2, bsp_creation_dept_id);
//                cs.setString(3, bsp_creation_dept_name);
//                cs.setInt(4, bsp_creation_jurisdiction_level);
//                cs.setInt(5, bsp_verification_dept_id);
//                cs.setString(6, bsp_verification_dept_name);
//                cs.setInt(7, bsp_verification_jurisdiction_level);
//                cs.setInt(8, dept_id);
//                cs.setString(9, dept_name);
//                cs.setInt(10, bsp_approval_jurisdiction_level);
//                cs.setString(11, userId);
//                
//                cs.execute();
//
//                cs2 = con.prepareCall("{call scheme_onboarding.insert_scheme_ob_sh_dept_pp_data(?,?,?,?,?,?,?,?,?,?,?)}");
//                cs2.setInt(1, Integer.parseInt(scheme_id));
//                cs2.setInt(2, pp_creation_dept_id);
//                cs2.setString(3, pp_creation_dept_id_name);
//                cs2.setInt(4, pp_creation_jurisdiction_level);
//                cs2.setInt(5, pp_verification_dept_id);
//                cs2.setString(6, pp_verification_dept_name);
//                cs2.setInt(7, pp_verification_jurisdiction_level);
//                cs2.setInt(8, dept_id);
//                cs2.setString(9, dept_name);
//                cs2.setInt(10, pp_approval_jurisdiction_level);
//                cs2.setString(11, userId);
//                if (cs2.execute()) {
//                    status = 1;
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (con != null) {
//                    try {
//                        con.close();
//                    } catch (SQLException ex) {
//                    }
//                    con = null;
//                }
//                if (ps != null) {
//                    try {
//                        ps.close();
//                    } catch (SQLException ex) {
//                    }
//                }
//                if (ps1 != null) {
//                    try {
//                        ps1.close();
//                    } catch (SQLException ex) {
//                    }
//                }
//            }
//                    if (status > 0) {
//                        out.print(scheme_id);
//                    }
                } else {

                    if (bsp_creation_dept_id != null) {
                        String scheme_bsp_array = "";
                        scheme_bsp_array += "[";
                        for (int k = 0; k < bsp_creation_dept_id.length; k++) {
                            bsp_creation_dept_name = cm.getDepartmentName(Integer.parseInt(bsp_creation_dept_id[k]));
                            bsp_verification_dept_name = cm.getDepartmentName(Integer.parseInt(bsp_verification_dept_id[k]));
                            // dept_name = cm.getDepartmentName(Integer.parseInt(dept_id[k]));
                            if (k == 0) {
                                scheme_bsp_array += "{ 'maker_dept_id':" + bsp_creation_dept_id[k] + " ,'maker_dept_name':" + bsp_creation_dept_name + " , 'checker_dept_id':" + bsp_verification_dept_id[k] + ",'checker_dept_name':" + bsp_verification_dept_name + "  , 'approver_dept_id':" + dept_id + ", 'approver_dept_name':" + dept_name + " }";
                            } else {
                                scheme_bsp_array += ", { 'maker_dept_id':" + bsp_creation_dept_id[k] + " ,'maker_dept_name':" + bsp_creation_dept_name + " , 'checker_dept_id':" + bsp_verification_dept_id[k] + ",'checker_dept_name':" + bsp_verification_dept_name + "  , 'approver_dept_id':" + dept_id + ", 'approver_dept_name':" + dept_name + " }";
                            }
                        }
                        scheme_bsp_array += "]";

                        try {
                            scheme_bsp_json = new JSONArray(scheme_bsp_array);
                            scheme_bspObj.put("scheme_bsp_json_array", scheme_bsp_json);

                            cs1 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_sh_dept_bsp_data(?,?,?::json)}");
                            cs1.setInt(1, Integer.parseInt(scheme_id));
                            cs1.setString(2, userId);
                            cs1.setString(3, scheme_bspObj.toString());
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
//                try {
//                con = new DBCon.DBSource().connectToBMSDB().getConnection();
//                cs = con.prepareCall("{call scheme_onboarding.update_scheme_ob_sh_dept_bsp_data(?,?,?,?,?,?,?,?,?,?,?)}");
//                cs.setInt(1, Integer.parseInt(scheme_id));
//                cs.setInt(2, bsp_creation_dept_id);
//                cs.setString(3, bsp_creation_dept_id_name);
//                cs.setInt(4, bsp_creation_jurisdiction_level);
//                cs.setInt(5, bsp_verification_dept_id);
//                cs.setString(6, bsp_verification_dept_name);
//                cs.setInt(7, bsp_verification_jurisdiction_level);
//                cs.setInt(8, dept_id);
//                cs.setString(9, dept_name);
//                cs.setInt(10, bsp_approval_jurisdiction_level);
//                cs.setString(11, userId);
//                
//                cs.execute();
//
//                cs2 = con.prepareCall("{call scheme_onboarding.update_scheme_ob_sh_dept_pp_data(?,?,?,?,?,?,?,?,?,?,?)}");
//                cs2.setInt(1, Integer.parseInt(scheme_id));
//                cs2.setInt(2, pp_creation_dept_id);
//                cs2.setString(3, pp_creation_dept_id_name);
//                cs2.setInt(4, pp_creation_jurisdiction_level);
//                cs2.setInt(5, pp_verification_dept_id);
//                cs2.setString(6, pp_verification_dept_name);
//                cs2.setInt(7, pp_verification_jurisdiction_level);
//                cs2.setInt(8, dept_id);
//                cs2.setString(9, dept_name);
//                cs2.setInt(10, pp_approval_jurisdiction_level);
//                cs2.setString(11, userId);
//                if (cs2.execute()) {
//                    status = 1;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (con != null) {
//                    try {
//                        con.close();
//                    } catch (SQLException ex) {
//                    }
//                    con = null;
//                }
//                if (ps != null) {
//                    try {
//                        ps.close();
//                    } catch (SQLException ex) {
//                    }
//                }
//                if (ps1 != null) {
//                    try {
//                        ps1.close();
//                    } catch (SQLException ex) {
//                    }
//                }
//            }
                if (status > 0) {
                    out.print(scheme_id);
                }
            } catch (Exception e) {
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
