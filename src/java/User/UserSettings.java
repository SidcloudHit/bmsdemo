/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.AjaxServlet;
import Common.CommonMethod;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "UserSettings", urlPatterns = {"/UserSettings"})
public class UserSettings extends HttpServlet {

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
            String message = "", userId = "";
//            if (session.getAttribute("user_id") != null) {
//                //this user is the logged in user
//                uId = session.getAttribute("user_id").toString();
//            } else {
//                message = "Session time-out, please login again.";
//                request.setAttribute("LogMsg", message);
//                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
//                return;
//            }

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();
            }

            CommonMethod cm = new CommonMethod();
            UserSettingsClass usc = new UserSettingsClass();

            if (request.getParameter("getUserSchemes") != null) {
                int schemeId = 0;
                if (request.getParameter("schemeId") != null && !request.getParameter("schemeId").equals("")) {
                    schemeId = Integer.parseInt(request.getParameter("schemeId"));
                }
                JSONObject jsObjUserSchemes = new JSONObject();
                JSONArray jsArrUserSchemes = null;
                try {
                    if (schemeId > 0) {
                        jsArrUserSchemes = usc.getUserSchemes(userId, schemeId);
                    } else {
                        jsArrUserSchemes = usc.getUserSchemes(userId);
                    }

                    jsObjUserSchemes.put("aaData", jsArrUserSchemes);
                } catch (Exception ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsObjUserSchemes);
                out.flush();
                out.close();
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
