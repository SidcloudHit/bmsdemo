/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "ManageEmployeeInfo", urlPatterns = {"/ManageEmployeeInfo"})
public class ManageEmployeeInfo extends HttpServlet {

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
            int deptId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "",message="";
            String account="",ifsc="",bankName="";
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
                if (request.getParameter("txtAccount") != null) {
                    if (request.getParameter("txtAccount").trim().length() > 0) {
                        account = request.getParameter("txtAccount");
                        account = SecurityClass.killchar(account);
                    } else {
                        message = "Account number can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                        return;
                    }
                } else {
                    message = "Account number can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                    return;
                }
                if (request.getParameter("txtIfsc") != null) {
                    if (request.getParameter("txtIfsc").trim().length() > 0) {
                        ifsc = request.getParameter("txtIfsc");
                        ifsc = SecurityClass.killchar(ifsc);
                    } else {
                        message = "IFSC code can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                        return;
                    }
                } else {
                    message = "IFSC code can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                    return;
                }
                
                if (request.getParameter("txtBankName") != null) {
                    if (request.getParameter("txtBankName").trim().length() > 0) {
                        bankName = request.getParameter("txtBankName");
                        bankName = SecurityClass.killchar(bankName);
                    } else {
                        message = "Bank name can not be blank.";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                        return;
                    }
                } else {
                    message = "Bank name can not be blank.";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("Admin/manageEmployeeInfo.jsp");
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
                        query = "INSERT INTO bms_user.dept_user_extra_info	(user_id, account_number, ifsc, bank_name) VALUES (?, ?, ?, ?)";
                        ps = con.prepareStatement(query);
                        int i = 1;
                        ps.setString(i++, userId);
                        ps.setString(i++, account);
                        ps.setString(i++, ifsc);
                        ps.setString(i++, bankName);
                    }else if (edit == 1) {
                        query = "UPDATE bms_user.dept_user_extra_info\n"
                                + "   SET account_number=?, ifsc=?, bank_name=?  WHERE user_id = ?";
                        ps = con.prepareStatement(query);
                        int i = 1;
                       
                        ps.setString(i++, account);
                        ps.setString(i++, ifsc);
                        ps.setString(i++, bankName);
                        ps.setString(i++, userId);
                       
                        
                    }
                    

                
                    if (ps.executeUpdate() > 0) {
                        //con.commit();
                        message = "Data saved Successfully";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "success");
                        response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                    } else {
                        message = "Error in saving";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("Admin/manageEmployeeInfo.jsp");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect("Admin/manageEmployeeInfo.jsp");
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
            
            
            if (request.getParameter("employeeList") != null) {
                //int url_Id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlId")));
                // int group_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));

                String sql = "SELECT user_id, account_number, ifsc, bank_name FROM bms_user.dept_user_extra_info";

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
            
            
            
            if (request.getParameter("userId") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                String user_id = request.getParameter("userId");
                String sql = "SELECT  user_id, account_number, ifsc, bank_name FROM bms_user.dept_user_extra_info  WHERE user_id = '" + user_id + "'";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        
                        json.put("account_number", rset.getString("account_number"));
                        json.put("ifsc", rset.getString("ifsc"));     
                        json.put("bank_name", rset.getString("bank_name"));
                        json.put("user_id", rset.getString("user_id"));
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
            response.sendRedirect("Admin/manageEmployeeInfo.jsp");
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
