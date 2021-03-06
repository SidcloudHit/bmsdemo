/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import Common.CommonMethod;
import DataTablesWorks.BeneficiaryDataAll;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
public class DBTApproveFileGenaration_FilePush extends HttpServlet {

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
            Common.CommonMethod cm = new CommonMethod();
            HttpSession session = request.getSession();
            Connection con = null;
            int distId = 0, subdivId = 0, officeId = 0, makerOfficeId = 0, checkerOfficeId = 0, blockId = 0, gpId = 0, storeId = 0, schemeId = 0, instl = 0, status = 0;
            String schemeCode = "", finYr = "", gpList = "", batchId = "";
            boolean noLand = false;
            String message = "";

            int deptId = 0, roleId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                //officeId = Integer.parseInt(loginObj.getUserOfficeId());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }
            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                schemeId = Integer.parseInt(scheme.split("~")[0]);
                schemeCode = scheme.split("~")[1];
            }

            if (request.getParameter("distId") != null && !request.getParameter("distId").equals("")) {
                distId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("distId")));
            }
            if (request.getParameter("subdivId") != null && !request.getParameter("subdivId").equals("") && !request.getParameter("subdivId").equals("-1")) {
                subdivId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("subdivId")));
            }
            if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
                officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
            }
            if (request.getParameter("blockId") != null && !request.getParameter("blockId").equals("") && !request.getParameter("blockId").equals("-1")) {
                blockId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("blockId")));
            }
            if (request.getParameter("gpId") != null && !request.getParameter("gpId").equals("") && !request.getParameter("gpId").equals("-1")) {
                gpId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("gpId")));
            }
            if (request.getParameter("storeId") != null && !request.getParameter("storeId").equals("") && !request.getParameter("storeId").equals("-1")) {
                storeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("storeId")));
                gpList = cm.getGpListUnderVLWStore(storeId);
            }

            String sql = "";
            if (request.getParameter("dbtappEsigned") != null) {
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dbtappEsigned")));

                if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("checkerOfficeId") != null && !request.getParameter("checkerOfficeId").equals("") && !request.getParameter("checkerOfficeId").equals("-1")) {
                    checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerOfficeId")));
                }
                if (request.getParameter("makerOfficeId") != null && !request.getParameter("makerOfficeId").equals("") && !request.getParameter("makerOfficeId").equals("-1")) {
                    makerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("makerOfficeId")));
                }

                if (request.getParameter("land") != null && !request.getParameter("land").equals("")) {
                    noLand = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }
                if (type == 1) {
                    sql = "SELECT a.application_id,  financial_year, installment, financial_year,final_beneficiary_count, \n"
                            + "       final_dbt_amount, pdf_report_fid, pdf_signed_report_fid,entry_by, \n"
                            + " case when jsonfilegenerated is false then ' <button onclick=\"jsonfilegenerate('||a.application_id||')\" class=\"btn btn-primary btn-sm\"> <i class=\"fa fa-file-text\">&nbsp;&nbsp;</i>File Generate</button>' "
                            + " else '<i class=\"fa fa-check-circle-o\" style=\"color:green;\" aria-hidden=\"true\"></i> File Generated ' end as action"
                            //+ "' <button onclick=\"jsonfilegenerate('||a.application_id||')\" class=\"btn btn-primary btn-sm\"> <i class=\"fa fa-file-text\">&nbsp;&nbsp;</i>File Generate</button>' as action\n"
                            + "  FROM esign.dbt_esign_application_details a \n"
                            + "inner join public.master_scheme b on a.scheme_id=b.id\n"
                            + "inner join bms_user.mas_dept_users_group_based_office c on a.office_id=c.office_id\n"
                            + "inner join esign.beneficiary_approve_esign_json_file_flag d on d.application_id=a.application_id\n"
                            + "where application_status=2 AND a.entry_office_id = " + officeId + " "
                            + "AND a.office_id=" + checkerOfficeId + " and a.scheme_id=" + schemeId + " and a.installment=" + instl;
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }
            }
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            try {
                jsarr = cm.getResultJSON(sql, con);
                jsobj.put("aaData", jsarr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            out.print(jsobj);
            out.flush();
            out.close();

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
