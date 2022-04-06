/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataEntry;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Nibedita
 */
@WebServlet(name = "CropDetailsSave", urlPatterns = {"/CropDetailsSave"})
public class CropDetailsSave extends HttpServlet {

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
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
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

            int edit = 0, irriCount = 0, cropCount = 0, noFemale = 0, noMale = 0, noSC = 0, noST = 0, noOther = 0,
                    noFarmer = 0, nofamily = 0, famlyCover = 0, noRoFR = 0, marginalNo = 0,
                    smallNo = 0, mediumNo = 0, largeNo = 0, noFarmFamilies = 0, labourNo = 0, shareNo = 0,
                    singleCrop = 0, doubleCrop = 0, tripleCrop = 0, kccSponsored = 0, kccSanction = 0;

            String message = "", ddlDistrict1 = "", ddlDistrict1Name = "", ddlSubDiv1 = "",
                    ddlSubDiv1Name = "", ddlLocType = "", ddlUrbRurBody = "", ddlUrbRurBodyName = "", ddlWardGp = "", ddlWardGpName = "", fYear = "";

            Double marginalArea = 0.0, smallArea = 0.0, mediumArea = 0.0, largeArea = 0.0,
                    forestArea = 0.0, sownArea = 0.0, wasteLand = 0.0, nonagriarea = 0.0, barrenarea = 0.0,
                    fallowland = 0.0, grazingland = 0.0, cropping_intensity = 0.0, net_irrigated_area = 0.0, amtDisbursed = 0.0;

            boolean draft = false, finalSubmit = false;

            if (request.getParameter("btnSubmitDraft") != null) {
                draft = true;
            } else if (request.getParameter("btnSubmit") != null) {
                finalSubmit = true;
            }

            JSONObject json_irri_obj = new JSONObject();
            JSONArray json_irri_array = new JSONArray();
            JSONObject json_crop_obj = new JSONObject();
            JSONArray json_crop_array = new JSONArray();

            edit = Integer.parseInt(request.getParameter("hdnEdit"));
            irriCount = Integer.parseInt(request.getParameter("hdnIrriCount"));
            cropCount = Integer.parseInt(request.getParameter("hdnCropCount"));

            if (request.getParameter("ddlDistrict1") != null) {
                ddlDistrict1 = Security.SecurityClass.killchar(request.getParameter("ddlDistrict1"));
                ddlDistrict1Name = Security.SecurityClass.killchar(request.getParameter("ddlDistrict1Name"));
                if (finalSubmit && ddlDistrict1.equals("")) {
                    message = "District Cannot be Blank";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                    return;
                }
            }
            if (request.getParameter("ddlSubDiv1") != null) {
                ddlSubDiv1 = Security.SecurityClass.killchar(request.getParameter("ddlSubDiv1"));
                ddlSubDiv1Name = Security.SecurityClass.killchar(request.getParameter("ddlSubDiv1Name"));
                if (finalSubmit && (ddlSubDiv1.equals("") || ddlSubDiv1.equals("-1") || ddlSubDiv1.equals("0"))) {
                    message = "Agri Sub-Division Cannot be Blank";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                    return;
                }
            }
            if (request.getParameter("ddlLocType") != null) {
                ddlLocType = Security.SecurityClass.killchar(request.getParameter("ddlLocType"));
            }
            if (request.getParameter("ddlUrbRurBody") != null) {
                ddlUrbRurBody = Security.SecurityClass.killchar(request.getParameter("ddlUrbRurBody"));
                ddlUrbRurBodyName = Security.SecurityClass.killchar(request.getParameter("ddlUrbRurBodyName"));
            }
            if (request.getParameter("ddlWardGp") != null) {
                ddlWardGp = Security.SecurityClass.killchar(request.getParameter("ddlWardGp"));
                if (ddlWardGp.equals("0") || ddlWardGp.equals("-1") || ddlWardGp.equals("")) {
                    message = "Ward/GP/VC Cannot be Blank";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                    return;
                }
                ddlWardGpName = Security.SecurityClass.killchar(request.getParameter("ddlWardGpName"));
            } else {
                message = "Ward/GP/VC Cannot be Blank";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                return;
            }

