/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esign;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.sun.scenario.effect.ImageData;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Debajit
 */
public class iText_TEST_slvt extends HttpServlet {

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
        HttpSession session = request.getSession();
        try {
            int organization_id;
            organization_id = Integer.parseInt(session.getAttribute("org_id").toString().trim());
            String argument_type = request.getParameter("argument_type").toString();
            String argument = request.getParameter("argument").toString();
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet res = null;
            byte[] gen_pdf = null;
            String encodedBase64 = "";
            if (argument_type.equals("srchReport")) {
                argument = Security.SecurityClass.killchar(argument);
                int applicationId = Integer.parseInt(argument.trim());
                //Search here
                if (con == null) {
                    con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                }
                String strSQL = " SELECT COALESCE(pdf_report,'') AS out_report FROM estd.health_facility_establishment_details WHERE application_id=? ";
                ps = con.prepareStatement(strSQL);
                ps.setInt(1, applicationId);
                res = ps.executeQuery();
                if (res.next()) {//Already Available..........
                    String avl_report = res.getString("out_report");
                    if (avl_report.equalsIgnoreCase("") == false) {
                        BASE64Decoder decoder = new BASE64Decoder();
                        gen_pdf = decoder.decodeBuffer(avl_report);
                    } else {
                        gen_pdf = gen_pdf(organization_id, applicationId, request, response);
                        if (gen_pdf != null) {
                            encodedBase64 = new String(Base64.encodeBase64(gen_pdf));
                            //Save PDF...........
                            strSQL = "UPDATE estd.health_facility_establishment_details SET pdf_report = ?,application_status=4 , application_status_str='Provisional Certificate Generated' , is_provisional_ceritficated_generated=1 WHERE application_id=?";
                            ps = con.prepareStatement(strSQL.toString());
                            ps.setString(1, encodedBase64);
                            ps.setInt(2, applicationId);
                            ps.executeUpdate();
                            
                            //commonclass.common validate = new commonclass.common();
                        }
                    }
                }
                /* TODO output your page here. You may use following sample code. */
                ServletOutputStream os = response.getOutputStream();
                response.setContentType("application/pdf");
                os.write(gen_pdf);
                os.flush();
                os.close();
            }
        } catch (Exception ex) {

        }
    }

    private static final long serialVersionUID = 1L;

    public byte[] gen_pdf(int organization_id, int applicationId, HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        String output = "";
        String distname = "", establishment_name = "", application_date = "", comm_address = "", owner_name = "",app_year="",generation_date="";
        byte[] pdf = null;
        //get the output stream for writing binary data in the response.
        //create some special styles and font sizes
        Font bfBold14 = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, new BaseColor(0, 0, 0));
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bfBold11 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, new BaseColor(0, 0, 0));
        bfBold11.setColor(BaseColor.BLUE);
        Font bf16 = new Font(Font.FontFamily.TIMES_ROMAN, 16);
        Font bold10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;

        BASE64Decoder decoder = new BASE64Decoder();

        Connection con = null;

        try {
            if (con == null) {
                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            }
            /*String strSQL = " select substr(hfed.application_id::text,1,4) as app_year,lgd.get_district_name(hfed.state_code,hfed.district_code) as dist_name,establishment_name,to_char(application_entry_date,'dd/MM/yyyy hh:mi:ss AM') as application_date,coalesce(hfed.village_town_city,'')||' ,State:'||lgd.get_state_name(hfed.state_code)||' ,District:'|| lgd.get_district_name(hfed.state_code,hfed.district_code)||' ,Pin No:'||coalesce(hfed.pin_code::text,'') as comm_address, \n"
                    + "coalesce(owner_full_name,'') as owner_name,to_char(current_date,'dd/MM/yyyy') as generation_date from estd.health_facility_establishment_details as hfed ,estd.health_facility_owner_details as hfod where  hfed.application_id = hfod.application_id and hfed.organization_id = hfod.organization_id\n"
                    + "  and hfod.application_id=? and  hfed.is_cancel=0";*/
             String strSQL = " select substr(hfed.application_id::text,1,4) as app_year,lgd.get_district_name(hfed.state_code,hfed.district_code) as dist_name,establishment_name,to_char(application_entry_date,'dd/MM/yyyy hh:mi:ss AM') as application_date,coalesce(hfed.village_town_city,'')||' ,State:'||lgd.get_state_name(hfed.state_code)||' ,District:'|| lgd.get_district_name(hfed.state_code,hfed.district_code)||' ,Pin No:'||coalesce(hfed.pin_code::text,'') as comm_address, \n"
                    + "'Mr R Das' as owner_name,to_char(current_date,'dd/MM/yyyy') as generation_date from estd.health_facility_establishment_details as hfed  where   hfed.application_id=? and  hfed.is_cancel=0";
            //+ "and hfod.organization_id=162720001";
            ps = con.prepareStatement(strSQL);
            ps.setInt(1, applicationId);
            res = ps.executeQuery();
            if (res.next()) {
                app_year=res.getString("app_year");
                distname = res.getString("dist_name");
                establishment_name = res.getString("establishment_name");
                application_date = res.getString("application_date");
                comm_address = res.getString("comm_address");
                owner_name = res.getString("owner_name");
                generation_date = res.getString("generation_date");
            }
            Document doc = new Document();
            doc.setPageSize(PageSize.A4);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            /*Image image = Image.getInstance(request.getContextPath()+"/image/emblem.png");

            image.setAlignment(Element.ALIGN_CENTER);
            doc.add(image);*/
            Paragraph p6 = new Paragraph("Provisional Registration No.:No.F.9(" + applicationId + ")/-CMO("+ distname +")-CLE/"+app_year, bfBold11);
            p6.setAlignment(Element.ALIGN_LEFT);
            doc.add(p6);
            
            Paragraph p7 = new Paragraph("(Form C)", bfBold14);
            p7.setAlignment(Element.ALIGN_CENTER);
            doc.add(p7);

            doc.add(new Paragraph(" "));
            Paragraph p2 = new Paragraph("GOVERNMENT OF TRIPURA", bfBold14);
            p2.setAlignment(Element.ALIGN_CENTER);
            doc.add(p2);

            Paragraph p1 = new Paragraph("District Registering authority", bfBold12);
            p1.setAlignment(Element.ALIGN_CENTER);
            doc.add(p1);

            Paragraph p4 = new Paragraph("("+ distname +")", bfBold12);
            p4.setAlignment(Element.ALIGN_CENTER);
            doc.add(p4);

            Paragraph p3 = new Paragraph("CERTIFICATE OF PROVISIONAL REGISTRATION", bfBold11);
            p3.setAlignment(Element.ALIGN_CENTER);
            doc.add(p3);

            

            doc.add(new Paragraph(" "));
            Paragraph subTitle = new Paragraph("");

            doc.add(new LineSeparator(1, 100, BaseColor.BLACK, 0, serialVersionUID));
            doc.add(new Paragraph(" "));

            Paragraph p5 = new Paragraph("           ");
            p5.add(new Paragraph("This is to certify that "+ establishment_name +" located at "+comm_address, bold10));
            p5.add(new Paragraph("Owned by "+ owner_name +" has been granted provisional registration as a clinical establishment", bold10));
            p5.add(new Paragraph("under section 12 of The Tripura Clinical Establishments (Registration and Regulation) Act, 2018. The Clinical", bold10));
            p5.add(new Paragraph("Establishments is registered for providing medical services as a (Type of Clinical Establishment viz. Hospital, ", bold10));
            p5.add(new Paragraph("diagnostic centers etc).......(Allopathic/Homoeopathic/Ayurvedic etc.)...... system of medicine.", bold10));
            p5.setAlignment(Element.ALIGN_LEFT);
            doc.add(p5);

            doc.add(new Paragraph(" "));
            Paragraph p8 = new Paragraph("This Certificate is valid for a period of 6(six) months from the date of issue.", bold10);
            p8.setAlignment(Element.ALIGN_CENTER);
            doc.add(p8);
            doc.add(new Paragraph(" "));
            doc.add(new LineSeparator(1, 100, BaseColor.BLACK, 0, serialVersionUID));

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));
            PdfPTable table4 = new PdfPTable(3); // the arg is the number of columns
            table4.setWidthPercentage(100);
            PdfPCell cell411 = new PdfPCell(new Phrase("Place:", bfBold11));
            cell411.setColspan(1);
            cell411.setBorder(PdfPCell.NO_BORDER);
            cell411.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell411);

            PdfPCell cell412 = new PdfPCell(subTitle);
            cell412.setColspan(1);
            cell412.setBorder(PdfPCell.NO_BORDER);
            table4.addCell(cell412);

            PdfPCell cell413 = new PdfPCell(new Phrase("(Registering Authority)", bfBold11));
            cell413.setColspan(1);
            cell413.setBorder(PdfPCell.NO_BORDER);
            cell413.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table4.addCell(cell413);

            PdfPCell cell421 = new PdfPCell(new Phrase("Date:"+generation_date, bfBold11));
            cell421.setColspan(1);
            cell421.setBorder(PdfPCell.NO_BORDER);
            cell421.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell421);

            PdfPCell cell422 = new PdfPCell(subTitle);
            cell422.setColspan(2);
            cell422.setBorder(PdfPCell.NO_BORDER);
            table4.addCell(cell422);

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
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Access Denied.Please try Again.");
            }
        }

        return pdf;
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
