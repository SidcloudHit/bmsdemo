/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Nibedita
 */
@WebServlet(name = "ManageApplicationURL", urlPatterns = {"/ManageApplicationURL"})
public class ManageApplicationUrl extends HttpServlet {

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
            int edit = 0, isHeader = 0, isActive = 0, url_id = 0, urlHeader = 0;
            int deptId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";
            String url = "", urlName = "", message = "", urlDesc = "";
            CommonMethod cm = new CommonMethod();

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                //deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());

            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }

            if (request.getParameter("btnSubmit") != null) {

                edit = Integer.parseInt(request.getParameter("hdnEdit"));

                url_id = Integer.parseInt(request.getParameter("hdnUrlId"));
                if (request.getParameter("txtURL") != null) {
                    if (request.getParameter("txtURL").trim().length() > 0) {
                        url = request.getParameter("txtURL");
                        url = SecurityClass.killchar(url);
                    } else {
                        message = "URL can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageApplicationUrl.jsp");
                        return;
                    }
                } else {
                    message = "URL can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageApplicationUrl.jsp");
                    return;
                }
                if (request.getParameter("txtUrlName") != null) {
                    if (request.getParameter("txtUrlName").trim().length() > 0) {
                        urlName = request.getParameter("txtUrlName");
                        urlName = SecurityClass.killchar(urlName);
                    } else {
                        message = "URL Name can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageApplicationUrl.jsp");
                        return;
                    }
                } else {
                    message = "URL Name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageApplicationUrl.jsp");
                    return;
                }
                if (request.getParameter("radHeader") != null) {
                    if (request.getParameter("radHeader").trim().length() > 0) {
                        isHeader = Integer.parseInt(SecurityClass.killchar(request.getParameter("radHeader")));
                    }
                }
                if (request.getParameter("radActive") != null) {
                    if (request.getParameter("radActive").trim().length() > 0) {
                        isActive = Integer.parseInt(SecurityClass.killchar(request.getParameter("radActive")));
                    }
                }

                if (request.getParameter("txtUrlDesc") != null) {
                    if (request.getParameter("txtUrlDesc").trim().length() > 0) {
                        urlDesc = SecurityClass.killchar(request.getParameter("txtUrlDesc"));
                    }
                }

                if (request.getParameter("ddlHeaders") != null) {
                    if (request.getParameter("ddlHeaders").trim().length() > 0) {
                        urlHeader = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlHeaders")));
                    }
                }

                Connection con = null;
                PreparedStatement ps = null;
                PreparedStatement ps1 = null;
                ResultSet rs = null;
                String query = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    //con.setAutoCommit(false);

                    if (edit == 0) {
                        query = "INSERT INTO mas_application_url(url, url_name, is_header, is_active, header_id, url_desc)\n"
                                + "    VALUES (?, ?, ?, ?, ?, ?);";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setString(i++, url);
                        ps.setString(i++, urlName);
                        ps.setInt(i++, isHeader);
                        ps.setInt(i++, isActive);
                        ps.setInt(i++, urlHeader);
                        ps.setString(i++, urlDesc);

                    } else if (edit == 1) {
                        query = "UPDATE mas_application_url\n"
                                + "   SET url=?, url_name=?, is_header=?, is_active=?, header_id=?,  url_desc=?  WHERE url_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setString(i++, url);
                        ps.setString(i++, urlName);
                        ps.setInt(i++, isHeader);
                        ps.setInt(i++, isActive);
                        ps.setInt(i++, urlHeader);
                        ps.setString(i++, urlDesc);
                        ps.setInt(i++, url_id);

                    }
                    if (ps.executeUpdate() > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageApplicationUrl.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageApplicationUrl.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("Admin/manageApplicationUrl.jsp");
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

            if (request.getParameter("urlList") != null) {
                //int url_Id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlId")));
                // int group_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));

                String sql = "SELECT url_id, url, url_name, is_header, is_active, url_desc FROM mas_application_url ORDER BY url_id ASC, is_header DESC, is_active DESC ";

                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(ManageUserRole.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/manageApplicationUrl.jsp");
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
