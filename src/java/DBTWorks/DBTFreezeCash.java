/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "DBTFreezeCash", urlPatterns = {"/DBTFreezeCash"})
public class DBTFreezeCash extends HttpServlet {

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
            HttpSession session = request.getSession();
            int i = 0, result = 0, status = 0;
            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];
                Common.CommonMethod cm = new CommonMethod();
                int instl = 0;
                String finYr = "", message = "", userflag = "";
                int deptId = 0, roleId = 0, officeId = 0, direct = 0, amount = 0;
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

                if (request.getParameter("finYr") != "" && request.getParameter("finYr") != null) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != "" && request.getParameter("instl") != null) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("status") != "" && request.getParameter("status") != null) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }
                if (request.getParameter("amount") != "") {
                    amount = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("amount")));
                }

                if (request.getParameter("userflag") != "" && request.getParameter("userflag") != null) {
                    userflag = Security.SecurityClass.killchar(request.getParameter("userflag"));
                }

                String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                String[] benids = null;
                Integer[] amounts = null;
                String myJsonData = request.getParameter("json");

                try {
                    jsarr1 = new JSONArray(myJsonData);
                    benids = new String[jsarr1.length()];
                    amounts = new Integer[jsarr1.length()];
                    //jsobj1.put("dbtBenList", jsarr1);
                    for (int n = 0; n < jsarr1.length(); n++) {
                        jsobj1 = jsarr1.getJSONObject(n);

                        benids[i] = jsobj1.getString("rc_mem");
                        //String eligible_dbt_amount=jsobj1.getString("eligible_dbt_amount");
                        double  elgAmountDouble = jsobj1.getDouble("eligible_dbt_amount");//Double.parseDouble(eligible_dbt_amount);
                        int elgAmount=(int)elgAmountDouble;
                        if (elgAmount == 0) {
                            elgAmount = amount;
                        }
                        amounts[i] = elgAmount;
                        i++;
                    }

                } catch (JSONException ex) {
                    ex.getMessage();
                }

                if (userflag.equals("approver")) {
                    if (request.getParameter("direct") != "" && request.getParameter("direct") != null) {
                        direct = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("direct")));
                    }

                    if (direct == 0) {
                        DBTStatusUpdate.updateCashDBTRequestStatus(tabName, schemeId, finYr, instl, benids, "", userId);
                    } else if (direct == 1) {
//                        if (request.getParameter("amount") != "") {
//                            amount = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("amount")));
//                        }
//                        for (int m = 0; m < jsarr1.length(); m++) {
//                            amounts[m] = amount;
//                        }

                        DBTStatusUpdate.InitiateCashDBTRequestDirect(tabName, schemeId, finYr, instl, amounts, benids, "", userId);
                    }

                }

                result = DBTStatusUpdate.updateCashDBTFreezeStatus(tabName, schemeId, finYr, instl, benids, userId, status);

            }
            out.print(result);
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
