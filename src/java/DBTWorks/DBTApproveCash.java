/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import Common.CommonMethod;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
public class DBTApproveCash extends HttpServlet {

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
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            String today = formatter.format(currentDate);
            String today1 = formatter1.format(currentDate);
            String message = "";
            int deptId = 0, roleId = 0, officeId = 0;
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

            /* TODO output your page here. You may use following sample code. */
            if (request.getParameter("reqType") != null) {
                int reqType = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("reqType")));
                if (reqType == 1) {
                    if (request.getParameter("schemeId") != null) {
                        String scheme = request.getParameter("schemeId");
                        int schemeId = Integer.parseInt(scheme.split("~")[0]);
                        String schemeCode = scheme.split("~")[1];
                        Common.CommonMethod cm = new CommonMethod();
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        long batchId = timestamp.getTime();

                        int i = 0, instl = 0, result = 0;
                        String finYr = "";

                        JSONObject jsobj1 = new JSONObject();
                        JSONArray jsarr1 = new JSONArray();

                        if (request.getParameter("finYr") != "" || request.getParameter("finYr") != null) {
                            finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                        }
                        if (request.getParameter("instl") != "" || request.getParameter("instl") != null) {
                            instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                        }
                        String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                        String[] benids = null;
                        String myJsonData = request.getParameter("json");

                        try {
                            jsarr1 = new JSONArray(myJsonData);
                            benids = new String[jsarr1.length()];
                            for (int n = 0; n < jsarr1.length(); n++) {
                                jsobj1 = jsarr1.getJSONObject(n);
                                benids[i] = jsobj1.getString("rc_mem");
                                i++;
                            }
                        } catch (JSONException ex) {
                            ex.getMessage();
                        }// batchid="241" + String.valueOf(batchId)
                        result = DBTStatusUpdate.updateCashDBTRequestStatus(tabName, schemeId, finYr, instl, benids, "", userId);
                        out.print(result);
                        out.flush();
                        out.close();
                    }
                } else if (reqType == 2) {
                      if (request.getParameter("schemeId") != null) {
                        String scheme = request.getParameter("schemeId");
                        int schemeId = Integer.parseInt(scheme.split("~")[0]);
                        String schemeCode = scheme.split("~")[1];
                        Common.CommonMethod cm = new CommonMethod();
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        long batchId = timestamp.getTime();

                        int i = 0, instl = 0, result = 0;
                        String finYr = "";

                        JSONObject jsobj1 = new JSONObject();
                        JSONArray jsarr1 = new JSONArray();

                        if (!"".equals(request.getParameter("finYr")) || request.getParameter("finYr") != null) {
                            finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                        }
                        if (!"".equals(request.getParameter("instl")) || request.getParameter("instl") != null) {
                            instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                        }
                        String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                        String db_function_name = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_payment";
                        String[] benids = null;
                        String myJsonData = request.getParameter("json");

                        try {
                            jsarr1 = new JSONArray(myJsonData);
                            benids = new String[jsarr1.length()];
                            for (int n = 0; n < jsarr1.length(); n++) {
                                jsobj1 = jsarr1.getJSONObject(n);
                                benids[i] = jsobj1.getString("rc_mem");
                                i++;
                            }
                        } catch (JSONException ex) {
                            ex.getMessage();
                        }// batchid="241" + String.valueOf(batchId)
                        result = DBTStatusUpdate.CalculateBeneficiaryDBTAmount(tabName,db_function_name, schemeId, finYr, instl, benids, "", userId);
                        out.print(result);
                        out.flush();
                        out.close();
                    }
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
