/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ob_Department;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import log.errorlog;

/**
 *
 * @author NIC-Arindam
 */
public class ob_dept_dal {
errorlog log=new errorlog();
    public int saveDeptOnboardingData(String user_id, int dept_id, String empName1, String empCode1, String empDesignation1, String email1, String mobile1, String dob1, String hrmsCode1, String hex_password, String empName2, String empCode2, String empDesignation2, String email2, String mobile2, String dob2, String hrmsCode2, String user_password) {

        Connection con = null;
        CallableStatement cs = null;
        int status = 0;
        int i = 1;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            cs = con.prepareCall("{call department_onboarding.insert_dept_onboarding_data(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)}");
            // user_id = generateUserId();
            cs.setString(i++, user_id);
            cs.setInt(i++, dept_id);
            cs.setString(i++, empName1);
            cs.setString(i++, empCode1);
            cs.setString(i++, empDesignation1);
            cs.setString(i++, email1);
            cs.setString(i++, mobile1);
            cs.setDate(i++, java.sql.Date.valueOf(dob1));
            cs.setString(i++, hrmsCode1);
            cs.setString(i++, hex_password);
            cs.setString(i++, empName2);
            cs.setString(i++, empCode2);
            cs.setString(i++, empDesignation2);
            cs.setString(i++, email2);
            cs.setString(i++, mobile2);
            if(!dob2.equals("")){
            cs.setDate(i++, java.sql.Date.valueOf(dob2));
            }
            else{
              cs.setDate(i++, java.sql.Date.valueOf("1111-11-11"));   
            }
            cs.setString(i++, hrmsCode2);
            cs.setString(i++, user_password);
            if (cs.execute()) {
                status = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
        }

        return status;
    }

    public int updateDeptOnboardingData(int dept_id, String empName1, String empCode1, String empDesignation1, String email1, String mobile1, String dob1, String hrmsCode1, String hex_password, String empName2, String empCode2, String empDesignation2, String email2, String mobile2, String dob2, String hrmsCode2, String user_password) {

        Connection con = null;
        CallableStatement cs = null;
        int status = 0;
        int i = 1;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            cs = con.prepareCall("{call department_onboarding.update_dept_onboarding_data(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(i++, dept_id);
            cs.setString(i++, empName1);
            cs.setString(i++, empCode1);
            cs.setString(i++, empDesignation1);
            cs.setString(i++, email1);
            cs.setString(i++, mobile1);
            cs.setDate(i++, java.sql.Date.valueOf(dob1));
            cs.setString(i++, hrmsCode1);
            cs.setString(i++, hex_password);
            cs.setString(i++, empName2);
            cs.setString(i++, empCode2);
            cs.setString(i++, empDesignation2);
            cs.setString(i++, email2);
            cs.setString(i++, mobile2);
            if(!dob2.equals("")){
            cs.setDate(i++, java.sql.Date.valueOf(dob2));
            }
            else{
              cs.setDate(i++, java.sql.Date.valueOf("1111-11-11"));   
            }
            cs.setString(i++, hrmsCode2);
            if (cs.execute()) {
                status = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
        }

        return status;
    }

    public String approveDept(int deptId) throws SQLException {
        String status = "0";
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            con.setAutoCommit(false);
            ps = con.prepareStatement("UPDATE department_onboarding.nodal_officer_data set status='1' where dept_id=" + deptId);
            ps.executeUpdate();
            ps1 = con.prepareStatement("UPDATE mas_department set is_onboarded=1 where department_id=" + deptId);
            ps1.executeUpdate();
            ps2 = con.prepareStatement("UPDATE notification.tbl_default_user_passwd_notify set status=1 where dept_id=" + deptId);
            ps2.executeUpdate();
            con.commit();
            con.setAutoCommit(true);

            status = "1";

//            if (ps.executeUpdate() > 0) {
//
//                if (ps1.executeUpdate() > 0) {
//
//                    ps2.executeUpdate();
//                    status = "1";
//                }
//            }




        } catch (Exception e) {
            e.printStackTrace();
            int error_log_insert = log.InsertErrorLog(this.getClass().getSimpleName(), e.getMessage());
            con.rollback();
            con.setAutoCommit(true);
            status = "0";
        } finally {
            con.close();
            ps.close();
        }
        return status;
    }
}
