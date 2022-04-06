/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esign;

import Common.CommonMethod;
import Master.Data.AdminUser;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdk.nashorn.api.scripting.JSObject;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "GeneratePDF", urlPatterns = {"/GeneratePDF"})
public class GeneratePDF extends HttpServlet {

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
            Common.CommonMethod cm = new CommonMethod();
            HttpSession session = request.getSession();
            Connection con = null;
            int deptId = 0, officeId = 0, schemeId = 0, instl = 0, status = 0, checkerOfficeId = 0, roleId = 0, workflow = 2, direct = 0, applId = 0;
            String schemeCode = "", schemeName = "", finYr = "", instlName = "", sql = "", userId = "", message = "", tabName = "", userflag = "", userName = "";
            PreparedStatement ps = null;
            ResultSet res = null;
            byte[] gen_pdf = null;
            String encodedBase64 = "";
            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();
                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                roleId = Integer.parseInt(loginObj.getUserRole());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
            }

            if (userId.equals("")) {
                message = "Session time-out, please login again.";
                request.setAttribute("LogMsg", message);
                getServletContext().getRequestDispatcher("/Logout").forward(request, response);
                return;
            }
            if (request.getParameter("argument_type") != null) {
                String argument_type = request.getParameter("argument_type").toString();

                if (argument_type.equals("freezeBenList")) {
                    int type = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("argument")));

                    if (request.getParameter("schemeId") != null) {
                        String scheme = request.getParameter("schemeId");
                        schemeId = Integer.parseInt(scheme.split("~")[0]);
                        schemeCode = scheme.split("~")[1];
                        schemeName = cm.getSchemeName(schemeId);
                    }

