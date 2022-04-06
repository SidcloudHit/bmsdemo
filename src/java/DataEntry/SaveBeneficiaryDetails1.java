/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataEntry;

import Common.CommonMethod;
import Security.SecurityClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author acer
 */
@WebServlet(name = "SaveBeneficiaryDetails", urlPatterns = {"/SaveBeneficiaryDetails"})
public class SaveBeneficiaryDetails1 extends HttpServlet {

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
            String txtBeneficiaryName = "", txtFathersName = "", txtDateOfBirth = "", radGender = "", radCategory = "", txtMobile = "",
                    txtUIDCardNo = "", txtRationCardNo = "", txtRationCardMem = "", ddlDistrict1 = "", ddlDistrict1Name = "", ddlSubDiv1 = "",
                    ddlSubDiv1Name = "", ddlUrbRurBody = "", ddlUrbRurBodyName = "", ddlWardGp = "", ddlWardGpName = "", txtAddrOth = "",
                    ddlPs = "", ddlPsName = "", ddlPo = "", ddlPoName = "", txtAddrPIN = "", txtBankName = "", txtAccNo = "", txtIfsCode = "",
                    query1 = "", query2 = "", message = "", schemeCode = "", schemeName = "", officeName = "", finYear = "";

            int ddlBenCategory = 1, ddlBenSubCategory = 0, edit = 0, benId = 0, addlIdRowCount = 0, schemeId = 0, instl = 0, blockId = 0, gpId = 0, officeId = 0;
            boolean head_of_family = false;

            JSONObject addlIdObj = new JSONObject();
            JSONArray addlIdArray = new JSONArray();

            if (request.getParameter("hdnEdit") != null) {
                edit = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("hdnEdit")));
            }

//            if (edit == 2) {
//                if (request.getParameter("hdnBenId") != null) {
//                    benId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("hdnBenId")));
//                }
//
//            }
            if (request.getParameter("hdnschemeId") != null) {
                schemeId = Integer.parseInt(request.getParameter("hdnschemeId"));
            }

            if (request.getParameter("hdnschemeCode") != null) {
                schemeCode = request.getParameter("hdnschemeCode");
            }

            if (request.getParameter("hdnschemeName") != null) {
                schemeName = request.getParameter("hdnschemeName");
            }
            if (request.getParameter("hdnofficeName") != null) {
                officeName = request.getParameter("hdnofficeName");
            }
            if (request.getParameter("hdnfinYear") != null) {
                finYear = request.getParameter("hdnfinYear");
            }

            if (request.getParameter("hdninstl") != null) {
                instl = Integer.parseInt(request.getParameter("hdninstl"));
            }
            if (request.getParameter("hdnblockId") != null) {
                blockId = Integer.parseInt(request.getParameter("hdnblockId"));
            }
            if (request.getParameter("hdngpId") != null) {
                gpId = Integer.parseInt(request.getParameter("hdngpId"));
            }
            if (request.getParameter("hdnofficeId") != null) {
                officeId = Integer.parseInt(request.getParameter("hdnofficeId"));
            }

            if (request.getParameter("txtBeneficiaryName") != null) {
                txtBeneficiaryName = Security.SecurityClass.killchar(request.getParameter("txtBeneficiaryName"));
            }
            if (request.getParameter("txtFathersName") != null) {
                txtFathersName = Security.SecurityClass.killchar(request.getParameter("txtFathersName"));
            }
            if (request.getParameter("txtDateOfBirth") != null) {
                txtDateOfBirth = Security.SecurityClass.killchar(request.getParameter("txtDateOfBirth"));
            }
            if (request.getParameter("radGender") != null) {
                radGender = Security.SecurityClass.killchar(request.getParameter("radGender"));
            }
            if (request.getParameter("radCategory") != null) {
                radCategory = Security.SecurityClass.killchar(request.getParameter("radCategory"));
            }