            if (request.getParameter("ddlFYear") != null) {
                fYear = SecurityClass.killchar(request.getParameter("ddlFYear"));
                if (fYear.equals("0") || ddlWardGp.equals("")) {
                    message = "Financial Year Cannot be Blank";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                    return;
                }
            } else {
                message = "Financial Year Cannot be Blank";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                return;
            }
            if (request.getParameter("txtMale") != null) {
                if ((request.getParameter("txtMale").trim().length() > 0) && cm.isNumeric(request.getParameter("txtMale"))) {
                    noMale = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtMale")));
                    if (finalSubmit && (noMale == 0)) {
                        message = "Number of Male Cannot be 0";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                        return;
                    }
                }
            }
            if (request.getParameter("txtFemale") != null) {
                if ((request.getParameter("txtFemale").trim().length() > 0) && cm.isNumeric(request.getParameter("txtFemale"))) {
                    noFemale = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtFemale")));
                    if (finalSubmit && (noFemale == 0)) {
                        message = "Number of Female Cannot be 0";
                        session.setAttribute("RegMsg", message);
                        session.setAttribute("type", "danger");
                        response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                        return;
                    }
                }
            }

            if (request.getParameter("txtSC") != null) {
                if (request.getParameter("txtSC").trim().length() > 0) {
                    noSC = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtSC")));
                }
            }
            if (request.getParameter("txtST") != null) {
                if (request.getParameter("txtST").trim().length() > 0) {
                    noST = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtST")));
                }
            }
            if (request.getParameter("txtOther") != null) {
                if (request.getParameter("txtOther").trim().length() > 0) {
                    noOther = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtOther")));
                }
            }
            if (request.getParameter("txtNoFarmer") != null) {
                if (request.getParameter("txtNoFarmer").trim().length() > 0) {
                    noFarmer = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtNoFarmer")));
                }
            }
            if (request.getParameter("txtPattaHolder") != null) {
                if (request.getParameter("txtPattaHolder").trim().length() > 0) {
                    nofamily = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtPattaHolder")));
                }
            }
            if (request.getParameter("txtKCC") != null) {
                if (request.getParameter("txtKCC").trim().length() > 0) {
                    kccSponsored = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtKCC")));
                }
            }
            if (request.getParameter("txtKCCSanc") != null) {
                if (request.getParameter("txtKCCSanc").trim().length() > 0) {
                    kccSanction = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtKCCSanc")));
                }
            }
            if (request.getParameter("txtRoFR") != null) {
                if (request.getParameter("txtRoFR").trim().length() > 0) {
                    famlyCover = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtRoFR")));
                }
            }
            if (request.getParameter("txtAmtDisbursed") != null) {
                if (request.getParameter("txtAmtDisbursed").trim().length() > 0) {
                    amtDisbursed = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtAmtDisbursed")));
                }
            }
            if (request.getParameter("txtMarginalNo") != null) {
                if (request.getParameter("txtMarginalNo").trim().length() > 0) {
                    marginalNo = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtMarginalNo")));
                }
            }
            if (request.getParameter("txtMarginalArea") != null) {
                if (request.getParameter("txtMarginalArea").trim().length() > 0) {
                    marginalArea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtMarginalArea")));
                }
            }
            if (request.getParameter("txtSmallNo") != null) {
                if (request.getParameter("txtSmallNo").trim().length() > 0) {
                    smallNo = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtSmallNo")));
                }
            }
            if (request.getParameter("txtSmallArea") != null) {
                if (request.getParameter("txtSmallArea").trim().length() > 0) {
                    smallArea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtSmallArea")));
                }
            }
            if (request.getParameter("txtMediumNo") != null) {
                if (request.getParameter("txtMediumNo").trim().length() > 0) {
                    mediumNo = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtMediumNo")));
                }
            }
            if (request.getParameter("txtMediumArea") != null) {
                if (request.getParameter("txtMediumArea").trim().length() > 0) {
                    mediumArea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtMediumArea")));
                }
            }
            if (request.getParameter("txtLargeNo") != null) {
                if (request.getParameter("txtLargeNo").trim().length() > 0) {
                    largeNo = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtLargeNo")));
                }
            }
            if (request.getParameter("txtLargeArea") != null) {
                if (request.getParameter("txtLargeArea").trim().length() > 0) {
                    largeArea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtLargeArea")));
                }
            }
            if (request.getParameter("txtNoFarmFamilies") != null) {
                if (request.getParameter("txtNoFarmFamilies").trim().length() > 0) {
                    noFarmFamilies = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtNoFarmFamilies")));
                }
            }
            if (request.getParameter("txtAgriLabourNo") != null) {
                if (request.getParameter("txtAgriLabourNo").trim().length() > 0) {
                    labourNo = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtAgriLabourNo")));
                }
            }
            if (request.getParameter("txtShareNo") != null) {
                if (request.getParameter("txtShareNo").trim().length() > 0) {
                    shareNo = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtShareNo")));
                }
            }
            if (request.getParameter("txtForestArea") != null) {
                if (request.getParameter("txtForestArea").trim().length() > 0) {
                    forestArea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtForestArea")));
                }
            }
            if (request.getParameter("txtSownArea") != null) {
                if (request.getParameter("txtSownArea").trim().length() > 0) {
                    sownArea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtSownArea")));
                }
            }
            if (request.getParameter("txtSingleCrop") != null) {
                if (request.getParameter("txtSingleCrop").trim().length() > 0) {
                    singleCrop = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtSingleCrop")));
                }
            }
            if (request.getParameter("txtDoubleCrop") != null) {
                if (request.getParameter("txtDoubleCrop").trim().length() > 0) {
                    doubleCrop = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtDoubleCrop")));
                }
            }
            if (request.getParameter("txtTripleCrop") != null) {
                if (request.getParameter("txtTripleCrop").trim().length() > 0) {
                    tripleCrop = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtTripleCrop")));
                }
            }
            if (request.getParameter("txtWasteLand") != null) {
                if (request.getParameter("txtWasteLand").trim().length() > 0) {
                    wasteLand = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtWasteLand")));
                }
            }
            if (request.getParameter("txtNonAgri") != null) {
                if (request.getParameter("txtNonAgri").trim().length() > 0) {
                    nonagriarea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtNonAgri")));
                }
            }
            if (request.getParameter("txtBarren") != null) {
                if (request.getParameter("txtBarren").trim().length() > 0) {
                    barrenarea = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtBarren")));
                }
            }
            if (request.getParameter("txtFallowLand") != null) {
                if (request.getParameter("txtFallowLand").trim().length() > 0) {
                    fallowland = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtFallowLand")));
                }
            }
            if (request.getParameter("txtGazingLand") != null) {
                if (request.getParameter("txtGazingLand").trim().length() > 0) {
                    grazingland = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtGazingLand")));
                }
            }

