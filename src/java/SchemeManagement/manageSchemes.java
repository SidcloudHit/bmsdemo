/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchemeManagement;

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
public class manageSchemes {

    public String changeSchemeManagementDate(int schemeId, int deptId, String txtApplDate, String txtVeriDate, String txtApprDate, Connection con) {
        String result = "";
        PreparedStatement ps = null, ps1 = null;
        String query = "UPDATE scheme_onboarding.scheme_ob_basic_info set active_upto='" + txtApplDate + "'::DATE,last_date_for_verification='" + txtVeriDate + "'::DATE,"
                + "last_date_for_approval='" + txtApprDate + "'::DATE WHERE sl_no=" + schemeId + " AND department_id=" + deptId;

        try {
            ps = con.prepareStatement(query);

            if (ps.executeUpdate() > 0) {
                result = "1";
            }

        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };

        }
        return result;
    }

    public String checkUserWorkingPeriod(int schemeId, int deptId, int role,Connection con) {
        String result = "";
        String param = "";
        switch (role) {
            case 4:
                param = "DATE(last_date_for_approval)";
                break;
            case 5:
                param = "DATE(last_date_for_verification)";
                break;
            default:
                param = "DATE(active_upto)";
                break;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "Select " + param + " as expiary_date from scheme_onboarding.scheme_ob_basic_info where sl_no=" + schemeId ;
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("expiary_date");
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
            
        }
        return result;
    }
}
