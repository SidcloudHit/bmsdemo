<%-- 
    Document   : rcverifyresponse
    Created on : Aug 10, 2020, 5:09:16 PM
    Author     : Diptanu
--%>

<%@page import="Common.CommonMethod"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="org.apache.tomcat.util.codec.binary.Base64"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="javax.crypto.Mac"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Response</title>
    </head>
    <body>
        <script>
            // var a = '<% out.print(request.getParameter("fullname_ll")); %>';
            // decode
            // alert(decodeURI(a));
        </script>


        <%
            String rc_mem="", guardian_name = "", errormsg = "", userid = "", reqnumber = "", fullname = "", uniqueid = "", rc = "", dob = "", gender = "", caste = "", address_lgd_gp_vc_ward_code = "";
            String areaname = "", relation_with_guardian = "", postoffice = "", pincode = "", bankacno = "", ifsc_code = "", beneficiary_category = "", beneficiary_sub_category = "", purpose = "", remarks = "";
            String email = "", mobile = "", txt_ref = "", timestamp = "", status = "";
            String msg = "";
            if (request.getParameter("reqnumber") != null) {
                reqnumber = request.getParameter("reqnumber");
            }
            if (request.getParameter("txn_ref_no") != null) {
                txt_ref = request.getParameter("txn_ref_no");
            }
            if (request.getParameter("tdate") != null) {
                timestamp = request.getParameter("tdate");
            }
            if (request.getParameter("status") != null) {
                status = request.getParameter("status");
            }
            if (request.getParameter("errormsg") != null) {
                errormsg = request.getParameter("errormsg");
            }
            if (request.getParameter("uniqueid") != null) {
                uniqueid = request.getParameter("uniqueid");
            }
            if (request.getParameter("rationcard_memberid") != null) {
                rc_mem = request.getParameter("rationcard_memberid");
            }
            if (request.getParameter("fullname") != null) {
                fullname = request.getParameter("fullname");
            }
            if (request.getParameter("guardian_name") != null) {
                guardian_name = request.getParameter("guardian_name");
            }
            if (request.getParameter("relation_with_guardian") != null) {
                relation_with_guardian = request.getParameter("relation_with_guardian");
            }
            if (request.getParameter("dob") != null) {
                dob = request.getParameter("dob");
            }
            if (request.getParameter("gender") != null) {
                gender = request.getParameter("gender");
            }
            if (request.getParameter("caste") != null) {
                caste = request.getParameter("caste");
            }
            if (request.getParameter("address_lgd_gp_vc_ward_code") != null) {
                address_lgd_gp_vc_ward_code = request.getParameter("address_lgd_gp_vc_ward_code");
            }
            if (request.getParameter("pincode") != null) {
                pincode = request.getParameter("pincode");
            }
            if (request.getParameter("email") != null) {
                email = request.getParameter("email");
            }
            if (request.getParameter("mobile") != null) {
                mobile = request.getParameter("mobile");
            }

            //reqnumber + "|" + txn_ref_no + "|" + status + "|" + tdate 
            msg = reqnumber + "|" + txt_ref + "|" + status + "|" + timestamp;

            String appkey = "bc41d311f41cf6a474d6e1655ab48eae44b457788b4a5f131b405bd14cc40855";
            String hashResponse = CreateToken(msg, appkey);

            String hashGet = request.getParameter("hash");
            //out.println(msg);
            // out.println("<br>HasthGet  -"+hashGet);
            //  out.println(" <br/>hashResponse -"+hashResponse);
            if (hashResponse.equals(hashGet)) {
                out.print("Hasth Match");
            } else {
                out.print("Hasth Not Match");
            }


        %> 



        <%  //URLDecoder.decode(request.getParameter("fullname_ll"), StandardCharsets.UTF_8.toString()) 
            int data_status = 0;
            if (errormsg.equals("")) {
                Connection con = null;
                Connection con1 = null;
                PreparedStatement ps = null;
                PreparedStatement ps1 = null;
                ResultSet rs = null;
                String query1 = "UPDATE public.beneficiary_cigt8_data_temp SET rc_mem=?, rc_no=? WHERE tempid=?";
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                try {
                    ps = con.prepareStatement(query1);
                    ps.setString(1, rc_mem);
                    ps.setString(2, rc);
                    ps.setString(3, uniqueid);

                    data_status = ps.executeUpdate();
                     if (data_status > 0) {
                         String query = "UPDATE rcverify.rcvalidation SET txtrefno=?,status=?,entry_date=CURRENT_TIMESTAMP WHERE unique_id=? AND reqnumber=?  ";
                con = new DBCon.DBSource().connectToBMSDB().getConnection();
                    ps1 = con.prepareStatement(query);
                    ps1.setString(1, txt_ref);
                    ps1.setString(2, status);
                    ps1.setString(3, uniqueid);
                    ps1.setString(4, reqnumber);
                    ps1.executeUpdate();
                        out.println("<script type=\"text/javascript\">");
                        out.println("location='beneficiaryRCVerification.jsp';");
                        out.println("</script>");
                    }
                   
                } catch (SQLException ex) {
                    Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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
            } else {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Error:Please Check the Data.');");
                out.println("location='beneficiaryRCVerification.jsp';");
                out.println("</script>");
            }
        %>
    </body>
</html>
<%!
    public String CreateToken(String message, String secret) {
        String hash = "";
        try {
            // String secret = secret;
            // String message = message;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));

        } catch (Exception e) {
            System.out.println("Error");
        }
        return hash;

    }
%>
