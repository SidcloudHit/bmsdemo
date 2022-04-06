/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Common.CommonMethod;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

/**
 *
 * @author NIC-Arindam
 */
public class UserSettingsClass {

    Common.CommonMethod cm = new CommonMethod();

    public JSONArray getUserSchemes(String userId) throws Exception {
//        String sql = "SELECT a.scheme_id, c.scheme_name ,a.role_id, d.role_name ,a.office_id, e.office_name FROM bms_user.map_admin_workflow a "
//                + "INNER JOIN bms_user.map_dept_users_group_based_office_users b ON a.office_id = b.office_id "
//                + "INNER JOIN master_scheme c ON a.scheme_id = c.id "
//                + "INNER JOIN bms_user.mas_user_roles d ON a.role_id = d.role_id "
//                + "INNER JOIN bms_user.mas_dept_users_group_based_office e ON e.office_id = a.office_id "
//                + "WHERE b.user_id ='" + userId + "'";

        String sql = "SELECT a.department_id,g.department_name, b.scheme_id, d.scheme_name ,b.role_id, e.role_name ,c.office_id, f.office_name, \n"
                + "(SELECT mapping_id FROM bms_user.map_admin_workflow WHERE scheme_id = b.scheme_id \n"
                + "AND (office_id = c.office_id OR parent_office_id = c.office_id ) AND role_id = b.role_id) AS workflow \n"
                + "FROM bms_user.map_dept_users_group_users a \n"
                + "INNER JOIN bms_user.map_dept_users_group_schemes b ON a.group_id = b.group_id \n"
                + "INNER JOIN bms_user.map_dept_users_group_based_office_users c ON a.group_id = c.group_id AND a.user_id = c.user_id \n"
                + "INNER JOIN master_scheme d ON b.scheme_id = d.id \n"
                + "INNER JOIN bms_user.mas_user_roles e ON b.role_id = e.role_id \n"
                + "INNER JOIN bms_user.mas_dept_users_group_based_office f ON f.office_id = c.office_id \n"
                + "INNER JOIN mas_department g ON a.department_id = g.department_id \n"
                + "WHERE c.user_id ='" + userId + "' ORDER BY b.scheme_id,b.role_id";

        return cm.getResultJSON(sql);
    }

    public JSONArray getUserSchemes(String userId, int schemeId) throws Exception {
//        String sql = "SELECT a.scheme_id, c.scheme_name ,a.role_id, d.role_name ,a.office_id, e.office_name FROM bms_user.map_admin_workflow a "
//                + "INNER JOIN bms_user.map_dept_users_group_based_office_users b ON a.office_id = b.office_id "
//                + "INNER JOIN master_scheme c ON a.scheme_id = c.id "
//                + "INNER JOIN bms_user.mas_user_roles d ON a.role_id = d.role_id "
//                + "INNER JOIN bms_user.mas_dept_users_group_based_office e ON e.office_id = a.office_id "
//                + "WHERE b.user_id ='" + userId + "'";

        String sql = "SELECT a.department_id,g.department_name, b.scheme_id, d.scheme_name ,b.role_id, e.role_name ,c.office_id, f.office_name, \n"
                + "(SELECT mapping_id FROM bms_user.map_admin_workflow WHERE scheme_id = b.scheme_id \n"
                + "AND (office_id = c.office_id OR parent_office_id = c.office_id ) AND role_id = b.role_id) AS workflow \n"
                + "FROM bms_user.map_dept_users_group_users a \n"
                + "INNER JOIN bms_user.map_dept_users_group_schemes b ON a.group_id = b.group_id \n"
                + "INNER JOIN bms_user.map_dept_users_group_based_office_users c ON a.group_id = c.group_id AND a.user_id = c.user_id \n"
                + "INNER JOIN master_scheme d ON b.scheme_id = d.id \n"
                + "INNER JOIN bms_user.mas_user_roles e ON b.role_id = e.role_id \n"
                + "INNER JOIN bms_user.mas_dept_users_group_based_office f ON f.office_id = c.office_id \n"
                + "INNER JOIN mas_department g ON a.department_id = g.department_id \n"
                + "WHERE c.user_id ='" + userId + "' AND b.scheme_id = " + schemeId + " ORDER BY b.scheme_id,b.role_id";

        return cm.getResultJSON(sql);
    }

    public String getUserSchemeList(String userId, String defaultValue) throws Exception {
//        String sql = "SELECT a.scheme_id, c.scheme_name ,a.role_id, d.role_name ,a.office_id, e.office_name FROM bms_user.map_admin_workflow a "
//                + "INNER JOIN bms_user.map_dept_users_group_based_office_users b ON a.office_id = b.office_id "
//                + "INNER JOIN master_scheme c ON a.scheme_id = c.id "
//                + "INNER JOIN bms_user.mas_user_roles d ON a.role_id = d.role_id "
//                + "INNER JOIN bms_user.mas_dept_users_group_based_office e ON e.office_id = a.office_id "
//                + "WHERE b.user_id ='" + userId + "'";

        String sql = "SELECT b.scheme_id, d.scheme_name \n"
                + "FROM bms_user.map_dept_users_group_users a \n"
                + "INNER JOIN bms_user.map_dept_users_group_schemes b ON a.group_id = b.group_id \n"
                + "INNER JOIN master_scheme d ON b.scheme_id = d.id \n"
                + "INNER JOIN mas_department g ON a.department_id = g.department_id  \n"
                + "WHERE a.user_id ='" + userId + "' ORDER BY b.scheme_id";

        return cm.fillDropdown(sql, defaultValue);
    }

    public JSONArray getUserGroup(String userId) throws Exception {
        String sql = "";

        return cm.getResultJSON(sql);
    }

    public JSONArray getUserGroupBasedOffice(String userId, int groupId) throws Exception {
        String sql = "";

        return cm.getResultJSON(sql);
    }

}
