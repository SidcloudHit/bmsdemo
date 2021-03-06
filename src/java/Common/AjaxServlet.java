/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import DBTWorks.DBTFileGenerationCash;
import SessionPackage.LoginClass;
import User.mathCaptchaGen;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import ob_Department.ob_dept_dal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "AjaxServlet", urlPatterns = {"/AjaxServlet"})
public class AjaxServlet extends HttpServlet {

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
            String ilistArray = "";
            String ilistIDArray = "";
            HttpSession session = request.getSession();
            CommonMethod cm = new CommonMethod();
            JSONArray jsarr = null;
            JSONObject jsobj = null;
            JSONArray jsarr2 = null;
            JSONObject jsobj2 = null;
            JSONArray jsarr3 = null;
            JSONObject jsobj3 = null;

            if (request.getParameter("email") != null) {
                String email = Security.SecurityClass.killchar(request.getParameter("email"));
                if (cm.checkEmailExists(email)) {
                    msg += "var msg = \'no\';";
                } else {
                    msg += "var msg = \'yes\';";
                }
                out.print(msg);
            }
            if (request.getParameter("adminEmail") != null) {
                String email = Security.SecurityClass.killchar(request.getParameter("adminEmail"));
                if (cm.adminCheckEmailExists(email)) {
                    msg += "var msg = \'no\';";
                } else {
                    msg += "var msg = \'yes\';";
                }
                out.print(msg);
            }

