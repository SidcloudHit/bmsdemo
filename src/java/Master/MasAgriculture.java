/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package Master;

/**
 *
 * @author Nibedita
 */
public class MasAgriculture {

    private String dist_code;
    private String dist_name;
    private String subdiv_code;
    private String subdiv_name;
    //private String location_type;
    private String block_code;
    private String block_name;
    private String ward_code;
    private String ward_name;
    private String fyear;
    private int nofemale;
    private int nomale;
    private int nosc;
    private int nost;
    private int noother;
    private int nofarmer;
    private int nofamily;
    private int kccsponsored;
    private int kccsanction;
    private int famlycover;
    private double amountdisbursed;
    private int marginalfarmerno;
    private double marginalfarmerarea;
    private int smallfarmerno;
    private double smallfarmerarea;
    private int mediumfarmerno;
    private double mediumfarmerarea;
    private int largefarmerno;
    private double largefarmerarea;
    private int farmfamilyno;
    private int noagrilabour;
    private int sharecroppers;
    private double forestarea;
    private double sownarea;
    private int singlecrop;
    private int doublecrop;
    private int triplecrop;
    private double wasteland;
    private double nonagriarea;
    private double barrenarea;
    private double fallowland;
    private double grazingland;
    private double cropping_intensity;
    private double net_irrigated_area;
    private boolean final_stat;
    private String entry_by;
    private String entry_time;
    private String last_update_time;
    private String final_time;

    public String getFyear() {
        return fyear;
    }

    public void setFyear(String fyear) {
        this.fyear = fyear;
    }

    public int getNofemale() {
        return nofemale;
    }

    public void setNofemale(int nofemale) {
        this.nofemale = nofemale;
    }

    public int getNomale() {
        return nomale;
    }

    public void setNomale(int nomale) {
        this.nomale = nomale;
    }

    public int getNosc() {
        return nosc;
    }

    public void setNosc(int nosc) {
        this.nosc = nosc;
    }

    public int getNost() {
        return nost;
    }

    public void setNost(int nost) {
        this.nost = nost;
    }

    public int getNoother() {
        return noother;
    }

    public void setNoother(int noother) {
        this.noother = noother;
    }

    public int getNofarmer() {
        return nofarmer;
    }

    public void setNofarmer(int nofarmer) {
        this.nofarmer = nofarmer;
    }

    public int getNofamily() {
        return nofamily;
    }

    public void setNofamily(int nofamily) {
        this.nofamily = nofamily;
    }

    public int getKccsponsored() {
        return kccsponsored;
    }

    public int getFamlycover() {
        return famlycover;
    }

    public void setFamlycover(int famlycover) {
        this.famlycover = famlycover;
    }

    public double getAmountdisbursed() {
        return amountdisbursed;
    }

    public void setAmountdisbursed(double amountdisbursed) {
        this.amountdisbursed = amountdisbursed;
    }

    public int getMarginalfarmerno() {
        return marginalfarmerno;
    }

    public void setMarginalfarmerno(int marginalfarmerno) {
        this.marginalfarmerno = marginalfarmerno;
    }

    public Double getMarginalfarmerarea() {
        return marginalfarmerarea;
    }

    public void setMarginalfarmerarea(Double marginalfarmerarea) {
        this.setMarginalfarmerarea((double) marginalfarmerarea);
    }

    public int getSmallfarmerno() {
        return smallfarmerno;
    }

    public void setSmallfarmerno(int smallfarmerno) {
        this.smallfarmerno = smallfarmerno;
    }

    public Double getSmallfarmerarea() {
        return smallfarmerarea;
    }

    public void setSmallfarmerarea(Double smallfarmerarea) {
        this.setSmallfarmerarea((double) smallfarmerarea);
    }

    public int getMediumfarmerno() {
        return mediumfarmerno;
    }

    public void setMediumfarmerno(int mediumfarmerno) {
        this.mediumfarmerno = mediumfarmerno;
    }

    public Double getMediumfarmerarea() {
        return mediumfarmerarea;
    }

    public void setMediumfarmerarea(Double mediumfarmerarea) {
        this.setMediumfarmerarea((double) mediumfarmerarea);
    }

