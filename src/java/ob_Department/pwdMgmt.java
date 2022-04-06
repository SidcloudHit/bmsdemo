/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Department;

import Security.SecurityClass;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.errorlog;

/**
 *
 * @author Administrator
 */
public class pwdMgmt {
errorlog log=new errorlog();
    public String generateDefaultPassword() throws NoSuchAlgorithmException {
        String user_password = "bms";
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        int myInt = sr.nextInt(1000);
        user_password = user_password + String.format("%06d", myInt);
        return user_password;
    }
     public String getUserDetails(String user_id) {
        String ans = "";
        String emp_mobile = "", emp_email = "",message="",status="";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT slno, user_id, dept_id, uname, email, mobile, email_msg, sms_msg, is_notified, entry_datetime,status FROM notification.tbl_default_user_passwd_notify where user_id='" + user_id + "'";

        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                user_id = rs.getString("user_id");
                emp_mobile = rs.getString("mobile");
                emp_email = rs.getString("email");
                message=rs.getString("email_msg");
                status=rs.getString("status");
            }
        } catch (SQLException ex) {
            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), ex.getMessage());
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
        ans = emp_mobile + "~" + emp_email + "~" + user_id + "~" + message+"~"+status;
        return ans;

    }
}
