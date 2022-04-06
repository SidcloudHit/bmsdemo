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
@WebServlet(name = "DBTInitiateCash", urlPatterns = {"/DBTInitiateCash"})
public class DBTInitiateCash extends HttpServlet {

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
            int result = 0;
            if (request.getParameter("schemeId") != null) {
                String scheme = request.getParameter("schemeId");
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];
                Common.CommonMethod cm = new CommonMethod();
                int j = 0, instl = 0, amount = 0; //distId = 0, subdivId = 0, 
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

                boolean noLandAllowed = false;

                if (request.getParameter("finYr") != "" || request.getParameter("finYr") != null) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (request.getParameter("instl") != "" || request.getParameter("instl") != null) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                if (request.getParameter("amount") != "") {
                    amount = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("amount")));
                }
                if (request.getParameter("land") != "") {
                    noLandAllowed = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("land")));
                }

//                if (request.getParameter("distId") != "" || request.getParameter("distId") != null) {
//                    distId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("distId")));
//                }
//                if (request.getParameter("subdivId") != "" || request.getParameter("subdivId") != null) {
//                    subdivId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("subdivId")));
//                }
                String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long batchId = 0;//timestamp.getTime();

                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                String[] benids = null;
                Integer[] bentype = null;
                String myJsonData = request.getParameter("json");
//                myJsonData = myJsonData.replace("[", "");
//                myJsonData = myJsonData.replace("]", "");
//                String[] bids = myJsonData.split(",");
//
//                Integer[] benids = new Integer[bids.length];
//
//                for (int i = 0; i < bids.length; i++) {
//                    benids[i] = Integer.parseInt(bids[i]);
//                }

                try {
                    jsarr1 = new JSONArray(myJsonData);
                    benids = new String[jsarr1.length()];
                    bentype = new Integer[jsarr1.length()];
                    //jsobj1.put("dbtBenList", jsarr1);
                    for (int n = 0; n < jsarr1.length(); n++) {
                        jsobj1 = jsarr1.getJSONObject(n);

                        benids[j] = jsobj1.getString("rc_mem");
                        bentype[j] = jsobj1.getInt("ben_type");
                        j++;
                    }

                } catch (JSONException ex) {
                    ex.getMessage();
                }


                /*JSONObject json_obj = new JSONObject();
                JSONArray json_array = new JSONArray();
                try {
                    json_array = new JSONArray(myJsonData);
                    json_obj.put("ben_id_list", json_array);
                } catch (JSONException ex) {
                    ex.getMessage();
                }*/
                Connection con = null;
                CallableStatement ps = null;
                ResultSet rs = null;
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    Array dArray1 = con.createArrayOf("integer", bentype);
                    Array dArray = con.createArrayOf("varchar", benids);
                    String query = "";
                    int i = 1;

                    ps = con.prepareCall("{call dbt_cash_initiate(?,?,?,?,?,?,?,?,?,?)}");
                    ps.setString(i++, tabName);
                    ps.setString(i++, String.valueOf(batchId));
                    ps.setInt(i++, schemeId);
                    ps.setString(i++, finYr);
                    ps.setInt(i++, instl);
                    ps.setInt(i++, amount);
                    ps.setBoolean(i++, noLandAllowed);
                    ps.setString(i++, userId);
                    ps.setArray(i++, dArray);//json_obj.toString());
                    ps.setArray(i++, dArray1);//json_obj.toString());
                    //rs = ps.executeQuery();
                    if (ps.execute()) {
                        rs = ps.getResultSet();
                        while (rs.next()) {
                            result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
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
