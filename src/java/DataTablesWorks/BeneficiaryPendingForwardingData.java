/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTablesWorks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Tapash Majumder
 */
@WebServlet(name = "BeneficiaryPendingForwardingData", urlPatterns = {"/BeneficiaryPendingForwardingData"})
public class BeneficiaryPendingForwardingData extends HttpServlet {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BeneficiaryPendingForwardingData</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BeneficiaryPendingForwardingData at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        response.setContentType("text/html;charset=UTF-8");
        String financial_year=request.getParameter("finYr");
        String split_scheme_Id=request.getParameter("schemeId");
        String[] a=split_scheme_Id.split("~");
        String scheme_Id=a[0].toString();
        String installment=request.getParameter("instl");
        String office_Id=request.getParameter("officeId");
        System.out.println(financial_year);
        System.out.println(scheme_Id);
        System.out.println(installment);
        System.out.println(office_Id);
        PrintWriter out = response.getWriter();

        JSONArray arr = new JSONArray();
        JSONObject appObj = new JSONObject();
        JSONObject appDetails = new JSONObject();
        String query = "select x.lgd_block_name as block_name,x.lgd_gp_name as gp_name,count(x.member_id) as total,count(x.beneficiary_id) as forwarded,\n"
                + "(count(x.member_id)-count(x.beneficiary_id)) as pending from\n"
                + "(select a.lgd_block_name,a.lgd_gp_name,a.member_id,b.beneficiary_id from (\n"
                + "select map_maker_to_lgd_gp.lgd_block_name,map_maker_to_lgd_gp.lgd_gp_name,beneficiary_cigt8_data.rc_mem as member_id\n"
                + "from beneficiary_cigt8_data, map_maker_to_lgd_gp\n"
                + "where beneficiary_cigt8_data.lgd_block_code=map_maker_to_lgd_gp.lgd_block_code and\n"
                + "map_maker_to_lgd_gp.lgd_gp_code=beneficiary_cigt8_data.lgd_gp_code and\n"
                + "map_maker_to_lgd_gp.office_id='"+office_Id+"' group by map_maker_to_lgd_gp.lgd_block_name,\n"
                + "map_maker_to_lgd_gp.lgd_gp_name,beneficiary_cigt8_data.rc_mem\n"
                + "union all\n"
                + "select map_maker_to_lgd_gp.lgd_block_name,map_maker_to_lgd_gp.lgd_gp_name,beneficiary_cigt8_data_temp.tempid  as member_id\n"
                + "from beneficiary_cigt8_data_temp,map_maker_to_lgd_gp\n"
                + "where map_maker_to_lgd_gp.lgd_block_code=beneficiary_cigt8_data_temp.lgd_block_code\n"
                + "and map_maker_to_lgd_gp.lgd_gp_code=beneficiary_cigt8_data_temp.lgd_gp_code\n"
                + "and map_maker_to_lgd_gp.office_id='"+office_Id+"' group by\n"
                + "map_maker_to_lgd_gp.lgd_block_name,map_maker_to_lgd_gp.lgd_gp_name,\n"
                + "beneficiary_cigt8_data_temp.tempid) a\n"
                + "left join\n"
                + "(select beneficiary_id from beneficiary_cigt8_tran_dbt_cash_2020_2021 where installment="+installment+" and financial_year='"+financial_year+"' and scheme_id="+Integer.parseInt(scheme_Id)+") b on\n"
                + "a.member_id=b.beneficiary_id\n"
                + ") x group by x.lgd_block_name,x.lgd_gp_name";
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                appDetails.put("block_name", rs.getString("block_name"));
                appDetails.put("gp_name", rs.getString("gp_name"));
                appDetails.put("total_beneficiary", rs.getString("total"));
                appDetails.put("forwarded_beneficiary", rs.getString("forwarded"));
                appDetails.put("pending_beneficiary", rs.getString("pending"));
                arr.add(appDetails);

            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        appObj.put("data", arr);
        response.getWriter().print(appObj.toString());

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
