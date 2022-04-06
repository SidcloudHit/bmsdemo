/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Arindam Mukherjee
 */
@WebServlet(name = "LRDataCheck", urlPatterns = {"/LRDataCheck"})
public class LRDataCheck extends HttpServlet {

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
            String msg = "";
            HttpSession session = request.getSession();
            if (request.getParameter("searchKhatiyan") != null) {
                String villCode = Security.SecurityClass.killchar(request.getParameter("villCode"));
                String khatiyan = Security.SecurityClass.killchar(request.getParameter("khatiyan"));

                msg = verifyLand(villCode, khatiyan);
                out.print(msg);
            } else if (request.getParameter("searchMapping") != null) {
                int beneficiary_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("beneficiary_id")));

                msg = searchLandMapping(beneficiary_id);
                out.print(msg);
            } else if (request.getParameter("mapPlot") != null) {
                int beneficiary_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("beneficiary_id")));
                int state_code = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("state_code")));
                int dist_code = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("dist_code")));
                int subdiv_code = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("subdiv_code")));
                int rev_circle_code = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("rev_circle_code")));
                int tehsil_code = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("tehsil_code")));
                int rev_vill_code = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("rev_vill_code")));
                String dist_name = Security.SecurityClass.killchar(request.getParameter("dist_name"));
                String subdiv_name = Security.SecurityClass.killchar(request.getParameter("subdiv_name"));
                String rev_circle_name = Security.SecurityClass.killchar(request.getParameter("rev_circle_name"));
                String tehsil_name = Security.SecurityClass.killchar(request.getParameter("tehsil_name"));
                String rev_vill_name = Security.SecurityClass.killchar(request.getParameter("rev_vill_name"));
                int khatian_no = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("khatian_no")));
                int plot_no = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("plot_no")));
                double plot_area = Double.parseDouble(Security.SecurityClass.killchar(request.getParameter("plot_area")));
                String plot_area_unit = Security.SecurityClass.killchar(request.getParameter("plot_area_unit"));
                String plot_land_class = Security.SecurityClass.killchar(request.getParameter("plot_land_class"));
                String plot_land_subclass = Security.SecurityClass.killchar(request.getParameter("plot_land_subclass"));
                int ownership_type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("ownership_type")));
                double ownership_share = Double.parseDouble(Security.SecurityClass.killchar(request.getParameter("ownership_share")));
                String owners_info_from_lr = Security.SecurityClass.killchar(request.getParameter("owners_info_from_lr"));

                Connection con = null;
                PreparedStatement ps = null;
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    int i = 1;

                    String query = "INSERT INTO dbt_beneficiary_land_info(beneficiary_id, state_code, dist_code, subdiv_code, \n"
                            + "            rev_circle_code, tehsil_code, rev_vill_code,dist_name, subdiv_name, \n"
                            + "            rev_circle_name, tehsil_name, rev_vill_name, khatian_no, plot_no, plot_area, \n"
                            + "            plot_area_unit, plot_land_class, plot_land_subclass, \n"
                            + "            ownership_type, ownership_share, owners_info_from_lr,entry_by)\n"
                            + "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    ps = con.prepareStatement(query);
                    ps.setInt(i++, beneficiary_id);
                    ps.setInt(i++, state_code);
                    ps.setInt(i++, dist_code);
                    ps.setInt(i++, subdiv_code);
                    ps.setInt(i++, rev_circle_code);
                    ps.setInt(i++, tehsil_code);
                    ps.setInt(i++, rev_vill_code);
                    ps.setString(i++, dist_name);
                    ps.setString(i++, subdiv_name);
                    ps.setString(i++, rev_circle_name);
                    ps.setString(i++, tehsil_name);
                    ps.setString(i++, rev_vill_name);
                    ps.setInt(i++, khatian_no);
                    ps.setInt(i++, plot_no);
                    ps.setDouble(i++, plot_area);
                    ps.setString(i++, plot_area_unit);
                    ps.setString(i++, plot_land_class);
                    ps.setString(i++, plot_land_subclass);
                    ps.setInt(i++, ownership_type);
                    ps.setDouble(i++, ownership_share);
                    ps.setString(i++, owners_info_from_lr);
                    ps.setString(i++, "1");//session.getAttribute("user_id").toString()

                    int stat = ps.executeUpdate();
                    if (stat > 0) {
                        msg = "Saved Successfully";
                    } else {
                        msg = "Error in Saving";
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(LRDataCheck.class.getName()).log(Level.SEVERE, null, ex);
                    msg = "Error in Saving";
                }
                out.print(msg);
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

    public String verifyLand(String lgdVillCode, String khatianNo) {

        String owner = "";
        //String ownerShare[];
        String plotNo = "";
        String plotArea = "";
        String landClass = "";
        String landSubClass = "";
        //String ownershipType;
        int ownerCount;
        int plotCount;
        int slNo = 1;

        String res = "";
        String url = "http://164.100.127.15/lrservice_tripura/webresources/khatians/khatian?lgd_village_code=" + lgdVillCode + "&khatian_no=" + khatianNo;
        int responseCode = 0;
        CommonMethod cm = new CommonMethod();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Length", Integer.toString(url.getBytes().length));
            con.setRequestProperty("Content-Language", "en-US");
            con.setUseCaches(false);
            con.setDoOutput(true);
            BufferedReader in;
            String responseString = "";
            String outputString = "";
            responseCode = con.getResponseCode();

            if (responseCode == 200) {
                if (con.getInputStream() != null) {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    while ((responseString = in.readLine()) != null) {
                        outputString = outputString + responseString;
                    }
                    Document document = cm.parseXmlFile(outputString);
                    //Font banglaFont = new Font("SutonnyMJ", Font.BOLD, 22);
                    //ownershipType = document.getElementsByTagName("owner_count").item(0).getTextContent();
                    NodeList nodePlot = document.getElementsByTagName("plots").item(0).getChildNodes();
                    plotCount = nodePlot.getLength();
                    //plotNo = new String[plotCount];
                    //plotArea = new String[plotCount];
                    //landClass = new String[plotCount];
                    //landSubClass = new String[plotCount];
                    if (plotCount > 0) {
                        for (int i = 0; i < plotCount; i++) {
                            res += "<tr>";
                            res += "<td>" + slNo + "</td>";
                            plotNo = document.getElementsByTagName("plot_no").item(i).getTextContent(); //nodePlot.item(i).
                            res += "<td>" + plotNo + "</td>";
                            plotArea = document.getElementsByTagName("area").item(i).getTextContent();//nodePlot.item(i).
                            res += "<td>" + plotArea + "</td>";
                            landClass = document.getElementsByTagName("land_class").item(i).getTextContent();
                            landSubClass = "";//document.getElementsByTagName("office_name").item(0).getTextContent();
                            res += "<td>" + landClass + "</td>";
                            res += "<td>"
                                    + "<div class=\"form-group\">"
                                    + "           <div class=\"input-group\"> "
                                    + "                    <div class=\"form-check-inline\">" //radio icheck-material-blue
                                    + "                               <label class=\"form-check-label\" for=\"radOwner_" + slNo + "\">"
                                    + "                               <input type=\"radio\" class=\"form-check-input\"  id=\"radOwner_" + slNo + "\" name=\"radOwnershipType_" + slNo + "\" value=\"1\" onclick=\"return isOwner(" + slNo + ")\">" //class=\"custom-control-input\" 
                                    + "                               Owner </label>" //class=\"custom-control-label\" 
                                    + "                    </div>"
                                    + "                    <div class=\"form-check-inline\">" //radio icheck-material-blue
                                    + "                               <label class=\"form-check-label\" for=\"radShared_" + slNo + "\">"
                                    + "                               <input type=\"radio\" class=\"form-check-input\" id=\"radShared_" + slNo + "\" name=\"radOwnershipType_" + slNo + "\" value=\"2\" onclick=\"return isOwner(" + slNo + ")\" >" //class=\"custom-control-input\" 
                                    + "                               Shared Owner </label>" //class=\"custom-control-label\" 
                                    + "                    </div> "
                                    + "                    <div class=\"form-check-inline\">" //radio icheck-material-blue
                                    + "                               <label class=\"form-check-label\" for=\"radTenant_" + slNo + "\">"
                                    + "                               <input type=\"radio\" class=\"form-check-input\" id=\"radTenant_" + slNo + "\" name=\"radOwnershipType_" + slNo + "\" value=\"3\" onclick=\"return isOwner(" + slNo + ")\" >" //class=\"custom-control-input\" 
                                    + "                               Tenant </label>" //class=\"custom-control-label\" 
                                    + "                    </div> "
                                    + "           </div>"
                                    + "</div>"
                                    + "</td>";

                            res += "<td><div class=\"form-group col-md-3\">"
                                    + "           <div class=\"input-group mb-3\">"
                                    + "                    <input type=\"text\" size=\"80\" id=\"txtOwnersPlotShare_" + slNo + "\" name=\"txtOwnersPlotShare_" + slNo + "\" class=\"form-control form-control-sm\"/>"
                                    + "           </div>"
                                    + "</div></td>";

                            NodeList nodeOwner = document.getElementsByTagName("owners").item(0).getChildNodes();
                            ownerCount = nodeOwner.getLength();
                            //owner = new String[ownerCount];
                            //ownerShare = new String[ownerCount];
                            res += "<td>";
                            for (int j = 0; j < ownerCount; j++) {
                                if (j > 0) {
                                    res += "<br />";
                                }
                                // owner[j] = document.getElementsByTagName("name").item(j).getTextContent();
                                //ownerShare[j] = document.getElementsByTagName("share").item(j).getTextContent();
                                res += document.getElementsByTagName("name").item(j).getTextContent() + "(" + document.getElementsByTagName("share").item(j).getTextContent() + ")";
                                owner += document.getElementsByTagName("name").item(j).getTextContent() + "(" + document.getElementsByTagName("share").item(j).getTextContent() + ")";
                            }
                            res += "</td>";
                            res += "<td>"
                                    + "<div class=\"form-group col-md-1\">"
                                    + "            <div class=\"input-group\">"
                                    + "                      <button type=\"button\" id=\"btnAdd_" + slNo + "\" name=\"btnAdd_" + slNo + "\" class=\"btn btn-primary\" onclick=\"addLandtoBeneficiary('" + slNo + "', '" + plotNo + "', '" + plotArea + "', 'Acre', '" + landClass + "', '" + landSubClass + "', '" + owner + "')\">"
                                    + "                               <i class=\"fa fa-add\">&nbsp;&nbsp;</i>Map with Beneficiary"
                                    + "                      </button>"
                                    + "            </div>"
                                    + " </div>"
                                    + "</td>";
                            res += "</tr>";

                            slNo++;
                        }
                    } else {
                        res += "<tr>";
                        res += "<td colspan=\"7\">No Data Found</td>";
                        res += "</tr>";
                    }
                    in.close();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return res;
    }

    public String searchLandMapping(int beneficiaryId) {
        String result = "";
        int i = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT land_id, state_code, dist_code, subdiv_code,rev_circle_code, tehsil_code, rev_vill_code,"
                + " dist_name, subdiv_name,rev_circle_name, tehsil_name, rev_vill_name, khatian_no, plot_no, plot_area, "
                + "       plot_area_unit, plot_land_class, plot_land_subclass, ownership_type, ownership_share, owners_info_from_lr"
                + "  FROM dbt_beneficiary_land_info WHERE beneficiary_id = " + beneficiaryId;

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                i++;
                result += "<tr>";
                result += "<td>" + i + "<input type=\"hidden\" id=\"hdnLandId_" + i + "\" value=\"" + rs.getString("land_id") + "\"/></td>";
                result += "<td>District: " + rs.getString("dist_name") + "<br />"
                        + "Sub-Division: " + rs.getString("subdiv_name") + "<br />"
                        + "Revenue Circle: " + rs.getString("rev_circle_name") + "<br />"
                        + "Tehsil: " + rs.getString("tehsil_name") + "<br />"
                        + "Revenue Village / Mouza: " + rs.getString("rev_vill_name") + "<br />"
                        + "Khatian No: " + rs.getString("khatian_no") + "<br />"
                        + "</td>";

                result += "<td>" + rs.getString("plot_no") + "</td>";
                result += "<td>" + rs.getDouble("plot_area") + "</td>";
                result += "<td>" + rs.getString("plot_land_class") + "</td>";
                result += "<td>" + rs.getString("ownership_type") + "</td>";
                result += "<td>" + rs.getString("ownership_share") + "</td>";
                result += "<td>" + rs.getString("owners_info_from_lr") + "</td>";
                result += "<td>"
                        + "<div class=\"form-group col-md-1\">"
                        + "            <div class=\"input-group\">"
                        + "                      <button type=\"button\" id=\"btnEdit_" + i + "\" name=\"btnEdit_" + i + "\" class=\"btn btn-primary\" onclick=\"editLandMapping('" + i + "')\">"
                        + "                               <i class=\"fa fa-edit\">&nbsp;&nbsp;</i>Edit Mapping"
                        + "                      </button>"
                        + "            </div>"
                        + " </div>"
                        + "</td>";
                result += "</tr>";

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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
