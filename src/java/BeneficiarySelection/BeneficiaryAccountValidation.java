/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import Beneficiary.BeneficiaryStatusUpdate;
import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import log.errorlog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "BeneficiaryAccountValidation", urlPatterns = {"/BeneficiaryAccountValidation"})
public class BeneficiaryAccountValidation extends HttpServlet {

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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            errorlog log = new errorlog();
            
            int i = 0, result = 0, status = 0;
            int deptId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "", rcMemId = "";
            String message = "";
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            String today = formatter.format(currentDate);
            String today1 = formatter1.format(currentDate);
            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }

            if (request.getParameter("status") != "" && request.getParameter("status") != null) {
                status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
            }

            if (request.getParameter("AccValid") != null) {
                String token = "", pushAck = "", strJson = "";
                int valiadation_details = 0;
                if (request.getParameter("schemeId") != null) {
                    String scheme = request.getParameter("schemeId");
                    int schemeId = Integer.parseInt(scheme.split("~")[0]);
                    String schemeCode = scheme.split("~")[1];
                    Common.CommonMethod cm = new CommonMethod();
                    String schemeName = cm.getSchemeName(schemeId);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    long batchId = timestamp.getTime();

                    //String filename = schemeCode + "_241" + batchId + "_1" + ".json";
                    String jsonStr = "", stringJson = "";//"["
                    String jsonStrTemp = "";
                    String jsonStrSummary = "";
                    String jsonStrSummary1 = "";
                    String jsonStrbeneficiaryInvalid = "";
                    int totalAmount = 0, instl = 0;
                    //String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                    //String tabName1 = "tran_" + schemeId + "_" + schemeCode.toLowerCase() + "_beneficiary_status_mapping";

                    JSONObject jsobj = new JSONObject();
                    JSONArray jsarr = new JSONArray();
                    JSONArray jsarr2 = new JSONArray();
                    JSONObject jsobj1 = new JSONObject();
                    JSONArray jsarr1 = new JSONArray();
/////////////////////////////////////////////////////////////////////////////                        
                    JSONObject jsobj3 = new JSONObject();
                    JSONArray jsarr3 = new JSONArray();
                    JSONArray jsarr4 = new JSONArray();
/////////////////////////////////////////////////////////////////////////////
                    String[] benids = null;
                    String myJsonData = request.getParameter("json");

                    try {
                        jsarr1 = new JSONArray(myJsonData);
                        benids = new String[jsarr1.length()];
                        for (int n = 0; n < jsarr1.length(); n++) {
                            jsobj1 = jsarr1.getJSONObject(n);
                            if (i > 0) {
                                jsonStrTemp += ",";
                            }
                            benids[i] = jsobj1.getString("rc_mem");
                            int benType = jsobj1.getInt("ben_type");
                            if (benType == 1) {
                                rcMemId = jsobj1.getString("rc_mem");
                            } else if (benType == 2) {
                                rcMemId = "";
                            }

                            benids[i] = jsobj1.getString("rc_mem");
                            //out.print(benids[i]);
                            jsonStrTemp += "{"
                                    + "            \"name\":\"" + jsobj1.getString("full_name") + "\","
                                    + "            \"memberid\":\"" + rcMemId + "\","
                                    + "            \"bankac\":\"" + jsobj1.getString("bank_account") + "\","
                                    + "            \"ifsc\":\"" + jsobj1.getString("ifsc") + "\""
                                    //+ "            \"transaction_amount\":\"" + jsobj1.getInt("amount") 

                                    + " }";
                            //System.out.print(jsonStrTemp);
                            //totalAmount += jsobj1.getInt("amount");

                            i++;

                        }
                        jsonStr += jsonStrTemp;
                    } catch (JSONException ex) {
                        ex.getMessage();
                    }

                    jsonStrSummary = "{" + "\"total_beneficiary_count\":\"" + i + "\","
                            + "\"reference_date\":\"" + today1 + "\","
                            + "\"batchid\":\"241" + batchId + "\","
                            + "\"remarks\":\"Data Sent for Validation\"" + "}";

                    Connection con = null;
                    try {
                        jsarr = new JSONArray("[" + jsonStr + "]");
                        jsobj.put("beneficiary", jsarr);
                        jsarr2 = new JSONArray("[" + jsonStrSummary + "]");
                        //JSONObject  jsobj2 =  new JSONObject(jsonStrSummary);
                        jsobj.put("summary", jsarr2);
                        strJson = jsobj.toString();
                        System.out.println(strJson);

                        con = new DBCon.DBSource().connectToBMSDB().getConnection();
                        //con.setAutoCommit(false);
                        insertValidationRequest(schemeId, "241" + batchId, strJson, con);
                        token = getToken();

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        pushAck = getValidationPush(token, strJson);//getValidationPushTest(token, strJson);//getValidationPush(token, strJson);
                        System.out.println(pushAck);

//                        pushAck = "{"
//                                + "    \"summary\": ["
//                                + "        {"
//                                + "            \"reference_uniqueno\": \"153\",\n"
//                                + "            \"total_beneficiary_count\": 1,\n"
//                                + "            \"reference_date\": \"04-03-2022\",\n"
//                                + "            \"batchid\": \"241" + batchId + "\",\n"
//                                + "            \"remarks\": \"Data post successfully\",\n"
//                                + "            \"status\": \"success\"\n"
//                                + "        }\n"
//                                + "    ],\n"
//                                + "    \"beneficiaryInvalid\": []\n"
//                                + "}";
                        if (con == null || con.isClosed()) {
                            con = new DBCon.DBSource().connectToBMSDB().getConnection();
                            //con.setAutoCommit(false);
                        }
                        insertValidationAck(schemeId, "241" + batchId, pushAck, con);

                        if (!pushAck.equals("")) {

                            jsobj3 = new JSONObject(pushAck);
                            jsarr3 = jsobj3.getJSONArray("summary");
                            String ackStatus = jsarr3.getJSONObject(0).getString("status");
                            String ackBatchId = jsarr3.getJSONObject(0).getString("batchid");
                            //int ackBenCount = jsarr3.getJSONObject(0).getInt("total_beneficiary_count");

                            if (ackBatchId.equals("241" + batchId)) {
                                valiadation_details = insertValidationDetails(schemeId, schemeCode, "241" + batchId, pushAck, strJson, ackStatus, con);
                            }
                        }

                        //con.commit();
                    } catch (JSONException ex) {
                        //con.rollback();
                        Logger.getLogger(BeneficiaryAccountValidation.class.getName()).log(Level.SEVERE, null, ex);
                        log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
                    } finally {
                        try {
                            if (con != null) {
                                //con.setAutoCommit(true);
                                con.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        };
                    }

                    //out.print(valid_data);
                    //out.print(valiadation_details);
                }
                out.print(pushAck);
            }
            //result = BeneficiaryStatusUpdate.updateBeneficiaryFreezeStatus(tabName, tabName1, schemeId, benids, userId, status, roleId);
            if (request.getParameter("validResponse") != null) {
                String token = "", pullResponse = "";
                String batchId = request.getParameter("batchId");
                String refDate = request.getParameter("refDate");
                int scheme_id = Integer.parseInt(request.getParameter("scheme").split("~")[0]);
                String scheme_code = request.getParameter("scheme").split("~")[1];
                token = getToken();
                pullResponse = getPulledResponse(token, batchId, refDate, scheme_id, scheme_code);
                
                out.print(pullResponse);

            }
        }
        //out.print(result);

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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(BeneficiaryAccountValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(BeneficiaryAccountValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("client_id", "YBd06BifJ5FbtYexiIYe6biwoWziwB7X")
                    .addFormDataPart("client_secret", "6AJLqxzupIlf40kqiYJ5aaLzyKagmObM")
                    .addFormDataPart("provision_key", "JmII8BQBf5CPaoFSR7HD876e2DmhlstY")
                    .addFormDataPart("grant_type", "client_credentials")
                    .addFormDataPart("scope", "email")
                    .build();
            Request request = new Request.Builder()
                    .url("https://apigw.trsc.nic.in/npci_bank_verification/oauth2/token")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            data = response.body().string().toString();
            JSONObject j = new JSONObject(data);
            String token = j.getString("access_token");
            System.out.println(token);
            res = token; //getBankNameList(accessToken);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;

    }

    public String getValidationPush(String token, String data_val) throws IOException, JSONException {
        String result = "", msg = "";
        if (token != null && data_val != null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1500, TimeUnit.SECONDS)
                    .writeTimeout(1500, TimeUnit.SECONDS)
                    .readTimeout(1500, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, data_val);
            Request request = new Request.Builder()
                    .url("https://apigw.trsc.nic.in/npci_bank_verification/ncpi/bankaccountverification/pushData")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string().toString();

        }
        return result;

    }

    public String getValidationPushTest(String token, String data_val) throws IOException, JSONException {
        String result = "", msg = "";
        if (token != null && data_val != null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1500, TimeUnit.SECONDS)
                    .writeTimeout(1500, TimeUnit.SECONDS)
                    .readTimeout(1500, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, data_val);
            Request request = new Request.Builder()
                    .url("http://10.183.9.226:8085/npci_bank_verification/ncpi/bankaccountverification/pushData")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string().toString();

        }
        return result;

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
            result = response.body().string().toString();
        }
        return result;
    }

    public String getPulledResponse(String token, String batchId, String ref_date, int scheme_id, String scheme_code) throws IOException {
        String result = "";
        Common.CommonMethod cm = new CommonMethod();
        if (token != null && batchId != null && ref_date != null) {
            ref_date=cm.changeDateFormat(ref_date,"-","-");
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://apigw.trsc.nic.in/npci_bank_verification/ncpi/bankaccountverification/getResponseByBatchid?batchid=" + batchId + "&referencedate=" + ref_date + "&access_token=" + token)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string().toString();
            CallableStatement cs = null;
            Connection con = null;
            try {
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                cs = con.prepareCall("{call beneficiary.update_account_validation_details(?,?,?,?::json,?)}");
                cs.setString(1, batchId);
                cs.setInt(2, scheme_id);
                cs.setString(3, scheme_code);
                cs.setString(4, result);
                cs.setString(5, ref_date);
                cs.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                    }
                    con = null;
                }

            }
        }
        return result;
    }

    public int insertValidationDetails(int scheme_id, String scheme_code, String batchId, String push_ack, String json) throws SQLException {

        int i = 1;
        int status = 0;
        Connection con = null;
        PreparedStatement ps = null;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {

            ps = con.prepareCall("{CALL beneficiary.insert_account_validation_details(?,?,?,?::json,?::json)}");
            ps.setString(i++, batchId);
            ps.setInt(i++, scheme_id);
            ps.setString(i++, scheme_code);
            ps.setString(i++, push_ack);
            ps.setString(i++, json);

            if (ps.executeUpdate() > 0) {

                System.out.println(status);
            } else {

                System.out.println(status);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

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

    public int insertValidationRequest(int scheme_id, String batchId, String json, Connection con) throws SQLException {

        int i = 1;
        int status = 0;
        //Connection con = null;
        PreparedStatement ps = null;

        //con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String query = "INSERT INTO beneficiary.tbl_account_validation_push_details(batch_id, scheme_id, request_details, req_time)VALUES (?, ?, ?, CURRENT_TIMESTAMP);";
        try {

            ps = con.prepareStatement(query);
            ps.setString(i++, batchId);
            ps.setInt(i++, scheme_id);
            ps.setString(i++, json);

            if (ps.executeUpdate() > 0) {

                System.out.println(status);
            } else {

                System.out.println(status);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

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

    public int insertValidationAck(int scheme_id, String batchId, String push_ack, Connection con) throws SQLException {

        int i = 1;
        int status = 0;
        //Connection con = null;
        PreparedStatement ps = null;

        //con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String query = "UPDATE beneficiary.tbl_account_validation_push_details SET ack_deatils = ? ,ack_time = CURRENT_TIMESTAMP WHERE batch_id = ? AND scheme_id = ?;";
        try {

            ps = con.prepareStatement(query);
            ps.setString(i++, push_ack);
            ps.setString(i++, batchId);
            ps.setInt(i++, scheme_id);

            if (ps.executeUpdate() > 0) {

                System.out.println(status);
            } else {

                System.out.println(status);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

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

    public int insertValidationDetails(int scheme_id, String scheme_code, String batchId, String push_ack, String json, String ackStatus, Connection con) throws SQLException {
        int i = 1;
        int status = 0;
        //Connection con = null;
        PreparedStatement ps = null;

        //con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {

            ps = con.prepareCall("{CALL beneficiary.insert_account_validation_details(?,?,?,?,?::json,?::json)}");
            ps.setString(i++, batchId);
            ps.setInt(i++, scheme_id);
            ps.setString(i++, scheme_code);
            ps.setString(i++, ackStatus);
            ps.setString(i++, push_ack);
            ps.setString(i++, json);

            if (ps.executeUpdate() > 0) {

                System.out.println(status);
            } else {

                System.out.println(status);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

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

    public int updateValidationDetails(int scheme_id, String scheme_code, long batchId, String push_ack, String json) throws SQLException {
        String batch = Long.toString(batchId);
        int i = 1;
        int status = 0;
        Connection con = null;
        PreparedStatement ps = null;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {

            ps = con.prepareCall("{CALL beneficiary.insert_account_validation_details(?,?,?,?::json,?::json)}");
            ps.setString(i++, batch);
            ps.setInt(i++, scheme_id);
            ps.setString(i++, scheme_code);
            ps.setString(i++, push_ack);
            ps.setString(i++, json);

            if (ps.executeUpdate() > 0) {

                System.out.println(status);
            } else {

                System.out.println(status);

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

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

}
