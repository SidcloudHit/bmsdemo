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
 * @author Nibedita
 */
public class MasterUserRoleUrl extends Master.MasterUserRoleUrl {
    String query;

    public MasterUserRoleUrl() {
    }

    public MasterUserRoleUrl(String mapping_id) {
       query = "SELECT mapping_id, scheme_id, role_id, url_id, is_active, priority_no FROM map_scheme_role_url WHERE mapping_id = " + mapping_id ;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                //this.setDepartment_id(res.getInt("department_id"));
                this.setMapping_id(res.getInt("mapping_id"));
                this.setScheme_id(res.getInt("scheme_id"));
                this.setRole_id(res.getInt("role_id"));
                this.setUrl_id(res.getInt("url_id"));
                this.setIs_active(res.getInt("is_active"));
                this.setPriority_no(res.getInt("priority_no"));
                //this.setIs_main_pg(res.getInt("is_main_pg"));
                

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