//                    if (request.getParameter("officeId") != null && !request.getParameter("officeId").equals("") && !request.getParameter("officeId").equals("-1")) {
//                        officeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("officeId")));
//                    }
                    if (request.getParameter("finYr") != null && !request.getParameter("finYr").equals("")) {
                        finYr = Security.SecurityClass.killchar(request.getParameter("finYr"));
                    }
                    if (request.getParameter("instl") != null && !request.getParameter("instl").equals("") && !request.getParameter("instl").equals("-1")) {
                        instl = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("instl")));
                        instlName = request.getParameter("instlName");
                    }
                    if (request.getParameter("status") != null && !request.getParameter("status").equals("")) {
                        status = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("status")));
                    }
                    if (request.getParameter("userflag") != null && !request.getParameter("userflag").equals("")) {
                        userflag = Security.SecurityClass.killchar(request.getParameter("userflag"));
                    }
                    if (request.getParameter("checkerofficeid") != null && !request.getParameter("checkerofficeid").equals("") && !request.getParameter("checkerofficeid").equals("-1")) {
                        checkerOfficeId = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("checkerofficeid")));
                    }

                    JSONArray jsarr1 = new JSONArray();
                    String benidsJsonData = request.getParameter("selectedbenid");
                    int j = 0;
                    String result = "";
                    try {
                        jsarr1 = new JSONArray(benidsJsonData);

                        for (int n = 0; n < jsarr1.length(); n++) {
                            JSONObject jsobj1 = jsarr1.getJSONObject(n);
                            if (n == 0) {
                                result += "'" + jsobj1.getString("rc_mem") + "'";
                            } else {
                                result += "," + "'" + jsobj1.getString("rc_mem") + "'";
                            }
                            j++;
                        }

                    } catch (JSONException ex) {
                        ex.getMessage();
                    }

                    if (type == 1) {
                        tabName = "transaction.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_tran_dbt_cash_" + finYr.replace("-", "_");

                        sql = "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                                + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, c.amount, b.office_name   "
                                + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                + "INNER JOIN " + tabName + " c ON a.rc_mem = c.beneficiary_id "
                                + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND a.source_code <> 3 ";
                        //AND no_land_allowed = " + noLand + " 

                        if (result.length() > 1) {
                            sql += "AND a.rc_mem IN  (" + result + ") ";
                        }

                        if (userflag.equals("initiator") && officeId > 0) {
                            sql += "AND a.office_id = " + officeId + " ";
                        } else if (userflag.equals("verifier") && officeId > 0) {
                            String childOfficeList = "";
                            childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
                        } else if (userflag.equals("approver") && checkerOfficeId > 0) {
                            String childOfficeList = "";
                            childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                            if (childOfficeList.length() > 1) {
                                sql += "AND a.office_id IN (" + childOfficeList + ") ";
                            }
                        }
                        if (status > 0) {
                            sql += "AND c.status = " + status + " ";
                        }
                        sql += " UNION ";
                        sql += "SELECT a.rc_mem,1 AS ben_type, a.full_name, COALESCE(a.father_husband_name,'') AS guardian_name, '' AS mobile, " //COALESCE(b.mobile_no,'')
                                + "COALESCE(a.bank_account_number,'') AS bank_account, COALESCE(a.ifsc_code,'') AS ifsc, " + schemeId + " AS scheme_id, a.scheme_code, "
                                + "COALESCE(a.scheme_name,'') AS scheme_name, '' AS bank_name, c.amount, b.office_name   "
                                + "FROM beneficiary.beneficiary_" + schemeId + "_" + schemeCode.toLowerCase() + "_data a   " //INNER JOIN rc_member b ON a.rc_mem = b.member_id 
                                + "INNER JOIN bms_user.mas_dept_users_group_based_office b ON a.office_id = b.office_id "
                                + "INNER JOIN " + tabName + " c ON a.rc_mem = c.beneficiary_id "
                                + "WHERE c.scheme_id = " + schemeId + " AND  financial_year = '" + finYr + "' AND  installment = " + instl + " AND a.source_code = 3 ";
                        //AND no_land_allowed = " + noLand + " 

                        if (result.length() > 1) {
                            sql += "AND a.rc_mem IN  (" + result + ") ";
                        }

                        if (userflag.equals("initiator") && officeId > 0) {
                            sql += "AND a.office_id = " + officeId + " ";
                        } else if (userflag.equals("verifier") && officeId > 0) {
                            String childOfficeList = "";
                            childOfficeList = cm.getChildOfficeList(schemeId, workflow, officeId, 1);
                            sql += "AND a.office_id IN (" + childOfficeList + ") ";
                        } else if (userflag.equals("approver") && checkerOfficeId > 0) {
//                            String childOfficeList = "";
//                            childOfficeList = cm.getChildOfficeList(schemeId, workflow, checkerOfficeId, 1);
                            sql += "AND a.office_id = " + checkerOfficeId + " ";//IN (" + childOfficeList + ") ";
                        }
                        if (status > 0) {
                            sql += "AND c.status = " + status + " ";
                        }

                        sql += "ORDER BY rc_mem "; //LIMIT 500

                        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                        int finalBeneficiaryCount = 0;
                        double finalDBTAmount = 0;
                        JSONArray jsarr = null;

                        try {
                            jsarr = cm.getResultJSON(sql, con);
                            finalBeneficiaryCount = jsarr.length();
                            //jsobj.put("aaData", jsarr);
                        } catch (Exception ex) {
                            Logger.getLogger(GeneratePDF.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //generating pdf file
                        gen_pdf = gen_pdf(deptId, userName, officeId, schemeName, finYr, instlName, finalBeneficiaryCount, userId, jsarr);

                        //uploading file to esign files DB
                        //InputStream targetStream = new ByteArrayInputStream(gen_pdf);
                        encodedBase64 = new String(Base64.encodeBase64(gen_pdf));
                        String fileFreezedPDFName = schemeCode.toLowerCase() + finYr.replace("-", "_") + "_" + String.valueOf(instl) + "_" + String.valueOf(officeId) + ".pdf";
                        long fileFreezedPDFSize = encodedBase64.getBytes().length;
                        String fileFreezedPDFType = "application/pdf";
                        int fileFreezedPDFFid = cm.uploadEsignFile(encodedBase64, userId, fileFreezedPDFName, fileFreezedPDFSize, fileFreezedPDFType);

                        //changed by arindam start
                        String[] benids = null;
                        int i = 0;
                        if (userflag.equals("approver")) {
                            JSONObject jsobj = new JSONObject();
                            benids = new String[jsarr.length()];
                            for (int n = 0; n < jsarr.length(); n++) {
                                jsobj = jsarr.getJSONObject(n);

                                benids[i] = jsobj.getString("rc_mem");
                                finalDBTAmount += jsobj.getDouble("amount");

                                i++;
                            }
                        }

                        //creating esign application 
                        EsignMethods esign = new EsignMethods();

                        //esign.updateTranEsignApplId(tabName, schemeId, finYr, instl, applId, uId);
                        if (userflag.equals("approver")) {
                            applId = esign.saveEsignApplicationInitiation(checkerOfficeId, schemeId, finYr, instl, finalBeneficiaryCount, fileFreezedPDFFid, userId, roleId, finalDBTAmount, officeId);
                            if (request.getParameter("direct").equals("") && request.getParameter("direct") != null) {
                                direct = Integer.parseInt(Security.SecurityClass.killchar(request.getParameter("direct")));
                            }
                            if (direct == 0) {
                                esign.updateTranEsignApplId(applId, userId, roleId, benids);
                            } else if (direct == 1) {
                                esign.updateDirectTranEsignApplId(applId, userId, roleId, benids);
                            }
                        } else {
                            applId = esign.saveEsignApplicationInitiation(officeId, schemeId, finYr, instl, finalBeneficiaryCount, fileFreezedPDFFid, userId, roleId, finalDBTAmount, officeId);
                            esign.updateTranEsignApplId(applId, userId, roleId, benids);
                        }

                        //changed by arindam end
                        session.setAttribute("esignApplId", applId);
//                        ServletOutputStream os = response.getOutputStream();
//                        response.setContentType("application/pdf");
//                        os.write(gen_pdf);
//                        os.flush();
//                        os.close();
                        out.print(fileFreezedPDFFid);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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

    private static final long serialVersionUID = 1L;

    public byte[] gen_pdf(int deptId, String userName, int office_id, String scheme_name, String financial_year, String instal_name, int final_beneficiary_count, String uId, JSONArray benDetails) throws DocumentException {
        String output = "";
        String officeName = "", deptName = "", generation_date = "";
        CommonMethod cm = new CommonMethod();
        officeName = cm.getOfficeName(office_id);
//        AdminUser usr = new AdminUser(uId);

        deptName = cm.getDepartmentName(deptId);

        byte[] pdf = null;
        //get the output stream for writing binary data in the response.
        //create some special styles and font sizes
        Font bfBold14 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, new BaseColor(0, 0, 0));
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bfBold11 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, new BaseColor(0, 0, 0));
        Font bfBold9 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf9 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, new BaseColor(0, 0, 0));

        //bfBold11.setColor(BaseColor.BLUE);
        Font bf16 = new Font(Font.FontFamily.TIMES_ROMAN, 16);
        Font bold10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        BASE64Decoder decoder = new BASE64Decoder();

        try {

            Document doc = new Document();
            doc.setPageSize(PageSize.A4);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            doc.add(new Paragraph(" "));
            Paragraph p1 = new Paragraph("GOVERNMENT OF TRIPURA", bfBold14);
            p1.setAlignment(Element.ALIGN_CENTER);
            doc.add(p1);

            Paragraph p2 = new Paragraph("Beneficiary Management System", bfBold12);
            p2.setAlignment(Element.ALIGN_CENTER);
            doc.add(p2);

            Paragraph p3 = new Paragraph(deptName + " - " + officeName, bfBold12);
            p3.setAlignment(Element.ALIGN_CENTER);
            doc.add(p3);

            Paragraph p4 = new Paragraph("Final Beneficiary List of " + scheme_name + " for FY - " + financial_year + " Installment " + instal_name + "", bfBold11);
            p4.setAlignment(Element.ALIGN_CENTER);
            doc.add(p4);

            Paragraph p5 = new Paragraph("Total Beneficiary Count " + final_beneficiary_count + "", bfBold9);
            p5.setAlignment(Element.ALIGN_CENTER);
            doc.add(p5);

            doc.add(new Paragraph(" "));
            Paragraph subTitle = new Paragraph("");

            doc.add(new LineSeparator(1, 100, BaseColor.BLACK, 0, serialVersionUID));
            doc.add(new Paragraph(" "));

//Table Header
            PdfPTable table1 = new PdfPTable(7);
            table1.setWidthPercentage(100);
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(new Phrase("Sl. No.", bfBold9));
            table1.addCell(new Phrase("Beneficiary ID", bfBold9));
            table1.addCell(new Phrase("Beneficiary Name", bfBold9));
            table1.addCell(new Phrase("Guardian's Name", bfBold9));
            table1.addCell(new Phrase("Bank Account Number", bfBold9));
            table1.addCell(new Phrase("IFSC", bfBold9));
            table1.addCell(new Phrase("DBT Amount", bfBold9));
            table1.setHeaderRows(1);
            PdfPCell[] cells = table1.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {

                cells[j].setBackgroundColor(BaseColor.LIGHT_GRAY);
                cells[j].setBorderColorTop(BaseColor.BLACK);
                cells[j].setBorderColorLeft(BaseColor.BLACK);
            }
            JSONObject jsobj = new JSONObject();
            for (int n = 0; n < benDetails.length(); n++) {
                jsobj = benDetails.getJSONObject(n);
                table1.addCell(new Phrase(Integer.toString(n + 1), bf9));
                table1.addCell(new Phrase(jsobj.getString("rc_mem"), bf9));
                table1.addCell(new Phrase(jsobj.getString("full_name"), bf9));
                table1.addCell(new Phrase(jsobj.getString("guardian_name"), bf9));
                table1.addCell(new Phrase(jsobj.getString("bank_account"), bf9));
                table1.addCell(new Phrase(jsobj.getString("ifsc"), bf9));
                table1.addCell(new Phrase(String.valueOf(jsobj.getInt("amount")), bf9));

            }

            doc.add(table1);

            doc.add(new Paragraph(" "));
            doc.add(new LineSeparator(1, 100, BaseColor.BLACK, 0, serialVersionUID));

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            PdfPTable table4 = new PdfPTable(3); // the arg is the number of columns
            table4.setWidthPercentage(100);

            PdfPCell cell411 = new PdfPCell(subTitle);
            cell411.setColspan(2);
            cell411.setBorder(PdfPCell.NO_BORDER);
            table4.addCell(cell411);

            PdfPCell cell413 = new PdfPCell(new Phrase(userName, bfBold11));
            cell413.setColspan(1);
            cell413.setBorder(PdfPCell.NO_BORDER);
            cell413.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table4.addCell(cell413);

            PdfPCell cell421 = new PdfPCell(subTitle);
            cell421.setColspan(2);
            cell421.setBorder(PdfPCell.NO_BORDER);
            table4.addCell(cell421);

            PdfPCell cell423 = new PdfPCell(new Phrase("(List Finalised By)", bfBold9));
            cell423.setColspan(1);
            cell423.setBorder(PdfPCell.NO_BORDER);
            cell423.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table4.addCell(cell423);

            doc.add(table4);
            doc.add(new Paragraph(" "));

            doc.close();
            pdf = baos.toByteArray();

        } catch (Exception e) {
            String error = e.getMessage();

            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            doc.setPageSize(PageSize.A4);
            doc.open();
            Paragraph p3 = new Paragraph(error, bf16);
            p3.setAlignment(Element.ALIGN_LEFT);
            doc.add(p3);

            doc.close();
            pdf = baos.toByteArray();
        }

        return pdf;
    }

}
