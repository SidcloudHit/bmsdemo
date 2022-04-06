/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeneficiarySelection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author FINANCE-PC
 */
@WebServlet(name = "BeneficiaryDataRecord", urlPatterns = {"/BeneficiaryDataRecord"})
public class BeneficiaryDataRecord extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String dept_code = "", userid = "", message = "", reqnumber = "", fullname = "", uniqueid = "", rc = "", dob = "", gender = "", caste = "", address_lgd_gp_vc_ward_code = "";
        String areaname = "", policestation = "", postoffice = "", pincode = "", bankacno = "", ifsc_code = "", beneficiary_category = "", beneficiary_sub_category = "", purpose = "", remarks = "";
        String email = "", mobile = "", appkey = "", app_url = "", response_url = "", scheme = "", sl_id = "", schemename = "";
        int update;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
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
        try {

//        if (request.getParameter("dept_code") != null) {
//            dept_code = request.getParameter("dept_code");
//        }
//        if (request.getParameter("userid") != null) {
//            userid = request.getParameter("userid");
//        }
            if (request.getParameter("reqnumber") != null) {
                reqnumber = Security.SecurityClass.killchar(request.getParameter("reqnumber"));
            }
            if (request.getParameter("fullname") != null) {
                fullname = Security.SecurityClass.killchar(request.getParameter("fullname"));
            }
            if (request.getParameter("uniqueid") != null) {
                uniqueid = Security.SecurityClass.killchar(request.getParameter("uniqueid"));
            }
            if (request.getParameter("rc") != null) {
                rc = Security.SecurityClass.killchar(request.getParameter("rc"));
            }
            if (request.getParameter("ddlScheme") != null) {
                scheme = Security.SecurityClass.killchar(request.getParameter("ddlScheme"));
                schemename = request.getParameter("ddlSchemeName");
            }
            if (request.getParameter("dob") != null) {
                dob = Security.SecurityClass.killchar(request.getParameter("dob"));
            }
            if (request.getParameter("gender") != null) {
                gender = Security.SecurityClass.killchar(request.getParameter("gender"));
            }

            if (request.getParameter("caste") != null) {
                caste = Security.SecurityClass.killchar(request.getParameter("caste"));
            }
            if (request.getParameter("address_lgd_gp_vc_ward_code") != null) {
                address_lgd_gp_vc_ward_code = Security.SecurityClass.killchar(request.getParameter("address_lgd_gp_vc_ward_code"));
            }
            if (request.getParameter("areaname") != null) {
                areaname = Security.SecurityClass.killchar(request.getParameter("areaname"));
            }
            if (request.getParameter("policestation") != null) {
                policestation = Security.SecurityClass.killchar(request.getParameter("policestation"));
            }
            if (request.getParameter("postoffice") != null) {
                postoffice = Security.SecurityClass.killchar(request.getParameter("postoffice"));
            }
            if (request.getParameter("pincode") != null) {
                pincode = Security.SecurityClass.killchar(request.getParameter("pincode"));
            }
            if (request.getParameter("bankacno") != null) {
                bankacno = Security.SecurityClass.killchar(request.getParameter("bankacno"));
            }
            if (request.getParameter("ifsc_code") != null) {
                ifsc_code = Security.SecurityClass.killchar(request.getParameter("ifsc_code"));
            }
            if (request.getParameter("beneficiary_category") != null) {
                beneficiary_category = Security.SecurityClass.killchar(request.getParameter("beneficiary_category"));
            }
            if (request.getParameter("beneficiary_sub_category") != null) {
                beneficiary_sub_category = Security.SecurityClass.killchar(request.getParameter("beneficiary_sub_category"));
            }
            if (request.getParameter("purpose") != null) {
                purpose = Security.SecurityClass.killchar(request.getParameter("purpose"));
            }
            if (request.getParameter("remarks") != null) {
                remarks = Security.SecurityClass.killchar(request.getParameter("remarks"));
            }
            if (request.getParameter("email") != null) {
                email = Security.SecurityClass.killchar(request.getParameter("email"));
            }
            if (request.getParameter("mobile") != null) {
                mobile = Security.SecurityClass.killchar(request.getParameter("mobile"));
            }

            String[] sch = scheme.split("~");
            schemeId = Integer.parseInt(sch[0]);
            String schemecode = sch[1];
            //String schemename = sch[2];
            String query1 = "INSERT INTO rc_verification (rc, schemeid ,userid, reqnumber, entry_date) VALUES (?,?,?, ?,CURRENT_TIMESTAMP);";
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            ps = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, rc);
            ps.setString(2, schemecode);
            ps.setString(3, userId);
            ps.setString(4, reqnumber);

            update = ps.executeUpdate();
            if (update > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {

                    sl_id = rs.getString("id");
                }
            }
            uniqueid = sl_id;

            String sql = "SELECT * FROM public.tbl_integration_info where app_name_flag='rcverify'";
            ps1 = con.prepareStatement(sql);
            rs = ps1.executeQuery();
            while (rs.next()) {
                dept_code = rs.getString("dept_code");
                userid = rs.getString("user_id");
                appkey = rs.getString("unique_key");
                app_url = rs.getString("app_url");
                // response_url = rs.getString("response_url");
            }
            
            ResourceBundle resource = ResourceBundle.getBundle("Common/config");
            response_url = resource.getString("rcverify.response.url");

            //http://10.183.5.206:8084/rcverify/preverifyprocess
            //response_url = "http://localhost:8080/bms/SaveRCResponse";