//            if (request.getParameter("txtReligion") != null) {
//                txtReligion = Security.SecurityClass.killchar(request.getParameter("txtReligion"));
//            }
            if (request.getParameter("txtMobile") != null) {
                txtMobile = Security.SecurityClass.killchar(request.getParameter("txtMobile"));
            }
//            if (request.getParameter("txtEmail") != null) {
//                txtEmail = Security.SecurityClass.killchar(request.getParameter("txtEmail"));
//            }
//            if (request.getParameter("ddlQual") != null) {
//                ddlQual = Security.SecurityClass.killchar(request.getParameter("ddlQual"));
//            }
//            if (request.getParameter("txtAnnualIncome") != null) {
//                txtAnnualIncome = Security.SecurityClass.killchar(request.getParameter("txtAnnualIncome"));
//            }
//            if (request.getParameter("txtOccupation") != null) {
//                txtOccupation = Security.SecurityClass.killchar(request.getParameter("txtOccupation"));
//            }
            if (request.getParameter("txtUIDCardNo") != null) {
                txtUIDCardNo = Security.SecurityClass.killchar(request.getParameter("txtUIDCardNo"));
            }
//            if (request.getParameter("txtPANCardNo") != null) {
//                txtPANCardNo = Security.SecurityClass.killchar(request.getParameter("txtPANCardNo"));
//            }
            if (request.getParameter("txtRationCardNo") != null) {
                txtRationCardNo = Security.SecurityClass.killchar(request.getParameter("txtRationCardNo"));
            }
            if (request.getParameter("txtRationCardMem") != null) {
                txtRationCardMem = Security.SecurityClass.killchar(request.getParameter("txtRationCardMem"));
            }
            if (request.getParameter("radHof") != null) {
                head_of_family = Boolean.parseBoolean(Security.SecurityClass.killchar(request.getParameter("radHof")));
            }
//            if (request.getParameter("txtEpicNo") != null) {
//                txtEpicNo = Security.SecurityClass.killchar(request.getParameter("txtEpicNo"));
//            }
//            if (request.getParameter("txtDrivingLicenseNo") != null) {
//                txtDrivingLicenseNo = Security.SecurityClass.killchar(request.getParameter("txtDrivingLicenseNo"));
//            }
//            if (request.getParameter("txtMNREGAJobCardNo") != null) {
//                txtMNREGAJobCardNo = Security.SecurityClass.killchar(request.getParameter("txtMNREGAJobCardNo"));
//            }
//            if (request.getParameter("txtMNREGAJobCardMem") != null) {
//                txtMNREGAJobCardMem = Security.SecurityClass.killchar(request.getParameter("txtMNREGAJobCardMem"));
//            }
//            if (request.getParameter("txtPMKISANId") != null) {
//                txtPMKISANId = Security.SecurityClass.killchar(request.getParameter("txtPMKISANId"));
//            }
//            if (request.getParameter("txtKisanCreditCardNo") != null) {
//                txtKisanCreditCardNo = Security.SecurityClass.killchar(request.getParameter("txtKisanCreditCardNo"));
//            }
//            if (request.getParameter("txtSoilHealthCardNo") != null) {
//                txtSoilHealthCardNo = Security.SecurityClass.killchar(request.getParameter("txtSoilHealthCardNo"));
//            }
//            if (request.getParameter("txtCropInsuranceNo") != null) {
//                txtCropInsuranceNo = Security.SecurityClass.killchar(request.getParameter("txtCropInsuranceNo"));
//            }
            if (request.getParameter("ddlDistrict1") != null) {
                ddlDistrict1 = Security.SecurityClass.killchar(request.getParameter("ddlDistrict1"));
            }
            if (request.getParameter("ddlSubDiv1") != null) {
                ddlSubDiv1 = Security.SecurityClass.killchar(request.getParameter("ddlSubDiv1"));
            }
