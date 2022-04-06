/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class errorlog {

    public int InsertErrorLog(String error_class_name, String error_msg) {
        int status = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "INSERT INTO log.bmsportal_error_log(java_class_name, log_description, entry_datetime) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try {
            con = con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            ps = con.prepareStatement(sql);
            ps.setString(1, error_class_name);
            ps.setString(2, error_msg);

            status = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(errorlog.class.getName()).log(Level.SEVERE, null, ex);
            int error_log_insert = InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
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

        return status;
    }
}
