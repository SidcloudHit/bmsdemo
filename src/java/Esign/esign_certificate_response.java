/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esign;

import Common.CommonMethod;
import DBTWorks.DBTStatusUpdate;
import DataTablesWorks.BeneficiaryDataAll;
import Master.Data.AdminUser;
import SessionPackage.LoginClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Arnab
 */
@WebServlet(name = "esign_certificate_response", urlPatterns = {"/esign_certificate_response"})
public class esign_certificate_response extends HttpServlet {

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
        //PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            Common.CommonMethod cm = new CommonMethod();
            Connection con = null;
            PreparedStatement psmt = null;
            int success = 0;
            String uId = "", message = "", signedPdf = "", requestID = "", applicationId = "", status = "";

            int distId = 0, subdivId = 0, officeId = 0, makerOfficeId = 0, checkerOfficeId = 0, blockId = 0, gpId = 0, storeId = 0, schemeId = 0, instl = 0, roleId = 0, deptId = 0;
            String schemeCode = "", finYr = "", gpList = "", batchId = "", userName = "", userDes = "";

            HttpSession session = request.getSession();

//            if (session.getAttribute("user_id") != null) {
            //uId = "20082600011";//session.getAttribute("user_id").toString();
//            } else {
//                message = "Session time-out, please login again.";
//                request.setAttribute("LogMsg", message);
//                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
//                return;
//            }
            signedPdf = request.getParameter("pdfB64signed");
            requestID = request.getParameter("requestID");
            status = request.getParameter("status");
            int applIdLen = Integer.parseInt(requestID.substring(0, 1));

            applicationId = requestID.substring(1, (applIdLen + 1));
            //String applicationId = session.getAttribute("applicationId").toString();
            //applicationId  = Double.valueOf(session.getAttribute("applicationId").toString());

            if (con == null) {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }

            psmt = con.prepareStatement("INSERT INTO esign.tran_esign_response("
                    + "request_id, application_id, esign_txn_id, signed_by, pdfb64_signed, status, txn_timestamp, signer_certificateb64, hash, error_msg, response_code)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            psmt.setString(1, requestID);
            psmt.setInt(2, Integer.parseInt(applicationId));
            psmt.setString(3, request.getParameter("eSignTxnID"));
            psmt.setString(4, request.getParameter("signedBy"));
            psmt.setString(5, signedPdf);
            psmt.setString(6, status);
            psmt.setString(7, request.getParameter("txnTimestamp"));
            psmt.setString(8, request.getParameter("signerCertificateB64"));
            psmt.setString(9, request.getParameter("hash"));
            psmt.setString(10, request.getParameter("errorMsg"));
            psmt.setString(11, request.getParameter("responseCode"));
            success = psmt.executeUpdate();

            if (success > 0 && status.equals("success")) //saved successful in transaction_response table
            {
                EsignMethods esign = new EsignMethods();//changed by arindam 
                String reqDets = esign.getRequesterId(Integer.parseInt(applicationId), 1);
                uId = reqDets.split("#")[0];//changed by arindam
                roleId = Integer.parseInt(reqDets.split("#")[2]);
                schemeId = Integer.parseInt(reqDets.split("#")[1]);
                officeId = Integer.parseInt(reqDets.split("#")[3]);

                String fileSignedPDFName = requestID + ".pdf";
                long fileSignedPDFSize = signedPdf.getBytes().length;
                String fileSignedPDFType = "application/pdf";
                int fileSignedPDFFid = cm.uploadEsignFile(signedPdf, uId, fileSignedPDFName, fileSignedPDFSize, fileSignedPDFType);

                //int update_status = esign.updateEsignApplicationInitiation(Integer.parseInt(applicationId), fileSignedPDFFid, uId);
                int update_status = esign.updateEsignApplicationInitiation(Integer.parseInt(applicationId), fileSignedPDFFid, uId, 1); //changed by arindam
                if (update_status > 0) {

                    //AdminUser usr = new AdminUser(uId);
//                    if (session.getAttribute("loginInfo") != null) {
//
//                        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");
//
//                        roleId = Integer.parseInt(loginObj.getUserRole());
//                    }
                    DBTStatusUpdate.updateCashDBTSignStatus(Integer.parseInt(applicationId), roleId);

                    //////condition check for user role for approver
                    String query = "INSERT INTO esign.beneficiary_approve_esign_json_file_flag(application_id, is_filegenerated) VALUES (?, ?);";
                    if (con == null) {
                        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    }
                    PreparedStatement ps = null;
                    try {

                        ps = con.prepareStatement(query);
                        ps.setInt(1, Integer.parseInt(applicationId));
                        ps.setBoolean(2, false);
                        ps.executeUpdate();

                    } catch (SQLException ex) {
                        Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {

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

                    String userSessionDets = cm.getUserSessionDetails(uId);
                    userName = userSessionDets.split("#")[0];
                    deptId = Integer.parseInt(userSessionDets.split("#")[1]);
                    userDes = userSessionDets.split("#")[2];

                    SessionPackage.LoginClass loginInfo = new LoginClass();
                    loginInfo.setUserId(uId);
                    loginInfo.setUserDeptId(Integer.toString(deptId));
                    loginInfo.setUserName(userName);
                    loginInfo.setUserDesignation(userDes);
                    loginInfo.setUserDeptName(cm.getDepartmentName(deptId));
                    loginInfo.setUserScheme(Integer.toString(schemeId));
                    loginInfo.setUserSchemeName(cm.getSchemeName(schemeId));
                    loginInfo.setUserRole(Integer.toString(roleId));
                    loginInfo.setUserRoleName(cm.getRoleName(roleId));
                    loginInfo.setUserOfficeId(Integer.toString(officeId));
                    loginInfo.setUserOfficeName(cm.getOfficeName(officeId));
                    session = request.getSession(true);
                    session.setAttribute("loginInfo", loginInfo);

                    response.sendRedirect("Reports/esignResponse.jsp?fid=" + fileSignedPDFFid);

//                    EsignWorks ew = new EsignWorks();
//                    byte[] gen_pdf = ew.getPDFData(fileSignedPDFFid);
//                    ServletOutputStream os = response.getOutputStream();
//                    response.setContentType("application/pdf");
//                    os.write(gen_pdf);
//                    os.flush();
//                    os.close();
                }

            }
            //response.sendRedirect(request.getContextPath() + "//beneficiarySelectionFreezeNEsign.jsp");
            /*out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title> eSignResponse</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h5> requestID " + request.getParameter("requestID")+ "</h5> <br>");
            out.println("<h5> eSignTxnID " + request.getParameter("eSignTxnID") + "</h5> <br>");
            out.println("<h5> signedBy " + request.getParameter("signedBy") + "</h5> <br>");
            out.println("<h5> pdfB64signed " + request.getParameter("pdfB64signed") + "</h5> <br>");
            out.println("<h5> status " + request.getParameter("status") + "</h5> <br>");
            out.println("<h5> txnTimestamp " + request.getParameter("txnTimestamp") + "</h5> <br>");
            out.println("<h5> signerCertificateB64" + request.getParameter("signerCertificateB64") + "</h5> <br>");
            out.println("<h5> hash " + request.getParameter("hash") + "</h5> <br>");
            out.println("<h5> errorMsg " + request.getParameter("errorMsg") + "</h5> <br>");
            out.println("<h5> responseCode " + request.getParameter("responseCode") + "</h5> <br>");
            
            //out.println("<br><br><br><br><h5> eSignResponse at " + request.toString()+ "</h5> <br>");
            
            out.println("</body>");
            out.println("</html>");*/

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //out.close();
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
