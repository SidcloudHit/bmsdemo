/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import Common.CommonMethod;

import Master.Data.AdminUser;
import Security.SecurityClass;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.ws.rs.Path;
import org.apache.commons.compress.utils.IOUtils;
import org.bouncycastle.crypto.tls.DigestAlgorithm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "saveExcelDataToTmp", urlPatterns = {"/saveExcelDataToTmp"})
@MultipartConfig
public class saveExcelDataToTmp extends HttpServlet {

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

            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession();

            String userName = "", message = "", userId = "";
            int deptId = 0, officeId = 0, roleId = 0;

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();
                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
            }

            Connection con = null;
            //Connection con1 = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            int legacy_data_status = 0, total_row_no = 0, installment = 0;
            String fy = "", benificiary_count = "", scheme = "";
            //String user_id = "";
            String dept = "", dept_name = "", installment_name = "", scheme_name = "", schemeCode = "", validjson = "", invalidjson = "", headerarr = "";
            String ctypeExcel = "";
            String fileExcelName = "";
            InputStream isExcel = null;
            InputStream isExcel_data_stream = null;
            InputStream isExcel_valid_data_stream = null;
            int meta_status = 0;
            String valid_excel_file = "true";
            long fileExcelSize = 0;
            int fileExcelFid = 0, schemeId = 0;
            Part filePart = null;
            int mapping_data = 0;
            CommonMethod cm = new CommonMethod();
            Security.AES aes = new Security.AES();
            int status = 0;
            if (request.getParameter("fy") != null) {
                fy = SecurityClass.killchar(request.getParameter("fy"));
            }
            if (request.getParameter("installment") != null) {
                installment = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("installment")));
                //installment = SecurityClass.killchar(request.getParameter("installment"));
            }
            if (request.getParameter("installment_name") != null) {
                installment_name = SecurityClass.killchar(request.getParameter("installment_name"));
            }
            if (request.getParameter("ddlSchemes") != null) {
                scheme = request.getParameter("ddlSchemes");
                schemeId = Integer.parseInt(scheme.split("~")[0]);
                schemeCode = scheme.split("~")[1];
            }
            if (request.getParameter("validjson") != null) {
                validjson = request.getParameter("validjson");
            }
            if (request.getParameter("invalidjson") != null) {
                invalidjson = request.getParameter("invalidjson");
            }
            if (request.getParameter("headerarr") != null) {
                headerarr = request.getParameter("headerarr");
            }

//            {
//                scheme = SecurityClass.killcharMin(request.getParameter("scheme"));
//            }
//            if (request.getParameter("scheme_name") != null) {
//                scheme_name = request.getParameter("scheme_name");
//            }
            if (request.getParameter("benificiary_count") != null) {
                benificiary_count = SecurityClass.killchar(request.getParameter("benificiary_count"));
            }

