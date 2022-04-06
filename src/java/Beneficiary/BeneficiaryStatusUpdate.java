/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beneficiary;

import Common.CommonMethod;
import DBTWorks.*;
import Master.Data.AdminUser;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author NIC-Arindam
 */
public class BeneficiaryStatusUpdate {

    public static int updateBeneficiaryFreezeStatus(String tabName, String tabName1, int schemeId, String[] benids, String uId, int status, int roleId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        CallableStatement cs = null;
        //AdminUser usr = new AdminUser(uId);
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            cs = con.prepareCall("{call beneficiary.beneficiary_transaction_table_create(?)}");
            cs.setString(1, tabName1);
            cs.execute();

            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call beneficiary.dbt_beneficiary_freeze(?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName1);
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);//json_obj.toString());
            ps.setInt(i++, status);
            ps.setInt(i++, roleId);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }
public static int updateBeneficiaryUnFreezeStatus(String tabName, String tabName1, int schemeId, String[] benids, String uId, int status, int roleId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        CallableStatement cs = null;
        //AdminUser usr = new AdminUser(uId);
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call beneficiary.dbt_beneficiary_unfreeze(?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName1);
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);//json_obj.toString());
            ps.setInt(i++, status);
            ps.setInt(i++, roleId);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }
    public static int updateBeneficiarySignStatus(String tabName, String tabName1, int schemeId, int applId, int uRole, String[] benids) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call beneficiary.beneficiary_signed(?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setString(i++, tabName1);
            ps.setInt(i++, schemeId);
            ps.setInt(i++, applId);
            ps.setInt(i++, uRole);
            ps.setArray(i++, dArray);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));
                }

                //result = 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }

    public static int updateCashDBTVerificationStatus(String tabName, int schemeId, String finYr, int instl, String[] benids, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_verify(?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);//json_obj.toString());
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }

    public static int updateKindDBTVerificationStatus(String tabName, int schemeId, String finYr, int instl, Integer[] itemIds, String[] benids, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("VARCHAR", benids);
            Array iArray = con.createArrayOf("INTEGER", itemIds);
            String query = "";
            int i = 1;

            ps = con.prepareCall("{CALL dbt_kind_verify(?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);
            ps.setArray(i++, iArray);

//            ps = con.prepareCall("{CALL update_dbt_verification_kind(?,?,?,?,?,?)}");
//            ps.setInt(i++, schemeId);
//            ps.setString(i++, finYr);
//            ps.setInt(i++, instl);
//            ps.setString(i++, uId);
//            ps.setArray(i++, iArray);
//            ps.setArray(i++, dArray);//json_obj.toString());
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }

    public static int updateCashDBTRequestStatus(String tabName, int schemeId, String finYr, int instl, String[] benids, String batchId, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_approve(?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setString(i++, batchId);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);//json_obj.toString());
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }

    public static int updateCashDBTRequestStatusjsoncreated(String tabName, int schemeId, String finYr, int instl, String[] benids) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_approve_file_created(?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setArray(i++, dArray);//json_obj.toString());
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
        return result;
    }

    public static void updateisfilegenarated(int application_id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "UPDATE esign.dbt_esign_application_details   SET  jsonfilegenerated=true WHERE application_id =?;";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, application_id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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

    public static void updateFilePushStatus(String tabName, String batchId) {
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "UPDATE " + tabName + " SET status=?, lastupdated=now() WHERE batch_id=?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, 6);
            ps.setString(i++, batchId);
            int status = ps.executeUpdate();

//            if (ps.execute()) {
//                rs = ps.getResultSet();
//                while (rs.next()) {
//                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
//                }
//            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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

    public static void updateKindDBTRequestStatus(int schemeId, String finYr, int instl, Integer[] itemIds, Integer[] benids, String batchId, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("BIGINT", benids);
            Array iArray = con.createArrayOf("INTEGER", itemIds);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{CALL update_dbt_approve_kind(?,?,?,?,?,?,?)}");
            ps.setString(i++, String.valueOf(batchId));
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setString(i++, uId);
            ps.setArray(i++, iArray);
            ps.setArray(i++, dArray);//json_obj.toString());
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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

    public static void updateCashDBTResponseStatus(String batchId, JSONArray beneficiary) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "";
            int i = 1;
            ps = con.prepareCall("{CALL update_dbt_response_cash(?,?::json)}");
            ps.setString(i++, batchId);
            ps.setObject(i++, beneficiary);
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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

    public static void updateKindDBTResponseStatus(String storeId, String batchId, JSONArray beneficiary) throws JSONException {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "";
            int i = 1;
            JSONObject beneficiaryObj = new JSONObject();
            beneficiaryObj.put("beneficiary", beneficiary);

            ps = con.prepareCall("{CALL update_dbt_response_kind(?,?,?::json)}");
            ps.setString(i++, batchId);
            ps.setString(i++, storeId);
            ps.setObject(i++, beneficiaryObj.toString());
            if (ps.execute()) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    result = Integer.parseInt(rs.getString(1));//"Number of Beneficiary Mapped with Scheme Code : " + schemeCode + " is " + rs.getString(1) + "";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            };
            try {
                if (ps != null) {
                    ps.close();
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
