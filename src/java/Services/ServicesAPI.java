/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import DBTWorks.DBTStatusUpdate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import nic.security.ServerAuthentication;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author NIC-Arindam
 */
@Path("services")
public class ServicesAPI {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServicesAPI
     */
    public ServicesAPI() {
    }

    /**
     * Retrieves representation of an instance of Services.ServicesAPI
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object

        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ServicesAPI
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public void putJson(String content) {

    }

    @POST
    @Path("/getfamilydetails")
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getName(@QueryParam("rcno") String rcno) throws JSONException {
        JSONArray jsarr = null;
        JSONObject jsobj = null;
        String msg = "";

        try {
            msg = Common.RCDataCheck.verifyRCByRCID(rcno);

            jsarr = new JSONArray(msg);
            jsobj = new JSONObject();
            jsobj.put("familydetails", jsarr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsobj.toString();

    }

    @POST
    @Path("/getfamilydetailsat")
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getNameAT(@QueryParam("rcno") String rcno) throws JSONException {
        JSONArray jsarr = null;
        JSONObject jsobj = null;
        String msg = "";

        try {
            msg = Common.RCDataCheck.verifyRCByRCIDAT(rcno);

            jsarr = new JSONArray(msg);
            jsobj = new JSONObject();
            jsobj.put("familydetails", jsarr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsobj.toString();

    }

    @POST
    @Path("/validateFromRC")
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String validateFromRC(@QueryParam("uid") String uid) throws JSONException {
        JSONArray jsarr = null;
        JSONObject jsobj = null;
        String msg = "";

        try {
            msg = Common.RCDataCheck.validateBeneficiaryFromRC(uid);

            jsarr = new JSONArray(msg);
            jsobj = new JSONObject();
            jsobj.put("beneficiarydetails", jsarr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsobj.toString();

    }

    @POST
    @Path("/payment_response_file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProfile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws JSONException {
        JSONObject resultobj = new JSONObject();
        JSONObject error_object = new JSONObject();
        JSONObject summery = null;
        Boolean isSuccessFlag = true;
        Boolean is_duplicate = false;
        boolean is_valid = true, is_valid_beneficiary = true, is_matched = false;
        String rsa_client_slno = "";

        String appkey = ""; //6fa91fbb677417915aaca1190ca2a32dae333ebc6af26646dc8d2d79280ea338
        String hashuserid = ""; //dfb62ced17bd3b9c9e7ff9c0b056d221c2e7e706f275f7b16d880edd7efd5b9e
        appkey = getUserIdHash(1);
        hashuserid = getUserIdHash(2);
        ResultSet rs = null;
        Connection con = null;
        con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
        PreparedStatement ps = null;
        CallableStatement cs = null;
        end_try:
        try {
            if (fileInputStream == null || contentDispositionHeader == null) {
                isSuccessFlag = false;
                break end_try;
            }
            String filename = contentDispositionHeader.getFileName();
            String extension = "";
            int i = filename.lastIndexOf('.');
            int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (i > p) {
                extension = filename.substring(i + 1);
            }
            if (!extension.equalsIgnoreCase("json")) {
                isSuccessFlag = false;
                break end_try;
            }

//            ResourceBundle resource = ResourceBundle.getBundle("Common/config");
//            String filePath = resource.getString("dbt.cash.res")+ hashuserid;    
            String filePath = "D:\\uploads\\clientresponse\\" + hashuserid;
            createFolderIfNotExists(filePath);

            filename = System.currentTimeMillis() + filename;

            String jsonfilepath = filePath + "\\resultjsonfiles";
            createFolderIfNotExists(jsonfilepath);
            jsonfilepath = jsonfilepath + "\\" + filename;

            filePath = filePath + "\\" + filename;
            OutputStream outputStream = null;

            int read = 0;
            byte[] bytes = new byte[1024];
            outputStream = new FileOutputStream(new File(filePath));
            while ((read = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            /////Write to temp folder
            outputStream.close();
            ///// file size check
            File filefile = new File(filePath);
            String filesize = getFileSizeKiloBytes(filefile);
            if (Double.parseDouble(filesize) > 2000.00) {
                isSuccessFlag = false;
                break end_try;
            }
            ///// file size check

            ///////read temp json file
            InputStream in = new FileInputStream(filefile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            //result = out.toString();
            JSONObject resultobja = new JSONObject(out.toString());
            String encpaymentkey = (String) resultobja.get("keyClient");
            String encdata = (String) resultobja.get("encdataresponse");
            String dbttype = (String) resultobja.get("dbttype");
            if (encpaymentkey.equals("") && encdata.equals("")) {
                isSuccessFlag = false;
                break end_try;
            }

            try {

                //encpaymentkey decrypt with app key
                ServerAuthentication sa = new ServerAuthentication();

                String paymentkey_json = sa.decryptClientKeyWithAppKey(encpaymentkey, appkey);
                JSONObject paymentkeyjsonobj = new JSONObject(paymentkey_json);
                String paymentkey = (String) paymentkeyjsonobj.get("Result");
                //encpaymentkey decrypt with app key

                //encdata decrypt with payment key
                String encdatajsonstring = sa.decryptClientKeyWithAppKey(encdata, paymentkey);
                JSONObject jsonobj = new JSONObject(encdatajsonstring);
                String mainjson = (String) jsonobj.get("Result");
                //encdata decrypt with payment key

                //create result json files
                File file1 = new File(jsonfilepath);
                boolean result1 = Files.deleteIfExists(file1.toPath());
                try (FileWriter file = new FileWriter(jsonfilepath)) {
                    file.write(mainjson.toString());
                }

                //create result json files
                //get hashuserid and appkey
                JSONObject json_data_json_obj = new JSONObject(mainjson);
                JSONObject obj2 = new JSONObject(json_data_json_obj.getJSONObject("summary").toString());
                summery = new JSONObject(json_data_json_obj.getJSONObject("summary").toString());
                String useridhash = (String) obj2.optString("hashuserid");
                String batchid = (String) obj2.optString("batchid");
                int total_beneficiary_count = Integer.parseInt(obj2.getString("total_beneficiary_count"));
                String total_transaction_amount = (String) obj2.optString("total_transaction_amount");
                String scheme_code = (String) obj2.optString("scheme_code");
                String scheme_name = (String) obj2.optString("scheme_name");
                String reference_date = (String) obj2.optString("reference_date");
                String remarks = (String) obj2.optString("remarks");
                String success_beneficiary_count = (String) obj2.optString("success_beneficiary_count");
                String success_amount = (String) obj2.optString("success_amount");
                String fail_beneficiary_count = (String) obj2.optString("fail_beneficiary_count");
                String fail_amount = (String) obj2.optString("fail_amount");
                String table_name = "";

                JSONArray beneficiary = new JSONArray(json_data_json_obj.getJSONArray("beneficiary").toString());

                String batch_status = "";
                //for table name and check for ambiguity of requests
                Connection conn = null;
                if (conn == null || conn.isClosed()) {
                    conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }
                String query = "SELECT  COALESCE(source_table_name,'') as source_table_name, COALESCE(is_response_status,'') as is_response_status FROM public.dbtgenerated_file_list where batch_id=?;";

                ps = conn.prepareStatement(query);
                ps.setString(1, batchid);
                rs = ps.executeQuery();

                while (rs.next()) {
                    batch_status = rs.getString("is_response_status");
                    table_name = rs.getString("source_table_name");
                }

                if (!batch_status.equalsIgnoreCase("success") && !table_name.equals("")) {
                    //insert to rsa client
                    con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
                    try {

                        cs = con.prepareCall("{call insert_payment_response(?,?,?,?,?,?,?)}");
                        cs.setString(1, useridhash);
                        cs.setString(2, dbttype);
                        cs.setString(3, filePath);
                        cs.setString(4, ".json");
                        cs.setString(5, jsonfilepath);
                        cs.setString(6, batchid);
                        cs.registerOutParameter(7, Types.VARCHAR);
                        //if (
                        cs.execute();
                        isSuccessFlag = true;
                        rsa_client_slno = cs.getString(7);
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                        isSuccessFlag = false;
                    } finally {
                        try {
                            if (con != null) {
                                con.close();
                            }
                            if (ps != null) {
                                ps.close();
                            }
                        } catch (Exception e) {
                        }
                    }
                    // insert into rsa client

                    ///validatation check
                    JSONArray error_beneficiary_array = new JSONArray();
                    String errormsg = "";
                    if (conn == null || conn.isClosed()) {
                        conn = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    }
                    query = "SELECT beneficiary_id,  amount	FROM transaction." + table_name + " where batch_id=?";

                    ps = conn.prepareStatement(query);
                    ps.setString(1, batchid);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        is_valid_beneficiary = true;
                        errormsg = "";
                        for (int n = 0; n < beneficiary.length(); n++) {
                            JSONObject paymentresponse = beneficiary.getJSONObject(n);
                            JSONObject detailspaymentobj = paymentresponse.getJSONObject("payment_response");
                            String ration_card_member_id = detailspaymentobj.optString("ration_card_member_id");
                            String supplementary = detailspaymentobj.optString("supplementary");
                            String transaction_amount = detailspaymentobj.optString("transaction_amount");
                            String transaction_date = detailspaymentobj.optString("transaction_date");
                            String transaction_ref_no = detailspaymentobj.optString("transaction_ref_no");
                            String transaction_status = detailspaymentobj.optString("transaction_status");
                            String unique_number = detailspaymentobj.optString("unique_number");
                            if (ration_card_member_id.equalsIgnoreCase(rs.getString("beneficiary_id"))) {
                                is_matched = true;
                                if (Double.parseDouble(transaction_amount) != rs.getDouble("amount")) {
                                    errormsg += "Amount Not Matched ";
                                    is_valid = false;
                                    is_valid_beneficiary = false;
                                } else if (transaction_status.equalsIgnoreCase("success")) {
                                    if (transaction_date.equals("") || transaction_ref_no.equals("") || !dateValidation(transaction_date)) {
                                        is_valid = false;
                                        is_valid_beneficiary = false;
                                        errormsg += "Transaction Date or Transaction Ref. No. Invalid";
                                    }
                                }
                                if (!is_valid_beneficiary) {
                                    JSONObject payment_details = new JSONObject();
                                    payment_details.put("unique_number", unique_number);
                                    payment_details.put("ration_card_member_id", ration_card_member_id);
                                    payment_details.put("transaction_amount", transaction_amount);
                                    payment_details.put("transaction_status", transaction_status);
                                    payment_details.put("transaction_date", transaction_date);
                                    payment_details.put("supplementary", supplementary);
                                    payment_details.put("transaction_ref_no", transaction_ref_no);
                                    payment_details.put("error_message", errormsg);
                                    JSONObject payment_response = new JSONObject();
                                    payment_response.put("payment_response", payment_details);
                                    error_beneficiary_array.put(payment_response);
                                }
//                                else {
//                                    JSONObject payment_details = new JSONObject();
//                                    payment_details.put("unique_number", unique_number);
//                                    payment_details.put("ration_card_member_id", ration_card_member_id);
//                                    payment_details.put("transaction_amount", transaction_amount);
//                                    payment_details.put("transaction_status", transaction_status);
//                                    payment_details.put("transaction_date", transaction_date);
//                                    payment_details.put("supplementary", supplementary);
//                                    payment_details.put("transaction_ref_no", transaction_ref_no);
//                                    payment_details.put("error_message", errormsg);
//                                    JSONObject payment_response = new JSONObject();
//                                    payment_response.put("payment_response", payment_details);
//                                    error_beneficiary_array.put(payment_response);
//                                }
                            }
                        }
                        error_object.put("beneficiary", error_beneficiary_array);

                        //rs.getDouble("amount");
                    }
                    System.out.println(error_object);
                    if (is_valid) {
                        ///validatation check
                        if (dbttype.equals("1")) {
                            DBTStatusUpdate.updateCashDBTResponseStatus(batchid,
                                    useridhash,
                                    total_beneficiary_count,
                                    total_transaction_amount,
                                    scheme_code,
                                    scheme_name,
                                    reference_date,
                                    remarks,
                                    success_beneficiary_count,
                                    success_amount,
                                    fail_beneficiary_count,
                                    fail_amount,
                                    filePath,
                                    jsonfilepath,
                                    beneficiary);
                        } else if (dbttype.equals("2")) {
                            //String storeId = (String) obj2.get("vlw_store_lgd");
                            //DBTStatusUpdate.updateKindDBTResponseStatus(storeId, batchid, beneficiary);
                        }
                    } else {
                        isSuccessFlag = false;
                    }

                } else {
                    isSuccessFlag = false;
                    is_duplicate = true;
                }

                //save to database insert_payment_response(useridhash text, encpaymentkey text, encdata text)
                //con = obj.getConnection();
            } catch (Exception ex) {
                isSuccessFlag = false;
            }

        } catch (Exception e) {
            isSuccessFlag = false;
            e.getMessage();
        } finally {
            if (isSuccessFlag && is_valid && !is_duplicate) {
                resultobj.put("status", "1");
                resultobj.put("error_code", "0");
                resultobj.put("result", "Success");
                if (!rsa_client_slno.equals("")) {
                    update_rsc_client_payment_response(rsa_client_slno, "success", resultobj.toString());
                }
            } else if (is_duplicate) {
                resultobj.put("status", "0");
                resultobj.put("result", "Fail");
                resultobj.put("error_code", "1");
                resultobj.put("error_message", "Already Received This Request.");
            } else if (!is_valid) {
                error_object.put("summary", summery);
                resultobj.put("status", "0");
                resultobj.put("result", "Fail");
                resultobj.put("error_code", "2");
                resultobj.put("error_message", "Validation Failed.");
                resultobj.put("detailed_error_message", error_object);
                if (!rsa_client_slno.equals("")) {
                    update_rsc_client_payment_response(rsa_client_slno, "fail", resultobj.toString());
                }
            } else {
                resultobj.put("status", "0");
                resultobj.put("result", "Fail");
                resultobj.put("error_code", "3");
                resultobj.put("error_message", "Invalid Request.");
                if (!rsa_client_slno.equals("")) {
                    update_rsc_client_payment_response(rsa_client_slno, "fail", resultobj.toString());
                }
            }

            return resultobj.toString();
        }
    }

    private static void update_rsc_client_payment_response(String slno, String status, String response_json) {
        Connection con = null;
        con = new DBCon.DBSource().connectToRSAClientDB().getConnection();
        PreparedStatement ps = null;
        try {

            String query = "UPDATE public.payment_response SET is_response_status=?, response_json=? WHERE slno=?::INTEGER;";
            ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setString(2, response_json);
            ps.setString(3, slno);
            ps.execute();
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static boolean dateValidation(String date) {
        boolean status = false;
        if (checkDate(date)) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(date);
                status = true;
            } catch (Exception e) {
                status = false;
            }
        }
        return status;
    }

    static boolean checkDate(String date) {
        String pattern = "(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";
        boolean flag = false;
        if (date.matches(pattern)) {
            flag = true;
        }
        return flag;
    }

    private static String getFileSizeKiloBytes(File file) {
        String s = (double) file.length() / 1024 + "";
        return s;
    }

    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
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

}