            if (request.getParameter("prevEmail") != null) {
                String email = Security.SecurityClass.killchar(request.getParameter("prevEmail"));
                String sql = "SELECT * FROM mas_dept_user  WHERE user_email = '" + email + "'";
                Connection con = null;
                PreparedStatement ps = null;
                Statement smt = null;
                ResultSet rs = null;
                String user_id = "", user_name = "", user_mobile = "", user_email = "", user_dob = "", rc_mem = "";
                String managedBy = "";

                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rs = smt.executeQuery(sql);

                    if (rs.next()) {
//                        json = new JSONObject();
//                        json.put("url_id", rset.getString("url_id"));
//                        json.put("url", rset.getString("url"));
//                        json.put("url_name", rset.getString("url_name"));
//                        json.put("is_header", rset.getString("is_header"));
//                        json.put("is_active", rset.getString("is_active"));
//                        json.put("url_desc", rset.getString("url_desc"));
//                        returnData = json.toString();
                        user_id = rs.getString("user_id");
                        user_name = rs.getString("user_name");
                        user_mobile = rs.getString("user_mobile");
                        user_email = rs.getString("user_email");
                        user_dob = rs.getString("user_dob");
                        rc_mem = rs.getString("rc_mem");
                        managedBy = rs.getString("user_created_by");
                        msg += "var user_id = \'" + user_id + "\';";
                        msg += "var user_name = \'" + user_name + "\';";
                        msg += "var user_mobile = \'" + user_mobile + "\';";
                        msg += "var user_email = \'" + user_email + "\';";
                        msg += "var user_dob = \'" + user_dob + "\';";
                        msg += "var rc_mem = \'" + rc_mem + "\';";
                        //msg += "var user_name = \'" + user_name + "\' user_mobile = \'" + user_mobile + "\' user_email = \'" + user_email + "\' user_dob = \'" + user_dob +"\' rc_mem = \'" + rc_mem +  "\';";

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

                out.print(msg);
            }

            if (request.getParameter("mobile") != null) {
                String mobile = Security.SecurityClass.killchar(request.getParameter("mobile"));
                if (cm.checkMobileExists(mobile)) {
                    msg += "var msg = \'no\';";
                } else {
                    msg += "var msg = \'yes\';";
                }
                out.print(msg);
            }
            if (request.getParameter("adminMobile") != null) {
                String mobile = Security.SecurityClass.killchar(request.getParameter("adminMobile"));
                if (cm.adminCheckMobileExists(mobile)) {
                    msg += "var msg = \'no\';";
                } else {
                    msg += "var msg = \'yes\';";
                }
                out.print(msg);
            }
             if (request.getParameter("captcha") != null) {
                Map<String, String> gencaptchamap = new HashMap<String, String>();
                mathCaptchaGen generate = new mathCaptchaGen();
                gencaptchamap = generate.generateCaptcha();

                String opt = "", fnum = "", snum = "", missing = "", calc = "", solution = "";
                missing = gencaptchamap.get("missing");

                fnum = gencaptchamap.get("first");
                snum = gencaptchamap.get("second");
                solution = gencaptchamap.get("solution");

                opt = gencaptchamap.get("operation");
                if (Integer.parseInt(fnum) > Integer.parseInt(snum)) {
                    calc = fnum + opt + snum + " = ";
                } else {
                    calc = snum + opt + fnum + " = ";
                }
                session.setAttribute("captcha_solution", "");
                session.setAttribute("captcha_solution", solution);

                graphicsutility gutil = new graphicsutility();

                out.print(gutil.getimagefromstring(calc));
//                BufferedImage bImage = (BufferedImage) gutil.getimagefromstring(calc);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(bImage, "jpg", baos);
//                baos.flush();
//                byte[] imageInByteArray = baos.toByteArray();
//                baos.close();
//
//                String b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(imageInByteArray);
//                out.print("<img src='data:image/jpg;base64," + b64 + " alt='Image not found' />");
                out.flush();
                out.close();

                //out.print(prd);
            }
            /*Banti Edit Start*/
            if (request.getParameter("approve_dept") != null) {
                String status = "";
                int deptId = Integer.parseInt(request.getParameter("dept_id"));
                ob_dept_dal dept_dal = new ob_dept_dal();
                try {
                    status = dept_dal.approveDept(deptId);
                } catch (Exception ex) {
                    ex.getMessage();
                }
                out.print(status);
                out.flush();
                out.close();
            }
            /* Banti Edit End*/
            if (request.getParameter("prevMobile") != null) {
                String mobile = Security.SecurityClass.killchar(request.getParameter("prevMobile"));
                String sql = "SELECT * FROM mas_dept_user  WHERE user_mobile = '" + mobile + "'";
                Connection con = null;
                PreparedStatement ps = null;
                Statement smt = null;
                ResultSet rs = null;
                String user_id = "", user_name = "", user_mobile = "", user_email = "", user_dob = "", rc_mem = "";
                String managedBy = "";

                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rs = smt.executeQuery(sql);

                    if (rs.next()) {
//                        json = new JSONObject();
//                        json.put("url_id", rset.getString("url_id"));
//                        json.put("url", rset.getString("url"));
//                        json.put("url_name", rset.getString("url_name"));
//                        json.put("is_header", rset.getString("is_header"));
//                        json.put("is_active", rset.getString("is_active"));
//                        json.put("url_desc", rset.getString("url_desc"));
//                        returnData = json.toString();
                        user_id = rs.getString("user_id");
                        user_name = rs.getString("user_name");
                        user_mobile = rs.getString("user_mobile");
                        user_email = rs.getString("user_email");
                        user_dob = rs.getString("user_dob");
                        rc_mem = rs.getString("rc_mem");
                        managedBy = rs.getString("user_created_by");
                        msg += "var user_id = \'" + user_id + "\';";
                        msg += "var user_name = \'" + user_name + "\';";
                        msg += "var user_mobile = \'" + user_mobile + "\';";
                        msg += "var user_email = \'" + user_email + "\';";
                        msg += "var user_dob = \'" + user_dob + "\';";
                        msg += "var rc_mem = \'" + rc_mem + "\';";
                        //msg += "var user_name = \'" + user_name + "\' user_mobile = \'" + user_mobile + "\' user_email = \'" + user_email + "\' user_dob = \'" + user_dob +"\' rc_mem = \'" + rc_mem +  "\';";

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

                out.print(msg);
            }
            if (!"".equals(request.getParameter("scheme_data")) && request.getParameter("scheme_data") != null) {
                String scheme_id = request.getParameter("scheme_data").split("~")[0];
                String scheme_name = request.getParameter("scheme_data").split("~")[2];
                String dept_name = cm.getDepartmentName(Integer.parseInt(request.getParameter("dept_id")));
                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");
                if (scheme_id != null && scheme_id != "") {
                    loginObj.setUserScheme(scheme_id);
                    loginObj.setUserSchemeName(scheme_name);
                    loginObj.setUserDeptName(dept_name);
                }
                session.removeAttribute("loginInfo");
                session = request.getSession(true);
                session.setAttribute("loginInfo", loginObj);
                out.print("ok");

            }
            if (request.getParameter("MapUserGroup") != null) {
                int i = 0;
                int deptId = Integer.parseInt(request.getParameter("deptId"));
                int groupId = Integer.parseInt(request.getParameter("groupId"));
                JSONObject jsobj1 = new JSONObject();
                JSONArray jsarr1 = new JSONArray();
                String[] userids = null;
                int result = 0;
                String myJsonData = request.getParameter("json");

                try {
                    jsarr1 = new JSONArray(myJsonData);
                    userids = new String[jsarr1.length()];
                    //jsobj1.put("dbtBenList", jsarr1);
                    for (int n = 0; n < jsarr1.length(); n++) {
                        jsobj1 = jsarr1.getJSONObject(n);

                        userids[i] = jsobj1.getString("user_id");
                        result = cm.userGroupMapping(deptId, groupId, userids[i]);
                        i++;

                    }
                    //msg += "var result = \'" + result + "\';";

                } catch (JSONException ex) {
                    ex.getMessage();
                }

                out.print(result);
                out.flush();
                out.close();

            }

            if (request.getParameter("MapScheme") != null) {
                int i = 0;
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                int deptId = Integer.parseInt(request.getParameter("deptId"));
                int workflowId = Integer.parseInt(request.getParameter("workflowType"));
                int groupId = Integer.parseInt(request.getParameter("groupId"));
                int schemeId = Integer.parseInt(request.getParameter("schemeId"));
                //String[] alltype = request.getParameterValues("type");
                //String[] type = alltype[0].split(",");
//                JSONObject jsobj1 = new JSONObject();
//                JSONArray jsarr1 = new JSONArray();
//                int[] schemeids = null;
                int result = 0;
//                String myJsonData = request.getParameter("json");

                try {
                    int check_prev_entered_data = cm.check_prev_entered_data(deptId, groupId, roleId, schemeId, workflowId);
                    if (check_prev_entered_data == 0) {
                        result = cm.roleSchemeMapping(deptId, groupId, roleId, schemeId, workflowId);
                    } else {
                        result = 3;
                    }
//                    jsarr1 = new JSONArray(myJsonData);
//                    schemeids = new int[jsarr1.length()];
//                    //jsobj1.put("dbtBenList", jsarr1);
//                    for (int n = 0; n < jsarr1.length(); n++) {
//                        jsobj1 = jsarr1.getJSONObject(n);
//                        
//                        schemeids[i] = jsobj1.getInt("id");
//                        //type[i] = jsobj1.getInt("type1");
//                        //result = cm.roleSchemeMapping(deptId, roleId, schemeids[i], Integer.parseInt(type[i]));
//                        result = cm.roleSchemeMapping(deptId, groupId, roleId, schemeId, workflowId);
//                        i++;
//                        
//                    }
                    //msg += "var result = \'" + result + "\';";

                } catch (Exception ex) {
                    ex.getMessage();
                }

                out.print(result);
                out.flush();
                out.close();

            }

            if (request.getParameter("loginId") != null) {
                String loginId = Security.SecurityClass.killchar(request.getParameter("loginId"));
                if (cm.checkLoginIdExists(loginId)) {
                    msg += "var msg = \'no\';";
                } else {
                    msg += "var msg = \'yes\';";
                }
                out.print(msg);
            }

            if (request.getParameter("schmp") != null) {
                String scheme = Security.SecurityClass.killchar(request.getParameter("schemeId"));
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];

                int prd = cm.getSchemePeriodicity(schemeId);
                int amount = cm.getSchemeAmount(schemeId);

                JSONObject jsobjret1 = new JSONObject();
                try {
                    jsobjret1.put("prd", Integer.toString(prd));
                    jsobjret1.put("amount", Integer.toString(amount));
                } catch (JSONException ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                out.print(jsobjret1.toString());
                out.flush();
                out.close();

                //out.print(prd);
            }

            if (request.getParameter("isDeptAvl") != null && "1".equals(request.getParameter("isDeptAvl"))) {
                int dept_id = Integer.parseInt(request.getParameter("dept_id"));
                String dept_status = "";
                try {
                    dept_status = cm.getDeptOnboardingStatus(dept_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.print(dept_status.toString());
                out.flush();
                out.close();
            }

            if (request.getParameter("bsid") != null) {
                String bsid = Security.SecurityClass.killchar(request.getParameter("bsid"));
                int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("type")));
                try {
                    if (type == 1) {
                        msg = Common.RCDataCheck.verifyRCByUID(bsid);
                    } else if (type == 2) {
                        msg = Common.RCDataCheck.verifyRCByRCID(bsid);
                    } else if (type == 3) {
                        msg = Common.RCDataCheck.verifyRCByMemID(bsid);
                    }

                    jsarr = new JSONArray(msg);
                    jsobj = new JSONObject();
                    jsobj.put("aaData", jsarr);
                } catch (Exception ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj);
            }
//             if (request.getParameter("getCalculationParam") != null) {
//                int method = Integer.parseInt(request.getParameter("method"));
//                String getCalculationParamHtml = "";
//                try {
//                    getCalculationParamHtml = cm.getCalculationParamHtml(method);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                out.print(getCalculationParamHtml.toString());
//                out.flush();
//                out.close();
//            }

            if (request.getParameter("getcat_financial_status_html") != null) {
                int schemeid = Integer.parseInt(request.getParameter("scheme_id"));
                String amount_cond = request.getParameter("amount_cond");
                String getcat_financial_status_html = "";
                try {
                    getcat_financial_status_html = cm.getcat_financial_status_html(schemeid, amount_cond);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.print(getcat_financial_status_html.toString());
                out.flush();
                out.close();
            }
            if (request.getParameter("getdocument_list_html_preview") != null) {
                int schemeid = Integer.parseInt(request.getParameter("scheme_id"));
                int doc_type = Integer.parseInt(request.getParameter("doc_type"));
                String getdocument_list_html = "";
                try {
                    getdocument_list_html = cm.getdocument_list_html_preview(schemeid, doc_type);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.print(getdocument_list_html.toString());
                out.flush();
                out.close();
            }
             if (request.getParameter("getUnitDropdown") != null) {
                int method = Integer.parseInt(request.getParameter("method"));
                String getUnitDropdownHtml = "";
                try {
                    getUnitDropdownHtml = cm.getUnitDropdownHtml(method,"-Please Select-");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.print(getUnitDropdownHtml.toString());
                out.flush();
                out.close();
            }
            if (request.getParameter("getdocument_list_html") != null) {
                int schemeid = Integer.parseInt(request.getParameter("scheme_id"));
                String getdocument_list_html = "";
                try {
                    getdocument_list_html = cm.getdocument_list_html(schemeid);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.print(getdocument_list_html.toString());
                out.flush();
                out.close();
            }

            if (request.getParameter("searchBen") != null) {
                int id = 0;
                String uid = "";//Security.SecurityClass.killchar(request.getParameter("uid"));
                String rc = Security.SecurityClass.killchar(request.getParameter("rc"));
                String rcm = Security.SecurityClass.killchar(request.getParameter("rcm"));
                String name = Security.SecurityClass.killchar(request.getParameter("name"));

                try {
                    id = Common.RCDataCheck.verifyBenExists(uid, rc, rcm);
                } catch (SQLException ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(id);
            }

            if (request.getParameter("searchBenAccByPmkisan") != null) {
                int id = 0;
                String pmkisanId = Security.SecurityClass.killchar(request.getParameter("pmkisanId"));

                try {
                    msg = Common.RCDataCheck.getBankAccDetByPmkisanId(pmkisanId);
                } catch (SQLException ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(msg);
            }

            if (request.getParameter("schmDbtItem") != null) {
                String result = "";
                int i = 0;
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                int benId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("benId")));
                String finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                int instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                String scheme = Security.SecurityClass.killchar(request.getParameter("schemeId"));
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                String schemeCode = scheme.split("~")[1];

                String sql = "SELECT item_name, item_quantity, item_unit FROM tran_dbt_scheme_beneficiary_kind a "
                        + "INNER JOIN master_dbt_items b ON a.item_id = b.item_id "
                        + "WHERE beneficiary_id = " + benId + " AND scheme_id = " + schemeId + " "
                        + "AND financial_year ='" + finYr + "' AND installment = " + instl + " AND status = 1 AND approved = false";

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                try {
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        i++;
                        result += "<tr>";
                        result += "<td>" + i + "</td>";
                        result += "<td>" + rs.getString("item_name") + "</td>";
                        result += "<td>" + rs.getInt("item_quantity") + " " + rs.getString("item_unit") + "</td>";
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
            }

            if (request.getParameter("schmb") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                String finYr = "", tabName = "";
                int instl = 0;
                String scheme = Security.SecurityClass.killchar(request.getParameter("schemeId"));
                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                //String schemeCode = scheme.split("~")[1];
                if (!request.getParameter("finYr").equals("") && request.getParameter("finYr") != null) {
                    finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                }
                if (!request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1") && request.getParameter("instl") != null) {
                    instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                }
                int dbtType = cm.getSchemeDbtType(schemeId);

                if (dbtType == 1) {
                    tabName = "tran_dbt_scheme_beneficiary_payment";
                } else if (dbtType == 2) {
                    tabName = "tran_dbt_scheme_beneficiary_kind";
                }
                String sql = "SELECT DISTINCT batch_id FROM " + tabName + " WHERE scheme_id = " + schemeId + " AND financial_year = '" + finYr + "' AND installment=" + instl;
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    String wList = "var wlistArray = new Array();";
                    String wListID = "var wlistIDArray = new Array();";

                    int i = 0;
                    while (rset.next()) {
                        String item_name = rset.getString(1);
                        String item_id = rset.getString(1);

                        wList = wList + "wlistArray[" + i + "]=\"" + item_name + "\";";
                        wListID = wListID + "wlistIDArray[" + i + "]=\"" + item_id + "\";";
                        i++;

                    }
                    if (i == 0) {
                        out.print("No Data");
                    } else {
                        out.print(wList + wListID);
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
            }

            if (request.getParameter("category") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                String sql = "";
                String category = Security.SecurityClass.killchar(request.getParameter("category"));
                Integer itemVal = 0;

                try {
                    if (request.getParameter("value") != null || !(request.getParameter("value").equals(""))) {
                        itemVal = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value")));
                    }

                    if (category.equals("fillDbtItems")) {
                        sql = "SELECT item_id||'#'||item_measurement_unit, item_name FROM master_dbt_items WHERE item_category_id = " + itemVal + " AND active = 1";
                    } else if (category.equals("fillSaStore")) {
                        sql = "SELECT DISTINCT subdivisional_store_code, administrative_entity_name FROM lgd.subdivisional_store WHERE lgd_dist_code=" + itemVal + " ORDER BY administrative_entity_name";
                    } else if (category.equals("fillVlwStore")) {
                        sql = "SELECT DISTINCT store_code, administrative_entity_name FROM lgd.vlw_store WHERE subdivisional_store_code=" + itemVal + " ORDER BY administrative_entity_name";
                    } else if (category.equals("fillSaGp")) {
                        sql = "SELECT DISTINCT lgd_gp_code, gp_vc_name FROM lgd.vlwstore_gpmapping WHERE subdivisional_store_code='" + itemVal + "' AND lgd_gp_code IS NOT NULL ORDER BY gp_vc_name";
                    } else if (category.equals("fillvlw")) {
                        sql = "SELECT DISTINCT store_code, store_name FROM lgd.vlwstore_gpmapping WHERE block_code='" + itemVal + "' ORDER BY store_name";
                    } else if (category.equals("fillSubDiv")) {
                        sql = "SELECT lgd_subdivision_code, subdivision_name FROM lgd.tbl_subdivision WHERE lgd_district_code='" + itemVal + "' ORDER BY subdivision_name";
                    } else if (category.equals("fillOfficeBlock")) {
                        sql = "SELECT DISTINCT lgd_block_code, lgd_block_name FROM map_maker_to_lgd_gp WHERE office_id= " + itemVal + " ORDER BY lgd_block_name";
                    } else if (category.equals("fillBlock")) {
                        sql = "SELECT DISTINCT block_code, block_name FROM lgd.master_dist_sd_block_bac_gp WHERE subdiv_code= " + itemVal + " ORDER BY block_name";
                    } else if (category.equals("fillGp")) {
                        sql = "SELECT DISTINCT gp_vc_ward_code, gp_vc_ward_name FROM lgd.master_dist_sd_block_bac_gp WHERE block_code=" + itemVal + " ORDER BY gp_vc_ward_name";
                    } else if (category.equals("fillPoliceStation")) {
                        sql = "SELECT DISTINCT sl_no, police_station_name FROM lgd.mas_police_station WHERE district_lgd_code='" + itemVal + "' ORDER BY police_station_name";
                    } else if (category.equals("fillPostOffice")) {
                        sql = "SELECT DISTINCT sl||'#'||pin, post_office FROM lgd.mas_post_office WHERE subdiv_lgd_code='" + itemVal + "' ORDER BY post_office";
                    } else if (category.equals("fillRevCircle")) {
                        sql = "SELECT DISTINCT lgd_circle_code, circle_name FROM lgd.revenue_village_master WHERE lgd_subdiv_code='" + itemVal + "' ORDER BY circle_name";
                    } else if (category.equals("fillTehsil")) {
                        sql = "SELECT DISTINCT lgd_tehsil_code, tehsil_name FROM lgd.revenue_village_master WHERE lgd_circle_code='" + itemVal + "' ORDER BY tehsil_name";
                    } else if (category.equals("fillRevVill")) {
                        sql = "SELECT lgd_village_code, revenuevillage_name FROM lgd.revenue_village_master WHERE lgd_tehsil_code='" + itemVal + "' ORDER BY revenuevillage_name";
                    } else if (category.equals("fillCropName")) {
                        sql = "SELECT crop_id, crop_name FROM master_crop_name WHERE crop_type_id='" + itemVal + "' ORDER BY crop_name";

                    } else if (category.equals("fillDeptOnBSchemes")) {
                        sql = "SELECT id||'~'||scheme_codification||'~'||scheme_name, scheme_name FROM master_scheme "
                                + "INNER JOIN scheme_onboarding.scheme_ob_sh_dept_bsp ON scheme_ob_sh_dept_bsp.scheme_id=master_scheme.onboarding_id AND (scheme_ob_sh_dept_bsp.maker_dept_id=" + itemVal + " OR "
                                + "scheme_ob_sh_dept_bsp.checker_dept_id=" + itemVal + " OR \n"
                                + " scheme_ob_sh_dept_bsp.approver_dept_id=" + itemVal + ") "
                                + "WHERE master_scheme.status = '1' AND master_scheme.onboarding_id is NOT NULL GROUP BY  master_scheme.id";

                    }else if (category.equals("fillUrlPermSchemes")) {
                        sql = "SELECT id, scheme_name FROM master_scheme "
                                + "INNER JOIN scheme_onboarding.scheme_ob_sh_dept_bsp ON scheme_ob_sh_dept_bsp.scheme_id=master_scheme.onboarding_id AND (scheme_ob_sh_dept_bsp.maker_dept_id=" + itemVal + " OR "
                                + "scheme_ob_sh_dept_bsp.checker_dept_id=" + itemVal + " OR \n"
                                + " scheme_ob_sh_dept_bsp.approver_dept_id=" + itemVal + ") "
                                + "WHERE master_scheme.status = '1' AND master_scheme.onboarding_id is NOT NULL GROUP BY  master_scheme.id";

                    } else if (category.equals("fillHeadersOnUrl")) {
                        sql = "SELECT url_id, url_name FROM mas_application_url WHERE  header_id = " + itemVal + " AND is_active = 1";

                    } else if (category.equals("fillRole")) {
                        //sql = "SELECT role_id, role_name FROM mas_admin_user_role WHERE department_id='" + itemVal + "' ORDER BY role_name";

                    } else if (category.equals("fillOffice")) {
                        sql = "SELECT office_id, office_name FROM bms_user.mas_dept_users_group_based_office WHERE role_id='" + itemVal + "' ORDER BY office_name"; // need scheme 
                    } else if (category.equals("fillGroup")) {
                        sql = "SELECT group_id, group_name FROM bms_user.mas_dept_users_group WHERE department_id='" + itemVal + "' ORDER BY group_name";
                    } else if (category.equals("fillDeptScheme")) {
                        sql = "SELECT id, scheme_name FROM master_scheme ms "
                                + " WHERE maker_ben_dept = " + itemVal + " OR checker_ben_dept = " + itemVal + " OR approver_ben_dept = " + itemVal + " "
                                + " OR maker_pay_dept = " + itemVal + " OR checker_pay_dept = " + itemVal + " OR approver_pay_dept=" + itemVal;

                    } else if (category.equals("fillGroupOffices")) {
                        sql = "SELECT a.office_id, a.office_name FROM bms_user.mas_dept_users_group_based_office a "
                                + "INNER JOIN bms_user.map_dept_users_group_based_office_users b ON a.office_id = b.office_id "
                                + "WHERE b.group_id = " + itemVal + " ORDER BY a.office_name";
                    } else {
                        Integer itemVal1 = 0;

                        if (category.equals("fillOfficeGp")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }

                            sql = "SELECT DISTINCT lgd_gp_code, lgd_gp_name FROM map_maker_to_lgd_gp WHERE lgd_block_code='" + itemVal + "' AND office_id= " + itemVal1 + " ORDER BY lgd_gp_name";
                        } else if (category.equals("fillSchemesOnRoles")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")).split("~")[0]);
                            }
                            int onboarding_scheme_id = cm.getOnboardedSchemeId(itemVal1);
                            String role_id_list = cm.getdept_roleid_list(onboarding_scheme_id, itemVal);

                            if (!"".equals(role_id_list)) {
                                sql = "SELECT role_id, role_name FROM bms_user.mas_user_roles where role_id IN(" + role_id_list + ")";
                            } else {
                                sql = "SELECT role_id, role_name FROM bms_user.mas_user_roles WHERE role_type = 4";
                            }

                        } else if (category.equals("fillGroupUsers")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }

                            sql = "SELECT u.user_id, emp_name FROM bms_user.mas_dept_users u "
                                    + "INNER JOIN bms_user.map_dept_users_group_users gu ON u.user_id = gu.user_id "
                                    + "WHERE gu.department_id = " + itemVal + " AND gu.group_id = " + itemVal1 + " "
                                    + "ORDER BY emp_name ASC";

                        } else if (category.equals("fillParentOffice")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }
//                            String roleLevel = cm.getRoleLevel(itemVal);
//                            if (roleLevel.equals("Maker")) {
//                                sql = "SELECT DISTINCT office_id, office_name \n"
//                                        + "FROM mas_role_based_office a \n"
//                                        + "INNER JOIN mas_admin_user_role b ON a.role_id = b.role_id AND a.dept_id = b.department_id \n"
//                                        + "WHERE role_level = 'Checker' AND department_id = " + itemVal1 + " \n"
//                                        + "ORDER BY office_name";
//                            } else if (roleLevel.equals("Checker")) {
//                                sql = "SELECT DISTINCT office_id, office_name \n"
//                                        + "FROM mas_role_based_office a \n"
//                                        + "INNER JOIN mas_admin_user_role b ON a.role_id = b.role_id AND a.dept_id = b.department_id \n"
//                                        + "WHERE role_level = 'Approver' AND department_id = " + itemVal1 + " \n"
//                                        + "ORDER BY office_name";
//                            }

                        } /* else if (category.equals("fillMultiOffice")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }

                            sql = "SELECT DISTINCT office_id, office_name FROM mas_role_based_office a WHERE dept_id = " + itemVal + " AND role_id = " + itemVal1 + " ORDER BY office_name";
                        } */ else if (category.equals("fillScheme")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }

                            sql = "SELECT id, scheme_name , scheme_codification FROM master_scheme ms INNER JOIN admin_user_role_scheme_mapping rs ON ms.id=rs.scheme_id WHERE dept_id= " + itemVal + "  AND role_id= " + itemVal1 + "  ORDER BY scheme_name ";
                        } //else if (category.equals("fillSchemeRole")) {
                        else if (category.equals("fillMultiOffice")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }
                            sql = " SELECT a.office_id, office_name FROM bms_user.mas_dept_users_group_based_office a "
                                    + "WHERE office_id NOT IN (SELECT office_id FROM admin_user_role_scheme_office_mapping map "
                                    + "WHERE map.role_id = " + itemVal + " AND map.scheme_id = " + itemVal1 + ") ";

                        } else if (category.equals("fillMapParentDepartment")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }
                            String roleName = cm.getRoleName(itemVal, itemVal1);

                            if (roleName.equals("Maker")) {

                                sql = "SELECT a.department_id, department_name  FROM mas_department a "
                                        + "WHERE a.department_id IN (SELECT department_id FROM bms_user.map_dept_users_group_schemes a "
                                        + "INNER JOIN bms_user.mas_user_roles b ON a.role_id = b.role_id "
                                        + "WHERE scheme_id = " + itemVal + " AND b.role_name = 'Checker' )";

                            } else if (roleName.equals("Checker")) {

                                sql = "SELECT a.department_id, department_name  FROM mas_department a "
                                        + "WHERE a.department_id IN (SELECT department_id FROM bms_user.map_dept_users_group_schemes a "
                                        + "INNER JOIN bms_user.mas_user_roles b ON a.role_id = b.role_id "
                                        + "WHERE scheme_id = " + itemVal + " AND b.role_name = 'Approver' )";

                            } else if (roleName.equals("Approver")) {

                                sql = "SELECT 0 AS department_id, 'No Department' AS department_name  FROM mas_department LIMIT 1";
                            }

                        } else if (category.equals("fillMapParentOffice")) {
                            if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                            }
                            String roleName = cm.getRoleName(itemVal, itemVal1);

                            if (roleName.equals("Maker")) {
//                                sql = " SELECT DISTINCT office_id,office_name FROM admin_user_role_scheme_mapping map \n"
//                                        + " INNER JOIN master_scheme ms ON id = scheme_id \n"
//                                        + " INNER JOIN mas_role_based_office off ON off.dept_id = ms.department_id \n"
//                                        + " WHERE off.dept_id = (SELECT checker_ben_dept FROM master_scheme WHERE id = " + itemVal1 + ") AND scheme_id = " + itemVal1 + " \n"
//                                        + " ORDER BY office_name";

//                                sql = " SELECT DISTINCT a.office_id, office_name FROM mas_role_based_office a "
//                                        + " WHERE dept_id = (SELECT checker_ben_dept FROM master_scheme WHERE id = " + itemVal1 + ") "
//                                        + " AND role_id = (SELECT a.role_id FROM admin_user_role_scheme_mapping a INNER JOIN mas_admin_user_role b ON a.role_id = b.role_id "
//                                        + " WHERE scheme_id = " + itemVal1 + " AND b.role_level = 'Checker' )";
                                sql = "SELECT office_id, office_name \n"
                                        + "FROM bms_user.mas_dept_users_group_based_office a \n"
                                        + "INNER JOIN bms_user.mas_user_roles b ON a.role_id = b.role_id AND a.dept_id = b.department_id \n"
                                        + "WHERE role_name = 'Checker' AND department_id = " + itemVal1 + " \n"
                                        + "ORDER BY office_name";

                            } else if (roleName.equals("Checker")) {
//                                sql = " SELECT DISTINCT office_id,office_name FROM admin_user_role_scheme_mapping map \n"
//                                        + " INNER JOIN master_scheme ms ON id = scheme_id \n"
//                                        + " INNER JOIN mas_role_based_office off ON off.dept_id = ms.department_id \n"
//                                        + " WHERE off.dept_id = (SELECT approver_ben_dept FROM master_scheme WHERE id = " + itemVal1 + ") AND scheme_id = " + itemVal1 + " \n"
//                                        + " ORDER BY office_name";

//                                sql = " SELECT DISTINCT a.office_id, office_name FROM mas_role_based_office a "
//                                        + " WHERE dept_id = (SELECT approver_ben_dept FROM master_scheme WHERE id = " + itemVal1 + ") "
//                                        + " AND role_id = (SELECT a.role_id FROM admin_user_role_scheme_mapping a INNER JOIN mas_admin_user_role b ON a.role_id = b.role_id "
//                                        + " WHERE scheme_id = " + itemVal1 + " AND b.role_level = 'Approver' )";
                                sql = "SELECT DISTINCT office_id, office_name \n"
                                        + "FROM bms_user.mas_dept_users_group_based_office a \n"
                                        + "INNER JOIN bms_user.mas_user_roles b ON a.role_id = b.role_id AND a.dept_id = b.department_id \n"
                                        + "WHERE role_name = 'Approver' AND department_id = " + itemVal1 + " \n"
                                        + "ORDER BY office_name";
                            } else if (roleName.equals("Approver")) {

                                sql = "SELECT 0 AS office_id, 'No Office' AS office_name  FROM bms_user.mas_dept_users_group_based_office LIMIT 1";
                            }

                        } else {
                            Integer itemVal2 = 0;

                            if (category.equals("fillSchemeGroup")) {
                                if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                    itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                                }

                                if (request.getParameter("value2") != null || !(request.getParameter("value2").equals(""))) {
                                    itemVal2 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value2")));
                                }

                                sql = "SELECT DISTINCT grp.group_id, grp.group_name FROM bms_user.mas_dept_users_group grp \n"
                                        + " INNER JOIN bms_user.map_dept_users_group_schemes map ON map.group_id = grp.group_id \n"
                                        + " WHERE map.department_id = " + itemVal + " AND map.scheme_id = " + itemVal1 + " AND (mapping_type = " + itemVal2 + " OR mapping_type = 3) ORDER BY group_name";

                            } else if (category.equals("fillMapParentGroup")) {
                                if (request.getParameter("value1") != null || !(request.getParameter("value1").equals(""))) {
                                    itemVal1 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value1")));
                                }

                                if (request.getParameter("value2") != null || !(request.getParameter("value2").equals(""))) {
                                    itemVal2 = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("value2")));
                                }
                                String roleName = cm.getRoleName(itemVal, itemVal1);
                                if (roleName.equals("Maker")) {
                                    sql = "SELECT a.group_id, group_name FROM bms_user.mas_dept_users_group a "
                                            + "INNER JOIN bms_user.map_dept_users_group_schemes b ON a.group_id = b.group_id "
                                            + "INNER JOIN bms_user.mas_user_roles c ON b.role_id = c.role_id "
                                            + "WHERE scheme_id = " + itemVal + " AND b.department_id = " + itemVal2 + " AND c.role_name = 'Checker'";

                                } else if (roleName.equals("Checker")) {
                                    sql = "SELECT a.group_id, group_name FROM bms_user.mas_dept_users_group a "
                                            + "INNER JOIN bms_user.map_dept_users_group_schemes b ON a.group_id = b.group_id "
                                            + "INNER JOIN bms_user.mas_user_roles c ON b.role_id = c.role_id "
                                            + "WHERE scheme_id = " + itemVal + " AND b.department_id = " + itemVal2 + " AND c.role_name = 'Approver'";

                                } else if (roleName.equals("Approver")) {

//                                    sql = "SELECT 0 AS group_id, 'No Group' AS group_name  FROM bms_user.mas_dept_users_group LIMIT 1";
                                    sql = "SELECT group_id, group_name  FROM bms_user.mas_dept_users_group WHERE department_id = " + itemVal2 + " LIMIT 1";
                                }

                            } else if (category.equals("fillChildOffice")) {
                                sql = "SELECT office_id, office_name FROM bms_user.map_admin_workflow WHERE  parent_office_id = " + itemVal + " AND scheme_id = " + itemVal1 + "  AND  (mapping_type = " + itemVal2 + " OR mapping_type = 3)";
                            }

                        }

//                        if (itemVal1 == 1 || itemVal1 == 2 || itemVal1 == 3) {
//                            if (category.equals("fillUrbRurBody")) {
//                                sql = "SELECT lgd_urban_code, lgd_urban_name FROM lgd.tbl_urban_body_sub WHERE lgd_subdiv_code='" + itemVal + "' ORDER BY lgd_urban_name";
//                            } else if (category.equals("fillWardGp")) {
//                                sql = "SELECT lgd_ward_code, lgd_ward_name FROM lgd.tbl_urban_ward WHERE lgd_urban_body_code='" + itemVal + "' ORDER BY lgd_ward_name";
//                            }
//                        } else if (itemVal1 == 4 || itemVal1 == 5) {
//                            if (category.equals("fillUrbRurBody")) {
//                                sql = "SELECT lgd_rural_body_code, lgd_rural_body_name FROM lgd.tbl_rural_body_sub WHERE lgd_subdiv_code='" + itemVal + "' ORDER BY lgd_rural_body_name";
//                            } else if (category.equals("fillWardGp")) {
//                                sql = "SELECT lgd_gp_code, lgd_gp_name FROM lgd.tbl_rural_gp WHERE lgd_rural_body_code='" + itemVal + "' ORDER BY lgd_gp_name";
//                            }
//                        }
                    }

                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    String wList = "var wlistArray = new Array();";
                    String wListID = "var wlistIDArray = new Array();";

                    int i = 0;
                    while (rset.next()) {
                        String item_name = rset.getString(2);
                        String item_id = rset.getString(1);

                        wList = wList + "wlistArray[" + i + "]=\"" + item_name + "\";";
                        wListID = wListID + "wlistIDArray[" + i + "]=\"" + item_id + "\";";
                        i++;

                    }
                    if (i == 0) {
                        //     out.print("No Data");
                        wList = wList + "wlistArray[" + i + "]=\"\";";
                        wListID = wListID + "wlistIDArray[" + i + "]=\"\";";
                        out.print(wList + wListID);
                    } else {
                        out.print(wList + wListID);
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
            }

            if (request.getParameter("addliddata") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                String benId = Security.SecurityClass.killchar(request.getParameter("benId"));

                String sql = "SELECT additional_id_type_id, additional_id_type_value FROM dbt_beneficiary_additional_ids WHERE beneficiary_id = " + benId;

                String addlIdTypeArray = "", addlIdTypeValArray = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    addlIdTypeArray += "var addlIdTypeArray = new Array();";
                    addlIdTypeValArray += "var addlIdTypeValArray = new Array();";

                    int i = 1;
                    while (rset.next()) {
                        int type = rset.getInt("additional_id_type_id");
                        String val = rset.getString("additional_id_type_value");

                        addlIdTypeArray += "addlIdTypeArray[" + i + "]=\"" + type + "\";";
                        addlIdTypeValArray += "addlIdTypeValArray[" + i + "]=\"" + val + "\";";

                        i++;

                    }
                    if (i == 1) {
                        out.print("No Data");

                    } else {

                        out.print(addlIdTypeArray + addlIdTypeValArray);
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
            }

            if (request.getParameter("agriGpReports") != null) {

            }

            if (request.getParameter("nodel_officer_edit") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                String dept_id = Security.SecurityClass.killchar(request.getParameter("dept_id"));
                String sql = "SELECT * FROM  department_onboarding.nodal_officer_data  WHERE dept_id = '" + dept_id + "'";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("user_id", rset.getString("id"));
                        json.put("nodal1_name", rset.getString("nodal1_name"));
                        json.put("nodal1_code", rset.getString("nodal1_code"));
                        json.put("nodal1_designation", rset.getString("nodal1_designation"));
                        json.put("nodal1_email", rset.getString("nodal1_email"));
                        json.put("nodal1_mobile", rset.getString("nodal1_mobile"));
                        json.put("nodal1_dob", rset.getString("nodal1_dob"));
                        json.put("nodal1_hrms_code", rset.getString("nodal1_hrms_code"));
                        json.put("nodal2_name", rset.getString("nodal2_name"));
                        json.put("nodal2_code", rset.getString("nodal2_code"));
                        json.put("nodal2_designation", rset.getString("nodal2_designation"));
                        json.put("nodal2_email", rset.getString("nodal2_email"));
                        json.put("nodal2_mobile", rset.getString("nodal2_mobile"));
                        json.put("nodal2_dob", rset.getString("nodal2_dob"));
                        json.put("nodal2_hrms_code", rset.getString("nodal2_hrms_code"));
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

            /* Developed by Nibedita  */
            if (request.getParameter("roleList") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                //  CommonMethod cm = new CommonMethod();
                String returnData = null;
                String[][] roleList = null;
                roleList = cm.getroleList(deptId);
                if (roleList != null) {
                    int len = roleList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        //returnData += "<td ><input type=\"text\" id=\"roleId" + (i+1) + "\" name=\"roleId\" value=\"" + roleList[i][0] + "\"></td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + roleList[i][1] + "</td>";
                        returnData += "<td>" + roleList[i][2] + "</td>";
                        returnData += "<td>" + roleList[i][3] + "</td>";

                        returnData += "<td><button type=\"button\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\" value=\"" + roleList[i][0] + "\" >Edit</button></td>"; //onclick=\"edit_roleList(" + roleList[i][0] + ")\"
                        returnData += "<td><button type=\"button\" id=\"btnSchemeList\" name=\"btnSchemeList\" class=\"btn btn-primary\" value=\"" + roleList[i][0] + "#" + roleList[i][4] + "\" >Map Scheme</button></td>"; //onclick=\"mapScheme(" + roleList[i][0] + "," + roleList[i][4] + ")\"
                        returnData += "<td><button type=\"button\" id=\"btnMappedSchemeList\" name=\"btnMappedSchemeList\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#myModal1\"  value=\"" + roleList[i][0] + "#" + roleList[i][4] + "\" >View Mapped Scheme</button></td>"; //onclick=\"mapScheme(" + roleList[i][0] + "," + roleList[i][4] + ")\"
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No Role Details Found !!!";
                }
                out.print(returnData);
            }

            if (request.getParameter("roleEdit") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int role_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                String sql = "SELECT * FROM bms_user.mas_user_roles  WHERE role_id = " + role_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("role_id", rset.getString("role_id"));
                        json.put("role_name", rset.getString("role_name"));
                        json.put("department_id", rset.getString("department_id"));
                        //json.put("role_level", rset.getString("role_level"));

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

            if (request.getParameter("groupEdit") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int group_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                String sql = "SELECT * FROM bms_user.mas_dept_users_group  WHERE group_id = " + group_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("group_id", rset.getString("group_id"));
                        json.put("group_name", rset.getString("group_name"));
                        json.put("department_id", rset.getString("department_id"));
                        //json.put("role_level", rset.getString("role_level"));

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

            if (request.getParameter("urlList") != null) {
                //  CommonMethod cm = new CommonMethod();
                String returnData = null;
                String[][] urlList = null;
                String Yes = "Yes";
                String No = "No";
                urlList = cm.getUrlList();
                if (urlList != null) {
                    int len = urlList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        returnData += "<td style=\"display:none\">" + urlList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + urlList[i][1] + "</td>";
                        returnData += "<td>" + urlList[i][2] + "</td>";
                        if (urlList[i][3].equals("0")) {
                            returnData += "<td> No </td>";
                        } else {
                            returnData += "<td> Yes </td>";
                        }
                        // returnData += "<td>" + urlList[i][3] + "</td>";

                        if (urlList[i][4].equals("0")) {
                            returnData += "<td> No </td>";
                        } else {
                            returnData += "<td> Yes </td>";
                        }

                        returnData += "<td><button type=\"submit\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_urlList(" + urlList[i][0] + ")\">Edit</button>"
                                + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No URL Details Found !!!";
                }
                out.print(returnData);
            }

            if (request.getParameter("userList") != null) {
                /*
                //  CommonMethod cm = new CommonMethod();
                //int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("userList")));
                // int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("role")));
                String returnData = "";
                String[][] userList = null;
                //userList = cm.getUserList(deptId, roleId);
                userList = cm.getUserList();
                if (userList != null) {
                    int len = userList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        returnData += "<td style=\"display:none\">" + userList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + userList[i][1] + "</td>";
                        //returnData += "<td>" + userList[i][7] + "</td>";
                        returnData += "<td>" + userList[i][2] + "</td>";
                        returnData += "<td>" + userList[i][3] + "</td>";
                        returnData += "<td>" + userList[i][4] + "</td>";
                        returnData += "<td>" + userList[i][5] + "</td>";
                        returnData += "<td>" + userList[i][6] + "</td>";
                        returnData += "<td>" + userList[i][7] + "</td>";
                        returnData += "<td>" + userList[i][8] + "</td>";
                        /* returnData += "<td>" + userList[i][3] + "</td>";
                        returnData += "<td>" + userList[i][4] + "</td>";*/
                //returnData += "<td>" + userList[i][5] + "</td>";
                /*  returnData += "<td>" + userList[i][6] + "</td>";*/
//                        returnData += "<td>" + userList[i][8] + "</td>";
//                        if (userList[i][5].equals("Male")) {
//                            returnData += "<td> Male </td>";
//                        } else {
//                            returnData += "<td> Female </td>";
//                        }
//
//                        //returnData += "<td>" + userList[i][5] + "</td>";
//                        returnData += "<td>" + userList[i][9] + "</td>";
//                        returnData += "<td>" + userList[i][10] + "</td>";
//                        returnData += "<td>" + userList[i][11] + "</td>";
//                        returnData += "<td>" + userList[i][12] + "</td>";
//                        returnData += "<td>" + userList[i][13] + "</td>";
//                        returnData += "<td style=\"display:none\">" + userList[i][14] + "</td>";
//                        int fid = Integer.parseInt(userList[i][14]);
//                        if (fid > 0) {
//                            returnData += "<td> <img id=\"preview_photo\" src=\"../ViewFile?fId=" + fid + "\" alt=\"\" height=\"100px\" width=\"100px\" style=\"border: 1px solid black;\" class=\"mx-auto\"/> </td>";
//                        } else {
//                            returnData += "<td></td>";
//                        }
                /*    returnData += "<td><button type=\"button\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_userList('" + userList[i][0] + "')\">Edit</button>"
                                + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No User Details Found !!!";
                }
                out.print(returnData);*/

                //int schemeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("schemeId")));
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                Connection con = null;

                String sql = "SELECT user_id,emp_name,emp_code,emp_designation,emp_email,emp_mobile,emp_dob,COALESCE(hrms_code,'') AS hrms_code,COALESCE(login_id,'') AS login_id  FROM bms_user.mas_dept_users WHERE emp_department_id = " + deptId + " ";

                jsobj2 = new JSONObject();
                try {
                    jsarr2 = cm.getResultJSON(sql);
                    jsobj2.put("aaData", jsarr2);
                } catch (Exception ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj2);
                out.flush();
                out.close();

            }

            if (request.getParameter("adminUserList") != null) {
                //  CommonMethod cm = new CommonMethod();
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("adminUserList")));
                int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("role")));
                String returnData = "";
                String[][] userList = null;
                userList = cm.getUserList(deptId, roleId);
                if (userList != null) {
                    int len = userList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        returnData += "<td style=\"display:none\">" + userList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + userList[i][1] + "</td>";
                        returnData += "<td>" + userList[i][2] + "</td>";
                        returnData += "<td>" + userList[i][3] + "</td>";
                        returnData += "<td>" + userList[i][4] + "</td>";
                        returnData += "<td>" + userList[i][5] + "</td>";
                        returnData += "<td>" + userList[i][6] + "</td>";
                        // returnData += "<td>" + userList[i][7] + "</td>";
                        // returnData += "<td>" + userList[i][8] + "</td>";
                        /* returnData += "<td>" + userList[i][3] + "</td>";
                        returnData += "<td>" + userList[i][4] + "</td>";*/
                        //returnData += "<td>" + userList[i][5] + "</td>";
                        /*  returnData += "<td>" + userList[i][6] + "</td>";*/
//                        returnData += "<td>" + userList[i][8] + "</td>";
//                        if (userList[i][5].equals("Male")) {
//                            returnData += "<td> Male </td>";
//                        } else {
//                            returnData += "<td> Female </td>";
//                        }
//
//                        //returnData += "<td>" + userList[i][5] + "</td>";
//                        returnData += "<td>" + userList[i][9] + "</td>";
//                        returnData += "<td>" + userList[i][10] + "</td>";
//                        returnData += "<td>" + userList[i][11] + "</td>";
//                        returnData += "<td>" + userList[i][12] + "</td>";
//                        returnData += "<td>" + userList[i][13] + "</td>";
//                        returnData += "<td style=\"display:none\">" + userList[i][14] + "</td>";
//                        int fid = Integer.parseInt(userList[i][14]);
//                        if (fid > 0) {
//                            returnData += "<td> <img id=\"preview_photo\" src=\"../ViewFile?fId=" + fid + "\" alt=\"\" height=\"100px\" width=\"100px\" style=\"border: 1px solid black;\" class=\"mx-auto\"/> </td>";
//                        } else {
//                            returnData += "<td></td>";
//                        }
                        returnData += "<td><button type=\"button\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_userList('" + userList[i][0] + "')\">Edit</button>"
                                + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No User Details Found !!!";
                }
                out.print(returnData);
            }

            if (request.getParameter("groupList") != null) {

                Connection con = null;
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                String sql = "SELECT grp.department_id, group_id, group_name, department_name FROM bms_user.mas_dept_users_group grp INNER JOIN mas_department AS dept ON grp.department_id = dept.department_id WHERE grp.department_id = " + deptId;

                jsobj2 = new JSONObject();
                try {
                    jsarr2 = cm.getResultJSON(sql);
                    jsobj2.put("aaData", jsarr2);
                } catch (Exception ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj2);
                out.flush();
                out.close();

            }

            if (request.getParameter("urlId") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int url_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlId")));
                String sql = "SELECT * FROM mas_application_url  WHERE url_id = " + url_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("url_id", rset.getString("url_id"));
                        json.put("url", rset.getString("url"));
                        json.put("url_name", rset.getString("url_name"));
                        json.put("is_header", rset.getString("is_header"));
                        json.put("is_active", rset.getString("is_active"));
                        json.put("url_desc", rset.getString("url_desc"));
                        json.put("header_id", rset.getString("header_id"));
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

            if (request.getParameter("editUser") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                String user_id = Security.SecurityClass.killchar(request.getParameter("userId"));
                //String sql = "SELECT * FROM mas_admin_user_account  WHERE user_id = '" + user_id + "'";
                String sql = "SELECT * FROM bms_user.mas_dept_users  WHERE user_id = '" + user_id + "'";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("user_id", rset.getString("user_id"));
                        json.put("emp_name", rset.getString("emp_name"));
                        json.put("emp_code", rset.getString("emp_code"));
                        json.put("emp_designation", rset.getString("emp_designation"));
                        // json.put("user_role", rset.getString("user_role"));
                        json.put("emp_email", rset.getString("emp_email"));
                        json.put("emp_mobile", rset.getString("emp_mobile"));
//                        json.put("user_gender", rset.getString("user_gender"));
//                        json.put("user_alt_mobile", rset.getString("user_alt_mobile"));
//                        json.put("user_department_id", rset.getString("user_department_id"));
//                        json.put("user_designation", rset.getString("user_designation"));
//                        json.put("address1_road_landmark", rset.getString("address1_road_landmark"));
//                        json.put("address1_city_town_village", rset.getString("address1_city_town_village"));
//                        json.put("address1_po", rset.getString("address1_po"));
//                        json.put("address1_district", rset.getString("address1_district"));
//                        json.put("address1_pin", rset.getString("address1_pin"));
//                        json.put("user_photo_fid", rset.getInt("user_photo_fid"));
//                        json.put("user_rc_member_id", rset.getString("user_rc_member_id"));
                        json.put("emp_dob", rset.getString("emp_dob"));
                        json.put("hrms_code", rset.getString("hrms_code"));
                        json.put("rc_mem", rset.getString("rc_mem"));
//                        json.put("user_office", rset.getString("user_office"));
//                        json.put("user_login_id", rset.getString("user_login_id"));

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

            if (request.getParameter("adminUserId") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                String user_id = Security.SecurityClass.killchar(request.getParameter("userId"));
                String sql = "SELECT * FROM mas_admin_users_account  WHERE user_id = '" + user_id + "'";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("user_id", rset.getString("user_id"));
                        json.put("emp_name", rset.getString("user_name"));
                        json.put("user_role", rset.getString("user_role"));
                        json.put("emp_email", rset.getString("user_email"));
                        json.put("emp_mobile", rset.getString("user_mobile"));
                        json.put("user_department_id", rset.getString("user_department_id"));
                        json.put("user_designation", rset.getString("user_designation"));
                        json.put("rc_mem", rset.getString("rc_mem"));
                        json.put("user_login_id", rset.getString("user_login_id"));

//                        json.put("user_gender", rset.getString("user_gender"));
//                        json.put("user_alt_mobile", rset.getString("user_alt_mobile"));
//                        
//                        json.put("address1_road_landmark", rset.getString("address1_road_landmark"));
//                        json.put("address1_city_town_village", rset.getString("address1_city_town_village"));
//                        json.put("address1_po", rset.getString("address1_po"));
//                        json.put("address1_district", rset.getString("address1_district"));
//                        json.put("address1_pin", rset.getString("address1_pin"));
//                        json.put("user_photo_fid", rset.getInt("user_photo_fid"));
//                        json.put("user_rc_member_id", rset.getString("user_rc_member_id"));
//                        json.put("user_office", rset.getString("user_office"));
//                        json.put("user_login_id", rset.getString("user_login_id"));
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

            if (request.getParameter("officeList") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeList")));
                int groupId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));
                //  CommonMethod cm = new CommonMethod();
                /*     String returnData = "";
                String[][] officeList = null;
                officeList = cm.getOfficeList(deptId, groupId);
                String Yes = "Yes";
                String No = "No";

                if (officeList != null) {
                    int len = officeList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        //returnData += "<td style=\"display:none\">" + urlRoleList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + officeList[i][1] + "</td>";
                        returnData += "<td>" + officeList[i][2] + "</td>";
                        returnData += "<td>" + officeList[i][3] + "</td>";
                        //returnData += "<td>" + officeList[i][4] + "</td>";

                        returnData += "<td><button type=\"submit\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_officeList(" + officeList[i][0] + ")\">Edit</button>"
                                + " </td>";
                        returnData += "<td><button type=\"submit\" id=\"btnMapUserOffice\" name=\"btnMapUserOffice\" class=\"btn btn-primary\">Map User</button>" //onclick=\"edit_officeList(" + officeList[i][0] + ")\"
                                + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No Role Details Found !!!";
                }
                out.print(returnData);*/
                Connection con = null;
                String sql = "SELECT grp.department_id, group_id, group_name, department_name FROM bms_user.mas_dept_users_group grp INNER JOIN mas_department AS dept ON grp.department_id = dept.department_id WHERE grp.department_id = " + deptId;

                jsobj2 = new JSONObject();
                try {
                    jsarr2 = cm.getResultJSON(sql);
                    jsobj2.put("aaData", jsarr2);
                } catch (Exception ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj2);
                out.flush();
                out.close();

            }

            if (request.getParameter("urlRoleList") != null) {
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlRoleList")));
                int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("role")));
                //  CommonMethod cm = new CommonMethod();
                String returnData = "";
                String[][] urlRoleList = null;
                urlRoleList = cm.getUrlRoleList(deptId, roleId);
                String Yes = "Yes";
                String No = "No";

                if (urlRoleList != null) {
                    int len = urlRoleList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        //returnData += "<td style=\"display:none\">" + urlRoleList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + urlRoleList[i][1] + "</td>";
                        returnData += "<td>" + urlRoleList[i][2] + "</td>";
                        returnData += "<td>" + urlRoleList[i][3] + "</td>";

                        if (urlRoleList[i][4].equals("0")) {
                            returnData += "<td>" + Yes + "</td>";
                        } else {
                            returnData += "<td>" + No + "</td>";
                        }

                        if (urlRoleList[i][5].equals("0")) {
                            returnData += "<td>" + Yes + "</td>";
                        } else {
                            returnData += "<td>" + No + "</td>";
                        }

                        returnData += "<td>" + urlRoleList[i][6] + "</td>";

                        returnData += "<td><button type=\"submit\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_urlRoleList(" + urlRoleList[i][0] + ")\">Edit</button>"
                                + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No Role Details Found !!!";
                }
                out.print(returnData);
            }

            if (request.getParameter("urlRoleId") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int url_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("urlRoleId")));
                String sql = "SELECT * FROM map_admin_user_role_url  WHERE map_id = " + url_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("map_id", rset.getString("map_id"));
                        json.put("department_id", rset.getString("department_id"));
                        json.put("role_id", rset.getString("role_id"));
                        json.put("url_id", rset.getString("url_id"));
                        json.put("is_main_pg", rset.getString("is_main_pg"));
                        json.put("is_active", rset.getString("is_active"));
                        json.put("priority_no", rset.getString("priority_no"));

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

            if (request.getParameter("editOffice") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int office_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
                String sql = "SELECT * FROM bms_user.mas_dept_users_group_based_office  WHERE office_id = " + office_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();

                        //json.put("sl_no", rset.getString("sl_no"));
                        json.put("department_id", rset.getString("department_id"));
                        //   json.put("group_id", rset.getString("group_id"));
                        json.put("office_id", rset.getString("office_id"));
                        json.put("office_name", rset.getString("office_name"));
                        //json.put("parent_office_id", rset.getString("parent_office_id"));
                        json.put("lgd_mapping_required", rset.getString("lgd_mapping_required"));
                        json.put("dist_code", rset.getString("dist_code"));
                        json.put("subdiv_code", rset.getString("subdiv_code"));
                        json.put("block_code", rset.getString("block_code"));
                        json.put("ward_code", rset.getString("ward_code"));
                        json.put("last_mile_address", rset.getString("last_mile_address"));

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

            if (request.getParameter("reportList") != null) {
                //  CommonMethod cm = new CommonMethod();
                String returnData = null;
                String[][] reportList = null;
                reportList = cm.getReoprtList();
                if (reportList != null) {
                    int len = reportList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        returnData += "<td style=\"display:none\">" + reportList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + reportList[i][1] + "</td>";
                        returnData += "<td>" + reportList[i][2] + "</td>";
                        returnData += "<td>" + reportList[i][3] + "</td>";

                        returnData += "<td><button type=\"submit\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_reportList(" + reportList[i][0] + ")\">Edit</button>"
                                + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No Role Details Found !!!";
                }
                out.print(returnData);
            }

            if (request.getParameter("reoprtId") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                JSONObject json = null;
                String returnData = "";
                int report_id = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("reoprtId")));
                String sql = "SELECT * FROM map_admin_user_reporting_structure  WHERE map_id = " + report_id + "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    if (rset.next()) {
                        json = new JSONObject();
                        json.put("map_id", rset.getString("map_id"));
                        json.put("department_id", rset.getString("department_id"));
                        json.put("user_id_reportee", rset.getString("user_id_reportee"));
                        json.put("user_id_reporter", rset.getString("user_id_reporter"));
                        //json.put("is_main_pg", rset.getString("is_main_pg"));

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

            if (request.getParameter("irr") != null) {

                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                String sql = "SELECT id, category FROM master_irrigation";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    ilistArray += "var ilistArray = new Array();";
                    ilistIDArray += "var ilistIDArray = new Array();";

                    int i = 0;
                    while (rset.next()) {
                        String id = rset.getString("id");
                        String type = rset.getString("category");

                        ilistArray = ilistArray + "ilistArray[" + i + "]=\"" + type + "\";";
                        ilistIDArray = ilistIDArray + "ilistIDArray[" + i + "]=\"" + id + "\";";
                        i++;

                    }
                    if (i == 0) {
                        out.print("No Data");
                    } else {
                        out.print(ilistArray + ilistIDArray);
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
            }

            if (request.getParameter("cropFlag") != null) {

                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                Statement smt1 = null;
                ResultSet rset1 = null;
                String sql = "SELECT crop_type_id, crop_type_name FROM master_crop_type";
                String sql1 = "SELECT season_id, season_name FROM master_season";

                String cTypeListArray = "", cTypeListIDArray = "", cSeasonListArray = "", cSeasonListIDArray = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);
                    smt1 = con.createStatement();
                    rset1 = smt1.executeQuery(sql1);

                    cTypeListArray += "var cTypeListArray = new Array();";
                    cTypeListIDArray += "var cTypeListIDArray = new Array();";

                    int i = 0;
                    while (rset.next()) {
                        String id = rset.getString("crop_type_id");
                        String name = rset.getString("crop_type_name");

                        cTypeListArray = cTypeListArray + "cTypeListArray[" + i + "]=\"" + name + "\";";
                        cTypeListIDArray = cTypeListIDArray + "cTypeListIDArray[" + i + "]=\"" + id + "\";";
                        i++;

                    }

                    /* Season Data */
                    cSeasonListArray += "var cSeasonListArray = new Array();";
                    cSeasonListIDArray += "var cSeasonListIDArray = new Array();";
                    int j = 0;
                    while (rset1.next()) {
                        String id = rset1.getString("season_id");
                        String name = rset1.getString("season_name");

                        cSeasonListArray = cSeasonListArray + "cSeasonListArray[" + j + "]=\"" + name + "\";";
                        cSeasonListIDArray = cSeasonListIDArray + "cSeasonListIDArray[" + j + "]=\"" + id + "\";";
                        j++;

                    }

                    if (i == 0 || j == 0) {
                        out.print("No Data");

                    } else {
                        out.print(cTypeListArray + cTypeListIDArray + cSeasonListArray + cSeasonListIDArray);
                    }
//                    if (j == 0) {
//                        out.print("No Season Data");
//                    } else {
//                        out.print(cSeasonListArray + cSeasonListIDArray);
//                    }

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
            }

            if (request.getParameter("cropdata") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                String gp = Security.SecurityClass.killchar(request.getParameter("gp"));
                String year = Security.SecurityClass.killchar(request.getParameter("year"));
                String sql = "SELECT * FROM dbt_crop_details  WHERE ward_code = '" + gp + "' AND fYear = '" + year + "'";

                String cropTypeArray = "", cropNameArray = "", cropSeasonArray = "", cropAreaArray = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    cropTypeArray += "var cropTypeArray = new Array();";
                    cropNameArray += "var cropNameArray = new Array();";
                    cropSeasonArray += "var cropSeasonArray = new Array();";
                    cropAreaArray += "var cropAreaArray = new Array();";

                    int i = 1;
                    while (rset.next()) {
                        int type = rset.getInt("crop_type");
                        int name = rset.getInt("crop_name");
                        int season = rset.getInt("crop_season");
                        Double area = rset.getDouble("crop_area");

                        cropTypeArray = cropTypeArray + "cropTypeArray[" + i + "]=\"" + type + "\";";
                        cropNameArray = cropNameArray + "cropNameArray[" + i + "]=\"" + name + "\";";
                        cropSeasonArray = cropSeasonArray + "cropSeasonArray[" + i + "]=\"" + season + "\";";
                        cropAreaArray = cropAreaArray + "cropAreaArray[" + i + "]=\"" + area + "\";";
                        i++;

                    }
                    if (i == 1) {
                        out.print("No Data");

                    } else {
                        out.print(cropNameArray + cropSeasonArray + cropTypeArray + cropAreaArray);
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
            }

            if (request.getParameter("irrdata") != null) {
                Connection con = null;
                Statement smt = null;
                ResultSet rset = null;
                String gp = Security.SecurityClass.killchar(request.getParameter("gp"));
                String year = Security.SecurityClass.killchar(request.getParameter("year"));
                String sql = "SELECT * FROM dbt_irrigation_details  WHERE ward_code = '" + gp + "' AND fYear = '" + year + "'";

                String irTypeArray = "", irNameArray = "", irNoArray = "", irAreaArray = "";
                try {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                    smt = con.createStatement();
                    rset = smt.executeQuery(sql);

                    irTypeArray += "var irTypeArray = new Array();";
                    irNameArray += "var irNameArray = new Array();";
                    irNoArray += "var irNoArray = new Array();";
                    irAreaArray += "var irAreaArray = new Array();";

                    int i = 1;
                    while (rset.next()) {
                        int type = rset.getInt("irrigation_type");
                        int name = rset.getInt("irrigation_cat");
                        int no = rset.getInt("irrigation_no");
                        Double area = rset.getDouble("irrigation_area");

                        irTypeArray = irTypeArray + "irTypeArray[" + i + "]=\"" + type + "\";";
                        irNameArray = irNameArray + "irNameArray[" + i + "]=\"" + name + "\";";
                        irNoArray = irNoArray + "irNoArray[" + i + "]=\"" + no + "\";";
                        irAreaArray = irAreaArray + "irAreaArray[" + i + "]=\"" + area + "\";";
                        i++;

                    }
                    if (i == 1) {
                        out.print("No Data");

                    } else {

                        out.print(irTypeArray + irNameArray + irNoArray + irAreaArray);
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
            }

            if (request.getParameter("schemeOfficeMapping") != null) {
                /*
                //int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeList")));
                String userId = Security.SecurityClass.killchar(request.getParameter("userId"));
                //  CommonMethod cm = new CommonMethod();
                String returnData = "";
                String[][] userList = null;
                userList = cm.getMappingUserList(userId);
                String Yes = "Yes";
                String No = "No";

                if (userList != null) {
                    int len = userList.length;
                    for (int i = 0; i < len; i++) {

                        returnData += "<tr>";
                        //returnData += "<td style=\"display:none\">" + urlRoleList[i][0] + "</td>";
                        returnData += "<td>" + (i + 1) + "</td>";
                        returnData += "<td>" + userList[i][2] + "</td>";
                        returnData += "<td>" + userList[i][3] + "</td>";
                        returnData += "<td>" + userList[i][4] + "</td>";
                        returnData += "<td>" + userList[i][5] + "</td>";
                        returnData += "<td>" + userList[i][6] + "</td>";
                        returnData += "<td style=\"display:none\">" + userList[i][7] + "</td>";
                        returnData += "<td><input type=\"text\" id=\"office" + userList[i][8] + "\" value=\"" + userList[i][8] + "\"></td>";

                        //returnData += "<td><button type=\"submit\" id=\"btnEdit\" name=\"btnEdit\" class=\"btn btn-primary\"onclick=\"edit_officeList(" + officeList[i][0] + ")\">Edit</button>"
                        //  + " </td>";
                        returnData += "</tr>";
                    }
                } else {
                    returnData = "No Details Found !!!";
                }
                out.print(returnData); 
                
                 */
                int deptId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("deptId")));
                int schemeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("schemeId")));
                int workflow = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("workflow")));
                int groupId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("groupId")));

                //int roleId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("roleId")));
                Connection con = null;

                /*  String sql = "SELECT map.sl_no, map.user_id, emp_name, role_name, scheme_name, office_name, map.scheme_id, map.office_id, \n "
                        + " (SELECT office_name FROM mas_role_based_office off WHERE map.parent_office = off.office_id) AS parent_office \n"
                        + " FROM admin_user_role_scheme_office_mapping AS map \n"
                        + " INNER JOIN mas_dept_user AS u ON map.user_id = u.user_id\n"
                        //+ " INNER JOIN mas_department AS dept ON map.dept_id = dept.department_id\n"dept.department_name, 
                        + " INNER JOIN mas_admin_user_role AS  usrRole ON usrRole.role_id = map.role_id\n"
                        + " INNER JOIN master_scheme AS  scheme ON map.scheme_id = scheme.id\n"
                        + " INNER JOIN mas_role_based_office AS  off ON map.office_id = off.office_id\n"
                        + " WHERE map.scheme_id = " + schemeId;*/
                String sql = "SELECT  mapping_type ,(SELECT department_name FROM public.mas_department WHERE department_id = map.department_id) AS departmet_name,"
                        + "(SELECT scheme_name FROM master_scheme WHERE id = map.scheme_id) AS scheme_name, "
                        + "(SELECT role_name FROM bms_user.mas_user_roles WHERE role_id = map.role_id ) AS role_name,  "
                        + "(SELECT office_name FROM bms_user.mas_dept_users_group_based_office WHERE office_id = map.office_id ) AS office_name,  "
                        + "(SELECT COALESCE(department_name,'NA') AS department_name FROM public.mas_department WHERE department_id = map.parent_department_id) AS parent_departmet_name, "
                        + "(SELECT office_name FROM bms_user.mas_dept_users_group_based_office off WHERE map.parent_office_id = off.office_id) AS parent_office_name,"
                        + "  COALESCE(login_id,'') AS login_id "
                        + " FROM bms_user.map_admin_workflow AS map "
                        + " WHERE map.department_id =" + deptId + " AND map.scheme_id =" + schemeId + " AND map.mapping_type =" + workflow + " AND map.group_id =" + groupId;

                jsobj3 = new JSONObject();
                try {
                    jsarr3 = cm.getResultJSON(sql);
                    jsobj3.put("aaData", jsarr3);
                } catch (Exception ex) {
                    Logger.getLogger(AjaxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.print(jsobj3);
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
