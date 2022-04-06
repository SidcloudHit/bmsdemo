/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Schemes;

import Common.CommonMethod;
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
public class Scheme_ob_method {

    public int insert_scheme_basic_data(String scheme_code, String scheme_name, String component_name, String scheme_type, int dept, String dept_name,
            String launch_date, String launch_fy, String active_upto, int scheme_benefit, int beneficiary_fy_status, String scheme_desc, int data_entry_from,
            String verification_last_date, String approval_last_date) {
        int scheme_id = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("INSERT INTO scheme_onboarding.scheme_ob_basic_info_draft(department_id, department_name, "
                    + "benefit_type, scheme_name, scheme_codification,component_name,scheme_type, description,	created, lastupdated, "
                    + "probable_launch_date, launch_fy, active_upto, beneficiary_financial_status,scheme_data_source,"
                    + "last_date_for_verification,last_date_for_approval)"
                    + "VALUES (?, ?, ?, ?,?,?::integer, ?,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?::date, ?, ?::date,"
                    + " ?,?,?::date,?::date) RETURNING sl_no");
            ps.setInt(1, dept);
            ps.setString(2, dept_name);
            ps.setInt(3, scheme_benefit);
            ps.setString(4, scheme_name);
            ps.setString(5, scheme_code);
            ps.setString(6, component_name);
            ps.setString(7, scheme_type);
            ps.setString(8, scheme_desc);
            ps.setString(9, launch_date);
            ps.setString(10, launch_fy);
            ps.setString(11, active_upto.equals("") ? null : active_upto);
            ps.setInt(12, beneficiary_fy_status);
            ps.setInt(13, data_entry_from);
            ps.setString(14, verification_last_date.equals("") ? null : verification_last_date);
            ps.setString(15, approval_last_date.equals("") ? null : approval_last_date);
            rs = ps.executeQuery();
            if (rs.next()) {
                scheme_id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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
        return scheme_id;
    }
}
