/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NIC-Arindam
 */
public class UserControl {

    public boolean transferUser(String userId, String remarks) {
        boolean stat = false;
        Connection con = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            con.setAutoCommit(false);
            BMSUserEnableDisable ud = new BMSUserEnableDisable();

            ud.disableDeptUserFromGroup(userId, remarks, con);
            ud.disableDeptUserFromOffice(userId, remarks, con);
            con.commit();
            stat = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return stat;
    }

    public boolean blockUser(String userId, String remarks) {
        boolean stat = false;
        Connection con = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            con.setAutoCommit(false);
            BMSUserEnableDisable ud = new BMSUserEnableDisable();

            ud.disableDeptUserFromGroup(userId, remarks, con);
            ud.disableDeptUserFromOffice(userId, remarks, con);
            ud.disableDeptUser(userId, remarks, con);
            con.commit();
            stat = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return stat;
    }

}
