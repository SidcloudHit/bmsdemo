/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCon;

import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author acer
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */

 /* all password update with hashing code
            //Connection con = (Connection) getServletContext().getAttribute("DBConnection");
            SecurityClass sc = new SecurityClass();
            //String hex_password = sc.gethexString(user_password);
            
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query1 = "", query2 = "";
            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                //con.setAutoCommit(false);

                query1 = "SELECT user_id,user_password FROM mas_admin_user_account";
                ps = con.prepareStatement(query1);
                rs = ps.executeQuery();
                while (rs.next()) {
                    query2 = "UPDATE mas_admin_user_account SET  user_password = ? WHERE user_id = ?";

                    ps1 = con.prepareStatement(query2);
                    int i = 1;
                    ps1.setString(i++, sc.gethexString(rs.getString(2)));
                    ps1.setString(i++, rs.getString(1));

//                    if (ps1.executeUpdate() > 0) {
//                        System.out.println("Success");
//                    } else {
//                        System.out.println("Failure");
//                    }
                }

            } catch (Exception ex) {
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
            }*/
            try {
                //URL url = new URL(request.getRequestURL().toString());
                URL url = new URL("https://bmsuat.trsc.nic.in/abcd.jsp");
                

                String host = url.getHost();
                String userInfo = url.getUserInfo();
                String scheme = url.getProtocol();
                int port = url.getPort();
                String _Path = url.getPath();
                String path = "";//request.getAttribute("javax.servlet.forward.request_uri").toString();
                String query = "";//request.getAttribute("javax.servlet.forward.query_string").toString();
                out.println("scheme-"+scheme+", userInfo-"+userInfo+", host-"+host+", port-"+port+", path-"+_Path+", query-"+query );
                
                out.print(scheme+"://"+host+"/SaveRCResponse");
                
                
                URI uri = new URI(scheme, userInfo, host, port, path, query, null);
            } catch (Exception ex) {
                Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            //return uri.toString();

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
