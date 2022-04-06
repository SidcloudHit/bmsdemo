/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author acer
 */
public class RCDataCheck {

    public static String verifyRCByRCID(String rationCardId) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT udf_get_member_details_by_rcid_or_memberid_new('" + rationCardId + "',null)";
        try {
            con = new DBCon.DBSource().connectToRCDataDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }
    
    public static String verifyRCByRCIDAT(String rationCardId) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT udf_get_member_details_by_rcid_or_memberid_new_changes('" + rationCardId + "',null)";
        try {
            con = new DBCon.DBSource().connectToRCDataDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }

    public static String verifyRCByMemID(String memberId) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT udf_get_member_details_by_rcid_or_memberid_new(null,'" + memberId + "')";
        try {
            con = new DBCon.DBSource().connectToRCDataDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }

    public static String verifyRCByUID(String uId) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT udf_get_member_details_by_uid_new('" + uId + "')";
        try {
            con = new DBCon.DBSource().connectToRCDataDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }
    
    /* Validate beneficiaries from RC Database */
    public static String validateBeneficiaryFromRC(String uId) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT validate_member('" + uId + "')";
        try {
            con = new DBCon.DBSource().connectToRCDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }
    
    
    public static int verifyBenExists(String uId,String rcNo,String rcMem) throws java.sql.SQLException {
        int result = 0;
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT find_beneficiary('" + uId + "','" + rcMem + "')";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }
    
    
    public static String getBankAccDetByPmkisanId(String pmkisanId) throws java.sql.SQLException {
        String result = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        String query = "SELECT get_ben_accdet_from_pmkisan('" + pmkisanId + "')";
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                result = res.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                res.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return result;
    }
}
