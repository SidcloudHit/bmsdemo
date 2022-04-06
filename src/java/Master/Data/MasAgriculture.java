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
 * @author Nibedita
 */
public class MasAgriculture extends Master.MasAgriculture {

    String query;

    public MasAgriculture() {
    }

    public MasAgriculture(String ward_code, String fYear) {
        query = "SELECT * FROM agri_basic_report WHERE ward_code = '" + ward_code + "' AND fYear = '" + fYear + "'";
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                this.setDist_code(res.getString("dist_code"));
                this.setDist_name(res.getString("dist_name"));
                this.setSubdiv_code(res.getString("subdiv_code"));
                this.setSubdiv_name(res.getString("subdiv_name"));
                //this.setLocation_type(res.getString("location_type"));
                this.setBlock_code(res.getString("block_code"));
                this.setBlock_name(res.getString("block_name"));
                this.setWard_code(res.getString("ward_code"));
                this.setWard_name(res.getString("ward_name"));
                this.setFyear(res.getString("fyear"));
                this.setNofemale(res.getInt("nofemale"));
                this.setNomale(res.getInt("nomale"));
                this.setNosc(res.getInt("nosc"));
                this.setNost(res.getInt("nost"));
                this.setNoother(res.getInt("noother"));
                this.setNofarmer(res.getInt("nofarmer"));
                this.setNofamily(res.getInt("nofamily"));
                this.setKccsponsored(res.getInt("kccsponsored"));
                this.setKccsanction(res.getInt("kccsanction"));
                this.setFamlycover(res.getInt("famlycover"));
                this.setAmountdisbursed(res.getDouble("amountdisbursed"));
                this.setMarginalfarmerno(res.getInt("marginalfarmerno"));
                this.setMarginalfarmerarea(res.getDouble("marginalfarmerarea"));
                this.setSmallfarmerno(res.getInt("smallfarmerno"));
                this.setSmallfarmerarea(res.getDouble("smallfarmerarea"));
                this.setMediumfarmerno(res.getInt("mediumfarmerno"));
                this.setMediumfarmerarea(res.getDouble("mediumfarmerarea"));
                this.setLargefarmerno(res.getInt("largefarmerno"));
                this.setLargefarmerarea(res.getDouble("largefarmerarea"));
                this.setFarmfamilyno(res.getInt("farmfamilyno"));
                this.setNoagrilabour(res.getInt("noagrilabour"));
                this.setSharecroppers(res.getInt("sharecroppers"));
                this.setForestarea(res.getDouble("forestarea"));
                this.setSownarea(res.getDouble("sownarea"));
                this.setSinglecrop(res.getInt("singlecrop"));
                this.setDoublecrop(res.getInt("doublecrop"));
                this.setTriplecrop(res.getInt("triplecrop"));
                this.setWasteland(res.getDouble("wasteland"));
                this.setNonagriarea(res.getDouble("nonagriarea"));
                this.setBarrenarea(res.getDouble("barrenarea"));
                this.setFallowland(res.getDouble("fallowland"));
                this.setGrazingland(res.getDouble("grazingland"));
                this.setCropping_intensity(res.getDouble("cropping_intensity"));
                this.setNet_irrigated_area(res.getDouble("net_irrigated_area"));
                this.setFinal_stat(res.getBoolean("final_stat"));
                this.setEntry_by(res.getString("entry_by"));
                this.setEntry_time(res.getString("entry_time"));
                this.setLast_update_time(res.getString("last_update_time"));
                this.setFinal_time(res.getString("final_time"));

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

    public MasAgriculture(String label, String label_code, String fYear) {
        int nofemale = 0;
        int nomale = 0;
        int nosc = 0;
        int nost = 0;
        int noother = 0;
        int nofarmer = 0;
        int nofamily = 0;
        int kccsponsored = 0;
        int kccsanction = 0;
        int famlycover = 0;
        double amountdisbursed = 0;
        int marginalfarmerno = 0;
        Double marginalfarmerarea = 0.0;
        int smallfarmerno = 0;
        Double smallfarmerarea = 0.0;
        int mediumfarmerno = 0;
        Double mediumfarmerarea = 0.0;
        int largefarmerno = 0;
        Double largefarmerarea = 0.0;
        int farmfamilyno = 0;
        int noagrilabour = 0;
        int sharecroppers = 0;
        Double forestarea = 0.0;
        Double sownarea = 0.0;
        int singlecrop = 0;
        int doublecrop = 0;
        int triplecrop = 0;
        Double wasteland = 0.0;
        Double nonagriarea = 0.0;
        Double barrenarea = 0.0;
        Double fallowland = 0.0;
        Double grazingland = 0.0;
        boolean final_stat = true;

//        if (label.equals("b")) {
//            query = "SELECT * FROM agri_basic_report WHERE block_code = '" + label_code + "' AND fYear = '" + fYear + "'";
//        } else 
        if (label.equals("sd")) {
            query = "SELECT * FROM agri_basic_report WHERE subdiv_code = '" + label_code + "' AND fYear = '" + fYear + "'";
        } else if (label.equals("d")) {
            query = "SELECT * FROM agri_basic_report WHERE dist_code = '" + label_code + "' AND fYear = '" + fYear + "'";
        } else if (label.equals("s")) {
            query = "SELECT * FROM agri_basic_report WHERE fYear = '" + fYear + "'";
        }

        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                this.setDist_code(res.getString("dist_code"));
                this.setDist_name(res.getString("dist_name"));
                this.setSubdiv_code(res.getString("subdiv_code"));
                this.setSubdiv_name(res.getString("subdiv_name"));
                //this.setLocation_type(res.getString("location_type"));
                //this.setBlock_code(res.getString("block_code"));
                //this.setBlock_name(res.getString("block_name"));
                this.setWard_code(res.getString("ward_code"));
                this.setWard_name(res.getString("ward_name"));
                this.setFyear(res.getString("fyear"));
//                this.setEntry_by(res.getString("entry_by"));
//                this.setEntry_time(res.getString("entry_time"));
//                this.setLast_update_time(res.getString("last_update_time"));
//                this.setFinal_time(res.getString("final_time"));

                final_stat &= res.getBoolean("final_stat");
                nofemale += res.getInt("nofemale");
                nomale += res.getInt("nomale");
                nosc += res.getInt("nosc");
                nost += res.getInt("nost");
                noother += res.getInt("noother");
                nofarmer += res.getInt("nofarmer");
                nofamily += res.getInt("nofamily");
                kccsponsored += res.getInt("kccsponsored");
                kccsanction += res.getInt("kccsanction");
                famlycover += res.getInt("famlycover");
                amountdisbursed += res.getDouble("amountdisbursed");
                marginalfarmerno += res.getInt("marginalfarmerno");
                marginalfarmerarea += res.getDouble("marginalfarmerarea");
                smallfarmerno += res.getInt("smallfarmerno");
                smallfarmerarea += res.getDouble("smallfarmerarea");
                mediumfarmerno += res.getInt("mediumfarmerno");
                mediumfarmerarea += res.getDouble("mediumfarmerarea");
                largefarmerno += res.getInt("largefarmerno");
                largefarmerarea += res.getDouble("largefarmerarea");
                farmfamilyno += res.getInt("farmfamilyno");
                noagrilabour += res.getInt("noagrilabour");
                sharecroppers += res.getInt("sharecroppers");
                forestarea += res.getDouble("forestarea");
                sownarea += res.getDouble("sownarea");
                singlecrop += res.getInt("singlecrop");
                doublecrop += res.getInt("doublecrop");
                triplecrop += res.getInt("triplecrop");
                wasteland += res.getDouble("wasteland");
                nonagriarea += res.getDouble("nonagriarea");
                barrenarea += res.getDouble("barrenarea");
                fallowland += res.getDouble("fallowland");
                grazingland += res.getDouble("grazingland");

            }

            this.setNofemale(nofemale);
            this.setNomale(nomale);
            this.setNosc(nosc);
            this.setNost(nost);
            this.setNoother(noother);
            this.setNofarmer(nofarmer);
            this.setNofamily(nofamily);
            this.setKccsponsored(kccsponsored);
            this.setKccsanction(kccsanction);
            this.setFamlycover(famlycover);
            this.setAmountdisbursed(amountdisbursed);
            this.setMarginalfarmerno(marginalfarmerno);
            this.setMarginalfarmerarea(marginalfarmerarea);
            this.setSmallfarmerno(smallfarmerno);
            this.setSmallfarmerarea(smallfarmerarea);
            this.setMediumfarmerno(mediumfarmerno);
            this.setMediumfarmerarea(mediumfarmerarea);
            this.setLargefarmerno(largefarmerno);
            this.setLargefarmerarea(largefarmerarea);
            this.setFarmfamilyno(farmfamilyno);
            this.setNoagrilabour(noagrilabour);
            this.setSharecroppers(sharecroppers);
            this.setForestarea(forestarea);
            this.setSownarea(sownarea);
            this.setSinglecrop(singlecrop);
            this.setDoublecrop(doublecrop);
            this.setTriplecrop(triplecrop);
            this.setWasteland(wasteland);
            this.setNonagriarea(nonagriarea);
            this.setBarrenarea(barrenarea);
            this.setFallowland(fallowland);
            this.setGrazingland(grazingland);
            this.setFinal_stat(final_stat);
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
