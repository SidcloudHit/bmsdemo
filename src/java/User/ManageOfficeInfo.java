/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.SecurityClass;
import static com.itextpdf.text.pdf.PdfFileSpecification.url;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "ManageOfficeInfo", urlPatterns = {"/ManageOfficeInfo"})
public class ManageOfficeInfo extends HttpServlet {

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
            int edit = 0;
            int deptId = 0, roleId = 0,officeId = 0;
            String userId = "", userName = "", userDes = "" ;
            String officeType = "",runBy="",instituteName="",email="";
            String  message = "";
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
                
                if (request.getParameter("hiddenOfficeId") != null) {
                        
                    officeId= Integer.parseInt(request.getParameter("hiddenOfficeId"));
                }

                
                if (request.getParameter("ddlOfficeType") != null) {
                    if (request.getParameter("ddlOfficeType").trim().length() > 0) {
                        officeType = SecurityClass.killchar(request.getParameter("ddlOfficeType"));
                    }
                }
                
                
                if (request.getParameter("ddlRunBy") != null) {
                    if (request.getParameter("ddlRunBy").trim().length() > 0) {
                        runBy = SecurityClass.killchar(request.getParameter("ddlRunBy"));
                    }
                }

                if (request.getParameter("txtInstituteName") != null) {
                    if (request.getParameter("txtInstituteName").trim().length() > 0) {
                        instituteName = request.getParameter("txtInstituteName");
                        instituteName = SecurityClass.killchar(instituteName);
                    } else {
                        message = "Institute name can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageOfficeInfo.jsp");
                        return;
                    }
                } else {
                    message = "Institute name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageOfficeInfo.jsp");
                    return;
                }
                if (request.getParameter("txtEmail") != null) {
                    if (request.getParameter("txtEmail").trim().length() > 0) {
                        email = request.getParameter("txtEmail");
                        email = SecurityClass.killchar(email);
                    } else {
                        message = "Email  can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageOfficeInfo.jsp");
                        return;
                    }
                } else {
                    message = "Email can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageOfficeInfo.jsp");
                    return;
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
                        query = "INSERT INTO bms_user.office_extra_info (office_id, office_email, office_type_id, run_by_id, institute_name) VALUES (?, ?, ?, ?, ?)";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setInt(i++, officeId);
                        ps.setString(i++, email);
                        ps.setString(i++, officeType);
                        ps.setString(i++, runBy);
                        ps.setString(i++, instituteName);
                    
                    
                    }else if (edit == 1) {
                        query = "UPDATE bms_user.office_extra_info\n"
                                + "   SET office_email=?, office_type_id=?, run_by_id=?, institute_name=?  WHERE office_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;
                       
                        ps.setString(i++, email);
                        ps.setString(i++, officeType);
                        ps.setString(i++, runBy);
                        ps.setString(i++, instituteName);
                        ps.setInt(i++, officeId);
                        
                    }


                
                    if (ps.executeUpdate() > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageOfficeInfo.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageOfficeInfo.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("Admin/manageOfficeInfo.jsp");
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
            
            
            if (request.getParameter("officeList") != null) {
                //int url_Id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlId")));
                // int group_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));

                String sql = "SELECT office_id, office_email, institute_name, office_type, run_by FROM bms_user.office_extra_info o inner join master_office_type t on o.office_type_id = t.office_type_id inner join master_office_governby g on o.run_by_id = g.run_by_id";

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
            
            
             if (request.getParameter("officeId") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int office_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
                String sql = "SELECT office_id, office_email, office_type_id, run_by_id, institute_name FROM bms_user.office_extra_info  WHERE office_id = " + office_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("office_id", rset.getString("office_id"));
                        json.put("office_email", rset.getString("office_email"));
                        json.put("office_type_id", rset.getString("office_type_id"));
                        
                        json.put("run_by_id", rset.getString("run_by_id"));
                        json.put("institute_name", rset.getString("institute_name"));
                        
                       
                       
                        returnData = json.toString();

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (rset != null) {
                            rset.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    };
                    try {
                        if (smt != null) {
                            smt.close();
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
                out.print(returnData);
            }


        
   

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("Admin/manageOfficeInfo.jsp");
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