//            String[] arr = scheme.split("~");
//            String benefit_type = arr[2];
//            scheme_name = arr[3].trim();
            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }
            //AdminUser usr = new AdminUser(user_id);

            CSVReader reader = null;

            List<List<String>> records = new ArrayList<List<String>>();
            try {
                filePart = null;
                filePart = request.getPart("fileExcel");
            } catch (Exception ex) {
                ex.printStackTrace();
                message = "File upload error! Please try again";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("BeneficiarySelection/beneficiaryDataUpload.jsp");
                valid_excel_file = "false";
                return;
            }

            if (filePart != null) {
                ctypeExcel = filePart.getContentType();
                fileExcelSize = filePart.getSize();
                fileExcelName = filePart.getSubmittedFileName();
                if (fileExcelSize > 0) {

                    //if (!(ctypeExcel.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
                    if (!(ctypeExcel.equalsIgnoreCase("application/vnd.ms-excel")) && !(ctypeExcel.equalsIgnoreCase("application/octet-stream"))) {
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Please Upload CSV File Only.');");
                        out.println("location='BeneficiarySelection/beneficiaryDataUpload.jsp';");
                        out.println("</script>");
                        valid_excel_file = "false";
                        return;
                    }
                    if ((fileExcelSize / 1024) > 10240) { // filesize in kb
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Size of file is more than 10 MB, Please use smaller size.')");
                        out.println("location='BeneficiarySelection/beneficiaryDataUpload.jsp';");
                        out.println("</script>");
                        valid_excel_file = "false";
                        return;
                    }

                    isExcel_data_stream = filePart.getInputStream();
                    reader = new CSVReader(new InputStreamReader(isExcel_data_stream, "UTF-8"));

                    String[] lineInArray;
                    try {
                        while ((lineInArray = reader.readNext()) != null) {
                            if ((lineInArray[0].trim().isEmpty()) && (lineInArray[1].trim().isEmpty() && (lineInArray[2].trim().isEmpty()) && (lineInArray[3].trim().isEmpty()))) {
                            } else {
                                records.add(Arrays.asList(lineInArray));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    isExcel_data_stream.close();
                    reader.close();

//                    total_row_no = records.size() - 3;
//                    mapping_data = cm.checkMappingData(user_id, schemeCode, fy);
//                    if (mapping_data < total_row_no) {
//                        out.println("<script type=\"text/javascript\">");
//                        out.println("alert('Target Mismatch. Please Check your target for the Scheme " + scheme_name + " for the financial year " + fy + "')");
//                        out.println("location='BeneficiarySelection/beneficiaryDataUpload.jsp';");
//                        out.println("</script>");
//                        valid_excel_file = "false";
//                        return;
//                    }
                }
            }
            String filename = "";
            if ("true".equals(valid_excel_file)) {
                String query = "";
                if (!records.isEmpty()) {

                    try {
                        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                        String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_dump";
//                        if (mapping_data >= total_row_no) {

                        int batchSize = 40;
                        int count = 0, nextcount = 0, error_cnt = 0;
                        int trancount = 0;
                        int batch_id = cm.getExcelDataUploadBatchId();
                        ResourceBundle resource = ResourceBundle.getBundle("Common/config");

                        String filePath = resource.getString("bms.csv.upload") + deptId;
                        createFolderIfNotExists(filePath);
                        filePath = filePath + "\\" + schemeCode;
                        createFolderIfNotExists(filePath);
                        filename = System.currentTimeMillis() + fileExcelName;
                        filePath = filePath + "\\" + filename;

                        //InputStream initialStream = filePart.getInputStream();
                        File targetFile = new File(filePath);

                        java.nio.file.Files.copy(
                                filePart.getInputStream(),
                                targetFile.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                        IOUtils.closeQuietly(filePart.getInputStream());

                        //successlist.add(Arrays.asList(validjson));
                        String guardians_name = "", gender = "", caste = "", pid = "", rc_mem = "", home_add = "", ifsc = "", district_name = "", subdivision_name = "", block_name = "", gp_name = "", pan = "";
                        String dob = "", bank_name = "";
                        String bank_account_no = "";
                        String amount = "";
                        JSONArray beneficiarylist = new JSONArray();
                        JSONArray successarr = new JSONArray(validjson);
                        JSONArray invalidlistarrservlet = new JSONArray(invalidjson);
                        for (int i = 0; i < successarr.length(); i++) {
                            JSONObject beneficiary = new JSONObject();

                            boolean pidsuccess = true, rc_no_success = false;
                            String pid_clean = "";
                            String rc_clean = "";

                            if (!"".equals(successarr.getJSONArray(i).get(0).toString())) {
                                String name = Security.SecurityClass.killchar(successarr.getJSONArray(i).get(1).toString().replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9]", " "));
                                if (!"".equals(successarr.getJSONArray(i).get(2).toString())) {
                                    String pid_value = Security.SecurityClass.killcharNumber(successarr.getJSONArray(i).get(2).toString().trim());
                                    pid_clean = pid_value.replaceAll("\\<.*?\\>", "").replaceAll("\\s", "");
                                    BigInteger pid_no = new BigInteger(pid_clean);
                                    //  BigInteger pid_no=BigInteger.valueOf(pid_clean);
                                    if (VerhoeffAlgorithm.validateVerhoeff(String.valueOf(pid_no))) {
                                        pid = aes.encrypt(String.valueOf(pid_no));
                                    } else {
                                        pid = "";
                                        pidsuccess = false;
                                    }
                                } else {
                                    pid = "";
                                }
                                String rc_no = "";
                                if (!"".equals(successarr.getJSONArray(i).get(3))) {
                                    String rc = Security.SecurityClass.killcharNumber(successarr.getJSONArray(i).get(3).toString());
                                    rc_clean = rc.trim().replaceAll("\\<.*?\\>", "").replaceAll("\\s", "");
                                    BigInteger number = new BigInteger(rc_clean);
                                    rc_no = String.valueOf(number);
//                                    }
                                } else {
                                    rc_no = "";
                                }
                                if (rc_no.length() == 12 || rc_no.length() == 14) {
                                    rc_no_success = true;
                                }

                                if (!"".equals(successarr.getJSONArray(i).get(4).toString())) {
                                    bank_account_no = Security.SecurityClass.stripXSS(successarr.getJSONArray(i).get(4).toString());
                                    //bank_account_no = Security.SecurityClass.killchar(successarr.getJSONArray(i).get(4).toString().replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9]", " "));
                                } else {
                                    bank_account_no = "";
                                }
                                if (!"".equals(successarr.getJSONArray(i).get(5).toString())) {
                                    ifsc = Security.SecurityClass.stripXSS(successarr.getJSONArray(i).get(5).toString().replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9]", " "));
                                } else {
                                    ifsc = "";
                                }

                                if (!"".equals(successarr.getJSONArray(i).get(6).toString())) {
                                    district_name = Security.SecurityClass.killchar(successarr.getJSONArray(i).get(6).toString().replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9]", " "));
                                } else {
                                    district_name = "";
                                }
                                if (!"".equals(successarr.getJSONArray(i).get(7).toString())) {
                                    subdivision_name = Security.SecurityClass.killchar(successarr.getJSONArray(i).get(7).toString().replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9]", " "));
                                } else {
                                    subdivision_name = "";
                                }
                                if (!"".equals(successarr.getJSONArray(i).get(8).toString())) {
                                    block_name = Security.SecurityClass.killchar(successarr.getJSONArray(i).get(8).toString().replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9]", " "));
                                } else {
                                    block_name = "";
                                }
                                if (!"".equals(successarr.getJSONArray(i).get(9).toString())) {
                                    gp_name = Security.SecurityClass.killchar(successarr.getJSONArray(i).get(9).toString().replaceAll("\\<.*?\\>", ""));
                                } else {
                                    gp_name = "";
                                }

                                boolean isbanksuccess = true;
                                String accVldMsg = "";
                                if (bank_account_no.equals("")) {
                                    isbanksuccess = false;
                                    accVldMsg = "Invalid Bank Account Number";
                                } else {
                                    String regex = "[0-9]{9,18}";
                                    if (!bank_account_no.matches(regex)) {
                                        isbanksuccess = false;
                                        accVldMsg = "Invalid Bank Account Number";
                                    }
                                }
                                if (ifsc.equals("")) {
                                    isbanksuccess = false;
                                    accVldMsg = "Invalid IFSC";
                                } else {
                                    String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";
                                    if (!ifsc.matches(regex)) {
                                        isbanksuccess = false;
                                        accVldMsg = "Invalid IFSC";
                                    }
                                }
                                boolean pidAndRcnoSuccess = true;
                                if (!pid.equals("") || !rc_no.equals("")) {
                                    if (!pidsuccess && !rc_no_success) {
                                        pidAndRcnoSuccess = false;
                                    }
                                }
                                if (!isbanksuccess) {
                                    JSONArray array = new JSONArray();
                                    array.put(String.valueOf(invalidlistarrservlet.length() + 1));
                                    array.put(name.equals("") ? JSONObject.NULL : name);
                                    array.put(pid.equals("") ? JSONObject.NULL : aes.decrypt(String.valueOf(pid)));
                                    array.put(rc_no.equals("") ? JSONObject.NULL : rc_no);
                                    array.put(bank_account_no.equals("") ? JSONObject.NULL : bank_account_no);
                                    array.put(ifsc.equals("") ? JSONObject.NULL : ifsc);
                                    array.put(district_name.equals("") ? JSONObject.NULL : district_name);
                                    array.put(subdivision_name.equals("") ? JSONObject.NULL : subdivision_name);
                                    array.put(block_name.equals("") ? JSONObject.NULL : block_name);
                                    array.put(gp_name.equals("") ? JSONObject.NULL : gp_name);
                                    array.put(accVldMsg);
                                    array.put("");
                                    array.put("");
                                    array.put("");
                                    array.put("");
                                    invalidlistarrservlet.put(array);
                                } else if (!pidAndRcnoSuccess) {
                                    JSONArray array = new JSONArray();
                                    array.put(String.valueOf(invalidlistarrservlet.length() + 1));
                                    array.put(name.equals("") ? JSONObject.NULL : name);
                                    array.put(pid.equals("") ? JSONObject.NULL : aes.decrypt(String.valueOf(pid)));
                                    array.put(rc_no.equals("") ? JSONObject.NULL : rc_no);
                                    array.put(bank_account_no.equals("") ? JSONObject.NULL : bank_account_no);
                                    array.put(ifsc.equals("") ? JSONObject.NULL : ifsc);
                                    array.put(district_name.equals("") ? JSONObject.NULL : district_name);
                                    array.put(subdivision_name.equals("") ? JSONObject.NULL : subdivision_name);
                                    array.put(block_name.equals("") ? JSONObject.NULL : block_name);
                                    array.put(gp_name.equals("") ? JSONObject.NULL : gp_name);
                                    array.put("Aadhar No. or Ration Card No. Invalid");
                                    array.put("");
                                    array.put("");
                                    array.put("");
                                    array.put("");
                                    invalidlistarrservlet.put(array);
                                } else {
                                    String plaintext = name + pid + rc_no + bank_account_no;
                                    String hashtext = aes.encrypt(String.valueOf(plaintext));
                                    beneficiary.put("beneficiaryname", name.equals("") ? JSONObject.NULL : name);
                                    beneficiary.put("rcnumber", rc_no.equals("") ? JSONObject.NULL : rc_no);
                                    beneficiary.put("uid", pid.equals("") ? JSONObject.NULL : pid);
                                    beneficiary.put("bankaccountno", bank_account_no.equals("") ? JSONObject.NULL : bank_account_no);
                                    beneficiary.put("ifsc", ifsc.equals("") ? JSONObject.NULL : ifsc);
                                    beneficiary.put("districtname", district_name.equals("") ? JSONObject.NULL : district_name);
                                    beneficiary.put("subdivisionname", subdivision_name.equals("") ? JSONObject.NULL : subdivision_name);
                                    beneficiary.put("blockname", block_name.equals("") ? JSONObject.NULL : block_name);
                                    beneficiary.put("gpname", gp_name.equals("") ? JSONObject.NULL : gp_name);
                                    beneficiary.put("schemecode", schemeCode.equals("") ? JSONObject.NULL : schemeCode);
                                    beneficiary.put("user_id", userId.equals("") ? JSONObject.NULL : userId);
                                    beneficiary.put("office_id", officeId);
                                    beneficiary.put("installment", installment);
                                    beneficiary.put("batch_id", batch_id);
                                    beneficiary.put("hashtext", hashtext);
                                    beneficiary.put("scheme_id", schemeId);
                                    beneficiarylist.put(beneficiary);
                                }

                            }
                        }

                        CallableStatement cs = null;
                        String duplicatejsonstring = "";
                        try {
                            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                            cs = con.prepareCall("{call beneficiary.insert_beneficiary_selection_to_datadump(?::json,?,?)}");
                            cs.setString(1, beneficiarylist.toString());
                            cs.setString(2, tabName);
                            cs.registerOutParameter(3, Types.VARCHAR);
                            cs.execute();
                            duplicatejsonstring = cs.getString(3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONArray resultarray = new JSONArray(duplicatejsonstring);
                        JSONObject json_obj_src_list = resultarray.getJSONObject(0);
                        JSONArray resultsuccessarray = new JSONArray(json_obj_src_list.getJSONArray("successjson").toString());
                        JSONArray resultfailedarray = new JSONArray(json_obj_src_list.getJSONArray("failedjson").toString());  //invalidjson  string
                        JSONArray invalidlistarr = new JSONArray(invalidlistarrservlet.toString());

                        for (int i = 0; resultfailedarray.length() > i; i++) {
                            JSONArray array = new JSONArray();
                            array.put(String.valueOf(invalidlistarr.length() + 1));
                            array.put(resultfailedarray.getJSONObject(i).optString("beneficiaryname"));
                            array.put(!resultfailedarray.getJSONObject(i).optString("uid").equals("") ? aes.decrypt(String.valueOf(resultfailedarray.getJSONObject(i).getString("uid"))) : "");
                            array.put(resultfailedarray.getJSONObject(i).optString("rcnumber"));
                            array.put(resultfailedarray.getJSONObject(i).optString("bankaccountno"));
                            array.put(resultfailedarray.getJSONObject(i).optString("ifsc"));
                            array.put(resultfailedarray.getJSONObject(i).optString("districtname"));
                            array.put(resultfailedarray.getJSONObject(i).optString("subdivisionname"));
                            array.put(resultfailedarray.getJSONObject(i).optString("blockname"));
                            array.put(resultfailedarray.getJSONObject(i).optString("gpname"));
                            array.put("duplicate records");
                            array.put("");
                            array.put("");
                            array.put("");
                            array.put("");
                            invalidlistarr.put(array);
                        }

                        //api call     getslnoforapicall
                        String slnosjsonnstring = "";
                        slnosjsonnstring = cm.getslnoforapicall(tabName, officeId);
                        JSONArray slnoarr = new JSONArray(slnosjsonnstring);

                        CompletableFutureDemo CF = new CompletableFutureDemo();

                        CF.ApiCallableFuture(resultsuccessarray, slnoarr, tabName, schemeId, schemeCode, fy, installment, batch_id, deptId, dept_name, installment_name, userId); //officeId, con

//                        if (resultsuccessarray.length() > 0) {
//                            for (int i = 0; i < slnoarr.length(); i++) {
//                                try {
//                                    String query_set = "", beneficiaryname = "", uid = "", rcnumber = "", accountnumber = "", ifscdb = "", districtnamedb = "", subdivisionnamedb = "", block_mcdb = "", gp_vc_warddb = "";
//                                    int instalmentdb = 0;
//                                    String finalret = "";
//                                    int _sln = 0;
//                                    HttpURLConnection urlConn = null;
//                                    OutputStream ouputStream = null;
//                                    BufferedReader reader1 = null;
//
//                                    int perc = 0;
//                                    int distleven = 1000;
//                                    String ready_toapprove = "N";
//                                    String approvedby = "", auto_approval_data = "";
//                                    String strapiresponselog = "";
//                                    ResultSet rs2 = null;
//                                    PreparedStatement pst = null;
//                                    int slno = slnoarr.getJSONObject(i).getInt("slno");
//                                    String sql = "select slno,coalesce(beneficiaryname,'') as beneficiaryname,coalesce(uid,'') as uid,coalesce(rcnumber,'') as rcnumber, coalesce(accountnumber,'') as accountnumber, coalesce(ifsc,'') as ifsc, coalesce(districtname,'') as districtname, coalesce(subdivisionname,'') as subdivisionname, coalesce(block_mc,'') as block_mc, coalesce(gp_vc_ward,'') as gp_vc_ward,instalment from " + tabName + " where slno=" + slno;
//                                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//                                    pst = con.prepareStatement(sql);
//                                    rs2 = pst.executeQuery();
//                                    if (rs2.next()) {
//                                        try {
//                                            _sln = rs2.getInt("slno");
//                                            beneficiaryname = rs2.getString("beneficiaryname").trim();
//                                            uid = rs2.getString("uid").trim();
//                                            rcnumber = rs2.getString("rcnumber").trim();
//                                            accountnumber = rs2.getString("accountnumber").trim();
//                                            ifscdb = rs2.getString("ifsc").trim();
//                                            districtnamedb = rs2.getString("districtname").trim();
//                                            subdivisionnamedb = rs2.getString("subdivisionname").trim();
//                                            block_mcdb = rs2.getString("block_mc").trim();
//                                            gp_vc_warddb = rs2.getString("gp_vc_ward").trim();
//                                            instalmentdb = rs2.getInt("instalment");
//
//                                            String basurl = "https://bms.tripura.gov.in/ssotapi/webresources/services";
//
//                                            String url1 = basurl + "/getMemberDetailsByUidOrRcNoName?uid=" + URLEncoder.encode(uid, "UTF-8") + "&rcNo=" + URLEncoder.encode(rcnumber, "UTF-8") + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");
//
//                                            Logger.getLogger(saveExcelDataToTmp.class.getName()).log(Level.INFO, "Requesting  URL: " + url1, "");
//
//                                            String datainput = "uid=" + uid + "&rcNo=" + rcnumber + "&memberName=" + URLEncoder.encode(beneficiaryname, "UTF-8");
//                                            URL urlObj = new URL(url1);
//                                            urlConn = (HttpURLConnection) urlObj.openConnection();
//                                            urlConn.setDoOutput(true);
//                                            urlConn.setRequestMethod("POST");
//
//                                            urlConn.setRequestProperty("Content-Type", "application/json");
//                                            //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                                            urlConn.setConnectTimeout(360000);
//
//                                            urlConn.setRequestProperty("Accept", "application/json");
//
//                                            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                                                System.err.println("Unable to connect to the URL " + url1 + ", Error Code: " + urlConn.getResponseCode());
//
//                                            } else {
//                                                InputStream is = urlConn.getInputStream();
//                                                reader1 = new BufferedReader(new InputStreamReader((is)));
//                                                String tmpStr = null;
//                                                while ((tmpStr = reader1.readLine()) != null) {
//
//                                                    finalret += tmpStr;
//                                                }
//
//                                                String fullnameapi = "", duplicateapi = "", rc_member_idapi = "", missing_in_rcapi = "", genderapi = "", casteapi = "", panapi = "", rc_noapi = "", last_mile_addressapi = "", fps_idapi = "", fps_latapi = "", fps_longapi = "", postrcvalidation_distlgdcodeapi = "", postrcvalidation_distlgdnameapi = "", postrcvalidation_subdivlgdcodeapi = "", postrcvalidation_subdivlgdnameapi = "";
//                                                String postrcvalidation_blocklgdcode = "", postrcvalidation_blocklgdname = "", postrcvalidation_gplgdcode = "", postrcvalidation_gplgdname = "", postrcvalidation_hof_rc_member_id = "", postrcvalidation_hofname = "", nearest_lat_long_desc = "";
//                                                String father_name = "";
//                                                JSONObject jsonObj = new JSONObject(finalret);
//
//                                                JSONArray details = new JSONArray(jsonObj.get("beneficiarydetails").toString());
//                                                for (int j = 0; j < details.length(); j++) {
//
//                                                    boolean isvalidflag = false;
//                                                    JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());
//
//                                                    String strnodata = "";
//                                                    try {
//                                                        strnodata = tmpOBJ.get("message").toString().trim();
//                                                    } catch (JSONException ex) {
//                                                    }
//                                                    if (strnodata.equalsIgnoreCase(" No Data Found".trim())) {
//                                                        isvalidflag = false;
//                                                    } else {
//                                                        fullnameapi = tmpOBJ.get("fullname").toString();
//                                                        duplicateapi = tmpOBJ.get("duplicate_in_rc").toString();
//                                                        rc_member_idapi = tmpOBJ.get("ration_card_memberid").toString();
//                                                        missing_in_rcapi = tmpOBJ.get("missing_in_rc").toString();
//
//                                                        genderapi = tmpOBJ.get("gender").toString();
//                                                        casteapi = tmpOBJ.get("caste").toString();
//                                                        rc_noapi = tmpOBJ.get("ration_card_no").toString();
//                                                        last_mile_addressapi = tmpOBJ.get("landmark").toString();
//
//                                                        fps_idapi = tmpOBJ.get("fps_id").toString();
//                                                        fps_latapi = tmpOBJ.get("fps_lat").toString();
//                                                        fps_longapi = tmpOBJ.get("fps_long").toString();
//                                                        postrcvalidation_distlgdcodeapi = tmpOBJ.get("lgd_district_code").toString();
//                                                        postrcvalidation_distlgdnameapi = tmpOBJ.get("lgd_district_name").toString();
//                                                        postrcvalidation_subdivlgdcodeapi = tmpOBJ.get("lgd_subdiv_code").toString();
//                                                        postrcvalidation_subdivlgdnameapi = tmpOBJ.get("lgd_subdiv_name").toString();
//                                                        postrcvalidation_blocklgdcode = tmpOBJ.get("lgd_block_code").toString();
//                                                        postrcvalidation_blocklgdname = tmpOBJ.get("lgd_block_name").toString();
//
//                                                        postrcvalidation_gplgdcode = tmpOBJ.get("lgd_gp_code").toString();
//                                                        postrcvalidation_gplgdname = tmpOBJ.get("lgd_gp_name").toString();
//
//                                                        postrcvalidation_hof_rc_member_id = tmpOBJ.get("hof_member_id").toString();
//                                                        postrcvalidation_hofname = tmpOBJ.get("hof_name").toString();
//                                                        nearest_lat_long_desc = tmpOBJ.get("nearest_lat_long_desc").toString();
//
//                                                        String parameter_missing = tmpOBJ.get("parameter_missing").toString();
//                                                        String invalid_rc_no = tmpOBJ.get("invalid_rc_no").toString();
//                                                        distleven = Integer.parseInt(tmpOBJ.optString("levenshtein_dst").equals("") ? "1000" : tmpOBJ.get("levenshtein_dst").toString());
//                                                        father_name = tmpOBJ.optString("father_name");
//                                                        dob = tmpOBJ.optString("member_dob");
//
//                                                        //optString
//                                                        //missing_in_rc,duplicate_in_rc,parameter_missing,invalid_rc_no,levenshtein_dst
//                                                        if (missing_in_rcapi.equalsIgnoreCase("false")
//                                                                && duplicateapi.equalsIgnoreCase("false")
//                                                                && parameter_missing.equalsIgnoreCase("false")
//                                                                && invalid_rc_no.equalsIgnoreCase("false")
//                                                                && distleven == 0) {
//                                                            isvalidflag = true;
//                                                        } else {
//                                                            isvalidflag = false;
//                                                        }
//                                                    }
//                                                    JSONObject jobjforsave = new JSONObject();
//                                                    jobjforsave.put("scheme_id", schemeId);
//                                                    jobjforsave.put("scheme_code", schemeCode.equals("") ? JSONObject.NULL : schemeCode);
//                                                    jobjforsave.put("fy", fy.equals("") ? JSONObject.NULL : fy);
//                                                    jobjforsave.put("installment", installment);
//                                                    jobjforsave.put("full_name", beneficiaryname.equals("") ? JSONObject.NULL : beneficiaryname);
//                                                    jobjforsave.put("father_name", father_name.equals("") ? JSONObject.NULL : father_name);
//                                                    jobjforsave.put("guardians_name", guardians_name.equals("") ? JSONObject.NULL : guardians_name);
//                                                    jobjforsave.put("gender", genderapi.equals("") ? JSONObject.NULL : genderapi);
//                                                    jobjforsave.put("dob", dob.equals("") ? JSONObject.NULL : dob);
//                                                    jobjforsave.put("caste", casteapi.equals("") ? JSONObject.NULL : casteapi);
//                                                    jobjforsave.put("pid", uid.equals("") ? JSONObject.NULL : uid);
//                                                    jobjforsave.put("pan", pan.equals("") ? JSONObject.NULL : pan);
//                                                    jobjforsave.put("rc_no", rcnumber.equals("") ? JSONObject.NULL : rcnumber);
//                                                    jobjforsave.put("rc_mem", rc_mem.equals("") ? JSONObject.NULL : rc_mem);
//                                                    jobjforsave.put("bank_name", bank_name.equals("") ? JSONObject.NULL : bank_name);
//                                                    jobjforsave.put("ifsc", ifscdb.equals("") ? JSONObject.NULL : ifscdb);
//                                                    jobjforsave.put("bank_account_no", accountnumber.equals("") ? JSONObject.NULL : accountnumber);
//                                                    jobjforsave.put("amount", JSONObject.NULL);
//                                                    jobjforsave.put("lgd_dist_code", JSONObject.NULL);
//                                                    jobjforsave.put("district_name", districtnamedb.equals("") ? JSONObject.NULL : districtnamedb);
//                                                    jobjforsave.put("lgd_subdiv_code", JSONObject.NULL);
//                                                    jobjforsave.put("subdivision_name", subdivisionnamedb.equals("") ? JSONObject.NULL : subdivisionnamedb);
//                                                    jobjforsave.put("lgd_block_code", JSONObject.NULL);
//                                                    jobjforsave.put("block_name", block_mcdb.equals("") ? JSONObject.NULL : block_mcdb);
//                                                    jobjforsave.put("lgd_gp_code", "");
//                                                    jobjforsave.put("gp_name", gp_vc_warddb.equals("") ? JSONObject.NULL : gp_vc_warddb);
//                                                    jobjforsave.put("last_mile_addresstext", "");
//                                                    jobjforsave.put("uploadbatchid", batch_id);
//                                                    jobjforsave.put("postrcvalidation_fullname", fullnameapi.equals("") ? JSONObject.NULL : fullnameapi);
//                                                    jobjforsave.put("postrcvalidation_duplicate_in_rc", duplicateapi.equals("") ? JSONObject.NULL : duplicateapi);
//                                                    jobjforsave.put("postrcvalidation_missing_in_rc", missing_in_rcapi.equals("") ? JSONObject.NULL : missing_in_rcapi);
//                                                    jobjforsave.put("postrcvalidation_rc_member_id", rc_member_idapi.equals("") ? JSONObject.NULL : rc_member_idapi);
//                                                    jobjforsave.put("postrcvalidation_outputtext", JSONObject.NULL);
//                                                    jobjforsave.put("scheme_name", scheme_name.equals("") ? JSONObject.NULL : scheme_name);
//                                                    jobjforsave.put("dept_id", dept_id);
//                                                    jobjforsave.put("dept_name", dept_name.equals("") ? JSONObject.NULL : dept_name);
//                                                    jobjforsave.put("benefit_type_cash_kind", JSONObject.NULL);
//                                                    jobjforsave.put("installment_name", installment_name.equals("") ? JSONObject.NULL : installment_name);
//                                                    jobjforsave.put("postrcdetails_bymemberid_outputtext", JSONObject.NULL);
//                                                    jobjforsave.put("isapproved_readyfordashboard", JSONObject.NULL);
//                                                    jobjforsave.put("approved_by", JSONObject.NULL);
//                                                    jobjforsave.put("approved_datetime", JSONObject.NULL);
//                                                    jobjforsave.put("benef_name_matchresult_dist_leven", distleven);
//                                                    jobjforsave.put("matched_by", JSONObject.NULL);
//                                                    jobjforsave.put("matched_datetime", JSONObject.NULL);
//                                                    jobjforsave.put("postrcdetails_byname_outputtext", JSONObject.NULL);
//                                                    jobjforsave.put("uploaded_by", JSONObject.NULL);
//                                                    jobjforsave.put("uploaded_datetime", JSONObject.NULL);
//                                                    jobjforsave.put("fps_id", fps_idapi.equals("") ? JSONObject.NULL : fps_idapi);
//                                                    jobjforsave.put("fps_lat", fps_latapi.equals("") ? JSONObject.NULL : fps_latapi);
//                                                    jobjforsave.put("fps_long", fps_longapi.equals("") ? JSONObject.NULL : fps_longapi);
//                                                    jobjforsave.put("postrcvalidation_distlgdcode", postrcvalidation_distlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdcodeapi);
//                                                    jobjforsave.put("postrcvalidation_distlgdname", postrcvalidation_distlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_distlgdnameapi);
//                                                    jobjforsave.put("postrcvalidation_subdivlgdcode", postrcvalidation_subdivlgdcodeapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdcodeapi);
//                                                    jobjforsave.put("postrcvalidation_subdivlgdname", postrcvalidation_subdivlgdnameapi.equals("") ? JSONObject.NULL : postrcvalidation_subdivlgdnameapi);
//                                                    jobjforsave.put("postrcvalidation_blocklgdcode", postrcvalidation_blocklgdcode.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdcode);
//                                                    jobjforsave.put("postrcvalidation_blocklgdname", postrcvalidation_blocklgdname.equals("") ? JSONObject.NULL : postrcvalidation_blocklgdname);
//                                                    jobjforsave.put("postrcvalidation_gplgdcode", postrcvalidation_gplgdcode.equals("") ? JSONObject.NULL : postrcvalidation_gplgdcode);
//                                                    jobjforsave.put("postrcvalidation_gplgdname", postrcvalidation_gplgdname.equals("") ? JSONObject.NULL : postrcvalidation_gplgdname);
//                                                    jobjforsave.put("postrcvalidation_hof_rc_member_id", postrcvalidation_hof_rc_member_id.equals("") ? JSONObject.NULL : postrcvalidation_hof_rc_member_id);
//                                                    jobjforsave.put("postrcvalidation_hofname", postrcvalidation_hofname.equals("") ? JSONObject.NULL : postrcvalidation_hofname);
//                                                    jobjforsave.put("nearest_lat_long_desc", nearest_lat_long_desc.equals("") ? JSONObject.NULL : nearest_lat_long_desc);
//                                                    jobjforsave.put("is_disapproved", JSONObject.NULL);
//                                                    jobjforsave.put("transaction_datasentbyinsert_update", JSONObject.NULL);
//                                                    jobjforsave.put("postrcvalidation_rc_no", rc_noapi.equals("") ? JSONObject.NULL : rc_noapi);
//                                                    jobjforsave.put("component_name", JSONObject.NULL);
//                                                    jobjforsave.put("is_duplicate", JSONObject.NULL);
//                                                    jobjforsave.put("entryby", user_id);
//                                                    jobjforsave.put("instalment", instalmentdb);
//                                                    jobjforsave.put("office_id", office_Id);
//                                                    if (isvalidflag) {
//                                                        String tabNamedata = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
//                                                        try {
//                                                            int x = 0;
//                                                            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//                                                            cs = con.prepareCall("{call insert_beneficiary_selection_to_data(?,?,?,?::json)}");
//                                                            cs.setInt(++x, _sln);
//                                                            cs.setString(++x, tabName);
//                                                            cs.setString(++x, tabNamedata);
//                                                            cs.setString(++x, jobjforsave.toString());
//                                                            cs.execute();
//                                                        } catch (Exception e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    } else {
//                                                        String tabNamedatatemp = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_tamp";
//                                                        try {
//                                                            int x = 0;
//                                                            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//                                                            cs = con.prepareCall("{call insert_beneficiary_selection_to_datatemp(?,?,?,?::json)}");
//                                                            cs.setInt(++x, _sln);
//                                                            cs.setString(++x, tabName);
//                                                            cs.setString(++x, tabNamedatatemp);
//                                                            cs.setString(++x, jobjforsave.toString());
//
//                                                            //cs.registerOutParameter(4, Types.VARCHAR);
//                                                            cs.execute();
//                                                            //duplicatejsonstring = cs.getString(3);
//                                                        } catch (Exception e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                        } catch (MalformedURLException ex) {
//                                            Logger.getLogger(saveExcelDataToTmp.class.getName()).log(Level.SEVERE, null, ex);
//                                        } catch (ProtocolException ex) {
//                                            Logger.getLogger(saveExcelDataToTmp.class.getName()).log(Level.SEVERE, null, ex);
//                                        } catch (IOException ex) {
//                                            Logger.getLogger(saveExcelDataToTmp.class.getName()).log(Level.SEVERE, null, ex);
//                                        }
//
//                                    }
//
//                                } catch (Exception ex) {
//                                    Logger.getLogger(saveExcelDataToTmp.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//                        }
                        //api call end
                        session.setAttribute("failedjsonarray", invalidlistarr.toString());
                        session.setAttribute("successjson", resultsuccessarray.toString());
                        session.setAttribute("headerarr", headerarr);
                        //request.getRequestDispatcher("BeneficiarySelection/report.jsp").forward(request, response);
                        out.println("<script type=\"text/javascript\">");
                        out.println("location='BeneficiarySelection/report.jsp';");
                        out.println("</script>");

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Error:Please Check the Uploaded File.');");
                        out.println("location='BeneficiarySelection/beneficiaryDataUpload.jsp';");
                        out.println("</script>");
                    }

                }
            }
        }
    }

    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

    private static void moveFile(String src, String dest) {

        Path result = null;
        try {
            Files.move(Paths.get(src), Paths.get(dest));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
        if (result != null) {
            System.out.println("File moved successfully.");
        } else {
            System.out.println("File movement failed.");
        }

    }

//    public boolean checkvalidIFSC(String value) {
//        boolean msg = false;
//        if (value.trim().length() > 0) {
//            String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";
//            if (value.trim().matches(regex)) {
//                //validate += "--Please Enter a valid IFS Code(It should be 11 characters long, The characters should be upper case alphabets) <br/>";
//                msg = true;
//            }
//        }
//        return msg;
//    }
//
//    public boolean checkvalidBankacc(String value) {
//        boolean msg = false;
//        if (value.trim().length() > 0) {
//            String regex = "[0-9]{9,18}";
//            if (value.trim().matches(regex)) {
//                // validate += "--Please Enter a valid Bank Account (between 9-18 numbers) <br/>";
//                msg = true;
//            }
//        }
//        return msg;
//    }

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