//            if (request.getParameter("ddlLocType") != null) {
//                ddlLocType = Security.SecurityClass.killchar(request.getParameter("ddlLocType"));
//            }
            if (request.getParameter("ddlUrbRurBody") != null) {
                ddlUrbRurBody = Security.SecurityClass.killchar(request.getParameter("ddlUrbRurBody"));
            }
            if (request.getParameter("ddlWardGp") != null) {
                ddlWardGp = Security.SecurityClass.killchar(request.getParameter("ddlWardGp"));
            }
            if (request.getParameter("txtAddrOth") != null) {
                txtAddrOth = Security.SecurityClass.killchar(request.getParameter("txtAddrOth"));
            }
            if (request.getParameter("ddlPs") != null) {
                ddlPs = Security.SecurityClass.killchar(request.getParameter("ddlPs"));
            }
            if (request.getParameter("ddlPo") != null) {
                ddlPo = Security.SecurityClass.killchar(request.getParameter("ddlPo"));
            }
            if (request.getParameter("txtAddrPIN") != null) {
                txtAddrPIN = Security.SecurityClass.killchar(request.getParameter("txtAddrPIN"));
            }
            if (request.getParameter("txtBankName") != null) {
                txtBankName = Security.SecurityClass.killchar(request.getParameter("txtBankName"));
            }
            if (request.getParameter("txtAccNo") != null) {
                txtAccNo = Security.SecurityClass.killchar(request.getParameter("txtAccNo"));
            }
            if (request.getParameter("txtIfsCode") != null) {
                txtIfsCode = Security.SecurityClass.killchar(request.getParameter("txtIfsCode"));
            }

            /* insert function for addl_id_details */
