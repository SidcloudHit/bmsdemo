/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class BeneficiarySchemeDetails extends Master.BeneficiarySchemeDetails {
      String query;

    public BeneficiarySchemeDetails() {
    }

    public BeneficiarySchemeDetails(int dept_id) {
        
        query = "SELECT * FROM beneficiary.beneficiary_scheme_details WHERE department_id = " + dept_id;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);

            while (res.next()) {
             
               this.setDept_id(res.getInt("department_id"));
               this.setScheme_id(res.getString("scheme_id"));
               this.setScheme_code(res.getString("scheme_code"));
               this.setRc_mem(res.getString("rc_mem"));
               this.setBeneficiary_name(res.getString("beneficiary_name"));
               this.setCreated_on(res.getString("entrydate"));
               this.setCreated_by(res.getString("entryby"));

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
      
    public BeneficiarySchemeDetails(String rc_mem) {
        
        query = "SELECT * FROM beneficiary.beneficiary_scheme_details WHERE rc_mem = " + rc_mem;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);

            while (res.next()) {
             
               this.setDept_id(res.getInt("department_id"));
               this.setScheme_id(res.getString("scheme_id"));
               this.setScheme_code(res.getString("scheme_code"));
               this.setRc_mem(res.getString("rc_mem"));
               this.setBeneficiary_name(res.getString("beneficiary_name"));
               this.setCreated_on(res.getString("entrydate"));
               this.setCreated_by(res.getString("entryby"));

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
