/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author acer
 */
public class AdminUser extends Master.AdminUser {

    String query;

    public AdminUser() {
    }

    public AdminUser(String user_id) {
        query = "SELECT * FROM mas_admin_user_account WHERE user_id = '" + user_id + "' ";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                this.setUser_id(res.getString("user_id"));
                this.setUser_name(res.getString("user_name"));
                this.setUser_email(res.getString("user_email"));
                this.setUser_mobile(res.getString("user_mobile"));
                this.setUser_gender(res.getString("user_gender"));
                this.setUser_designation(res.getString("user_designation"));
                this.setUser_alt_mobile(res.getString("user_alt_mobile"));
                this.setUser_department_id(res.getInt("user_department_id"));
                this.setUser_role(res.getInt("user_role"));
                this.setAddress1_city_town_village(res.getString("address1_city_town_village"));
                this.setAddress1_road_landmark(res.getString("address1_road_landmark"));
                this.setAddress1_po(res.getString("address1_po"));
                this.setAddress1_district(res.getString("address1_district"));
                this.setAddress1_pin(res.getString("address1_pin"));
                this.setUser_rc_member_id(res.getString("user_rc_member_id"));
                this.setUser_office(res.getInt("user_office"));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (stmt != null) {
                    stmt.close();
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
}
