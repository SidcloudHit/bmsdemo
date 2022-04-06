/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esign;

import Common.CommonMethod;
import DBTWorks.DBTStatusUpdate;
import com.itextpdf.text.pdf.codec.Base64;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Arnab
 */
@WebServlet(name = "esign_certificate_request", urlPatterns = {"/esign_certificate_request"})
public class esign_certificate_request extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(true);
            int applicationId = 0;

            Common.CommonMethod cm = new CommonMethod();

            //get pdf from DB
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet res = null;
            String strSQL = "";
            String avl_report = "", encodedBytes = "";
            int success = 0, serviceId = 0, purpose = 0;
            String message = "", deptCode = "", requestURL = "", aspCode = "", responseURL = "", appKey = "", deptUID = "", serviceID = "";
            String uniqueReqId = "", msg = "", hash = "", empName = "", esignApplTabName = "";
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
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

            if (session.getAttribute("esignApplId") != null) {
                applicationId = Integer.parseInt(session.getAttribute("esignApplId").toString());
                session.removeAttribute("esignApplId");
            } else if (request.getParameter("esignApplId") != null) {
                applicationId = Integer.parseInt(request.getParameter("esignApplId"));
            }

            purpose = Integer.parseInt(request.getParameter("purpose"));
            serviceId = Integer.parseInt(request.getParameter("serviceId"));
            empName = userName;//"SOURABH DEBNATH";//"ARINDAM MUKHERJEE";//

            if (con == null) {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }

            if (purpose == 1) {
                esignApplTabName = "esign.dbt_esign_application_details";
            } else if (purpose == 2) {
                esignApplTabName = "esign.beneficiary_esign_application_details";
            }

            strSQL = " SELECT pdf_report_fid FROM " + esignApplTabName + " WHERE application_id=? ";

            ps = con.prepareStatement(strSQL);
            ps.setInt(1, applicationId);
            res = ps.executeQuery();
            if (res.next()) {//get pdf..........
                avl_report = cm.getEsignFile(res.getInt("pdf_report_fid"));
            }
            encodedBytes = URLEncoder.encode(avl_report, "UTF-8");
            strSQL = " SELECT dept_code, asp_code, user_id, service_id, app_key, request_url, response_url FROM esign.mas_esign_param where service_id =?";
            ps = con.prepareStatement(strSQL);
            ps.setString(1, String.valueOf(serviceId));
            res = ps.executeQuery();
            if (res.next()) {//Get admin data..........
                //organizationId = res.getString("organization_id");
                deptCode = res.getString("dept_code").trim();
                requestURL = res.getString("request_url").trim();
                aspCode = res.getString("asp_code").trim();
                responseURL = res.getString("response_url").trim();
                appKey = res.getString("app_key").trim();
                deptUID = res.getString("user_id").trim();
                serviceID = res.getString("service_id").trim();
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long batchId = timestamp.getTime();

            uniqueReqId = Integer.toString(applicationId).length() + Integer.toString(applicationId) + String.valueOf(batchId);

            msg = uniqueReqId + "|" + deptUID + "|" + encodedBytes + "|" + empName + "|" + aspCode + "|" + serviceID + "|" + responseURL; //dynamic
            hash = CreateToken(msg, appKey);

            /*insert details into transaction table*/
            ps = con.prepareStatement("INSERT INTO esign.tran_esign_request("
                    + "request_id, dept_code, user_id, service_id, asp_code, encoded_bytes,signer_name, hash, msg, entry_by)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

            ps.setString(1, uniqueReqId);
            ps.setString(2, deptCode);
            ps.setString(3, deptUID);
            ps.setString(4, serviceID);
            ps.setString(5, aspCode);
            ps.setString(6, encodedBytes);
            ps.setString(7, empName);
            ps.setString(8, hash);
            ps.setString(9, msg);
            ps.setString(10, userId);
            success = ps.executeUpdate();

            /*insert details into transaction table end*/
            if (success > 0) {// success 
                out.println("<html>");
                out.println("<head>");
                out.println("<script>");
                out.println(" function disableSubmit() {");
                out.println(" document.getElementById(\"submit\").disabled = true;");
                out.println(" }");
                out.println(" function activateButton(element) {");
                out.println("  if(element.checked) {");
                out.println("   document.getElementById(\"submit\").disabled = false;");
                out.println("  }");
                out.println("  else{");
                out.println("    document.getElementById(\"submit\").disabled = true;");
                out.println("  }");
                out.println(" }");
                out.println("</script>");
                out.println("</head>");
                out.println("<body onload=\"disableSubmit()\">");
                out.println("<form name=\"form1\" target=\"_top\"  method= \"post\" action=\"" + requestURL + "\" >");
                out.println("<input type=\"hidden\" name=\"requestID\"  value= \"" + uniqueReqId + "\" >");//dynamic
                out.println("<input type=\"hidden\" name=\"userID\"  value= \"" + deptUID + "\" >");
                out.println("<input type=\"hidden\" name=\"pdfB64\"  value= \"" + encodedBytes + "\" >");//dynamic
                out.println("<input type=\"hidden\" name=\"signerName\"  value= \"" + empName + "\" >");//dynamic
                out.println("<input type=\"hidden\" name=\"deptID\"  value= \"" + deptCode + "\" >");
                out.println("<input type=\"hidden\" name=\"serviceID\"  value= \"" + serviceID + "\" >");
                out.println("<input type=\"hidden\" name=\"aspCode\"  value= \"" + aspCode + "\" >");
                out.println("<input type=\"hidden\" name=\"responseURL\"  value= \"" + responseURL + "\" >");
                out.println("<input type=\"hidden\" name=\"hash\"  value= \"" + hash + "\" >");
                out.println("<p align=\"center\"><strong>Consent for Authentication</strong></p><p align=\"center\"><em>National Informatics Centre</em></p><p align=\"justify\">I hereby state that I have no objection in authenticating myself with Aadhaar based authentication system and consent to providing my Aadhaar number, Biometric and/or One Time Pin (OTP) data for Aadhaar based authentication for the purposes of availing of the ______<u><em>nic-eSign Gateway</em></u>____ from ____<u><em>National Informatics Centre</em></u>____. I understand that the Biometrics and/or OTP I provide for authentication shall be used only for authenticating my identity through the Aadhaar Authentication system, for obtaining my e-KYC through Aadhaar e-KYC service and for the issuance of Digital Signature Certificate (DSC) for this specific transaction and for no other purposes. For the creation of DSC, I understand that the options that I have chosen are the ones that shall be populated in the DSC generated by the CA and I provide my consent for the same. I also understand that the following fields in the DSC generated by the CA are mandatory and I give my consent for using the Aadhaar provided e-KYC information to populate the corresponding fields in the DSC.</p><p align=\"justify\"><ul><li>Common Name (name as obtained from e-KYC)</li><li>Unique Identifier (hash of Aadhaar number)</li><li>Pseudonym (unique code sent by UIDAI in e-KYC response)</li><li>State or Province (state as obtained from e-KYC)</li><li>Postal Code (postal code as obtained from e-KYC)</li><li>Telephone Number (hash of phone as obtained from e-KYC)</li></ul></p><palign=\\\"justify\\\">I understand that ____<u><em>National Informatics Centre</em></u>____ shall ensure security and confidentiality of my personal identity data provided for the purpose of Aadhaar based authentication.<br/><br/></p>");
                out.println("<input type=\"checkbox\" name=\"terms\" id=\"terms\" onchange=\"activateButton(this)\">  I Agree with Terms & Coditions");
                out.println("<input type=\"submit\" name=\"submit\"  value= \"Proceed\" >");
                out.println("</form>");
                // out.println("<center><h3>Please wait while we are redirecting...</h3></center>");
                out.println("</body>");
                out.println("</html>");

                //DBTStatusUpdate.updateCashDBTSignStatus(applicationId, uId);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }

    public static String CreateToken(String message, String secret) {
        String hash = "";
        try {
            // String secret = secret;
            // String message = message;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));

        } catch (Exception e) {
            System.out.println("Error");
        }
        return hash;
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
