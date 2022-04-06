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
 * @author Arindam Mukherjee
 */
public class MappenLandInfo extends Master.MappenLandInfo {

    String query;

    public MappenLandInfo() {
    }

    public MappenLandInfo(int landId) {
        query = "SELECT * FROM dbt_beneficiary_land_info WHERE land_id = " + landId;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);

            while (res.next()) {
                this.setLand_id(res.getInt("land_id"));
                this.setBeneficiary_id(res.getInt("beneficiary_id"));
                this.setState_code(res.getInt("state_code"));
                this.setDist_code(res.getInt("dist_code"));
                this.setDist_name(res.getString("dist_name"));
                this.setSubdiv_code(res.getInt("subdiv_code"));
                this.setSubdiv_name(res.getString("subdiv_name"));
                this.setRev_circle_code(res.getInt("rev_circle_code"));
                this.setRev_circle_name(res.getString("rev_circle_name"));
                this.setTehsil_code(res.getInt("tehsil_code"));
                this.setTehsil_name(res.getString("tehsil_name"));
                this.setRev_vill_code(res.getInt("rev_vill_code"));
                this.setRev_vill_name(res.getString("rev_vill_name"));
                this.setKhatian_no(res.getInt("khatian_no"));
                this.setPlot_no(res.getInt("plot_no"));
                this.setPlot_area(res.getDouble("plot_area"));
                this.setPlot_area_unit(res.getString("plot_area_unit"));
                this.setPlot_land_class(res.getString("plot_land_class"));
                this.setPlot_land_subclass(res.getString("plot_land_subclass"));
                this.setOwnership_type(res.getInt("ownership_type"));
                this.setOwnership_share(res.getDouble("ownership_share"));
                this.setOwners_info_from_lr(res.getString("owners_info_from_lr"));
                this.setEntry_by(res.getString("entry_by"));
                this.setCreated(res.getString("created"));
                this.setLastupdated(res.getString("lastupdated"));

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
