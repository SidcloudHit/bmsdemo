/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import Common.CommonMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nic.security.AcutalDataCommunication;
import nic.security.ClientAuthentication;
import nic.security.ServerAuthentication;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
public class DBTFileGenerationCash_new extends HttpServlet {

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
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            String today = formatter.format(currentDate);
            String today1 = formatter1.format(currentDate);
            String message = "", sql = "";
            Connection con = null;
            int counter = 0;
            int deptId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";

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

            /* TODO output your page here. You may use following sample code. */
            if (request.getParameter("reqType") != null) {
                int reqType = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("reqType")));
                if (reqType == 1) {

                    if (request.getParameter("fileGen") != null) {

                        int instl = 0, appid = 0, officeid = 0, checkerofficeid = 0, totalFiles = 0, totalFilesReq = 0, totalBens = 0;
                        String finYr = "";
                        boolean jsonFileGenerated = false;
                        JSONObject jsobjret1 = new JSONObject();

                        String scheme = request.getParameter("schemeId");
                        int schemeId = Integer.parseInt(scheme.split("~")[0]);
                        String schemeCode = scheme.split("~")[1];

                        //String filename = schemeCode + "_" + today + ".json";
                        Common.CommonMethod cm = new CommonMethod();
                        String schemeName = cm.getSchemeName(schemeId);
                        if (request.getParameter("finYr") != "" && request.getParameter("finYr") != null) {
                            finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                        }
                        if (request.getParameter("instl") != "" && request.getParameter("instl") != null) {
                            instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                        }
                        if (request.getParameter("appid") != "" && request.getParameter("appid") != null) {
                            appid = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("appid")));
                        }
                        if (request.getParameter("officeid") != "" && request.getParameter("officeid") != null) {
                            officeid = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeid")));
                        }
                        if (request.getParameter("checkerofficeid") != "" && request.getParameter("checkerofficeid") != null) {
                            checkerofficeid = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerofficeid")));
                        }

                        String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");

                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                        try {

                            String query = "select count(*) as totalcount from ("
                                    + "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                                    + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                    + "a.scheme_name, '' AS bank_name, c.amount, b.office_name   "
                                    + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a " //INNER JOIN rc_member b ON a.rc_mem = b.member_id
                                    + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                    + "INNER JOIN transaction." + tabName + " c ON a.rc_mem = c.beneficiary_id "
                                    + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + "  AND c.status = 15  and approver_esign_appl_id=" + appid;

//                            query += " UNION "
//                                    + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                                    + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                                    + "a.scheme_name, '' AS bank_name, amount, b.office_name   "
//                                    + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                                    + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                                    + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                                    + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + "  AND c.status = 15  and approver_esign_appl_id=" + appid;
                            query += ") p";
                            ps = con.prepareStatement(query);

                            rs = ps.executeQuery();
                            if (rs.next()) {
                                counter = rs.getInt("totalcount");
                            }
                            rs.close();
                            ps.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        totalFilesReq = counter / 500;
                        int rem = counter % 500;
                        if (rem > 0) {
                            totalFilesReq = totalFilesReq + 1;
                        }

                        for (int x = 0; x < counter; x += 500) {
                            int i = 0, totalAmount = 0;
                            String rcMemId = "";
                            String jsonStr = "";//"["
                            String jsonStrTemp = "";
                            String jsonStrSummary = "";

                            String[] benids = null;
                            JSONObject jsobj = new JSONObject();
                            JSONArray jsarr = new JSONArray();

                            JSONObject jsobj1 = new JSONObject();
                            JSONArray jsarr1 = new JSONArray();

                            try {
                                if (con == null || con.isClosed()) {
                                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                }

                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                long batchId = timestamp.getTime();
                                String filename = schemeCode + "_241" + batchId + "_1" + ".json";

                                sql = "SELECT a.rc_mem, 1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                                        + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                        + "a.scheme_name, '' AS bank_name, c.amount, b.office_name   "
                                        + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a " //INNER JOIN rc_member b ON a.rc_mem = b.member_id
                                        + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                        + "INNER JOIN transaction." + tabName + " c ON a.rc_mem = c.beneficiary_id "
                                        + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + "  AND c.status = 15  and approver_esign_appl_id=" + appid;

//                            sql += " UNION "
//                                    + "SELECT a.tempid AS rc_mem, 2 AS ben_type, a.full_name, COALESCE(a.father_name,'') AS guardian_name, '' AS mobile, "
//                                    + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
//                                    + "a.scheme_name, '' AS bank_name, amount, b.office_name   "
//                                    + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp a   "
//                                    + "INNER JOIN mas_role_based_office b ON a.office_id = b.office_id "
//                                    + "INNER JOIN transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_") + " c ON a.tempid = c.beneficiary_id "
//                                    + "WHERE scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + "  AND c.status = 15  and approver_esign_appl_id=" + appid;
                                sql += " ORDER BY rc_mem LIMIT 500";

                                try {
                                    jsarr = cm.getResultJSON(sql, con);
                                    jsobj.put("aaData", jsarr);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    benids = new String[jsarr.length()];
                                    totalBens += jsarr.length();
                                    for (int n = 0; n < jsarr.length(); n++) {
                                        jsobj1 = jsarr.getJSONObject(n);
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

                                        jsonStrTemp += "{\"personal_details\":{"
                                                + "            \"name\":\"" + jsobj1.getString("full_name") + "\","
                                                + "            \"father_name\":\"" + jsobj1.getString("guardian_name") + "\","
                                                + "            \"mobile_number\":\"" + jsobj1.getString("mobile") + "\","
                                                + "            \"unique_number\":\"" + jsobj1.getString("rc_mem") + "\","
                                                + "            \"ration_card_member_id\":\"" + rcMemId + "\""
                                                + "         },\"bank_details\":{"
                                                + "            \"bank_account\":\"" + jsobj1.getString("bank_account") + "\","
                                                + "            \"bank_ifsc\":\"" + jsobj1.getString("ifsc") + "\","
                                                + "            \"bank_name\":\"" + jsobj1.getString("bank_name") + "\","
                                                + "            \"transaction_amount\":\"" + jsobj1.getInt("amount") + "\","
                                                + "	       \"supplementary\":\"n\""
                                                + "         }"
                                                + " }";
                                        totalAmount += jsobj1.getInt("amount");
                                        i++;
                                    }
                                    jsonStr += jsonStrTemp;
                                } catch (JSONException ex) {
                                    ex.getMessage();
                                }

                                jsonStrSummary = "{"
                                        + "\"batchid\":\"241" + batchId + "\","
                                        + "\"hashuserid\":\"" + getUserIdHash(2) + "\","
                                        + "\"dbttype\":\"1\","
                                        + "\"total_beneficiary_count\":\"" + i + "\","
                                        + "\"total_transaction_amount\":\"" + totalAmount + "\","
                                        + "\"scheme_code\":\"" + schemeCode + "\","
                                        + "\"scheme_name\":\"" + schemeName + "\","
                                        + "\"reference_date\":\"" + today1 + "\","
                                        + "\"remarks\":\"Data Pushed for DBT\""
                                        + "}";

                                try {
                                    jsarr = new JSONArray("[" + jsonStr + "]");
                                    jsobj.put("beneficiary", jsarr);
                                    JSONObject jsobj2 = new JSONObject(jsonStrSummary);
                                    jsobj.put("summary", jsobj2);
                                    jsobj.put("Signature", "signing bytes");

                                } catch (JSONException ex) {
                                    Logger.getLogger(DBTFileGenerationCash.class.getName()).log(Level.SEVERE, null, ex);
                                }

//                out.print(jsobj);
//                out.flush();
//                out.close();
//                ServletContext sc = getServletContext();
//                String path = sc.getRealPath("/JsonFiles/" + filename);
                                ResourceBundle resource = ResourceBundle.getBundle("Common/config");
                                String path = resource.getString("dbt.cash.req") + filename;

                                File file1 = new File(path);
                                boolean result1 = Files.deleteIfExists(file1.toPath());

                                try (FileWriter file = new FileWriter(path)) {
                                    file.write(jsobj.toString());
                                    System.out.println("file write success");
                                } catch (Exception ex) {
                                    System.out.println("file write error");
                                    ex.printStackTrace();
                                }

                                session.setAttribute("filename", path);

//                            DBTStatusUpdate.updateCashDBTBatchUpdate(tabName, schemeId, finYr, instl, benids, "241" + String.valueOf(batchId));
                                String encSek = "";
                                String encFileName = "";
                                if (session.getAttribute("enc_sek") != null) {
                                    encSek = session.getAttribute("enc_sek").toString();
                                    if (!isSekValid(encSek)) {
                                        encSek = dbtAuth();
                                        session.setAttribute("enc_sek", encSek);
                                    }
                                } else {
                                    encSek = dbtAuth();
                                    session.setAttribute("enc_sek", encSek);
                                }

                                encFileName = encryptDbtFile(encSek, filename, jsobj.toString());
//                            if (!encFileName.equals("")) {
//                                DBTStatusUpdate.updateisfilegenarated(appid);
//                            }

                                if (con == null || con.isClosed()) {
                                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                }

                                if (!encFileName.equals("") && i > 0) {
                                    totalFiles += 1;

                                    if (totalFiles == totalFilesReq) {
                                        jsonFileGenerated = true;
                                    }
                                    DBTStatusUpdate.updateCashDBTFileGeneration(tabName, schemeId, finYr, instl, benids, "241" + String.valueOf(batchId), appid, i, officeid, checkerofficeid, encFileName, resource.getString("dbt.cash.req"), userId, jsonFileGenerated, con);
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(DBTFileGenerationCash_new.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                try {
                                    if (rs != null) {
                                        rs.close();
                                    }
                                    if (ps != null) {
                                        ps.close();
                                    }
                                    if (con != null) {
                                        con.close();
                                    }

                                } catch (Exception e) {
                                    Logger.getLogger(DBTFileGenerationCash_new.class.getName()).log(Level.SEVERE, null, e);
                                }
                            }

//                            if (i > 0) {
//                                try {
//                                    CallableStatement cs = null;
//                                    if (con == null || con.isClosed()) {
//                                        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//                                    }
//
//                                    cs = con.prepareCall("{call insertgeneratedfiledetails(?,?,?,?,?,?,?,?,?,?,?)}");
//
//                                    cs.setInt(1, schemeId);
//                                    cs.setString(2, finYr);
//                                    cs.setInt(3, instl);
//                                    cs.setInt(4, i);
//                                    cs.setInt(5, officeid);
//                                    cs.setInt(6, checkerofficeid);
//                                    cs.setString(7, encFileName);
//                                    cs.setString(8, resource.getString("dbt.cash.req"));
//                                    cs.setString(9, userId);
//                                    cs.setString(10, "241" + String.valueOf(batchId));
//                                    cs.setInt(11, appid);
//                                    cs.execute();
//                                    DBTStatusUpdate.updateCashDBTRequestStatusjsoncreated(tabName, schemeId, finYr, instl, benids);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    try {
//                                        if (rs != null) {
//                                            rs.close();
//                                        }
//                                        if (ps != null) {
//                                            ps.close();
//                                        }
//                                        if (con != null) {
//                                            con.close();
//                                        }
//
//                                    } catch (Exception e) {
//
//                                    }
//                                }
//                            }
                            i = 0;

                            // office_id_ integer, makeroffice_id_ integer,filename_ text, filepath_ text,is_file_push_ boolean,pushed_by_ text,batch_id_ text
                        }

                        try {
                            jsobjret1.put("totalFiles", totalFiles);
                            jsobjret1.put("totalBens", totalBens);
                        } catch (JSONException ex) {
                            Logger.getLogger(DBTFileGenerationCash.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        out.print(jsobjret1.toString());
                        out.flush();
                        out.close();
                    }

                }

                if (reqType == 3) {    ///for json file created list fetch
                    try {
                        if (request.getParameter("schemeId") != null) {
                            String jsonStr = "";//"["
                            String jsonStrTemp = "";
                            String jsonStrSummary = "";
                            int i = 0, totalAmount = 0, instl = 0, appid = 0, officeid = 0, checkerofficeid = 0;
                            String finYr = "", rcMemId = "";
                            String[] benids = null;
                            JSONObject jsobj = new JSONObject();
                            JSONArray jsarr = new JSONArray();

                            JSONObject jsobj1 = new JSONObject();
                            JSONArray jsarr1 = new JSONArray();
                            String scheme = request.getParameter("schemeId");
                            int schemeId = Integer.parseInt(scheme.split("~")[0]);
                            String schemeCode = scheme.split("~")[1];

                            //String filename = schemeCode + "_" + today + ".json";
                            Common.CommonMethod cm = new CommonMethod();
                            String schemeName = cm.getSchemeName(schemeId);
                            if (request.getParameter("finYr") != "" && request.getParameter("finYr") != null) {
                                finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                            }
                            if (request.getParameter("instl") != "" && request.getParameter("instl") != null) {
                                instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                            }
                            if (request.getParameter("officeid") != "" && request.getParameter("officeid") != null) {
                                officeid = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeid")));
                            }
                            if (request.getParameter("checkerofficeid") != "" && request.getParameter("checkerofficeid") != null) {
                                checkerofficeid = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerofficeid")));
                            }
                            sql = "SELECT  scheme_id, financial_year, \n"
                                    + "installment, beneficiary_count,  a.office_id, checkeroffice_id, filename, filepath, "
                                    + "is_file_push, pushed_by, created, batch_id,"
                                    + "CASE WHEN (is_file_push = false AND file_session_valid) THEN ' <button onclick=\"filepush('''||filename||''')\" class=\"btn btn-primary btn-sm\"> <i class=\"fa fa-share-square-o\">&nbsp;&nbsp;</i>Push</button>' "
                                    + "WHEN (is_file_push = false AND file_session_valid = false ) THEN '<i class=\"fa fa-times-circle-o\" style=\"color:red;\" aria-hidden=\"true\"></i> File Expired' "
                                    + "ELSE '<i class=\"fa fa-check-circle-o\" style=\"color:green;\" aria-hidden=\"true\"></i> File Pushed' END as action "
                                    //+ "' <button onclick=\"filepush('''||filename||''')\" class=\"btn btn-primary btn-sm\"> <i class=\"fa fa-share-square-o\">&nbsp;&nbsp;</i>Push</button>' as action\n "
                                    + "FROM dbtgenerated_file_list a "
                                    + "where scheme_id=" + schemeId + " and financial_year='" + finYr + "' and installment=" + instl + " and office_id=" + officeid + " "
                                    + "and checkeroffice_id=" + checkerofficeid + " and created_by='" + userId + "' ";
                            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                            JSONArray jsarr2 = null;
                            JSONObject jsobj2 = new JSONObject();
                            try {
                                jsarr2 = cm.getResultJSON(sql, con);
                                jsobj2.put("aaData", jsarr2);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            out.print(jsobj2);
                            out.flush();
                            out.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (con != null) {
                                con.close();
                            }
                        } catch (Exception e) {

                        }
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

    public String getUserIdHash(int opt) {
        String userIdHash = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
        String query = "SELECT userid_, useridhash_, app_key  FROM dbtregistration limit 1";
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (opt == 1) {
                    userIdHash = rs.getString("app_key").toString();
                } else if (opt == 2) {
                    userIdHash = rs.getString("useridhash_").toString();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (Exception e) {

            }
        }
        return userIdHash;
    }

    private String dbtAuth() {
        String encSek = "";
        String pubkey = "";
        String clientkey = "";
        String appkey = "";
        //String userid = "";
//                String appkey = "6fa91fbb677417915aaca1190ca2a32dae333ebc6af26646dc8d2d79280ea338";
//                String userid = "nayan";
        String app_client_enc = "";
        String hashuserd = "";
        String rsa_enc_clientKey = "";
        String resultws = "";
        Connection con = null;
        CallableStatement cs = null;
        try {

            //userid and app key from database
            con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
//            String query = "SELECT userid_, useridhash_, app_key  FROM dbtregistration limit 1";
//            ps = con.prepareStatement(query);
//
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                appkey = rs.getString("app_key").toString();
//                //userid = rs.getString("userid_").toString();
//                hashuserd = rs.getString("useridhash_").toString();
//            }

            appkey = getUserIdHash(1);
            hashuserd = getUserIdHash(2);
            // rs.close();
            //userid and app key from database

            //response.setHeader("Content-Disposition", "attachment;filename=output.txt");
            ServletContext cntxt = this.getServletContext();
            String fName = "/pubkey.txt";
            InputStream ins = cntxt.getResourceAsStream(fName);
            //try {
            if (ins != null) {
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader reader = new BufferedReader(isr);
                int n = 0;
                String word = "";
                while ((word = reader.readLine()) != null) {
                    pubkey = pubkey + word;
                }
            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            ClientAuthentication ca = new ClientAuthentication();
            String clint = ca.GenerateClientKey();
            JSONObject jsonobj = new JSONObject(clint);
            clientkey = (String) jsonobj.get("Result");

            //   WebServiceRegistration wsr = new WebServiceRegistration();
            // String app = wsr.generateAppKey(userid);
            // JSONObject jsonobject2 = new JSONObject(app);
            // appkey = (String) jsonobject2.get("Result");
            String enclint = ca.encryptClientKeyWithAppKey(clientkey, appkey);
            JSONObject jsonobject3 = new JSONObject(enclint);
            app_client_enc = (String) jsonobject3.get("Result");

            String encmsg = app_client_enc;//+ "|" + hashuserd;
            String rsa_enc = ca.getRSAEncryptedClientKey(encmsg, pubkey);
            JSONObject jsonobject5 = new JSONObject(rsa_enc);
            rsa_enc_clientKey = (String) jsonobject5.get("Result");

            JSONObject result = new JSONObject();

            //
            //query = "";
            //try {
            con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
            cs = con.prepareCall("{call insertclient_enc(?,?,?,?,?,?)}");
            cs.setString(1, clientkey);
            cs.setString(2, appkey);
            cs.setString(3, hashuserd);
            cs.setString(4, app_client_enc);
            cs.setString(5, rsa_enc_clientKey);
            cs.setString(6, pubkey);
            //client_key text, app_key text, hash_userid text, enc_app_client_key text, rsa_enc_clientkey text, pub_key text
            if (cs.execute()) {
                //result.put("status", "1");
//                            result.put("clientkey", clientkey);
//                            result.put("clientkey", appkey);
//                            result.put("hashuserd", hashuserd);
//                            result.put("app_client_enc", app_client_enc);
//                            result.put("pubkey", pubkey);
//                            result.put("rsa_enc_clientKey", rsa_enc_clientKey);

                //webservice call
                //try {
                //String rsa_enc_clientkey = "WEZCX%2Bv3ctC1cF910Gadg1npK2Cxik%2FE4TR41vnxGuZsLSpYk%2Fx16qCSFMmxMhYB7YIXtz5Nrzfas9JgIuzgaf6sZwZTOvSwHnMEJrgSWMAzHHNI8jdKDgjsnqCUHCqPuIsxDRanWINX90Xh7f3yNkZPqEBXfknAM198PjcVtJ56S4v3quRQTZ7%2BqhoeX1pq4CXavlQNTO0SExqjT3KVFueKpuVXCHW%2BBIldB2TP5aUXWuLq0fHkTCJKo5kYvFNOvI6SJZsoUCSRtCtFbARdaDpiPMt11NSxBk39YxmsusKnZ6Vi1%2FZF1btiufWe7ey3CxP291sUe3ieZXzdzG9hgw%3D%3D&useridh=dfb62ced17bd3b9c9e7ff9c0b056d221c2e7e706f275f7b16d880edd7efd5b9e";
                String baseurl = "http://10.183.15.140:80/dbtPortal/api/controller/authentication?rsa_enc_clientkey=";
                //String param=rsa_enc_clientKey+"&useridh="+hashuserd;
                URL url = new URL(baseurl + URLEncoder.encode(rsa_enc_clientKey, "UTF-8") + "&useridh=" + URLEncoder.encode(hashuserd, "UTF-8"));

                //URL url = new URL("http://10.183.9.32:8080/dbtPortal/api/controller/authentication?rsa_enc_clientkey=" + rsa_enc_clientKey+"&useridh="+hashuserd);//your url i.e fetch data from .
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP Error code : "
                            + conn.getResponseCode());
                }
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String output;
                while ((output = br.readLine()) != null) {
                    //System.out.println(output);
                    //resultws = output.toString();
                    result = new JSONObject(output.toString());
                }
                conn.disconnect();
                //JSONObject jsonobject10 = new JSONObject(result);
                String sts = (String) result.get("Status");
                encSek = (String) result.get("encsek");
                if (sts.equals("1")) {
                    cs = con.prepareCall("{call updatersaclient(?,?)}");
                    cs.setString(1, rsa_enc_clientKey);
                    cs.setString(2, encSek);
                    if (cs.execute()) {
                        result.put("update", "done");
                    }
                }

//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                //webservice call
            } else {
                result.put("Status", "0");
            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            //encSek = encsek;//result.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (cs != null) {
                    cs.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (Exception e) {

            }
        }

        return encSek;
    }

    private String encryptDbtFile(String encSek, String fileName, String fileContent) {
        String status = "";
        String pubkey = "";
        String result = "0";
        String rsa_enc_clientKey = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //response.setHeader("Content-Disposition", "attachment;filename=output.txt");                    
            String sekenckey = encSek;
            String clientkey = "";
            String hash_userid = "";
//                    String clientkey = "cfd8490450a7cccaf13b4734e42de6c59a235f86d0866daf26ad6b2ec0028ebe";
//                    String hash_userid = "dfb62ced17bd3b9c9e7ff9c0b056d221c2e7e706f275f7b16d880edd7efd5b9e";
//                    
            con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
            String query = "SELECT  client_key, hash_userid FROM rsaclient where enc_sek=? limit 1";
            ps = con.prepareStatement(query);
            ps.setString(1, sekenckey);

            rs = ps.executeQuery();
            while (rs.next()) {
                clientkey = rs.getString("client_key").toString();
                hash_userid = rs.getString("hash_userid").toString();
            }
            rs.close();

            ServerAuthentication sa = new ServerAuthentication();
            ClientAuthentication ca = new ClientAuthentication();
            AcutalDataCommunication adc = new AcutalDataCommunication();

            String jsonobj1 = sa.decryptClientKeyWithAppKey(sekenckey, clientkey);
            JSONObject sekobj = new JSONObject(jsonobj1);
            String sek = (String) sekobj.get("Result");
            //  String sek = sekenckey;
            //read file

            String filename = fileName.replace(".json", "_Enc.json");

            /*
            
            ServletContext cntxt = this.getServletContext();
            String fName = "/pubkey.txt";
            InputStream ins = cntxt.getResourceAsStream(fName);
            //try {
            if (ins != null) {
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader reader = new BufferedReader(isr);
                int n = 0;
                String word = "";
                while ((word = reader.readLine()) != null) {
                    pubkey = pubkey + word;
                }
            }
             
             */
 /* Disabling this part as content is passed as function parameter*/
//            String content = "";
//            ServletContext cntxt = this.getServletContext();
//            InputStream ins = cntxt.getResourceAsStream(fileName);
//            if (ins != null) {
//                InputStreamReader isr = new InputStreamReader(ins);
//                try (BufferedReader br = new BufferedReader(isr)) {
//
//                    String strCurrentLine;
//
//                    while ((strCurrentLine = br.readLine()) != null) {
//                        //System.out.println(strCurrentLine);
//                        content = content + strCurrentLine;
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            /**/
////
            JSONObject json = new JSONObject(fileContent);
            //   json.put("name", "TEST");
            //    json.put("link", "LINK_TEST");

            String rek = adc.generateSymmetricResponseEncryptionKey();

            String jsonobj2 = ca.encryptClientKeyWithAppKey(json.toString(), rek);
            JSONObject encrek = new JSONObject(jsonobj2);
            String enc_rek_with_json = (String) encrek.get("Result");

            JSONObject enc_rek_with_json1 = new JSONObject();
            enc_rek_with_json1.put("encRekWithData", enc_rek_with_json);
            enc_rek_with_json1.put("rek", rek);

            String jsonobj3 = ca.encryptClientKeyWithAppKey(enc_rek_with_json1.toString(), sek);
            JSONObject finaljsonobj = new JSONObject(jsonobj3);
            String finalenc = (String) finaljsonobj.get("Result");

            JSONObject enc = new JSONObject();
            enc.put("enc_data", finalenc);
            enc.put("enc_sek", sekenckey);
            enc.put("hashuserid", hash_userid);
//            ServletContext sc = getServletContext();
//            String path = sc.getRealPath(filename);

            ResourceBundle resource = ResourceBundle.getBundle("Common/config");
            String path = resource.getString("dbt.cash.req") + filename;

            File file1 = new File(path);
            boolean result1 = Files.deleteIfExists(file1.toPath());

            try (FileWriter file = new FileWriter(path)) {
                file.write(enc.toString());
            }

            status = filename;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

            }
        }

        return status;
    }

    private String dbtFileUpload(String encSek, String fileName) {
        String status = "";
        String pubkey = "";
        String result = "0";
        String rsa_enc_clientKey = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //response.setHeader("Content-Disposition", "attachment;filename=output.txt");                    
            String sekenckey = encSek;
            String clientkey = "";
            String hash_userid = "";
//                    String clientkey = "cfd8490450a7cccaf13b4734e42de6c59a235f86d0866daf26ad6b2ec0028ebe";
//                    String hash_userid = "dfb62ced17bd3b9c9e7ff9c0b056d221c2e7e706f275f7b16d880edd7efd5b9e";
//                    
            con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
            String query = "SELECT  client_key, hash_userid FROM rsaclient where enc_sek=? limit 1";
            ps = con.prepareStatement(query);
            ps.setString(1, sekenckey);

            rs = ps.executeQuery();
            while (rs.next()) {
                clientkey = rs.getString("client_key").toString();
                hash_userid = rs.getString("hash_userid").toString();
            }
            rs.close();

            ServerAuthentication sa = new ServerAuthentication();
            ClientAuthentication ca = new ClientAuthentication();
            AcutalDataCommunication adc = new AcutalDataCommunication();

            String jsonobj1 = sa.decryptClientKeyWithAppKey(sekenckey, clientkey);
            JSONObject sekobj = new JSONObject(jsonobj1);
            String sek = (String) sekobj.get("Result");
            //  String sek = sekenckey;
            //read file

            String filename = fileName.replace(".json", "_Enc.json");

            /*
            
            ServletContext cntxt = this.getServletContext();
            String fName = "/pubkey.txt";
            InputStream ins = cntxt.getResourceAsStream(fName);
            //try {
            if (ins != null) {
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader reader = new BufferedReader(isr);
                int n = 0;
                String word = "";
                while ((word = reader.readLine()) != null) {
                    pubkey = pubkey + word;
                }
            }
             
             */
            String content = "";
            ServletContext cntxt = this.getServletContext();
            InputStream ins = cntxt.getResourceAsStream(fileName);
            if (ins != null) {
                InputStreamReader isr = new InputStreamReader(ins);
                try (BufferedReader br = new BufferedReader(isr)) {

                    String strCurrentLine;

                    while ((strCurrentLine = br.readLine()) != null) {
                        //System.out.println(strCurrentLine);
                        content = content + strCurrentLine;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
////

            JSONObject json = new JSONObject(content);
            //   json.put("name", "TEST");
            //    json.put("link", "LINK_TEST");

            String rek = adc.generateSymmetricResponseEncryptionKey();

            String jsonobj2 = ca.encryptClientKeyWithAppKey(json.toString(), rek);
            JSONObject encrek = new JSONObject(jsonobj2);
            String enc_rek_with_json = (String) encrek.get("Result");

            JSONObject enc_rek_with_json1 = new JSONObject();
            enc_rek_with_json1.put("encRekWithData", enc_rek_with_json);
            enc_rek_with_json1.put("rek", rek);

            String jsonobj3 = ca.encryptClientKeyWithAppKey(enc_rek_with_json1.toString(), sek);
            JSONObject finaljsonobj = new JSONObject(jsonobj3);
            String finalenc = (String) finaljsonobj.get("Result");

            JSONObject enc = new JSONObject();
            enc.put("enc_data", finalenc);
            enc.put("enc_sek", sekenckey);
            enc.put("hashuserid", hash_userid);
            ServletContext sc = getServletContext();
            String path = sc.getRealPath(filename);

            File file1 = new File(path);
            boolean result1 = Files.deleteIfExists(file1.toPath());

            try (FileWriter file = new FileWriter(path)) {
                file.write(enc.toString());
            }
            String url = "http://10.183.15.140:80/dbtPortal/api/controller/dbtfileupload";
            //File encFile = new File(path);
            status = upload(url, file1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

            }
        }

        return status;
    }

    public String upload(String url, File file) throws IOException {
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .build();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(okhttp3.MediaType.parse("application/json"), file))
                //  .addFormDataPart("other_field", "other_field_value")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        okhttp3.Response response = client.newCall(request).execute();
        String s = response.body().string();
        return s;
    }

    public boolean isSekValid(String sek) {
        boolean status = false;
        String query = "SELECT FLOOR(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - sek_timestamp))/60) AS valid_minutes FROM public.rsaclient WHERE enc_sek = '" + sek + "' ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) < 28) {
                    status = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTFileGenerationCash_new.class.getName()).log(Level.SEVERE, null, ex);
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
        return status;
    }
}