//            int addlIdType = 0;
//            String addlIdVal = "";
//            addlIdRowCount = Integer.parseInt(request.getParameter("hdnAddlIdCount"));
//            String center_array = "";
//            //if (irriCount > 0) {
//            center_array += "[";
//            for (int j = 1; j <= addlIdRowCount; j++) {
//
//                if (!request.getParameter("ddlAddlIdType" + j).equals("")) {
//                    addlIdType = Integer.parseInt(SecurityClass.killchar(request.getParameter("ddlAddlIdType" + j)));
//                } else {
//                    addlIdType = 0;
//                }
//                if (!request.getParameter("txtAddlIdValue" + j).equals("")) {
//                    addlIdVal = SecurityClass.killchar(request.getParameter("txtAddlIdValue" + j));
//                } else {
//                    addlIdVal = "";
//                }
//
//                if ((addlIdType > 0) && (!addlIdVal.equals(""))) {
//                    if (j == 1) {
//                        center_array += "{ 'additional_id_type_id':" + addlIdType + " , 'additional_id_type_value':'" + addlIdVal + "' }";
//                    } else {
//                        center_array += ",{ 'additional_id_type_id':" + addlIdType + " , 'additional_id_type_value':'" + addlIdVal + "' }";
//                    }
//                }
//            }
//            center_array += "]";
//            //String strIrrigations = request.getParameter("center_array").toString();
//            //}
//            try {
//                addlIdArray = new JSONArray(center_array);
//                addlIdObj.put("addl_id_json_array", addlIdArray);
//            } catch (JSONException ex) {
//                ex.getMessage();
//            }
            /* end insert function for addl_id_details */
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            int idVal = 0;
            try {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                con.setAutoCommit(false);
                int i = 1;
                if (edit == 1) {
//                    query1 = "INSERT INTO dbt_beneficiary(\n"
//                            + "            beneficiary_name, father_name, dob, gender, caste, religion, mobile, email, education_qly, annual_income, occupation, aadhaar, pan, ration_card_no, \n"
//                            + "            rationcard_memberid, epic_no, dl_no, dist_code, dist_name, subdiv_code, subdiv_name, location_type, block_code, block_name, ward_code, ward_name, rev_vill_code, \n"
//                            + "            rev_vill_name, landmark, ps_code, ps_name, po_code, po_name, pin, beneficiary_category_id, beneficiary_subcategory_id, bank_name, \n"
//                            + "            bank_account, ifsc, head_of_family)\n"
//                            + "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // RETURNING beneficiary_id
                    String tabName = "beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data_temp";
                    query1 = "INSERT INTO " + tabName + "(\n"
                            + "            full_name, father_name, district, project_name, block_mc_np_adc_nonadc, gp_ward_name, gender, date_of_birth, \n"
                            + "            category_code, bank_account_number, aadhaar, scheme_name, lgd_dist_code, lgd_subdiv_code, \n"
                            + "            lgd_block_code, lgd_gp_code, scheme_code, rc_no, rc_mem, ifsc_code, tempid)\n"
                            + "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

                    ps = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS); //
                    ps.setString(i++, txtBeneficiaryName);
                    ps.setString(i++, txtFathersName);
                    ps.setString(i++, txtDateOfBirth);
                    ps.setString(i++, radGender);
                    ps.setString(i++, radCategory);
//                    ps.setString(i++, txtReligion);
                    ps.setString(i++, txtMobile);
//                    ps.setString(i++, txtEmail);
//                    ps.setString(i++, ddlQual);
//                    ps.setString(i++, txtAnnualIncome);
//                    ps.setString(i++, txtOccupation);
                    ps.setString(i++, txtUIDCardNo);
//                    ps.setString(i++, txtPANCardNo);
                    ps.setString(i++, txtRationCardNo);
                    ps.setString(i++, txtRationCardMem);
//                    ps.setString(i++, txtEpicNo);
//                    ps.setString(i++, txtDrivingLicenseNo);
                    ps.setString(i++, ddlDistrict1);
                    ps.setString(i++, ddlDistrict1Name);
                    ps.setString(i++, ddlSubDiv1);
                    ps.setString(i++, ddlSubDiv1Name);
//                    ps.setString(i++, ddlLocType);
                    ps.setString(i++, ddlUrbRurBody);
                    ps.setString(i++, ddlUrbRurBodyName);
                    ps.setString(i++, ddlWardGp);
                    ps.setString(i++, ddlWardGpName);
//                    ps.setString(i++, ddlRevVill);
//                    ps.setString(i++, ddlRevVillname);
                    ps.setString(i++, txtAddrOth);
                    ps.setString(i++, ddlPs);
                    ps.setString(i++, ddlPsName);
                    ps.setString(i++, ddlPo.split("#")[0]);
                    ps.setString(i++, ddlPoName);
                    ps.setString(i++, txtAddrPIN);
                    ps.setInt(i++, ddlBenCategory);
                    ps.setInt(i++, ddlBenSubCategory);
                    //ps.setString(i++, txtBankName);
                    ps.setString(i++, txtAccNo);
                    ps.setString(i++, txtIfsCode);
                    ps.setBoolean(i++, head_of_family);

                } else if (edit == 2) {
                    query1 = "UPDATE dbt_beneficiary\n"
                            + "   SET beneficiary_name=?, father_name=?, dob=?, gender=?,caste=?, religion=?, mobile=?, email=?, education_qly=?, annual_income=?, occupation=?, aadhaar=?, \n"
                            + "       pan=?, ration_card_no=?, rationcard_memberid=?, epic_no=?, dl_no=?, pmkisan_id=?, \n"
                            + "       dist_code=?, dist_name=?, subdiv_code=?, subdiv_name=?, location_type=?, \n"
                            + "       block_code=?, block_name=?, ward_code=?, ward_name=?, rev_vill_code=?,rev_vill_name=?, landmark=?, ps_code=?, ps_name=?, po_code=?, \n"
                            + "       po_name=?, pin=?, beneficiary_category_id=?, beneficiary_subcategory_id=?,bank_name=?, bank_account=?, ifsc=?, head_of_family=?, lastupdated = now() \n"
                            + "       \n"
                            + " WHERE beneficiary_id=? ";

                    ps = con.prepareStatement(query1);
                    ps.setString(i++, txtBeneficiaryName);
                    ps.setString(i++, txtFathersName);
                    ps.setString(i++, txtDateOfBirth);
                    ps.setString(i++, radGender);
                    ps.setString(i++, radCategory);
//                    ps.setString(i++, txtReligion);
                    ps.setString(i++, txtMobile);
//                    ps.setString(i++, txtEmail);
//                    ps.setString(i++, ddlQual);
//                    ps.setString(i++, txtAnnualIncome);
//                    ps.setString(i++, txtOccupation);
                    ps.setString(i++, txtUIDCardNo);
//                    ps.setString(i++, txtPANCardNo);
                    ps.setString(i++, txtRationCardNo);
                    ps.setString(i++, txtRationCardMem);
//                    ps.setString(i++, txtEpicNo);
//                    ps.setString(i++, txtDrivingLicenseNo);
//                    ps.setString(i++, txtPMKISANId);
                    ps.setString(i++, ddlDistrict1);
                    ps.setString(i++, ddlDistrict1Name);
                    ps.setString(i++, ddlSubDiv1);
                    ps.setString(i++, ddlSubDiv1Name);
//                    ps.setString(i++, ddlLocType);
                    ps.setString(i++, ddlUrbRurBody);
                    ps.setString(i++, ddlUrbRurBodyName);
                    ps.setString(i++, ddlWardGp);
                    ps.setString(i++, ddlWardGpName);
//                    ps.setString(i++, ddlRevVill);
//                    ps.setString(i++, ddlRevVillname);
                    ps.setString(i++, txtAddrOth);
                    ps.setString(i++, ddlPs);
                    ps.setString(i++, ddlPsName);
                    ps.setString(i++, ddlPo.split("#")[0]);
                    ps.setString(i++, ddlPoName);
                    ps.setString(i++, txtAddrPIN);
                    ps.setInt(i++, ddlBenCategory);
                    ps.setInt(i++, ddlBenSubCategory);
                    ps.setString(i++, txtBankName);
                    ps.setString(i++, txtAccNo);
                    ps.setString(i++, txtIfsCode);
                    ps.setBoolean(i++, head_of_family);
                    ps.setInt(i++, benId);
                }
                try {
                    idVal = ps.executeUpdate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (idVal > 0) {
                    i = 1;
                    if (edit == 1) {
                        rs = ps.getGeneratedKeys();
                        if (rs != null && rs.next()) {
                            benId = rs.getInt(1);
                        }
                    }

//                    int temp = saveAddlIdData(benId, addlIdObj);
//                    if (temp > 0) {
                    con.commit();
                    message = "Beneficiary Saved Successfully";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "success");
                    response.sendRedirect("DEO/beneficiaryDataUpdate.jsp?benid=" + benId);
//                    } else {
//                        message = "Error in Saving";
//                        session.setAttribute("RegMsg", message);
//                        session.setAttribute("type", "danger");
//                        response.sendRedirect("DEO/beneficiaryCheckFromRC.jsp");
//                    }

                } else {
                    message = "Error in Saving";
                    session.setAttribute("RegMsg", message);
                    session.setAttribute("type", "danger");
                    response.sendRedirect("DEO/beneficiaryCheckFromRC.jsp");
                }

            } catch (SQLException ex) {
                Logger.getLogger(SaveBeneficiaryDetails1.class.getName()).log(Level.SEVERE, null, ex);
                message = "Error in Saving";
                session.setAttribute("RegMsg", message);
                session.setAttribute("type", "danger");
                response.sendRedirect("DEO/beneficiaryCheckFromRC.jsp");
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

    public int saveAddlIdData(int benId, JSONObject addlIdObj) {
        Connection con = null;

        CallableStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        CommonMethod cm = new CommonMethod();
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String query = "";

        try {
            ps = con.prepareCall("{CALL insert_addl_id_details (?,?::json)}");
            ps.setInt(1, benId);
            ps.setObject(2, addlIdObj.toString());
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
}