    public int getLargefarmerno() {
        return largefarmerno;
    }

    public void setLargefarmerno(int largefarmerno) {
        this.largefarmerno = largefarmerno;
    }

    public Double getLargefarmerarea() {
        return largefarmerarea;
    }

    public void setLargefarmerarea(Double largefarmerarea) {
        this.setLargefarmerarea((double) largefarmerarea);
    }

    public int getFarmfamilyno() {
        return farmfamilyno;
    }

    public void setFarmfamilyno(int farmfamilyno) {
        this.farmfamilyno = farmfamilyno;
    }

    public int getNoagrilabour() {
        return noagrilabour;
    }

    public void setNoagrilabour(int noagrilabour) {
        this.noagrilabour = noagrilabour;
    }

    public int getSharecroppers() {
        return sharecroppers;
    }

    public void setSharecroppers(int sharecroppers) {
        this.sharecroppers = sharecroppers;
    }

    public Double getForestarea() {
        return forestarea;
    }

    public void setForestarea(Double forestarea) {
        this.setForestarea((double) forestarea);
    }

    public Double getSownarea() {
        return sownarea;
    }

    public void setSownarea(Double sownarea) {
        this.setSownarea((double) sownarea);
    }

    public int getSinglecrop() {
        return singlecrop;
    }

    public void setSinglecrop(int singlecrop) {
        this.singlecrop = singlecrop;
    }

    public int getDoublecrop() {
        return doublecrop;
    }

    public void setDoublecrop(int doublecrop) {
        this.doublecrop = doublecrop;
    }

    public int getTriplecrop() {
        return triplecrop;
    }

    public void setTriplecrop(int triplecrop) {
        this.triplecrop = triplecrop;
    }

    public Double getWasteland() {
        return wasteland;
    }

    public void setWasteland(Double wasteland) {
        this.setWasteland((double) wasteland);
    }

    public Double getNonagriarea() {
        return nonagriarea;
    }

    public void setNonagriarea(Double nonagriarea) {
        this.setNonagriarea((double) nonagriarea);
    }

    public Double getBarrenarea() {
        return barrenarea;
    }

    public void setBarrenarea(Double barrenarea) {
        this.setBarrenarea((double) barrenarea);
    }

    public Double getFallowland() {
        return fallowland;
    }

    public void setFallowland(Double fallowland) {
        this.setFallowland((double) fallowland);
    }

    public Double getGrazingland() {
        return grazingland;
    }

    public void setGrazingland(Double grazingland) {
        this.setGrazingland((double) grazingland);
    }

    /**
     * @return the dist_code
     */
    public String getDist_code() {
        return dist_code;
    }

    /**
     * @param dist_code the dist_code to set
     */
    public void setDist_code(String dist_code) {
        this.dist_code = dist_code;
    }

    /**
     * @return the dist_name
     */
    public String getDist_name() {
        return dist_name;
    }

    /**
     * @param dist_name the dist_name to set
     */
    public void setDist_name(String dist_name) {
        this.dist_name = dist_name;
    }

    /**
     * @return the subdiv_code
     */
    public String getSubdiv_code() {
        return subdiv_code;
    }

    /**
     * @param subdiv_code the subdiv_code to set
     */
    public void setSubdiv_code(String subdiv_code) {
        this.subdiv_code = subdiv_code;
    }

    /**
     * @return the subdiv_name
     */
    public String getSubdiv_name() {
        return subdiv_name;
    }

    /**
     * @param subdiv_name the subdiv_name to set
     */
    public void setSubdiv_name(String subdiv_name) {
        this.subdiv_name = subdiv_name;
    }

    /**
     * @return the block_code
     */
    public String getBlock_code() {
        return block_code;
    }

    /**
     * @param block_code the block_code to set
     */
    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    /**
     * @return the block_name
     */
    public String getBlock_name() {
        return block_name;
    }

    /**
     * @param block_name the block_name to set
     */
    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    /**
     * @return the ward_code
     */
    public String getWard_code() {
        return ward_code;
    }

    /**
     * @param ward_code the ward_code to set
     */
    public void setWard_code(String ward_code) {
        this.ward_code = ward_code;
    }