            if (request.getParameter("txtCroppingIntensity") != null) {
                if (request.getParameter("txtCroppingIntensity").trim().length() > 0) {
                    cropping_intensity = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtCroppingIntensity")));
                }
            }

            if (request.getParameter("txtNetIrrigatedArea") != null) {
                if (request.getParameter("txtNetIrrigatedArea").trim().length() > 0) {
                    net_irrigated_area = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtNetIrrigatedArea")));
                }
            }

            /* insert function for irrigation_details */
            int irri_type = 0, irri_cat = 0, irri_no = 0;
            Double irri_area = 0.0;

            String center_array = "";
            //if (irriCount > 0) {
            center_array += "[";
            for (int j = 1; j <= irriCount; j++) {
                if (!request.getParameter("ddlIrrigationType" + j).equals("0") && !request.getParameter("ddlIrrigationType" + j).equals("")) {
                    irri_type = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlIrrigationType" + j)));
                } else {
                    irri_type = 0;
                }
                if (!request.getParameter("ddlIrrigationCat" + j).equals("")) {
                    irri_cat = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlIrrigationCat" + j)));
                } else {
                    irri_cat = 0;
                }
                if (!request.getParameter("txtIrrigationNo" + j).equals("") && cm.isNumeric(request.getParameter("txtIrrigationNo" + j))) {
                    irri_no = Integer.parseInt(SecurityClass.killchar(request.getParameter("txtIrrigationNo" + j)));
                } else {
                    irri_no = 0;
                }
                if (!request.getParameter("txtcommandArea" + j).equals("") && cm.isNumeric(request.getParameter("txtcommandArea" + j))) {
                    irri_area = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtcommandArea" + j)));
                } else {
                    irri_area = 0.0;
                }

                if ((irri_type > 0) && (irri_cat > 0) && (irri_no > 0) && (irri_area > 0)) {
                    if (j == 1) {
                        center_array += "{ 'irri_type':" + irri_type + " , 'irri_cat':" + irri_cat + " , 'irri_no':" + irri_no + " , 'irri_area':" + irri_area + "  }";
                    } else {
                        center_array += ",{ 'irri_type':" + irri_type + " , 'irri_cat':" + irri_cat + " , 'irri_no':" + irri_no + " , 'irri_area':" + irri_area + "  }";
                    }
                }
            }
            center_array += "]";
            //String strIrrigations = request.getParameter("center_array").toString();
            //}
            try {
                json_irri_array = new JSONArray(center_array);
                json_irri_obj.put("irri_json_array", json_irri_array);
            } catch (JSONException ex) {
                ex.getMessage();
            }
            /* end insert function for irrigation_details */

