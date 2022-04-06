/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcVerify;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Diptanu
 */
@WebServlet(name = "BeneficiaryDataEntry", urlPatterns = {"/BeneficiaryDataEntry"})
public class BeneficiaryDataEntry extends HttpServlet {

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
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String id = "", dept_code = "", userid = "", reqnumber = "", fullname = "", uniqueid = "", rc = "", dob = "", gender = "", caste = "", address_lgd_gp_vc_ward_code = "";
        String areaname = "", policestation = "", postoffice = "", pincode = "", bankacno = "", ifsc_code = "", beneficiary_category = "", beneficiary_sub_category = "", purpose = "", remarks = "";
        String email = "", mobile = "";
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        int myInt = sr.nextInt(1000000);
        reqnumber = String.format("%06d", myInt);
        if (request.getParameter("rcver") != null) {
            
                dept_code = "1503";
                userid = "ditbmstri";
                beneficiary_category = "3";
                beneficiary_sub_category = "4";
                purpose = "1";
            
            if (request.getParameter("fullname") != null) {
                fullname = request.getParameter("fullname");
            }
            
            if (request.getParameter("uniqueid") != null) {
                uniqueid = request.getParameter("uniqueid");
            }

            if (request.getParameter("bankacno") != null) {
                bankacno = request.getParameter("bankacno");
            }
            if (request.getParameter("ifsc_code") != null) {
                ifsc_code = request.getParameter("ifsc_code");
            }
                

            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String query1 = "INSERT INTO rcverify.rcvalidation (reqnumber,unique_id, entry_date) VALUES (?, ?,CURRENT_TIMESTAMP);";
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement(query1);
            ps.setString(1, reqnumber);
            ps.setString(2, uniqueid);
            ps.executeUpdate();
            //String msg = dept_code + "|" + userid + "|" + reqnumber + "|" + uniqueid + "|" + beneficiary_category + "|" + "http://localhost:8080/bms/DBTInitiator/rcverifyresponse.jsp";
            String msg = dept_code + "|" + userid + "|" + reqnumber + "|" + uniqueid + "|" + beneficiary_category + "|" + "https://bmsuat.trsc.nic.in/bms/DBTInitiator/rcverifyresponse.jsp";
            //deptcode + "|" + userid + "|" + reqnumber + "|" + uniqueid + "|" + beneficiary_category + "|" + uurl
            String appkey = "bc41d311f41cf6a474d6e1655ab48eae44b457788b4a5f131b405bd14cc40855";
            String hash = CreateToken(msg, appkey);
            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("</head>");
                out.println("<body onload=\"document.form1.submit()\">");
                //out.println("<form name=\"form1\"  method= \"post\" action=\"http://10.183.19.196/rcverify/preverifyprocess\" >");
                out.println("<form name=\"form1\"  method= \"post\" action=\"https://bmsuat.trsc.nic.in/rcverify/preverifyprocess\" >");

                out.println("<input type=\"hidden\" name=\"deptcode\"  value= \"" + dept_code + "\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"userid\"  value= \"" + userid + "\" >");//mandatorynote
                out.println("<input type=\"hidden\" name=\"reqnumber\"  value= \"" + reqnumber + "\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"fullname\"  value= \"" + fullname + "\" >");
                out.println("<input type=\"hidden\" name=\"uniqueid\"  value= \"" + uniqueid + "\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"dob\"  value= \"\" >"); //dd-mm-yyyy
                out.println("<input type=\"hidden\" name=\"gender\"  value= \"\" >");//M /F / T
                out.println("<input type=\"hidden\" name=\"caste\"  value= \"\" >"); //SC ST OBC Others
                out.println("<input type=\"hidden\" name=\"address_lgd_gp_vc_ward_code\"  value= \"\" >"); //254177

                out.println("<input type=\"hidden\" name=\"areaname\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"policestation\"  value= \"\" >"); //Belonia
                out.println("<input type=\"hidden\" name=\"postoffice\"  value= \"\" >"); //
                out.println("<input type=\"hidden\" name=\"pincode\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"bankacno\"  value= \"" + bankacno + "\" >"); //123456789
                out.println("<input type=\"hidden\" name=\"ifsc_code\"  value= \"" + ifsc_code + "\" >");  //SBIN0000001
                out.println("<input type=\"hidden\" name=\"beneficiary_category\"  value= \"" + beneficiary_category + "\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"beneficiary_sub_category\"  value= \"" + beneficiary_sub_category + "\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"purpose\"  value= \"" + purpose + "\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"remarks\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"email\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"mobile\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"rc\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"hash\"  value= " + hash + " >");//mandatory
                out.println("<input type=\"hidden\" name=\"uurl\"  value= \"http://localhost:8080/bms/DBTInitiator/rcverifyresponse.jsp\" >");//mandatory
                out.println("<input type=\"hidden\" name=\"additional_param1\"  value= \"\" >"); //addi1
                out.println("<input type=\"hidden\" name=\"additional_param2\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"additional_param3\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"additional_param4\"  value= \"\" >");
                out.println("<input type=\"hidden\" name=\"additional_param5\"  value= \"\" >");

                // out.println("<input type=\"submit\" name=\"eVerify\"  value= \"eVerify\" >");
                out.println("</form>");
                out.println("<center><h3>Please wait while we are redirecting...</h3></center>");
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.flush();
                out.close();
            }
        }
    }

    private String CreateToken(String message, String secret) {
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
        } catch (SQLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(BeneficiaryDataEntry.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(BeneficiaryDataEntry.class.getName()).log(Level.SEVERE, null, ex);
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

}
