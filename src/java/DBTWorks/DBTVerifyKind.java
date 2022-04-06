/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
@WebServlet(name = "DBTVerifyKind", urlPatterns = {"/DBTVerifyKind"})
public class DBTVerifyKind extends HttpServlet {

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
            int result = 0;
            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];
                Common.CommonMethod cm = new CommonMethod();
                int i = 0, j = 0, instl = 0;
                String finYr = "", message = "";
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
                ArrayList<String> benIdList = new ArrayList<String>();
                ArrayList<Integer> itemIdList = new ArrayList<Integer>();
                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                String[] benIds = null;
                Integer[] itemIds = null;

                if (request.getParameter("finYr") != "" || request.getParameter("finYr") != null) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != "" || request.getParameter("instl") != null) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }

                String tabName = "transaction.beneficiary_" + schemeCode.toLowerCase() + "_tran_dbt_kind_" + finYr.replace("-", "_");
                String myJsonData = request.getParameter("json");

                try {
                    jsarr1 = new JSONArray(myJsonData);
                    //jsobj1.put("dbtBenList", jsarr1);
                    for (int n = 0; n < jsarr1.length(); n++) {
                        jsobj1 = jsarr1.getJSONObject(n);

                        String items[] = jsobj1.getString("items").split("~");
                        for (int x = 0; x < items.length; x++) {
                            benIdList.add(jsobj1.getString("rc_mem"));
                            itemIdList.add(Integer.parseInt(items[x].split("-")[0]));
                            j++;
                        }
                        i++;
                    }
                } catch (JSONException ex) {
                    ex.getMessage();
                }

                benIds = new String[j];
                benIds = benIdList.toArray(benIds);
                itemIds = new Integer[j];
                itemIds = itemIdList.toArray(itemIds);

                result = DBTStatusUpdate.updateKindDBTVerificationStatus(tabName, schemeId, finYr, instl, itemIds, benIds, userId);

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
