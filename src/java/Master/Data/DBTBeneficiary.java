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
 * @author NIC-Arindam
 */
public class DBTBeneficiary extends Master.DBTBeneficiary {

    String query;

    public DBTBeneficiary() {
    }

    public DBTBeneficiary(int beneficiary_id) {
        query = "SELECT * FROM dbt_beneficiary WHERE beneficiary_id = " + beneficiary_id;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);

            while (res.next()) {
                this.setBeneficiary_id(res.getInt("beneficiary_id"));
                this.setBeneficiary_name(res.getString("beneficiary_name"));
                this.setFather_name(res.getString("father_name"));
                this.setDob(res.getString("dob"));
                this.setGender(res.getString("gender"));
                this.setCaste(res.getString("caste"));
                this.setReligion(res.getString("religion"));
                this.setMobile(res.getString("mobile"));
                this.setEmail(res.getString("email"));
                this.setEducation_qly(res.getString("education_qly"));
                this.setAadhaar(res.getString("aadhaar"));
                this.setPan(res.getString("pan"));
                this.setRation_card_no(res.getString("ration_card_no"));
                this.setRationcard_memberid(res.getString("rationcard_memberid"));
                this.setEpic_no(res.getString("epic_no"));
                this.setDl_no(res.getString("dl_no"));
                this.setMgnrega_jobcard_no(res.getString("mgnrega_jobcard_no"));
                this.setMgnrega_jobcard_member_id(res.getString("mgnrega_jobcard_member_id"));
                this.setPmkisan_id(res.getString("pmkisan_id"));
                this.setKisan_credit_card_no(res.getString("kisan_credit_card_no"));
                this.setSoil_health_card_no(res.getString("soil_health_card_no"));
                this.setInsurance_no(res.getString("insurance_no"));
                this.setDist_code(res.getString("dist_code"));
                this.setDist_name(res.getString("dist_name"));
                this.setSubdiv_code(res.getString("subdiv_code"));
                this.setSubdiv_name(res.getString("subdiv_name"));
                this.setLocation_type(res.getString("location_type"));
                this.setBlock_code(res.getString("block_code"));
                this.setBlock_name(res.getString("block_name"));
                this.setWard_code(res.getString("ward_code"));
                this.setWard_name(res.getString("ward_name"));
                this.setRev_vill_code(res.getString("rev_vill_code"));
                this.setRev_vill_name(res.getString("rev_vill_name"));
                this.setLandmark(res.getString("landmark"));
                this.setPs_code(res.getString("ps_code"));
                this.setPs_name(res.getString("ps_name"));
                this.setPo_code(res.getString("po_code"));
                this.setPo_name(res.getString("po_name"));
                this.setPin(res.getString("pin"));
                this.setBank_name(res.getString("bank_name"));
                this.setBank_account(res.getString("bank_account"));
                this.setIfsc(res.getString("ifsc"));
                this.setAnnual_income(res.getString("annual_income"));
                this.setOccupation(res.getString("occupation"));
                this.setHead_of_family(res.getBoolean("head_of_family"));
                this.setActive(res.getBoolean("active"));

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