//            response_url = "https://bmsuat.trsc.nic.in/bms/SaveRCResponse";
            //response_url = "https://bms.tripura.gov.in/bms/SaveRCResponse";
            String msg = dept_code + "|" + userid + "|" + reqnumber + "|" + uniqueid + "|" + beneficiary_category + "|" + response_url;
            Common.CommonMethod deocm = new Common.CommonMethod();
            String hash = deocm.createToken(msg, appkey);

            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("</head>");
            out.println("<body onload=\"document.form1.submit()\">");
            out.println("<form name=\"form1\"  method= \"post\" action=\"" + app_url + "\" >");

            out.println("<input type=\"hidden\" name=\"deptcode\"  value= \"" + dept_code + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"userid\"  value= \"" + userid + "\" >");//mandatorynote
            out.println("<input type=\"hidden\" name=\"reqnumber\"  value= \"" + reqnumber + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"fullname\"  value= \"" + fullname + "\" >");
            out.println("<input type=\"hidden\" name=\"uniqueid\"  value= \"" + uniqueid + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"dob\"  value= \"" + dob + "\" >"); //dd-mm-yyyy
            out.println("<input type=\"hidden\" name=\"gender\"  value= \"" + gender + "\" >");//M /F / T
            out.println("<input type=\"hidden\" name=\"caste\"  value= \"" + caste + "\" >"); //SC ST OBC Others
            out.println("<input type=\"hidden\" name=\"address_lgd_gp_vc_ward_code\"  value= \"" + address_lgd_gp_vc_ward_code + "\" >"); //254177

            out.println("<input type=\"hidden\" name=\"areaname\"  value= \"" + areaname + "\" >");
            out.println("<input type=\"hidden\" name=\"policestation\"  value= \"" + policestation + "\" >"); //Belonia
            out.println("<input type=\"hidden\" name=\"postoffice\"  value= \"" + postoffice + "\" >"); //
            out.println("<input type=\"hidden\" name=\"pincode\"  value= \"" + pincode + "\" >");
            out.println("<input type=\"hidden\" name=\"bankacno\"  value= \"" + bankacno + "\" >"); //123456789
            out.println("<input type=\"hidden\" name=\"ifsc_code\"  value= \"" + ifsc_code + "\" >");  //SBIN0000001
            out.println("<input type=\"hidden\" name=\"beneficiary_category\"  value= \"" + beneficiary_category + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"beneficiary_sub_category\"  value= \"" + beneficiary_sub_category + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"purpose\"  value= \"" + purpose + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"remarks\"  value= \"" + remarks + "\" >");
            out.println("<input type=\"hidden\" name=\"email\"  value= \"" + email + "\" >");
            out.println("<input type=\"hidden\" name=\"mobile\"  value= \"" + mobile + "\" >");
            out.println("<input type=\"hidden\" name=\"rc\"  value= \"" + rc + "\" >");
            out.println("<input type=\"hidden\" name=\"hash\"  value= " + hash + " >");//mandatory
            out.println("<input type=\"hidden\" name=\"uurl\"  value= \"" + response_url + "\" >");//mandatory
            out.println("<input type=\"hidden\" name=\"additional_param1\"  value= \"" + schemecode + "\" >"); //addi1
            out.println("<input type=\"hidden\" name=\"additional_param2\"  value= \"" + schemename + "\" >");
            out.println("<input type=\"hidden\" name=\"additional_param3\"  value= \"" + schemeId + "\" >");
            out.println("<input type=\"hidden\" name=\"additional_param4\"  value= \"\" >");
            out.println("<input type=\"hidden\" name=\"additional_param5\"  value= \"\" >");

            // out.println("<input type=\"submit\" name=\"eVerify\"  value= \"eVerify\" >");
            out.println("</form>");
            out.println("<center><h3>Please wait while we are redirecting...</h3></center>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
            Logger.getLogger(RcDataValidation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.flush();
            // out.close();
            con.close();
            ps.close();
            ps1.close();
            rs.close();
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(BeneficiaryDataRecord.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BeneficiaryDataRecord.class.getName()).log(Level.SEVERE, null, ex);
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