    /**
     * @return the ward_name
     */
    public String getWard_name() {
        return ward_name;
    }

    /**
     * @param ward_name the ward_name to set
     */
    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    /**
     * @param marginalfarmerarea the marginalfarmerarea to set
     */
    public void setMarginalfarmerarea(double marginalfarmerarea) {
        this.marginalfarmerarea = marginalfarmerarea;
    }

    /**
     * @param smallfarmerarea the smallfarmerarea to set
     */
    public void setSmallfarmerarea(double smallfarmerarea) {
        this.smallfarmerarea = smallfarmerarea;
    }

    /**
     * @param mediumfarmerarea the mediumfarmerarea to set
     */
    public void setMediumfarmerarea(double mediumfarmerarea) {
        this.mediumfarmerarea = mediumfarmerarea;
    }

    /**
     * @param largefarmerarea the largefarmerarea to set
     */
    public void setLargefarmerarea(double largefarmerarea) {
        this.largefarmerarea = largefarmerarea;
    }

    /**
     * @param forestarea the forestarea to set
     */
    public void setForestarea(double forestarea) {
        this.forestarea = forestarea;
    }

    /**
     * @param sownarea the sownarea to set
     */
    public void setSownarea(double sownarea) {
        this.sownarea = sownarea;
    }

    /**
     * @param wasteland the wasteland to set
     */
    public void setWasteland(double wasteland) {
        this.wasteland = wasteland;
    }

    /**
     * @param nonagriarea the nonagriarea to set
     */
    public void setNonagriarea(double nonagriarea) {
        this.nonagriarea = nonagriarea;
    }

    /**
     * @param barrenarea the barrenarea to set
     */
    public void setBarrenarea(double barrenarea) {
        this.barrenarea = barrenarea;
    }

    /**
     * @param fallowland the fallowland to set
     */
    public void setFallowland(double fallowland) {
        this.fallowland = fallowland;
    }

    /**
     * @param grazingland the grazingland to set
     */
    public void setGrazingland(double grazingland) {
        this.grazingland = grazingland;
    }

    /**
     * @return the cropping_intensity
     */
    public double getCropping_intensity() {
        return cropping_intensity;
    }

    /**
     * @param cropping_intensity the cropping_intensity to set
     */
    public void setCropping_intensity(double cropping_intensity) {
        this.cropping_intensity = cropping_intensity;
    }

    /**
     * @return the net_irrigated_area
     */
    public double getNet_irrigated_area() {
        return net_irrigated_area;
    }

    /**
     * @param net_irrigated_area the net_irrigated_area to set
     */
    public void setNet_irrigated_area(double net_irrigated_area) {
        this.net_irrigated_area = net_irrigated_area;
    }

    /**
     * @param kccsponsored the kccsponsored to set
     */
    public void setKccsponsored(int kccsponsored) {
        this.kccsponsored = kccsponsored;
    }

    /**
     * @return the kccsanction
     */
    public int getKccsanction() {
        return kccsanction;
    }

    /**
     * @param kccsanction the kccsanction to set
     */
    public void setKccsanction(int kccsanction) {
        this.kccsanction = kccsanction;
    }

    /**
     * @return the final_stat
     */
    public boolean isFinal_stat() {
        return final_stat;
    }

    /**
     * @param final_stat the final_stat to set
     */
    public void setFinal_stat(boolean final_stat) {
        this.final_stat = final_stat;
    }

    /**
     * @return the entry_by
     */
    public String getEntry_by() {
        return entry_by;
    }

    /**
     * @param entry_by the entry_by to set
     */
    public void setEntry_by(String entry_by) {
        this.entry_by = entry_by;
    }

    /**
     * @return the entry_time
     */
    public String getEntry_time() {
        return entry_time;
    }

    /**
     * @param entry_time the entry_time to set
     */
    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    /**
     * @return the last_update_time
     */
    public String getLast_update_time() {
        return last_update_time;
    }

    /**
     * @param last_update_time the last_update_time to set
     */
    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    /**
     * @return the final_time
     */
    public String getFinal_time() {
        return final_time;
    }

    /**
     * @param final_time the final_time to set
     */
    public void setFinal_time(String final_time) {
        this.final_time = final_time;
    }

}
