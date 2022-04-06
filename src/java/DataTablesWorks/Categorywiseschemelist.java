/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTablesWorks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Categorywiseschemelist", urlPatterns = {"/Categorywiseschemelist"})
public class Categorywiseschemelist extends HttpServlet {

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
            CallableStatement cs = null;
            Connection con = null;
            String scheme_str_list = "", cashscheme = "", kindscheme = "", cashkindscheme = "";
            int deptid = 0;
            JSONArray jsarr = null;
            JSONObject jsobj = new JSONObject();
            con = new DBCon.DBSource().connectToBMSDB().getConnection();

            if (request.getParameter("dept_id") != null && !"".equals(request.getParameter("dept_id"))) {
                deptid = Integer.parseInt(request.getParameter("dept_id"));
            }
            if (request.getParameter("cashscheme") != null && !"".equals(request.getParameter("cashscheme"))) {
                cashscheme = "1";
            } else if (request.getParameter("kindscheme") != null && !"".equals(request.getParameter("kindscheme"))) {
                kindscheme = "1";
            } else {
                cashkindscheme = "1";
            }
            if ("1".equals(cashscheme)) {

                try {
                    cs = con.prepareCall("{call scheme_onboarding.getCashSchemeList(?,?)}");
                    cs.setInt(1, deptid);
                    cs.registerOutParameter(2, Types.VARCHAR);

                    cs.execute();
                    scheme_str_list = cs.getString(2);
                    if(scheme_str_list==null){
                       scheme_str_list= "[{\"scheme_codification\":\"\",\"scheme_name\":\"No Data Found\",\"launch_date\":\"\",\"scheme_available\":\"\"}]";
                    }
                    jsarr = new JSONArray(scheme_str_list);
                    jsobj.put("aaData", jsarr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
            else if ("1".equals(kindscheme)) {

                try {
                    cs = con.prepareCall("{call scheme_onboarding.getKindSchemeList(?,?)}");
                    cs.setInt(1, deptid);
                    cs.registerOutParameter(2, Types.VARCHAR);

                    cs.execute();
                    scheme_str_list = cs.getString(2);
                    if(scheme_str_list==null){
                       scheme_str_list= "[{\"scheme_codification\":\"\",\"scheme_name\":\"No Data Found\",\"launch_date\":\"\",\"scheme_available\":\"\"}]";
                    }
                    jsarr = new JSONArray(scheme_str_list);
                    jsobj.put("aaData", jsarr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
            else {
                try {
                    cs = con.prepareCall("{call scheme_onboarding.getCashKindSchemeList(?,?)}");
                    cs.setInt(1, deptid);
                    cs.registerOutParameter(2, Types.VARCHAR);

                    cs.execute();
                    scheme_str_list = cs.getString(2);
                    if(scheme_str_list==null){
                       scheme_str_list= "[{\"scheme_codification\":\"\",\"scheme_name\":\"No Data Found\",\"launch_date\":\"\",\"scheme_available\":\"\"}]";
                    }
                    jsarr = new JSONArray(scheme_str_list);
                    jsobj.put("aaData", jsarr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //  JSONArray memId_json = new JSONArray(mem_id_str_list);
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
