/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import notification.otpgenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Banti
 */
@WebServlet(name = "forgetPassword", urlPatterns = {"/forgetPassword"})
public class forgetPassword extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        PrintWriter out = response.getWriter();
        String user_email = "", email_otp = "", mobile_otp = "", created_on = "", flag = "", uemail = "";
        String user_details = "", mobile_no = "", user_id = "";
        flag = request.getParameter("flag");
        if (request.getParameter("user_email") != null) {
            user_email = Security.SecurityClass.killchar(request.getParameter("user_email"));
        }
        if (request.getParameter("txtemailotp") != null) {
            email_otp = Security.SecurityClass.killchar(request.getParameter("txtemailotp"));
        }
        if (request.getParameter("txtmobileotp") != null) {
            mobile_otp = Security.SecurityClass.killchar(request.getParameter("txtmobileotp"));
        }
        String stat = "", msg = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CommonMethod cm = new CommonMethod();
        boolean status = false;
        String email_regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        
        if ("1".equals(flag)) {

            if (user_email.matches(email_regex)) {
                status = cm.checkEmailExists(user_email);
            } else {
                status = cm.checkMobileExists(user_email);
            }
            if (status == true) {
                user_details = cm.getUserDetails(user_email);
                mobile_no = user_details.split("~")[0];
                user_id = user_details.split("~")[1];
                uemail = user_details.split("~")[2];
                otpgenerator otpcls = new otpgenerator();
                int otp = otpcls.generateotp(10000);
                String message = "";
                //  int mobileotp=otpcls.generateotp(1000);
                int savestatus = otpcls.saveintoDB(otp, otp, uemail, mobile_no, "999", 10);
                if (savestatus > 0) {
                    String regex = ".(?=.{4})";
                    String subst = "*";
                    Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(mobile_no);
                    String mask_mobile = matcher.replaceAll(subst);

                    String email_mask = "(?<=.{3}).(?=[^@]*?@)";
                    Pattern email_pattern = Pattern.compile(email_mask);
                    Matcher email_matcher = email_pattern.matcher(uemail);
                    String email_mask_result = email_matcher.replaceAll(subst);

                    String displaypassword = "OTP have been sent to the registered mobile no <strong>" + mask_mobile + "</strong> and email ID <strong>" + email_mask_result + "</strong> through sms and email.";

                    //Notification.otpgenerator otg = new Notification.otpgenerator();
                    //int otp = otg.generateotp(10000);
                    String smsmsg="OTP is "+otp+" to reset your password. - BMS Portal Tripura";
                   // String smsmsg = "OTP is "+otp+" to reset your password. - BMS Portal Tripura"; //"OTP is " + otp + " to reset your password. - BMS Portal Tripura";
                    notification.notify ntify = new notification.notify();
                    ntify.notifysms(smsmsg, mobile_no, "1007161822551668253", "1001640530000017041");
                    ntify.notifyemail(smsmsg, uemail, "Password Reset in the BMS Portal");

                    message = "OTP Sent Successfully.<br/> " + displaypassword + "";
                }
                out.print(message);
//                session.invalidate();
//                session = request.getSession(true);
//                session.setAttribute("email", user_email);
//                session.setAttribute("mobile", mobile_no);
//                response.sendRedirect("User/forgotPassword.jsp");
//
//                return;
            } else {
                out.print("notok");
//                msg = "User ID Not Found. Please check your User ID.";
//                session.setAttribute("RegMsg", msg);
//                session.setAttribute("type", "danger");
//                response.sendRedirect("User/forgotPassword.jsp");
//                return;
            }

            // out.print(status+"~"+mobile_no);
            //  out.flush();
            //  out.close();
        } else {
            if (!"".equals(email_otp) && !"".equals(mobile_otp)) {
                user_details = cm.getUserDetails(user_email);
                mobile_no = user_details.split("~")[0];
                user_id = user_details.split("~")[1];
                uemail = user_details.split("~")[2];
                String otp_status = cm.checkOtp(uemail, email_otp, mobile_otp);
                if (otp_status.equals("")) {
                    msg = "OTP Mismatch !";
                    session.setAttribute("RegMsg", msg);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
                }

                try {
                    con = new DBCon.DBSource().connectToBMSDB().getConnection();
                    ps = con.prepareStatement("SELECT count(*) as otp FROM citizen.tbl_otp WHERE email = '" + uemail + "' AND email_otp='" + email_otp + "' AND mobile_otp='" + mobile_otp + "' AND expired_on > CURRENT_TIMESTAMP");
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        if (rs.getInt("otp") > 0) {
                            stat = "yes";
                        }
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

                if ("yes".equals(stat)) {
                    msg = "OTP Matched Successfully";
                    session.setAttribute("email", uemail);
                    response.sendRedirect("User/forgotPassChange.jsp");
                } else {
                    msg = "OTP Expired !";
                    session.setAttribute("RegMsg", msg);
                    session.setAttribute("type", "danger");
                    session.setAttribute("email", "");
                    response.sendRedirect("User/forgotPassword.jsp");
                    return;
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

}
