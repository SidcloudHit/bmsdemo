/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esign;

import Common.CommonMethod;
import Master.Data.AdminUser;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NIC-Arindam
 */
public class EsignMethods {

    public int saveEsignApplicationInitiation(int office_id, int scheme_id, String financial_year, int installment, int final_beneficiary_count, int pdf_report_fid, String uId, int roleId, double final_dbt_amount, int entryOfficeId) {
        int applId = 0, i = 1;
        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        query = "INSERT INTO esign.dbt_esign_application_details(office_id, scheme_id, financial_year, installment, final_beneficiary_count, pdf_report_fid, "
                + "application_status, entry_by, role_id, final_dbt_amount, entry_office_id)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); //
            ps.setInt(i++, office_id);
            ps.setInt(i++, scheme_id);
            ps.setString(i++, financial_year);
            ps.setInt(i++, installment);
            ps.setInt(i++, final_beneficiary_count);
            ps.setInt(i++, pdf_report_fid);
            ps.setInt(i++, 1);
            ps.setString(i++, uId);
            ps.setInt(i++, roleId);
            ps.setDouble(i++, final_dbt_amount);
            ps.setInt(i++, entryOfficeId);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                applId = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return applId;
    }

    public int updateTranEsignApplId(int application_id, String uId, int roleId, String[] benids) {
        int result = 0, i = 1;
        String query = "";
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
//        AdminUser usr = new AdminUser(uId);
        Array dArray = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            if (benids != null) {
                dArray = con.createArrayOf("varchar", benids);
            }

            ps = con.prepareCall("{call dbt_cash_set_sign_appl_id(?,?,?,?)}");
            ps.setInt(i++, application_id);
            ps.setInt(i++, roleId);
            ps.setString(i++, uId);
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

    public int updateDirectTranEsignApplId(int application_id, String uId, int roleId, String[] benids) {
        int result = 0, i = 1;
        String query = "";
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
//        AdminUser usr = new AdminUser(uId);
        Array dArray = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            if (benids != null) {
                dArray = con.createArrayOf("varchar", benids);
            }

            ps = con.prepareCall("{call dbt_cash_direct_set_sign_appl_id(?,?,?,?)}");
            ps.setInt(i++, application_id);
            ps.setInt(i++, roleId);
            ps.setString(i++, uId);
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

    public String getRequesterId(int applicationId, int reqFor) {
        String result = "", esignApplTabName = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";

        if (reqFor == 1) {
            esignApplTabName = "esign.dbt_esign_application_details";
            query = "SELECT entry_by,scheme_id,role_id,entry_office_id  FROM " + esignApplTabName + " WHERE application_id = " + applicationId;
        } else if (reqFor == 2) {
            esignApplTabName = "esign.beneficiary_esign_application_details";
            query = "SELECT entry_by,scheme_id,role_id,office_id  FROM " + esignApplTabName + " WHERE application_id = " + applicationId;
        }

        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1) + "#" + rs.getString(2) + "#" + rs.getString(3) + "#" + rs.getString(4);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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

    public int updateEsignApplicationInitiation(int application_id, int pdf_signed_report_fid, String uId, int reqFor) {
        int status = 0, i = 1;
        String esignApplTabName = "";
        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (reqFor == 1) {
            esignApplTabName = "esign.dbt_esign_application_details";
        } else if (reqFor == 2) {
            esignApplTabName = "esign.beneficiary_esign_application_details";
        }

        query = "UPDATE " + esignApplTabName + " "
                + "SET pdf_signed_report_fid=?, application_status=?, lastupdated_by=?, "
                + "lastupdated_time= now() "
                + "WHERE application_id=?";

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            ps = con.prepareStatement(query); //
            ps.setInt(i++, pdf_signed_report_fid);
            ps.setInt(i++, 2);
            ps.setString(i++, uId);
            ps.setInt(i++, application_id);
            int temp = ps.executeUpdate();

            if (temp > 0) {
                status = 2;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

//    public int updateTranEsignApplId(String tabName, int scheme_id, String financial_year, int installment, int application_id, String uId) {
//        int status = 0, i = 1;
//        String query = "";
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        AdminUser usr = new AdminUser(uId);
//        CommonMethod cm = new CommonMethod();
//        String roleLevel = cm.getRoleLevel(usr.getUser_role());
//
//        if (roleLevel.equals("Maker")) {
//            query = "UPDATE " + tabName + " SET initiator_esign_appl_id=? WHERE scheme_id=? AND financial_year=? AND installment=? AND freezed_by=?";
//        } else if (roleLevel.equals("Checker")) {
//            query = "UPDATE " + tabName + " SET verifier_esign_appl_id=? WHERE scheme_id=? AND financial_year=? AND installment=? AND verified_by=?";
//        } else if (roleLevel.equals("Approver")) {
//            query = "UPDATE " + tabName + " SET approver_esign_appl_id=? WHERE scheme_id=? AND financial_year=? AND installment=? AND approved_by=?";
//        }
//
//        try {
//            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
//            ps = con.prepareStatement(query);
//            ps.setInt(i++, application_id);
//            ps.setInt(i++, scheme_id);
//            ps.setString(i++, financial_year);
//            ps.setInt(i++, installment);
//            ps.setString(i++, uId);
//            status = ps.executeUpdate();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return status;
//    }
    public String checkIfFreezedFileExists(int office_id, int scheme_id, String financial_year, int installment, String uId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT application_id, pdf_report_fid FROM esign.dbt_esign_application_details WHERE office_id = ? AND scheme_id = ? AND financial_year = ? AND installment = ? AND pdf_report_fid > 0");
            ps.setInt(1, office_id);
            ps.setInt(2, scheme_id);
            ps.setString(3, financial_year);
            ps.setInt(4, installment);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("application_id") + "#" + rs.getInt("pdf_report_fid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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

    public String checkIfSignedFileExists(int office_id, int scheme_id, String financial_year, int installment, String uId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT application_id, pdf_signed_report_fid FROM esign.dbt_esign_application_details WHERE office_id = ? AND scheme_id = ? AND financial_year = ? AND installment = ? AND pdf_signed_report_fid > 0");
            ps.setInt(1, office_id);
            ps.setInt(2, scheme_id);
            ps.setString(3, financial_year);
            ps.setInt(4, installment);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("application_id") + "#" + rs.getInt("pdf_signed_report_fid");

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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

    public int saveBenEsignApplicationInitiation(int office_id, int scheme_id, int final_beneficiary_count, int pdf_report_fid, String uId, int roleId) {
        int applId = 0, i = 1;
        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        query = "INSERT INTO esign.beneficiary_esign_application_details( office_id, scheme_id, final_beneficiary_count, \n"
                + " pdf_report_fid, application_status, entry_by, role_id)\n"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        ;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); //
            ps.setInt(i++, office_id);
            ps.setInt(i++, scheme_id);
            ps.setInt(i++, final_beneficiary_count);
            ps.setInt(i++, pdf_report_fid);
            ps.setInt(i++, 1);
            ps.setString(i++, uId);
            ps.setInt(i++, roleId);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                applId = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return applId;
    }

    public int updateBenEsignApplId(String tabName, int schemeId, int application_id, String uId, int roleId, String[] benids) {
        int result = 0, i = 1;
        String query = "";
        Connection con = null;
        CallableStatement ps = null;
        ResultSet rs = null;
//        AdminUser usr = new AdminUser(uId);
        Array dArray = null;

        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            if (benids != null) {
                dArray = con.createArrayOf("varchar", benids);
            }

            ps = con.prepareCall("{call beneficiary.dbt_beneficiary_set_sign_appl_id(?, ?, ?, ?, ?, ?)}");
            ps.setString(i++, tabName);
            ps.setInt(i++, application_id);
            ps.setInt(i++, schemeId);
            ps.setInt(i++, roleId);
            ps.setString(i++, uId);
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

    public String checkIfFreezedBenFileExists(int office_id, int scheme_id, String uId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT application_id, pdf_report_fid FROM esign.beneficiary_esign_application_details WHERE office_id = ? AND scheme_id = ? AND pdf_report_fid > 0 AND application_status = 1");
            ps.setInt(1, office_id);
            ps.setInt(2, scheme_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("application_id") + "#" + rs.getInt("pdf_report_fid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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

    public String checkIfSignedBenFileExists(int office_id, int scheme_id, String uId) {
        String result = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        try {
            ps = con.prepareStatement("SELECT application_id, pdf_signed_report_fid FROM esign.beneficiary_esign_application_details WHERE office_id = ? AND scheme_id = ? AND pdf_signed_report_fid > 0 AND application_status = 2");
            ps.setInt(1, office_id);
            ps.setInt(2, scheme_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("application_id") + "#" + rs.getInt("pdf_signed_report_fid");

            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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

    public String[] getRequestedBenList(String tabName, int roleId, int applId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> option_name = new ArrayList<String>();
        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
        String sql = "";

        if (roleId == 4) { //Approver
            sql = "SELECT beneficiary_id FROM beneficiary." + tabName + " WHERE approver_esign_appl_id = " + applId;
        } else if (roleId == 5) { //Checker
            sql = "SELECT beneficiary_id FROM beneficiary." + tabName + " WHERE verifier_esign_appl_id = " + applId;
        } else if (roleId == 6) { //Maker
            sql = "SELECT beneficiary_id FROM beneficiary." + tabName + " WHERE initiator_esign_appl_id = " + applId;
        }

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                option_name.add(rs.getString("beneficiary_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
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
        String[] nameArr = new String[option_name.size()];
        nameArr = option_name.toArray(nameArr);

        return nameArr;

    }

}
