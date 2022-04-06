/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esign;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "EsignWorks", urlPatterns = {"/EsignWorks"})
public class EsignWorks extends HttpServlet {

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

        try {
            Common.CommonMethod cm = new CommonMethod();
            HttpSession session = request.getSession();
            //Connection con = null;
            int officeId = 0, schemeId = 0, instl = 0, status = 0, checkerOfficeId = 0;
            String schemeCode = "", finYr = "", message = "", instlName = "";
            int deptId = 0, roleId = 0;
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

            EsignMethods esign = new EsignMethods();

            if (request.getParameter("search") != null) {
                int searchType = Integer.parseInt(request.getParameter("search"));
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();

                int fid = 0, applId = 0;
                if (request.getParameter("schemeId") != null) {
                    String scheme = request.getParameter("schemeId");
                    schemeId = Integer.parseInt(scheme.split("~")[0]);
                    schemeCode = scheme.split("~")[1];
                }

                if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                    officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
                }
                if (searchType == 1 || searchType == 2) {

                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                    }

                    //int stype = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("search")));
                    int dbtType = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbttype")));

                    if (searchType == 1) {
                        if (dbtType == 1) {
                            String result = esign.checkIfSignedFileExists(officeId, schemeId, finYr, instl, userId);
                            applId = Integer.parseInt(result.split("#")[0]);
                            fid = Integer.parseInt(result.split("#")[1]);
                        }
                    } else if (searchType == 2) {
                        if (dbtType == 1) {
                            String result = esign.checkIfSignedFileExists(officeId, schemeId, finYr, instl, userId);
                            applId = Integer.parseInt(result.split("#")[0]);
                            fid = Integer.parseInt(result.split("#")[1]);
                        }
                    }

                    //if (fid > 0) {
                    out.print(fid);
                    //}
                } else if (searchType == 3 || searchType == 4) {
                    if (searchType == 3) {
                        String result = esign.checkIfSignedBenFileExists(officeId, schemeId, userId);
                        applId = Integer.parseInt(result.split("#")[0]);
                        fid = Integer.parseInt(result.split("#")[1]);
                    } else if (searchType == 4) {
                        String result = esign.checkIfFreezedBenFileExists(officeId, schemeId, userId);
                        applId = Integer.parseInt(result.split("#")[0]);
                        fid = Integer.parseInt(result.split("#")[1]);
                    }
                    session.setAttribute("esignApplId", applId);
                    //if (fid > 0) {
                    out.print(fid);
                    //}
                }

            }

            if (request.getParameter("benFileList") != null) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();

                Connection con = null;
                if (request.getParameter("schemeId") != null) {
                    String scheme = request.getParameter("schemeId");
                    schemeId = Integer.parseInt(scheme.split("~")[0]);
                    schemeCode = scheme.split("~")[1];
                }

                if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                    officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
                }
//                String sql = "SELECT application_id, scheme_name, final_beneficiary_count,1 AS type, pdf_report_fid AS fid, '' AS txn_timestamp FROM esign.beneficiary_esign_application_details a "
//                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
//                        + "WHERE office_id = " + officeId + " AND scheme_id = " + schemeId + " AND pdf_report_fid > 0 AND pdf_signed_report_fid = 0 AND a.application_status = 1  "
//                        + "UNION "
//                        + "SELECT a.application_id, scheme_name, final_beneficiary_count,2 AS type, pdf_signed_report_fid AS fid, txn_timestamp FROM esign.beneficiary_esign_application_details a "
//                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
//                        + "INNER JOIN esign.tran_esign_response c ON a.application_id = c.application_id "
//                        + "WHERE office_id = " + officeId + " AND scheme_id = " + schemeId + " AND pdf_signed_report_fid > 0 AND a.application_status = 2 AND c.status = 'success'";

                String sql = "SELECT a.application_id, scheme_name, final_beneficiary_count,2 AS type, pdf_signed_report_fid AS fid, txn_timestamp FROM esign.beneficiary_esign_application_details a "
                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
                        + "INNER JOIN esign.tran_esign_response c ON a.application_id = c.application_id "
                        + "WHERE office_id = " + officeId + " AND scheme_id = " + schemeId + " AND pdf_signed_report_fid > 0 AND a.application_status = 2 AND c.status = 'success'";

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql, con);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(EsignWorks.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

            if (request.getParameter("paymentFileList") != null) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();

                Connection con = null;
                if (request.getParameter("schemeId") != null) {
                    String scheme = request.getParameter("schemeId");
                    schemeId = Integer.parseInt(scheme.split("~")[0]);
                    schemeCode = scheme.split("~")[1];
                }

                if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                    officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
                }

                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }

                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }

