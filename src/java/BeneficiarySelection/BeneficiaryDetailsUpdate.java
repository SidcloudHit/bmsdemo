/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import Common.AjaxServlet;
import Common.CommonMethod;
import Security.SecurityClass;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static org.bouncycastle.asn1.cms.CMSObjectIdentifiers.data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "BeneficiaryDetailsUpdate", urlPatterns = {"/BeneficiaryDetailsUpdate"})
public class BeneficiaryDetailsUpdate extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            //int edit = 0, editable = 0, user_role = 0, user_office = 0, district = 0;
            String message = "", bankName = "", bankAccount = "", bankAbbr = "", ifscCode = "", rcMemId = "", schemeCode = "", logRemarks = "";
            int updatePurposeId = 0, status = 0;
            CommonMethod cm = new CommonMethod();
//            edit = Integer.parseInt(request.getParameter("hdnEdit"));
//            int prev_edit = Integer.parseInt(request.getParameter("hdnPrevEdit"));

            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0, groupId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
                schemeId = Integer.parseInt(loginObj.getUserScheme());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);

                return;
            }

            if (request.getParameter("updateAccInfo") != null) {
                updatePurposeId = 1;
                status = 2;
                //if (request.getParameter("btnupdateAccInfo") != null) {
                if (request.getParameter("rCmem") != null) {

                    rcMemId = request.getParameter("rCmem");
                }

                if (request.getParameter("bankName") != null) {

                    if (request.getParameter("bankName").trim().length() > 0) {
                        bankName = request.getParameter("bankName");
                        bankName = SecurityClass.killchar(bankName);

                        bankAbbr = request.getParameter("bankAbbr");
                        bankAbbr = SecurityClass.killchar(bankAbbr);

                    } else {
                        message = "Bank Name can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }
                } else {
                    message = "Bank Name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    return;
                }

                if (request.getParameter("banKacc") != null) {
                    if (request.getParameter("banKacc").trim().length() > 0) {
                        bankAccount = request.getParameter("banKacc");
                        bankAccount = SecurityClass.killchar(bankAccount);
                    } else {
                        message = "Account No can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }
                } else {
                    message = "Account No can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    return;
                }

                if (request.getParameter("ifsCcode") != null) {
                    if (request.getParameter("ifsCcode").trim().length() > 0) {
                        ifscCode = request.getParameter("ifsCcode");
                        ifscCode = SecurityClass.killchar(ifscCode);

                    } else {
                        message = "IFSC can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }
                } else {
                    message = "IFSC can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    return;
                }

                if (request.getParameter("remarks") != null) {
                    if (request.getParameter("remarks").trim().length() > 0) {
                        logRemarks = request.getParameter("remarks");
                        logRemarks = SecurityClass.killchar(logRemarks);
                    } else {
                        message = "Remarks can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }
                } else {
                    message = "Remarks can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    return;
                }

                schemeCode = cm.getSchemeCode(schemeId);
                Connection con = null;
                CallableStatement cs = null;
                PreparedStatement ps = null;

                ResultSet rs = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    con.setAutoCommit(false);

                    createBeneficiaryLog(schemeId, schemeCode, rcMemId, updatePurposeId, logRemarks, userId, userName, con);

                    query = "UPDATE beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data SET bank_name=?, bank_abbr=?, bank_account_number=?, ifsc_code=?, status = ? WHERE rc_mem=? AND office_id = ?";

                    ps = con.prepareStatement(query);
                    int i = 0;
                    ps.setString(++i, bankName);
                    ps.setString(++i, bankAbbr);
                    ps.setString(++i, bankAccount);
                    ps.setString(++i, ifscCode);
                    ps.setInt(++i, status);
                    ps.setString(++i, rcMemId);
                    ps.setInt(++i, officeId);

                    if (ps.executeUpdate() > 0) {
                        updateBeneficiaryTxnStatus(schemeId, schemeCode, rcMemId, status, con);
                        con.commit();
                        //con.setAutoCommit(true);
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    try {
                        con.rollback();
                        con.setAutoCommit(true);
                    } catch (SQLException ex1) {
                        Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
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
                            con.setAutoCommit(true);
                            con.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    };
                }
                //}

            }

            if (request.getParameter("updateStatusInfo") != null) {
                updatePurposeId = 4;

                if (request.getParameter("rCmem") != null) {
                    rcMemId = request.getParameter("rCmem");
                }

                if (request.getParameter("status") != null) {
                    if (request.getParameter("status").trim().length() > 0) {
                        status = Integer.parseInt(SecurityClass.killchar(request.getParameter("status")));
                    } else {
                        message = "Status can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }
                } else {
                    message = "Status can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    return;
                }

                if (request.getParameter("remarks") != null) {
                    if (request.getParameter("remarks").trim().length() > 0) {
                        logRemarks = request.getParameter("remarks");
                        logRemarks = SecurityClass.killchar(logRemarks);
                    } else {
                        message = "Remarks can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }
                } else {
                    message = "Remarks can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    return;
                }

                schemeCode = cm.getSchemeCode(schemeId);
                Connection con = null;
                CallableStatement cs = null;
                PreparedStatement ps = null;

                ResultSet rs = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    con.setAutoCommit(false);

                    createBeneficiaryLog(schemeId, schemeCode, rcMemId, updatePurposeId, logRemarks, userId, userName, con);

                    query = "UPDATE beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data SET status = ? WHERE rc_mem=? AND office_id = ?";

                    ps = con.prepareStatement(query);
                    int i = 0;
                    ps.setInt(++i, status);
                    ps.setString(++i, rcMemId);
                    ps.setInt(++i, officeId);

                    if (ps.executeUpdate() > 0) {
                        updateBeneficiaryTxnStatus(schemeId, schemeCode, rcMemId, status, con);
                        con.commit();
                        //con.setAutoCommit(true);
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    try {
                        con.rollback();
                        con.setAutoCommit(true);
                    } catch (SQLException ex1) {
                        Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("DBTInitiator/beneficiaryListManagementEdit.jsp");
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
                            con.setAutoCommit(true);
                            con.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    };
                }

            }

            if (request.getParameter("request_for") != null) {
                String token = "", bankList = "", ifscValidity = "";
                String requestFor = request.getParameter("request_for");
                if (session.getAttribute("banktoken") != null) {
                    token = session.getAttribute("banktoken").toString();
                }

                if (requestFor.equals("banklist")) {
                    if (!token.equals("")) {
                        try {
                            bankList = getBankNameList(token);
                        } catch (JSONException ex) {
                            Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        token = getToken();
                        session.setAttribute("banktoken", token);
                        try {
                            bankList = getBankNameList(token);
                        } catch (JSONException ex) {
                            Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    out.print(bankList);

                }

                if (requestFor.equals("ifscValidity")) {

                    if (request.getParameter("ifscCode") != null) {
                        if (request.getParameter("ifscCode").trim().length() > 0) {
                            ifscCode = request.getParameter("ifscCode");
                            ifscCode = SecurityClass.killchar(ifscCode);

                        }
                    }

                    if (!token.equals("")) {
                        try {
                            ifscValidity = getIFCValidity(token, ifscCode);
                        } catch (JSONException ex) {
                            Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        token = getToken();
                        session.setAttribute("banktoken", token);
                        try {
                            ifscValidity = getIFCValidity(token, ifscCode);
                        } catch (JSONException ex) {
                            Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    out.print(ifscValidity);
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

    public String getToken() throws IOException {
        String data = "";
        String url = "";
        String json = "";
        String res = "";
        String t = "";
        try {
            //OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(7200000, TimeUnit.MILLISECONDS)
                    .build();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("client_id", "bIMwIhFIc1jCiKi87m9i866KsVRvFmVv")
                    .addFormDataPart("client_secret", "mFGnddNZXM7O0pnyfLTGJGIUG5ItvOZm")
                    .addFormDataPart("provision_key", "tf54qm8YAwh1lUKHi7qZ00xN50orLZmT")
                    .addFormDataPart("grant_type", "client_credentials")
                    .addFormDataPart("scope", "email")
                    .build();

            Request request = new Request.Builder().url("https://apigw.trsc.nic.in/bankinfoapi/oauth2/token").method("POST", formBody).build();
            okhttp3.Response response = client.newCall(request).execute();
            data = response.body().string().toString();

            JSONObject j = new JSONObject(data);
            String accessToken = j.getString("access_token");
            System.out.println(accessToken);
            res = accessToken; //getBankNameList(accessToken);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String getIFCValidity(String token, String ifsc) throws IOException, JSONException {
        String result = "";
        if (token != null && ifsc != null) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://apigw.trsc.nic.in/bankinfoapi/findbyifsc?ifsc=" + ifsc + "&access_token=" + token)
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                result = response.body().string().toString();
            } else {
                result = "IFSC Not Verifiable";
            }

        }
        return result;
    }

    public String getBankNameList(String token) throws IOException, JSONException {
        String result;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url("https://apigw.trsc.nic.in/bankinfoapi/listdistinctbank?access_token=" + token).method("GET", null).build();
        okhttp3.Response response = client.newCall(request).execute();
        result = response.body().string().toString();
        System.out.print(result);
        JSONArray jsarr = new JSONArray(result);
        JSONObject x = new JSONObject();
        x.put("bankNameList", jsarr);
        //String bName = x.getString("bankname");
        System.out.print(x.toString());
        return x.toString();

    }

    public int createBeneficiaryLog(int schemeId, String schemeCode, String rcMemId, int logPurposeId, String logRemarks, String userId, String userName, Connection con) {
        int op = 0;
        PreparedStatement ps = null;
        String tabName = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
        String query = "INSERT INTO beneficiary.log_beneficiary_data(\n"
                + "	log_purpose_id, log_remarks, log_created_by, log_creator_name, sl_no, full_name, father_husband_name, district, subdivision, block_mc_np_adc_nonadc, gp_ward_name, gender, date_of_birth, \n"
                + "	caste, bank_account_number, pid, scheme_name, lgd_dist_code, lgd_subdiv_code, lgd_block_code, lgd_gp_code, scheme_code, duplicate_in_rc, missing_in_rc, rc_no, rc_mem, ifsc_code, \n"
                + "	entryby, office_id, entrydate, status, source_code, recomendation_list_id, online_application_id, eligible_dbt_amount, scheme_id, ben_email, ben_mobile, bank_name, bank_abbr, \n"
                + "	entry_financial_year, additional_field1, additional_field2, additional_field3, additional_field4, additional_field5, additional_field6, additional_field7, additional_field8, \n"
                + "	additional_field9, additional_field10, is_query_raised, query_id, bank_account_validation_status, bank_account_validation_requested, bank_account_validation_status_code)\n"
                + " SELECT ?, ?, ?, ?, sl_no, full_name, father_husband_name, district, subdivision, block_mc_np_adc_nonadc, gp_ward_name, gender, date_of_birth, \n"
                + "	caste, bank_account_number, pid, scheme_name, lgd_dist_code, lgd_subdiv_code, lgd_block_code, lgd_gp_code, scheme_code, duplicate_in_rc, missing_in_rc, rc_no, rc_mem, ifsc_code, \n"
                + "	entryby, office_id, entrydate, status, source_code, recomendation_list_id, online_application_id, eligible_dbt_amount, scheme_id, ben_email, ben_mobile, bank_name, bank_abbr, \n"
                + "	entry_financial_year, additional_field1, additional_field2, additional_field3, additional_field4, additional_field5, additional_field6, additional_field7, additional_field8, \n"
                + "	additional_field9, additional_field10, is_query_raised, query_id, bank_account_validation_status, bank_account_validation_requested, bank_account_validation_status_code "
                + " FROM " + tabName + " a WHERE a.scheme_id = ? AND a.rc_mem = ?";

        try {
            ps = con.prepareStatement(query);

            int i = 0;
            ps.setInt(++i, logPurposeId);
            ps.setString(++i, logRemarks);
            ps.setString(++i, userId);
            ps.setString(++i, userName);
            ps.setInt(++i, schemeId);
            ps.setString(++i, rcMemId);

            op = ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }
        return op;
    }

    public int updateBeneficiaryTxnStatus(int schemeId, String schemeCode, String rcMemId, int status, Connection con) {
        int op = 0;
        PreparedStatement ps = null;
        String tabName = "beneficiary.tran_" + schemeId + "_" + schemeCode.toLowerCase() + "_beneficiary_status_mapping";
        String query = "UPDATE " + tabName + " SET status = ? WHERE scheme_id = ? AND rc_mem = ?";
        try {
            ps = con.prepareStatement(query);

            int i = 0;
            ps.setInt(++i, status);
            ps.setInt(++i, schemeId);
            ps.setString(++i, rcMemId);

            op = ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BeneficiaryDetailsUpdate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
        }

        return op;
    }

}
