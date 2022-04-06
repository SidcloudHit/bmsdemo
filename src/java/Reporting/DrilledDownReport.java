/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reporting;

import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "DrilledDownReport", urlPatterns = {"/DrilledDownReport"})
public class DrilledDownReport extends HttpServlet {

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
            Common.CommonMethod cm = new CommonMethod();
            HttpSession session = request.getSession();
            if (request.getParameter("ddr") != null) {
                int reqType = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddr")));
                String fYear = "", rcNo = "";

                JSONArray entityList = null;
                try {
                    if (reqType == 1) {
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    } else if (reqType == 2) {
                        fYear = Security.SecurityClass.killchar(request.getParameter("fYear"));
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    } else if (reqType == 3) {
                        rcNo = Security.SecurityClass.killchar(request.getParameter("rcNo"));
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    } else if (reqType == 4) {
                        fYear = Security.SecurityClass.killchar(request.getParameter("fYear"));
                        rcNo = Security.SecurityClass.killchar(request.getParameter("rcNo"));
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    }
                    session.setAttribute("rcNo", rcNo);
                    session.setAttribute("fYear", fYear);

                } catch (Exception ex) {
                    Logger.getLogger(DrilledDownReport.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(entityList);

            }

            if (request.getParameter("ddr1") != null) {
                int reqType = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddr")));
                String fYear = "", rcNo = "";

                JSONArray entityList = null;
                try {
                    if (reqType == 1) {
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    } else if (reqType == 2) {
                        fYear = Security.SecurityClass.killchar(request.getParameter("fYear"));
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    } else if (reqType == 3) {
                        rcNo = Security.SecurityClass.killchar(request.getParameter("rcNo"));
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    } else if (reqType == 4) {
                        fYear = Security.SecurityClass.killchar(request.getParameter("fYear"));
                        rcNo = Security.SecurityClass.killchar(request.getParameter("rcNo"));
                        entityList = cm.getDrilledDownData(fYear, rcNo);
                    }
                    session.setAttribute("rcNo", rcNo);
                    session.setAttribute("fYear", fYear);

                } catch (Exception ex) {
                    Logger.getLogger(DrilledDownReport.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(entityList);

            }

            if (request.getParameter("ddb") != null) {
                String fYear = "", rcNo = "";

                int rType = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ddb")));
                fYear = session.getAttribute("fYear").toString();
                //rcNo = session.getAttribute("rcNo").toString();

                String gpCode = "", rcCode = "";
                if (request.getParameter("gpCode") != "" || request.getParameter("gpCode") != null) {
                    gpCode = Security.SecurityClass.killchar(request.getParameter("gpCode"));
                }

                if (request.getParameter("rcCode") != "" || request.getParameter("rcCode") != null) {
                    rcCode = Security.SecurityClass.killchar(request.getParameter("rcCode"));
                }

                JSONArray entityList = null;

                try {
                    if (rType == 1) {
                        entityList = cm.getDrilledDownBenFamily(gpCode, fYear);
                    } else if (rType == 2) {
                        entityList = cm.getDrilledDownBen(fYear, rcCode);
                    }

                } catch (Exception ex) {
                    Logger.getLogger(DrilledDownReport.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(entityList);
                session.removeAttribute("fYear");
                session.removeAttribute("rcNo");
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

//    public JSONArray getDrilledDownData() throws java.sql.SQLException {
//        String result = "";
//        Connection con = null;
//        Statement stmt = null;
//        ResultSet res = null;
//        JSONArray jsarr = null;
//        String query = "SELECT get_drilled_down_report()";
//        try {
//            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//            stmt = con.createStatement();
//            //con.wait(1800000);
//            res = stmt.executeQuery(query);
//            while (res.next()) {
//                result = res.getString(1);
//            }
//            
//            jsarr = new JSONArray(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (res != null) {
//                res.close();
//            }
//            if (stmt != null) {
//                stmt.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        }
//
//        
//        return jsarr;
//    }
}
