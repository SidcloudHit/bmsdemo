/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
//import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author NIC-Arindam
 */
@WebServlet(name = "uploadfileservlet", urlPatterns = {"/uploadfileservlet"})
@MultipartConfig
public class uploadfileservlet extends HttpServlet {

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

            String str_usid = "", retPage = "", message = "";
            Part filePart = null;
            try {
                filePart = request.getPart("gfile");
            } catch (Exception ex) {
                message = "Unknown error.";
                retPage = "uploadfile.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(retPage);
                request.setAttribute("cptmsg", message);
                dispatcher.forward(request, response);
                return;
            }

            InputStream inputStream_tocheck = filePart.getInputStream();
            InputStream inputStream_data = filePart.getInputStream();
            Security.secure_xlfile_validation sxl = new Security.secure_xlfile_validation();

            if (!sxl.isSafe(inputStream_tocheck)) {
                message = "Please select valid excel file.";
//            retPage = "uploadfile.jsp";
//            RequestDispatcher dispatcher = request.getRequestDispatcher(retPage);
//            request.setAttribute("cptmsg", message);
//            dispatcher.forward(request, response);

                try {
                    inputStream_tocheck.close();

                } catch (IOException exstr) {
                    exstr.printStackTrace();
                }

            }
            try {

                XSSFWorkbook wb = new XSSFWorkbook(inputStream_data);
                XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
                Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
                while (itr.hasNext()) {
                    Row row = itr.next();
                    Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
//                        out.print(cell.getAddress().toString()+ "\t\t\t");
//                        out.print(cell.getStringCellValue() + "\t\t\t");
                        switch (cell.getCellType()) {
                            case STRING:
                                //field that represents string cell type  
                                out.print(cell.getStringCellValue() + "\t\t\t");
                                break;

                            case NUMERIC:
                                //field that represents number cell type  
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    out.print(dateFormat.format(cell.getDateCellValue()) + "\t\t\t");
                                } else if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    out.print(dateFormat.format(cell.getDateCellValue()) + "\t\t\t");
                                } else if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
                                    out.print(dateFormat.format(cell.getDateCellValue()) + "\t\t");
                                } else if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                                    out.print(dateFormat.format(cell.getDateCellValue()) + "\t\t");
                                } else {
                                    out.print(cell.getNumericCellValue() + "\t\t");
                                }
                                //out.print(cell.getNumericCellValue() + "\t\t\t");
                                break;

                            case BOOLEAN:
                                //Boolean cell type

                                break;

                            case BLANK:
                                //Blank cell type

                                break;

                            case FORMULA:
                                //Formula cell type

                                break;

                            case ERROR:
                                //Error cell type
                                break;

                            case _NONE:
                                //Unknown type, used to represent a state prior to initialization or the lack of a concrete type.
                                break;

                            default:
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
