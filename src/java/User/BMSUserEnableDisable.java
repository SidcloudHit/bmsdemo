/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

/**
 *
 * @author NIC-Arindam
 */
public class BMSUserEnableDisable {

////////////////////////////////////////////////////////////////////////// Master Data Disable Functions    
    //Dept User Login Disable
    public boolean disableDeptUser(String userId, String remarks) {
        boolean stat = false;
        //flag in the login table to be disabled

        return stat;
    }

    public boolean disableDeptUser(String userId, String remarks, java.sql.Connection con) {
        boolean stat = false;
        //flag in the login table to be disabled
        String query = "UPDATE bms_user.mas_dept_users"
                + "	SET is_blocked=?, block_date=?, block_remarks=?"
                + "	WHERE user_id=?";
        return stat;
    }

    //Admin User Login Disable
    public boolean disableAdminUser(String userId, String remarks) {
        boolean stat = false;
        //flag in the login table to be disabled
        return stat;
    }

    public boolean disableAdminUser(String userId, String remarks, java.sql.Connection con) {
        boolean stat = false;
        //flag in the login table to be disabled
        return stat;
    }

    //Group Disable
    public boolean disableDeptUserGroup(int groupId) {
        boolean stat = false;
        //flag in the group table to be disabled
        //disableDeptUserFromGroup(groupId);
        //disableSchemeFromGroup(groupId);
        //disableGroupReportingWorkflow(groupId);
        return stat;
    }

    //Group Based Office Disable
    public boolean disableDeptUserGroupOffice(int officeId) {
        boolean stat = false;
        //flag in the office table to be disabled
        //disableDeptUserFromOffice(officeId);
        //disableOfficeReportingWorkflow(officeId);
        return stat;
    }

////////////////////////////////////////////////////////////////////////// Mapping Data Disable Functions - User Group Mapping
    //Disable One Employee From Group -- Promotion / Retirement / Deputation
    public boolean disableDeptUserFromGroup(String userId, String remarks) {
        boolean stat = false;
        saveHistoryDeptUserFromGroup(userId, remarks);
        deleteDeptUserFromGroup(userId);

        return stat;
    }

    public boolean disableDeptUserFromGroup(String userId, String remarks, java.sql.Connection con) {
        boolean stat = false;
        saveHistoryDeptUserFromGroup(userId, remarks, con);
        deleteDeptUserFromGroup(userId, con);

        return stat;
    }

//    //Disable All Employee From Group
//    public boolean disableDeptUserFromGroup(int groupId) {
//        String userId = "";
//        //For all employees in the group
//        saveDeptUserFromGroupHistory(userId, groupId);
//        deleteDeptUserFromGroup(userId, groupId);
//
//        return false;
//    }
    public boolean deleteDeptUserFromGroup(String userId) {
        boolean stat = false;

        return stat;
    }

    public boolean deleteDeptUserFromGroup(String userId, java.sql.Connection con) {
        boolean stat = false;

        return stat;
    }

    public boolean saveHistoryDeptUserFromGroup(String userId, String remarks) {
        boolean stat = false;

        return stat;
    }

    public boolean saveHistoryDeptUserFromGroup(String userId, String remarks, java.sql.Connection con) {
        boolean stat = false;

        return stat;
    }

////////////////////////////////////////////////////////////////////////// Mapping Data Disable Functions - User Group Office Mapping    
    //Employee Disabled from Group Based Office -- Transfer / Deputation
    public boolean disableDeptUserFromOffice(String userId, String remarks) {
        boolean stat = false;
        saveHistoryDeptUserFromOffice(userId, remarks);
        deleteDeptUserFromOffice(userId);

        return stat;
    }

    public boolean disableDeptUserFromOffice(String userId, String remarks, java.sql.Connection con) {
        boolean stat = false;
        saveHistoryDeptUserFromOffice(userId, remarks, con);
        deleteDeptUserFromOffice(userId, con);

        return stat;
    }

    public boolean deleteDeptUserFromOffice(String userId) {
        boolean stat = false;

        return stat;
    }

    public boolean deleteDeptUserFromOffice(String userId, java.sql.Connection con) {
        boolean stat = false;

        return stat;
    }

    public boolean saveHistoryDeptUserFromOffice(String userId, String remarks) {
        boolean stat = false;

        return stat;
    }

    public boolean saveHistoryDeptUserFromOffice(String userId, String remarks, java.sql.Connection con) {
        boolean stat = false;

        return stat;
    }
////////////////////////////////////////////////////////////////////////// Mapping Data Disable Functions - Scheme Group Role Mapping 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //Disable All Scheme From Group -- Group Disabled
////    public boolean disableSchemeFromGroup(int groupId) {
////
////        return false;
////    }
//    //Disable One Scheme From Group
//    public boolean disableSchemeFromGroup(int schemeId, int groupId) {
//
//        return false;
//    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //Employee Disabled from Group Based Office -- Office Disabled
//
//    public boolean disableDeptUserFromOffice(int officeId) {
//        //Insert into history table
//        //Update 
//        return false;
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    // Disable Reporting Flow -- Office Disabled
//    public boolean disableOfficeReportingWorkflow(int officeId) {
//
//        return false;
//    }
//
//    // Disable Reporting Flow -- Group Disabled
//    public boolean disableGroupReportingWorkflow(int groupId) {
//
//        return false;
//    }
//
//    // Disable Reporting Flow -- User Disabled
//    public boolean disableUserReportingWorkflow(String userId) {
//
//        return false;
//    }
//
//    // Disable Reporting Flow
////    public boolean disableReportingWorkflow(String userId, int schemeId, int mappingType, int officeId, int parentOfficeId) {
////
////        return false;
////    }
}
