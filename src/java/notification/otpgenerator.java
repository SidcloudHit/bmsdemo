/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notification;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author Administrator
 */
public class otpgenerator {

    public int generateotp(int seed) {
        SecureRandom srnd = new SecureRandom();
        int otp = 0;
        //otp = srnd.nextInt(1000000);
        otp = srnd.nextInt(seed);
        return otp;
    }

    public int saveintoDB(int emailotp, int mobileotp, String email, String mobile, String userid, int otp_lifetime_inmin) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int status = 0;
        
        try {
            con = new DBCon.DBSource().connectToBMSDB().getConnection();
            ps = con.prepareStatement("INSERT INTO citizen.tbl_otp(\n"
                    + "	user_id, mobile_otp, email_otp, reason, created_on, email, mobile_no, expired_on)\n"
                    + "	VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, NOW() + '" + otp_lifetime_inmin + " MINUTES'::INTERVAL)");
            ps.setInt(1, Integer.parseInt(userid));
            ps.setString(2, String.valueOf(mobileotp));
            ps.setString(3, String.valueOf(emailotp));
            ps.setString(4, "Forgot Password");
            ps.setString(5, email);
            ps.setString(6, mobile);

            status = ps.executeUpdate();

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
        return status;
    }

}
