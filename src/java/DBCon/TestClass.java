/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCon;

import Security.SecurityClass;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;

/**
 *
 * @author acer
 */
public class TestClass {

    public static void main(String[] argv) throws Exception {

        System.out.println("Hello");

        //Common.LandRecordsCheck.verifyLand("272631", "98");
        /* Connection con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement("SELECT COUNT(*) FROM master_bank_name");
//                ps.setString(1, email);
//                ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs != null) {
                rs.next();
//                    User user = new User(rs.getString("name"), rs.getString("email"), rs.getString("country"), rs.getInt("id"));
//                    logger.info("User found with details=" + user);
//                    HttpSession session = request.getSession();
//                    session.setAttribute("User", user);
//                    response.sendRedirect("home.jsp");;
//                } else {
//                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
//                    PrintWriter out = response.getWriter();
//                    logger.error("User not found with email=" + email);
                System.out.println("<font color=red>" + rs.getString(1) + "</font>");
//                    rd.include(request, response);
            } else {
                System.out.println("<font color=red>error</font>");
            }
        } catch (Exception e) {
            e.printStackTrace();
//                logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (Exception e) {
//                    logger.error("SQLException in closing PreparedStatement or ResultSet");;
            }

        }*/
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//
//        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//        System.out.println("Financial month : " + month);
//        if (month <= 3) {
//            System.out.println("Financial Year : " + (year - 1) + "-" + year);
//        } else {
//            System.out.println("Financial Year : " + year + "-" + (year + 1));
//        }
//        SecurityClass sc = new SecurityClass();
//        //String hex_password = sc.gethexString(user_password);
//        Connection con = null;
//        PreparedStatement ps = null;
//        PreparedStatement ps1 = null;
//        ResultSet rs = null;
//        String query1 = "", query2 = "";
//        try {
//            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//            //con.setAutoCommit(false);
//
//            query1 = "SELECT user_id,user_password FROM mas_admin_user_account";
//            ps = con.prepareStatement(query1);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                query2 = "UPDATE mas_admin_user_account SET  user_password = ? WHERE user_id = ?";
//
//                ps1 = con.prepareStatement(query2);
//                int i = 1;
//                ps1.setString(i++, sc.gethexString(rs.getString(2)));
//                ps1.setString(i++, rs.getString(1));
//
//                if (ps1.executeUpdate() > 0) {
//                    System.out.println("Success");
//                } else {
//                    System.out.println("Failure");
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            };
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            };
//            try {
//                if (con != null) {
//                    con.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            };
//        }
//String str = "0.55";
//System.out.println(str.matches("^(?:\\d+(?:\\.{1}\\d+)?)$"));
//        try (InputStream input = new FileInputStream("..//Common//config.properties")) {
//
//            Properties prop = new Properties();
//
//            // load a properties file
//            prop.load(input);
//
//            // get the property value and print it out
//            System.out.println(prop.getProperty("dbt.cash.req"));
//            System.out.println(prop.getProperty("dbt.cash.res"));
//            System.out.println(prop.getProperty("dbt.kind.req"));
//            System.out.println(prop.getProperty("dbt.kind.res"));
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        ResourceBundle resource = ResourceBundle.getBundle("Common/config");
//        String path = resource.getString("dbt.cash.req");
//        System.out.println(path);
//String s1="CIGT8_1600165689549_1";
//System.out.println(s1.substring(0,5));
//System.out.println(s1.substring(6,19));
//        HashMap<Integer, Integer> itemQuantity = new HashMap<>();
//        HashMap<Integer, String> itemName = new HashMap<>();
//
//        itemQuantity.put(1, 10);
//        itemName.put(1, "x");
//        itemQuantity.put(2, 10);
//        itemName.put(2, "y");
//        itemQuantity.put(3, 10);
//        itemName.put(3, "z");
        String uid = "ssotApiUser";
        String pwd = "BMS@241#ssotAPI";
        String encPwd = Security.AESEncryption.encrypt(pwd);

        System.out.println(encPwd);
    }

    /*
 * Imports the content from the specified DataTable into a new Aspose.Words Table object.
 * The table is inserted at the current position of the document builder and using the current builder's formatting if any is defined.
     */
//public static Table importTableFromDataTable(DocumentBuilder builder, DataTable dataTable, boolean importColumnHeadings) throws Exception
//{
//    Table table = builder.startTable();
//
//    ResultSetMetaData metaData = dataTable.getResultSet().getMetaData();
//    int numColumns = metaData.getColumnCount();
//
//    // Check if the names of the columns from the data source are to be included in a header row.
//    if (importColumnHeadings)
//    {
//        // Store the original values of these properties before changing them.
//        boolean boldValue = builder.getFont().getBold();
//        int paragraphAlignmentValue = builder.getParagraphFormat().getAlignment();
//
//        // Format the heading row with the appropriate properties.
//        builder.getFont().setBold(true);
//        builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
//
//        // Create a new row and insert the name of each column into the first row of the table.
//        for (int i = 1; i < numColumns + 1; i++)
//        {
//            builder.insertCell();
//            builder.writeln(metaData.getColumnName(i));
//        }
//
//        builder.endRow();
//
//        // Restore the original formatting.
//        builder.getFont().setBold(boldValue);
//        builder.getParagraphFormat().setAlignment(paragraphAlignmentValue);
//    }
//
//    // Iterate through all rows and then columns of the data.
//    while(dataTable.getResultSet().next())
//    {
//        for (int i = 1; i < numColumns + 1; i++)
//        {
//            // Insert a new cell for each object.
//            builder.insertCell();
//
//            // Retrieve the current record.
//            Object item = dataTable.getResultSet().getObject(metaData.getColumnName(i));
//            // This is name of the data type.
//            String typeName = item.getClass().getSimpleName();
//
//            if(typeName.equals("byte[]"))
//            {
//                // Assume a byte array is an image. Other data types can be added here.
//                builder.insertImage((byte[])item, 50, 50);
//            }
//            else if(typeName.equals("Timestamp"))
//            {
//                // Define a custom format for dates and times.
//                builder.write(new SimpleDateFormat("MMMM d, yyyy").format((Timestamp)item));
//            }
//            else
//            {
//                // By default any other item will be inserted as text.
//                builder.write(item.toString());
//            }
//
//        }
//
//        // After we insert all the data from the current record we can end the table row.
//        builder.endRow();
//    }
//
//    // We have finished inserting all the data from the DataTable, we can end the table.
//    builder.endTable();
//
//    return table;
//}
}