//                String sql = "SELECT application_id, scheme_name, financial_year, final_beneficiary_count,1 AS type, pdf_report_fid AS fid, '' AS txn_timestamp FROM esign.dbt_esign_application_details a "
//                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
//                        + "WHERE office_id = " + officeId + " AND scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND pdf_report_fid > 0 "
//                        + "AND pdf_signed_report_fid = 0 AND application_status = 1 "
//                        + " UNION "
//                        + "SELECT a.application_id, scheme_name, financial_year, final_beneficiary_count,2 AS type, pdf_signed_report_fid AS fid, txn_timestamp FROM esign.dbt_esign_application_details a "
//                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
//                        + "INNER JOIN esign.tran_esign_response c ON a.application_id = c.application_id "
//                        + "INNER JOIN esign.tran_esign_request d ON c.request_id = d.request_id "
//                        + "WHERE office_id = " + officeId + " AND scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " "
//                        + "AND pdf_signed_report_fid > 0 AND application_status = 2 AND c.status = 'success' AND service_id = '3'";

                String sql = "SELECT a.application_id, scheme_name, financial_year, final_beneficiary_count,2 AS type, pdf_signed_report_fid AS fid, txn_timestamp, e.office_name "
                        + "FROM esign.dbt_esign_application_details a "
                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
                        + "INNER JOIN esign.tran_esign_response c ON a.application_id = c.application_id "
                        + "INNER JOIN esign.tran_esign_request d ON c.request_id = d.request_id "
                        + "INNER JOIN bms_user.mas_dept_users_group_based_office e ON a.office_id = e.office_id "
                        + "WHERE entry_office_id = " + officeId + " AND scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' " //office_id = " + checkerOfficeId + " AND 
                        + "AND  installment = " + instl + " AND pdf_signed_report_fid > 0 AND application_status = 2 AND c.status = 'success' AND service_id = '3'";

                if (checkerOfficeId > 0) {
                    sql += "office_id = " + checkerOfficeId;
                }

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql, con);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(EsignWorks.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

            if (request.getParameter("getFileListSigned") != null) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();

                String tabName = "", addlSql = "", userflag = "", service = "";
                Connection con = null;
                if (request.getParameter("schemeId") != null) {
                    String scheme = request.getParameter("schemeId");
                    schemeId = Integer.parseInt(scheme.split("~")[0]);
                    schemeCode = scheme.split("~")[1];
                }

                int workflow = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("purpose")));

                if (workflow == 1) {
                    tabName = "esign.beneficiary_esign_application_details";
                    service = " AND service_id IN ('4','5','6') ";

                } else if (workflow == 2) {
                    tabName = "esign.dbt_esign_application_details";

                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                        instlName = request.getParameter("instlName");
                    }

                    addlSql = " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " ";
                    service = " AND service_id IN ('1','2','3') ";
                }

                if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                    officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
                }

                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("userflag") != null && !request.getParameter("userflag").equals("")) {
                    userflag = Security.SecurityClass.killchar(request.getParameter("userflag"));
                }

                String sql = "SELECT a.application_id, scheme_name, final_beneficiary_count,2 AS type, pdf_signed_report_fid AS fid, txn_timestamp, office_name FROM " + tabName + " a "
                        + "INNER JOIN master_scheme b ON a.scheme_id = b.id "
                        + "INNER JOIN esign.tran_esign_response c ON a.application_id = c.application_id "
                        + "INNER JOIN esign.tran_esign_request e ON c.request_id = e.request_id "
                        + "INNER JOIN bms_user.mas_dept_users_group_based_office d ON a.office_id = d.office_id "
                        + "WHERE scheme_id = " + schemeId + " AND pdf_signed_report_fid > 0 AND a.application_status = 2 AND c.status = 'success'" + service;

                if (userflag.equals("verifier") && officeId > 0) {
                    String childOfficeList = "";
                    childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                    if (childOfficeList.length() > 1) {
                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
                    }
                } else if (userflag.equals("approver")) {
                    String childOfficeList = "";
                    childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                    if (childOfficeList.length() > 1) {
                        sql += "AND a.office_id IN (" + childOfficeList + ") ";
                    }
                }

                if (workflow == 2) {
                    sql += addlSql;
                }

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql, con);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(EsignWorks.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

            if (request.getParameter("showPdf") != null) {
                int fid = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("fid")));
                if (fid > 0) {
                    byte[] gen_pdf = getPDFData(fid);
                    response.setContentType("application/pdf");
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    response.setDateHeader("Expires", 0); // Proxies.
                    ServletOutputStream os = response.getOutputStream();

                    os.write(gen_pdf);
                    os.flush();
                    os.close();
                }
            }

        } catch (Exception ex) {

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

    public byte[] getPDFData(int fid) throws IOException {
        byte[] gen_pdf = null;
        Common.CommonMethod cm = new CommonMethod();
        String pdfB64 = "";
        pdfB64 = cm.getEsignFile(fid);
        BASE64Decoder decoder = new BASE64Decoder();
        gen_pdf = decoder.decodeBuffer(pdfB64);
        return gen_pdf;
    }

}