 /* insert function for crop_details */
            int crop_type = 0, crop_name = 0, crop_season = 0;
            Double crop_area = 0.0;

            String crop_array = "";
            //if (cropCount > 0) {
            crop_array = "[";
            for (int j = 1; j <= cropCount; j++) {
                if (!request.getParameter("ddlCropType" + j).equals("")) {
                    crop_type = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlCropType" + j)));
                } else {
                    crop_type = 0;
                }
                if (!request.getParameter("ddlCropName" + j).equals("") && !request.getParameter("ddlCropName" + j).equals("0") && !request.getParameter("ddlCropName" + j).equals("-1")) {
                    crop_name = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlCropName" + j)));
                } else {
                    crop_name = 0;
                }
                if (!request.getParameter("ddlCropSeason" + j).equals("")) {
                    crop_season = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlCropSeason" + j)));
                } else {
                    crop_season = 0;
                }
                if (!request.getParameter("txtCropArea" + j).equals("") && cm.isNumeric(request.getParameter("txtCropArea" + j))) {
                    crop_area = Double.parseDouble(SecurityClass.killchar(request.getParameter("txtCropArea" + j)));
                } else {
                    crop_area = 0.0;
                }

                if ((crop_type > 0) && (crop_name > 0) && (crop_season > 0) && (crop_area > 0)) {
                    if (j == 1) {
                        crop_array += "{ 'crop_type':" + crop_type + " , 'crop_name':" + crop_name + " , 'crop_season':" + crop_season + " , 'crop_area':" + crop_area + "  }";
                    } else {
                        crop_array += ",{ 'crop_type':" + crop_type + " , 'crop_name':" + crop_name + " , 'crop_season':" + crop_season + " , 'crop_area':" + crop_area + "  }";
                    }
                }
            }
            crop_array += "]";
            //String strIrrigations = request.getParameter("center_array").toString();
            // }
            try {
                json_crop_array = new JSONArray(crop_array);
                json_crop_obj.put("crop_json_array", json_crop_array);
            } catch (JSONException ex) {
                ex.getMessage();
            }

            /* end insert function for irrigation_details */
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            String query = "";
            String query1 = "";

