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
public class MasterApplicationUrl extends Master.MasterApplicationUrl {
    String query;

    public MasterApplicationUrl() {
    }

    public MasterApplicationUrl(String url_id) {
       query = "SELECT * FROM mas_application_url WHERE url_id = " + url_id ;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                this.setUrl_id(res.getInt("url_id"));
                this.setUrl(res.getString("url"));
                this.setUrl_name(res.getString("url_name"));
                this.setIs_header(res.getInt("is_header"));
                this.setIs_active(res.getInt("is_active"));
                

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

