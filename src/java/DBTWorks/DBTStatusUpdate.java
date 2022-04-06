/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBTWorks;

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
public class DBTStatusUpdate {

    public static int updateCashDBTFreezeStatus(String tabName, int schemeId, String finYr, int instl, String[] benids, String uId, int status) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_freeze(?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setString(i++, uId);
            ps.setArray(i++, dArray);//json_obj.toString());
            ps.setInt(i++, status);
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

    public static int updateCashDBTSignStatus(int applId, int uRole) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();

            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_signed(?,?)}");
            ps.setInt(i++, applId);
            ps.setInt(i++, uRole);
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

    public static int updateCashDBTBatchUpdate(String tabName, int schemeId, String finYr, int instl, String[] benids, String batchId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_approve_update_batchid(?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setString(i++, batchId);
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
    
    public static int CalculateBeneficiaryDBTAmount(String tabName,String db_function_name, int schemeId, String finYr, int instl, String[] benids, String batchId, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call payment_calculation."+db_function_name+"(?,?,?,?,?,?)}");
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

    public static int InitiateCashDBTRequestDirect(String tabName, int schemeId, String finYr, int instl, Integer[] amounts, String[] benids, String batchId, String uId) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            Array dArray1 = con.createArrayOf("integer", amounts);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call dbt_cash_initiate_direct(?,?,?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setString(i++, batchId);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setArray(i++, dArray1);
            ps.setBoolean(i++, true);
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
            String query = "UPDATE transaction." + tabName + " SET status=?, lastupdated = now() WHERE batch_id=?";
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

    public static void updateFilePushStatus(String tabName, String batchId, Connection con) {
        int result = 0;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
//            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "UPDATE transaction." + tabName + " SET status=?, lastupdated = now() WHERE batch_id=?";
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

        }
    }

    public static void updateDBTFileStatus(String batchId, String userId, String tabname, Connection con) {
        int result = 0;
        //Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "UPDATE dbtgenerated_file_list SET source_table_name=?, is_file_push=?, pushed_by = ?, pushed_at = now(), file_session_valid = ? WHERE batch_id=?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, tabname);
            ps.setBoolean(i++, true);
            ps.setString(i++, userId);
            ps.setBoolean(i++, false);
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
//            try {
//                if (con != null) {
//                    con.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            };
        }
    }

    public static void updateDBTFileStatus(String batchId, String userId, Connection con) {
        int result = 0;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            String query = "UPDATE dbtgenerated_file_list SET is_file_push=?, pushed_by = ?, pushed_at = now(), file_session_valid = ? WHERE batch_id=?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setBoolean(i++, true);
            ps.setString(i++, userId);
            ps.setString(i++, batchId);
            ps.setBoolean(i++, false);
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

    public static void updateCashDBTResponseStatus(String batchId,
            String useridhash,
            int total_beneficiary_count,
            String total_transaction_amount,
            String scheme_code,
            String scheme_name,
            String reference_date,
            String remarks,
            String success_beneficiary_count,
            String success_amount,
            String fail_beneficiary_count,
            String fail_amount,
            String filePath,
            String jsonfilepath,
            JSONArray beneficiary) {
        int result = 0;
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call transaction.update_dbt_response_cash(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?::json)}");
            ps.setString(i++, batchId);
            ps.setString(i++, useridhash);
            ps.setInt(i++, total_beneficiary_count);
            ps.setString(i++, total_transaction_amount);
            ps.setString(i++, scheme_code);
            ps.setString(i++, scheme_name);
            ps.setString(i++, reference_date);
            ps.setString(i++, remarks);
            ps.setString(i++, success_beneficiary_count);
            ps.setString(i++, success_amount);
            ps.setString(i++, fail_beneficiary_count);
            ps.setString(i++, fail_amount);
            ps.setString(i++, filePath);
            ps.setString(i++, jsonfilepath);
            ps.setString(i++, beneficiary.toString());
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

    public static int updateCashDBTFileGeneration(String tabName, int schemeId, String finYr, int instl, String[] benids, String batchId, int esignApplId, int benCount, int officeId, int checkerOfficeId,
            String encFileName, String filePath, String userId, boolean jsonFileGenerated, Connection con) {
        int result = 0;
        //Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        try {
            //con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            Array dArray = con.createArrayOf("varchar", benids);
            String query = "";
            int i = 1;
            ps = con.prepareCall("{call transaction.update_dbt_cash_file_generation(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            ps.setString(i++, tabName);
            ps.setString(i++, batchId);
            ps.setInt(i++, schemeId);
            ps.setString(i++, finYr);
            ps.setInt(i++, instl);
            ps.setArray(i++, dArray);
            ps.setInt(i++, esignApplId);
            ps.setInt(i++, benCount);
            ps.setInt(i++, officeId);
            ps.setInt(i++, checkerOfficeId);
            ps.setString(i++, encFileName);
            ps.setString(i++, filePath);
            ps.setString(i++, userId);
            ps.setBoolean(i++, jsonFileGenerated);

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
//            try {
//                if (con != null) {
//                    con.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            };
        }

        return result;
    }

}