            try {

                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                //con.setAutoCommit(false);
                if (edit == 0 || edit == 2) {
                    query = "INSERT INTO agri_basic_report(\n"
                            + "dist_code, dist_name, subdiv_code, subdiv_name, location_type, \n"
                            + "block_code, block_name, ward_code, ward_name, fyear, nofemale, nomale, nosc, nost, noother, \n"
                            + "nofarmer, nofamily, kccsponsored, kccsanction, famlycover, amountdisbursed, \n"
                            + "marginalfarmerno, marginalfarmerarea, smallfarmerno, smallfarmerarea, \n"
                            + "mediumfarmerno, mediumfarmerarea, largefarmerno, largefarmerarea, \n"
                            + "farmfamilyno, noagrilabour, sharecroppers, forestarea, sownarea, \n"
                            + "singlecrop, doublecrop, triplecrop, wasteland, nonagriarea, barrenarea, \n"
                            + "fallowland, grazingland, cropping_intensity,net_irrigated_area, entry_by, final_stat, final_time )\n"
                            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \n"
                            + "?, ?, ?, ?, ?, ?, now());";

                    int i = 1;

                    ps = con.prepareStatement(query);
                    ps.setString(i++, ddlDistrict1);
                    ps.setString(i++, ddlDistrict1Name);
                    ps.setString(i++, ddlSubDiv1);
                    ps.setString(i++, ddlSubDiv1Name);
                    ps.setString(i++, ddlLocType);
                    ps.setString(i++, ddlUrbRurBody);
                    ps.setString(i++, ddlUrbRurBodyName);
                    ps.setString(i++, ddlWardGp);
                    ps.setString(i++, ddlWardGpName);
                    ps.setString(i++, fYear);
                    ps.setInt(i++, noFemale);
                    ps.setInt(i++, noMale);
                    ps.setInt(i++, noSC);
                    ps.setInt(i++, noST);
                    ps.setInt(i++, noOther);
                    ps.setInt(i++, noFarmer);
                    ps.setInt(i++, nofamily);
                    ps.setInt(i++, kccSponsored);
                    ps.setInt(i++, kccSanction);
                    ps.setInt(i++, famlyCover);
                    ps.setDouble(i++, amtDisbursed);
                    ps.setInt(i++, marginalNo);
                    ps.setDouble(i++, marginalArea);
                    ps.setInt(i++, smallNo);
                    ps.setDouble(i++, smallArea);
                    ps.setInt(i++, mediumNo);
                    ps.setDouble(i++, mediumArea);
                    ps.setInt(i++, largeNo);
                    ps.setDouble(i++, largeArea);
                    ps.setInt(i++, noFarmFamilies);
                    ps.setInt(i++, labourNo);
                    ps.setInt(i++, shareNo);
                    ps.setDouble(i++, forestArea);
                    ps.setDouble(i++, sownArea);
                    ps.setInt(i++, singleCrop);
                    ps.setInt(i++, doubleCrop);
                    ps.setInt(i++, tripleCrop);
                    ps.setDouble(i++, wasteLand);
                    ps.setDouble(i++, nonagriarea);
                    ps.setDouble(i++, barrenarea);
                    ps.setDouble(i++, fallowland);
                    ps.setDouble(i++, grazingland);
                    ps.setDouble(i++, cropping_intensity);
                    ps.setDouble(i++, net_irrigated_area);
                    ps.setString(i++, userId);
                    ps.setBoolean(i++, finalSubmit);

                    if (irriCount > 0) {
                        int result = saveData(ddlWardGp, fYear, json_irri_obj);
                    }
                    if (cropCount > 0) {
                        int result1 = saveCropData(ddlWardGp, fYear, json_crop_obj);
                    }
                } else if (edit == 1) {
                    query = "UPDATE agri_basic_report\n"
                            + " SET nofemale=?, nomale=?, nosc=?, \n"
                            + " nost=?, noother=?, nofarmer=?, nofamily=?, kccsponsored=?, kccsanction=?, \n"
                            + " famlycover=?, amountdisbursed=?, marginalfarmerno=?, marginalfarmerarea=?, \n"
                            + " smallfarmerno=?, smallfarmerarea=?, mediumfarmerno=?, mediumfarmerarea=?, \n"
                            + " largefarmerno=?, largefarmerarea=?, farmfamilyno=?, noagrilabour=?, \n"
                            + " sharecroppers=?, forestarea=?, sownarea=?, singlecrop=?, doublecrop=?, \n"
                            + " triplecrop=?, wasteland=?, nonagriarea=?, barrenarea=?, fallowland=?, grazingland=? ,"
                            + " cropping_intensity=?, net_irrigated_area=?, final_stat=?, final_time = now(), last_update_time = now() \n"
                            + " WHERE ward_code=? AND fyear=?";

                    int i = 1;

                    ps = con.prepareStatement(query);
                    ps.setInt(i++, noFemale);
                    ps.setInt(i++, noMale);
                    ps.setInt(i++, noSC);
                    ps.setInt(i++, noST);
                    ps.setInt(i++, noOther);
                    ps.setInt(i++, noFarmer);
                    ps.setInt(i++, nofamily);
                    ps.setInt(i++, kccSponsored);
                    ps.setInt(i++, kccSanction);
                    ps.setInt(i++, famlyCover);
                    ps.setDouble(i++, amtDisbursed);
                    ps.setInt(i++, marginalNo);
                    ps.setDouble(i++, marginalArea);
                    ps.setInt(i++, smallNo);
                    ps.setDouble(i++, smallArea);
                    ps.setInt(i++, mediumNo);
                    ps.setDouble(i++, mediumArea);
                    ps.setInt(i++, largeNo);
                    ps.setDouble(i++, largeArea);
                    ps.setInt(i++, noFarmFamilies);
                    ps.setInt(i++, labourNo);
                    ps.setInt(i++, shareNo);
                    ps.setDouble(i++, forestArea);
                    ps.setDouble(i++, sownArea);
                    ps.setInt(i++, singleCrop);
                    ps.setInt(i++, doubleCrop);
                    ps.setInt(i++, tripleCrop);
                    ps.setDouble(i++, wasteLand);
                    ps.setDouble(i++, nonagriarea);
                    ps.setDouble(i++, barrenarea);
                    ps.setDouble(i++, fallowland);
                    ps.setDouble(i++, grazingland);
                    ps.setDouble(i++, cropping_intensity);
                    ps.setDouble(i++, net_irrigated_area);
                    ps.setBoolean(i++, finalSubmit);
                    ps.setString(i++, ddlWardGp);
                    ps.setString(i++, fYear);

                    if (irriCount > 0) {
                        int result = saveData(ddlWardGp, fYear, json_irri_obj);
                    }
                    if (cropCount > 0) {
                        int result1 = saveCropData(ddlWardGp, fYear, json_crop_obj);
                    }
                }

                if (ps.executeUpdate() > 0) {
                    //con.commit();
                    if (draft) {
                        message = "Information Saved Successfully as Draft";
                    } else if (finalSubmit) {
                        message = "Information Submitted Successfully";
                    }

                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("DEO/agriBasicReportEntry.jsp?gpId=" + ddlWardGp + "&fYear=" + fYear);
                    //return;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(CropDetailsSave.class.getName()).log(Level.SEVERE, null, ex);
                message = "Error in saving";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("DEO/agriBasicReportEntry.jsp");
                //return;
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

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(CropDetailsSave.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public int saveData(String ddlWardGp, String fYear, JSONObject jsonObjIrrigation) {
        Connection con = null;

        CallableStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        CommonMethod cm = new CommonMethod();
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String query = "";

        try {
            ps = con.prepareCall("{CALL irrigation_details (?,?,?::json)}");
            ps.setString(1, ddlWardGp);
            ps.setString(2, fYear);
            ps.setObject(3, jsonObjIrrigation.toString());
            result = ps.executeUpdate();

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

    public int saveCropData(String ddlWardGp, String fYear, JSONObject jsonObjCrop) {
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        CommonMethod cm = new CommonMethod();
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String query = "";

        try {
            ps = con.prepareCall("{CALL crop_details (?,?,?::json)}");
            ps.setString(1, ddlWardGp);
            ps.setString(2, fYear);
            ps.setObject(3, jsonObjCrop.toString());
            result = ps.executeUpdate();

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
