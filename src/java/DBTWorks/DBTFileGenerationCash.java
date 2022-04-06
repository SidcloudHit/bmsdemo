/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import Common.CommonMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javax.servlet.annotation.WebServlet;
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
 * @author NIC-Arindam
 */
@WebServlet(name = "DBTFileGenerationCash", urlPatterns = {"/DBTFileGenerationCash"})
public class DBTFileGenerationCash extends HttpServlet {

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
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            String today = formatter.format(currentDate);
            String today1 = formatter1.format(currentDate);
            String message = "";

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
                    if (request.getParameter("schemeId") != null) {

                        String scheme = request.getParameter("schemeId");
                        int schemeId = Integer.parseInt(scheme.split("~")[0]);
                        String schemeCode = scheme.split("~")[1];

                        //String filename = schemeCode + "_" + today + ".json";
                        Common.CommonMethod cm = new CommonMethod();
                        String schemeName = cm.getSchemeName(schemeId);
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        long batchId = timestamp.getTime();
                        String filename = schemeCode + "_241" + batchId + "_1" + ".json";
                        String jsonStr = "";//"["
                        String jsonStrTemp = "";
                        String jsonStrSummary = "";
                        int i = 0, totalAmount = 0, instl = 0;
                        String finYr = "", rcMemId = "";
                        String[] benids = null;
                        JSONObject jsobj = new JSONObject();
                        JSONArray jsarr = new JSONArray();

                        JSONObject jsobj1 = new JSONObject();
                        JSONArray jsarr1 = new JSONArray();

                        if (request.getParameter("finYr") != "" || request.getParameter("finYr") != null) {
                            finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                        }
                        if (request.getParameter("instl") != "" || request.getParameter("instl") != null) {
                            instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                        }
                        String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");

                        String myJsonData = request.getParameter("json");

                        try {
                            jsarr1 = new JSONArray(myJsonData);
                            benids = new String[jsarr1.length()];
                            //jsobj1.put("dbtBenList", jsarr1);
                            for (int n = 0; n < jsarr1.length(); n++) {
                                jsobj1 = jsarr1.getJSONObject(n);

                                //int benid = jsobj.getInt("beneficiary_id");
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

//                String myJsonData = request.getParameter("json");
//                myJsonData = myJsonData.replace("[", "");
//                myJsonData = myJsonData.replace("]", "");
//                String[] bids = myJsonData.split(",");
//
//                Integer[] benids = new Integer[bids.length];
//
//                for (int i = 0; i < bids.length; i++) {
//                    benids[i] = Integer.parseInt(bids[i]);
//                }
                        //jsonStr += "]";
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

//                   out.print(jsobj);
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

                        DBTStatusUpdate.updateCashDBTRequestStatus(tabName, schemeId, finYr, instl, benids, "241" + String.valueOf(batchId), userId);
                        String encSek = dbtAuth();
                        String encFileName = encryptDbtFile(encSek, filename, jsobj.toString());

                        JSONObject jsobjret1 = new JSONObject();
                        try {
                            jsobjret1.put("fileName", encFileName);
                            jsobjret1.put("nob", i);
                        } catch (JSONException ex) {
                            Logger.getLogger(DBTFileGenerationCash.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        out.print(jsobjret1.toString());
                        out.flush();
                        out.close();
                    }
                } else if (reqType == 2) {

                    String finYr = "", fileName = "", batchId = "";
                    String scheme = request.getParameter("schemeId");
                    int schemeId = Integer.parseInt(scheme.split("~")[0]);
                    String schemeCode = scheme.split("~")[1];
                    if (request.getParameter("finYr") != "" || request.getParameter("finYr") != null) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("fileName") != "" || request.getParameter("fileName") != null) {
                        fileName = request.getParameter("fileName");
                        schemeCode = fileName.substring(0, 5);
                        batchId = fileName.substring(6, 22);
                    }

                    String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                    String status = "";
                    if (checkFileSessionValidity(schemeId, batchId, fileName, tabName)) {

                        ResourceBundle resource = ResourceBundle.getBundle("Common/config");
                        String path = resource.getString("dbt.cash.req") + fileName;
                        String dbtSwitchUrl = resource.getString("dbt.switch.url");
                        File file1 = new File(path);

//                    String url = "";
                        //String url = "http://10.183.15.140:80/dbtPortal/api/controller/dbtfileupload";
                        //String url = "http://10.183.15.140:8084/dbtPortal/api/controller/dbtfileupload"; //for testing
                        //status = "{\"Result\": \"File Pushed\", \"CTOS_ack\":\"SUCCESS\"}";
                        status = upload(dbtSwitchUrl, file1);
                        System.out.println(status);
                        try {
                            JSONObject jsobj = new JSONObject(status);
                            if (jsobj.getString("CTOS_ack").trim().equalsIgnoreCase("SUCCESS")) {
                                Connection con = null;
                                try {
                                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                    con.setAutoCommit(false);
                                    DBTStatusUpdate.updateFilePushStatus(tabName, batchId, con);
                                    DBTStatusUpdate.updateDBTFileStatus(batchId, userId, tabName, con);
                                    con.commit();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                    try {
                                        con.rollback();
                                    } catch (SQLException ex1) {
                                        Logger.getLogger(DBTFileGenerationCash.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                                } finally {
                                    try {
                                        if (con != null) {
                                            con.setAutoCommit(true);
                                            con.close();
                                        }
                                    } catch (Exception e) {
                                        Logger.getLogger(DBTFileGenerationCash.class.getName()).log(Level.SEVERE, null, e);
                                    };
                                }

                            }

                        } catch (JSONException ex) {
                            Logger.getLogger(DBTFileGenerationCash.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        status = "{\"Result\": \"File session expired\", \"CTOS_ack\":\"Failed\"}";
                    }

                    out.print(status);
                    out.flush();
                    out.close();
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

    public boolean checkFileSessionValidity(int schemeId, String batchId, String fileName, String tabName) {
        boolean valid = false;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            String query = "";
            int i = 1;
            ps = con.prepareCall("{call public.check_file_session_validity(?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, batchId);
            ps.setString(i++, fileName);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    valid = rs.getBoolean(1);//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
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

        return valid;
    }

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
        String s = "";
        try {
            //OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1000000, TimeUnit.MILLISECONDS)
                    .build();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(okhttp3.MediaType.parse("application/json"), file))
                    //  .addFormDataPart("other_field", "other_field_value")
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            okhttp3.Response response = client.newCall(request).execute();
            s = response.body().string().toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }

}
