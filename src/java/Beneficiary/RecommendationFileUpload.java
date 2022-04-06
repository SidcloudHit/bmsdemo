/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beneficiary;

import Common.CommonMethod;
import Master.Data.AdminUser;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "RecommendationFileUpload", urlPatterns = {"/RecommendationFileUpload"})
@MultipartConfig(maxFileSize = 4194304)	// upload file's size up to 4MB
public class RecommendationFileUpload extends HttpServlet {

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
            CommonMethod cm = new CommonMethod();
            String message = "", user_id = "";
            String scheme = Security.SecurityClass.killchar(request.getParameter("schemeId"));
            int schemeId = Integer.parseInt(scheme.split("~")[0]);
            String schemeCode = scheme.split("~")[1];
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

            if (request.getParameter("recomFileUpload") != null) {

                String recomFrom = Security.SecurityClass.killchar(request.getParameter("recomFrom"));
                int benCount = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("benCount")));

                String ctypeRfile = "";
                String fileRfileName = "";
                InputStream isRfile = null;
                long fileRfileSize = 0;
                int fileRfileFid = 0, listId = 0;
                Part filePart = null;

                try {
                    filePart = null;
                    filePart = request.getPart("file");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message = "File upload error! Please try again";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DBTInitiator/uploadRecommendations.jsp");
                    return;
                }

                if (filePart != null) {
                    ctypeRfile = filePart.getContentType();
                    fileRfileSize = filePart.getSize();
                    fileRfileName = filePart.getSubmittedFileName();
                    if (fileRfileSize > 0) {
                        if (!(ctypeRfile.equalsIgnoreCase("image/jpeg") || ctypeRfile.equalsIgnoreCase("image/png") || ctypeRfile.equalsIgnoreCase("application/pdf"))) {
                            message = "Please Select a JPEG,PNG or PDF file.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("DBTInitiator/uploadRecommendations.jsp");
                            return;
                        }
                        if ((fileRfileSize / 1024) > 2048) { // filesize in kb
                            message = "Size of Photo file is more than 2 MB, Please use smaller size.";
                            session.setAttribute("RegMsg", message);
                            session.setAttribute("type", "danger");
                            response.sendRedirect("DBTInitiator/uploadRecommendations.jsp");
                            return;
                        }
                        // obtains input stream of the upload file
                        isRfile = filePart.getInputStream();
                    }
                }

                if (isRfile != null && fileRfileSize > 0) {
                    fileRfileFid = cm.uploadFile(isRfile, user_id, fileRfileName, fileRfileSize, ctypeRfile);
                }

                if (fileRfileFid > 0) {
                    listId = saveSchemeRecommendationList(schemeId, schemeCode, fileRfileFid, recomFrom, benCount, user_id);
                }

                out.print(listId);
                out.flush();
                out.close();
            }

            if (request.getParameter("recomFileList") != null) {
                Connection con = null;
                String sql = "SELECT list_id, scheme_name, recomendation_file_id, recommendation_from, beneficiary_count, DATE(entry_time) AS upload_date,"
                        + " (SELECT COUNT(*) FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data m WHERE m.recomendation_list_id = a.recomendation_file_id) AS recomended_beneficiary \n"
                        + " FROM tbl_recommendation_file_list a"
                        + " INNER JOIN master_scheme b ON a.scheme_id = b.id AND a.scheme_code = b.scheme_codification"
                        + " WHERE entry_by = '" + user_id + "' AND scheme_id = " + schemeId + " AND scheme_code = '" + schemeCode + "'"
                        + " ORDER BY entry_time DESC";

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql, con);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(RecommendationFileUpload.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

            if (request.getParameter("mappedBenList") != null) {
                int listId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("listId")));
                Connection con = null;
                String sql = "SELECT a.rc_mem,a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name \n"
                        + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   \n"
                        //+ "INNER JOIN tran_scheme_beneficiary_status_mapping b ON a.rc_mem = b.beneficiary_id\n"
                        + "WHERE recomendation_list_id = " + listId; //b.scheme_code = '" + schemeCode + "' AND 

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                JSONArray jsarr = null;
                JSONObject jsobj = new JSONObject();
                try {
                    jsarr = cm.getResultJSON(sql, con);
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(RecommendationFileUpload.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
                out.flush();
                out.close();
            }

            if (request.getParameter("recomFileBenMapping") != null) {
                int listId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("listId")));
                int i = 0;
                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                String[] benids = null;
                String myJsonData = request.getParameter("json");

                try {
                    jsarr1 = new JSONArray(myJsonData);
                    benids = new String[jsarr1.length()];
                    //jsobj1.put("dbtBenList", jsarr1);
                    for (int n = 0; n < jsarr1.length(); n++) {
                        jsobj1 = jsarr1.getJSONObject(n);
                        benids[i] = jsobj1.getString("rc_mem");
                        i++;
                    }

                } catch (JSONException ex) {
                    ex.getMessage();
                }
                String tabName = "beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data";
                int result = updateBeneficiaryRecomList(tabName, listId, schemeId, benids, user_id);

                out.print(result);
                out.flush();
                out.close();

            }

            if (request.getParameter("recomFileESign") != null) {

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

    public int saveSchemeRecommendationList(int scheme_id, String scheme_code, int recomendation_file_id, String recommendation_from, int beneficiary_count, String uId) {
        int listId = 0, i = 1;
        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        query = "INSERT INTO tbl_recommendation_file_list(scheme_id, scheme_code, recomendation_file_id, recommendation_from, beneficiary_count, entry_by)  VALUES (?, ?, ?, ?, ?, ?)";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); //

            ps.setInt(i++, scheme_id);
            ps.setString(i++, scheme_code);
            ps.setInt(i++, recomendation_file_id);
            ps.setString(i++, recommendation_from);
            ps.setInt(i++, beneficiary_count);
            ps.setString(i++, uId);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                listId = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listId;
    }

    public int updateBeneficiaryRecomList(String tabName, int listId, int schemeId, String[] benids, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call beneficiary.beneficiary_recommendation_update_new(?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, listId);
            ps.setInt(i++, schemeId);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);//json_obj.toString());

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
        return result;
    }

}
