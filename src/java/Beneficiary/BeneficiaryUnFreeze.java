/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beneficiary;

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
 * @author acer
 */
@WebServlet(name = "BeneficiaryUnFreeze", urlPatterns = {"/BeneficiaryUnFreeze"})
public class BeneficiaryUnFreeze extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            int i = 0, result = 0, status = 0;
            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];
                Common.CommonMethod cm = new CommonMethod();
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

                if (request.getParameter("status") != "" || request.getParameter("status") != null) {
                    status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                }

                String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                String tabName1 = "tran_" + schemeId + "_" + schemeCode.toLowerCase() + "_beneficiary_status_mapping";
                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                String[] benids = null;
                String myJsonData = request.getParameter("json");

                try {
                    jsarr1 = new JSONArray(myJsonData);
                    benids = new String[jsarr1.length()];
                    //jsobj1.put("dbtBenList", jsarr1);
                    for (int n = 0; n < jsarr1.length(); n++) {
                        jsobj1 = jsarr1.getJSONObject(n);

                        benids[i] = jsobj1.getString("rc_mem");

                        i++;
                    }

                } catch (JSONException ex) {
                    ex.getMessage();
                }

                result = BeneficiaryStatusUpdate.updateBeneficiaryUnFreezeStatus(tabName, tabName1, schemeId, benids, userId, status, roleId);

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
