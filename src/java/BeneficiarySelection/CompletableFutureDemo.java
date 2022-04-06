package BeneficiarySelection;

import Common.CommonMethod;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nayan
 */
public class CompletableFutureDemo {

    FileHandler handler;

    public void ApiCallableFuture(JSONArray resultsuccessarray, JSONArray slnoarr, String tabName, int schemeId, String schemeCode, String fy, int installment,
            int batch_id, int dept_id, String dept_name, String installment_name, String user_id) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                boolean append = true;

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                ResourceBundle resource = ResourceBundle.getBundle("Common/config");
                String filename = resource.getString("bms.apicall.log") + "/error_" + java.time.LocalDate.now() + ".log";

                CommonMethod cm = new CommonMethod();
                Security.AES aes = new Security.AES();
                Connection conn = null;
                //dbcon obj = new dbcon();
                CallableStatement cs = null;
                ResultSet rs = null;

                Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");

                SimpleFormatter formatter = new SimpleFormatter();

                try {
                    /* Call API */
                    handler = new FileHandler(filename, append);
                    logger.addHandler(handler);

                    handler.setFormatter(formatter);
                    String slnosjsonnstring = "";
//                    slnosjsonnstring = cm.getslnoforapicall(tabName);
//                    JSONArray slnoarr = new JSONArray(slnosjsonnstring);
                    if (resultsuccessarray.length() > 0) {
                        for (int i = 0; i < slnoarr.length(); i++) {
                            Random random = new Random();
                            System.out.println(i);
                            try {
                                String query_set = "", beneficiaryname = "", uid = "", rcnumber = "", accountnumber = "", ifscdb = "", districtnamedb = "", subdivisionnamedb = "", block_mcdb = "", gp_vc_warddb = "";
                                int instalmentdb = 0, office_id = 0;
                                String finalret = "";
                                int _sln = 0;
                                HttpURLConnection urlConn = null;
                                OutputStream ouputStream = null;
                                BufferedReader reader1 = null;

                                int perc = 0;
                                int distleven = 1000;
                                String ready_toapprove = "N";
                                String approvedby = "", auto_approval_data = "";
                                String strapiresponselog = "", dob = "", guardians_name = "", rc_mem = "", bank_name = "", scheme_name = "";
                                ResultSet rs2 = null;
                                PreparedStatement pst = null;
                                int slno = slnoarr.getJSONObject(i).getInt("slno");
                                String sql = "SELECT slno,coalesce(beneficiaryname,'') as beneficiaryname,coalesce(uid,'') as uid,coalesce(rcnumber,'') as rcnumber, coalesce(accountnumber,'') as accountnumber, "
                                        + "coalesce(ifsc,'') as ifsc, coalesce(districtname,'') as districtname, coalesce(subdivisionname,'') as subdivisionname, coalesce(block_mc,'') as block_mc, "
                                        + "coalesce(gp_vc_ward,'') as gp_vc_ward, office_id FROM beneficiary." + tabName + " WHERE slno=" + slno;

                                if (conn == null || conn.isClosed()) {
                                    //Fix Start
                                    Thread l_thread = Thread.currentThread();
                                    l_thread.setContextClassLoader(this.getClass().getClassLoader());
                                    //Fix End

                                    conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
//                                    System.out.print("connection null");
//                                    String dsName = "java:/comp/env/bms";
//                                    try {
//                                        InitialContext ctx;
//
//                                        ctx = new InitialContext();
//                                        DataSource ds = (DataSource) ctx.lookup(dsName);
//                                        if (ds == null) {
//                                            System.out.print("Data source not found!");
//                                        }
//                                        try {
//                                            conn = ds.getConnection();
//                                        } catch (SQLException ex) {
//                                            ex.printStackTrace();
//                                        }
//                                    } catch (NamingException ex) {
//                                        ex.printStackTrace();
//                                    }
                                } else {
                                    System.out.println(conn.getClientInfo().elements().toString());
                                }
                                //System.out.print(sql);
                                pst = conn.prepareStatement(sql);
                                rs2 = pst.executeQuery();
                                if (rs2.next()) {
                                    try {
                                        _sln = rs2.getInt("slno");
                                        beneficiaryname = rs2.getString("beneficiaryname").trim();
                                        uid = rs2.getString("uid").trim();
                                        rcnumber = rs2.getString("rcnumber").trim();
                                        accountnumber = rs2.getString("accountnumber").trim();
                                        ifscdb = rs2.getString("ifsc").trim();
                                        districtnamedb = rs2.getString("districtname").trim();
                                        subdivisionnamedb = rs2.getString("subdivisionname").trim();
                                        block_mcdb = rs2.getString("block_mc").trim();
                                        gp_vc_warddb = rs2.getString("gp_vc_ward").trim();
                                        office_id = Integer.parseInt(rs2.getString("office_id").trim());

                                        String basurl = "https://bms.tripura.gov.in/ssotapi/webresources/services";
                                        //String basurl = "https://bmsuat.trsc.nic.in/ssotapi/webresources/services";
                                        //String basurl = "http://127.0.0.1:8080/ssotapi/webresources/services";

                                        String url1 = basurl + "/getMemberDetailsByUidOrRcNoName?uid=" + URLEncoder.encode(uid, "UTF-8") + "&rcNo=" + URLEncoder.encode(rcnumber, "UTF-8") + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");

                                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.INFO, "Requesting  URL: " + url1, "");

                                        String datainput = "uid=" + uid + "&rcNo=" + rcnumber + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");
                                        URL urlObj = new URL(url1);
                                        urlConn = (HttpURLConnection) urlObj.openConnection();
                                        urlConn.setDoOutput(true);
                                        urlConn.setRequestMethod("POST");

                                        urlConn.setRequestProperty("Content-Type", "application/json");
                                        //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                        urlConn.setConnectTimeout(600000);
                                        urlConn.setReadTimeout(600000);

                                        urlConn.setRequestProperty("Accept", "application/json");

                                        if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                            System.err.println("Unable to connect to the URL " + url1 + ", Error Code: " + urlConn.getResponseCode());

                                        } else {
                                            InputStream is = urlConn.getInputStream();
                                            reader1 = new BufferedReader(new InputStreamReader((is)));
                                            String tmpStr = null;
                                            while ((tmpStr = reader1.readLine()) != null) {

                                                finalret += tmpStr;
                                            }

                                            String fullnameapi = "", duplicateapi = "", rc_member_idapi = "", missing_in_rcapi = "", genderapi = "", casteapi = "", pan = "", rc_noapi = "", last_mile_addressapi = "", fps_idapi = "", fps_latapi = "", fps_longapi = "", postrcvalidation_distlgdcodeapi = "", postrcvalidation_distlgdnameapi = "", postrcvalidation_subdivlgdcodeapi = "", postrcvalidation_subdivlgdnameapi = "";
                                            String postrcvalidation_blocklgdcode = "", postrcvalidation_blocklgdname = "", postrcvalidation_gplgdcode = "", postrcvalidation_gplgdname = "", postrcvalidation_hof_rc_member_id = "", postrcvalidation_hofname = "", nearest_lat_long_desc = "";
                                            String father_name = "";
                                            JSONObject jsonObj = new JSONObject(finalret);

                                            JSONArray details = new JSONArray(jsonObj.get("beneficiarydetails").toString());
                                            for (int j = 0; j < details.length(); j++) {

                                                boolean isvalidflag = false;
                                                JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());

                                                String strnodata = "";
                                                try {
                                                    strnodata = tmpOBJ.get("message").toString().trim();
                                                } catch (JSONException ex) {
                                                }
                                                if (strnodata.equalsIgnoreCase(" No Data Found".trim())) {
                                                    isvalidflag = false;
                                                } else {
                                                    fullnameapi = tmpOBJ.optString("fullname").toString();
                                                    duplicateapi = tmpOBJ.optString("duplicate_in_rc").toString();
                                                    rc_member_idapi = tmpOBJ.optString("ration_card_memberid").toString();
                                                    missing_in_rcapi = tmpOBJ.optString("missing_in_rc").toString();

                                                    genderapi = tmpOBJ.optString("gender").toString();
                                                    casteapi = tmpOBJ.optString("caste").toString();
                                                    rc_noapi = tmpOBJ.optString("ration_card_no").toString();
                                                    last_mile_addressapi = tmpOBJ.optString("landmark").toString();

                                                    fps_idapi = tmpOBJ.optString("fps_id").toString();
                                                    fps_latapi = tmpOBJ.optString("fps_lat").toString();
                                                    fps_longapi = tmpOBJ.optString("fps_long").toString();
                                                    postrcvalidation_distlgdcodeapi = tmpOBJ.optString("lgd_district_code").toString();
                                                    postrcvalidation_distlgdnameapi = tmpOBJ.optString("lgd_district_name").toString();
                                                    postrcvalidation_subdivlgdcodeapi = tmpOBJ.optString("lgd_subdiv_code").toString();
                                                    postrcvalidation_subdivlgdnameapi = tmpOBJ.optString("lgd_subdiv_name").toString();
                                                    postrcvalidation_blocklgdcode = tmpOBJ.optString("lgd_block_code").toString();
                                                    postrcvalidation_blocklgdname = tmpOBJ.get("lgd_block_name").toString();

                                                    postrcvalidation_gplgdcode = tmpOBJ.optString("lgd_gp_code").toString();
                                                    postrcvalidation_gplgdname = tmpOBJ.optString("lgd_gp_name").toString();

                                                    postrcvalidation_hof_rc_member_id = tmpOBJ.optString("hof_member_id").toString();
                                                    postrcvalidation_hofname = tmpOBJ.optString("hof_name").toString();
                                                    nearest_lat_long_desc = tmpOBJ.optString("nearest_lat_long_desc").toString();

                                                    String parameter_missing = tmpOBJ.optString("parameter_missing").toString();
                                                    String invalid_rc_no = tmpOBJ.optString("invalid_rc_no").toString();
                                                    distleven = Integer.parseInt(tmpOBJ.optString("levenshtein_dst").equals("") ? "1000" : tmpOBJ.get("levenshtein_dst").toString());
                                                    father_name = tmpOBJ.optString("father_name");
                                                    dob = tmpOBJ.optString("member_dob");

                                                    //optString
                                                    //missing_in_rc,duplicate_in_rc,parameter_missing,invalid_rc_no,levenshtein_dst
                                                    if (missing_in_rcapi.equalsIgnoreCase("false")
                                                            && duplicateapi.equalsIgnoreCase("false")
                                                            && parameter_missing.equalsIgnoreCase("false")
                                                            && invalid_rc_no.equalsIgnoreCase("false")
                                                            && distleven == 0) {
                                                        isvalidflag = true;
                                                    } else {
                                                        isvalidflag = false;
                                                    }
                                                }
                                                JSONObject jobjforsave = new JSONObject();
                                                jobjforsave.put("scheme_id", schemeId);
                                                jobjforsave.put("scheme_code", schemeCode.equals("") ? JSONObject.NULL : schemeCode);
                                                jobjforsave.put("fy", fy.equals("") ? JSONObject.NULL : fy);
                                                jobjforsave.put("installment", installment);
                                                jobjforsave.put("full_name", beneficiaryname.equals("") ? JSONObject.NULL : beneficiaryname);
                                                jobjforsave.put("father_name", father_name.equals("") ? JSONObject.NULL : father_name);
                                                jobjforsave.put("guardians_name", guardians_name.equals("") ? JSONObject.NULL : guardians_name);
                                                jobjforsave.put("gender", genderapi.equals("") ? JSONObject.NULL : genderapi);
                                                jobjforsave.put("dob", dob.equals("") ? JSONObject.NULL : dob);
                                                jobjforsave.put("caste", casteapi.equals("") ? JSONObject.NULL : casteapi);
                                                jobjforsave.put("pid", uid.equals("") ? JSONObject.NULL : uid);
                                                jobjforsave.put("pan", pan.equals("") ? JSONObject.NULL : pan);
                                                jobjforsave.put("rc_no", rcnumber.equals("") ? JSONObject.NULL : rcnumber);
                                                jobjforsave.put("rc_mem", rc_mem.equals("") ? JSONObject.NULL : rc_mem);
                                                jobjforsave.put("bank_name", bank_name.equals("") ? JSONObject.NULL : bank_name);
                                                jobjforsave.put("ifsc_code", ifscdb.equals("") ? JSONObject.NULL : ifscdb);
                                                jobjforsave.put("bank_account_number", accountnumber.equals("") ? JSONObject.NULL : accountnumber);
                                                jobjforsave.put("amount", JSONObject.NULL);
                                                jobjforsave.put("lgd_dist_code", JSONObject.NULL);
                                                jobjforsave.put("district_name", districtnamedb.equals("") ? JSONObject.NULL : districtnamedb);
                                                jobjforsave.put("lgd_subdiv_code", JSONObject.NULL);
                                                jobjforsave.put("subdivision_name", subdivisionnamedb.equals("") ? JSONObject.NULL : subdivisionnamedb);
                                                jobjforsave.put("lgd_block_code", JSONObject.NULL);
                                                jobjforsave.put("block_name", block_mcdb.equals("") ? JSONObject.NULL : block_mcdb);
                                                jobjforsave.put("lgd_gp_code", "");
                                                jobjforsave.put("gp_name", gp_vc_warddb.equals("") ? JSONObject.NULL : gp_vc_warddb);
                                                jobjforsave.put("last_mile_addresstext", "");
                                                jobjforsave.put("uploadbatchid", batch_id);
                                                jobjforsave.put("postrcvalidation_fullname", fullnameapi.equals("") ? JSONObject.NULL : fullnameapi);
                                                jobjforsave.put("postrcvalidation_duplicate_in_rc", duplicateapi.equals("") ? JSONObject.NULL : duplicateapi);
                                                jobjforsave.put("postrcvalidation_missing_in_rc", missing_in_rcapi.equals("") ? JSONObject.NULL : missing_in_rcapi);
                                                jobjforsave.put("postrcvalidation_rc_member_id", rc_member_idapi.equals("") ? JSONObject.NULL : rc_member_idapi);
                                                jobjforsave.put("postrcvalidation_outputtext", JSONObject.NULL);
                                                jobjforsave.put("scheme_name", scheme_name.equals("") ? JSONObject.NULL : scheme_name);
                                                jobjforsave.put("dept_id", dept_id);
                                                jobjforsave.put("dept_name", dept_name.equals("") ? JSONObject.NULL : dept_name);
                                                jobjforsave.put("benefit_type_cash_kind", JSONObject.NULL);
                                                jobjforsave.put("installment_name", installment_name.equals("") ? JSONObject.NULL : installment_name);
                                                jobjforsave.put("postrcdetails_bymemberid_outputtext", JSONObject.NULL);
                                                jobjforsave.put("isapproved_readyfordashboard", JSONObject.NULL);
                                                jobjforsave.put("approved_by", JSONObject.NULL);
                                                jobjforsave.put("approved_datetime", JSONObject.NULL);
                                                jobjforsave.put("benef_name_matchresult_dist_leven", distleven);
                                                jobjforsave.put("matched_by", JSONObject.NULL);
                                                jobjforsave.put("matched_datetime", JSONObject.NULL);
                                                jobjforsave.put("postrcdetails_byname_outputtext", JSONObject.NULL);
                                                jobjforsave.put("uploaded_by", JSONObject.NULL);
                                                jobjforsave.put("uploaded_datetime", JSONObject.NULL);
                                                jobjforsave.put("fps_id", fps_idapi.equals("") ? JSONObject.NULL : fps_idapi);
                                                jobjforsave.put("fps_lat", fps_latapi.equals("") ? JSONObject.NULL : fps_latapi);
                                                jobjforsave.put("fps_long", fps_longapi.equals("") ? JSONObject.NULL : fps_longapi);
                                                jobjforsave.put("postrcvalidation_distlgdcode", postrcvalidation_distlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdcodeapi);
                                                jobjforsave.put("postrcvalidation_distlgdname", postrcvalidation_distlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdnameapi);
                                                jobjforsave.put("postrcvalidation_subdivlgdcode", postrcvalidation_subdivlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdcodeapi);
                                                jobjforsave.put("postrcvalidation_subdivlgdname", postrcvalidation_subdivlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdnameapi);
                                                jobjforsave.put("postrcvalidation_blocklgdcode", postrcvalidation_blocklgdcode.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdcode);
                                                jobjforsave.put("postrcvalidation_blocklgdname", postrcvalidation_blocklgdname.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdname);
                                                jobjforsave.put("postrcvalidation_gplgdcode", postrcvalidation_gplgdcode.equals("") ? JSONObject.NULL : postrcvalidation_gplgdcode);
                                                jobjforsave.put("postrcvalidation_gplgdname", postrcvalidation_gplgdname.equals("") ? JSONObject.NULL : postrcvalidation_gplgdname);
                                                jobjforsave.put("postrcvalidation_hof_rc_member_id", postrcvalidation_hof_rc_member_id.equals("") ? JSONObject.NULL : postrcvalidation_hof_rc_member_id);
                                                jobjforsave.put("postrcvalidation_hofname", postrcvalidation_hofname.equals("") ? JSONObject.NULL : postrcvalidation_hofname);
                                                jobjforsave.put("nearest_lat_long_desc", nearest_lat_long_desc.equals("") ? JSONObject.NULL : nearest_lat_long_desc);
                                                jobjforsave.put("is_disapproved", JSONObject.NULL);
                                                jobjforsave.put("transaction_datasentbyinsert_update", JSONObject.NULL);
                                                jobjforsave.put("postrcvalidation_rc_no", rc_noapi.equals("") ? JSONObject.NULL : rc_noapi);
                                                jobjforsave.put("component_name", JSONObject.NULL);
                                                jobjforsave.put("is_duplicate", JSONObject.NULL);
                                                jobjforsave.put("entryby", user_id);
                                                jobjforsave.put("instalment", instalmentdb);
                                                jobjforsave.put("source_code", 1);
                                                jobjforsave.put("office_id", office_id);
                                                if (isvalidflag) {
                                                    String tabNamedata = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                                                    try {
                                                        int x = 0;
                                                        if (conn == null || conn.isClosed()) {
                                                            //  conn = obj.getConnection();
                                                            //conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                                            conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
                                                            ////////////////////   
//                                                            String dsName = "java:/comp/env/bms";
//                                                            try {
//                                                                InitialContext ctx;
//
//                                                                ctx = new InitialContext();
//                                                                DataSource ds = (DataSource) ctx.lookup(dsName);
//                                                                if (ds == null) {
//                                                                    //throw new Exception("Data source not found!");
//                                                                    //int i=0;
//
//                                                                    System.out.print("Data source not found!");
//                                                                }
//                                                                try {
//                                                                    conn = ds.getConnection();
//                                                                } catch (SQLException ex) {
//                                                                    ex.printStackTrace();
//                                                                }
//                                                            } catch (NamingException ex) {
//                                                                ex.printStackTrace();
//                                                            }

                                                            //////////////////////////////   
                                                        }
                                                        cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_data(?,?,?,?::json)}");
                                                        cs.setInt(++x, _sln);
                                                        cs.setString(++x, tabName);
                                                        cs.setString(++x, tabNamedata);
                                                        cs.setString(++x, jobjforsave.toString());

                                                        cs.execute();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                                                        handler.flush();

                                                    }
                                                } else {
                                                    String tabNamedatatemp = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
                                                    try {
                                                        int x = 0;
                                                        if (conn == null || conn.isClosed()) {
                                                            conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
                                                        }
                                                        cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_datatemp(?,?,?,?::json)}");
                                                        cs.setInt(++x, _sln);
                                                        cs.setString(++x, tabName);
                                                        cs.setString(++x, tabNamedatatemp);
                                                        cs.setString(++x, jobjforsave.toString());

                                                        //cs.registerOutParameter(4, Types.VARCHAR);
                                                        System.out.println(cs.toString());
                                                        cs.execute();
                                                        //duplicatejsonstring = cs.getString(3);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                                                        handler.flush();
                                                    }
                                                }
                                                cs.close();
                                            }
                                        }

                                    } catch (MalformedURLException ex) {
                                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                        logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                        handler.flush();
                                    } catch (ProtocolException ex) {
                                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                        logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                        handler.flush();
                                    } catch (IOException ex) {
                                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                        logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                        handler.flush();
                                    }

                                }

                            } catch (Exception ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            }
                            TimeUnit.SECONDS.sleep(1);
                        }
                    }
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    handler.flush();
                    throw new IllegalStateException(e);

                } catch (IOException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } catch (SecurityException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {

                        }
                    }
                }
                System.out.println("I'll run in a separate thread than the main thread.");

            }

        }
        );

        //handler.close();
    }

    public void ApiCallableFutureFailedCases(String tabName, int schemeId, String schemeCode, String fy, int installment,
            int batch_id, int dept_id, String dept_name, String installment_name, String user_id) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                boolean append = true;

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                ResourceBundle resource = ResourceBundle.getBundle("Common/config");
                String filename = resource.getString("bms.apicall.log") + "/error_" + java.time.LocalDate.now() + ".log";

                CommonMethod cm = new CommonMethod();
                Security.AES aes = new Security.AES();
                Connection conn = null;
                //dbcon obj = new dbcon();
                CallableStatement cs = null;
                ResultSet rs = null;

                Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");

                SimpleFormatter formatter = new SimpleFormatter();

                try {
                    /* Call API */
                    handler = new FileHandler(filename, append);
                    logger.addHandler(handler);

                    handler.setFormatter(formatter);
                    String slnosjsonnstring = "";

                    //System.out.println(i);
                    try {
                        String query_set = "", beneficiaryname = "", uid = "", rcnumber = "", accountnumber = "", ifscdb = "", districtnamedb = "", subdivisionnamedb = "", block_mcdb = "", gp_vc_warddb = "";
                        int instalmentdb = 0, office_id = 0;
                        String finalret = "";
                        int _sln = 0;
                        HttpURLConnection urlConn = null;
                        OutputStream ouputStream = null;
                        BufferedReader reader1 = null;

                        int perc = 0;
                        int distleven = 1000;
                        String ready_toapprove = "N";
                        String approvedby = "", auto_approval_data = "";
                        String strapiresponselog = "", dob = "", guardians_name = "", rc_mem = "", bank_name = "", scheme_name = "";
                        ResultSet rs2 = null;
                        PreparedStatement pst = null;
                        //int slno = slnoarr.getJSONObject(i).getInt("slno");
                        String sql = "SELECT slno,coalesce(beneficiaryname,'') AS beneficiaryname,coalesce(uid,'') AS uid,coalesce(rcnumber,'') AS rcnumber, coalesce(accountnumber,'') AS accountnumber, "
                                + "coalesce(ifsc,'') AS ifsc, coalesce(districtname,'') AS districtname, coalesce(subdivisionname,'') AS subdivisionname, coalesce(block_mc,'') AS block_mc, "
                                + "coalesce(gp_vc_ward,'') AS gp_vc_ward, office_id FROM beneficiary." + tabName + " WHERE isapicall IS NULL ";

                        if (conn == null || conn.isClosed()) {
                            //Fix Start
                            Thread l_thread = Thread.currentThread();
                            l_thread.setContextClassLoader(this.getClass().getClassLoader());
                            //Fix End

                            conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
//                                    System.out.print("connection null");
//                                    String dsName = "java:/comp/env/bms";
//                                    try {
//                                        InitialContext ctx;
//
//                                        ctx = new InitialContext();
//                                        DataSource ds = (DataSource) ctx.lookup(dsName);
//                                        if (ds == null) {
//                                            System.out.print("Data source not found!");
//                                        }
//                                        try {
//                                            conn = ds.getConnection();
//                                        } catch (SQLException ex) {
//                                            ex.printStackTrace();
//                                        }
//                                    } catch (NamingException ex) {
//                                        ex.printStackTrace();
//                                    }
                        } else {
                            System.out.println(conn.getClientInfo().elements().toString());
                        }
                        //System.out.print(sql);
                        pst = conn.prepareStatement(sql);
                        rs2 = pst.executeQuery();
                        while (rs2.next()) {
                            try {
                                _sln = rs2.getInt("slno");
                                beneficiaryname = rs2.getString("beneficiaryname").trim();
                                uid = rs2.getString("uid").trim();
                                rcnumber = rs2.getString("rcnumber").trim();
                                accountnumber = rs2.getString("accountnumber").trim();
                                ifscdb = rs2.getString("ifsc").trim();
                                districtnamedb = rs2.getString("districtname").trim();
                                subdivisionnamedb = rs2.getString("subdivisionname").trim();
                                block_mcdb = rs2.getString("block_mc").trim();
                                gp_vc_warddb = rs2.getString("gp_vc_ward").trim();
                                office_id = Integer.parseInt(rs2.getString("office_id").trim());

                                String basurl = "https://bms.tripura.gov.in/ssotapi/webresources/services";
                                //String basurl = "https://bmsuat.trsc.nic.in/ssotapi/webresources/services";
                                //String basurl = "http://127.0.0.1:8080/ssotapi/webresources/services";

                                String url1 = basurl + "/getMemberDetailsByUidOrRcNoName?uid=" + URLEncoder.encode(uid, "UTF-8") + "&rcNo=" + URLEncoder.encode(rcnumber, "UTF-8") + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");

                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.INFO, "Requesting  URL: " + url1, "");

                                String datainput = "uid=" + uid + "&rcNo=" + rcnumber + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");
                                URL urlObj = new URL(url1);
                                urlConn = (HttpURLConnection) urlObj.openConnection();
                                urlConn.setDoOutput(true);
                                urlConn.setRequestMethod("POST");

                                urlConn.setRequestProperty("Content-Type", "application/json");
                                //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                urlConn.setConnectTimeout(600000);
                                urlConn.setReadTimeout(600000);

                                urlConn.setRequestProperty("Accept", "application/json");

                                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                    System.err.println("Unable to connect to the URL " + url1 + ", Error Code: " + urlConn.getResponseCode());

                                } else {
                                    InputStream is = urlConn.getInputStream();
                                    reader1 = new BufferedReader(new InputStreamReader((is)));
                                    String tmpStr = null;
                                    while ((tmpStr = reader1.readLine()) != null) {

                                        finalret += tmpStr;
                                    }

                                    String fullnameapi = "", duplicateapi = "", rc_member_idapi = "", missing_in_rcapi = "", genderapi = "", casteapi = "", pan = "", rc_noapi = "", last_mile_addressapi = "", fps_idapi = "", fps_latapi = "", fps_longapi = "", postrcvalidation_distlgdcodeapi = "", postrcvalidation_distlgdnameapi = "", postrcvalidation_subdivlgdcodeapi = "", postrcvalidation_subdivlgdnameapi = "";
                                    String postrcvalidation_blocklgdcode = "", postrcvalidation_blocklgdname = "", postrcvalidation_gplgdcode = "", postrcvalidation_gplgdname = "", postrcvalidation_hof_rc_member_id = "", postrcvalidation_hofname = "", nearest_lat_long_desc = "";
                                    String father_name = "";
                                    JSONObject jsonObj = new JSONObject(finalret);

                                    JSONArray details = new JSONArray(jsonObj.get("beneficiarydetails").toString());
                                    for (int j = 0; j < details.length(); j++) {

                                        boolean isvalidflag = false;
                                        JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());

                                        String strnodata = "";
                                        try {
                                            strnodata = tmpOBJ.get("message").toString().trim();
                                        } catch (JSONException ex) {
                                        }
                                        if (strnodata.equalsIgnoreCase(" No Data Found".trim())) {
                                            isvalidflag = false;
                                        } else {
                                            fullnameapi = tmpOBJ.optString("fullname").toString();
                                            duplicateapi = tmpOBJ.optString("duplicate_in_rc").toString();
                                            rc_member_idapi = tmpOBJ.optString("ration_card_memberid").toString();
                                            missing_in_rcapi = tmpOBJ.optString("missing_in_rc").toString();

                                            genderapi = tmpOBJ.optString("gender").toString();
                                            casteapi = tmpOBJ.optString("caste").toString();
                                            rc_noapi = tmpOBJ.optString("ration_card_no").toString();
                                            last_mile_addressapi = tmpOBJ.optString("landmark").toString();

                                            fps_idapi = tmpOBJ.optString("fps_id").toString();
                                            fps_latapi = tmpOBJ.optString("fps_lat").toString();
                                            fps_longapi = tmpOBJ.optString("fps_long").toString();
                                            postrcvalidation_distlgdcodeapi = tmpOBJ.optString("lgd_district_code").toString();
                                            postrcvalidation_distlgdnameapi = tmpOBJ.optString("lgd_district_name").toString();
                                            postrcvalidation_subdivlgdcodeapi = tmpOBJ.optString("lgd_subdiv_code").toString();
                                            postrcvalidation_subdivlgdnameapi = tmpOBJ.optString("lgd_subdiv_name").toString();
                                            postrcvalidation_blocklgdcode = tmpOBJ.optString("lgd_block_code").toString();
                                            postrcvalidation_blocklgdname = tmpOBJ.get("lgd_block_name").toString();

                                            postrcvalidation_gplgdcode = tmpOBJ.optString("lgd_gp_code").toString();
                                            postrcvalidation_gplgdname = tmpOBJ.optString("lgd_gp_name").toString();

                                            postrcvalidation_hof_rc_member_id = tmpOBJ.optString("hof_member_id").toString();
                                            postrcvalidation_hofname = tmpOBJ.optString("hof_name").toString();
                                            nearest_lat_long_desc = tmpOBJ.optString("nearest_lat_long_desc").toString();

                                            String parameter_missing = tmpOBJ.optString("parameter_missing").toString();
                                            String invalid_rc_no = tmpOBJ.optString("invalid_rc_no").toString();
                                            distleven = Integer.parseInt(tmpOBJ.optString("levenshtein_dst").equals("") ? "1000" : tmpOBJ.get("levenshtein_dst").toString());
                                            father_name = tmpOBJ.optString("father_name");
                                            dob = tmpOBJ.optString("member_dob");

                                            //optString
                                            //missing_in_rc,duplicate_in_rc,parameter_missing,invalid_rc_no,levenshtein_dst
                                            if (missing_in_rcapi.equalsIgnoreCase("false")
                                                    && duplicateapi.equalsIgnoreCase("false")
                                                    && parameter_missing.equalsIgnoreCase("false")
                                                    && invalid_rc_no.equalsIgnoreCase("false")
                                                    && distleven == 0) {
                                                isvalidflag = true;
                                            } else {
                                                isvalidflag = false;
                                            }
                                        }
                                        JSONObject jobjforsave = new JSONObject();
                                        jobjforsave.put("scheme_id", schemeId);
                                        jobjforsave.put("scheme_code", schemeCode.equals("") ? JSONObject.NULL : schemeCode);
                                        jobjforsave.put("fy", fy.equals("") ? JSONObject.NULL : fy);
                                        jobjforsave.put("installment", installment);
                                        jobjforsave.put("full_name", beneficiaryname.equals("") ? JSONObject.NULL : beneficiaryname);
                                        jobjforsave.put("father_name", father_name.equals("") ? JSONObject.NULL : father_name);
                                        jobjforsave.put("guardians_name", guardians_name.equals("") ? JSONObject.NULL : guardians_name);
                                        jobjforsave.put("gender", genderapi.equals("") ? JSONObject.NULL : genderapi);
                                        jobjforsave.put("dob", dob.equals("") ? JSONObject.NULL : dob);
                                        jobjforsave.put("caste", casteapi.equals("") ? JSONObject.NULL : casteapi);
                                        jobjforsave.put("pid", uid.equals("") ? JSONObject.NULL : uid);
                                        jobjforsave.put("pan", pan.equals("") ? JSONObject.NULL : pan);
                                        jobjforsave.put("rc_no", rcnumber.equals("") ? JSONObject.NULL : rcnumber);
                                        jobjforsave.put("rc_mem", rc_mem.equals("") ? JSONObject.NULL : rc_mem);
                                        jobjforsave.put("bank_name", bank_name.equals("") ? JSONObject.NULL : bank_name);
                                        jobjforsave.put("ifsc_code", ifscdb.equals("") ? JSONObject.NULL : ifscdb);
                                        jobjforsave.put("bank_account_number", accountnumber.equals("") ? JSONObject.NULL : accountnumber);
                                        jobjforsave.put("amount", JSONObject.NULL);
                                        jobjforsave.put("lgd_dist_code", JSONObject.NULL);
                                        jobjforsave.put("district_name", districtnamedb.equals("") ? JSONObject.NULL : districtnamedb);
                                        jobjforsave.put("lgd_subdiv_code", JSONObject.NULL);
                                        jobjforsave.put("subdivision_name", subdivisionnamedb.equals("") ? JSONObject.NULL : subdivisionnamedb);
                                        jobjforsave.put("lgd_block_code", JSONObject.NULL);
                                        jobjforsave.put("block_name", block_mcdb.equals("") ? JSONObject.NULL : block_mcdb);
                                        jobjforsave.put("lgd_gp_code", "");
                                        jobjforsave.put("gp_name", gp_vc_warddb.equals("") ? JSONObject.NULL : gp_vc_warddb);
                                        jobjforsave.put("last_mile_addresstext", "");
                                        jobjforsave.put("uploadbatchid", batch_id);
                                        jobjforsave.put("postrcvalidation_fullname", fullnameapi.equals("") ? JSONObject.NULL : fullnameapi);
                                        jobjforsave.put("postrcvalidation_duplicate_in_rc", duplicateapi.equals("") ? JSONObject.NULL : duplicateapi);
                                        jobjforsave.put("postrcvalidation_missing_in_rc", missing_in_rcapi.equals("") ? JSONObject.NULL : missing_in_rcapi);
                                        jobjforsave.put("postrcvalidation_rc_member_id", rc_member_idapi.equals("") ? JSONObject.NULL : rc_member_idapi);
                                        jobjforsave.put("postrcvalidation_outputtext", JSONObject.NULL);
                                        jobjforsave.put("scheme_name", scheme_name.equals("") ? JSONObject.NULL : scheme_name);
                                        jobjforsave.put("dept_id", dept_id);
                                        jobjforsave.put("dept_name", dept_name.equals("") ? JSONObject.NULL : dept_name);
                                        jobjforsave.put("benefit_type_cash_kind", JSONObject.NULL);
                                        jobjforsave.put("installment_name", installment_name.equals("") ? JSONObject.NULL : installment_name);
                                        jobjforsave.put("postrcdetails_bymemberid_outputtext", JSONObject.NULL);
                                        jobjforsave.put("isapproved_readyfordashboard", JSONObject.NULL);
                                        jobjforsave.put("approved_by", JSONObject.NULL);
                                        jobjforsave.put("approved_datetime", JSONObject.NULL);
                                        jobjforsave.put("benef_name_matchresult_dist_leven", distleven);
                                        jobjforsave.put("matched_by", JSONObject.NULL);
                                        jobjforsave.put("matched_datetime", JSONObject.NULL);
                                        jobjforsave.put("postrcdetails_byname_outputtext", JSONObject.NULL);
                                        jobjforsave.put("uploaded_by", JSONObject.NULL);
                                        jobjforsave.put("uploaded_datetime", JSONObject.NULL);
                                        jobjforsave.put("fps_id", fps_idapi.equals("") ? JSONObject.NULL : fps_idapi);
                                        jobjforsave.put("fps_lat", fps_latapi.equals("") ? JSONObject.NULL : fps_latapi);
                                        jobjforsave.put("fps_long", fps_longapi.equals("") ? JSONObject.NULL : fps_longapi);
                                        jobjforsave.put("postrcvalidation_distlgdcode", postrcvalidation_distlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdcodeapi);
                                        jobjforsave.put("postrcvalidation_distlgdname", postrcvalidation_distlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdnameapi);
                                        jobjforsave.put("postrcvalidation_subdivlgdcode", postrcvalidation_subdivlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdcodeapi);
                                        jobjforsave.put("postrcvalidation_subdivlgdname", postrcvalidation_subdivlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdnameapi);
                                        jobjforsave.put("postrcvalidation_blocklgdcode", postrcvalidation_blocklgdcode.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdcode);
                                        jobjforsave.put("postrcvalidation_blocklgdname", postrcvalidation_blocklgdname.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdname);
                                        jobjforsave.put("postrcvalidation_gplgdcode", postrcvalidation_gplgdcode.equals("") ? JSONObject.NULL : postrcvalidation_gplgdcode);
                                        jobjforsave.put("postrcvalidation_gplgdname", postrcvalidation_gplgdname.equals("") ? JSONObject.NULL : postrcvalidation_gplgdname);
                                        jobjforsave.put("postrcvalidation_hof_rc_member_id", postrcvalidation_hof_rc_member_id.equals("") ? JSONObject.NULL : postrcvalidation_hof_rc_member_id);
                                        jobjforsave.put("postrcvalidation_hofname", postrcvalidation_hofname.equals("") ? JSONObject.NULL : postrcvalidation_hofname);
                                        jobjforsave.put("nearest_lat_long_desc", nearest_lat_long_desc.equals("") ? JSONObject.NULL : nearest_lat_long_desc);
                                        jobjforsave.put("is_disapproved", JSONObject.NULL);
                                        jobjforsave.put("transaction_datasentbyinsert_update", JSONObject.NULL);
                                        jobjforsave.put("postrcvalidation_rc_no", rc_noapi.equals("") ? JSONObject.NULL : rc_noapi);
                                        jobjforsave.put("component_name", JSONObject.NULL);
                                        jobjforsave.put("is_duplicate", JSONObject.NULL);
                                        jobjforsave.put("entryby", user_id);
                                        jobjforsave.put("instalment", instalmentdb);
                                        jobjforsave.put("source_code", 1);
                                        jobjforsave.put("office_id", office_id);
                                        if (isvalidflag) {
                                            String tabNamedata = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                                            try {
                                                int x = 0;
                                                if (conn == null || conn.isClosed()) {
                                                    //  conn = obj.getConnection();
                                                    //conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                                    conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
                                                    ////////////////////   
//                                                            String dsName = "java:/comp/env/bms";
//                                                            try {
//                                                                InitialContext ctx;
//
//                                                                ctx = new InitialContext();
//                                                                DataSource ds = (DataSource) ctx.lookup(dsName);
//                                                                if (ds == null) {
//                                                                    //throw new Exception("Data source not found!");
//                                                                    //int i=0;
//
//                                                                    System.out.print("Data source not found!");
//                                                                }
//                                                                try {
//                                                                    conn = ds.getConnection();
//                                                                } catch (SQLException ex) {
//                                                                    ex.printStackTrace();
//                                                                }
//                                                            } catch (NamingException ex) {
//                                                                ex.printStackTrace();
//                                                            }

                                                    //////////////////////////////   
                                                }
                                                cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_data(?,?,?,?::json)}");
                                                cs.setInt(++x, _sln);
                                                cs.setString(++x, tabName);
                                                cs.setString(++x, tabNamedata);
                                                cs.setString(++x, jobjforsave.toString());

                                                cs.execute();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                                                handler.flush();

                                            }
                                        } else {
                                            String tabNamedatatemp = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
                                            try {
                                                int x = 0;
                                                if (conn == null || conn.isClosed()) {
                                                    conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
                                                }
                                                cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_datatemp(?,?,?,?::json)}");
                                                cs.setInt(++x, _sln);
                                                cs.setString(++x, tabName);
                                                cs.setString(++x, tabNamedatatemp);
                                                cs.setString(++x, jobjforsave.toString());

                                                //cs.registerOutParameter(4, Types.VARCHAR);
                                                System.out.println(cs.toString());
                                                cs.execute();
                                                //duplicatejsonstring = cs.getString(3);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                                                handler.flush();
                                            }
                                        }
                                        cs.close();
                                    }
                                }

                            } catch (MalformedURLException ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            } catch (ProtocolException ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            } catch (IOException ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            }

                        }

                    } catch (Exception ex) {
                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                        logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                        handler.flush();
                    }
                    TimeUnit.SECONDS.sleep(1);

                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    handler.flush();
                    throw new IllegalStateException(e);

                } catch (IOException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } catch (SecurityException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {

                        }
                    }
                }
                System.out.println("I'll run in a separate thread than the main thread.");

            }

        }
        );

        //handler.close();
    }

    public void ApiCallableFutureFailedCasesOfficeWise(String tabName, int schemeId, String schemeCode, String fy, int installment,
            int batch_id, int dept_id, String dept_name, String installment_name, String user_id, int office_Id) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                boolean append = true;

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                ResourceBundle resource = ResourceBundle.getBundle("Common/config");
                String filename = resource.getString("bms.apicall.log") + "/error_" + java.time.LocalDate.now() + ".log";

                CommonMethod cm = new CommonMethod();
                Security.AES aes = new Security.AES();
                Connection conn = null;
                //dbcon obj = new dbcon();
                CallableStatement cs = null;
                ResultSet rs = null;

                Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");

                SimpleFormatter formatter = new SimpleFormatter();

                try {
                    /* Call API */
                    handler = new FileHandler(filename, append);
                    logger.addHandler(handler);

                    handler.setFormatter(formatter);
                    String slnosjsonnstring = "";

                    //System.out.println(i);
                    try {
                        String query_set = "", beneficiaryname = "", uid = "", rcnumber = "", accountnumber = "", ifscdb = "", districtnamedb = "", subdivisionnamedb = "", block_mcdb = "", gp_vc_warddb = "";
                        int instalmentdb = 0;
                        String finalret = "";
                        int _sln = 0;
                        HttpURLConnection urlConn = null;
                        OutputStream ouputStream = null;
                        BufferedReader reader1 = null;

                        int perc = 0;
                        int distleven = 1000;
                        String ready_toapprove = "N";
                        String approvedby = "", auto_approval_data = "";
                        String strapiresponselog = "", dob = "", guardians_name = "", rc_mem = "", bank_name = "", scheme_name = "";
                        ResultSet rs2 = null;
                        PreparedStatement pst = null;
                        //int slno = slnoarr.getJSONObject(i).getInt("slno");
                        String sql = "SELECT slno,coalesce(beneficiaryname,'') AS beneficiaryname,coalesce(uid,'') AS uid,coalesce(rcnumber,'') AS rcnumber, coalesce(accountnumber,'') AS accountnumber, "
                                + "coalesce(ifsc,'') AS ifsc, coalesce(districtname,'') AS districtname, coalesce(subdivisionname,'') AS subdivisionname, coalesce(block_mc,'') AS block_mc, "
                                + "coalesce(gp_vc_ward,'') AS gp_vc_ward FROM beneficiary." + tabName + " WHERE office_id = " + office_Id + " AND isapicall IS NULL ";

                        if (conn == null || conn.isClosed()) {
                            //Fix Start
                            Thread l_thread = Thread.currentThread();
                            l_thread.setContextClassLoader(this.getClass().getClassLoader());
                            //Fix End

                            conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
//                                    System.out.print("connection null");
//                                    String dsName = "java:/comp/env/bms";
//                                    try {
//                                        InitialContext ctx;
//
//                                        ctx = new InitialContext();
//                                        DataSource ds = (DataSource) ctx.lookup(dsName);
//                                        if (ds == null) {
//                                            System.out.print("Data source not found!");
//                                        }
//                                        try {
//                                            conn = ds.getConnection();
//                                        } catch (SQLException ex) {
//                                            ex.printStackTrace();
//                                        }
//                                    } catch (NamingException ex) {
//                                        ex.printStackTrace();
//                                    }
                        } else {
                            System.out.println(conn.getClientInfo().elements().toString());
                        }
                        //System.out.print(sql);
                        pst = conn.prepareStatement(sql);
                        rs2 = pst.executeQuery();
                        while (rs2.next()) {
                            try {
                                _sln = rs2.getInt("slno");
                                beneficiaryname = rs2.getString("beneficiaryname").trim();
                                uid = rs2.getString("uid").trim();
                                rcnumber = rs2.getString("rcnumber").trim();
                                accountnumber = rs2.getString("accountnumber").trim();
                                ifscdb = rs2.getString("ifsc").trim();
                                districtnamedb = rs2.getString("districtname").trim();
                                subdivisionnamedb = rs2.getString("subdivisionname").trim();
                                block_mcdb = rs2.getString("block_mc").trim();
                                gp_vc_warddb = rs2.getString("gp_vc_ward").trim();

                                String basurl = "https://bms.tripura.gov.in/ssotapi/webresources/services";
                                //String basurl = "https://bmsuat.trsc.nic.in/ssotapi/webresources/services";
                                //String basurl = "http://127.0.0.1:8080/ssotapi/webresources/services";

                                String url1 = basurl + "/getMemberDetailsByUidOrRcNoName?uid=" + URLEncoder.encode(uid, "UTF-8") + "&rcNo=" + URLEncoder.encode(rcnumber, "UTF-8") + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");

                                System.out.print(url1);

                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.INFO, "Requesting  URL: " + url1, "");

                                String datainput = "uid=" + uid + "&rcNo=" + rcnumber + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");
                                URL urlObj = new URL(url1);
                                urlConn = (HttpURLConnection) urlObj.openConnection();
                                urlConn.setDoOutput(true);
                                urlConn.setRequestMethod("POST");

                                urlConn.setRequestProperty("Content-Type", "application/json");
                                //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                urlConn.setConnectTimeout(600000);
                                urlConn.setReadTimeout(600000);

                                urlConn.setRequestProperty("Accept", "application/json");

                                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                    System.err.println("Unable to connect to the URL " + url1 + ", Error Code: " + urlConn.getResponseCode());

                                } else {
                                    InputStream is = urlConn.getInputStream();
                                    reader1 = new BufferedReader(new InputStreamReader((is)));
                                    String tmpStr = null;
                                    while ((tmpStr = reader1.readLine()) != null) {

                                        finalret += tmpStr;
                                    }

                                    String fullnameapi = "", duplicateapi = "", rc_member_idapi = "", missing_in_rcapi = "", genderapi = "", casteapi = "", pan = "", rc_noapi = "", last_mile_addressapi = "", fps_idapi = "", fps_latapi = "", fps_longapi = "", postrcvalidation_distlgdcodeapi = "", postrcvalidation_distlgdnameapi = "", postrcvalidation_subdivlgdcodeapi = "", postrcvalidation_subdivlgdnameapi = "";
                                    String postrcvalidation_blocklgdcode = "", postrcvalidation_blocklgdname = "", postrcvalidation_gplgdcode = "", postrcvalidation_gplgdname = "", postrcvalidation_hof_rc_member_id = "", postrcvalidation_hofname = "", nearest_lat_long_desc = "";
                                    String father_name = "";
                                    JSONObject jsonObj = new JSONObject(finalret);

                                    JSONArray details = new JSONArray(jsonObj.get("beneficiarydetails").toString());
                                    for (int j = 0; j < details.length(); j++) {

                                        boolean isvalidflag = false;
                                        JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());

                                        String strnodata = "";
                                        try {
                                            strnodata = tmpOBJ.get("message").toString().trim();
                                        } catch (JSONException ex) {
                                        }
                                        if (strnodata.equalsIgnoreCase(" No Data Found".trim())) {
                                            isvalidflag = false;
                                        } else {
                                            fullnameapi = tmpOBJ.optString("fullname").toString();
                                            duplicateapi = tmpOBJ.optString("duplicate_in_rc").toString();
                                            rc_member_idapi = tmpOBJ.optString("ration_card_memberid").toString();
                                            missing_in_rcapi = tmpOBJ.optString("missing_in_rc").toString();

                                            genderapi = tmpOBJ.optString("gender").toString();
                                            casteapi = tmpOBJ.optString("caste").toString();
                                            rc_noapi = tmpOBJ.optString("ration_card_no").toString();
                                            last_mile_addressapi = tmpOBJ.optString("landmark").toString();

                                            fps_idapi = tmpOBJ.optString("fps_id").toString();
                                            fps_latapi = tmpOBJ.optString("fps_lat").toString();
                                            fps_longapi = tmpOBJ.optString("fps_long").toString();
                                            postrcvalidation_distlgdcodeapi = tmpOBJ.optString("lgd_district_code").toString();
                                            postrcvalidation_distlgdnameapi = tmpOBJ.optString("lgd_district_name").toString();
                                            postrcvalidation_subdivlgdcodeapi = tmpOBJ.optString("lgd_subdiv_code").toString();
                                            postrcvalidation_subdivlgdnameapi = tmpOBJ.optString("lgd_subdiv_name").toString();
                                            postrcvalidation_blocklgdcode = tmpOBJ.optString("lgd_block_code").toString();
                                            postrcvalidation_blocklgdname = tmpOBJ.get("lgd_block_name").toString();

                                            postrcvalidation_gplgdcode = tmpOBJ.optString("lgd_gp_code").toString();
                                            postrcvalidation_gplgdname = tmpOBJ.optString("lgd_gp_name").toString();

                                            postrcvalidation_hof_rc_member_id = tmpOBJ.optString("hof_member_id").toString();
                                            postrcvalidation_hofname = tmpOBJ.optString("hof_name").toString();
                                            nearest_lat_long_desc = tmpOBJ.optString("nearest_lat_long_desc").toString();

                                            String parameter_missing = tmpOBJ.optString("parameter_missing").toString();
                                            String invalid_rc_no = tmpOBJ.optString("invalid_rc_no").toString();
                                            distleven = Integer.parseInt(tmpOBJ.optString("levenshtein_dst").equals("") ? "1000" : tmpOBJ.get("levenshtein_dst").toString());
                                            father_name = tmpOBJ.optString("father_name");
                                            dob = tmpOBJ.optString("member_dob");

                                            //optString
                                            //missing_in_rc,duplicate_in_rc,parameter_missing,invalid_rc_no,levenshtein_dst
                                            if (missing_in_rcapi.equalsIgnoreCase("false")
                                                    && duplicateapi.equalsIgnoreCase("false")
                                                    && parameter_missing.equalsIgnoreCase("false")
                                                    && invalid_rc_no.equalsIgnoreCase("false")
                                                    && distleven == 0) {
                                                isvalidflag = true;
                                            } else {
                                                isvalidflag = false;
                                            }
                                        }
                                        JSONObject jobjforsave = new JSONObject();
                                        jobjforsave.put("scheme_id", schemeId);
                                        jobjforsave.put("scheme_code", schemeCode.equals("") ? JSONObject.NULL : schemeCode);
                                        jobjforsave.put("fy", fy.equals("") ? JSONObject.NULL : fy);
                                        jobjforsave.put("installment", installment);
                                        jobjforsave.put("full_name", beneficiaryname.equals("") ? JSONObject.NULL : beneficiaryname);
                                        jobjforsave.put("father_name", father_name.equals("") ? JSONObject.NULL : father_name);
                                        jobjforsave.put("guardians_name", guardians_name.equals("") ? JSONObject.NULL : guardians_name);
                                        jobjforsave.put("gender", genderapi.equals("") ? JSONObject.NULL : genderapi);
                                        jobjforsave.put("dob", dob.equals("") ? JSONObject.NULL : dob);
                                        jobjforsave.put("caste", casteapi.equals("") ? JSONObject.NULL : casteapi);
                                        jobjforsave.put("pid", uid.equals("") ? JSONObject.NULL : uid);
                                        jobjforsave.put("pan", pan.equals("") ? JSONObject.NULL : pan);
                                        jobjforsave.put("rc_no", rcnumber.equals("") ? JSONObject.NULL : rcnumber);
                                        jobjforsave.put("rc_mem", rc_mem.equals("") ? JSONObject.NULL : rc_mem);
                                        jobjforsave.put("bank_name", bank_name.equals("") ? JSONObject.NULL : bank_name);
                                        jobjforsave.put("ifsc_code", ifscdb.equals("") ? JSONObject.NULL : ifscdb);
                                        jobjforsave.put("bank_account_number", accountnumber.equals("") ? JSONObject.NULL : accountnumber);
                                        jobjforsave.put("amount", JSONObject.NULL);
                                        jobjforsave.put("lgd_dist_code", JSONObject.NULL);
                                        jobjforsave.put("district_name", districtnamedb.equals("") ? JSONObject.NULL : districtnamedb);
                                        jobjforsave.put("lgd_subdiv_code", JSONObject.NULL);
                                        jobjforsave.put("subdivision_name", subdivisionnamedb.equals("") ? JSONObject.NULL : subdivisionnamedb);
                                        jobjforsave.put("lgd_block_code", JSONObject.NULL);
                                        jobjforsave.put("block_name", block_mcdb.equals("") ? JSONObject.NULL : block_mcdb);
                                        jobjforsave.put("lgd_gp_code", "");
                                        jobjforsave.put("gp_name", gp_vc_warddb.equals("") ? JSONObject.NULL : gp_vc_warddb);
                                        jobjforsave.put("last_mile_addresstext", "");
                                        jobjforsave.put("uploadbatchid", batch_id);
                                        jobjforsave.put("postrcvalidation_fullname", fullnameapi.equals("") ? JSONObject.NULL : fullnameapi);
                                        jobjforsave.put("postrcvalidation_duplicate_in_rc", duplicateapi.equals("") ? JSONObject.NULL : duplicateapi);
                                        jobjforsave.put("postrcvalidation_missing_in_rc", missing_in_rcapi.equals("") ? JSONObject.NULL : missing_in_rcapi);
                                        jobjforsave.put("postrcvalidation_rc_member_id", rc_member_idapi.equals("") ? JSONObject.NULL : rc_member_idapi);
                                        jobjforsave.put("postrcvalidation_outputtext", JSONObject.NULL);
                                        jobjforsave.put("scheme_name", scheme_name.equals("") ? JSONObject.NULL : scheme_name);
                                        jobjforsave.put("dept_id", dept_id);
                                        jobjforsave.put("dept_name", dept_name.equals("") ? JSONObject.NULL : dept_name);
                                        jobjforsave.put("benefit_type_cash_kind", JSONObject.NULL);
                                        jobjforsave.put("installment_name", installment_name.equals("") ? JSONObject.NULL : installment_name);
                                        jobjforsave.put("postrcdetails_bymemberid_outputtext", JSONObject.NULL);
                                        jobjforsave.put("isapproved_readyfordashboard", JSONObject.NULL);
                                        jobjforsave.put("approved_by", JSONObject.NULL);
                                        jobjforsave.put("approved_datetime", JSONObject.NULL);
                                        jobjforsave.put("benef_name_matchresult_dist_leven", distleven);
                                        jobjforsave.put("matched_by", JSONObject.NULL);
                                        jobjforsave.put("matched_datetime", JSONObject.NULL);
                                        jobjforsave.put("postrcdetails_byname_outputtext", JSONObject.NULL);
                                        jobjforsave.put("uploaded_by", JSONObject.NULL);
                                        jobjforsave.put("uploaded_datetime", JSONObject.NULL);
                                        jobjforsave.put("fps_id", fps_idapi.equals("") ? JSONObject.NULL : fps_idapi);
                                        jobjforsave.put("fps_lat", fps_latapi.equals("") ? JSONObject.NULL : fps_latapi);
                                        jobjforsave.put("fps_long", fps_longapi.equals("") ? JSONObject.NULL : fps_longapi);
                                        jobjforsave.put("postrcvalidation_distlgdcode", postrcvalidation_distlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdcodeapi);
                                        jobjforsave.put("postrcvalidation_distlgdname", postrcvalidation_distlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdnameapi);
                                        jobjforsave.put("postrcvalidation_subdivlgdcode", postrcvalidation_subdivlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdcodeapi);
                                        jobjforsave.put("postrcvalidation_subdivlgdname", postrcvalidation_subdivlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdnameapi);
                                        jobjforsave.put("postrcvalidation_blocklgdcode", postrcvalidation_blocklgdcode.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdcode);
                                        jobjforsave.put("postrcvalidation_blocklgdname", postrcvalidation_blocklgdname.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdname);
                                        jobjforsave.put("postrcvalidation_gplgdcode", postrcvalidation_gplgdcode.equals("") ? JSONObject.NULL : postrcvalidation_gplgdcode);
                                        jobjforsave.put("postrcvalidation_gplgdname", postrcvalidation_gplgdname.equals("") ? JSONObject.NULL : postrcvalidation_gplgdname);
                                        jobjforsave.put("postrcvalidation_hof_rc_member_id", postrcvalidation_hof_rc_member_id.equals("") ? JSONObject.NULL : postrcvalidation_hof_rc_member_id);
                                        jobjforsave.put("postrcvalidation_hofname", postrcvalidation_hofname.equals("") ? JSONObject.NULL : postrcvalidation_hofname);
                                        jobjforsave.put("nearest_lat_long_desc", nearest_lat_long_desc.equals("") ? JSONObject.NULL : nearest_lat_long_desc);
                                        jobjforsave.put("is_disapproved", JSONObject.NULL);
                                        jobjforsave.put("transaction_datasentbyinsert_update", JSONObject.NULL);
                                        jobjforsave.put("postrcvalidation_rc_no", rc_noapi.equals("") ? JSONObject.NULL : rc_noapi);
                                        jobjforsave.put("component_name", JSONObject.NULL);
                                        jobjforsave.put("is_duplicate", JSONObject.NULL);
                                        jobjforsave.put("entryby", user_id);
                                        jobjforsave.put("instalment", instalmentdb);
                                        jobjforsave.put("source_code", 1);
                                        jobjforsave.put("office_id", office_Id);
                                        if (isvalidflag) {
                                            String tabNamedata = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                                            try {
                                                int x = 0;
                                                if (conn == null || conn.isClosed()) {
                                                    //  conn = obj.getConnection();
                                                    //conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                                    conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
                                                    ////////////////////   
//                                                            String dsName = "java:/comp/env/bms";
//                                                            try {
//                                                                InitialContext ctx;
//
//                                                                ctx = new InitialContext();
//                                                                DataSource ds = (DataSource) ctx.lookup(dsName);
//                                                                if (ds == null) {
//                                                                    //throw new Exception("Data source not found!");
//                                                                    //int i=0;
//
//                                                                    System.out.print("Data source not found!");
//                                                                }
//                                                                try {
//                                                                    conn = ds.getConnection();
//                                                                } catch (SQLException ex) {
//                                                                    ex.printStackTrace();
//                                                                }
//                                                            } catch (NamingException ex) {
//                                                                ex.printStackTrace();
//                                                            }

                                                    //////////////////////////////   
                                                }
                                                cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_data(?,?,?,?::json)}");
                                                cs.setInt(++x, _sln);
                                                cs.setString(++x, tabName);
                                                cs.setString(++x, tabNamedata);
                                                cs.setString(++x, jobjforsave.toString());

                                                cs.execute();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                                                handler.flush();

                                            }
                                        } else {
                                            String tabNamedatatemp = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
                                            try {
                                                int x = 0;
                                                if (conn == null || conn.isClosed()) {
                                                    conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();//retConnection();
                                                }
                                                cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_datatemp(?,?,?,?::json)}");
                                                cs.setInt(++x, _sln);
                                                cs.setString(++x, tabName);
                                                cs.setString(++x, tabNamedatatemp);
                                                cs.setString(++x, jobjforsave.toString());

                                                //cs.registerOutParameter(4, Types.VARCHAR);
                                                System.out.println(cs.toString());
                                                cs.execute();
                                                //duplicatejsonstring = cs.getString(3);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                                                handler.flush();
                                            }
                                        }
                                        cs.close();
                                    }
                                }

                            } catch (MalformedURLException ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            } catch (ProtocolException ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            } catch (IOException ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            }

                        }

                    } catch (Exception ex) {
                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                        logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                        handler.flush();
                    }
                    TimeUnit.SECONDS.sleep(1);

                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    handler.flush();
                    throw new IllegalStateException(e);

                } catch (IOException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } catch (SecurityException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {

                        }
                    }
                }
                System.out.println("I'll run in a separate thread than the main thread.");

            }

        }
        );

        //handler.close();
    }

    public void PaymentCalculationCallableFuture(String tabName, int schemeId, String schemeCode, String fy, int installment, String user_id) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                boolean append = true;

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                ResourceBundle resource = ResourceBundle.getBundle("Common/config");
                String filename = resource.getString("bms.apicall.log") + "/error_" + java.time.LocalDate.now() + ".log";

                CommonMethod cm = new CommonMethod();
                Security.AES aes = new Security.AES();
                Connection conn = null;
                //dbcon obj = new dbcon();
                CallableStatement cs = null;
                ResultSet rs = null;

                Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");

                SimpleFormatter formatter = new SimpleFormatter();

                try {
                    /* Call API */
                    handler = new FileHandler(filename, append);
                    logger.addHandler(handler);

                    handler.setFormatter(formatter);
                    String slnosjsonnstring = "";

                    //System.out.println(i);
                    try {
                        String query_set = "", beneficiaryname = "", uid = "", rcnumber = "", accountnumber = "", ifscdb = "", districtnamedb = "", subdivisionnamedb = "", block_mcdb = "", gp_vc_warddb = "";
                        int instalmentdb = 0, office_id = 0;
                        String finalret = "";
                        int _sln = 0;
                        HttpURLConnection urlConn = null;
                        OutputStream ouputStream = null;
                        BufferedReader reader1 = null;

                        int perc = 0;
                        int distleven = 1000;
                        String ready_toapprove = "N";
                        String approvedby = "", auto_approval_data = "";
                        String strapiresponselog = "", dob = "", guardians_name = "", rc_mem = "", bank_name = "", scheme_name = "";
                        ResultSet rs2 = null;
                        PreparedStatement pst = null;
                        //int slno = slnoarr.getJSONObject(i).getInt("slno");
                        String sql = "SELECT * FROM beneficiary." + tabName + " WHERE status = 9 AND eligible_dbt_amount::integer = 0";

                        if (conn == null || conn.isClosed()) {
                            //Fix Start
                            Thread l_thread = Thread.currentThread();
                            l_thread.setContextClassLoader(this.getClass().getClassLoader());
                            //Fix End

                            conn = new DBCon.DBSource().connectToBMSDB().getConnection();//retConnection();
//                                   
                        } else {
                            System.out.println(conn.getClientInfo().elements().toString());
                        }

                        JSONArray jsarr = null;
                        JSONObject jsobj = new JSONObject();
                        try {
                            jsarr = cm.getResultJSON(sql, conn);
                            jsobj.put("benData", jsarr);
                        } catch (Exception ex) {
                            Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        for (int n = 0; n < jsarr.length(); n++) {
                            jsobj = jsarr.getJSONObject(n);
                            try {
                                int x = 0;
                                if (conn == null || conn.isClosed()) {
                                    conn = new DBCon.DBSource().connectToBMSDB().getConnection();
                                }

                                String dbFuncName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";

                                cs = conn.prepareCall("{call beneficiary.insert_beneficiary_selection_to_data(?,?,?,?::json)}");
                                cs.setInt(++x, schemeId);
                                cs.setString(++x, fy);
                                cs.setInt(++x, installment);
                                cs.setString(++x, jsobj.toString());

                            } catch (Exception ex) {
                                Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                                handler.flush();
                            }
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                        logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                        handler.flush();
                    }
                    TimeUnit.SECONDS.sleep(1);

                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    handler.flush();
                    throw new IllegalStateException(e);

                } catch (IOException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } catch (SecurityException ex) {
                    Logger.getLogger(CompletableFutureDemo.class.getName()).log(Level.SEVERE, null, ex);
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    handler.flush();
                } finally {
                    if (cs != null) {
                        try {
                            cs.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                System.out.println("I'll run in a separate thread than the main thread.");

            }

        }
        );

        //handler.close();
    }

//    public java.sql.Connection retConnection() {
//        String dsName = "java:/comp/env/bms";
//        try {
//            InitialContext ctx;
//
//            ctx = new InitialContext();
//            DataSource ds = (DataSource) ctx.lookup(dsName);
//            if (ds == null) {
//                //throw new Exception("Data source not found!");
//                //int i=0;
//
//                System.out.print("Data source not found!");
//            }
//            try {
//                return ds.getConnection();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } catch (NamingException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    public void ApiCallableFuture() {
//        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    /* Call API */
//                    for (int i = 0; i <= 100; i++) {
//                        Random random = new Random();
//                        System.out.println(random.nextInt(50));
//                        TimeUnit.SECONDS.sleep(5);
//                    }
//
//                } catch (InterruptedException e) {
//                    throw new IllegalStateException(e);
//                }
//                System.out.println("I'll run in a separate thread than the main thread.");
//            }
//        });
//    }
}
